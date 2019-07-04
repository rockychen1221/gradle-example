package com.littlefox.interceptor;

import com.littlefox.annotation.CrypticField;
import com.littlefox.constant.CrypticConstant;
import com.littlefox.cryptic.CrypticExecutor;
import com.littlefox.utils.CrypticConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.lang.reflect.Field;
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
    private CrypticConfig crypticConfig;

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
            value=(String) map.get(key);
            type=CrypticConstant.HASHMAP;
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
            text[i]=new CrypticExecutor(crypticConfig.getCryptic()).selectMapField(str[i],typeName,annotation);
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

    /**
     * 处理javaBean
     * @param obj
     * @param typeName
     */
    private void fieldIsCrypt(Object obj,String typeName){

        if(obj==null){
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        if (obj instanceof ArrayList<?>) {
            fields =((ArrayList) obj).get(0).getClass().getDeclaredFields();
        }
        int len;
        if (null != fields && 0 < (len = fields.length)) {
            // 标记是否有注解
            boolean isD = false;
            for (int i = 0; i < len; i++) {
                if (fields[i].isAnnotationPresent(CrypticField.class)) {
                    isD = true;
                    break;
                }
            }
            if (isD) {
                if (StringUtils.equalsIgnoreCase(typeName, CrypticConstant.BEFORE_SELECT)){
                    new CrypticExecutor(crypticConfig.getCryptic()).selectField(obj,typeName);
                } else if (StringUtils.equalsIgnoreCase(typeName, CrypticConstant.AFTER_SELECT)){
                    List<?> list = (ArrayList<?>) obj;
                    list.forEach(l -> new CrypticExecutor(crypticConfig.getCryptic()).selectField(l,typeName));
                }
            }
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if ("0".equals(crypticConfig.getCrypticSwitch())) {
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
                    if(p.getType().getTypeName().indexOf("model")>-1&&method.getParameterCount()==1||p.getName().equals("model")&&method.getParameterCount()==1){
                        if(StringUtils.equalsIgnoreCase(CrypticConstant.QUERY, methodName)){
                            fieldIsCrypt(parameter, CrypticConstant.BEFORE_SELECT);
                            break;
                        }else if (StringUtils.equalsIgnoreCase(CrypticConstant.UPDATE, methodName)) {
                            // 修改直接返回
                            new CrypticExecutor(crypticConfig.getCryptic()).updateField(parameter,commandType.name());
                            return invocation.proceed();
                        }
                    }else {
                        CrypticField annotation=p.getDeclaredAnnotation(CrypticField.class);
                        if (annotation!=null) {
                            if(StringUtils.equalsIgnoreCase(CrypticConstant.QUERY, methodName)){
                                parameter = mapFieldIsCrypt(parameter, CrypticConstant.BEFORE_SELECT,p.getName(),annotation);
                            }else if (StringUtils.equalsIgnoreCase(CrypticConstant.UPDATE, methodName)) {
                                // 修改直接返回
                                parameter = mapFieldIsCrypt(parameter, CrypticConstant.UPDATE,p.getName(),annotation,commandType.name());
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

            fieldIsCrypt(list,CrypticConstant.AFTER_SELECT);
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
