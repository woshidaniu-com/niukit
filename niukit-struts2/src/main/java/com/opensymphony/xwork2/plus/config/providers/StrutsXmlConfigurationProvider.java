/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.opensymphony.xwork2.plus.config.providers;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opensymphony.xwork2.FileManager;
import com.opensymphony.xwork2.FileManagerFactory;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.providers.XmlHelper;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.plus.utils.ClassPathFinder;
import com.opensymphony.xwork2.util.DomHelper;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;


/**
 * Looks in the classpath for an XML file, "struts-main.xml" by default, and uses it for the XWork configuration.
 */
@SuppressWarnings("unchecked")
public class StrutsXmlConfigurationProvider extends org.apache.struts2.config.StrutsXmlConfigurationProvider {

	protected static final Logger LOG = LoggerFactory.getLogger(StrutsXmlConfigurationProvider.class);
    protected Set<String> includedFileNames_2;
    protected String configFileName_2;
    protected Configuration configuration_2;
    protected Set<String> loadedFileUrls_2;
    protected boolean errorIfMissing_2;
    protected FileManager fileManager_2;
    
    static{
    	//在控制台测试这个类是不是代替了Struts2原来的类
    	LOG.debug("调用了自定义 ConfigurationProvider .");
    }
    
    public StrutsXmlConfigurationProvider() {
        this("struts-main.xml", true);
    }
    
    public StrutsXmlConfigurationProvider(String filename) {
        this(filename, true);
    }

    public StrutsXmlConfigurationProvider(String filename, boolean errorIfMissing) {
    	super(filename, errorIfMissing, null);
    	this.configFileName_2 = filename;
    	this.errorIfMissing_2 = errorIfMissing;
    }
    
    @Override
    public void init(Configuration configuration) {
    	this.configuration_2 = configuration;
        this.includedFileNames_2 = configuration.getLoadedFileNames();
        //利用反射将值传递给父类私有属性
        ReflectionUtils.setField("configuration", this, configuration );
        ReflectionUtils.setField("includedFileNames", this, configuration.getLoadedFileNames() );
        //调用自己的文档加载方式
        loadDocuments_2(configFileName_2);
    }
    
	protected void loadDocuments_2(String configFileName) {
        try {
        	loadedFileUrls_2 = (Set<String>) ReflectionUtils.getField("loadedFileUrls", this);
        	loadedFileUrls_2.clear();
            ReflectionUtils.setField("documents", this, loadConfigurationFiles_2(configFileName, null));
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigurationException("Error loading configuration file " + configFileName, e);
        }
    }
    
    //    protected void loadPackages(Element rootElement) throws ConfigurationException {
    //        NodeList packageList = rootElement.getElementsByTagName("package");
    //
    //        for (int i = 0; i < packageList.getLength(); i++) {
    //            Element packageElement = (Element) packageList.item(i);
    //            addPackage(packageElement);
    //        }
    //    }
    protected List<Document> loadConfigurationFiles_2(String fileName, Element includeElement) {
        
    	List<Document> docs = new ArrayList<Document>();
        List<Document> finalDocs = new ArrayList<Document>();
        if (!includedFileNames_2.contains(fileName)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading action configurations from: " + fileName);
            }

            includedFileNames_2.add(fileName);
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
						in.setSystemId(fileName);
						docs.add(DomHelper.parse(in, getDtdMappings()));//
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (mis != null) {
						try {
							mis.close();
						} catch (IOException e) {
							LOG.error("Unable to close input stream", e);
						}
					}
				}

			}
			// 如果没有搜索到，则表示不是jar文件中的配置
			else {
				Iterator<URL> urls = null;
	            InputStream is = null;
	            IOException ioException = null;
	            try {
	                urls = getConfigurationUrls(fileName);
	            } catch (IOException ex) {
	                ioException = ex;
	            }

            if (urls == null || !urls.hasNext()) {
                if (errorIfMissing_2) {
                    throw new ConfigurationException("Could not open files of the name " + fileName, ioException);
                } else {
                    if (LOG.isInfoEnabled()) {
                    LOG.info("Unable to locate configuration files of the name "
                            + fileName + ", skipping");
                    }
                    return docs;
                }
            }

            URL url = null;
            while (urls.hasNext()) {
                try {
                    url = urls.next();
                    is = fileManager_2.loadFile(url);

                    InputSource in = new InputSource(is);

                    in.setSystemId(url.toString());

                    docs.add(DomHelper.parse(in, getDtdMappings()));
                    loadedFileUrls_2.add(url.toString());
                } catch (XWorkException e) {
                    if (includeElement != null) {
                        throw new ConfigurationException("Unable to load " + url, e, includeElement);
                    } else {
                        throw new ConfigurationException("Unable to load " + url, e);
                    }
                } catch (Exception e) {
                    throw new ConfigurationException("Caught exception while loading file " + fileName, e, includeElement);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            LOG.error("Unable to close input stream", e);
                        }
                    }
                }
            }
			}

            //sort the documents, according to the "order" attribute
            Collections.sort(docs, new Comparator<Document>() {
                public int compare(Document doc1, Document doc2) {
                    return XmlHelper.getLoadOrder(doc1).compareTo(XmlHelper.getLoadOrder(doc2));
                }
            });

            for (Document doc : docs) {
                Element rootElement = doc.getDocumentElement();
                NodeList children = rootElement.getChildNodes();
                int childSize = children.getLength();

                for (int i = 0; i < childSize; i++) {
                    Node childNode = children.item(i);

                    if (childNode instanceof Element) {
                        Element child = (Element) childNode;

                        final String nodeName = child.getNodeName();

                        if ("include".equals(nodeName)) {
                            String includeFileName = child.getAttribute("file");
                            if (includeFileName.indexOf('*') != -1) {
                                // handleWildCardIncludes(includeFileName, docs, child);
                                ClassPathFinder wildcardFinder = new ClassPathFinder();
                                wildcardFinder.setPattern(includeFileName);
                                Vector<String> wildcardMatches = wildcardFinder.findMatches();
                                for (String match : wildcardMatches) {
                                    finalDocs.addAll(loadConfigurationFiles_2(match, child));
                                }
                            } else {
                                finalDocs.addAll(loadConfigurationFiles_2(includeFileName, child));
                            }
                        }
                    }
                }
                finalDocs.add(doc);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Loaded action configuration from: " + fileName);
            }
        }
        return finalDocs;
    }
    
    @Inject
    public void setFileManagerFactory(FileManagerFactory fileManagerFactory) {
    	super.setFileManagerFactory(fileManagerFactory);
        this.fileManager_2 = fileManagerFactory.getFileManager();
    }
    
    public String toString() {
        return ("Struts Main XML configuration provider ("+ configFileName_2 +")");
    }
}
