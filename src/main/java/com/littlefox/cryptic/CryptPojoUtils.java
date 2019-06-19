package com.littlefox.cryptic;

import com.littlefox.annotation.CrypticField;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;

public class CryptPojoUtils {

    /**
     * 查询处理
     * @param t
     * @param type
     * @param <T>
     */
    public static <T> void selectField(T t,String type) {
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
                                field.set(t,StringUtils.equalsIgnoreCase(type,"result")?CrypticUtils.getInstance().encrypt(fieldValue):CrypticUtils.getInstance().decrypt(fieldValue));
                                break;
                            default:
                                field.set(t,StringUtils.equalsIgnoreCase(type,"result")?CrypticUtils.getInstance().decrypt(fieldValue):CrypticUtils.getInstance().encrypt(fieldValue));
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
    public static <T> void updateField(T t) {
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
                            default:field.set(t, CrypticUtils.getInstance().encrypt(fieldValue));break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // return t;
    }
}
