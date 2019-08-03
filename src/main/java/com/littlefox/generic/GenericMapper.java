/*
 * All rights Reserved, Designed By DataDriver
 * Copyright:    DataDriver.Inc
 * Company:      Zhuo Wo Infomation Technology (ShangHai) CO.LTD
 */
package com.littlefox.generic;

/**
 * GenericMapper Dao的顶级接口
 *
 * @Action 所有自定义Dao的顶级接口, 封装常用的增删查改操作,
 * @Description Model : 代表数据库中的表 映射的Java对象类型;PK :代表对象的主键类型
 */
public interface GenericMapper<Model, PK> {

    /**
     * 插入对象
     *
     * @param model 对象
     */
    int insertSelective(Model model);

    /**
     * 更新对象
     *
     * @param model 对象
     */
    int updateByPrimaryKeySelective(Model model);

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    int deleteByPrimaryKey(PK id);

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return
     */
    Model selectByPrimaryKey(PK id);

}
