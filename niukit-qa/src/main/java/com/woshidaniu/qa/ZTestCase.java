/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.qa;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

/**
 * @类名称 ： ZTestCase.java
 * @类描述 ：
 * @创建人 ：shouquan
 * @创建时间 ：2017年5月15日 下午5:51:37
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */

public class ZTestCase {
	protected String caseIdentifier = "";
	@Rule
	public TestName name = new TestName();
	
	/**
	 * 自定义rule，测试程序运行情况
	 */
	@Rule
	public TestWatcher watchman = new TestWatcher() {
		private long startTime;
		private long endTime;

		protected void starting(Description d) {
			startTime = System.currentTimeMillis();
			caseIdentifier = d.getClassName() + "." + d.getMethodName();
			System.out.println("starting: " + caseIdentifier);
		}

		protected void succeeded(Description d) {
			caseIdentifier = d.getClassName() + " " + d.getMethodName();
			System.out.println("succeeded: " + caseIdentifier);
		}

		protected void failed(Throwable e, Description d) {
			caseIdentifier = d.getClassName() + " " + d.getMethodName();
			System.out.println("failed: " + caseIdentifier);
		}

		protected void finished(Description d) {
			endTime = System.currentTimeMillis();
			caseIdentifier = d.getClassName() + " " + d.getMethodName();
			BigDecimal bigDecimal = new BigDecimal(endTime - startTime).divide(new BigDecimal(1000));

			System.out.println("finished: " + caseIdentifier + ".executed "
					+ bigDecimal.setScale(3, RoundingMode.HALF_DOWN).toPlainString() + " s");
		}

	};
}
