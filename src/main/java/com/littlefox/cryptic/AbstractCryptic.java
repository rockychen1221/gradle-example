package com.littlefox.cryptic;

import com.littlefox.annotation.CrypticField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author rockychen
 */
public abstract class AbstractCryptic {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final String privateKey;
    protected final String algorithm;

    protected AbstractCryptic(final String privateKey, final String algorithm) {
        this.privateKey = privateKey;
        this.algorithm = algorithm;
    }

    /**
     * 查询处理
     * @param t
     * @param type
     * @param <T>
     */
    public <T> void selectField(T t,String type) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(CrypticField.class) && field.getType().toString().endsWith("String")) {
                        CrypticField anno =field.getAnnotation(CrypticField.class);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT:
                                field.set(t,StringUtils.equalsIgnoreCase(type,"result")? encryptSelf(fieldValue):decryptSelf(fieldValue));
                                break;
                            default:
                                field.set(t,StringUtils.equalsIgnoreCase(type,"result")?decryptSelf(fieldValue):encryptSelf(fieldValue));
                                break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // return t;
    }

    /**
     * 修改时对注解字段进行加解密处理（对于仅加密注解字段不用处理）
     * @param t
     * @param <T>
     */
    public <T> void updateField(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(CrypticField.class) && field.getType().toString().endsWith("String")) {
                        CrypticField anno =field.getAnnotation(CrypticField.class);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT:break;
                            default:field.set(t, encryptSelf(fieldValue));break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // return t;
    }

    public String encryptSelf(String str){
        return str;
    }
    public String decryptSelf(String str){
        return str;
    }

}
