package org.religion.umbanda.tad.model;

import org.joda.time.DateTime;

import java.util.UUID;

public class Event {

    private UUID id;
    private String title;
    private String notes;
    private DateTime date;
    private VisibilityType visibility;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public  int getYear() {
        return date == null ? 0 : date.getYear();
    }
}
