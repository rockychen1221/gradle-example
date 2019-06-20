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
     * 选择正确的报表查询器并获取数据，最终转化为成报表的数据集
     *
     * @return ReportDataSet报表数据集对象
     */
    public void execute(final String paramField) {
        final CrypticInterface cryptic = this.getCrypticInterface();
        if (cryptic == null) {
            throw new RuntimeException("未指定报表查询器对象!");
        }

    }

    private CrypticInterface getCrypticInterface() {
        if (this.cryptic != null) {
            return this.cryptic;
        }
        return CrypticFactory.create(this.privateKey, this.algorithm);
    }
}
