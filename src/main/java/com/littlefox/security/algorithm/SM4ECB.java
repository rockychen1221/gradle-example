package com.littlefox.security.algorithm;

import com.littlefox.security.crypto.AbstractICryptic;
import com.littlefox.security.utils.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 SM4CBC
 * @author rockychen
 */
@Slf4j
public class SM4ECB extends AbstractICryptic {

    public SM4ECB(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).encryptData_ECB(str);
        }catch (Exception e){
            log.error("SM4ECB decryptSelf Error : {}",str);
            return super.encryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).decryptData_ECB(str);
        }catch (Exception e){
            log.error("SM4ECB decryptSelf Error : {}",str);
            return super.decryptSelf(str);
        }
    }
}
