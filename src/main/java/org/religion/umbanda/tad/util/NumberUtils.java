package org.religion.umbanda.tad.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public final class NumberUtils {

    public static BigDecimal parseNumber(String number) {
        try {
            number = number.replace(".", "");
            number = number.replace(",", ".");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            String pattern = "#0.0#";
            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
            decimalFormat.setParseBigDecimal(true);
            return (BigDecimal) decimalFormat.parse(number);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Error parsing number [%s]", number), e);
        }
    }

    private NumberUtils() {
    }

}
