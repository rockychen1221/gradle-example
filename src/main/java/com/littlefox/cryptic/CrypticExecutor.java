package com.littlefox.cryptic;

/**
 * 数据执行器类，负责选择正确的报表查询器并获取数据，最终转化为成报表的数据集
 *
 * @author tomdeng
 */
public class CrypticExecutor {

    private String privateKey;
    private String algorithm;
    private CrypticInterface cryptic;

    /**
     * 数据执行器
     *
     */
    public CrypticExecutor(final CrypticInterface crypticInterface) {
        this.cryptic = crypticInterface;
    }

    public CrypticExecutor(final String privateKey,final String algorithm) {
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
        final CrypticInterface cryptic = this.getCrypticInterface();
        if (cryptic == null) {
            throw new RuntimeException("未指定算法对象!");
        }
        cryptic.selectField(t,type);
    }

    /**
     * 修改时对注解字段进行加解密处理（对于仅加密注解字段不用处理）
     * @param t
     * @param <T>
     */
    public <T> void updateField(T t) {
        final CrypticInterface cryptic = this.getCrypticInterface();
        if (cryptic == null) {
            throw new RuntimeException("未指定算法对象!");
        }
        cryptic.updateField(t);
    }

    private CrypticInterface getCrypticInterface() {
        if (this.cryptic != null) {
            return this.cryptic;
        }
        return CrypticFactory.create(this.privateKey, this.algorithm);
    }
}
