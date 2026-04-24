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
import process.visitor.financialprofil.ChooseTransferStrategyVisitor;
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
		revenueSharingManager = new RevenueSharingManager(league);
		regularSeasonMonthlyFinanceCalculator = new RegularSeasonMonthlyTeamFinanceCalculator(league);
		playoffMonthlyFinanceCalculator = new PlayoffMonthlyTeamFinanceCalculator(league);
		centralRevenueDistributor = new CentralRevenueDistributor(league);
		leagueExpenseCalculator = new LeagueExpenseCalculator(league);

		regularSeasonGameProcessor = new RegularSeasonGameFinanceProcessor(league);
		centralRevenueDistributor.setFinanceManager(this);
		leagueExpenseCalculator.setFinanceManager(this);
		logger.debug("Finance manager initialized");
	}

	// Initialization
	public void initializeFinance() {
		logger.info("Initializing finance data");
		financeInitializer.initializeFinance();
	}

	// Monthly simulation
	public void applyMonthlyFinance(int month) {
		logger.debug("Applying regular season monthly finance for month " + month);
		applyRegularSeasonMonthlyFinanceToAllTeams(month);
		distributeCentralRevenue(month);
		applyLeagueExpenses(month);
		applyRevenueSharing(month);
	}

	public void applyPlayoffMonthlyFinance(int month, ArrayList<Team> activePlayoffTeams) {
		logger.debug("Applying playoff monthly finance for month " + month + " with " + activePlayoffTeams.size()
				+ " active playoff teams");
		for (Team team : teamRepository.getAllTeams()) {
			if (activePlayoffTeams.contains(team)) {
				applyPlayoffMonthlyFinanceToTeam(team, month);
				continue;
			}

			applyPlayoffFixedCostsToTeam(team, month);
		}
		distributeCentralRevenue(month);
		applyLeagueExpenses(month);
	}

	private void distributeCentralRevenue(int month) {
		centralRevenueDistributor.distributeMonthlyCentralRevenue(month);
	}

	private void applyRevenueSharing(int month) {
		revenueSharingManager.applyRevenueSharing(month);
	}

	private void applyLeagueExpenses(int month) {
		leagueExpenseCalculator.applyMonthlyExpenses(month);
	}

	// Game finance
	public void calculateRegularSeasonGame(Game game, LocalDate date, int month) {
		logger.debug("Calculating regular season game finance for " + date + " month " + month);
		regularSeasonGameProcessor.calculateGame(game, date, month);
	}

	public void calculatePlayoffGame(Game game, LocalDate date, int month, PlayoffRound round) {
		logger.debug("Calculating playoff game finance for round " + round + " at " + date + " month " + month);
		PlayoffGameFinanceProcessor playoffGameFinanceProcessor = getOrCreatePlayoffGameProcessor(round);
		playoffGameFinanceProcessor.calculateGame(game, date, month);
	}

	private PlayoffGameFinanceProcessor getOrCreatePlayoffGameProcessor(PlayoffRound round) {
		PlayoffGameFinanceProcessor playoffGameProcessor = playoffGameProcessorsByRound.get(round);
		if (playoffGameProcessor == null) {
			playoffGameProcessor = new PlayoffGameFinanceProcessor(league, round);
			playoffGameProcessorsByRound.put(round, playoffGameProcessor);
		}
		return playoffGameProcessor;
	}

	private void applyRegularSeasonMonthlyFinanceToTeam(Team team, int month) {
		regularSeasonMonthlyFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyPlayoffFixedCostsToTeam(Team team, int month) {
		playoffMonthlyFinanceCalculator.applyMonthlyFixedCosts(team, month);
	}

	private void applyPlayoffMonthlyFinanceToTeam(Team team, int month) {
		playoffMonthlyFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyRegularSeasonMonthlyFinanceToAllTeams(int month) {
		for (Team team : teamRepository.getAllTeams()) {
			applyRegularSeasonMonthlyFinanceToTeam(team, month);
		}
	}

	// getters
	public GameStat getGameStat(Game game) {
		GameStat gameStat = regularSeasonGameProcessor.getGameStat(game);

		if (gameStat != null) {
			return gameStat;
		}

		for (PlayoffGameFinanceProcessor playoffGameFinanceProcessor : playoffGameProcessorsByRound.values()) {
			gameStat = playoffGameFinanceProcessor.getGameStat(game);

			if (gameStat != null) {
				return gameStat;
			}
		}

		return null;
	}

	// Team finance setup
	public void randomFinancialPolicy() {
		logger.debug("Randomizing financial policy for all teams");
		for (Team team : teamRepository.getAllTeams()) {
			FinancialPolicy financialPolicy = TeamUtility.randomFinancialProfil();
			chooseFinancialPolicy(team, financialPolicy);
		}
	}

	public void chooseFinancialPolicy(Team team, FinancialPolicy financialPolicy) {
		team.getTeamFinance().setFinancialProfil(financialPolicy);
		team.getTeamFinance()
				.setTeamTransferStrategy(financialPolicy.accept(new ChooseTransferStrategyVisitor(team.getRival())));
	}

	public void chooseMarketSize(Team team, MarketSize marketSize) {
		team.getTeamFinance().setMarketSize(marketSize);
	}

	public void randomMarketSize() {
		logger.debug("Randomizing market size for all teams");
		for (Team team : teamRepository.getAllTeams()) {
			MarketSize marketSize = TeamUtility.randomMarketSize();
			chooseMarketSize(team, marketSize);
		}
	}

	public double getTeamCurrentPayroll(Team team) {
		return team.getTeamFinance().getCurrentPayroll();
	}

	// Playoff bonuses
	public void applyPlayoffQualificationBonus(Team team, int month) {
		logger.debug("Applying playoff qualification bonus to " + team.getName() + " for month " + month);
		double bonus = calculatePlayoffQualificationBonus(team);

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
		double marketExposure = team.getTeamFinance().getMediaMarket().getBusinessOpportunityModifier();
		double prestige = team.getTeamFinance().getEconomicProfil().getHistoricalPrestige();

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
		for (Team team : teams) {
			applyPlayoffQualificationBonus(team, month);
		}
	}

	public void applyPlayoffRoundBonus(Team team, int month, PlayoffRound round) {
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
