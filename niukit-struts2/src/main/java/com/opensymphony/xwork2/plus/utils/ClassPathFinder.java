/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.opensymphony.xwork2.plus.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.util.PatternMatcher;
import com.opensymphony.xwork2.util.WildcardHelper;

/**
 * This class is an utility class that will search through the classpath
 * for files whose names match the given pattern. The filename is tested
 * using the given implementation of {@link com.opensymphony.xwork2.util.PatternMatcher} by default it 
 * uses {@link com.opensymphony.xwork2.util.WildcardHelper}
 *
 * @version $Rev$ $Date$
 * 
 * 修改{@link com.opensymphony.xwork2.util.ClassPathFinder}类使其支持，
 * 在struts.xml中使用<include file="struts-*.xml"></include>这种使用通配符的方式来包含每个模块的配置文件时可从jar包中读取配置
 * 
 */
public class ClassPathFinder{
	
	/**
     * The String pattern to test against.
     */
	private String pattern ;
	
	private int[] compiledPattern ;
	
	/**
     * The PatternMatcher implementation to use
     */
	private PatternMatcher<int[]> patternMatcher = new WildcardHelper();
	
	private Vector<String> compared = new Vector<String>();
	
	/**
	 * retrieves the pattern in use
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * sets the String pattern for comparing filenames
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
     * Builds a {@link java.util.Vector} containing Strings which each name a file
     * who's name matches the pattern set by setPattern(String). The classpath is 
     * searched recursively, so use with caution.
     *
     * @return Vector<String> containing matching filenames
     */
	public Vector<String> findMatches() {
		Vector<String> matches = new Vector<String>();
		URLClassLoader cl = getURLClassLoader();
		if (cl == null ) {
			throw new XWorkException("unable to attain an URLClassLoader") ;
		}
		URL[] parentUrls = cl.getURLs();
		compiledPattern = (int[]) patternMatcher.compilePattern(pattern);
		for (URL url : parentUrls) {
			if (!"file".equals(url.getProtocol())) {
				continue ;
			}
			URI entryURI ;
			try {
				entryURI = url.toURI();
			} catch (URISyntaxException e) {
				continue;
			}
			//entryURI是classpath下的路径或文件
			File entry = new File(entryURI) ;
			//checkEntries方法是用来搜索entryURI下的匹配的文件，如果有匹配的文件，返回该文件的文件名
			Vector<String> results = checkEntries(entry.list(), entry, "");
			if (results != null ) {
				//如果有匹配到的文件，加入匹配队列
				matches.addAll(results);
			}
		    //修改struts2源代码，使其搜索文件名以“jxt”开头的jar包中的配置文件。
			else{
				if (entry.getName().endsWith(".jar")) {
					JarFile jar = null;// 读取jar文件里的文件，使用jdk通过的类JarFile
					try {
						jar = new JarFile(entry);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (jar != null) {
						Enumeration<JarEntry> jes = jar.entries();
						while (jes.hasMoreElements()) {
							JarEntry je = jes.nextElement();
							if (!je.isDirectory()) {
								// entryToCheck 就是文件名
								String entryToCheck = je.getName();
								boolean doesMatch = patternMatcher.match(new HashMap<String, String>(),entryToCheck, compiledPattern);
								if (doesMatch) {
									// 因为jar里的配置文件读取方法不一样，这里保存了jar文件的路径和匹配到的文件名，当要读取里面匹配到的文件时，免去了搜索的步骤。
									matches.add(entry.getPath() + "#$" + entryToCheck);
								}
							}
						}
					}
				}
			}
		}
		return matches;
	}
	
	private Vector<String> checkEntries(String[] entries, File parent, String prefix) {
		
		if (entries == null ) {
			return null;
		}
		
		Vector<String> matches = new Vector<String>();
		for (String listEntry : entries) {
			File tempFile ;
			if (!"".equals(prefix) ) {
				tempFile = new File(parent, prefix + "/" + listEntry);
			}
			else {
				tempFile = new File(parent, listEntry);
			}
			if (tempFile.isDirectory() && 
					!(".".equals(listEntry) || "..".equals(listEntry)) ) {
				if	(!"".equals(prefix) ) {
					matches.addAll(checkEntries(tempFile.list(), parent, prefix + "/" + listEntry));
				}
				else {
					matches.addAll(checkEntries(tempFile.list(), parent, listEntry));
				}
			}
			else {
				
				String entryToCheck ;
				if ("".equals(prefix)) {
					entryToCheck = listEntry ;
				}
				else {
					entryToCheck = prefix + "/" + listEntry ;
				}
				
				if (compared.contains(entryToCheck) ) {
					continue;
				}
				else {
					compared.add(entryToCheck) ;
				}
				
				boolean doesMatch = patternMatcher.match(new HashMap<String,String>(), entryToCheck, compiledPattern);
				if (doesMatch) {
					matches.add(entryToCheck);
				}
			}
		}
		return matches ;
	}

	/**
	 * sets the PatternMatcher implementation to use when comparing filenames
	 * @param patternMatcher
	 */
	public void setPatternMatcher(PatternMatcher<int[]> patternMatcher) {
		this.patternMatcher = patternMatcher;
	}

	private URLClassLoader getURLClassLoader() {
		URLClassLoader ucl = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if(! (loader instanceof URLClassLoader)) {
			loader = ClassPathFinder.class.getClassLoader();
			if (loader instanceof URLClassLoader) {
				ucl = (URLClassLoader) loader ;
			}
		}
		else {
			ucl = (URLClassLoader) loader;
		}
		
		return ucl ;
	}
}