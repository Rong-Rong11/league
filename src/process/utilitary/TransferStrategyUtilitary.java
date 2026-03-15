package process.utilitary;
import config.TeamConfiguration;

import config.FinancialPolicy;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import data.team.finance.transfer.TeamTransferStrategy;

public class TransferStrategyUtilitary {

	public static TeamTransferStrategy chooseTransferStrategy(FinancialProfil financialProfil, String rivalTeamName) {
		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_AMBITIOUS)) {
			return chooseTransferStrategyAmbitious(rivalTeamName);
		}
		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_ECONOMIC)) {
			return chooseTransferStrategyEconomic(rivalTeamName);
		}
		return chooseTransferStrategyBalanced(rivalTeamName);
	}

	public static TeamTransferStrategy chooseTransferStrategyAmbitious(String rivalTeamName) {
		double random = Math.random();
		if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
			if (random < 0.5) {
				return new AllIn(FinancialPolicy.TRANSFER_STRATEGY_ALL_IN);
			}
			if (random < 0.8) {
				return new SuperstarBuild(FinancialPolicy.TRANSFER_STRATEGY_SUPERSTAR_BUILD);
			}
			return new SmallAdjust(FinancialPolicy.TRANSFER_STRATEGY_SMALL_ADJUST);
		}

		if (random < 0.35) {
			return new AllIn(FinancialPolicy.TRANSFER_STRATEGY_ALL_IN);
		}
		if (random < 0.7) {
			return new SmallAdjust(FinancialPolicy.TRANSFER_STRATEGY_SMALL_ADJUST);
		}
		return new Balanced(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
	}

	public static TeamTransferStrategy chooseTransferStrategyBalanced(String rivalTeamName) {
		double random = Math.random();
		if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
			if (random < 0.4) {
				return new SmallAdjust(FinancialPolicy.TRANSFER_STRATEGY_SMALL_ADJUST);
			}
			if (random < 0.8) {
				return new Balanced(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
			}
			return new SalaryDump(FinancialPolicy.TRANSFER_STRATEGY_SALARY_DUMP);

		}

		if (random < 0.2) {
			return new SmallAdjust(FinancialPolicy.TRANSFER_STRATEGY_SMALL_ADJUST);
		}
		if (random < 0.5) {
			return new Balanced(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
		}
		return new SalaryDump(FinancialPolicy.TRANSFER_STRATEGY_SALARY_DUMP);
	}

	public static TeamTransferStrategy chooseTransferStrategyEconomic(String rivalTeamName) {
		double random = Math.random();
		if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
			if (random < 0.2) {
				return new Rebuild(FinancialPolicy.TRANSFER_STRATEGY_REBUILD);
			}
			if (random < 0.5) {
				return new Balanced(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
			}
			return new SalaryDump(FinancialPolicy.TRANSFER_STRATEGY_SALARY_DUMP);

		}
		if (random < 0.4) {
			return new Rebuild(FinancialPolicy.TRANSFER_STRATEGY_REBUILD);
		}
		if (random < 0.7) {
			return new Balanced(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
		}
		return new SalaryDump(FinancialPolicy.TRANSFER_STRATEGY_SALARY_DUMP);

	}
}
