/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.visitor.teamtransfer.TeamTransferVisitor;

public class SeasonTradeSatisfactionVisitor
implements TeamTransferVisitor<Boolean> {
    private int transfersMade;
    private String seasonIntent;

    public SeasonTradeSatisfactionVisitor(int n, String string) {
        this.transfersMade = n;
        this.seasonIntent = string;
    }

    @Override
    public Boolean visit(AllIn allIn) {
        if (this.seasonIntent.equals("seller") && this.transfersMade < 7) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean visit(SuperstarBuild superstarBuild) {
        if (this.seasonIntent.equals("seller") && this.transfersMade < 3) {
            return false;
        }
        if (this.seasonIntent.equals("buyer") && this.transfersMade < 2) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean visit(SmallAdjust smallAdjust) {
        return this.transfersMade >= 3;
    }

    @Override
    public Boolean visit(Balanced balanced) {
        if (this.transfersMade >= 4) {
            return true;
        }
        if (this.seasonIntent.equals("seller") && this.transfersMade < 3) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean visit(Rebuild rebuild) {
        return this.transfersMade >= 6;
    }

    @Override
    public Boolean visit(SalaryDump salaryDump) {
        return this.transfersMade >= 5;
    }
}
