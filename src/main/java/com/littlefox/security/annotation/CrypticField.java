package com.littlefox.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加/解密注解（应用于字段，方法参数上）
 * @author rockychen
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CrypticField {
    /**
     * ONLY_ENCRYPT 仅加密,数据库存储为明文，查询出来加密，通过该值进行查询前会先解密
     * ENCRYPT （默认）加解密,数据库存储为密文，查询解密展示，插入时加密
     */
    enum Type{ ENCRYPT,ONLY_ENCRYPT}

    Type type() default Type.ENCRYPT;

}
