# 这是我的第一个gradle-example
我想了想正好最近有需要把加密业务替换成国密算法，便有了这个example

##2019.06.19
修改解密注解枚举，修改拦截处理逻辑
ENCRYPT （默认）加解密,数据库存储为密文，查询解密展示，插入时加密 （对于数据存在保密性，如登录密码等）
ONLY_ENCRYPT 仅加密,数据库存储为明文，查询出来加密，通过该值进行查询前会先解密 （适合在前端参数传递使用）

#gradle 使用中遇到的问题
1.lombok无法使用 （gradle版本问题 4.7以上不能使用 compile("org.projectlombok:lombok:1.18.2")或者compileOnly("org.projectlombok:lombok:1.18.2")）

# About example:
利用mybatis Interceptor 拦截修改和查询操作，对于参数/结果集进行加解密处理
通过自定义Java类注解`CrypticField`，来实现加解密逻辑





