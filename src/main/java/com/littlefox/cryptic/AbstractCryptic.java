package com.littlefox.cryptic;

/**
 * @author rockychen
 */
public abstract class AbstractCryptic {
    protected final String secretKey;
    protected final String algorithm;

    protected AbstractCryptic(final String secretKey, final String algorithm) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;
    }

    public String encryptSelf(String str){
        return str;
    }
    public String decryptSelf(String str){
        return str;
    }

}
