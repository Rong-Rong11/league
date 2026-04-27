package process.service.finance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import process.repository.TeamRepository;
import process.service.finance.distribution.central.CentralRevenueDistributor;
import process.service.finance.expense.LeagueExpenseCalculator;
import process.service.finance.game.processor.PlayoffGameFinanceProcessor;
import process.service.finance.game.processor.RegularSeasonGameFinanceProcessor;
import process.service.finance.initialization.FinanceInitializer;
import process.service.finance.playoff.PlayoffFinancialRules;
import process.service.finance.team.PlayoffMonthlyTeamFinanceCalculator;
import process.service.finance.team.RegularSeasonMonthlyTeamFinanceCalculator;
import process.utility.FinanceUtility;
import process.utility.TeamUtility;
import process.visitor.financialpolicy.ChooseTransferStrategyVisitor;
import log.LoggerUtility;

public class FinanceManager {
	private static final Logger logger = LoggerUtility.getLogger(FinanceManager.class, "text");
	private League league;
	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final FinanceInitializer financeInitializer = new FinanceInitializer();

	private final RevenueSharingManager revenueSharingManager;
	private final RegularSeasonMonthlyTeamFinanceCalculator regularSeasonMonthlyFinanceCalculator;
	private final PlayoffMonthlyTeamFinanceCalculator playoffMonthlyFinanceCalculator;
	private final CentralRevenueDistributor centralRevenueDistributor;
	private final LeagueExpenseCalculator leagueExpenseCalculator;

	private final RegularSeasonGameFinanceProcessor regularSeasonGameProcessor;
	private final HashMap<PlayoffRound, PlayoffGameFinanceProcessor> playoffGameProcessorsByRound = new HashMap<PlayoffRound, PlayoffGameFinanceProcessor>();

	public FinanceManager(League league) {
		this.league = league;
		if (league == null) {
			logger.warn("Finance manager initialized with null league");
		}
		revenueSharingManager = new RevenueSharingManager(league);
		regularSeasonMonthlyFinanceCalculator = new RegularSeasonMonthlyTeamFinanceCalculator(league);
		playoffMonthlyFinanceCalculator = new PlayoffMonthlyTeamFinanceCalculator(league);
		centralRevenueDistributor = new CentralRevenueDistributor(league);
		leagueExpenseCalculator = new LeagueExpenseCalculator(league);

		regularSeasonGameProcessor = new RegularSeasonGameFinanceProcessor(league);
		centralRevenueDistributor.setFinanceManager(this);
		leagueExpenseCalculator.setFinanceManager(this);
	}

	// Initialization
	public void initializeFinance() {
		logger.info("Initializing finance data");
		financeInitializer.initializeFinance();
		logger.info("Finance data initialized");
	}

	// Monthly simulation
	public void applyMonthlyFinance(int month) {
		logger.debug("Applying regular season monthly finance for month " + month);
		applyRegularSeasonMonthlyFinanceToAllTeams(month);
		distributeCentralRevenue(month);
		applyLeagueExpenses(month);
		applyRevenueSharing(month);
		logger.debug("Regular season monthly finance applied for month " + month);
	}

	public void applyPlayoffMonthlyFinance(int month, ArrayList<Team> activePlayoffTeams) {
		if (activePlayoffTeams == null) {
			logger.warn("Applying playoff monthly finance with null active playoff teams list");
			activePlayoffTeams = new ArrayList<Team>();
		}
		logger.debug("Applying playoff monthly finance for month " + month + " with " + activePlayoffTeams.size()
				+ " active playoff teams");
		for (Team team : teamRepository.getAllTeams()) {
			if (activePlayoffTeams.contains(team)) {
				applyPlayoffMonthlyFinanceToTeam(team, month);
				continue;
			}

			applyInactivePlayoffMonthlyFinanceToTeam(team, month);
		}
		distributeCentralRevenue(month);
		applyLeagueExpenses(month);
		logger.debug("Playoff monthly finance applied for month " + month);
	}

	private void distributeCentralRevenue(int month) {
		logger.trace("Distributing central revenue for month " + month);
		centralRevenueDistributor.distributeMonthlyCentralRevenue(month);
	}

	private void applyRevenueSharing(int month) {
		logger.trace("Applying revenue sharing for month " + month);
		revenueSharingManager.applyRevenueSharing(month);
	}

