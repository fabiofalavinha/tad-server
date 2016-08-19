package org.religion.umbanda.tad.model.financial;

import java.math.BigDecimal;

public interface BalanceCalculatorStrategy {

    Balance calculate(Balance balance, BigDecimal value);

    Balance rollback(Balance balance, BigDecimal value);

}
