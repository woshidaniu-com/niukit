package com.woshidaniu.cache.xmemcached;

import java.util.Locale;

/**
 * Memcached有个stats命令，通过它可以查看Memcached服务的许多状态信息。使用方法如下：
	先在命令行直接输入telnet 主机名端口号，连接到memcached服务器，然后再连接成功后，输入stats 命令，即可显示当前memcached服务的状态信息。
	比如在我本机测试如下：
	stats
	STAT pid 32608
	STAT uptime 19
	STAT time 1487842542
	STAT version 1.4.13
	STAT libevent 2.0.21-stable
	STAT pointer_size 64
	STAT curr_connections 10
	STAT total_connections 11
	STAT connection_structures 11
	STAT reserved_fds 20
	STAT cmd_get 0
	STAT cmd_set 0
	STAT cmd_flush 0
	STAT cmd_touch 0
	STAT get_hits 0
	STAT get_misses 0
	STAT delete_misses 0
	STAT delete_hits 0
	STAT incr_misses 0
	STAT incr_hits 0
	STAT decr_misses 0
	STAT decr_hits 0
	STAT cas_misses 0
	STAT cas_hits 0
	STAT cas_badval 0
	STAT touch_hits 0
	STAT touch_misses 0
	STAT auth_cmds 0
	STAT auth_errors 0
	STAT bytes_read 15
	STAT bytes_written 7
	STAT limit_maxbytes 67108864
	STAT accepting_conns 1
	STAT listen_disabled_num 0
	STAT threads 4
	STAT conn_yields 0
	STAT hash_power_level 16
	STAT hash_bytes 524288
	STAT hash_is_expanding 0
	STAT expired_unfetched 0
	STAT evicted_unfetched 0
	STAT bytes 0
	STAT curr_items 0
	STAT total_items 0
	STAT evictions 0
	STAT reclaimed 0
	END
	总结：stats命令总体来说很有用，通过这个命令我们很清楚当前memcached服务的各方面的信息。 
 */
public enum Stats {

