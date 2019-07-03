package com.littlefox.cryptic;

import com.littlefox.annotation.CrypticField;
import com.littlefox.constant.CrypticConstant;
import com.littlefox.utils.CrypticUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 算法执行器类，负责选择正确的算法加解密数据
 *
 * @author rockychen
 */
public class CrypticExecutor {

    private static Cryptic cryptic;

    /**
     * 数据执行器
     *
     */
    public CrypticExecutor(final Cryptic cryptic) {
        CrypticExecutor.cryptic = cryptic;
    }

    public static String dynamicEncrypt(String str){
        final Cryptic dynamicCryptic = getDynamicCrypticInterface();
        if (ObjectUtils.isEmpty(dynamicCryptic)) {
            return str;
        }
        return dynamicCryptic.encryptSelf(str);
    }

    public static String dynamicDecrypt(String str){
        final Cryptic dynamicCryptic = getDynamicCrypticInterface();
        if (ObjectUtils.isEmpty(dynamicCryptic)) {
            return str;
        }
        return dynamicCryptic.decryptSelf(str);
    }

    /**
     * 查询字段
     * @param fieldValue
     * @param type
     * @param annotation
     * @return
     */
    public String selectMapField(String fieldValue, String type, CrypticField annotation) {
        final Cryptic defaultCryptic = getCrypticInterface();

        if (defaultCryptic == null) {
            throw new RuntimeException("未指定算法对象!");
        }

        switch (annotation.type()){
            case ONLY_ENCRYPT://可切换算法注解
                //fieldValue=StringUtils.equalsIgnoreCase(type, CrypticConstant.AFTER_SELECT)? CrypticUtils.encryptSelf(fieldValue):CrypticUtils.decryptSelf(fieldValue);
                fieldValue=StringUtils.equalsIgnoreCase(type, CrypticConstant.AFTER_SELECT)? defaultCryptic.encryptSelf(fieldValue):defaultCryptic.decryptSelf(fieldValue);
                break;
            default:
                fieldValue=StringUtils.equalsIgnoreCase(type,CrypticConstant.AFTER_SELECT)?defaultCryptic.decryptSelf(fieldValue):defaultCryptic.encryptSelf(fieldValue);
                break;
        }
        return fieldValue;
    }

    /**
     * 修改字段
     * @param fieldValue
     * @param type
     * @param annotation
     * @return
     */
    public String updateMapField(String fieldValue, String type, CrypticField annotation) {
        final Cryptic defaultCryptic = getCrypticInterface();
        if (defaultCryptic == null) {
            throw new RuntimeException("未指定算法对象!");
        }
        switch (annotation.type()){
            case ONLY_ENCRYPT://可切换算法注解
                fieldValue=CrypticUtils.decryptSelf(fieldValue);
                break;
            default:
                fieldValue= defaultCryptic.encryptSelf(fieldValue);
                break;
        }
        return fieldValue;
    }

    /**
     * 查询处理
     * @param t
     * @param type
     * @param <T>
     */
    public <T> void selectField(T t,String type) {
        final Cryptic defaultCryptic = getCrypticInterface();

        if (defaultCryptic == null) {
            throw new RuntimeException("未指定算法对象!");
        }

        Field[] allFields=getAllFields(t.getClass());
        try {
            if (allFields != null && allFields.length > 0) {
                for (Field field : allFields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(CrypticField.class) && field.getType().toString().endsWith("String")) {
                        CrypticField anno =field.getAnnotation(CrypticField.class);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT://可切换算法注解
                                //field.set(t,StringUtils.equalsIgnoreCase(type, CrypticConstant.AFTER_SELECT)?CrypticUtils.encryptSelf(fieldValue):CrypticUtils.decryptSelf(fieldValue));
                                field.set(t,StringUtils.equalsIgnoreCase(type, CrypticConstant.AFTER_SELECT)?defaultCryptic.encryptSelf(fieldValue):defaultCryptic.decryptSelf(fieldValue));
                                break;
                            default:
                                field.set(t,StringUtils.equalsIgnoreCase(type,CrypticConstant.AFTER_SELECT)?defaultCryptic.decryptSelf(fieldValue):defaultCryptic.encryptSelf(fieldValue));
                                break;
                        }
                    } else if (field.getType() == List.class) {
                        List list = (List)field.get(t);
                        for (int i = 0; i < list.size(); i++) {
                            selectField(list.get(i), type);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改时对注解字段进行加解密处理（对于仅加密注解字段不用处理）
     * @param t
     * @param <T>
     */
    public <T> void updateField(T t,String type) {

        final Cryptic defaultCryptic = getCrypticInterface();

        if (defaultCryptic == null || defaultCryptic ==null) {
            throw new RuntimeException("未指定算法对象!");
        }
        //Field[] declaredFields = t.getClass().getDeclaredFields();
        Field[] allFields=getAllFields(t.getClass());
        try {
            if (allFields != null && allFields.length > 0) {
                for (Field field : allFields) {
                    if (field.isAnnotationPresent(CrypticField.class) && field.getType().toString().endsWith("String")) {
                        CrypticField anno =field.getAnnotation(CrypticField.class);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT:
                                if (!StringUtils.equalsAnyIgnoreCase(type,CrypticConstant.INSERT)){
                                    field.set(t,defaultCryptic.decryptSelf(fieldValue));
                                }
                                //field.set(t,CrypticUtils.decryptSelf(fieldValue));
                                break;
                            default:field.set(t, defaultCryptic.encryptSelf(fieldValue));break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Cryptic getCrypticInterface() {
        return cryptic;
    }

    private static Cryptic getDynamicCrypticInterface() {
        return null;
    }

    /**
     * 获取类所有字段
     * @param c
     * @return
     */
    private static Field[] getAllFields(Class<?> c){
        Field[] fields= null;
        // 获取父类，判断是否为实体类
        if (c.getSuperclass() != Object.class) {
            fields =getAllFields(c.getSuperclass());
        }
        // 获取类中的所有定义字段
        Field[] declaredfields = c.getDeclaredFields();
        return ArrayUtils.addAll(fields,declaredfields);
    }

}
