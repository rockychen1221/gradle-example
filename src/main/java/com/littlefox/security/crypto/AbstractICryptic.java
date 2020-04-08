package com.littlefox.security.crypto;

/**
 * @author rockychen
 */
public abstract class AbstractICryptic implements ICryptic {

    protected final String secretKey;

    protected AbstractICryptic(final String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String encryptSelf(String str) {
        return str;
    }

    @Override
    public String decryptSelf(String str) {
        return str;
    }
}
