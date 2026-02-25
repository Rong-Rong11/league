package data.player;

public class Player {
	private String id ; 
	private String name ; 
	private double note ; 
	private String position ; 
	private Asset preSeasonAssets ;
	private Asset currentSeasonAssets ; 
	private double salary ; 
	private HealthStatus healthStatus ; 
	private boolean preSeasonTransfer ; 
	private boolean isStar ;
	

	public boolean isStar() {
		return isStar;
	}
	public Player(String id, String name, double note, String position, Asset preSeasonAssets,
			double salary, boolean isStar) {
		this.id = id;
		this.name = name;
		this.note = note ; 
		this.position = position;
		this.preSeasonAssets = preSeasonAssets;
		currentSeasonAssets = new Asset() ; 
		this.salary = salary ; 
		healthStatus = new HealthStatus() ;
		preSeasonTransfer = false ; 
		this.isStar = isStar;
	}
	
	public void setStar(boolean isStar) {
		this.isStar = isStar;
	} 
	
	public String getName() {
		return name ; 
	}
	public Asset getPreSeasonAssets() {
		return preSeasonAssets;
	}
	public void setPreSeasonAssets(Asset preSeasonAssets) {
		this.preSeasonAssets = preSeasonAssets;
	}
	public Asset getCurrentSeasonAssets() {
		return currentSeasonAssets;
	}
	public void setCurrentSeasonAssets(Asset currentSeasonAssets) {
		this.currentSeasonAssets = currentSeasonAssets;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public String getPosition () {
		return position ; 
	}
	public HealthStatus getHealthStatus() {
		return healthStatus;
	}
	public void setHealthStatus(HealthStatus healthStatus) {
		this.healthStatus = healthStatus;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", note=" + note + ", position=" + position + ", preSeasonAssets="
				+ preSeasonAssets.toString() + ", currentSeasonAssets=" + currentSeasonAssets.toString() + ", salary=" + salary
				+ ", healthStatus=" + healthStatus.toString() + ", preSeasonTransfer=" + preSeasonTransfer + ", isStar=" + isStar
				;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
