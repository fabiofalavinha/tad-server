package org.religion.umbanda.tad.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class Archive {

    @JsonProperty("date")
    private DateTime archived;

    @JsonProperty("count")
    private int count;

    @JsonProperty("name")
    public String getName() {
        return String.format("%s %d", archived.toString("MMMM"), archived.getYear());
    }
    
    public DateTime getArchived() {
        return archived;
    }
    
    public void setArchived(DateTime archived) {
        this.archived = archived;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}