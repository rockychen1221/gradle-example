/*
 * All rights Reserved, Designed By DataDriver
 * Copyright: DataDriver.Inc
 * Company: Zhuo Wo Infomation Technology (ShangHai) CO.LTD
 */
package com.littlefox.cryptic;

import org.apache.commons.lang.StringUtils;

public enum CrypticTypeEnums {

    AES("AES", "版本session清除"), SM2("SM2", "新增"), SM3("SM3", "修改"), SM4("SM4", "物理删除");

    CrypticTypeEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public static CrypticTypeEnums getEnumByKey(String key){
        for (int i = 0; i < CrypticTypeEnums.values().length; i++) {
            if(StringUtils.equals(key,CrypticTypeEnums.values()[i].getKey())){
                return CrypticTypeEnums.values()[i];
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
