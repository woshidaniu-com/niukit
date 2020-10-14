/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.enhanced.utils;


public class StringUtils extends org.springframework.util.StringUtils {
	
	public static String getSafeObj(Object str) {
		return (isEmpty(str) || !(str instanceof String)) ? null : str.toString();
	}

	public static String getSafeStr(Object str) {
		return (isEmpty(str) || !(str instanceof String)) ? "" : str.toString();
	}

	public static String getSafeStr(String str) {
		return isEmpty(str) ? "" : str;
	}

	public static String getSafeStr(Object str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str.toString();
	}

	public static int getSafeInt(String str, String defaultStr) {
		return Integer.parseInt(isEmpty(str) ? defaultStr : str);
	}

	public static float getSafeFloat(String str, String defaultStr) {
		return Float.parseFloat(isEmpty(str) ? defaultStr : str);
	}

	public static long getSafeLong(Object str, String defaultStr) {
		return Long.parseLong(isEmpty(str) ? defaultStr : str
				.toString());
	}

	public static boolean getSafeBoolean(Object str, String defaultStr) {
		return Boolean.parseBoolean(isEmpty(str) ? defaultStr : str .toString());
	}
	
}
