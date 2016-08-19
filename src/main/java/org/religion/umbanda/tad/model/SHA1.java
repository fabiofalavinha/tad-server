package org.religion.umbanda.tad.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA1 {

    private static final String PROVIDER_NAME = "SHA1";

    public static String generate(String clearText) {
        if (clearText == null) {
            throw new IllegalArgumentException("Clear text could not be null");
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(PROVIDER_NAME);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
        messageDigest.update(clearText.getBytes());
        return toHex(messageDigest.digest());
    }

    private static String toHex(byte[] data) {
        final char hexDigit[] = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append(hexDigit[(data[i] >> 4) & 0x0f]);
            builder.append(hexDigit[data[i] & 0x0f]);
        }
        return builder.toString();
    }

}
