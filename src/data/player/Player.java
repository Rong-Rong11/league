/*
	* Decompiled with CFR 0.152.
	*/
package data.player;

import data.player.Asset;
import data.player.HealthStatus;

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

	public Player(String string, String string2, double d, String string3, Asset asset, double d2, boolean bl) {
		this.id = string;
		this.name = string2;
		this.position = string3;
		this.preSeasonAssets = asset;
		this.currentSeasonAssets = new Asset();
		this.salary = d2;
		this.healthStatus = new HealthStatus();
		this.preSeasonTransfer = false;
		this.isStar = bl;
		this.transfered = false;
	}

	public void setStar(boolean bl) {
		this.isStar = bl;
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

	public void setSalary(double d) {
		this.salary = d;
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

	public void setTransfered(boolean bl) {
		this.transfered = bl;
	}

	public String toString() {
		return "Player [id=" + this.id + ", name=" + this.name + ", note=, position=" + this.position + ", preSeasonAssets=" + this.preSeasonAssets.toString() + ", currentSeasonAssets=" + this.currentSeasonAssets.toString() + ", salary=" + this.salary + ", healthStatus=" + this.healthStatus.toString() + ", preSeasonTransfer=" + this.preSeasonTransfer + ", isStar=" + this.isStar;
	}
}
