package com.littlefox.interceptor;

import com.littlefox.annotation.CrypticField;
import com.littlefox.cryptic.CryptPojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
//@Component
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class })
})
public class ResultSetHandlerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //执行拦截对象真正的方法

   /*

        if(resultSet != null && resultSet.next()) {
            Account account = new Account();
            // infos字段
            String infos = resultSet.getString("infos");
            // 判断是否为空
            if(StringUtils.isNotBlank(infos)) {
                // fastjson泛型反序列化
                Map<String, Object> infMap = JSON.parseObject(infos, new TypeReference<Map>(){});
                account.setInfos(infMap);
                resultList.add(account);
            }
            // handleResultSets返回结果一定是一个List
            // size为1时，Mybatis会取第一个元素作为接口的返回值。
            return resultList;
        }
        return invocation.proceed();*/

        /*
        List<String> resList = new ArrayList<String>();
                 DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
                 //MappedStatement维护了一条<select|update|delete|insert>节点的封装
                 MappedStatement mappedStatement = defaultResultSetHandler.getMappedStatement();
                 //获取节点属性的集合
                 List<ResultMap> resultMaps = mappedStatement.getResultMaps();
                 int resultMapCount = resultMaps.size();
                 //获取当前resutType的类型
                 Class<?> resultType = resultMaps.get(0).getType();
                 if (resultMapCount > 0 && resultType.getName().equals("java.lang.String")) {
                         Object[] obj = invocation.getArgs();
                         Statement statement = (Statement) invocation.getArgs()[0];
                         //获得结果集
                         ResultSet resultSet = statement.getResultSet();

                         if (resultSet != null) {
                                 //获得对应列名
                                 ResultSetMetaData rsmd = resultSet.getMetaData();
                                 List<String> columnList = new ArrayList<String>();

                                 for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                         columnList.add(rsmd.getColumnName(i));
                                     }
                                while (resultSet.next()) {
                                         LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                                         for (String colName : columnList) {
                                                 map.put(colName, resultSet.getObject(colName));
                                             }

                                    }
                                return resList;
                             }
                     }
                 return invocation.proceed();
        return null;*/
        return  null;
    }

    @Override
    public Object plugin(Object target) {
        // 读取@Signature中的配置，判断是否需要生成代理类
        if (target instanceof ResultSetHandler) {
           return Plugin.wrap(target, this);
        } else {
           return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
