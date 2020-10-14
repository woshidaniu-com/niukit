/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
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
package com.woshidaniu.db.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 表主键标识
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {

	/**
	 * 
	 * 主键ID，默认 true 数据库自增
	 * 
	 * <p>
	 * 设置为 false 需要用户传入 ID 内容，工具包 IdWorker 可产品全局唯一 ID
	 * </p>
	 * 
	 * {@link IdWorker}
	 * 
	 */
	boolean auto() default true;

}
