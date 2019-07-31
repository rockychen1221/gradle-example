package com.littlefox.security.utils;

import com.littlefox.security.algorithm.AlgorithmFactory;
import com.littlefox.security.algorithm.CrypticAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 默认算法类
 */
@Component
public class AlgorithmConfig {

    //私钥
    @Value("${cryptic.secretKey}")
    private String secretKey;
    //默认算法
    private String algorithm;
    // 加解密开关，从配置获取
    @Value("${cryptic.switch:0}")
    private String algorithmSwitch;
    // 算法实现类
    private CrypticAlgorithm crypticAlgorithm;

    /**
     * 获取默认算法以及算法执行器
     * @param val
     */
    @Value("${cryptic.algorithm:0}")
    public void setAlgorithm(String val) {
        this.algorithm = val;
        this.crypticAlgorithm = getCrypticAlgorithm();
    }

    public CrypticAlgorithm getCrypticAlgorithm() {
        if (this.crypticAlgorithm != null) {
            return this.crypticAlgorithm;
        }
        return this.algorithmSwitch.equals("0")?null:AlgorithmFactory.create(this.secretKey,this.algorithm);
    }

}