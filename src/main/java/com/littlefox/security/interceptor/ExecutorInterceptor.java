package com.littlefox.security.interceptor;

import com.littlefox.security.annotation.Cryptic;
import com.littlefox.security.constant.CrypticConst;
import com.littlefox.security.utils.CrypticParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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
@Slf4j
@Component
@Intercepts({
        @Signature(type= Executor.class,method="update",args={MappedStatement.class,Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@ConditionalOnProperty(prefix = "cryptic", name = "enable", havingValue = "true")
public class ExecutorInterceptor implements Interceptor {

    private String countSuffix = "_COUNT";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        final String methodName = invocation.getMethod().getName();
        Executor executor = (Executor) invocation.getTarget();
        Object[] args= invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        Object parameter = args[1];
        String namespace = mappedStatement.getId();
        String className = namespace.substring(0,namespace.lastIndexOf("."));
        String methedName = namespace.substring(namespace.lastIndexOf(".") + 1);

        Method[] methods = Class.forName(className).getMethods();
        //过滤重写方法
        List<Method> methodList = Arrays.stream(methods).filter(m -> StringUtils.equals(m.getName(), methedName)).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(methodList)) {
            //针对pagehelper插件处理
            if (methedName.indexOf(countSuffix)>-1) {
                methodList = Arrays.stream(methods).filter(m -> StringUtils.equals(m.getName() + countSuffix, methedName)).collect(Collectors.toList());
            }
            for (int i = 0; i < methodList.size(); i++) {
                //过滤重写方法
                if (methodList.size() > 1 && i == 0) {
                    continue;
                }

                //获取重写方法
                Method method = methodList.get(0);

                Parameter[] params=method.getParameters();
                for(Parameter p : params) {
                    //判读是否Java基本数据类型 2019.7.10
                    if (!CrypticParamUtil.isJavaClass(p.getType())||p.getType()== List.class){
                        if(StringUtils.equalsIgnoreCase(CrypticConst.QUERY, methodName)){
                            CrypticParamUtil.selectField(parameter, CrypticConst.BEFORE_SELECT);
                        }else if (StringUtils.equalsIgnoreCase(CrypticConst.UPDATE, methodName)) {
                            // 修改直接返回
                            CrypticParamUtil.updateField(parameter,commandType.name());
                        }
                    }else {
                        Annotation[] declaredAnnotations=p.getDeclaredAnnotations();
                        //默认取方法参数名
                        final String[] paramKey = {p.getName()};
                        Arrays.stream(declaredAnnotations).forEach(annotation -> {
                            if (StringUtils.equalsAnyIgnoreCase(annotation.annotationType().getSimpleName(),"Param")){
                                paramKey[0] =p.getDeclaredAnnotation(Param.class).value();
                            }
                        });
                        Cryptic annotation=p.getDeclaredAnnotation(Cryptic.class);
                        if (annotation!=null) {
                            if(StringUtils.equalsIgnoreCase(CrypticConst.QUERY, methodName)){
                                parameter = CrypticParamUtil.mapFieldIsCrypt(parameter, CrypticConst.BEFORE_SELECT,paramKey[0],annotation);
                            }else if (StringUtils.equalsIgnoreCase(CrypticConst.UPDATE, methodName)) {
                                // 修改直接返回
                                parameter = CrypticParamUtil.mapFieldIsCrypt(parameter, CrypticConst.UPDATE,paramKey[0],annotation,commandType.name());
                            }
                        }
                    }
                }
            }
        }
        //BoundSql sql = mappedStatement.getBoundSql(parameter);
        //log.info(sql.getSql());

        if (StringUtils.equalsIgnoreCase(CrypticConst.UPDATE, methodName)) {
            // 修改直接返回
            return executor.update(mappedStatement,parameter);
        }

        /**
         * 查询处理
         */
        ResultHandler resultHandler = (ResultHandler) args[3];
        final RowBounds rowBounds = (RowBounds) args[2];

        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if(args.length == 4){
            //4 个参数时
            boundSql = mappedStatement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        //TODO 自己要进行的各种处理
        //注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
        Object returnValue = executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);

        if (returnValue instanceof ArrayList<?>) {
            List<?> list = (ArrayList<?>) returnValue;
            if (null == list || 1 > list.size())
                return returnValue;
            Object obj = list.get(0);
            if (null == obj)  // 这里虽然list不是空，但是返回字符串等有可能为空
                return returnValue;

            CrypticParamUtil.selectField(list, CrypticConst.AFTER_SELECT);
        }
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
