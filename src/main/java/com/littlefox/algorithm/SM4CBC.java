package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.Cryptic;
import com.littlefox.utils.sm4.SM4Utils;

/**
 * 对称加密 SM4CBC
 * @author rockychen
 */
public class SM4CBC extends AbstractCryptic implements Cryptic {

    public SM4CBC(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        return new SM4Utils(this.secretKey).encryptData_CBC(str);
    }

    @Override
    public String decryptSelf(String str) {
        return new SM4Utils(this.secretKey).decryptData_CBC(str);
    }

}
