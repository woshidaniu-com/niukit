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
package org.springframework.enhanced.context.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.enhanced.utils.SystemClock;

public class EventPoint {
	
	public static final EventPoint ROOT = new EventPoint("root" , "Event Source");
	
	/**
	 * 上一个事件
	 */
	protected EventPoint prev;
	/**
	 * 当前事件UID
	 */
	protected String uid;
	/**
	 * 当前事件发生时间
	 */
	protected long timestamp;
	/**
	 * 当前事件消息
	 */
	protected String message;
	/**
	 * 当前事件绑定数据
	 */
	protected Map<String, Object> data;
	
	public EventPoint(String uid, String message) {
		this(null, uid, message);
	}
	
	public EventPoint(EventPoint prev, String uid, String message) {
		this(prev, uid, SystemClock.now(), message);
	}
	
	public EventPoint(String uid, long timestamp, String message) {
		this(null, uid, timestamp, message);
	}
	
	public EventPoint(EventPoint prev, String uid, long timestamp, String message) {
		this(prev, uid, timestamp, message, null);
	}
	
	public EventPoint(String uid, String message, Map<String, Object> data) {
		this(null, uid, message, data);
	}
	
	public EventPoint(EventPoint prev, String uid, String message, Map<String, Object> data) {
		this(prev, uid, SystemClock.now(), message, data);
	}
	
	public EventPoint(String uid, long timestamp, String message, Map<String, Object> data) {
		this(null, uid, SystemClock.now(), message, data);
	}
	
	public EventPoint(EventPoint prev, String uid, long timestamp, String message, Map<String, Object> data) {
		this.prev = prev;
		this.uid = uid;
		this.timestamp = timestamp;
		this.message = message;
		this.data = data == null ? new HashMap<String, Object>() : data;
	}

	public EventPoint getPrev() {
		return prev;
	}

	public void setPrev(EventPoint prev) {
		this.prev = prev;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void put(String key, Object value) {
		getData().put(key, value);
	}
	
}
