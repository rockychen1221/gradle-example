package com.littlefox.utils;

import com.littlefox.cryptic.CrypticFactory;
import com.littlefox.cryptic.Cryptic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 默认算法类
 */
@Component
public class CrypticConfig {

    //私钥
    @Value("${cryptic.secretKey}")
    private String secretKey;
    //默认算法
    private String algorithm;
    // 加解密开关，从配置获取
    @Value("${cryptic.switch:0}")
    private String cryptic_switch;

    // 默认算法实现类
    private Cryptic cryptic;

    /**
     * 获取默认算法以及算法执行器
     * @param val
     */
    @Value("${cryptic.algorithm:0}")
    public void setAlgorithm(String val) {
        this.algorithm = val;
        this.cryptic = getCryptic();
    }

    public  String getSecretKey() {
        return secretKey;
    }

    public  String getAlgorithm() {
        return this.algorithm;
    }

    public  String getCrypticSwitch() {
        return this.cryptic_switch;
    }

    public Cryptic getCryptic() {
        if (this.cryptic != null) {
            return this.cryptic;
        }
        return this.cryptic_switch.equals("0")?null:CrypticFactory.create(this.secretKey,this.algorithm);
    }

}