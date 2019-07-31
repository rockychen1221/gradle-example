package com.littlefox.security.algorithm.impl;

import com.littlefox.security.algorithm.AbstractCryptic;
import com.littlefox.security.algorithm.CrypticAlgorithm;
import com.littlefox.security.utils.aes.AESUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 AES
 * @author rockychen
 */
@Slf4j
public class AES extends AbstractCryptic implements CrypticAlgorithm {

    public AES(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return AESUtils.encrypt(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES encryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return AESUtils.decrypt(this.secretKey,str);
        } catch (Exception e) {
            log.error("AES decryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }

}
