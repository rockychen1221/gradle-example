package com.littlefox.interceptor;

import com.littlefox.annotation.CrypticField;
import com.littlefox.cryptic.CryptPojoUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 全局拦截数据库创建和更新
 * <p>
 * Signature 对应 Invocation 构造器, type 为 Invocation.Object, method 为 Invocation.Method, args 为 Invocation.Object[]
 * method 对应的 update 包括了最常用的 insert/update/delete 三种操作, 因此 update 本身无法直接判断sql为何种执行过程
 * args 包含了其余多有的操作信息, 按数组进行存储, 不同的拦截方式有不同的参数顺序, 具体看type接口的方法签名, 然后根据签名解析, 参见官网
 *
 * @link http://www.mybatis.org/mybatis-3/zh/configuration.html#plugins 插件
 * <p>
 * MappedStatement 包括了SQL具体操作类型, 需要通过该类型判断当前sql执行过程
 */
@Component
@Intercepts({
        @Signature(type=Executor.class,method="update",args={MappedStatement.class,Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class ExecutorInterceptor implements Interceptor {

    private final Logger log = LoggerFactory.getLogger(ExecutorInterceptor.class);

    private String CRYPTIC_SWITCH="1";

    /**
     *
     * @param obj
     * @param typeName
     */
    private void  fieldIsCrypt(Object obj,String typeName){
        Field[] fields = obj.getClass().getDeclaredFields();

        if (obj instanceof ArrayList<?>) {
            fields =((ArrayList) obj).get(0).getClass().getDeclaredFields();
        }

        int len;
        if (null != fields && 0 < (len = fields.length)) {
            // 标记是否有解密注解
            boolean isD = false;
            for (int i = 0; i < len; i++) {
                /**
                 * 由于返回的是同一种类型列表，因此这里判断出来之后可以保存field的名称
                 *                      * 之后处理所有对象直接按照field名称查找Field从而改之即可
                 *                      * 有可能该类存在多个注解字段，所以需要保存到数组（项目中目前最多是2个）
                 *                      * @TODO 保存带DecryptField注解的字段名称到数组，按照名称获取字段并解密
                 */
                if (fields[i].isAnnotationPresent(CrypticField.class)) {
                    isD = true;
                    break;
                }
            } /// for end ~
            if (isD) {  // 将含有DecryptField注解的字段解密
                if (StringUtils.equalsIgnoreCase(typeName, "param")){
                    CryptPojoUtils.selectParamField(obj);
                }
                if (StringUtils.equalsIgnoreCase(typeName, "result")){
                    List<?> list = (ArrayList<?>) obj;
                    list.forEach(l -> CryptPojoUtils.selectResultField(l));
                }
            }
        } /// if end ~
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 根据签名指定的args顺序获取具体的实现类
        // 1. 获取MappedStatement实例, 并获取当前SQL命令类型
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType commandType = ms.getSqlCommandType();

        // 2. 获取当前正在被操作的类, 有可能是Java Bean, 也可能是普通的操作对象, 比如普通的参数传递
        // 普通参数, 即是 @Param 包装或者原始 Map 对象, 普通参数会被 Mybatis 包装成 Map 对象
        // 即是 org.apache.ibatis.binding.MapperMethod$ParamMap
        Object parameter = invocation.getArgs()[1];
        // 获取拦截器指定的方法类型, 通常需要拦截 update
        String methodName = invocation.getMethod().getName();
        log.info("NormalPlugin, methodName; {}, commandType: {}", methodName, commandType);
        //获取执行的sql
        BoundSql sql = ms.getBoundSql(parameter);
        log.info("sql is: {}", sql.getSql());

        /**
         * 拦截批量插入操作不仅繁琐，而且为了通用逐一通过反射加密不妥
         * 如果有批量操作，最好在传递参数之前，向list中添加之前就加密
         */
        if (!"0".equals(CRYPTIC_SWITCH)) {
            if (StringUtils.equalsIgnoreCase("update", methodName)
                    || StringUtils.equalsIgnoreCase("insert", methodName)) {
                CryptPojoUtils.updateField(parameter);
                return invocation.proceed();
            }
        }

        /**
         * 查询处理
         */
        Object[] args = invocation.getArgs();
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if(args.length == 4){
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        /**
         * 判断pagehelper分页后参数，参数形式为map
         */
        if (parameter instanceof HashMap<?,?>) {
            /*Map newMap = new HashMap(((HashMap) parameter).size());
            String className= ms.getParameterMap().getType().getName();
            Class clazz=Class.forName(className);

            Field[] fields=clazz.getDeclaredFields();
            Object obj = clazz.newInstance();

            ((HashMap<?, ?>) parameter).forEach((k,v)->{
                for (int i = 0; i < fields.length; i++) {
                    if(fields[i].getName().equals(k)) {
                        newMap.put(k,v);
                        break;
                    }
                }
            });
            org.apache.commons.beanutils.BeanUtils.populate(obj, newMap);
            fieldIsCrypt(obj,"param");
            Map map= new org.apache.commons.beanutils.BeanMap(obj);
            ((HashMap<?, ?>) parameter).putAll(map);*/
        }else {
            fieldIsCrypt(parameter,"param");
        }

        //TODO 自己要进行的各种处理
        //注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
        Object returnValue = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);

        if (returnValue instanceof ArrayList<?>) {
            List<?> list = (ArrayList<?>) returnValue;
            if (null == list || 1 > list.size())
                return returnValue;
            Object obj = list.get(0);
            if (null == obj)  // 这里虽然list不是空，但是返回字符串等有可能为空
                return returnValue;

            fieldIsCrypt(list,"result");
        } /// if end ~

        //TODO 自己要进行的各种处理
        //注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
