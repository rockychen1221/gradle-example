package com.littlefox.utils;

import com.littlefox.cryptic.CrypticFactory;
import com.littlefox.cryptic.CrypticInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 默认算法类
 */
@Component
public class CrypticConfig {

    //私钥
    private String privateKey;
    //默认算法（适合保存到数据库，不会变化的情况）
    private String algorithm;
    // 加解密开关，从配置获取
    private String cryptic_switch;

    // 默认算法实现类
    private CrypticInterface crypticInterface;

    /**
     * 从配置中获取秘钥
     * :默认值填写自己生成的秘钥
     * @param key
     */
    @Value("${cryptic.privatekey:0}")
    public void setPrivateKey(String key){
        this.privateKey = key;
    }

    /**
     * 获取开关
     * 默认为不加密
     * @param val
     */
    @Value("${cryptic.switch:0}")
    public void setCrypticSwitch(String val) {
        this.cryptic_switch = val;
    }

    /**
     * 获取默认算法以及算法执行器
     * @param val
     */
    @Value("${cryptic.algorithm:0}")
    public void setAlgorithm(String val) {
        this.algorithm = val;
        this.crypticInterface= getCrypticInterface();
    }

    public  String getPrivateKey() {
        return privateKey;
    }

    public  String getAlgorithm() {
        return this.algorithm;
    }

    public  String getCrypticSwitch() {
        return this.cryptic_switch;
    }

    public  CrypticInterface getCrypticInterface() {
        if (this.crypticInterface != null) {
            return this.crypticInterface;
        }
        return this.cryptic_switch.equals("0")?null:CrypticFactory.create(this.privateKey,this.algorithm);
    }

}