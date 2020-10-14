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
package org.springframework.enhanced.web.multipart.commons;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
/**
 * *******************************************************************
 * @className	： MyMultipartResolver
 * @description	： Spring中带进度条的文件上传
 * 	1、Spring的DispatcherServlet在初始化的时候会去容器中查找是否有可用的MultipartResolver，如果有的话就会使用此resolver将request转换为MultipartHttpServletRequest。
 * 	2、Spring提供了两个resolver，CommonsMultipartResolver，StandardServletMultipartResolver。我们可以任选其一。
 *	CommonsMultipartResolver的parseRequest方法调用commons-fileupload的ServletFileupload完成了对request的解析工作。
 * 	3、最后在controller的配置文件中指定resolver： <bean id="multipartResolver" class="org.springframework.enhanced.web.multipart.commons.MyMultipartResolver"></bean> 
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Jan 19, 2017 9:11:31 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class MyMultipartResolver extends CommonsMultipartResolver {
	
	@Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        /*String encoding = "utf-8";
        FileUpload fileUpload = super.prepareFileUpload(encoding);
        final HttpSession session = request.getSession();
        fileUpload.setProgressListener(new ProgressListener() {
            public void update(long pBytesRead, long pContentLength, int pItems) {
                int percent = (int) (((float)pBytesRead / (float)pContentLength) * 100);
                session.setAttribute("percent", percent + "%");
            }
        });
        
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return super.parseFileItems(fileItems, encoding);
        }
        catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        }
        catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }*/
		return null;
    }
	
}
