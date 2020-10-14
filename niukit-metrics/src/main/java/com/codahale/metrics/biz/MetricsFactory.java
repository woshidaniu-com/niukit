package com.codahale.metrics.biz;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistry.MetricSupplier;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * Singleton class to manage metrics.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MetricsFactory implements InitializingBean, DisposableBean {

	public static final String SERVLET_CONTEXT_METRIC_REGISTRY = ServletContext.class.getCanonicalName() +  ".registry";
	protected static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = new HealthCheckRegistry();
	protected static final MetricRegistry DEFAULT_REGISTRY = new MetricRegistry();

	protected MetricRegistry registry = DEFAULT_REGISTRY;
	protected JmxReporter jmxReporter;
	protected ConcurrentHashMap<String, Metric> COMPLIED_METRICS = new ConcurrentHashMap<String, Metric>();
	protected static final Logger LOG = LoggerFactory.getLogger(MetricsFactory.class);
	public MetricsFactory() {
		
	}

	public void afterPropertiesSet() throws Exception {
		jmxReporter = JmxReporter.forRegistry(registry).build();
		jmxReporter.start();
	}

	public void destroy() throws Exception {
		jmxReporter.stop();
	}

	public void setRegistry(MetricRegistry registry) {
		this.registry = registry;
	}

	public MetricRegistry getRegistry() {
		return registry;
	}

	/**
	 * 实例化一个Timer
	 */
	public Timer getTimer(String... names) {
		return getMetric(Timer.class, null, names);
	}
	
	/**
	 * 实例化一个Timer
	 */
	public Timer getTimer(Class<?> clazz, String... names) {
		return getMetric(Timer.class, clazz, names);
	}

	/**
	 * 实例化一个Histograms
	 */
	public Histogram getHistogram(String... names) {
		return getMetric(Histogram.class, null, names);
	}
	
	/**
	 * 实例化一个Histograms
	 */
	public Histogram getHistogram(Class<?> clazz, String... names) {
		return getMetric(Histogram.class, clazz, names);
	}

	/**
	 * 实例化一个counter,同样可以通过如下方式进行实例化再注册进去 Counter jobs = new Counter();
	 * metrics.register(MetricRegistry.name(TestCounter.class, "jobs"), jobs);
	 */
	public Counter getCounter(String... names) {
		return getMetric(Counter.class, null, names);
	}

	/**
	 * 实例化一个counter,同样可以通过如下方式进行实例化再注册进去 Counter jobs = new Counter();
	 * metrics.register(MetricRegistry.name(TestCounter.class, "jobs"), jobs);
	 */
	public Counter getCounter(Class<?> clazz, String... names) {
		return getMetric(Counter.class, clazz, names);
	}
	
	/**
	 * 实例化一个Meter
	 */
	public Meter getMeter(String... names) {
		return getMetric(Meter.class, null, names);
	}
	
	/**
	 * 实例化一个Meter
	 */
	public Meter getMeter(Class<?> clazz, String... names) {
		return getMetric(Meter.class, clazz, names);
	}

	private <T> T getMetric(Class<T> metricClass, Class<?> clazz, String... names) {
		String prefix = (clazz == null ? "" : "." + clazz.getName());
		String key = MetricRegistry.name(metricClass.getName() + prefix , names);
		Metric ret = COMPLIED_METRICS.get(key);
		if (ret != null) {
			return (T) ret;
		}
		if (metricClass == Histogram.class) {
			ret = this.getRegistry().histogram(MetricRegistry.name(prefix, names));
		}
		if (metricClass == Timer.class) {
			ret = this.getRegistry().timer(MetricRegistry.name(prefix, names));
		}
		if (metricClass == Meter.class) {
			ret = this.getRegistry().meter(MetricRegistry.name(prefix, names));
		}
		if (metricClass == Counter.class) {
			ret = this.getRegistry().counter(MetricRegistry.name(prefix, names));
		}
		Metric existing = COMPLIED_METRICS.putIfAbsent(key, ret);
		if (existing != null) {
			ret = existing;
		}
		return (T) ret;
	 }

	/**
	 * 实例化一个Gauge<T>
	 */
	public <T> Gauge<T> getGauge(MetricSupplier<Gauge> supplier, Class<?> clazz, String... names) {
		String key = MetricRegistry.name(clazz , names);
		Metric ret = COMPLIED_METRICS.get(key);
		if (ret != null) {
			return (Gauge<T>) ret;
		}
		ret = this.getRegistry().gauge(key, supplier);
		Metric existing = COMPLIED_METRICS.putIfAbsent(key, ret);
		if (existing != null) {
			ret = existing;
		}
		return (Gauge<T>) ret;
	}
	
	 public <T extends Metric> T register(String name, T metric) throws IllegalArgumentException {
		 return this.getRegistry().register(name, metric);
	 }
	
	 public void registerAll(MetricSet metrics) throws IllegalArgumentException {
		 this.getRegistry().registerAll(metrics);
	 }
	
	 public boolean remove(String name) {
		 return this.getRegistry().remove(name);
	 }
	 
	 public void removeMatching(MetricFilter filter) {
		 this.getRegistry().removeMatching(filter);
	 }
	
	/**
	 * 实例化一个专用于全局上下文的MetricRegistry
	 */
	public static MetricRegistry getContextMetricRegistry() {
		return DEFAULT_REGISTRY;
	}

	/**
	 * 实例化一个专用于全局上下文的HealthCheckRegistry
	 */
	public static HealthCheckRegistry getContextHealthCheckRegistry() {
		return HEALTH_CHECK_REGISTRY;
	}
	
	/**
	 * 实例化一个专用于仪表的registry
	 */
	public static MetricRegistry getGaugeMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Gauges");
	}

	/**
	 * 实例化一个专用于计数器的registry
	 */
	public static MetricRegistry getCounterMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Counters");
	}

	/**
	 * 实例化一个专用于直方图的registry
	 */
	public static MetricRegistry getHistogramMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Histograms");
	}

	/**
	 * 实例化一个专用于速率的registry
	 */
	public static MetricRegistry getMeterMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Meters");
	}

	/**
	 * 实例化一个专用于计时器的registry
	 */
	public static MetricRegistry getTimerMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Timers");
	}

	/**
	 * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
	 */
	public static MetricRegistry getMetricRegistry(String metrics) {
		return SharedMetricRegistries.getOrCreate(metrics);
	}
	
	/**
	 * 实例化一个Histograms
	 */
	public static <T> Histogram histogram(Class<T> clazz, String... names) {
		return getHistogramMetricRegistry().histogram(MetricRegistry.name(clazz, names));
	}

	/**
	 * 实例化一个Timer
	 */
	public static <T> Timer timer(Class<T> clazz, String... names) {
		return getTimerMetricRegistry().timer(MetricRegistry.name(clazz, names));
	}


	/**
	 * 实例化一个counter,同样可以通过如下方式进行实例化再注册进去 Counter jobs = new Counter();
	 * metrics.register(MetricRegistry.name(TestCounter.class, "jobs"), jobs);
	 */
	public static <T> Counter counter(Class<?> clazz, String... names) {
		return getCounterMetricRegistry().counter(MetricRegistry.name(clazz, names));
	}

	/**
	 * 实例化一个Meter
	 */
	public static <T> Meter meter(Class<?> clazz, String... names) {
		return getMeterMetricRegistry().meter(MetricRegistry.name(clazz, names));
	}
	
	/**
	 * 实例化一个Gauge<T>
	 */
	public static <T> Gauge<T> gauge(MetricSupplier<Gauge> supplier, Class<?> clazz, String... names) {
		return getGaugeMetricRegistry().gauge(MetricRegistry.name(clazz , names), supplier);
	}
	
}
