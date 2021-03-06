package org.religion.umbanda.tad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.religion.umbanda.tad.util.DateTimeUtils;

public class Archive {

    @JsonIgnore
    private DateTime archived;

    @JsonProperty("count")
    private int count;

    @JsonProperty("date")
    public String getDate() {
        return DateTimeUtils.toString(getArchived());
    }

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

    public void increaseCount() {
        count++;
    }

}