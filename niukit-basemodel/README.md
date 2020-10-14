
#niukit-basemodel

基础对象模型，因在部分基础组件中涉及到Javebean对象，特单独作为模块被其他模块依赖；（请注意，niutal框架中的对象大部分会继承BaseModel）

#描述
======================================================================
MultipleResourceBundle		多重国际化资源对象;实现多个国际化资源的聚合
PairListResourceBundle		基于键值对对象计划的国际化资源对象
ResourceBundleEnumeration	国际化资源对象遍历实现
DataRangeModel			数据范围模型对象
DataRangeRelation		数据范围关系
Page<T>					分页数据结果对象
PageBounds				分页参数对象
PageConstant			分页数据常量
BaseMap							自定义HashMap的子类；如Mybatis直接返回Map，其结果集字段名称为大写字符，返回BaseMap则结果集字段名称小写且分页时可包含分页信息
BaseModel							基础域模型；业务系统中的Model应都是该类的子类
CookieModel						Cookie参数模型对象
I18nModel							国际化数据模型对象
MapRowModel						因为Struts2无法直接JSON到List<Map>类型的转换，为了实现此转换，可采用此对象List<MapRowModel>
PairModel							键值对模型对象
QueryModel							分页查询参数模型对象
UserModel							用户信息对象顶层模型对象；建议所有的业务系统中的User对象集成此对象，以便进行通用业务逻辑实现

#更新说明
======================================================================

##V1.0.7-SNAPSHOT




