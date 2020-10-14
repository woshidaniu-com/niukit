package com.woshidaniu.basicutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.iterator.EnumerationIterator;
import com.woshidaniu.beanutils.ClassLoaderWrapper;

/**
 * 
 *@类名称	: LocationUtils.java
 *@类描述	：用来加载类，classpath下的资源文件，属性文件等。
 * 				  getExtendResourceAsURL(String relativePath)方法，可以使用../符号来加载 classpath外部的资源。
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:15:25 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class LocationUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(LocationUtils.class);
	
	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** URL prefix for loading from the file system: "file:" */
	public static final String FILE_URL_PREFIX = "file:";

	/** URL prefix for loading from the file system: "jar:" */
	public static final String JAR_URL_PREFIX = "jar:";

	/** URL protocol for a file in the file system: "file" */
	public static final String URL_PROTOCOL_FILE = "file";

	/** File extension for a regular jar file: ".jar" */
	public static final String JAR_FILE_EXTENSION = ".jar";

	/** Separator between JAR URL and file path within the JAR: "!/" */
	public static final String JAR_URL_SEPARATOR = "!/";
	
	protected static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

	/**
	 * Returns the default classloader (may be null).
	 * 
	 * @return The default classloader
	 */
	public static ClassLoader getDefaultClassLoader() {
		return classLoaderWrapper.getDefaultClassLoader();
	}

	/**
	 * Sets the default classloader
	 * 
	 * @param defaultClassLoader
	 *            - the new default ClassLoader
	 */
	public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		classLoaderWrapper.setDefaultClassLoader(defaultClassLoader);
	}
	
	private static int containSum(String source, String dest) {
		int containSum = 0;
		int destLength = dest.length();
		while (source.contains(dest)) {
			containSum = containSum + 1;
			source = source.substring(destLength);
		}
		return containSum;
	}

	private static String cutLastString(String location, String dest, int num) {
		for (int i = 0; i < num; i++) {
			location = location.substring(0, location.lastIndexOf(dest, location.length() - 2) + 1);
		}
		return location;
	}
	
	/**
	 * 得到指定Class所在的ClassLoader的ClassPath的绝对路径
	 */
	protected static String getAbsoluteClassPath(Class<?> clazz) {
		return clazz.getClassLoader().getResource(".").toString();
	}
	
	/**
	 * 
	 * ******************************************************************
	 * @description	： 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
	 * @author 		： kangzhidong
	 * @date 		：Mar 7, 2016 10:22:29 PM
	 * @param relativePath
	 * @return
	 * @throws IOException 
	 * ******************************************************************
	 */
	public static URL getExtendResourceAsURL(String relativePath) throws IOException{
		if (!relativePath.contains("../")) {
			return LocationUtils.getResourceAsURL(relativePath);
		}
		String classPathAbsolutePath = getAbsoluteClassPath(LocationUtils.class);
		if (relativePath.substring(0, 1).equals("/")) {
			relativePath = relativePath.substring(1);
		}
		String wildcardString = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
		relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);
		int containSum = LocationUtils.containSum(wildcardString, "../");
		classPathAbsolutePath = LocationUtils.cutLastString(classPathAbsolutePath, "/", containSum);
		String resourceAbsolutePath = classPathAbsolutePath + relativePath;
		URL resourceAbsoluteURL = null;
		try {
			resourceAbsoluteURL = new URL(resourceAbsolutePath);
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}
		return resourceAbsoluteURL;
	}
	
	/**
	 * Extract the URL for the actual jar file from the given URL
	 * (which may point to a resource in a jar file or to a jar file itself).
	 * @param jarUrl the original URL
	 * @return the URL for the actual jar file
	 * @throws MalformedURLException if no valid jar file URL could be extracted
	 */
	public static URL getJarResourceAsURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
		if (separatorIndex != -1) {
			String jarFile = urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			}
			catch (MalformedURLException ex) {
				// Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file system.
				if (!jarFile.startsWith("/")) {
					jarFile = "/" + jarFile;
				}
				return new URL(FILE_URL_PREFIX + jarFile);
			}
		}
		else {
			return jarUrl;
		}
	}
	
	public static URL getResourceAsURL(ClassLoader loader, String resource)
			throws IOException {
		URL url = classLoaderWrapper.getResourceAsURL(resource, loader);
		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return url;
	}
	
	/**
	 * Resolve the given resource location to a {@code java.net.URL}.
	 * <p>Does not check whether the URL actually exists; simply returns
	 * the URL that the given location would correspond to.
	 * @param resourceLocation the resource location to resolve: either a
	 * "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 * @return a corresponding URL object
	 * @throws FileNotFoundException if the resource cannot be resolved to a URL
	 */
	public static URL getResourceAsURL(String resourceLocation) throws FileNotFoundException {
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = getResourceAsURL(path, LocationUtils.class);
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			// try URL
			return new URL(resourceLocation);
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(resourceLocation).toURI().toURL();
			}
			catch (MalformedURLException ex2) {
				throw new FileNotFoundException("Resource location [" + resourceLocation +
						"] is neither a URL not a well-formed file path");
			}
		}
	}
	
	/**
     * Load a given resource.
     * <p/>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     */
	public static URL getResourceAsURL(String resource, Class<?> callingClass) {
		return classLoaderWrapper.getResourceAsURL(resource,
				callingClass.getClassLoader());
	}
	
	/**
     * Load all resources with a given name, potentially aggregating all results 
     * from the searched classloaders.  If no results are found, the resource name
     * is prepended by '/' and tried again.
     *
     * This method will try to load the resources using the following methods (in order):
     * <ul>
     *  <li>From Thread.currentThread().getContextClassLoader()
     *  <li>From ClassLoaderUtil.class.getClassLoader()
     *  <li>callingClass.getClassLoader()
     * </ul>
     *
     * @param resourceName The name of the resources to load
     * @param callingClass The Class object of the calling object
     */
	public static Iterator<URL> getResourceAsURLs(String resource,
			Class<?> callingClass, boolean aggregate) throws IOException {
		Iterator<URL> ite = classLoaderWrapper.getResourceAsURLs(resource, callingClass.getClassLoader());
		if (ite == null) {
			EnumerationIterator<URL> iterator = (EnumerationIterator<URL>) new EnumerationIterator<URL>();
			if (!iterator.hasNext() || aggregate) {
				iterator.addEnumeration(LocationUtils.class.getClassLoader().getResources(resource));
			}
			if (!iterator.hasNext() || aggregate) {
				iterator.addEnumeration(LocationUtils.class.getClassLoader().getResources('/' + resource));
			}
		}
		return ite;
	}
	
    /**
	 * Create a URI instance for the given URL,
	 * replacing spaces with "%20" URI encoding first.
	 * <p>Furthermore, this method works on JDK 1.4 as well,
	 * in contrast to the {@code URL.toURI()} method.
	 * @param url the URL to convert into a URI instance
	 * @return the URI instance
	 * @throws URISyntaxException if the URL wasn't a valid URI
	 * @see java.net.URL#toURI()
	 */
	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	/**
	 * Create a URI instance for the given location String,
	 * replacing spaces with "%20" URI encoding first.
	 * @param location the location String to convert into a URI instance
	 * @return the URI instance
	 * @throws URISyntaxException if the location wasn't a valid URI
	 */
	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtils.replace(location, " ", "%20"));
	}

	/**
	 * Set the {@link URLConnection#setUseCaches "useCaches"} flag on the
	 * given connection, preferring {@code false} but leaving the
	 * flag at {@code true} for JNLP based resources.
	 * @param con the URLConnection to set the flag on
	 */
	public static void useCachesIfNecessary(URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}
	
	 

	/**
	 * Determine whether the given URL points to a jar file itself,
	 * that is, has protocol "file" and ends with the ".jar" extension.
	 * @param url the URL to check
	 * @return whether the URL has been identified as a JAR file URL
	 * @since 4.1
	 */
	public static boolean isJarFileURL(URL url) {
		return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
	}
	 
    /**
	 * Return whether the given resource location is a URL:
	 * either a special "classpath" pseudo URL or a standard URL.
	 * @param resourceLocation the location String to check
	 * @return whether the location qualifies as a URL
	 * @see #CLASSPATH_URL_PREFIX
	 * @see java.net.URL
	 */
	public static boolean isURL(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		}
		catch (MalformedURLException ex) {
			return false;
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		System.out.print("");
		System.out.println(LocationUtils.getExtendResourceAsURL("../spring/dao.xml"));    
		System.out.println(LocationUtils.getExtendResourceAsURL("../../../src/log4j.properties"));    
		System.out.println(LocationUtils.getExtendResourceAsURL("log4j.properties"));
		System.out.println(LocationUtils.getResourceAsURL("log4j.properties"));
		System.out.println(LocationUtils.getResourceAsURL("log4j.properties",LocationUtils.class));
	}
}
