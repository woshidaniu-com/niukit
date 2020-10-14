package com.woshidaniu.io.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.basicutils.LocationUtils;

/**
 * 
 *@类名称	: ResourceUtils.java
 *@类描述	：用来加载类，classpath下的资源文件，属性文件等。  getExtendResource(StringrelativePath)方法，可以使用../符号来加载 classpath外部的资源。
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:15:46 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class ResourceUtils extends LocationUtils{
	
	protected static Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

	/** URL protocol for an entry from a jar file: "jar" */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** URL protocol for an entry from a zip file: "zip" */
	public static final String URL_PROTOCOL_ZIP = "zip";

	/** URL protocol for an entry from a WebSphere jar file: "wsjar" */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** URL protocol for an entry from a JBoss jar file: "vfszip" */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	/** URL protocol for a JBoss file system resource: "vfsfile" */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

	/** URL protocol for a general JBoss VFS resource: "vfs" */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/**
     * Resource path prefix that specifies to load from a classpath location, value is <b>{@code classpath:}</b>
     */
    public static final String CLASSPATH_PREFIX = "classpath:";
    /**
     * Resource path prefix that specifies to load from a url location, value is <b>{@code url:}</b>
     */
    public static final String URL_PREFIX = "url:";
    /**
     * Resource path prefix that specifies to load from a file location, value is <b>{@code file:}</b>
     */
    public static final String FILE_PREFIX = "file:";
    
	/**
	 * Resolve the given resource location to a {@code java.io.File},
	 * i.e. to a file in the file system.
	 * <p>Does not check whether the file actually exists; simply returns
	 * the File that the given location would correspond to.
	 * @param resourceLocation the resource location to resolve: either a
	 * "classpath:" pseudo URL, a "file:" URL, or a plain file path
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the resource cannot be resolved to
	 * a file in the file system
	 */
	public static File getResourceAsFile(String resourceLocation) throws FileNotFoundException {
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			ClassLoader cl = getDefaultClassLoader();
			URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
			if (url == null) {
				throw new FileNotFoundException(description +  " cannot be resolved to absolute file path because it does not exist");
			}
			return getResourceAsFile(url, description);
		}
		try {
			// try URL
			return getResourceAsFile(new URL(resourceLocation));
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}

	/**
	 * Resolve the given resource URL to a {@code java.io.File},
	 * i.e. to a file in the file system.
	 * @param resourceUrl the resource URL to resolve
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the URL cannot be resolved to
	 * a file in the file system
	 */
	public static File getResourceAsFile(URL resourceUrl) throws FileNotFoundException {
		return getResourceAsFile(resourceUrl, "URL");
	}

	/**
	 * Resolve the given resource URL to a {@code java.io.File},
	 * i.e. to a file in the file system.
	 * @param resourceUrl the resource URL to resolve
	 * @param description a description of the original resource that
	 * the URL was created for (for example, a class path location)
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the URL cannot be resolved to
	 * a file in the file system
	 */
	public static File getResourceAsFile(URL resourceUrl, String description) throws FileNotFoundException {
		Assert.notNull(resourceUrl, "Resource URL must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(
					description + " cannot be resolved to absolute file path " +
					"because it does not reside in the file system: " + resourceUrl);
		}
		try {
			return new File(toURI(resourceUrl).getSchemeSpecificPart());
		}
		catch (URISyntaxException ex) {
			// Fallback for URLs that are not valid URIs (should hardly ever happen).
			return new File(resourceUrl.getFile());
		}
	}

	/**
	 * Resolve the given resource URI to a {@code java.io.File},
	 * i.e. to a file in the file system.
	 * @param resourceUri the resource URI to resolve
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the URL cannot be resolved to
	 * a file in the file system
	 */
	public static File getResourceAsFile(URI resourceUri) throws FileNotFoundException {
		return getResourceAsFile(resourceUri, "URI");
	}

	/**
	 * Resolve the given resource URI to a {@code java.io.File},
	 * i.e. to a file in the file system.
	 * @param resourceUri the resource URI to resolve
	 * @param description a description of the original resource that
	 * the URI was created for (for example, a class path location)
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the URL cannot be resolved to
	 * a file in the file system
	 */
	public static File getResourceAsFile(URI resourceUri, String description) throws FileNotFoundException {
		Assert.notNull(resourceUri, "Resource URI must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
			throw new FileNotFoundException(
					description + " cannot be resolved to absolute file path " +
					"because it does not reside in the file system: " + resourceUri);
		}
		return new File(resourceUri.getSchemeSpecificPart());
	}
	
	public static File getResourceAsFile(ClassLoader loader, String filename) throws IOException{
		URL url = LocationUtils.getResourceAsURL(loader,filename);
		return url == null ? null : new File(url.getPath());
    }
	
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
		InputStream in = classLoaderWrapper.getResourceAsStream(resource, loader);
		if (in == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return in;
	}

	
    public static InputStream getResourceAsStream(Object self, String filename) throws IOException {
        return getResourceAsStream(self.getClass().getClassLoader(), filename);
    }


	/**
	 * Returns a resource on the classpath as a Stream object
	 * 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找 classpath外部的资源，需要使用../来查找
	 * 
	 * @param resource
	 *            The resource to find
	 * @return The InputStream of the resource found, or <code>null</code> if
	 *         the resource cannot be found from any of the three mentioned
	 *         ClassLoaders.
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static InputStream getResourceAsStream(String resource)
			throws IOException {
		InputStream stream = classLoaderWrapper.getResourceAsStream(resource);
		if (stream == null) {
			try {
				stream = getURLAsStream(getExtendResourceAsURL(resource));
			} catch (IOException e) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Resource [" + resource + "] was not found : ",
							e.getMessage());
				}
			}
		}

		if (stream == null && LOG.isTraceEnabled()) {
			LOG.trace("Resource ["
					+ resource
					+ "] was not found via the thread context, current, or "
					+ "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
		}

		return stream;
	}
 
	
	/**
    * This is a convenience method to load a resource as a stream.
    * The algorithm used to find the resource is given in getResource()
    * @param resourceName The name of the resource to load
    * @param callingClass The Class object of the calling object
    */
    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = LocationUtils.getResourceAsURL(resourceName, callingClass);
        try {
        	return getURLAsStream(url);
        } catch (IOException e) {
        	e.printStackTrace();
            return null;
        }
    }
    
	/**
	 * 
	 * ******************************************************************
	 * @description	： 加载URL指定的对象到InputStream
	 * @author 		： kangzhidong
	 * @date 		：Mar 7, 2016 10:40:33 PM
	 * @param url
	 * @return
	 * @throws IOException
	 * ******************************************************************
	 */
	public static InputStream getURLAsStream(URL url) throws IOException {
		try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
        	e.printStackTrace();
            return null;
        }
	}

    public static String getResourceAsString(ClassLoader loader, String filename) throws IOException {
        String ret = null;
        InputStream in = getResourceAsStream(loader, filename);
        if (in != null) {
            ret = IOUtils.toString(in, Charset.defaultCharset());
        }
        return ret;
    }

    public static String getResourceAsRaw(Object self, String filename) throws IOException {
        return getResourceAsRaw(self.getClass().getClassLoader(), filename);
    }

    public static String getResourceAsStr(ClassLoader loader, String filename) {
        try {
            return StringUtils.trimToEmpty(getResourceAsRaw(loader, filename));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getResourceAsStr(Object self, String filename) {
        return getResourceAsStr(self.getClass().getClassLoader(), filename);
    }

    /**
     * for use during constructors, when 'this' is not available // // makes a
     * guess that a dummy object is in the same classloader as the caller //
     * this assumprion may be somewhat dodgy, use with care ...
     */
    public static String getResourceAsStr(String filename) {
        return getResourceAsStr(getDefaultClassLoader(), filename);
    }
    
    /**
	 * Determine whether the given URL points to a resource in the file system,
	 * that is, has protocol "file", "vfsfile" or "vfs".
	 * @param url the URL to check
	 * @return whether the URL has been identified as a file system URL
	 */
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
				URL_PROTOCOL_VFS.equals(protocol));
	}

	/**
	 * Determine whether the given URL points to a resource in a jar file,
	 * that is, has protocol "jar", "zip", "vfszip" or "wsjar".
	 * @param url the URL to check
	 * @return whether the URL has been identified as a JAR URL
	 */
	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol) ||
				URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol));
	}

	/**
	 * Determine whether the given URL points to a jar file itself,
	 * that is, has protocol "file" and ends with the ".jar" extension.
	 * @param url the URL to check
	 * @return whether the URL has been identified as a JAR file URL
	 * @since 4.1
	 */
	public static boolean isJarFileURL(URL url) {
		return (URL_PROTOCOL_FILE.equals(url.getProtocol()) &&
				url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
	}
	

    /**
     * Returns the InputStream for the resource represented by the specified path, supporting scheme
     * prefixes that direct how to acquire the input stream
     * ({@link #CLASSPATH_PREFIX CLASSPATH_PREFIX},
     * {@link #URL_PREFIX URL_PREFIX}, or {@link #FILE_PREFIX FILE_PREFIX}).  If the path is not prefixed by one
     * of these schemes, the path is assumed to be a file-based path that can be loaded with a
     * {@link FileInputStream FileInputStream}.
     *
     * @param resourcePath the String path representing the resource to obtain.
     * @return the InputStraem for the specified resource.
     * @throws IOException if there is a problem acquiring the resource at the specified path.
     */
    public static InputStream getInputStreamForPath(String resourcePath) throws IOException {

        InputStream is;
        if (resourcePath.startsWith(CLASSPATH_PREFIX)) {
            is = loadFromClassPath(stripPrefix(resourcePath));

        } else if (resourcePath.startsWith(URL_PREFIX)) {
            is = loadFromUrl(stripPrefix(resourcePath));

        } else if (resourcePath.startsWith(FILE_PREFIX)) {
            is = loadFromFile(stripPrefix(resourcePath));

        } else {
            is = loadFromFile(resourcePath);
        }

        if (is == null) {
            throw new IOException("Resource [" + resourcePath + "] could not be found.");
        }

        return is;
    }

    private static InputStream loadFromFile(String path) throws IOException {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Opening file [" + path + "]...");
        }
        return new FileInputStream(path);
    }

    private static InputStream loadFromUrl(String urlPath) throws IOException {
    	LOG.debug("Opening url {}", urlPath);
        URL url = new URL(urlPath);
        return url.openStream();
    }

    private static InputStream loadFromClassPath(String path) throws IOException {
    	LOG.debug("Opening resource from class path [{}]", path);
        return getResourceAsStream(path);
    }

    private static String stripPrefix(String resourcePath) {
        return resourcePath.substring(resourcePath.indexOf(":") + 1);
    }
	
}