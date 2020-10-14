package com.woshidaniu.qa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.databene.feed4junit.Feeder;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.woshidaniu.qa.annotation.Order;

/**
 * @author shouquan
 * @version 创建时间：2017年5月15日 上午10:42:30 类说明
 */
public class ZTesterRuner extends Feeder {

	/**
	 * @param testClass
	 * @throws InitializationError
	 */
	public ZTesterRuner(Class<?> testClass) throws InitializationError {
		super(testClass);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> list = super.computeTestMethods();
		List<FrameworkMethod> copy = new ArrayList<FrameworkMethod>(list);
		Collections.sort(copy, new Comparator<FrameworkMethod>() {

			@Override
			public int compare(FrameworkMethod o1, FrameworkMethod o2) {
				// TODO Auto-generated method stub
				Order order1 = o1.getMethod().getAnnotation(Order.class);
				Order order2 = o2.getMethod().getAnnotation(Order.class);
				if (order1 != null && order2 != null) {
					if (order1.index() == order2.index()) {
						return 0;
					} else if (order1.index() > order2.index()) {
						return 1;
					} else {
						return -1;
					}
				} else {
					return 0;
				}
			}
		});
		return copy;
	}
	
}