	private void applyLeagueExpenses(int month) {
		logger.trace("Applying league expenses for month " + month);
		leagueExpenseCalculator.applyMonthlyExpenses(month);
	}

	// Game finance
	public void calculateRegularSeasonGame(Game game, LocalDate date, int month) {
		if (game == null || date == null) {
			logger.warn("Skipping regular season game finance calculation because game or date is null");
			return;
		}
		logger.debug("Calculating regular season game finance for " + date + " month " + month);
		regularSeasonGameProcessor.calculateGame(game, date, month);
	}

	public void calculatePlayoffGame(Game game, LocalDate date, int month, PlayoffRound round) {
		if (game == null || date == null || round == null) {
			logger.warn("Skipping playoff game finance calculation because game, date or round is null");
			return;
		}
		logger.debug("Calculating playoff game finance for round " + round + " at " + date + " month " + month);
		PlayoffGameFinanceProcessor playoffGameFinanceProcessor = getOrCreatePlayoffGameProcessor(round);
		playoffGameFinanceProcessor.calculateGame(game, date, month);
	}

	private PlayoffGameFinanceProcessor getOrCreatePlayoffGameProcessor(PlayoffRound round) {
		PlayoffGameFinanceProcessor playoffGameProcessor = playoffGameProcessorsByRound.get(round);
		if (playoffGameProcessor == null) {
			logger.debug("Creating playoff game finance processor for round " + round);
			playoffGameProcessor = new PlayoffGameFinanceProcessor(league, round);
			playoffGameProcessorsByRound.put(round, playoffGameProcessor);
		} else {
			logger.trace("Reusing playoff game finance processor for round " + round);
		}
		return playoffGameProcessor;
	}

	private void applyRegularSeasonMonthlyFinanceToTeam(Team team, int month) {
		if (team == null) {
			logger.warn("Skipping regular season monthly finance because team is null");
			return;
		}
		logger.trace("Applying regular season monthly finance to " + team.getName() + " month " + month);
		regularSeasonMonthlyFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyPlayoffFixedCostsToTeam(Team team, int month) {
		if (team == null) {
			logger.warn("Skipping playoff fixed costs because team is null");
			return;
		}
		logger.trace("Applying playoff fixed costs to " + team.getName() + " month " + month);
		playoffMonthlyFinanceCalculator.applyMonthlyFixedCosts(team, month);
	}

	private void applyInactivePlayoffMonthlyFinanceToTeam(Team team, int month) {
		if (team == null) {
			logger.warn("Skipping inactive playoff monthly finance because team is null");
			return;
		}
		logger.trace("Applying inactive playoff monthly finance to " + team.getName() + " month " + month);
		regularSeasonMonthlyFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyPlayoffMonthlyFinanceToTeam(Team team, int month) {
		if (team == null) {
			logger.warn("Skipping playoff monthly finance because team is null");
			return;
		}
		logger.trace("Applying playoff monthly finance to " + team.getName() + " month " + month);
		playoffMonthlyFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyRegularSeasonMonthlyFinanceToAllTeams(int month) {
		int appliedTeams = 0;
		for (Team team : teamRepository.getAllTeams()) {
			applyRegularSeasonMonthlyFinanceToTeam(team, month);
			if (team != null) {
				appliedTeams++;
			}
		}
		logger.debug("Regular season monthly finance applied to " + appliedTeams + " teams for month " + month);
	}

	// getters
	public GameStat getGameStat(Game game) {
		if (game == null) {
			logger.warn("Unable to get game stat because game is null");
			return null;
		}
		GameStat gameStat = regularSeasonGameProcessor.getGameStat(game);

		if (gameStat != null) {
			logger.trace("Found regular season game stat");
			return gameStat;
		}

		for (PlayoffGameFinanceProcessor playoffGameFinanceProcessor : playoffGameProcessorsByRound.values()) {
			gameStat = playoffGameFinanceProcessor.getGameStat(game);

			if (gameStat != null) {
				logger.trace("Found playoff game stat");
				return gameStat;
			}
		}

		logger.trace("No game stat found");
		return null;
	}

	// Team finance setup
	public void randomFinancialPolicy() {
		logger.debug("Randomizing financial policy for all teams");
		for (Team team : teamRepository.getAllTeams()) {
			FinancialPolicy financialPolicy = TeamUtility.randomFinancialPolicy();
			chooseFinancialPolicy(team, financialPolicy);
		}
		logger.debug("Financial policy randomized for all teams");
	}

	public void chooseFinancialPolicy(Team team, FinancialPolicy financialPolicy) {
		if (team == null || team.getTeamFinance() == null || financialPolicy == null) {
			logger.warn("Skipping financial policy choice because team, team finance or policy is null");
			return;
		}
		logger.debug("Choosing financial policy "
				+ financialPolicy.getClass().getSimpleName()
				+ " for "
				+ team.getName());
		team.getTeamFinance().getBehavior().setFinancialPolicy(financialPolicy);
		team.getTeamFinance().getBehavior()
				.setTeamTransferStrategy(financialPolicy.accept(new ChooseTransferStrategyVisitor(team.getRival())));
	}

	public void chooseMarketSize(Team team, MarketSize marketSize) {
		if (team == null || team.getTeamFinance() == null || marketSize == null) {
			logger.warn("Skipping market size choice because team, team finance or market size is null");
			return;
		}
		logger.debug("Choosing market size " + marketSize.getClass().getSimpleName() + " for " + team.getName());
		team.getTeamFinance().getStructure().setMarketSize(marketSize);
	}

	public void randomMarketSize() {
		logger.debug("Randomizing market size for all teams");
		for (Team team : teamRepository.getAllTeams()) {
			MarketSize marketSize = TeamUtility.randomMarketSize();
			chooseMarketSize(team, marketSize);
		}
		logger.debug("Market size randomized for all teams");
	}

	public double getTeamCurrentPayroll(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Returning 0 payroll because team or team finance is null");
			return 0.0;
		}
		return team.getTeamFinance().getCurrentPayroll();
	}

	// Playoff bonuses
	public void applyPlayoffQualificationBonus(Team team, int month) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Skipping playoff qualification bonus because team or team finance is null");
			return;
		}
		logger.debug("Applying playoff qualification bonus to " + team.getName() + " for month " + month);
		double bonus = calculatePlayoffQualificationBonus(team);
		logger.trace("Calculated playoff qualification bonus " + bonus + " for " + team.getName());

		FinanceUtility.addIncome(
				team.getTeamFinance().getBudget(),
				new Income(IncomeType.PLAYOFF_QUALIFICATION_BONUS, bonus),
				month);

		FinanceUtility.updateBudget(team.getTeamFinance().getBudget());
	}

