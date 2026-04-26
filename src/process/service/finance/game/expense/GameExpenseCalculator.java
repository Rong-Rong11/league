package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;

public abstract class GameExpenseCalculator implements GameExpenseBonusProvider {
	private static final Logger logger = LoggerUtility.getLogger(GameExpenseCalculator.class, "text");

	private GameStat gameStat;
	private StadiumCostCalculator stadiumCostCalculator;
	private StaffCostCalculator staffCostCalculator;
	private SecurityCostCalculator securityCostCalculator;
	private LogisticsCostCalculator logisticsCostCalculator;
	private TravelCostCalculator travelCostCalculator;

	public GameExpenseCalculator(GameStat gameStat) {
		this.gameStat = gameStat;
		this.stadiumCostCalculator = new StadiumCostCalculator(gameStat, this);
		this.staffCostCalculator = new StaffCostCalculator(gameStat, this);
		this.securityCostCalculator = new SecurityCostCalculator(gameStat, this);
		this.logisticsCostCalculator = new LogisticsCostCalculator(gameStat, this);
		this.travelCostCalculator = new TravelCostCalculator(gameStat, this);
	}

	public final void calculateGameExpenses(Game game) {
		if (game == null) {
			logger.warn("Skipping game expense calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping game expense calculation because game stat is null");
			return;
		}
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		double gamePopularity = gameStat.getPopularity();
		int attendees = gameStat.getAttendees();
		logger.debug("Calculating game expenses for "
				+ awayTeam.getName()
				+ " at "
				+ homeTeam.getName()
				+ " with popularity "
				+ gamePopularity
				+ " and attendees "
				+ attendees);

		logger.trace("Calculating stadium costs");
		stadiumCostCalculator.calculateStadiumCosts(homeTeam, attendees, gamePopularity, game);
		logger.trace("Calculating staff costs");
		staffCostCalculator.calculateStaffCosts(homeTeam, game);
		logger.trace("Calculating security costs");
		securityCostCalculator.calculateSecurityCosts(homeTeam, attendees, game);
		logger.trace("Calculating logistics costs");
		logisticsCostCalculator.calculateLogisticCosts(game);
		logger.trace("Calculating away travel costs");
		travelCostCalculator.calculateAwayTravelCost(awayTeam, game);
		logger.debug("Game expenses calculated for " + awayTeam.getName() + " at " + homeTeam.getName());
	}
}
