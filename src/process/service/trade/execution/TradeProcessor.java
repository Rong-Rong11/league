package process.service.trade.execution;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.player.Player;
import data.team.Team;
import log.LoggerUtility;

public class TradeProcessor {
	private static final Logger logger = LoggerUtility.getLogger(TradeProcessor.class, "text");

	private TradeValidator tradeValidator = new TradeValidator();
	private TradeApplier tradeApplier = new TradeApplier();
	private TradeImpact tradeImpact = new TradeImpact();

	public boolean processTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, int month, double salaryCap, double luxuryTaxLine) {

		if (teamA == null || teamB == null) {
			logger.warn("Skipping trade processing because one of the teams is null");
			return false;
		}

		logger.debug("Processing trade between " + teamA.getName() + " and " + teamB.getName());

		if (!this.tradeValidator.validateTrade(teamA, teamB, teamAIncoming, teamBIncoming, salaryCap)) {
			logger.debug("Trade validation failed between " + teamA.getName() + " and " + teamB.getName());
			return false;
		}

		this.tradeApplier.applyTrade(teamA, teamAIncoming);
		this.tradeApplier.applyTrade(teamB, teamBIncoming);

		this.tradeImpact.applyFinanceImpact(teamA, luxuryTaxLine, month);
		this.tradeImpact.applyFinanceImpact(teamB, luxuryTaxLine, month);

		logger.debug("Trade successfully processed between " + teamA.getName() + " and " + teamB.getName());

		return true;
	}
}
