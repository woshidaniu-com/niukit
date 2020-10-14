<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="ehcache.xsd">

	<!--
		磁盘储存配置 
		
		=======================

		diskStore元素是可选的。如果不想使用磁盘储存，可以注释或删除该配置元素。
		如果你有overflowToDisk，该名称表示当缓存中的元素大小超过缓存配置大小时，
		是否将其写入磁盘当中，或者diskPersistent，该名称表示将缓存元素持久化至 磁盘中，那么需要配置diskStore这个元素。
		如果没有配置这个元素，当一个需要磁盘存储的缓存被创建时，会报告一个警告，并 且java.io.tmpdir临时文件夹会被自动使用。
		diskStore只有一个属性，path。它表示.data和.index文件会在哪个目录下创建。
		如果path是如下例举的java系统变量中的一个，那么在虚拟机运行时，会将这些值 赋予path。为了保证向后兼容性，系统变量应该被显示关闭。
		java系统变量： 
		1.user.home － 用户home目录 
		2.user.dir － 用户当前工作目录
		3.java.io.tmpdir － 默认的临时文件目录 
		4.ehcache.disk.store.dir －
		需要用户在命令行中声明的系统变量 比如 java -Dehcache.disk.store.dir=/u01/myapp/diskdir
		... 子目录可以这样定义java.io.tmpdir/one
	-->
	<diskStore path="java.io.tmpdir/ehcache" />

	<!--
		EHCache是一个非常优秀的基于Java的Cache实现。它简单、易用，而且功能齐全，并且非常容易与Spring、Hibernate等流行的开源框架进行整合。
		通过使用EHCache可以减少网站项目中数据库服务器的访问压力，提高网站的访问速度，改善用户的体验。 缓存配置参数： name:缓存名称。
		maxElementsInMemory：缓存中允许创建的最大对象数
		eternal:缓存中对象是否为永久的，如果是，超时设置将被忽略，对象从不过期。
		timeToIdleSeconds：设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
		timeToLiveSeconds：设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，
		也就是对象存活时间无穷大。
		overflowToDisk：当内存中对象数量达到maxElementsInMemory时，Ehcache将会对象写到磁盘中。
		overflowToDisk：内存不足时，是否启用磁盘缓存。
		diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
		maxElementsOnDisk：硬盘最大缓存个数。 diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
		diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
		memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
		清空策略: 
		1 FIFO ,first in first out ,这个是大家最熟的,先进先出,不多讲了
		2 LFU , Less Frequently Used ,就是上面例子中使用的策略,直白一点就是讲一直以来最少被使用的.如上面所讲,缓存的元素有一个hit属性,hit 值最小的将会被清出缓存. 
		2 LRU ,Least Recently Used,最近最少使用的,缓存的元素有一个时间戳,当缓存容量满了,而又需要腾出地方来缓存新的元素的时候,那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存.
		clearOnFlush：内存数量最大时是否清除。
	-->

	<defaultCache maxElementsInMemory="10000" eternal="false"
		timeToIdleSeconds="120" timeToLiveSeconds="120" overflowToDisk="true"
		maxElementsOnDisk="10000000" diskPersistent="false"
		diskExpiryThreadIntervalSeconds="120" memoryStoreEvictionPolicy="LRU" />

	<cache name="SimplePageFragmentCachingFilter"
		maxElementsInMemory="1" maxElementsOnDisk="1" eternal="false"
		overflowToDisk="true" timeToIdleSeconds="300" timeToLiveSeconds="600"
		memoryStoreEvictionPolicy="LFU" />

	<cache name="SimplePageCachingFilter" maxElementsInMemory="10000"
		eternal="false" overflowToDisk="false" timeToIdleSeconds="900"
		timeToLiveSeconds="1800" memoryStoreEvictionPolicy="LFU" />


</ehcache>