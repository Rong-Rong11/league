package data.league.finance;

import config.FinanceConfiguration;

public class LeagueFinancialRules {

   private double salaryCap;
   private double luxuryTaxLine;
   private double minimumTeamSalary;
   private double capGrowthRate = FinanceConfiguration.CAP_GROWTH_RATE;

   public LeagueFinancialRules(double salaryCap, double luxuryTaxLine, double minimumTeamSalary) {
      this.salaryCap = salaryCap;
      this.luxuryTaxLine = luxuryTaxLine;
      this.minimumTeamSalary = minimumTeamSalary;
   }

   public double getSalaryCap() {
      return salaryCap;
   }

   public double getLuxuryTaxLine() {
      return luxuryTaxLine;
   }

   public double getMinimumTeamSalary() {
      return minimumTeamSalary;
   }

   public void setSalaryCap(double salaryCap) {
      this.salaryCap = salaryCap;
   }

   public void setLuxuryTaxLine(double luxuryTaxLine) {
      this.luxuryTaxLine = luxuryTaxLine;
   }

   public void setMinimumTeamSalary(double minimumTeamSalary) {
      this.minimumTeamSalary = minimumTeamSalary;
   }

   public double getCapGrowthRate() {
      return capGrowthRate;
   }

   public void setCapGrowthRate(double capGrowthRate) {
      this.capGrowthRate = capGrowthRate;
   }

}
