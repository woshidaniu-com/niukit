
#niukit-basicutils

1、最重要的常用基础工具类，该工程扩展了Apache常用基础工具类并增加了一些开发过程中会用到的工具函数。如空值判断，日期函数等
2、扩展common-beanutils，增加常用对象操作函数
3、各自文件压缩的工具函数
2、字符串格式化工具实现。
	a、扩展Fastjson的JSONObject对象，增加默认过滤空字段的JSON输出
	b、扩展pinyin4j组件，实现常用函数;
	c、实现字符串格式函数，分别实现基于Pattern或OGNL两种方式的字符格式化
4、扩展google.guava，实现对象缓存，流缓存默认接口
5、图片操作函数扩展实现
6、基于commons-io 的扩展实现；
	a、扩展更多的流操作函数；
	b、扩展更多的文件操作函数


#描述
======================================================================
com.woshidaniu.basicutils.net.AddressUtils		获取本机网卡信息
com.woshidaniu.basicutils.net.InetAddressUtils	获取本机网卡信息
com.woshidaniu.basicutils.net.MacAddressUtils	1、获取本机mac；2、获取远程主机mac
com.woshidaniu.basicutils.ArrayUtils			数组操作工具类
com.woshidaniu.basicutils.Assert				参数检查判断
com.woshidaniu.basicutils.BinaryUtils			二进制工具类
com.woshidaniu.basicutils.BlankUtils			空对象、空字符串判断工具类
com.woshidaniu.basicutils.BooleanUtils			布尔值工具类
com.woshidaniu.basicutils.CharsetUtils			字符工具类
com.woshidaniu.basicutils.CollectionUtils		集合工具类
com.woshidaniu.basicutils.ConfigUtils			初始化文件加载工具
com.woshidaniu.basicutils.DateUtils				日期工具类
com.woshidaniu.basicutils.ExceptionUtils		异常工具类
com.woshidaniu.basicutils.ExecutorUtils			线程相关工具类
com.woshidaniu.basicutils.IDCardUtils			身份证校验
com.woshidaniu.basicutils.LocaleUtils			Locale对象工具类
com.woshidaniu.basicutils.LocationUtils			文件定位加载工具类
com.woshidaniu.basicutils.MathUtils				算术工具类
com.woshidaniu.basicutils.Native2AsciiUtils		字符Ascii转换工具
com.woshidaniu.basicutils.NestedExceptionUtils	嵌套异常工具类
com.woshidaniu.basicutils.ObjectUtils			对象工具类
com.woshidaniu.basicutils.PredicateUtils		Predicate工具类
com.woshidaniu.basicutils.RandomUtils			随机数工具类
com.woshidaniu.basicutils.StrengthUtils			密码强度工具类
com.woshidaniu.basicutils.StringUtils			字符串工具类
com.woshidaniu.basicutils.TimeUtils				时间工具类
com.woshidaniu.basicutils.UniqID				UID工具类
com.woshidaniu.basicutils.URLUtils				URL工具类
com.woshidaniu.basicutils.ValueUtils			值处理工具类

#更新说明
======================================================================

##V1.0.7-SNAPSHOT
a、统一更新第三方组件依赖版本
b、合并学工niutal的基础组件




