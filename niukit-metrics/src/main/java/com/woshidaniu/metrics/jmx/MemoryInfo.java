package com.woshidaniu.metrics.jmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.woshidaniu.metrics.utils.CapacityUtils;
import com.woshidaniu.metrics.utils.CapacityUtils.Unit;

public class MemoryInfo {
	
	protected String prefix;
	protected String type;
	protected Map<String, Long> usage;
	protected Unit unit;
	
	public MemoryInfo(final String prefix,final String type,final Map<String, Long> usage,final Unit unit) {
		this.prefix = prefix;
		this.type = type;
		this.usage = usage;
		this.unit = unit;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String, Long> getUsage() {
		return usage;
	}

	public Unit getUnit() {
		return unit;
	}
	
	public Map<String, String> toMap() {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (String key : usage.keySet()) {
			dataMap.put(StringUtils.join(new String[]{prefix, type, key}, "."), CapacityUtils.getCapacityString(usage.get(key), unit) );
		}
		return dataMap;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
        buf.append("init = " + usage.get("init") + "(" + (usage.get("init") >> 10) + "K) ");
        buf.append("used = " + usage.get("used") + "(" + ( usage.get("used") >> 10) + "K) ");
        buf.append("committed = " + usage.get("committed") + "(" + (usage.get("committed") >> 10) + "K) " );
        buf.append("max = " + usage.get("max") + "(" + (usage.get("max") >> 10) + "K)");
		return buf.toString();
	}
	
	
}