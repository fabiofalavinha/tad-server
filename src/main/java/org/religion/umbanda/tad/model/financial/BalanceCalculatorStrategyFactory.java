package org.religion.umbanda.tad.model.financial;

import java.util.HashMap;
import java.util.Map;

public class BalanceCalculatorStrategyFactory {

    private static final Map<Category, BalanceCalculatorStrategy> strategyMap;

    static {
        strategyMap = new HashMap<>();
        strategyMap.put(Category.PAYABLE, new PayableBalanceCalculatorStrategy());
        strategyMap.put(Category.RECEIVABLE, new ReceivableBalanceCalculatorStrategy());
    }

    public BalanceCalculatorStrategy getBalanceCalculatorStrategy(Category category) {
        if (category == null) {
            throw new NullPointerException("category argument is null");
        }
        final BalanceCalculatorStrategy found = strategyMap.get(category);
        if (found == null) {
            throw new IllegalArgumentException(String.format("Could not find balance calculator strategy for category [%s]", category));
        }
        return found;
    }
}
