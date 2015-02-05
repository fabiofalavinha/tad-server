package org.religion.umbanda.tad.model;

import org.joda.time.DateTime;

public class Archive {
    
    private DateTime archived;
    
    public String getName() {
        return String.format("%s %d", archived.toString("MMM"), archived.getYear());
    }
    
    public DateTime getArchived() {
        return archived;
    }
    
    public void setArchived(DateTime archived) {
        this.archived = archived;
    }
    
}