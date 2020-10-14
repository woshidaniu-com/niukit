/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.core;

/**
 *@类名称	: OperatorType.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 24, 2016 10:14:08 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public final class OperatorType {
	private OperatorType() {
		// no instances of this class
	}

	public static final int BETWEEN = 0x00;
	public static final int NOT_BETWEEN = 0x01;
	public static final int EQUAL = 0x02;
	public static final int NOT_EQUAL = 0x03;
	public static final int GREATER_THAN = 0x04;
	public static final int LESS_THAN = 0x05;
	public static final int GREATER_OR_EQUAL = 0x06;
	public static final int LESS_OR_EQUAL = 0x07;
	/** default value to supply when the operator type is not used */
	public static final int IGNORED = BETWEEN;
	
	/* package */ public static void validateSecondArg(int comparisonOperator, String paramValue) {
		switch (comparisonOperator) {
			case BETWEEN:
			case NOT_BETWEEN:
				if (paramValue == null) {
					throw new IllegalArgumentException("expr2 must be supplied for 'between' comparisons");
				}
			// all other operators don't need second arg
		}
	}
}
