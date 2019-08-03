package com.littlefox.security.interceptor;

import com.littlefox.security.algorithm.AlgorithmExecutor;
import com.littlefox.security.annotation.CrypticField;
import com.littlefox.security.constant.CrypticConstant;
import com.littlefox.security.utils.AlgorithmConfig;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截器处理
 * @author rockychen
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type= Executor.class,method="update",args={MappedStatement.class,Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class ExecutorInterceptor implements Interceptor {

    @Autowired
    private AlgorithmConfig algorithmConfig;

    /**
     * 处理map or string参数
     * @param obj
     * @param typeName
     * @param key
     * @param annotation
     */
    private Object mapFieldIsCrypt(Object obj, String typeName, String key, CrypticField annotation, String... action){
        if (obj==null){
            return null;
        }
        String value= "";
        String type=CrypticConstant.STRING;
        if (obj instanceof HashMap<?,?>) {
            Map map=(HashMap<?, ?>)obj;
            if(map.containsKey("collection")){
                Object collection=map.get("collection");
                Object object=mapFieldIsCrypt(collection,typeName,key,annotation);
                ((HashMap<String, Object>)obj).put("collection",object);
                ((HashMap<String, Object>)obj).put("list",object);
                return obj;
            }else {
                value=(String) map.get(key);
            }
            type=CrypticConstant.HASHMAP;
        }else if (obj instanceof ArrayList<?>) {
            List<?> list = (ArrayList<?>) obj;
            if(!CollectionUtils.isEmpty(list)) {
                return (List) list.stream().map(o -> {
                    return mapFieldIsCrypt(o,typeName,key,annotation);
                }).collect(Collectors.toList());
            }
            type=CrypticConstant.ARRAYLIST;
        }else if (obj instanceof List<?>) {
            List<?> list = (List<String>) obj;
            if(!CollectionUtils.isEmpty(list)) {
                return (List) list.stream().map(o -> {
                    return mapFieldIsCrypt(o,typeName,key,annotation);
                }).collect(Collectors.toList());
            }
            type=CrypticConstant.ARRAYLIST;
        }else if (obj instanceof String){
            value= (String) obj;
        }

        if (StringUtils.isBlank(value)){
            return null;
        }
        //处理加密参数字符串形式
        String [] str=value.split(",");
        String [] text=new String[str.length];

        for (int i=0;i<str.length;i++){
            text[i]=new AlgorithmExecutor(algorithmConfig.getCrypticAlgorithm()).selectMapField(str[i],typeName,annotation);
        }
        List<String> cities = Arrays.asList(text);
        String strCommaSeparated = String.join(",", cities);

        if(StringUtils.equalsAnyIgnoreCase(type,CrypticConstant.HASHMAP)){
            ((HashMap<String, String>)obj).put(key,strCommaSeparated);
        }else {
            return strCommaSeparated;
        }
        return obj;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (ObjectUtils.isEmpty(algorithmConfig.getCrypticAlgorithm())) {
            return invocation.proceed();
        }

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

        if (methodList.size()>0||methedName.indexOf(CrypticConstant._COUNT)>-1){
            //针对pagehelper插件处理
            if (methedName.indexOf(CrypticConstant._COUNT)>-1){
                methodList = Arrays.stream(methods).filter(m -> StringUtils.equals(m.getName()+CrypticConstant._COUNT, methedName)).collect(Collectors.toList());
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
                    if(!AlgorithmExecutor.isJavaClass(p.getType())||p.getType().getTypeName().indexOf("model")>-1||p.getName().equals("model")||p.getType()== List.class&&p.getDeclaredAnnotation(CrypticField.class)==null){
                        if(StringUtils.equalsIgnoreCase(CrypticConstant.QUERY, methodName)){
                            new AlgorithmExecutor(algorithmConfig.getCrypticAlgorithm()).selectField(parameter,CrypticConstant.BEFORE_SELECT);
                        }else if (StringUtils.equalsIgnoreCase(CrypticConstant.UPDATE, methodName)) {
                            // 修改直接返回
                            new AlgorithmExecutor(algorithmConfig.getCrypticAlgorithm()).updateField(parameter,commandType.name());
                        }
                    }else {
                        Annotation[] declaredAnnotations=p.getDeclaredAnnotations();
                        //
                        final String[] paramKey = {p.getName()};
                        Arrays.stream(declaredAnnotations).forEach(annotation -> {
                            if (StringUtils.equalsAnyIgnoreCase(annotation.annotationType().getSimpleName(),"Param")){
                                paramKey[0] =p.getDeclaredAnnotation(Param.class).value();
                            }
                        });
                        CrypticField annotation=p.getDeclaredAnnotation(CrypticField.class);
                        if (annotation!=null) {
                            if(StringUtils.equalsIgnoreCase(CrypticConstant.QUERY, methodName)){
                                parameter = mapFieldIsCrypt(parameter, CrypticConstant.BEFORE_SELECT,paramKey[0],annotation);
                            }else if (StringUtils.equalsIgnoreCase(CrypticConstant.UPDATE, methodName)) {
                                // 修改直接返回
                                parameter = mapFieldIsCrypt(parameter, CrypticConstant.UPDATE,paramKey[0],annotation,commandType.name());
                            }
                        }
                    }
                }
            }
        }

        if (StringUtils.equalsIgnoreCase(CrypticConstant.UPDATE, methodName)) {
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

            new AlgorithmExecutor(algorithmConfig.getCrypticAlgorithm()).selectField(list,CrypticConstant.AFTER_SELECT);
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
