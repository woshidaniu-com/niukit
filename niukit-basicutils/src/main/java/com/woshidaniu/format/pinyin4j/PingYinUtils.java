package com.woshidaniu.format.pinyin4j;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 * 
 *@类名称	: PingYinUtils.java
 *@类描述	：基于pinyin4j的中文分词和转换工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:50:29 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class PingYinUtils {


	public static String hanziToPinyin(String hanzi) {
		return hanziToPinyin(hanzi, " ");
	}

	/**
	 * 将汉字转换成拼音
	 * 
	 * @param hanzi
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String hanziToPinyin(String hanzi, String separator) {
		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String pinyingStr = "";
		try {
			pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat,separator);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinyingStr;
	}

	/**
	 * 将字符串数组转换成字符串
	 * 
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String stringArrayToString(String[] str, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if (str.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 简单的将各个字符数组之间连接起来
	 * 
	 * @param str
	 * @return
	 */
	public static String stringArrayToString(String[] str) {
		return stringArrayToString(str, "");
	}

	/**
	 * 将字符数组转换成字符串
	 * 
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String charArrayToString(char[] ch, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			sb.append(ch[i]);
			if (ch.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符数组转换成字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String charArrayToString(char[] ch) {
		return charArrayToString(ch, " ");
	}

	/**
	 * 取汉字的首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否是大写
	 * @return
	 */
	public static char[] getHeadByChar(char src, boolean isCapital) {
		// 如果不是汉字直接返回
		if (src <= 128) {
			return new char[] { src };
		}
		// 获取所有的拼音
		String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src);
		// 创建返回对象
		int polyphoneSize = pinyingStr.length;
		char[] headChars = new char[polyphoneSize];
		int i = 0;
		// 截取首字符
		for (String s : pinyingStr) {
			char headChar = s.charAt(0);
			// 首字母是否大写，默认是小写
			if (isCapital) {
				headChars[i] = Character.toUpperCase(headChar);
			} else {
				headChars[i] = headChar;
			}
			i++;
		}

		return headChars;
	}

	/**
	 * 取汉字的首字母(默认是大写)
	 * 
	 * @param src
	 * @return
	 */
	public static char[] getHeadByChar(char src) {
		return getHeadByChar(src, true);
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @return
	 */
	public static String[] getHeadByString(String src) {
		return getHeadByString(src, true);
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital) {
		return getHeadByString(src, isCapital, null);
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital,
			String separator) {
		char[] chars = src.toCharArray();
		String[] headString = new String[chars.length];
		int i = 0;
		for (char ch : chars) {

			char[] chs = getHeadByChar(ch, isCapital);
			StringBuffer sb = new StringBuffer();
			if (null != separator) {
				int j = 1;

				for (char ch1 : chs) {
					sb.append(ch1);
					if (j != chs.length) {
						sb.append(separator);
					}
					j++;
				}
			} else {
				sb.append(chs[0]);
			}
			headString[i] = sb.toString();
			i++;
		}
		return headString;
	}

	/**
	 * 字符串集合转换字符串(逗号分隔)
	 * 
	 * @author wyh
	 * @param stringSet
	 * @return
	 */
	public static String makeStringByStringSet(Set<String> stringSet) {
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (String s : stringSet) {
			if (i == stringSet.size() - 1) {
				str.append(s);
			} else {
				str.append(s + ",");
			}
			i++;
		}
		return str.toString().toLowerCase();
	}

	/**
	 * 获取拼音集合
	 * 
	 * @author wyh
	 * @param src
	 * @return Set<String>
	 */
	public static Set<String> getPinyin(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			char[] srcChar;
			srcChar = src.toCharArray();
			// 汉语拼音格式输出类
			HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

			// 输出设置，大小写，音标方式等
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			String[][] temp = new String[src.length()][];
			for (int i = 0; i < srcChar.length; i++) {
				char c = srcChar[i];
				// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
				if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
					try {
						temp[i] = PinyinHelper.toHanyuPinyinStringArray(
								srcChar[i], hanYuPinOutputFormat);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else if (((int) c >= 65 && (int) c <= 90)
						|| ((int) c >= 97 && (int) c <= 122)) {
					temp[i] = new String[] { String.valueOf(srcChar[i]) };
				} else {
					temp[i] = new String[] { "" };
				}
			}
			String[] pingyinArray = Exchange(temp);
			Set<String> pinyinSet = new HashSet<String>();
			for (int i = 0; i < pingyinArray.length; i++) {
				pinyinSet.add(pingyinArray[i]);
			}
			return pinyinSet;
		}
		return null;
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	public static String[] Exchange(String[][] strJaggedArray) {
		String[][] temp = DoExchange(strJaggedArray);
		return temp[0];
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	private static String[][] DoExchange(String[][] strJaggedArray) {
		int len = strJaggedArray.length;
		if (len >= 2) {
			int len1 = strJaggedArray[0].length;
			int len2 = strJaggedArray[1].length;
			int newlen = len1 * len2;
			String[] temp = new String[newlen];
			int Index = 0;
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
					Index++;
				}
			}
			String[][] newArray = new String[len - 1][];
			for (int i = 2; i < len; i++) {
				newArray[i - 1] = strJaggedArray[i];
			}
			newArray[0] = temp;
			return DoExchange(newArray);
		} else {
			return strJaggedArray;
		}
	}

	/**
	 * 
	 * <p>方法说明：汉字转拼音<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:35:12<p>
	 * @param str 需要转换的字符串 
	 * @return 拼音字符串
	 */
	public static String converterToFUllSpell(String str) {
		return converterToFUllSpell(str, HanyuPinyinCaseType.LOWERCASE,HanyuPinyinToneType.WITHOUT_TONE);
	}

	
	/**
	 * 
	 * <p>方法说明：汉字转拼音<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:34:35<p>
	 * @param str 需要转换的字符串 
	 * @param caseType 大小写 {@link net.sourceforge.pinyin4j.format.HanyuPinyinCaseType}
	 * @return 拼音字符串
	 */
	public static String converterToFUllSpell(String str,HanyuPinyinCaseType caseType) {
		return converterToFUllSpell(str, caseType,HanyuPinyinToneType.WITHOUT_TONE);
	}

	
	/**
	 * 
	 * <p>方法说明：汉字转拼音<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:12:24<p>
	 * @param src 需要转换的字符串 
	 * @param caseType 大小写 {@link net.sourceforge.pinyin4j.format.HanyuPinyinCaseType}
	 * @param toneType 声调 {@link net.sourceforge.pinyin4j.format.HanyuPinyinToneType}
	 * @return 拼音字符串
	 */
	public static String converterToFUllSpell(String src,HanyuPinyinCaseType caseType, HanyuPinyinToneType toneType) {
		char[] t1 = src.toCharArray();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(caseType);// 大写||小写
		format.setToneType(toneType);// 第几声
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0 , j = t1.length; i < j; i++) {
				// 判断是否为汉字字符函数
				if (t1[i] >= 0x4e00 && t1[i] <= 0x9fbb) {
					sb.append(PinyinHelper.toHanyuPinyinStringArray(t1[i], format)[0]);
					sb.append(" ");
				} else {
					sb.append(Character.toString(t1[i]));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}

	
	/**
	 * 
	 * <p>方法说明：汉字转换为汉语拼音首字母（小写），英文字符不变<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:16:00<p>
	 * @param chines 中文字符串
	 * @return 拼音简写字符串
	 */
	public static String converterToFirstSpell(String chines) {
		StringBuilder pinyinName = new StringBuilder();
		// 转化为字符
		char[] nameChar = chines.toCharArray();
		// 汉语拼音格式输出类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置,大小写,音标方式等
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String[] hanyuPinyinStringArray = null;
		for (int i = 0; i < nameChar.length; i++) {
			// 如果是中文
			if (nameChar[i] >= 0x4e00 && nameChar[i] <= 0x9fbb) {
				try {
					hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if(hanyuPinyinStringArray != null){
						pinyinName.append(hanyuPinyinStringArray[0].charAt(0));
					}else{
						pinyinName.append(nameChar[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
					pinyinName.append(nameChar[i]);
				}
			} else {// 为英文字符
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

	
	/**
	 *  
	 * <p>方法说明：汉字转换为汉语拼音首字母（大写），英文字符不变<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:38:23<p>
	 * @param chines 待转换字符串
	 * @return 拼音首字母（大写）
	 */
	public static String converterToSpell(String chines) {
		StringBuilder pinyinName = new StringBuilder();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] >= 0x4e00 && nameChar[i] <= 0x9fbb) {
				try {
					String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if(hanyuPinyinStringArray != null){
						pinyinName.append(hanyuPinyinStringArray[0].charAt(0));
					}else{
						pinyinName.append(nameChar[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
					pinyinName.append(nameChar[i]);
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

	
	/**
	 * 
	 * <p>方法说明：将字符串转换成拼音数组<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午5:56:16<p>
	 * @param src 中文字符串
	 * @return 拼音数组
	 */
	public static String[] stringToPinyin(String src) {
		return stringToPinyin(src, false, null);
	}

	
	/**
	 * 
	 * <p>方法说明：将字符串转换成拼音数组<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午7:09:42<p>
	 * @param src 待转换字符
	 * @param separator 多音字拼音之间的分隔符
	 * @return 拼音数组
	 */
	public static String[] stringToPinyin(String src, String separator) {
		return stringToPinyin(src, true, separator);
	}

	
	/**
	 * 
	 * <p>方法说明：将字符串转换成拼音数组<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午7:10:18<p>
	 * @param src 待转换字符
	 * @param isPolyphone 是否查出多音字的所有拼音
	 * @param separator 多音字拼音之间的分隔符
	 * @return 拼音数组
	 */
	public static String[] stringToPinyin(String src, boolean isPolyphone,
			String separator) {
		// 判断字符串是否为空
		if ("".equals(src) || null == src) {
			return null;
		}
		char[] srcChar = src.toCharArray();
		int srcCount = srcChar.length;
		String[] srcStr = new String[srcCount];

		for (int i = 0; i < srcCount; i++) {
			if (srcChar[i] >= 0x4e00 && srcChar[i] <= 0x9fbb) {
				srcStr[i] = charToPinyin(srcChar[i], isPolyphone, separator);
			} else {
				srcStr[i] = String.valueOf(srcChar[i]);
			}
			
		}
		return srcStr;
	}

	
	
	/**
	 * 
	 * <p>方法说明：将单个字符转换成拼音<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月12日下午7:10:55<p>
	 * @param src 待转换字符
	 * @param isPolyphone 是否查出多音字
	 * @param separator 多音字分隔符
	 * @return 拼音字符
	 */
	public static String charToPinyin(char src, boolean isPolyphone,
			String separator) {
		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		StringBuffer tempPinying = new StringBuffer();
		// 如果是中文
		try {
			// 转换得出结果
			String[] strs = PinyinHelper.toHanyuPinyinStringArray(src,defaultFormat);
			// 是否查出多音字，默认是查出多音字的第一个字符
			if (isPolyphone && null != separator) {
				for (int i = 0; i < strs.length; i++) {
					tempPinying.append(strs[i]);
					if (strs.length != (i + 1)) {
						// 多音字之间用特殊符号间隔起来
						tempPinying.append(separator);
					}
				}
			} else {
				tempPinying.append(strs[0]);
			}

		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return tempPinying.toString();
	}
}