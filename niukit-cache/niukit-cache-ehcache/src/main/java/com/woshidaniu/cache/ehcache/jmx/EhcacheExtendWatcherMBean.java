package com.woshidaniu.cache.ehcache.jmx;

import java.util.List;

public interface EhcacheExtendWatcherMBean {  
    /** 
     * 获取延迟结果 
     *  
     * @return 
     */  
    List<String> getGlobalResult();  
    /** 
     * 获取剔除数量 
     *  
     * @return 
     */  
    long getEvictedCount();  
    /** 
     * 获取超时数量 
     *  
     * @return 
     */  
    long getExpiredCount();  
    /** 
     * 获取未命中统计 
     *  
     * @return 
     */  
    List<String> getMissStatisticsMap();  
}  