package org.springframework.enhanced.web.multipart.commons;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
/**
 * 
 *@类名称		： MultipartStatusResolver
 *@类描述		：Spring中带进度条的文件上传
 * 	1、Spring的DispatcherServlet在初始化的时候会去容器中查找是否有可用的MultipartResolver，如果有的话就会使用此resolver将request转换为MultipartHttpServletRequest。
 * 	2、Spring提供了两个resolver，CommonsMultipartResolver，StandardServletMultipartResolver。我们可以任选其一。
 *	CommonsMultipartResolver的parseRequest方法调用commons-fileupload的ServletFileupload完成了对request的解析工作。
 * 	3、最后在controller的配置文件中指定resolver： <bean id="multipartResolver" class="com.woshidaniu.spring.web.multipart.commons.MultipartStatusResolver"></bean>
 *@创建人		：kangzhidong
 *@创建时间	：Feb 24, 2017 4:02:02 PM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class MultipartStatusResolver extends CommonsMultipartResolver {
	
	@Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = "utf-8";
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
        }
    }
	
}
