package process.factory;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;
import process.utility.TeamUtility;
import process.visitor.financialpolicy.ChooseTransferStrategyVisitor;

public class TeamFactory {
	private static final Logger logger = LoggerUtility.getLogger(TeamFactory.class, "text");

	private static String checkRivalTeam(String rivalTeam) {
		if (rivalTeam.equals("")) {
			logger.debug("Rival team is empty, using default value none");
			return "none";
		}
		logger.trace("Using rival team " + rivalTeam);
		return rivalTeam;
	}

	public static Team createTeam(String line) {
		logger.trace("Parsing team CSV line");
		String[] data = line.split(",", -1);
		if (data.length <= 35) {
			logger.warn("Team CSV line has " + data.length + " fields, expected at least 36");
		}

		String teamName = data[2];
		logger.debug("Creating team " + teamName);
		String rivalTeamName = TeamFactory.checkRivalTeam(data[11]);
		double teamPopularity = Float.valueOf(data[12]).floatValue();
		FinancialPolicy financialPolicy = TeamUtility.randomFinancialPolicy();
		MarketSize marketSize = TeamUtility.randomMarketSize();
		Budget budget = new Budget(0.0);
		logger.debug("Team finance base data generated for "
				+ teamName
				+ " with policy "
				+ financialPolicy.getClass().getSimpleName()
				+ " and market size "
				+ marketSize.getClass().getSimpleName());

		TeamTransferStrategy teamTransferStrategy = financialPolicy
				.accept(new ChooseTransferStrategyVisitor(rivalTeamName));
		TeamFinance teamFinance = new TeamFinance(financialPolicy, budget, marketSize, teamTransferStrategy);
		String stadiumName = data[33];
		Stadium stadium = new Stadium(stadiumName, 0.0, 0);
		Team team = new Team(teamName, rivalTeamName, teamPopularity, teamFinance, stadium);
		team.setAbbreviation(data[3]);
		team.setConference(data[4]);
		team.setDivision(data[5]);
		team.setCity(data[34]);
		team.setShortName(data[35]);

		logger.debug("Team data parsed for "
				+ teamName
				+ " with abbreviation "
				+ data[3]
				+ ", conference "
				+ data[4]
				+ ", division "
				+ data[5]
				+ ", popularity "
				+ teamPopularity);
		logger.debug("Created team " + teamName + " with stadium " + stadiumName);
		return team;
	}

}