	private double calculatePlayoffQualificationBonus(Team team) {
		double bonus = 7;

		double performance = team.getTeamPerformance().getPerformanceRating();
		double popularity = team.getCurrentPopularity() / 100.0;
		double marketExposure = team.getTeamFinance().getStructure().getMediaMarket().getBusinessOpportunityModifier();
		double prestige = team.getTeamFinance().getStructure().getEconomicProfile().getHistoricalPrestige();

		bonus += performance * 0.9;
		bonus += popularity * 0.8;
		bonus += marketExposure * 0.5;
		bonus += prestige * 0.4;

		if (team.hasStarPlayer()) {
			bonus += 0.5;
		}

		return bonus;
	}

	public void applyPlayoffQualificationBonus(ArrayList<Team> teams, int month) {
		if (teams == null) {
			logger.warn("Skipping playoff qualification bonuses because teams list is null");
			return;
		}
		logger.debug("Applying playoff qualification bonuses to " + teams.size() + " teams for month " + month);
		for (Team team : teams) {
			applyPlayoffQualificationBonus(team, month);
		}
	}

	public void applyPlayoffRoundBonus(Team team, int month, PlayoffRound round) {
		if (team == null || team.getTeamFinance() == null || round == null) {
			logger.warn("Skipping playoff round bonus because team, team finance or round is null");
			return;
		}
		PlayoffFinancialRules playoffFinancialRules = new PlayoffFinancialRules(round);
		double bonus = playoffFinancialRules.getRoundQualificationBonus();

		if (bonus <= 0) {
			logger.warn("Ignoring playoff round bonus because computed bonus is non-positive for round " + round);
			return;
		}
		logger.debug("Applying playoff round bonus to " + team.getName() + " for round " + round + " month " + month);

		FinanceUtility.addIncome(
				team.getTeamFinance().getBudget(),
				new Income(IncomeType.PLAYOFF_ROUND_BONUS, bonus),
				month);

		FinanceUtility.updateBudget(team.getTeamFinance().getBudget());
	}
}
