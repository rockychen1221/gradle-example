# 这是我的第一个gradle-example

我想了想正好最近有需要把加密业务替换成国密算法，便有了这个example

# 加密注解介绍

1. 定义![image-20190628101914218](/Users/datadriver/Library/Application Support/typora-user-images/image-20190628101914218.png)
2. 仅加密 `@CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)`  （一般这个实际情况用的多一些）
   对于查询前会自动对传入参数进行解密（如编辑查询单个用户信息）
   对于查询后的返回结果会自动进行加密（如查询用户列表）
   对于修改前（新增、删除、修改）会自动进行解密
3. `@CrypticField` 默认 （使用场景不多）
   对于修改前（新增、修改、删除）、查询前会自动进行加密
   对于查询后的返回结果会自动进行解密



## 如何使用

### 方法1、拦截器自动处理

对于Java Bean

![image-20190628144906805](/Users/datadriver/Library/Application Support/typora-user-images/image-20190628144906805.png)


对于方法多参数传递在需要处理的参数上加上注解（注：继承了通用接口的请先重写该方法），仅对于`Mapper`层有效

![image-20190628095847885](/Users/datadriver/Library/Application Support/typora-user-images/image-20190628095847885.png)

### 方法2、手动处理 `CrypticUtils` 工具类

参数传递Map类型只能手动解密，或者把参数拆分，使用上面多参数方式处理

![image-20190628101730117](/Users/datadriver/Library/Application Support/typora-user-images/image-20190628101730117.png)



#### 注：

1. 算法只能对`String`类型进行加解密处理
2. 对于`javaBean`中存在嵌套`List`,目前可以处理，
3. `JavaBean`处理只针对在`model`包下，并且参数个数只有一个，请遵守约定
4. 对于查询返回结果不是`JavaBean` or `List<T>` 目前还没处理(可实现)，举例返回值是`String`等
5. `CrypticField`对于用户查询不友好（模糊查询，范围查询），因为数据库存储的是密文


## 2019.06.19

修改解密注解枚举，修改拦截处理逻辑
ENCRYPT （默认）加解密,数据库存储为密文，查询解密展示，插入时加密 （对于数据存在保密性，如登录密码等）
ONLY_ENCRYPT 仅加密,数据库存储为明文，查询出来加密，通过该值进行查询前会先解密 （适合在前端参数传递使用）

## 2019.06.20
引入抽象类，添加 AES、SM3、SM4 算法实现类，实现动态算法切换


## 2019.06.21
实现动态算法切换1.0,配置文件切换--》转化为数据库切换

## 2019.06.24
添加注解可在方法上使用，自动转换处理，添加手动工具类

## 2019.06.26
解决类嵌套依赖，注解不能处理问题



#### 待解决

pagehelper 分页bug

嵌套对象逻辑处理

对象继承处理

# 遇到的问题
1.gradle lombok无法使用 （gradle版本问题 4.7以上不能使用 compile("org.projectlombok:lombok:1.18.2")或者compileOnly("org.projectlombok:lombok:1.18.2")）
2 .注册多个@Component 时，感觉是按照代码结构从上往下扫描的


# About example:
利用mybatis Interceptor 拦截修改和查询操作，对于参数/结果集进行加解密处理
通过自定义Java类注解`CrypticField`，来实现加解密逻辑

关于动态算法切换，本example没有提供，只能来自`application.properties` 进行切换
动态算法实现逻辑：
注意事项：如果使用`pagehelper`插件进行查询分页，则不能再`CrypticExecutor`执行器类中进行查询操作，







