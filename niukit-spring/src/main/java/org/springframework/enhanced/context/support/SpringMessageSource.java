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
package org.springframework.enhanced.context.support;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

/**
 * *******************************************************************
 * @className	： HierarchicalMessageSource
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Mar 2, 2016 12:11:50 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public interface SpringMessageSource{
	
	/**
	 * Set the parent that will be used to try to resolve messages
	 * that this object can't resolve.
	 * @param parent the parent MessageSource that will be used to
	 * resolve messages that this object can't resolve.
	 * May be {@code null}, in which case no further resolution is possible.
	 */
	void setParentMessageSource(SpringMessageSource parent);

	/**
	 * Return the parent of this MessageSource, or {@code null} if none.
	 */
	SpringMessageSource getParentMessageSource();

	String getMessage(String code, Object[] args, String defaultMessage);

	String getMessage(String code, Object[] args) throws NoSuchMessageException;

	String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException;
	
}
