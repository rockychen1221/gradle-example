package com.littlefox.security.crypto;

/**
 * 神秘算法顶层接口
 * @author rockychen
 */
public interface ICryptic {
    String encryptSelf(String str);
    String decryptSelf(String str);
}
