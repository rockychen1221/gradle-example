package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.Cryptic;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * SM3单向加密，无法解密
 * @author rockychen
 */
public class SM3 extends AbstractCryptic implements Cryptic {

    public SM3(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        byte[] md = new byte[32];
        byte[] msg1 = str.getBytes();
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        return s.toUpperCase();
    }
}
