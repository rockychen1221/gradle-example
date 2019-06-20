package com.littlefox.utils;

import com.littlefox.cryptic.CrypticFactory;
import com.littlefox.cryptic.CrypticInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置文件工具类
 *
 * 说明：
 *   1. 默认加载以下文件：encrypt.properties、properties/config-common.properties、properties/config.properties、
 *   config.properties、application.properties，当文件存在且包含 encrypt.private.key 属性时认为是配置文件，会以该
 *   配置文件中的值为准，如果不存在以上文件则使用默认值，用户也可以通过 ConfigUtil.bundleNames() 方法来指定配置文件
 *   的名称，这种情况以指定的为准，若文件不存在则使用默认值
 *   2. 自定义私钥：在以上文件中新增属性 encrypt.private.key=私钥
 *      自定义算法：在以上文件中新增属性 encrypt.class.name=类的全路径
 *
 * @author trang
 */
@Component
public final class CrypticUtils {

    private static final Logger log = LoggerFactory.getLogger(CrypticUtils.class);

    //私钥
    private static String privateKey;
    //默认算法（适合保存到数据库，不会变化的情况）
    private static String defalutAlgorithm;

    // 加解密开关，从配置获取
    private static String cryptic_switch;

    // 默认算法
    private CrypticInterface crypticInterface;

    /**
     * 从配置中获取秘钥
     * :默认值填写自己生成的秘钥
     * @param key
     */
    @Value("${cryptic.privatekey:0}")
    public void setPrivateKey(String key){
        CrypticUtils.privateKey = key;
    }

    /**
     * 获取开关
     * 默认为不加密
     * @param val
     */
    @Value("${cryptic.switch:0}")
    public void setCrypticSwitch(String val) {
        CrypticUtils.cryptic_switch = val;
    }

    /**
     * 获取算法
     * 默认为不加密
     * @param val
     */
    @Value("${cryptic.defalutAlgorithm:0}")
    public void setDefalutAlgorithm(String val) {
        CrypticUtils.defalutAlgorithm = val;
    }


    private CrypticInterface getCrypticInterface() {
        if (this.crypticInterface != null) {
            return this.crypticInterface;
        }
        return CrypticFactory.create(CrypticUtils.privateKey,CrypticUtils.defalutAlgorithm);
    }



}