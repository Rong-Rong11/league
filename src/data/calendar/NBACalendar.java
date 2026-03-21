/*
 * Decompiled with CFR 0.152.
 */
package data.calendar;

import data.calendar.GameDay;
import java.time.LocalDate;
import java.util.TreeMap;

public class NBACalendar {
    private TreeMap<LocalDate, GameDay> calendar = new TreeMap();

    public TreeMap<LocalDate, GameDay> getCalendar() {
        return this.calendar;
    }

    public void setCalendar(TreeMap<LocalDate, GameDay> treeMap) {
        this.calendar = treeMap;
    }
}
