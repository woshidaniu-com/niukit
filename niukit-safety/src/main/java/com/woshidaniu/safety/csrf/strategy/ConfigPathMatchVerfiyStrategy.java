/**
 * 
 */
package com.woshidaniu.safety.csrf.strategy;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.core.AntPathMatcher;
import com.woshidaniu.web.core.PathMatcher;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：根据配置的路径判断是否需要验证CSRF TOKEN信息
 *	 com.woshidaniu.web.filter.security.csrf.ConfigPathMatchVerfiyStrategy
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ConfigPathMatchVerfiyStrategy implements CsrfVerifyStrategy{
	
	private static final Logger log = LoggerFactory.getLogger(ConfigPathMatchVerfiyStrategy.class);
	
	/**
	 * 需要拦截验证的路径
	 */
	protected Set<String> verifyPaths;
	
	/**
	 * 配置文件匹配名称
	 */
	protected String filePattern = "csrf-verfiy-path-*.conf";
	
	/**
	 * 匹配器
	 */
	protected PathMatcher patternMatcher;
	
	@Override
	public boolean needVerify(HttpServletRequest request) {
		if(verifyPaths == null){
			return false;
		}
		boolean isMatch = false;
		String requestPath = WebUtils.getPathWithinApplication(request);
		for (String pattern : verifyPaths) {
			if(patternMatcher.match(pattern, requestPath)){
				isMatch = true;
				break;
			}
		}
		return isMatch;
	}

	public void init(){
		if(patternMatcher == null){
			patternMatcher = new AntPathMatcher();
		}
		
		if(verifyPaths == null){
			verifyPaths = new HashSet<String>();
		}
		//读取配置文件//
		//读取以csrf-verfiy-path-*.conf这个名称格式的文件
		StringBuffer config = new StringBuffer();
		URL classpathURL = ConfigPathMatchVerfiyStrategy.class.getClassLoader().getResource("/conf/security/");
		File classpathFile = new File(classpathURL.getPath());
		File[] csrfPathConfigFiles = classpathFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return FilenameUtils.wildcardMatch(name, getFilePattern(), IOCase.INSENSITIVE);
			}
		});
		for (File file : csrfPathConfigFiles) {
			try {
				String readFileToString = FileUtils.readFileToString(file, "utf-8");
				config.append(readFileToString).append(",");
			} catch (IOException e) {
				log.error("CSRF配置文件：" +file.getName()+ "，读取失败！", e);
				e.printStackTrace();
			}
		}
		if(config.length() > 0){
			String[] configSplits = StringUtils.split(config.toString(), ",");
			for (String p : configSplits) {
				String trimToNull = StringUtils.trimToNull(p);
				if(trimToNull != null){
					if(log.isDebugEnabled()){
						log.debug("添加CSRF验证路径：{}", trimToNull);
					}
					verifyPaths.add(trimToNull);
				}
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("****************CSRF 验证路径*****************");
			for (String vp : verifyPaths) {
				log.debug("*[{}]",vp);
			}
			log.debug("****************CSRF 验证路径*****************");
		}
		
	}

	public Set<String> getVerifyPaths() {
		return verifyPaths;
	}

	public void setVerifyPaths(Set<String> verifyPaths) {
		this.verifyPaths = verifyPaths;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	public PathMatcher getPathMatcher() {
		return patternMatcher;
	}

	public void setPathMatcher(PathMatcher patternMatcher) {
		this.patternMatcher = patternMatcher;
	}

}
