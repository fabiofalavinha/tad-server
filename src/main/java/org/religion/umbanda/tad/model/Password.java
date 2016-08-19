package org.religion.umbanda.tad.model;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Password {

    public static Password randomPassword() {
        final SecureRandom random = new SecureRandom();
        return createBySHA1(new BigInteger(130, random).toString(32));
    }

    public static Password createBySHA1(String clearText) {
        return fromSecret(SHA1.generate(clearText));
    }

    public static Password fromSecret(String secret) {
        return new Password(secret);
    }

    private final String secret;

    private Password(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Password) {
            final Password other = (Password) o;
            return getSecret().equals(other.getSecret());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash += 17 * (secret != null && !"".equals(secret) ? secret.hashCode() : 0);
        return hash;
    }

}