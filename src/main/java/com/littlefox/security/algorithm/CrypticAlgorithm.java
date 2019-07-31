package com.littlefox.security.algorithm;

/**
 * 神秘算法顶层接口
 * @author rockychen
 */
public interface CrypticAlgorithm {
    String encryptSelf(String str);
    String decryptSelf(String str);
}
