package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.Cryptic;
import com.littlefox.utils.aes.AESUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 AES
 * @author rockychen
 */
@Slf4j
public class AES extends AbstractCryptic implements Cryptic {

    public AES(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return AESUtils.encrypt(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES encryptSelf error : {}",e);
            return super.decryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return AESUtils.decrypt(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES decryptSelf error : {}",e);
            return super.decryptSelf(str);
        }
    }

}
