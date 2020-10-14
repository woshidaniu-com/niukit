package com.woshidaniu.cache.ehcache.jmx;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.statistics.StatisticsGateway;
import net.sf.ehcache.statistics.extended.ExtendedStatistics;
import java.util.ArrayList;
import java.util.List;

public class EhcacheExtendWatcher implements EhcacheExtendWatcherMBean {
	private Ehcache ehcache;

	@Override
	public List<String> getGlobalResult() {
		ExtendedStatistics extendedStatistics = ehcache.getStatistics().getExtended();
		ExtendedStatistics.Result getResult = extendedStatistics.allGet();
		ExtendedStatistics.Result putResult = extendedStatistics.allPut();
		ExtendedStatistics.Result missResult = extendedStatistics.allMiss();
		List<String> resultList = new ArrayList<String>();
		String getStr = "allGet:count=" + getResult.count().value() + ";rate=" + getResult.rate().value()
				+ ";latency:average=" + getResult.latency().average().value() + ";minimum="
				+ getResult.latency().minimum().value() + ";maximum=" + getResult.latency().maximum().value();
		String putStr = "allPut:count=" + putResult.count().value() + ";rate=" + putResult.rate().value()
				+ ";latency:average=" + putResult.latency().average().value() + ";minimum="
				+ putResult.latency().minimum().value() + ";maximum=" + putResult.latency().maximum().value();
		String missStr = "allMiss:count=" + missResult.count().value() + ";rate=" + missResult.rate().value()
				+ ";latency:average=" + missResult.latency().average().value() + ";minimum="
				+ missResult.latency().minimum().value() + ";maximum=" + missResult.latency().maximum().value();
		resultList.add(getStr);
		resultList.add(putStr);
		resultList.add(missStr);
		return resultList;
	}

	@Override
	public long getEvictedCount() {
		StatisticsGateway statisticsGateway = ehcache.getStatistics();
		return statisticsGateway.cacheEvictedCount();
	}

	@Override
	public long getExpiredCount() {
		StatisticsGateway statisticsGateway = ehcache.getStatistics();
		return statisticsGateway.cacheExpiredCount();
	}

	@Override
	public List<String> getMissStatisticsMap() {
		StatisticsGateway statisticsGateway = ehcache.getStatistics();
		ExtendedStatistics.Result missResult = statisticsGateway.cacheMissOperation();
		ExtendedStatistics.Result missExpiredResult = statisticsGateway.cacheMissExpiredOperation();
		ExtendedStatistics.Result missMissNotFoundResult = statisticsGateway.cacheMissNotFoundOperation();
		String missResultStr = "missResult:count=" + missResult.count().value() + ";rate=" + missResult.rate().value()
				+ ";latency:average=" + missResult.latency().average().value() + ";minimum="
				+ missResult.latency().minimum().value() + ";maximum=" + missResult.latency().maximum().value();
		String missExpiredResultStr = "missExpiredResult:count=" + missExpiredResult.count().value() + ";rate="
				+ missExpiredResult.rate().value() + ";latency:average=" + missExpiredResult.latency().average().value()
				+ ";minimum=" + missExpiredResult.latency().minimum().value() + ";maximum="
				+ missExpiredResult.latency().maximum().value();
		String missMissNotFoundResultStr = "missMissNotFoundResult:count=" + missMissNotFoundResult.count().value()
				+ ";rate=" + missMissNotFoundResult.rate().value() + ";latency:average="
				+ missMissNotFoundResult.latency().average().value() + ";minimum="
				+ missMissNotFoundResult.latency().minimum().value() + ";maximum="
				+ missMissNotFoundResult.latency().maximum().value();
		List<String> resultList = new ArrayList<String>();
		resultList.add(missResultStr);
		resultList.add(missExpiredResultStr);
		resultList.add(missMissNotFoundResultStr);
		return resultList;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}
	
}