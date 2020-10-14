/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.woshidaniu.xmlhub.core.exception.TransformException;

/**
 * 
 * *******************************************************************
 * @className	： ObjectJaxbTransformer
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 17, 2016 12:18:21 PM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings("unchecked") 
public class ObjectJaxbTransformer<T> implements ObjectTransformer<T> {

	/**
	 * 多线程安全的Context. 
	 */
	protected JAXBContext jaxbContext = null;
    /** 
     * @param types  所有需要序列化的Root对象的类型. 
     */  
    public ObjectJaxbTransformer(Class<?>... types) {  
        try {  
            jaxbContext = JAXBContext.newInstance(types);  
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
    } 

	public T toObject(InputStream input){
		try {  
			return (T) createUnMarshaller().unmarshal(input);   
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
	}
	
	public T toObject(File xmlfile){
		try {  
			return (T) createUnMarshaller().unmarshal(xmlfile);   
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
	}
	
	public T toObject(String xmlpath){
		return this.toObject(new File(xmlpath));
	}
	
	 
	public T toObject(StringBuilder xmlStr){
		try {  
            StringReader reader = new StringReader(xmlStr.toString());  
            return (T) createUnMarshaller().unmarshal(reader);  
        } catch (JAXBException e) {  
            throw new TransformException(e);  
        }  
	}
	
	/**
	 * 
	 * @description	： 创建UnMarshaller, 设定相关参数. 
	 * @author 		： kangzhidong
	 * @date 		：Mar 17, 2016 11:51:49 AM
	 * @param <T>
	 * @param source
	 * @return
	 * @throws JAXBException
	 */
	protected Unmarshaller createUnMarshaller() throws JAXBException {
		// JDK 6.0－－总述及XML的新标准
		Unmarshaller m = jaxbContext.createUnmarshaller();
		//设置Unmarshaller
		//m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormated_ouput()); 
		//m.setProperty(Marshaller.JAXB_ENCODING, getEncoding()); 
		//m.setProperty(Marshaller.JAXB_FRAGMENT, isFragment()); 
		 
		return m;
	}
	
}
