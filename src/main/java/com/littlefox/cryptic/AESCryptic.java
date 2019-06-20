package com.littlefox.cryptic;

/**
 * @author rockychen
 */
public class AESCryptic extends AbstractCryptic implements CrypticInterface {

    @Override
    public <T> T encryptSelf() {
        return null;
    }

    @Override
    public <T> T decryptSelf() {
        return null;
    }


    protected AESCryptic(String privateKey, String defalutAlgorithm) {
        super(privateKey, defalutAlgorithm);
    }
}
