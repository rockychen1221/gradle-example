package com.littlefox.security.algorithm.impl;

import com.littlefox.security.algorithm.AbstractCryptic;
import com.littlefox.security.algorithm.CrypticAlgorithm;
import com.littlefox.security.utils.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 SM4CBC
 * @author rockychen
 */
@Slf4j
public class SM4CBC extends AbstractCryptic implements CrypticAlgorithm {

    public SM4CBC(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).encryptData_CBC(str);
        }catch (Exception e){
            log.error("SM4CBC encryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).decryptData_CBC(str);
        }catch (Exception e){
            log.error("SM4CBC decryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }

}
