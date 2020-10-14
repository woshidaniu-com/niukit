package org.springframework.enhanced.aop;

/**
 *@类名称:Operation.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：2014-7-24 上午09:39:20
 *@版本号:v1.0
 */

public enum Operation {

	/**
	 * 查询操作
	 */
	OP_SELECT {
		public String toString() {
			return "select:查询";
		}
	},
	/**
	 * 刪除 操作
	 */
	OP_DELETE {
		public String toString() {
			return "delete:删除";
		}
	},
	/**
	 * 更改操作
	 */
	OP_UPDATE {
		public String toString() {
			return "update:更新";
		}
	},
	/**
	 * 插入操作
	 */
	OP_INSERT {
		public String toString() {
			return "insert:新增";
		}
	}
	
}