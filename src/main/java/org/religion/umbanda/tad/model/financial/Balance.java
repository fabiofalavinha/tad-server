package org.religion.umbanda.tad.model.financial;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Balance {

    @JsonProperty
    private BigDecimal value;

    public Balance() {
        value = BigDecimal.ZERO;
    }

    public Balance(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
