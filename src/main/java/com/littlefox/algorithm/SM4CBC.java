package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.CrypticInterface;
import com.littlefox.utils.sm4.SM4Utils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * 对称加密 SM4CBC
 *
 * @author rockychen
 */
public class SM4CBC extends AbstractCryptic implements CrypticInterface {

    public SM4CBC(String secretKey, String algorithm) {
        super(secretKey, algorithm);
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
