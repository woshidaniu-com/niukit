package com.woshidaniu.metrics.jmx;

public enum MemProperty {
	
	/**
	 * 表示 Java 虚拟机在启动期间从操作系统请求的用于内存管理的初始内存容量（以字节为单位）。
	 * Java 虚拟机可能在运行过程中从操作系统请求更多的内存，也可能将内存释放给系统。init 的值可以是不明确的
	 */
	MEM_INIT("init"),
	/**
	 * 表示当前已经使用的内存量（以字节为单位）。 
	 */
	MEM_USED("used"),
	/**
	 * 表示保证可以由 Java 虚拟机使用的内存量（以字节为单位）。
	 * 已提交的内存量可以随时间而变化（增加或减少）。
	 * Java 虚拟机可能会将内存释放给系统，committed 可以小于 init。committed 将始终大于或等于 used。
	 */
	MEM_COMMITTED("committed"),
	/**
	 * 表示可以用于内存管理的最大内存量（以字节为单位）。
	 * 可以不定义其值。如果定义了该值，最大内存量可能随时间而更改。已使用的内存量和已提交的内存量将始终小于或等于 max（如果定义了 max）。
	 * 如果内存分配试图增加满足以下条件的已使用内存将会失败：used > committed，即使 used <= max 仍然为 true（例如，当系统的虚拟内存不足时）。 
	 */
	MEM_MAX("max");
	
	protected String key;
	
	MemProperty(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}