package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;

public abstract class GameExpenseCalculator implements GameExpenseBonusProvider {

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
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		double gamePopularity = gameStat.getPopularity();
		int attendees = gameStat.getAttendees();

		stadiumCostCalculator.calculateStadiumCosts(homeTeam, attendees, gamePopularity, game);
		staffCostCalculator.calculateStaffCosts(homeTeam, game);
		securityCostCalculator.calculateSecurityCosts(homeTeam, attendees, game);
		logisticsCostCalculator.calculateLogisticCosts(game);
		travelCostCalculator.calculateAwayTravelCost(awayTeam, game);
	}
}
