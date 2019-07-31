package com.littlefox.security.algorithm;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Constructor;

/**
 * 加密工厂方法类
 *
 * @author rockychen
 */
@Slf4j
public class AlgorithmFactory {

    public static CrypticAlgorithm create(final String secretKey, final String algorithm) {
        if (algorithm != null) {
            try {
                final Class<?> clazz = Class.forName(AlgorithmEnums.getEnumByKey(algorithm));
                final Constructor<?> constructor = clazz.getConstructor(String.class);
                return (CrypticAlgorithm)constructor.newInstance(secretKey);
            } catch (final Exception ex) {
                log.error("create algorithm engine error",ex);
                throw new RuntimeException("create algorithm engine error", ex);
            }
        }
        return null;
    }
}
