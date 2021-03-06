package com.woshidaniu.metrics;

import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarFactory;

import com.woshidaniu.metrics.sigar.OSEnvInfo;

import junit.framework.TestCase;

public class OSEnvInfo_Test extends TestCase {

	public void testInfo() throws Exception {
		System.out.println("======================Info=============================");
		Sigar sigar = (Sigar) SigarFactory.newSigar();
		Map<String, Object> infoMap = OSEnvInfo.info(sigar);
		
		System.out.println("用户名:    " + infoMap.get("host.username"));
        System.out.println("计算机名:    " + infoMap.get("host.computer.name"));
        System.out.println("计算机域名:    " + infoMap.get("host.userdomain"));
        System.out.println("本地IP地址:    " + infoMap.get("host.ip"));
        System.out.println("本地主机名:    " + infoMap.get("host.name"));
		
        System.out.println("操作系统CPU尾数:    " + infoMap.get("os.cpu.endian"));
        System.out.println("操作系统位数:    " + infoMap.get("os.datamodel"));
        System.out.println("os.machine:    " + infoMap.get("os.machine"));
        
        // 操作系统类型
        System.out.println("操作系统类型:    " + infoMap.get("os.name"));
        // 操作系统内核类型如： 386、486、586等x86
        System.out.println("操作系统内核类型:    " + infoMap.get("os.arch"));
        // 操作系统版本号
        System.out.println("操作系统的版本号:    " + infoMap.get("os.version"));
        // 操作系统补丁级别
        System.out.println("操作系统补丁级别:    " + infoMap.get("os.patch.level"));
        // 操作系统描述
        System.out.println("操作系统描述:    " + infoMap.get("os.desc"));
        // 操作系统开发商
        System.out.println("操作系统开发商:    " + infoMap.get("os.vendor"));
        // 操作系统开发商名称
        System.out.println("操作系统开发商名称:    " + infoMap.get("os.vendor.name"));
        // 操作系统开发商代码
        System.out.println("操作系统开发商代码:    " + infoMap.get("os.vendor.code"));
        // 操作系统开发商类型
        System.out.println("操作系统开发商代码:    " + infoMap.get("os.vendor.version"));
        
        
	}
	
	public void testMemory() throws Exception {
		System.out.println("======================memory=============================");
		Sigar sigar = (Sigar) SigarFactory.newSigar();
		Map<String, Object> infoMap = OSEnvInfo.memory(sigar);
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
		
	}
	
	public void testUsage() throws Exception {
		System.out.println("======================usage=============================");
		Sigar sigar = (Sigar) SigarFactory.newSigar();
		Map<String, Double> infoMap = OSEnvInfo.usage(sigar);
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + CpuPerc.format(infoMap.get(key)));
		}
		
	}
	
	public void testCpuInfos() throws Exception {
		System.out.println("======================CpuInfos=============================");
		Sigar sigar = (Sigar) SigarFactory.newSigar();
		List<Map<String, Object>> infoMap = OSEnvInfo.cpuInfos(sigar);
		for (Map<String, Object> map : infoMap) {
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
			}
			System.out.println("===================================================");
		}
	}
	
	public void testDiskInfos() throws Exception {
		System.out.println("======================DiskInfos=============================");
		Sigar sigar = (Sigar) SigarFactory.newSigar();
		List<Map<String, Object>> infoMap = OSEnvInfo.diskInfos(sigar);
		for (Map<String, Object> map : infoMap) {
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
			}
			System.out.println("===================================================");
		}
	}
	
	
}
