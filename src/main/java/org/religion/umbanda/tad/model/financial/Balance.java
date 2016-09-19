package org.religion.umbanda.tad.model.financial;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.religion.umbanda.tad.util.NumberUtils;

import java.math.BigDecimal;

public class Balance {

    public static Balance fromString(String balance) {
        return new Balance(NumberUtils.parseNumber(balance));
    }

    @JsonProperty
    private BigDecimal value;

    public Balance() {
        value = BigDecimal.ZERO;
    }

    public Balance(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public Balance(Balance balance) {
        this.value = balance.getValue();
    }

    public Balance(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Balance calculate(BigDecimal value, Category category) {
        final BalanceCalculatorStrategyFactory balanceCalculatorStrategyFactory = new BalanceCalculatorStrategyFactory();
        final BalanceCalculatorStrategy balanceCalculatorStrategy = balanceCalculatorStrategyFactory.getBalanceCalculatorStrategy(category);
        return balanceCalculatorStrategy.calculate(this, value);
    }

    public Balance rollback(BigDecimal value, Category category) {
        final BalanceCalculatorStrategyFactory balanceCalculatorStrategyFactory = new BalanceCalculatorStrategyFactory();
        final BalanceCalculatorStrategy balanceCalculatorStrategy = balanceCalculatorStrategyFactory.getBalanceCalculatorStrategy(category);
        return balanceCalculatorStrategy.rollback(this, value);
    }
}
