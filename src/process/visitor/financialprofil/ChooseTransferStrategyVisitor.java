package process.visitor.financialprofil;

import config.TeamConfiguration;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import data.team.finance.transfer.TeamTransferStrategy;

public class ChooseTransferStrategyVisitor implements FinancialProfilVisitor<TeamTransferStrategy> {
    private String rivalTeamName;

    public ChooseTransferStrategyVisitor(String rivalTeamName) {
        super();
        this.rivalTeamName = rivalTeamName;
    }

    @Override
    public TeamTransferStrategy visit(ThriftyPolicy thriftyProfil) {
        // TODO Auto-generated method stub
        double random = Math.random();
        if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
            if (random < 0.2) {
                return new Rebuild();
            }
            if (random < 0.5) {
                return new Balanced();
            }
            return new SalaryDump();
        }
        if (random < 0.4) {
            return new Rebuild();
        }
        if (random < 0.7) {
            return new Balanced();
        }
        return new SalaryDump();
    }

    @Override
    public TeamTransferStrategy visit(BalancedPolicy balancedProfil) {
        // TODO Auto-generated method stub
        double random = Math.random();
        if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
            if (random < 0.4) {
                return new SmallAdjust();
            }
            if (random < 0.8) {
                return new Balanced();
            }
            return new SalaryDump();
        }
        if (random < 0.2) {
            return new SmallAdjust();
        }
        if (random < 0.5) {
            return new Balanced();
        }
        return new SalaryDump();
    }

    @Override
    public TeamTransferStrategy visit(AmbitiousPolicy ambitiousProfil) {
        // TODO Auto-generated method stub
        double random = Math.random();
        if (!rivalTeamName.equals(TeamConfiguration.NO_RIVAL)) {
            if (random < 0.5) {
                return new AllIn();
            }
            if (random < 0.8) {
                return new SuperstarBuild();
            }
            return new SmallAdjust();
        }
        if (random < 0.35) {
            return new AllIn();
        }
        if (random < 0.7) {
            return new SmallAdjust();
        }
        return new Balanced();
    }

}
