#IP
jedis.ip=172.0.0.1
#Port
jedis.port=6379
 

#maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis
#borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；是否启用后进先出, 默认true
jedis.pool.lifo = true
#连接池中最少空闲的连接数,默认为0.
jedis.pool.minIdle = 0
#最大能够保持idel状态的对象数
#控制一个pool最多有多少个状态为idle的jedis实例；
jedis.pool.maxIdle = 8 
#控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
#如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
jedis.pool.maxTotal = 500 
#当池内没有返回对象时，最大等待时间获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
#表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
jedis.pool.maxWaitMillis = -1
#连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
jedis.pool.blockWhenExhausted = true
#逐出连接的最小空闲时间 默认1800000毫秒(30分钟)，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除。
#表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
jedis.pool.minEvictableIdleTimeMillis = -1
#连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
#对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
#在minEvictableIdleTimeMillis基础上，加入了至少 minIdle个对象已经在pool里面了。
#如果为-1，evicted不会根据idle time驱逐任何对象。
#如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
jedis.pool.softMinEvictableIdleTimeMillis = -1
#每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 .
#表示idle object evitor每次扫描的最多的对象数；
jedis.pool.numTestsPerEvictionRun = 3
#向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
#在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
jedis.pool.testOnBorrow = true
#向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
jedis.pool.testOnReturn = true 
#向连接池“获取”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
jedis.pool.testOnCreate = true 
#向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值.
#如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
jedis.pool.testWhileIdle = false
#“空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.
#表示idle object evitor两次扫描之间要sleep的毫秒数；
jedis.pool.timeBetweenEvictionRunsMillis = -1
#是否启用pool的jmx管理功能, 默认true -->
jedis.pool.jmxEnabled = true
#/MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i)
# 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好. -->
jedis.pool.jmxNamePrefix = pool
#表示当pool中的jedis实例都被allocated完时，pool要采取的操作；
#默认有三种
#WHEN_EXHAUSTED_FAIL（表示无jedis实例时，直接抛出NoSuchElementException）、
#WHEN_EXHAUSTED_BLOCK（则表示阻塞住，或者达到maxWait时抛出JedisConnectionException）、 
#WHEN_EXHAUSTED_GROW（则表示新建一个jedis实例，也就说设置的maxActive无用）；
jedis.pool.whenExhaustedAction=WHEN_EXHAUSTED_GROW
#设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
jedis.pool.evictionPolicyClassName=org.apache.commons.pool2.impl.DefaultEvictionPolicy
