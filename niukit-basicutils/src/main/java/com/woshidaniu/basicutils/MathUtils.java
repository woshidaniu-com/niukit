/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.basicutils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *@类名称	: MathUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 11, 2016 5:02:33 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class MathUtils {

	private static String part_num = "\\d*|0.\\d*[1-9]\\d*|[1-9]\\d*.\\d*";
	//(±a,±b),(±a,+∞),(-∞,±b)
	private static Pattern open_interval_pattern_1 = Pattern.compile("^\\((?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + ")))\\)$");
	private static Pattern open_interval_pattern_2 = Pattern.compile("^\\((?:([-+]?(?:" + part_num + ")))\\,(?:([+]∞))\\)$");
	private static Pattern open_interval_pattern_3 = Pattern.compile("^\\((?:([-]∞))\\,(?:([-+]?(?:" + part_num + ")))\\)$");
	//[±a,±b]
	private static Pattern closed_interval_pattern = Pattern.compile("^\\[(?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + ")))\\]$");
	//(±a,±b],(-∞,±b]
	private static Pattern left_half_open_interval_pattern = Pattern.compile("^\\((?:([-+]?(?:" + part_num + "|∞)))\\,(?:([-+]?(?:" + part_num + ")))\\]$");
	//[±a,±b),[±a,+∞)
	private static Pattern right_half_open_interval_pattern = Pattern.compile("^\\[(?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + "|∞)))\\)$");
	/**
	 * 正无穷大符号：+∞
	 */
	public static final String POSITIVE_INFINITE = "+∞";
	/**
	 * 负无穷大符号：-∞
	 */
	public static final String NEGATIVE_INFINITE = "-∞";
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;
		
	/**
	 * 开区间(±a,±b);不能是 (+∞,-∞) (+∞,+b) (-a,-∞)
	 */
	public static Matcher open_interval(String toMatchStr) {
		Matcher matcher_1 = open_interval_pattern_1.matcher(toMatchStr);
		if(matcher_1.matches()){
			return matcher_1;
		}
		Matcher matcher_2 = open_interval_pattern_2.matcher(toMatchStr);
		if(matcher_2.matches()){
			return matcher_2;
		}
		return open_interval_pattern_3.matcher(toMatchStr);
	}
	
	/**
	 * 闭区间[±a,±b];不能是 [±∞,±∞] [±a,±∞] [±a,±∞] [±∞,±b] [±∞,±b]
	 */ 
	public static Matcher closed_interval(String toMatchStr) {
		return closed_interval_pattern.matcher(toMatchStr);
	}
	
	/**
	 * 左半开半闭区间(±a,±b];不能是(±∞,±∞] (±a,±∞]
	 */
	public static Matcher left_half_open_interval(String toMatchStr) {
		return left_half_open_interval_pattern.matcher(toMatchStr);
	}
	
	/**
	 * 右半开半闭区间[±a,±b);不能是 [±a,±∞) [±∞,±∞) [±∞,±b)
	 */
	public static Matcher right_half_open_interval(String toMatchStr) {
		return right_half_open_interval_pattern.matcher(toMatchStr);
	}
	
	/**
	 * 区间范围检查：
	 * 如检查数字 48 是否在区间[12,+∞)中
	 */
	public static boolean contains(String x,String interval) {
		
		BigDecimal bigValue = new BigDecimal(x);
		// 开区间(±a,±b),(±a,+∞),(-∞,±b)
		Matcher matcher2 = open_interval(interval);
		if (matcher2.matches()) {
			String a = matcher2.group(1), b = matcher2.group(2);
			//(±a,+∞)
			if(POSITIVE_INFINITE.equals(b)){
				//传入的值x必须满足： x > a；才算在该区间内
				return bigValue.compareTo(new BigDecimal(a)) == 1;
			}
			//(-∞,±b)
			else if(NEGATIVE_INFINITE.equals(a)){
				//传入的值x必须满足： x < b；才算在该区间内
				return bigValue.compareTo(new BigDecimal(b)) == -1;
			}
			//(±a,±b)
			else{
				//传入的值x必须满足： a < x < b；才算在该区间内
				return bigValue.compareTo(new BigDecimal(a)) == 1 && bigValue.compareTo(new BigDecimal(b)) == -1;
			}
		}
		
		// 闭区间[±a,±b]
		Matcher matcher1 = closed_interval(interval);
		if (matcher1.matches()) {
			String a = matcher1.group(1), b = matcher1.group(2);
			BigDecimal bigA = new BigDecimal(a);
			BigDecimal bigB = new BigDecimal(b);
			//传入的值x必须满足：a ≤ x ≤ b；才算在该区间内
			return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0) && (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
		}
		
		// 左半开半闭区间(±a,±b],(-∞,±b]
		Matcher matcher3 = left_half_open_interval(interval);
		if (matcher3.matches()) {
			String a = matcher3.group(1), b = matcher3.group(2);
			BigDecimal bigB = new BigDecimal(b);
			//(-∞,±b]
			if(NEGATIVE_INFINITE.equals(a)){
				//传入的值x必须满足： x ≤ b；才算在该区间内
				return (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
			}
			//(±a,±b]
			else{
				BigDecimal bigA = new BigDecimal(a);
				//传入的值x必须满足： a < x ≤ b；才算在该区间内
				return (bigValue.compareTo(bigA) == 1) && (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
			}
		}
		
		// 右半开半闭区间[±a,±b),[±a,+∞)
		Matcher matcher4 = right_half_open_interval(interval);
		if (matcher4.matches()) {
			String a = matcher4.group(1), b = matcher4.group(2);
			BigDecimal bigA = new BigDecimal(a);
			//[±a,+∞)
			if(POSITIVE_INFINITE.equals(b)){
				//传入的值x必须满足： x ≥ a；才算在该区间内
				return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0);
			}
			//[±a,±b)
			else{
				BigDecimal bigB = new BigDecimal(b);
				//传入的值x必须满足： a ≤ x < b；才算在该区间内
				return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0) && (bigValue.compareTo(bigB) == -1);
			}
		}
		
		return false;
	}
	
	public static boolean isDigit(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2  乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	
	public static void main(String[] args) {
		//System.out.println("pattern:" + open_interval_pattern_1.pattern());
		//System.out.println("pattern:" + open_interval_pattern_2.pattern());
		//System.out.println("pattern:" + open_interval_pattern_3.pattern());
		//System.out.println("pattern:" + closed_interval_pattern.pattern());
		//System.out.println("pattern:" + left_half_open_interval_pattern.pattern());
		//System.out.println("pattern:" + right_half_open_interval_pattern.pattern());
		System.out.println(MathUtils.contains("78","[15," + POSITIVE_INFINITE + ")"));
		System.out.println(new BigDecimal("-45").toPlainString());
	}
		 
	
}
