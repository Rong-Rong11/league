package process.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import config.FinanceConfiguration;
import config.SimulationConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Income;
import data.league.Division;
import data.league.League;
import data.league.LeagueFinance;
import data.player.Player;
import data.team.Team;
import data.team.finance.AmbitiousProfil;
import data.team.finance.BalancedProfil;
import data.team.finance.EconomicalProfil;
import data.team.finance.FinancialProfil;
import process.factory.PlayerFactory;
import process.factory.TeamFactory;
import process.repositery.CurrentSeasonAssetRepositery;
import process.repositery.DivisionRepositery;
import process.repositery.PlayerRepositery;
import process.repositery.PreSeasonAssetRepositery;
import process.repositery.TeamRepositery;

public class LeagueBuilder {

	private String filename = "src/test/nba.csv";
	private PlayerRepositery playerRepositery = PlayerRepositery.getInstance();
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private DivisionRepositery divisionRepositery = DivisionRepositery.getInstance();
	private PreSeasonAssetRepositery preSeasonAssetRepositery = PreSeasonAssetRepositery.getInstance();
	private CurrentSeasonAssetRepositery currentSeasonAssetRepositery = CurrentSeasonAssetRepositery.getInstance();

	public LeagueBuilder() {

	}

	public League build() {
		League league = new League();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			String line;
			bufferedReader.readLine();

			while ((line = bufferedReader.readLine()) != null) {
				String[] data = line.split(",", -1);
				String teamName = data[2];
				String conferenceName = data[4];
				String divisionName = data[5];

				Player player = PlayerFactory.createPlayer(line);

				if (divisionRepositery.getDivision(divisionName) == null) {
					Division division = new Division(divisionName);
					if (conferenceName.equals("West")) {
						league.addDivisionWesternConference(division);
					} else {
						league.addDivisionEasternConference(division);
					}
					divisionRepositery.register(divisionName, division);
				}

				if (teamRepositery.getTeam(teamName) == null) {
					Team team = TeamFactory.createTeam(line);
					if (conferenceName.equals("West")) {
						league.addTeamWesternConference(team, divisionName);
					} else {
						league.addTeamEasternConference(team, divisionName);
					}
					teamRepositery.register(teamName, team);

				}

				teamRepositery.getTeam(teamName).addPlayer(player);
				playerRepositery.register(player.getName(), player);
				preSeasonAssetRepositery.register(player, player.getPreSeasonAssets());
				currentSeasonAssetRepositery.register(player, player.getCurrentSeasonAssets());
				setStarPlayerTeams();

			}
			bufferedReader.close();

		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		buildFinanceLeague(league);
		return league;
	}

	private void setStarPlayerTeams() {
		for (Team team : teamRepositery.getAllTeams()) {
			team.setStarPlayer();
		}
	}
	private void buildFinanceLeague(League league) {
		double initialBudget = FinanceConfiguration.INITIAL_LEAGUE_BUDGET ; 
		Budget budget = new Budget(initialBudget) ; 

		budget.addIncome(new Income(FinanceConfiguration.REVENUE_TYPE_TV, initialBudget * 0.65));
		budget.addIncome(new Income(FinanceConfiguration.REVENUE_TYPE_SPONSORING, initialBudget * 0.15));
		budget.addIncome(new Income(FinanceConfiguration.REVENUE_TYPE_MERCHANDISING, initialBudget * 0.10));
		budget.addIncome(new Income(FinanceConfiguration.REVENUE_TYPE_OTHER, initialBudget * 0.10));
		
		double salaryCap = (initialBudget * FinanceConfiguration.PLAYER_SHARE) / SimulationConfiguration.NUMBER_OF_TEAM ; 
		double luxuryTaxLine = salaryCap * FinanceConfiguration.LUXURYTAX_THRESHOLD_RATE ;
		double minimumTeamSalary = salaryCap * FinanceConfiguration.MINIMUM_TEAM_SALARY_RATE ;
		
		LeagueFinance leagueFinance = new LeagueFinance(budget, salaryCap, luxuryTaxLine, minimumTeamSalary) ; 
		league.setLeagueFinance(leagueFinance);
	}
}
