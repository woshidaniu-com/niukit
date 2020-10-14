# MyBatis 分页插件
---
> pagination 是一个简易的MyBatis物理分页插件，使用`org.apache.ibatis.session.RowBounds`及其子类作为分页参数，禁用MyBatis自带的内存分页。

## 分页原理
简单来说就是通过拦截StatementHandler重写sql语句，实现数据库的物理分页

## 如何使用分页插件 
> **mybatis** 配置文件中配置插件 [mybatis-config.xml]
```
<plugins>
    <!-- 
     | 分页插件配置 
     | 插件提供二种方言选择：1、默认方言 2、自定义方言实现类，两者均未配置则抛出异常！
     | dialectType 数据库方言  
     |             默认支持  mysql  oracle  hsql  sqlite  postgre 
     | dialectClass 方言实现类;默认 com.fastkit.sql.dialect.impl.MySql5Dialect
     |              自定义需要实现 com.fastkit.sql.dialect.Dialect 接口
     | -->
    <!-- 配置方式一、使用组件提供方言实现类 -->
    <plugin interceptor="com.fastkit.mybatis.interceptor.PaginationInterceptor">
        <property name="dialectType" value="mysql" />
    </plugin>
    <!-- 配置方式二、使用自定义方言实现类 -->
    <plugin interceptor="com.fastkit.mybatis.interceptor.PaginationInterceptor">
        <property name="dialectClass" value="xxx.dialect.XXDialect" />
    </plugin>
</plugins>
```
