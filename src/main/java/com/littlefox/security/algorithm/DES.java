package com.littlefox.security.algorithm;

import com.littlefox.security.crypto.AbstractICryptic;
import com.littlefox.security.utils.des.DESUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 DES
 * @author rockychen
 */
@Slf4j
public class DES extends AbstractICryptic {

    public DES(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return DESUtils.encode(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES encryptSelf Error : {}",str);
            return super.encryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return DESUtils.decode(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES decryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }

}
