# 这是我的第一个gradle-example

我想了想正好最近有需要把加密业务替换成国密算法，便有了这个example

## 加密注解介绍

1. 定义
```java
/**
* 加/解密注解（应用于字段，方法参数上）
* @author rockychen
*/
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CrypticField {
   /**
    * ONLY_ENCRYPT 仅加密,数据库存储为明文，查询出来加密，通过该值进行查询前会先解密
    * ENCRYPT （默认）加解密,数据库存储为密文，查询解密展示，插入时加密
    */
   enum Type{ ENCRYPT,ONLY_ENCRYPT}

   Type type() default Type.ENCRYPT;

}
```
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
```java
public class User extends Person{
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String id;

    private String userName;

    @CrypticField
    private String phone;

}
```

对于方法多参数传递在需要处理的参数上加上注解（注：继承了通用接口的请先重写该方法），仅对于`Mapper`层(即mybatis接口层)有效

### 方法2、手动处理 `AlgorithmUtils` 工具类

参数传递Map类型只能手动解密，或者把参数拆分，使用上面多参数方式处理

## 更新日志

### 2019.08.04
add Test example and DES

### 2019.08.03
修改 `Param` 注解逻辑

### 2019.07.31
优化代码,fix

### 2019.07.11
优化代码
修改对象继承中包含枚举类的判断逻辑，会导致堆栈溢出

### 2019.07.10
支持传入参数为List<String>场景，支持批量处理

### 2019.07.08
修改对象继承判断逻辑，加入嵌套bean处理

### 2019.07.04
修改pagehelper分页查询参数处理

### 2019.07.03
解决类继承依赖，结合注解自动处理当中的依赖关系

### 2019.06.26
解决类嵌套依赖，注解不能自动处理 (如`javaBean`中存在嵌套`List`)

### 2019.06.24
添加注解可在方法上使用，自动转换处理，添加手动工具类

### 2019.06.21
实现动态算法切换1.0,配置文件切换--》转化为数据库切换

### 2019.06.20
引入抽象类，添加 AES、SM3、SM4 算法实现类，实现动态算法切换

### 2019.06.19
修改解密注解枚举，修改拦截处理逻辑
ENCRYPT （默认）加解密,数据库存储为密文，查询解密展示，插入时加密 （对于数据存在保密性，如登录密码等）
ONLY_ENCRYPT 仅加密,数据库存储为明文，查询出来加密，通过该值进行查询前会先解密 （适合在前端参数传递使用）一般这个实际情况用的会多一些

## 待解决

~~pagehelper 分页bug~~

~~嵌套对象逻辑处理~~

~~对象继承处理~~

## Next
- [ ] 完善算法种类
- [ ] 针对每种情况补充测试实例
- [ ] 性能测试（完整版）
- [ ] 看有没有抽取成为jar的可能

## 遇到的问题

1. gradle lombok无法使用 （gradle版本问题 4.7以上不能使用 compile("org.projectlombok:lombok:1.18.2")或者compileOnly("org.projectlombok:lombok:1.18.2")）
2. 注册多个@Component 时，感觉是按照代码结构从上往下扫描的
3. 类中含有枚举类时，会存在循环查找
4. 在同一个方法中，当一个查询出来的结果对象，作为另外一个查询的查询参数，加密过程会修改对象的引用，需要将对象实现 `Cloneable` 接口，再将克隆对象作为参数传递查询即可
5. 关于区分识别执行的方法是来自于接口的默认方法还是继承类重写的方法，目前判断识别逻辑不严谨；

## 性能（结果和加密参数个数有关，以及类的复杂性）

|数据量|AES|SM4ECB|SM4CBC|
|:----:|:----:|:----:|:----:|
|10W||-|-|
|50W|13.468s|-|-|

## 注：

1. 算法只能对`String`类型进行加解密处理
2. `JavaBean`处理只针对在`model`包下，并且参数个数只有一个，请遵守约定
3. 对于查询返回结果不是`JavaBean` or `List<T>` 目前还没处理(可实现)，举例返回值是`String`等
4. `CrypticField`对于用户查询不友好（模糊查询，范围查询），因为数据库存储的是密文
5. 对于mybatis Sql 语法只能使用#{XXX},不能使用${XXX},因为在获取执行SQL的时候${XXX}会事先编译，而#{XXX}则会保留占位符？

## About example:
利用mybatis Interceptor 拦截修改和查询操作，对于参数/结果集进行加解密处理
通过自定义Java类注解`CrypticField`，来实现加解密逻辑

关于动态算法切换，本example没有提供，只能来自`application.properties` 进行切换
动态算法实现逻辑：
注意事项：如果使用`pagehelper`插件进行查询分页，则不能再`AlgorithmExecutor`执行器类中进行查询操作

使用中如有问题，欢迎提 [issue](https://github.com/rockychen1221/gradle-example/issues "issue")
