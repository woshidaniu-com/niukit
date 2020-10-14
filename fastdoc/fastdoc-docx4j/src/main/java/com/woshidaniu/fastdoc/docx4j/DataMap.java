/*
 * Copyright (c) 2010-2020, kangzhidong (hnxyhcwdl1003@163.com).
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
package com.woshidaniu.fastdoc.docx4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.KeyVal;

public class DataMap {

	Collection<KeyVal> data1 = new ArrayList<KeyVal>();
	Map<String, String> data2 = new HashMap<String, String>();
	Map<String, String> cookies = new HashMap<String, String>();

	public Collection<KeyVal> getData1() {
		return data1;
	}

	public void setData1(Collection<KeyVal> data1) {
		this.data1 = data1;
	}

	public Map<String, String> getData2() {
		return data2;
	}

	public void setData2(Map<String, String> data2) {
		this.data2 = data2;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

}
