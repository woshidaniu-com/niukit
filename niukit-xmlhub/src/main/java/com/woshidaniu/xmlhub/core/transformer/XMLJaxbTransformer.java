/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.apache.commons.io.output.StringBuilderWriter;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

import com.woshidaniu.xmlhub.core.CharEncoding;
import com.woshidaniu.xmlhub.core.exception.TransformException;

/**
 * *******************************************************************
 * @className	： XMLJaxbTransformer
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 17, 2016 10:46:34 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class XMLJaxbTransformer implements XMLTransformer {

	/**
	 * 用来指定已编组 XML 数据中输出编码的属性名称
	 */
	protected String encoding = CharEncoding.UTF_8;
	/**
	 * 用来指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称
	 */
	protected boolean formated_ouput = true;
	/**
	 * 用来指定 marshaller 是否将生成文档级事件（即调用 startDocument 或 endDocument）的属性名称
	 */
	protected boolean fragment = true;
	/**
	 *  用来指定将放置在已编组 XML 输出中的 xsi:noNamespaceSchemaLocation 属性值的属性名称
	 */
	protected String namespace = null;
	/**
	 *  用来指定将放置在已编组 XML 输出中的 xsi:schemaLocation 属性值的属性名称
	 */
	protected String schema_location = null;
	/**
	 * 多线程安全的Context. 
	 */
	protected JAXBContext jaxbContext = null;
    /** 
     * @param types 
     *            所有需要序列化的Root对象的类型. 
     */  
    public XMLJaxbTransformer(Class<?>... types) {  
        try {  
            jaxbContext = JAXBContext.newInstance(types);  
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
    } 
    
	@Override
	public <T> String toXML(T source) {
		StringBuilderWriter writer = new StringBuilderWriter();
		this.toXML(source, writer);
		return writer.toString();
	}
	
	@Override
	public String toXML(Collection<?> root, String rootName){
		try {  
            CollectionWrapper wrapper = new CollectionWrapper(root);  
            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName), CollectionWrapper.class, wrapper);  
            StringBuilderWriter writer = new StringBuilderWriter();
            this.createMarshaller().marshal(wrapperElement, writer);  
            return writer.toString();  
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
	}

	@Override
	public <T> void toXML(T source, String outPath) {
		this.toXML(source, new File(outPath));
	}

	@Override
	public <T> void toXML(T source, File output) {
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}

	@Override
	public <T> void toXML(T source, Writer output) {
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}

	@Override
	public <T> void toXML(T source, OutputStream output) {
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	public <T> void toXML(T source,Document output){
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	public <T> void toXML(T source,ContentHandler output){
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	public <T> void toXML(T source,Result output){
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	public <T> void toXML(T source,XMLStreamWriter output){
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	public <T> void toXML(T source,XMLEventWriter output){
		try {
			this.createMarshaller().marshal(source, output);
		} catch (JAXBException e) {
			throw new TransformException(e);
		}
	}
	
	/**
	 * 
	 * @description	： 创建Marshaller, 设定相关参数. 
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:51:49 AM
	 * @param <T>
	 * @param source
	 * @return
	 * @throws JAXBException
	 */
	protected Marshaller createMarshaller() throws JAXBException {
		// JDK 6.0－－总述及XML的新标准
		Marshaller m = jaxbContext.createMarshaller();
		//设置Marshaller
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormated_ouput()); 
		m.setProperty(Marshaller.JAXB_ENCODING, getEncoding()); 
		m.setProperty(Marshaller.JAXB_FRAGMENT, isFragment()); 
		if(getNamespace() != null){
			m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, getNamespace());
		}
		if(getSchema_location() != null){
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, getSchema_location()); 
		}
		return m;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isFormated_ouput() {
		return formated_ouput;
	}

	public void setFormated_ouput(boolean formatedOuput) {
		formated_ouput = formatedOuput;
	}

	public boolean isFragment() {
		return fragment;
	}

	public void setFragment(boolean fragment) {
		this.fragment = fragment;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getSchema_location() {
		return schema_location;
	}

	public void setSchema_location(String schemaLocation) {
		schema_location = schemaLocation;
	}
	
}
