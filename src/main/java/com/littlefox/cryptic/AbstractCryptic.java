package com.littlefox.cryptic;

/**
 * @author rockychen
 */
public abstract class AbstractCryptic {

    protected final String secretKey;

    protected AbstractCryptic(final String secretKey) {
        this.secretKey = secretKey;
    }

    public String encryptSelf(String str){
        return str;
    }
    public String decryptSelf(String str){
        return str;
    }

}
