package org.religion.umbanda.tad.model;

import org.joda.time.DateTime;

public class Archive {
    
    private String name;
    private DateTime archived;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public DateTime getArchived() {
        return archived;
    }
    
    public void setArchived(DateTime archived) {
        this.archived = archived;
    }
    
}