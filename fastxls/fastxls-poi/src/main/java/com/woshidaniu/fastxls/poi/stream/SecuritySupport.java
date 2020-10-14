/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 *@类名称 : SecuritySupport.java
 *@类描述 ：
 *@创建人 ：kangzhidong
 *@创建时间 ：Mar 29, 2016 9:29:23 AM
 *@修改人 ：
 *@修改时间 ：
 *@版本号 :v1.0
 */
@SuppressWarnings("unchecked")
public class SecuritySupport {

	public ClassLoader getContextClassLoader() throws SecurityException {
		return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						ClassLoader cl = null;
						// try {
						cl = Thread.currentThread().getContextClassLoader();
						// } catch (SecurityException ex) { }

						if (cl == null)
							cl = ClassLoader.getSystemClassLoader();

						return cl;
					}
				});
	}

	public String getSystemProperty(final String propName) {
		return (String) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return System.getProperty(propName);
			}
		});
	}

	public FileInputStream getFileInputStream(final File file)
			throws FileNotFoundException {
		try {
			return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {
						public Object run() throws FileNotFoundException {
							return new FileInputStream(file);
						}
					});
		} catch (PrivilegedActionException e) {
			throw (FileNotFoundException) e.getException();
		}
	}

	public InputStream getResourceAsStream(final ClassLoader cl,
			final String name) {
		return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						InputStream ris;
						if (cl == null) {
							ris = ClassLoader.getSystemResourceAsStream(name);
						} else {
							ris = cl.getResourceAsStream(name);
						}
						return ris;
					}
				});
	}

	public boolean doesFileExist(final File f) {
		return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return new Boolean(f.exists());
			}
		})).booleanValue();
	}

}
