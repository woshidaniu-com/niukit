package org.springframework.enhanced.web.servlet.view.freemarker.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.http.HttpServletRequest;

import org.xml.sax.InputSource;

import freemarker.cache.URLTemplateLoader;

public class ResourceTemplateLoader extends URLTemplateLoader {
	
	private String urlPrefix = null;
	private String filePrefix = "/";
	private Class<?> resJAR;
	private String sessionid;
	private static String STATIC_RESOURCE_FOLDER = "resource";

	public ResourceTemplateLoader(HttpServletRequest request, Class<?> resJAR) {
		super();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		this.urlPrefix = basePath;
		this.resJAR = resJAR;
		this.sessionid = request.getSession().getId();
	}

	@Override
	protected URL getURL(String fileName) {
		try {
			
			//修改源代码，如果fileName被“#$”标记，说明是jar包中的文件
            InputStream mis = null;
			if (fileName.indexOf("#$") != -1) {
				String realFile = fileName.substring(0, fileName.indexOf("#$"));// 从文件名中得到jar的文件名
				String targetName = fileName.substring(fileName.indexOf("#$") + 2);// 从文件名中得到匹配到的配置文件名
				try {
					JarFile jar = new JarFile(new File(realFile));
					//Enumeration<JarEntry> jes = jar.entries();
					JarEntry je = jar.getJarEntry(targetName);
					if (je != null) {
						mis = jar.getInputStream(je);
						// 从jar中获取指定的文件输入流。
						InputSource in = new InputSource(mis);
						// 以下代码是struts2得到文件输入流后读取文件的方法。
						//in.setSystemId(fileName);
						//docs.add(DomHelper.parse(in, dtdMappings));//
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (mis != null) {
						try {
							mis.close();
						} catch (IOException e) {
							//LOG.error("Unable to close input stream", e);
						}
					}
				}

			}
			
			
			
			if (fileName.startsWith("mvc/")) {
				return new URL(urlPrefix + fileName);
			} else if (fileName.startsWith("res/")) {
				String pluginFile = null;//PluginsMananger.pluginsMap.get(getModuleName(name.substring(4)));
				String resource = fileName.substring(4);
				String moduleName = getModuleName(resource);
				String resourceUrl = resource.replace(moduleName,STATIC_RESOURCE_FOLDER);
				URI uri = new URI("jar:file:" + pluginFile + "!" + filePrefix + resourceUrl);
				return uri.toURL();
				// return new URL(urlPrefix + name);
			} else {
				return resJAR.getResource(filePrefix + fileName);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getModuleName(String pathInfo) {
		String moduleName = null;
		// 过滤掉第一个/
		if (0 == pathInfo.indexOf("/")) {
			pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
		}
		// 获取模块名称，规则:
		// 1、获取第一个/前的字符
		// 2、如没有/时,获取整个字符，但不能是静态资源
		// pathInfo中含有/时，/前的字符串即为模块名称
		if (-1 != pathInfo.indexOf("/")) {
			moduleName = pathInfo.substring(0, pathInfo.indexOf("/"));
		}
		// pathInfo中不含有/时, 同时也没有携带资源，pathInfo即为模块名称
		else if (-1 == pathInfo.indexOf("/") && -1 == pathInfo.indexOf(".")) {
			moduleName = pathInfo;
		}
		return moduleName;
	}

	public long getLastModified(Object templateSource) {
		return ((SessionURLTemplateSource) templateSource).lastModified();
	}

	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		return new InputStreamReader(
				((SessionURLTemplateSource) templateSource).getInputStream(),
				encoding);
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		((SessionURLTemplateSource) templateSource).close();
	}

	public Object findTemplateSource(String name) throws IOException {
		URL url = getURL(name);
		return url == null ? null : new SessionURLTemplateSource(url, sessionid);
	}

	public void setSessionId(String sessionid) {
		this.sessionid = sessionid;
	}
}