package com.littlefox.security.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 算法枚举
 * @author rockychen
 */
public enum AlgorithmEnums{

    AES("AES", "com.littlefox.security.algorithm.AES"),
    SM3("SM3", "com.littlefox.security.algorithm.SM3"),
    SM4CBC("SM4CBC", "com.littlefox.security.algorithm.SM4CBC"),
    SM4ECB("SM4ECB", "com.littlefox.security.algorithm.SM4ECB");

    AlgorithmEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public static String getEnumByKey(String key){
        for (int i = 0; i < AlgorithmEnums.values().length; i++) {
            if(StringUtils.equals(key,AlgorithmEnums.values()[i].getKey())){
                return AlgorithmEnums.values()[i].getValue();
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
