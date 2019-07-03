package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.Cryptic;
import com.littlefox.utils.sm4.SM4Utils;

/**
 * 对称加密 SM4CBC
 * @author rockychen
 */
public class SM4ECB extends AbstractCryptic implements Cryptic {

    public SM4ECB(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        return new SM4Utils(this.secretKey).encryptData_ECB(str);
    }

    @Override
    public String decryptSelf(String str) {
        return new SM4Utils(this.secretKey).decryptData_ECB(str);
    }
}