	/** pid: memcached服务进程的进程ID */
	STAT_PID("pid"),
	/**uptime: memcached服务从启动到当前所经过的时间，单位是秒。*/
	STAT_UPTIME("uptime"),
	/**time: memcached服务器所在主机当前系统的时间戳，单位是秒。*/
	STAT_TIME("time"),
	/**version: memcached组件的版本。如：1.4.13。*/
	STAT_VERSION("version"),
	/**libevent: memcached使用的libevent的版本号。如：2.0.21-stable*/
	STAT_LIBEVENT("libevent"),
	/**pointer_size：服务器所在主机操作系统的指针大小，一般为32或64.*/
	STAT_POINTER_SIZE("pointer_size"),
	/**curr_connections：表示当前系统打开的连接数。*/
	STAT_CURR_CONNECTIONS("curr_connections"),
	/**total_connections：表示从memcached服务启动到当前时间，系统打开过的连接的总数。*/
	STAT_TOTAL_CONNECTIONS("total_connections"),
	/**connection_structures：表示从memcached服务启动到当前时间，被服务器分配的连接结构的数量。*/
	STAT_CONNECTION_STRUCTURES("connection_structures"),
	/**misc fds使用数 */
	STAT_RESERVED_FDS("reserved_fds"),
	/**cmd_get：get命令执行的次数。*/
	STAT_CMD_GET("cmd_get"),
	/**cmd_set：set命令执行的次数。*/
	STAT_CMD_SET("cmd_set"),
	/**flush_all命令执行的次数*/
	STAT_CMD_FLUSH("cmd_flush"),
	/**执行touch的次数，touch可以刷新过期时间*/
	STAT_CMD_TOUCH("cmd_touch"),
	/**get_hits：命中的次数,获取数据成功的次数。*/
	STAT_GET_HITS("get_hits"),
	/**get_misses：没有命中的次数,获取数据失败的次数。*/
	STAT_GET_MISSES("get_misses"),
	/**get_expired：*/
	STAT_GET_EXPIRED("get_expired"),
	/**get_flushed：*/
	STAT_GET_FLUSHED("get_flushed"),
	/**delete_misses: delete未命中次数*/
	STAT_DELETE_MISSES("delete_misses"),
	/**delethits: delete命中次数*/
	STAT_DELETHITS("delethits"),
	/**incr_misses: incr未命中次数*/
	STAT_INCR_MISSES("incr_misses"),
	/**incr_hits: incr命中次数*/
	STAT_INCR_HITS("incr_hits"),
	/**decr_misses: decr未命中次数*/
	STAT_DECR_MISSES("decr_misses"),
	/**decr_hits: decr命中次数*/
	STAT_DECR_HITS("decr_hits"),
	/**cas_misses: cas未命中次数*/
	STAT_CAS_MISSES("cas_misses"),
	/**cas_hits: cas命中次数*/
	STAT_CAS_HITS("cas_hits"),
	/**cas_badval: 使用擦拭次数 */
	STAT_CAS_BADVAL("cas_badval"),
	/**touch_misses: touch未命中次数*/
	STAT_TOUCH_MISSES("touch_misses"),
	/**touch_hits: touch命中次数  */
	STAT_TOUCH_HITS("touch_hits"),
	/**accepting_conns: 正在接受的连接数*/
	STAT_ACCEPTING_CONNS("accepting_conns"),
	/**auth_cmds: authentication 执行的次数 */
	STAT_AUTH_CMDS("auth_cmds"),
	/**auth_errors: authentication 执行失败的次数 */
	STAT_AUTH_ERRORS("auth_errors"),
	/**bytes_read：memcached服务器从网络读取的总的字节数。*/
	STAT_BYTES_READ("bytes_read"),
	/**bytes_written：memcached服务器发送到网络的总的字节数。*/
	STAT_BYTES_WRITTEN("bytes_written"),
	/**拒绝连接尝试的次数，因为memcached的达到其配置的连接限制（"-C"参数）。*/
	STAT_LISTEN_DISABLED_NUM("listen_disabled_num"),
	/**limit_maxbytes：memcached服务缓存允许使用的最大字节数。这里为67108864字节，也就是是64M.与我们启动memcached服务设置的大小一致。*/
	STAT_LIMIT_MAXBYTES("limit_maxbytes"),
	/***/
	STAT_TIME_IN_LISTEN_DISABLED_US("time_in_listen_disabled_us"),
	/**threads：当前Memcached服务器使用的线程数。("-t" 参数指定)。*/
	STAT_THREADS("threads"),
	/**memcached 启动至今有多少次打开的连接因为内部请求数达到 -R 参数指定的限值而被动放弃  */
	STAT_CONN_YIELDS("conn_yields"),
	/**hashpower的level，可以在启动的时候设置($ memcached -o hashpower=20)*/
	STAT_HASH_POWER_LEVEL("hash_power_level"),
	/**内存使用总量单位为byte*/
	STAT_HASH_BYTES("hash_bytes"),
	/**是否正在扩大hash表*/
	STAT_HASH_IS_EXPANDING("hash_is_expanding"),
	/***/
	STAT_EXPIRED_UNFETCHED("expired_unfetched"),
	/***/
	STAT_EVICTED_UNFETCHED("evicted_unfetched"),
	/**bytes：表示系统存储缓存对象所使用的存储空间，单位为字节。*/
	STAT_BYTES("bytes"),
	/**curr_items：表示当前缓存中存放的所有缓存对象的数量。不包括目前已经从缓存中删除的对象。*/
	STAT_CURR_ITEMS("curr_items"),
	/**total_items：表示从memcached服务启动到当前时间，系统存储过的所有对象的数量，包括目前已经从缓存中删除的对象。*/
	STAT_TOTAL_ITEMS("total_items"),
	/**evictions：为了给新的数据项目释放空间，从缓存移除的缓存对象的数目。比如超过缓存大小时根据LRU算法移除的对象，以及过期的对象。*/
	STAT_EVICTIONS("evictions"),
	/**reclaimed：memcached 启动至今有多少次在存储数据的时候使用了过期数据的空间  */
	STAT_RECLAIMED("reclaimed");
	
	protected String name;

	private Stats(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	static Stats valueOfIgnoreCase(String stat) {
		return valueOf(stat.toUpperCase(Locale.ENGLISH).trim());
	}
	
}
