package org.religion.umbanda.tad.service.vo;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.VisibilityType;

public class EventRequest {

    private String id;
    private String title;
    private String notes;
    private DateTime date;
    private VisibilityType visibility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }
}
