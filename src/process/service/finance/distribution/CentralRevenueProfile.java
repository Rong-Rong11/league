package process.service.finance.distribution;

public class CentralRevenueProfile {
    private final double tvRate;
    private final double sponsoringRate;
    private final double merchandisingRate;

    public CentralRevenueProfile(double tvRate, double sponsoringRate, double merchandisingRate) {
        this.tvRate = tvRate;
        this.sponsoringRate = sponsoringRate;
        this.merchandisingRate = merchandisingRate;
    }

    public double getTvRate() {
        return tvRate;
    }

    public double getSponsoringRate() {
        return sponsoringRate;
    }

    public double getMerchandisingRate() {
        return merchandisingRate;
    }
}
