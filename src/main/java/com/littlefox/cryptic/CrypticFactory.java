package com.littlefox.cryptic;

import java.lang.reflect.Constructor;

/**
 * 加密工厂方法类
 *
 * @author tomdeng
 */
public class CrypticFactory {
/*
    public static CrypticInterface create(final ReportDataSource dataSource) {
        return create(dataSource, null);
    }
*/

    public static CrypticInterface create(final String privateKey, final String defalutAlgorithm) {
        if (defalutAlgorithm != null) {
            try {
                final Class<?> clazz = Class.forName(defalutAlgorithm);
                final Constructor<?> constructor = clazz.getConstructor(String.class, String.class);
                return (CrypticInterface)constructor.newInstance(privateKey, defalutAlgorithm);
            } catch (final Exception ex) {
                throw new RuntimeException("create cryptic engine error", ex);
            }
        }
        return null;
    }
}
