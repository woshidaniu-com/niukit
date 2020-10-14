package com.woshidaniu.basicutils;

public abstract class StrengthUtils {

	// bitTotal函数
	// 计算出当前密码当中一共有多少种模式
	private static int bitTotal(int num) {
		int modes = 0;
		for (int i = 0; i < 4; i++) {
			if ((num & 1) == 1) {
				modes++;
			}
			num >>>= 1;
		}
		return modes;
	}

	// CharMode函数
	// 测试某个字符是属于哪一类.
	private static int CharMode(char iN) {
		if (iN >= 48 && iN <= 57) { // 数字
			return 1;
		}
		if (iN >= 65 && iN <= 90) { // 大写字母
			return 2;
		}
		if (iN >= 97 && iN <= 122) { // 小写
			return 4;
		} else {
			return 8; // 特殊字符
		}
	}

	// checkStrong函数
	// 返回密码的强度级别
	public static int getStrength(String pwd) {
		if (pwd.length() < 6) {
			return 0; // 密码太短
		}
		int Modes = 0;
		for (int i = 0; i < pwd.length(); i++) {
			// 测试每一个字符的类别并统计一共有多少种模式.
			Modes |= CharMode(pwd.charAt(i));
		}
		return bitTotal(Modes);
	}

	public static void main(String[] args) {
		System.out.println("woshidaniu123密码强度：" + StrengthUtils.getStrength("woshidaniu123"));
	}

}
