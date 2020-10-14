package com.woshidaniu.qa.core;

/**
 * @author
 * @version 创建时间：2017年5月12日 下午2:29:59 类说明
 */

public abstract class ClazzAroundObject {
	private Class claz;

	protected ClazzAroundObject(Class clazz) {
		this.claz = clazz;
	}

	@Override
	public int hashCode() {
		int result = 31 + ((claz == null) ? 0 : claz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			return claz == ((ClazzAroundObject) obj).claz;
		}
	}

	public Class getClazz() {
		return this.claz;
	}

	public void clean() {
		this.claz = null;
	}

	public static class ClazzBeforeObject extends ClazzAroundObject {
		public ClazzBeforeObject(Class clazz) {
			super(clazz);
		}
	}

	public static class ClazzAfterObject extends ClazzAroundObject {
		protected ClazzAfterObject(Class clazz) {
			super(clazz);
		}
	}
}
