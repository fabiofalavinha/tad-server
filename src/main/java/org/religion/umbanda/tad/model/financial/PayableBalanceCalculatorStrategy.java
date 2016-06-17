package org.religion.umbanda.tad.model.financial;

import java.math.BigDecimal;

public class PayableBalanceCalculatorStrategy implements BalanceCalculatorStrategy {

    @Override
    public Balance calculate(Balance balance, BigDecimal value) {
        return new Balance(balance.getValue().subtract(value));
    }
}
