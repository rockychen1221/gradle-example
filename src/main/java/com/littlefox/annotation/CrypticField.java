package com.littlefox.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @decription DecryptField
 * <p>字段解密注解</p>
 * @author Yampery
 * @date 2017/10/24 13:05
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrypticField {
    /**
     * ALL （默认） 加解密
     * DECRYPT 解密
     * ENCRYPT 加密
     */
    enum Type{ ALL,DECRYPT,ENCRYPT}

    Type type() default Type.ALL;

}
