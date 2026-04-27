package data.team;

public class Stadium {
	String name;
	double ticketPrice;
	int capacity;

	public Stadium(String name, double ticketPrice, int capacity) {
		this.name = name;
		this.ticketPrice = ticketPrice;
		this.capacity = capacity;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTicketPrice() {
		return this.ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
