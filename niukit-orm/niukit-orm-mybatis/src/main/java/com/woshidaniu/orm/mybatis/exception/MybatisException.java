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
package com.woshidaniu.orm.mybatis.exception;

import com.woshidaniu.basicutils.exception.NestedRuntimeException;


/**
 * 
 *@类名称	: MybatisException.java
 *@类描述	：自定义Mybatis 异常类
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 11:22:20 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class MybatisException extends NestedRuntimeException {

	private static final long serialVersionUID = 1L;

	public MybatisException(String message) {
		super(message);
	}

	public MybatisException(Throwable throwable) {
		super(throwable);
	}

	public MybatisException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
