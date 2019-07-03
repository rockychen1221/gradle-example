package com.littlefox.cryptic;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Constructor;

/**
 * 加密工厂方法类
 *
 * @author rockychen
 */
@Slf4j
public class CrypticFactory {

    public static Cryptic create(final String secretKey, final String algorithm) {
        if (algorithm != null) {
            try {
                final Class<?> clazz = Class.forName(CrypticTypeEnums.getEnumByKey(algorithm));
                final Constructor<?> constructor = clazz.getConstructor(String.class);
                return (Cryptic)constructor.newInstance(secretKey);
            } catch (final Exception ex) {
                log.error("create CrypticInterface engine error",ex);
                throw new RuntimeException("create CrypticInterface engine error", ex);
            }
        }
        return null;
    }
}
