package com.woshidaniu.metrics;

import java.util.List;
import java.util.Map;

import com.woshidaniu.metrics.jmx.JVMInfo;
import com.woshidaniu.metrics.jmx.MemoryInfo;
import com.woshidaniu.metrics.utils.CapacityUtils.Unit;

import junit.framework.TestCase;

public class JVMInfo_Test extends TestCase {

	public void testInfo() throws Exception {
		System.out.println("======================info=============================");
		Map<String, Object> infoMap = JVMInfo.info();
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
		
	}

	public void testMemory_MB() throws Exception {
		System.out.println("======================runtime=============================");
		System.out.println(JVMInfo.runtime(Unit.MB));
	}

	/*public void testMemory_KB() throws Exception {
		System.out.println(JVMInfo.runtime(Unit.KB));

	}*/
	
	
	public void testMemory() throws Exception {
		
		System.out.println("======================memory=============================");
		
		List<MemoryInfo> infoList = JVMInfo.memory(Unit.KB);
		for (MemoryInfo memoryMap : infoList) {
			System.out.println( memoryMap.getType() + ":" + memoryMap.toMap());
			System.out.println( memoryMap.getType() + ":" +memoryMap.toString());
            System.out.println("===================================================");
		}
	}
	
	public void testMemoryPool() throws Exception {
		
		System.out.println("======================memoryPool=============================");
		 
		List<MemoryInfo> infoList = JVMInfo.memoryPool(Unit.KB);
		for (MemoryInfo memoryMap : infoList) {
			System.out.println( memoryMap.getType() + ":" + memoryMap.toMap());
			System.out.println( memoryMap.getType() + ":" +memoryMap.toString());
            System.out.println("===================================================");
		}
	}
	
	public void testGc() throws Exception {
		System.out.println("==========================GarbageCollector=========================");
		for (Map<String, Object> infoMap : JVMInfo.gc()) {
			for (String key : infoMap.keySet()) {
				System.out.println(key + " : " + infoMap.get(key));
			}
		}
	}

}
