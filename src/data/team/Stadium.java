/*
 * Decompiled with CFR 0.152.
 */
package data.team;

public class Stadium {
    String name;
    double ticketPrice;
    int capacity;

    public Stadium(String string, double d, int n) {
        this.name = string;
        this.ticketPrice = d;
        this.capacity = n;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public double getTicketPrice() {
        return this.ticketPrice;
    }

    public void setTicketPrice(double d) {
        this.ticketPrice = d;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int n) {
        this.capacity = n;
    }
}
