package com.woshidaniu.cache.core.utils;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.cache.core.Destroyable;

/**
 * *******************************************************************
 * 
 * @className ： LifecycleUtils
 * @description ： TODO(描述这个类的作用)
 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date ： Oct 31, 2016 9:15:26 AM
 * @version V1.0
 * *******************************************************************
 */
@SuppressWarnings("rawtypes")
public abstract class LifecycleUtils {

	private static final Logger LOG = LoggerFactory.getLogger(LifecycleUtils.class);

	public static void destroy(Object o) {
		if (o instanceof Destroyable) {
			destroy((Destroyable) o);
		} else if (o instanceof Collection) {
			destroy((Collection) o);
		}
	}

	public static void destroy(Destroyable d) {
		if (d != null) {
			try {
				d.destroy();
			} catch (Throwable t) {
				if (LOG.isDebugEnabled()) {
					String msg = "Unable to cleanly destroy instance [" + d + "] of type [" + d.getClass().getName() + "].";
					LOG.debug(msg, t);
				}
			}
		}
	}

	/**
	 * Calls {@link #destroy(Object) destroy} for each object in the collection.
	 * If the collection is {@code null} or empty, this method returns quietly.
	 *
	 * @param c
	 *            the collection of objects to destroy.
	 * @since 0.9
	 */
	public static void destroy(Collection c) {
		if (c == null || c.isEmpty()) {
			return;
		}

		for (Object o : c) {
			destroy(o);
		}
	}

}
