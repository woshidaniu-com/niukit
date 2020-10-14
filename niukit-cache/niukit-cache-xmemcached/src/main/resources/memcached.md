#如果缓存转储或网络错误导致连接关闭，xmemcached 是否尝试再次连接 
memcached.enableHealSession = true 
#尝试再次连接会话间隔；单位毫秒 
memcached.healSessionInterval = 2000
#请求响应超时时间；单位毫秒 
memcached.opTimeout = 5000 
#连接池大小即客户端个数;推荐在0-30之间为好，太大则浪费系统资源，太小无法达到分担负载的目的
#默认的pool size是1。设置这一数值不一定能提高性能，请依据你的项目的测试结果为准。初步的测试表明只有在大并发下才有提升。 
#设置连接池的一个不良后果就是，同一个memcached的连接之间的数据更新并非同步的 
#因此你的应用需要自己保证数据更新的原子性（采用CAS或者数据之间毫无关联）
memcached.connectionPoolSize=20
#链接超时时间，10s
memcached.connectTimeout=10000 
#是否宕机报警  
memcached.failureMode=true  
#默认过期时间
memcached.expiry=0
#进行数据的压缩阀值，默认大于1KB时进行压缩  
memcached.compressionThreshold=1024  
#无权重多服务模式缓存配置，配置该值后后面的配置不再起作用；多个服务以",; \t\n"等分割
#memcached.servers=192.168.1.1:11211 192.168.1.2:11211
#无权重多服务模式缓存配置，配置该值后后面的配置不再起作用；多个服务以",; \t\n"等分割
#
#memcached.servers=192.168.1.1:11211 192.168.1.2:11211
#server1  
memcached.server1.host=10.11.155.26  
memcached.server1.port=11211  
memcached.server1.weight=4  
#server2  
memcached.server2.host=10.11.155.41  
memcached.server2.port=11211  
memcached.server2.weight=3                
#server3  
memcached.server3.host=10.10.76.31  
memcached.server3.port=11211  
memcached.server3.weight=2                    
#server4  
memcached.server4.host=10.10.76.35  
memcached.server4.port=11211  
memcached.server4.weight=1 
