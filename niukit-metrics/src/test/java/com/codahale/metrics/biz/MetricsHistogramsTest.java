package com.codahale.metrics.biz;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;


/**
 * 测试Histograms
 * Histograms主要使用来统计数据的分布情况，最大值、最小值、平均值、中位数，百分比（75%、90%、95%、98%、99%和99.9%）。
 * 例如，需要统计某个页面的请求响应时间分布情况，可以使用该种类型的Metrics进行统计。
 * 具体的样例代码如下：
 */
public class MetricsHistogramsTest {
    /**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry metrics = MetricsFactory.getHistogramMetricRegistry();

    /**
     * 在控制台上打印输出
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();

    /**
     * 实例化一个Histograms
     */
    private static final Histogram randomNums = metrics.histogram(MetricRegistry.name(MetricsHistogramsTest.class, "random"));

    public static void handleRequest(double random) {
        randomNums.update((int) (random*100));
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);
        Random rand = new Random();
        while(true){
            handleRequest(rand.nextDouble());
            Thread.sleep(100);
        }
    }

}