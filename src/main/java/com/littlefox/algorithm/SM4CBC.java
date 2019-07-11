package com.littlefox.algorithm;

import com.littlefox.cryptic.AbstractCryptic;
import com.littlefox.cryptic.Cryptic;
import com.littlefox.utils.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密 SM4CBC
 * @author rockychen
 */
@Slf4j
public class SM4CBC extends AbstractCryptic implements Cryptic {

    public SM4CBC(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).encryptData_CBC(str);
        }catch (Exception e){
            log.error("SM4CBC encryptSelf Error : {0}",str);
            return super.decryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            return new SM4Utils(this.secretKey).decryptData_CBC(str);
        }catch (Exception e){
            log.error("SM4CBC decryptSelf Error : {0}",str);
            return super.decryptSelf(str);
        }
    }

}
