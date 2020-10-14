package com.woshidaniu.spring.web.servlet.handler;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.woshidaniu.io.utils.FilemimeUtils;
import com.woshidaniu.io.utils.FiletypeUtils;

/**
 *  参考 http://www.cnblogs.com/com-itheima-crazyStone/p/6807342.html
 *  
 *  补充：文件上传大小限制tomcat服务器bug问题/
 *
 *	1、在使用文件上传大小限制及异常捕获配置后，当上传文件大小超过限制一定范围后，可以正确捕获异常并且跳转到指定页面，但是当上传文件超大（严重超出上传大小限制）时，就可能出现关于MaxUploadSizeExceededException 死循环状态，此时页面直接崩溃。
 *	2、针对Spring MVC文件上传大小限制出现的这个问题，可以换用tomcat7.0.39+版本的tomcat服务器；或者不限制大小，采用拦截器判断处理。
 *	
 */
public class FileUploadAcceptInterceptor extends HandlerInterceptorAdapter implements MessageSourceAware {

    protected static final Logger LOG = LoggerFactory.getLogger(FileUploadAcceptInterceptor.class);
    
    protected MessageSource messageSource;
    
	/** 指定所上传文件的总大小。注意maxUploadSize属性的限制不是针对单个文件，而是单次请求中所有文件的容量之和 ；单位：字节*/  
	protected Long maxUploadSize = -1L;
	/** 指定所上传单个文件的大小。单位：字节 */
	protected Long maxUploadSizePerFile = -1L;
	/** 指定是否使用严格模式校验文件内容是否匹配文件类型和mineType **/
	protected boolean useStrict = false;
	
	protected PathMatcher matcher = new AntPathMatcher();
	protected Set<String> allowedTypesSet = Collections.emptySet();
	protected Set<String> allowedExtensionsSet = Collections.emptySet();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 判断是否是文件上传请求
		if(request instanceof MultipartHttpServletRequest){
			//获取Locale
			Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);  
			
			MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
			//获取上传文件尺寸大小
            long requestSize = multipartRequest.getContentLength();
            if (maxUploadSize != null && maxUploadSize > 0 && requestSize > maxUploadSize) {
                //当上传文件总大小超过指定大小限制后，模拟抛出MaxUploadSizeExceededException异常
                throw new MaxUploadSizeExceededException(maxUploadSize);
            }
            
			Map<String, MultipartFile> files = multipartRequest.getFileMap();
			Iterator<String> iterator = files.keySet().iterator();
			// 对文件上传请求进行遍历
			while (iterator.hasNext()) {
				// 前端字段对应的key
				String formKey = iterator.next();
				// 上传文件对象
				MultipartFile multipartFile = files.get(formKey);
				// 文件类型检查
				if( !acceptFile(multipartFile, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getName(), locale)){
					return false;
				};
			}
		}
		return true;
	}
	
	protected boolean acceptFile(MultipartFile file, String filename, String contentType, String inputName, Locale locale) throws IOException {
        
		boolean fileIsAcceptable = false;
        
        // 1、检查上传的文件是否存在
        if (file == null) {
            String errMsg = messageSource.getMessage("springmvc.messages.error.uploading", new String[]{inputName}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
        } 
        // 2、检查上传文件是否超出大小限制
        else if (maxUploadSizePerFile != null && maxUploadSizePerFile > 0 &&  maxUploadSizePerFile < file.getSize()) {
            String errMsg = messageSource.getMessage("springmvc.messages.error.file.too.large", new String[]{inputName, filename, file.getName(), "" + file.getSize(), getMaximumSizeStr(locale)}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
        } 
        // 3、检查上传文件是否是允许的mimetypes
        else if ((!allowedTypesSet.isEmpty()) && (!containsItem(allowedTypesSet, contentType))) {
            String errMsg = messageSource.getMessage("springmvc.messages.error.content.type.not.allowed", new String[]{inputName, filename, file.getName(), contentType}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
        } 
        // 4、检查上传文件是否是允许的文件后缀
        else if ((!allowedExtensionsSet.isEmpty()) && (!hasAllowedExtension(allowedExtensionsSet, filename))) {
            String errMsg = messageSource.getMessage("springmvc.messages.error.file.extension.not.allowed", new String[]{inputName, filename, file.getName(), contentType}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
        } 
        // 5、检查是否伪造的文件类型；即不安全文件修改为安全文件后缀
        else if (useStrict == true && !FilenameUtils.getExtension(filename).equalsIgnoreCase(FiletypeUtils.getFileType(file.getBytes()))) {
        	String errMsg = messageSource.getMessage("springmvc.messages.error.file.extension.not.matched", new String[]{inputName, filename, file.getName(), contentType}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
        } 
        // 6、检查是否ContentType异常
        else if (useStrict == true && !file.getContentType().startsWith(FilemimeUtils.getFileMimeType(filename))) {
        	String errMsg = messageSource.getMessage("springmvc.messages.error.content.type.not.matched", new String[]{inputName, filename, file.getName(), contentType}, locale);
            if (LOG.isWarnEnabled()) {
                LOG.warn(errMsg);
            }
		} 
        // 通过检查
        else {
            fileIsAcceptable = true;
        }
        
        return fileIsAcceptable;
    }
	
	private String getMaximumSizeStr(Locale locale) {
        return NumberFormat.getNumberInstance(locale).format(getMaxUploadSizePerFile());
    }

    /**
     * @param extensionCollection - Collection of extensions (all lowercase).
     * @param filename            - filename to check.
     * @return true if the filename has an allowed extension, false otherwise.
     */
    private boolean hasAllowedExtension(Collection<String> extensionCollection, String filename) {
        if (filename == null) {
            return false;
        }

        String lowercaseFilename = filename.toLowerCase();
        for (String extension : extensionCollection) {
            if ("*".equals(extension.trim()) ||lowercaseFilename.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param itemCollection - Collection of string items (all lowercase).
     * @param item           - Item to search for.
     * @return true if itemCollection contains the item, false otherwise.
     */
    private boolean containsItem(Collection<String> itemCollection, String item) {
        for (String pattern : itemCollection) {
            if ("*".equals(pattern.trim()) || matcher.match(pattern, item)){
                return true;
            }
        }
        return false;
    }
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;	
	}
	
	public long getMaxUploadSize() {
		return maxUploadSize;
	}

	/**
	 * Set the maximum allowed size (in bytes) before an upload gets rejected.
	 * -1 indicates no limit (the default).
	 * @param maxUploadSize the maximum upload size allowed
	 */
	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}
	
	public long getMaxUploadSizePerFile() {
		return maxUploadSizePerFile;
	}

	/**
	 * Set the maximum allowed size (in bytes) for each individual file before
	 * an upload gets rejected. -1 indicates no limit (the default).
	 * @param maxUploadSizePerFile the maximum upload size per file
	 */
	public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
		this.maxUploadSizePerFile = maxUploadSizePerFile;
	}
	
	/**
     * Sets the allowed extensions
     * @param allowedExtensions A comma-delimited list of extensions
     */
    public void setAllowedExtensions(String allowedExtensions) {
        allowedExtensionsSet = StringUtils.commaDelimitedListToSet(allowedExtensions);
    }

    /**
     * Sets the allowed mimetypes
     * @param allowedTypes A comma-delimited list of types
     */
    public void setAllowedTypes(String allowedTypes) {
        allowedTypesSet = StringUtils.commaDelimitedListToSet(allowedTypes);
    }

	public boolean isUseStrict() {
		return useStrict;
	}

	public void setUseStrict(boolean useStrict) {
		this.useStrict = useStrict;
	}
	
    
}
