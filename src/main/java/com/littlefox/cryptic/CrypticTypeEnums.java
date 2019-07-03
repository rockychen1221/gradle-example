package com.littlefox.cryptic;

import org.apache.commons.lang3.StringUtils;

/**
 * 算法枚举
 * @author rockychen
 */
public enum CrypticTypeEnums{

    AES("AES", "com.littlefox.algorithm.AES"),
    SM3("SM3", "com.littlefox.algorithm.SM3"),
    SM4CBC("SM4CBC", "com.littlefox.algorithm.SM4CBC"),
    SM4ECB("SM4ECB", "com.littlefox.algorithm.SM4ECB");

    CrypticTypeEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public static String getEnumByKey(String key){
        for (int i = 0; i < CrypticTypeEnums.values().length; i++) {
            if(StringUtils.equals(key,CrypticTypeEnums.values()[i].getKey())){
                return CrypticTypeEnums.values()[i].getValue();
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
