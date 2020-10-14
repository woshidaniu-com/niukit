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
package com.opensymphony.oscache;

import java.io.Serializable;

import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;

/**
 * *******************************************************************
 * @className	： CacheConstants
 * @description	： oscache分布式缓存通知所需部分常量        :note each engineer has a duty to keep the code elegant     
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Nov 5, 2016 3:02:45 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class CacheConstants extends ClusterNotification{         
  
	public CacheConstants(int type, Serializable data) {
		super(type, data);
	}

	/**         
	* 添加缓存对象操作         
	*/
	public final static int ACTION_ADD_OBJ = 5;         
	/**         
	* 更新缓存对象操作         
	*/
	public final static int ACTION_UPDATE_OBJ = 6;         
	/**         
	* 删除缓存对象操作         
	*/
	public final static int ACTION_DELETE_OBJ = 7;         
	/**         
	* 刷新缓存对象         
	*/
	public final static int ACTION_FLUSH_OBJ = 8;         
	  
	/**         
	* 集群entry add处理         
	*/
	public final static int CLUSTER_ENTRY_ADD = 9;         
	  
	/**         
	* 集群entry update处理         
	*/
	public final static int CLUSTER_ENTRY_UPDATE = 10;         
	  
	/**         
	* 集群entry delete处理         
	*/
	public final static int CLUSTER_ENTRY_DELETE = 11;
	
	
	/**         
	* 添加缓存对象操作         
	*//*
	public final static int ACTION_ADD_OBJ = 1;         
	*//**         
	* 更新缓存对象操作         
	*//*
	public final static int ACTION_UPDATE_OBJ = 2;         
	*//**         
	* 删除缓存对象操作         
	*//*
	public final static int ACTION_DELETE_OBJ = 3;         
	*//**         
	* 刷新缓存对象         
	*//*
	public final static int ACTION_FLUSH_OBJ = 4;         
	  
	*//**         
	* 集群entry add处理         
	*//*
	public final static int CLUSTER_ENTRY_ADD = 20;         
	  
	*//**         
	* 集群entry update处理         
	*//*
	public final static int CLUSTER_ENTRY_UPDATE = 21;         
	  
	*//**         
	* 集群entry delete处理         
	*//*
	public final static int CLUSTER_ENTRY_DELETE = 22;*/
	
}
