package com.littlefox.security.utils;

import com.littlefox.security.annotation.Cryptic;
import com.littlefox.security.constant.CrypticConst;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理参数的判断逻辑
 * @author rockychen
 * @version 1.0
 * @date 2020-03-18 15:16
 */
public class CrypticParamUtil {

    /**
     * 查询字段
     * @param fieldValue
     * @param type
     * @param annotation
     * @return
     */
    public static String selectMapField(String fieldValue, String type, Cryptic annotation) {
        switch (annotation.type()){
            case ONLY_ENCRYPT:
                //可切换算法注解
                fieldValue= StringUtils.equalsIgnoreCase(type, CrypticConst.AFTER_SELECT)? CrypticUtil.encryptSelf(fieldValue): CrypticUtil.decryptSelf(fieldValue);
                break;
            default:
                fieldValue= StringUtils.equalsIgnoreCase(type, CrypticConst.AFTER_SELECT)? CrypticUtil.decryptSelf(fieldValue): CrypticUtil.encryptSelf(fieldValue);
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
    public String updateMapField(String fieldValue, String type, Cryptic annotation) {
        switch (annotation.type()){
            case ONLY_ENCRYPT:
                //可切换算法注解
                fieldValue= CrypticUtil.decryptSelf(fieldValue);
                break;
            default:
                fieldValue= CrypticUtil.encryptSelf(fieldValue);
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
    public static <T> void selectField(T t,String type) {
        if (t == null) {
            return;
        }

        //修改批量插入逻辑判断
        if (t instanceof List<?>) {
            List<?> list = (ArrayList<?>) t;
            if(!CollectionUtils.isEmpty(list)) {
                list.forEach(l -> selectField(l,type));
            }
        }

        Field[] allFields= getAllFields(t.getClass());
        try {
            if (allFields != null && allFields.length > 0) {
                for (Field field : allFields) {
                    field.setAccessible(true);
                    if (field.isEnumConstant()){
                        continue;
                    }
                    if (!isJavaClass(field.getType())){
                        selectField(field.get(t), type);
                    }
                    // 判断字段上是否有CrypticField注解，并且字段类型为String
                    if (field.isAnnotationPresent(Cryptic.class) && field.getType().toString().endsWith("String")) {
                        Cryptic anno =field.getAnnotation(Cryptic.class);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT://可切换算法注解
                                field.set(t, StringUtils.equalsIgnoreCase(type, CrypticConst.AFTER_SELECT)? CrypticUtil.encryptSelf(fieldValue): CrypticUtil.decryptSelf(fieldValue));
                                break;
                            default:
                                field.set(t, StringUtils.equalsIgnoreCase(type, CrypticConst.AFTER_SELECT)? CrypticUtil.decryptSelf(fieldValue): CrypticUtil.encryptSelf(fieldValue));
                                break;
                        }
                    } else if (field.getType() == List.class) {
                        List list = (List)field.get(t);
                        if(CollectionUtils.isEmpty(list)) {
                            continue;
                        }
                        list.forEach(i -> selectField(i, type));
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
    public static  <T> void updateField(T t,String type) {
        if (t == null) {
            return;
        }
        //修改批量插入逻辑判断
        if (t instanceof HashMap<?,?>) {
            Map map=(HashMap<?, ?>)t;
            if(map.containsKey("collection")){
                Object obj=map.get("collection");
                updateField(obj,type);
                obj=map.get("collection");
                map.put("collection",obj);
            }
        }

        if (t instanceof ArrayList<?>){
            List<?> list = (ArrayList<?>) t;
            if(!CollectionUtils.isEmpty(list)) {
                list.forEach(l -> updateField(l,type));
            }
        }

        Field[] allFields= getAllFields(t.getClass());

        try {
            if (allFields != null && allFields.length > 0) {
                for (Field field : allFields) {
                    //2019.7.10
                    field.setAccessible(true);
                    if (field.isEnumConstant()){
                        continue;
                    }
                    if (!isJavaClass(field.getType())){
                        updateField(field.get(t), type);
                    }

                    if (field.isAnnotationPresent(Cryptic.class) && field.getType().toString().endsWith("String")) {
                        Cryptic anno =field.getAnnotation(Cryptic.class);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if(StringUtils.isEmpty(fieldValue)) {
                            continue;
                        }
                        switch (anno.type()){
                            case ONLY_ENCRYPT:
                                /*if (!StringUtils.equalsAnyIgnoreCase(type,CrypticConstant.INSERT)){
                                    field.set(t,CrypticUtils.decryptSelf(fieldValue));
                                }*/
                                field.set(t, CrypticUtil.decryptSelf(fieldValue));
                                break;
                            default:field.set(t, CrypticUtil.encryptSelf(fieldValue));break;
                        }
                    } else if (field.getType() == List.class) {
                        //2019.7.10
                        List list = (List)field.get(t);
                        if(CollectionUtils.isEmpty(list)) {
                            continue;
                        }

                        list.forEach(i -> updateField(i, type));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理map参数
     * @param obj
     * @param typeName
     * @param key
     * @param annotation
     */
    public static Object mapFieldIsCrypt(Object obj, String typeName, String key, Cryptic annotation, String... action){
        if (obj==null){
            return null;
        }
        String value= "";
        String type= CrypticConst.STRING;
        if (obj instanceof HashMap<?,?>) {
            Map map=(HashMap<?, ?>)obj;
            if(map.containsKey("collection")){
                Object collection=map.get("collection");
                Object object=mapFieldIsCrypt(collection,typeName,key,annotation);
                ((HashMap<String, Object>)obj).put("collection",object);
                ((HashMap<String, Object>)obj).put("list",object);
                return obj;
            }else {
                value=(String) map.get(key);
            }
            type= CrypticConst.HASHMAP;
        }else if (obj instanceof ArrayList<?>) {
            List<?> list = (ArrayList<?>) obj;
            if(!CollectionUtils.isEmpty(list)) {
                return (List) list.stream().map(o -> {
                    return mapFieldIsCrypt(o,typeName,key,annotation);
                }).collect(Collectors.toList());
            }
        }else if (obj instanceof List<?>) {
            List<?> list = (List<String>) obj;
            if(!CollectionUtils.isEmpty(list)) {
                return (List) list.stream().map(o -> {
                    return mapFieldIsCrypt(o,typeName,key,annotation);
                }).collect(Collectors.toList());
            }
        }else if (obj instanceof String){
            value= (String) obj;
        }

        if (StringUtils.isBlank(value)){
            return null;
        }
        //处理加密参数字符串形式
        String [] str=value.split(",");
        String [] text=new String[str.length];

        for (int i=0;i<str.length;i++){
            text[i]= CrypticParamUtil.selectMapField(str[i],typeName,annotation);
        }
        List<String> cities = Arrays.asList(text);
        String strCommaSeparated = String.join(",", cities);
        if(StringUtils.equalsAnyIgnoreCase(type, CrypticConst.HASHMAP)){
            ((HashMap<String, String>)obj).put(key,strCommaSeparated);
        }else if (StringUtils.equalsAnyIgnoreCase(type, CrypticConst.ARRAYLIST)){
            return (ArrayList<?>) obj;
        }else {
            return strCommaSeparated;
        }
        return obj;
    }

    /**
     * 获取类所有字段
     * @param clz
     * @return
     */
    public static Field[] getAllFields(Class<?> clz){
        Field[] fields= null;
        // 获取父类，判断是否为实体类
        if (clz.getSuperclass() != Object.class) {
            fields =getAllFields(clz.getSuperclass());
        }
        // 获取类中的所有定义字段
        Field[] declaredfields = clz.getDeclaredFields();
        return ArrayUtils.addAll(fields,declaredfields);
    }

    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

}
