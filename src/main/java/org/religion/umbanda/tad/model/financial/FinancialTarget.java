package org.religion.umbanda.tad.model.financial;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinancialTarget {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private FinancialTargetType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FinancialTargetType getType() {
        return type;
    }

    public void setType(FinancialTargetType type) {
        this.type = type;
    }
}
