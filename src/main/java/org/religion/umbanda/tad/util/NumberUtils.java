package org.religion.umbanda.tad.util;

import java.math.BigDecimal;

public final class NumberUtils {

    public static BigDecimal parseNumber(String number) {
        number = number.replace(".", "");
        number = number.replace(",", ".");
        return new BigDecimal(number);
    }

    private NumberUtils() {
    }

}
