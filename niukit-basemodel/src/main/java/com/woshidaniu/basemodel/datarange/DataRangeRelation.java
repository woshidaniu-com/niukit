package com.woshidaniu.basemodel.datarange;

/**
 * 
 *@类名称	: DataRangeRelation.java
 *@类描述	：数据范围条件关系枚举类  15种: <,<=,>,>=,= ,<>,in ,not in,like %xx%,like %xx,like xx%, [a,b],(a,b),(a,b],[a,b)
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:32:48 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public enum DataRangeRelation {

	/**
	 * 小于 <
	 */
	LESS_THAN{
		public String toString() {
			return " < ";
		}
	},
	/**
	 * 小于等于 <=
	 */
	LESS_THAN_EQUAL{
		public String toString() {
			return " <= ";
		}
	},
	/**
	 * 大于 >
	 */
	GREATER_THAN{
		public String toString() {
			return " > ";
		}
	},
	/**
	 * 大于 等于 >=
	 */
	GREATER_THAN_EQUAL{
		public String toString() {
			return " >= ";
		}
	},
	/**
	 * 等于 =
	 */
	EQUAL{
		
		public String toString() {
			return " = ";
		}
	},
	/**
	 * 不等于 <> 
	 */
	UN_EQUAL{
		public String toString() {
			return " <> ";
		}
	},
	/**
	 * 在范围内 in
	 */
	IN{
		public String toString() {
			return " in ";
		}
	},
	/**
	 * 不在范围内  not in
	 */
	NOT_IN{
		public String toString() {
			return " not in ";
		}
	},
	/**
	 * 全模糊like '%xx%'
	 */
	LIKE{
		public String toString() {
			return " like ";
		}
	},
	/**
	 * 前缀（prefix）模糊like '%xx'
	 */
	LIKE_PREFIX{
		public String toString() {
			return " like ";
		}
	},
	/**
	 * 后缀（suffix）模糊like 'xx%'
	 */
	LIKE_SUFFIX{
		public String toString() {
			return " like ";
		}
	},
	/**
	 * 区间
	 * <code>
	 *  设a，b是两个实数而且a<b。我们规定：
　　	 *	1）满足不等式a≤x≤b的实数x的集合叫做闭区间，表示[a,b]；
	 *　2）满足不等式a<x<b的实数x的集合叫做开区间，表示(a,b﹚；
	 *　3）满足不等式a≤x<b，或a<x≤b的实数x的集合叫做半开半闭区间，表示[a,b),(a,b]。
	 *	4)满足不等式x>a或x<a的实数x的集合叫做无限区间，表示(a,+∞)，(-∞,b)
	 *	5）(+∞,-∞)=R（实数集合）
	 *　这里的实数a与b都叫做相应区间的端点。
	 *  </code>
	 */
	INTERVAL{
		public String toString() {
			return " [] ";
		}
	}
	
}



