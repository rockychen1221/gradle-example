package com.littlefox.cryptic;

import java.lang.reflect.Constructor;

/**
 * 加密工厂方法类
 *
 * @author rockychen
 */
public class CrypticFactory {

    public static CrypticInterface create(final String privateKey, final String algorithm) {
        if (algorithm != null) {
            try {
                final Class<?> clazz = Class.forName(CrypticTypeEnums.getEnumByKey(algorithm).getValue());
                final Constructor<?> constructor = clazz.getConstructor(String.class, String.class);
                return (CrypticInterface)constructor.newInstance(privateKey, algorithm);
            } catch (final Exception ex) {
                throw new RuntimeException("create cryptic engine error", ex);
            }
        }
        return null;
    }
}
