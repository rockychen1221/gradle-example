package com.littlefox.cryptic;

/**
 * 加密接口
 * @author rockychen
 */
public interface Cryptic {
    String encryptSelf(String str);
    String decryptSelf(String str);
}
