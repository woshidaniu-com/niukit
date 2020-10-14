#设置并发级别为8，并发级别是指可以同时写缓存的线程数
guava.cache.maxThread = 8
#设置缓存容器的初始容量为10
guava.cache.minSize = 10
#设置缓存最大容量限制，超过限制之后就会按照LRU最近虽少使用算法来移除缓存项
guava.cache.maxSize = 100
#根据某个键值对最后一次访问之后多少时间后移除;单位：分钟;默认一天之后
guava.cache.expireAfterAccess = 1440
#根据某个键值对被创建或值被替换后多少时间移除;单位：分钟
guava.cache.expireAfterWrite = 60
