package data.player;


public class Player {
	private String id;
	private String name;
	private String position;
	private Asset preSeasonAssets;
	private Asset currentSeasonAssets;
	private double salary;
	private HealthStatus healthStatus;
	private boolean preSeasonTransfer;
	private boolean isStar;
	private boolean transfered;

	public boolean isStar() {
		return this.isStar;
	}

	public Player(String id, String name, double ignoredNote, String position, Asset preSeasonAssets, double salary,
			boolean starPlayer) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.preSeasonAssets = preSeasonAssets;
		this.currentSeasonAssets = new Asset();
		this.salary = salary;
		this.healthStatus = new HealthStatus();
		this.preSeasonTransfer = false;
		this.isStar = starPlayer;
		this.transfered = false;
	}

	public void setStar(boolean starPlayer) {
		this.isStar = starPlayer;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Asset getPreSeasonAssets() {
		return this.preSeasonAssets;
	}

	public void setPreSeasonAssets(Asset asset) {
		this.preSeasonAssets = asset;
	}

	public Asset getCurrentSeasonAssets() {
		return this.currentSeasonAssets;
	}

	public void setCurrentSeasonAssets(Asset asset) {
		this.currentSeasonAssets = asset;
	}

	public double getSalary() {
		return this.salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getPosition() {
		return this.position;
	}

	public HealthStatus getHealthStatus() {
		return this.healthStatus;
	}

	public void setHealthStatus(HealthStatus healthStatus) {
		this.healthStatus = healthStatus;
	}

	public boolean isTransfered() {
		return this.transfered;
	}

	public void setTransfered(boolean transfered) {
		this.transfered = transfered;
	}

	public String toString() {
		return "Player [id=" + this.id + ", name=" + this.name + ", note=, position=" + this.position + ", preSeasonAssets=" + this.preSeasonAssets.toString() + ", currentSeasonAssets=" + this.currentSeasonAssets.toString() + ", salary=" + this.salary + ", healthStatus=" + this.healthStatus.toString() + ", preSeasonTransfer=" + this.preSeasonTransfer + ", isStar=" + this.isStar;
	}
}
