package com.littlefox.security.crypto;

import com.littlefox.security.utils.CrypticFactory;

/**
 * 秘密执行器类，负责选择正确的算法加解密数据
 *
 * @author rockychen
 */
public class CrypticExecutor {

    /**
     * 获取当前使用的算法，如没有返回默认配置算法
     * @return
     */
    public ICryptic getICryptic() {
        return CrypticFactory.create("4BB90812C2B9B0882A6FA7C203E4717F", "AES");
    }

}
