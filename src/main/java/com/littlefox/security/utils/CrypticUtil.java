package com.littlefox.security.utils;

import com.littlefox.security.crypto.CrypticExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类
 */
public class CrypticUtil {

    /**
     * 动态算法加密
     * @param str
     * @return
     */
    public static String encryptSelf(final String str){
        return cryptic(str,true);
    }


    /**
     * 动态算法解密
     * @param str
     * @return
     */
    public static String decryptSelf(final String str){
        return cryptic(str,false);
    }


    private static String cryptic(String str, boolean isEncrypt){

        if (StringUtils.isBlank(str)){
            return str;
        }
        //处理加密参数字符串形式
        String [] strArrays=str.split(",");
        String [] text = new String[strArrays.length];

        for (int i=0;i<strArrays.length;i++){
            if (isEncrypt){
                text[i]= new CrypticExecutor().getICryptic().encryptSelf(strArrays[i]);
            }else {
                text[i]= new CrypticExecutor().getICryptic().decryptSelf(strArrays[i]);
            }
        }
        List<String> cities = Arrays.asList(text);
        return String.join(",", cities);
    }

}
