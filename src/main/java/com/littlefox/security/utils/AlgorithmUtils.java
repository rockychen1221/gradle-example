package com.littlefox.security.utils;


import com.littlefox.security.algorithm.AlgorithmExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 算法工具类
 */
public class AlgorithmUtils {

    /**
     * 动态算法加密
     * @param str
     * @return
     */
    public static String encryptSelf(String str){
        return cryptic(str,"encryptSelf");
    }

    /**
     * 动态算法解密
     * @param str
     * @return
     */
    public static String decryptSelf(String str){
        return cryptic(str,"decryptSelf");
    }


    private static String cryptic(String str,String method){

        if (StringUtils.isBlank(str)){
            return str;
        }
        //处理加密参数字符串形式
        String [] strArrays=str.split(",");
        String [] text=new String[strArrays.length];

        for (int i=0;i<strArrays.length;i++){
            if (StringUtils.equalsAnyIgnoreCase(method,"encryptSelf")){
                text[i]= AlgorithmExecutor.dynamicEncrypt(strArrays[i]);
            }else if (StringUtils.equalsAnyIgnoreCase(method,"decryptSelf")){
                text[i]= AlgorithmExecutor.dynamicDecrypt(strArrays[i]);
            }
        }
        return String.join(",", Arrays.asList(text));
    }
}