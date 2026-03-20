/*
 * Decompiled with CFR 0.152.
 */
package data.league;

import data.calendar.NBACalendar;
import data.calendar.SpecialEvent;
import data.league.Ranking;
import java.time.LocalDate;
import java.util.HashMap;

public abstract class Season {
    private NBACalendar calendar = new NBACalendar();
    private LocalDate debutDate;
    private LocalDate endDate;
    private HashMap<LocalDate, SpecialEvent> specialEvents;
    private Ranking ranking;

    public Season(LocalDate localDate, LocalDate localDate2) {
        this.debutDate = localDate;
        this.endDate = localDate2;
        this.specialEvents = new HashMap();
        this.ranking = new Ranking();
    }

    public LocalDate getDebutDate() {
        return this.debutDate;
    }

    public void setDebutDate(LocalDate localDate) {
        this.debutDate = localDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate localDate) {
        this.endDate = localDate;
    }

    public NBACalendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(NBACalendar nBACalendar) {
        this.calendar = nBACalendar;
    }

    public void addSpecialEvents(SpecialEvent specialEvent) {
        if (!this.specialEvents.containsKey(specialEvent.getDate())) {
            this.specialEvents.put(specialEvent.getDate(), specialEvent);
        }
    }

    public Ranking getRanking() {
        return this.ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }
}
