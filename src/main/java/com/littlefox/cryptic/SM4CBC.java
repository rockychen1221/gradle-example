package com.littlefox.cryptic;

import com.littlefox.utils.sm4.SM4Utils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * 对称加密 SM4CBC
 *
 * @author rockychen
 */
public class SM4CBC extends AbstractCryptic implements CrypticInterface {

    private SM4Utils sm4;

    public SM4CBC(String privateKey, String algorithm) {
        super(privateKey, algorithm);
    }

    @Override
    public String encryptSelf(String str) {
        SM4Utils sm4 = getInstance();
        return sm4.encryptData_CBC(str);
    }

    @Override
    public String decryptSelf(String str) {
        SM4Utils sm4 = getInstance();
        return sm4.decryptData_CBC(str);
    }

    private SM4Utils getInstance() {
        if (sm4 == null) {
            // 当需要创建的时候在加锁
            synchronized (SM4Utils.class) {
                if (sm4 == null) {
                    sm4 = new SM4Utils();
                    init();
                }
            }
        }
        return sm4;
    }

    private void init(){
        sm4.secretKey = this.privateKey;
        sm4.hexString = true;
        sm4.iv = "31313131313131313131313131313131";
    }

}
