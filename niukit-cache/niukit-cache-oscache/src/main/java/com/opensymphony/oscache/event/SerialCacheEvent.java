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
package com.opensymphony.oscache.event;

import java.io.Serializable;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.events.CacheEvent;

/**
 * 序列信息类
 * @author slzs Nov 29, 2012 9:37:17 AM each engineer has a duty to keep the code elegant
 */
@SuppressWarnings("serial")
public class SerialCacheEvent extends CacheEvent implements Serializable {

	private Cache map = null;

	private CacheEntry entry = null;

	public SerialCacheEvent(Cache map, CacheEntry entry) {
		this(map, entry, null);
	}

	public SerialCacheEvent(Cache map, CacheEntry entry, String origin) {
		super(origin);
		this.map = map;
		this.entry = entry;
	}

	public CacheEntry getEntry() {
		return entry;
	}

	public String getKey() {
		return entry.getKey();
	}

	public Cache getMap() {
		return map;
	}

	public String toString() {
		return "key=" + entry.getKey();
	}
}