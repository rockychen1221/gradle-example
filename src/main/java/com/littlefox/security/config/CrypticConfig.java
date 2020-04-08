package com.littlefox.security.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 默认算法类
 */
@Data
@Component
@ConfigurationProperties(prefix = "cryptic")
public class CrypticConfig {

    //私钥
    @Value("${cryptic.secretKey}")
    private String secretKey;
    //默认算法
    @Value("${cryptic.algorithm}")
    private String algorithm;
    // 加解密开关，从配置获取
    @Value("${cryptic.enable}")
    private boolean enable = true;

}
