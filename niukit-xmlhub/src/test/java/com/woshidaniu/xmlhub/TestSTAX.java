/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.EventFilter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.woshidaniu.xmlhub.utils.XMLInputUtils;
import com.woshidaniu.xmlhub.utils.XMLOutputUtils;

public class TestSTAX {
	@Test
	public void testXMLStreamReader() throws IOException {
		try {
//			BufferedReader r = new BufferedReader(new InputStreamReader(in));
//			String tmp = null;
//			in.mark(0);
//			while((tmp=r.readLine())!=null){
//				System.out.println(tmp);
//			}
//			in.reset();
			XMLStreamReader reader = XMLInputUtils.getXMLStreamReader("books.xml");
			while (reader.hasNext()) {
				int type = reader.next();
				if(type==XMLStreamConstants.START_ELEMENT){
					System.out.print(reader.getName());
					System.out.println("dddddddd"+reader.getElementText());
				}else if(type==XMLStreamConstants.CHARACTERS){
					System.out.println();
				}else if(type==XMLStreamConstants.END_ELEMENT){
					System.out.println(reader.getName());
				}
			}
			reader.close();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testXMLEventReader(){
		try {
			
			/*TransformerFactory f = SAXTransformerFactory.newInstance();
			f.newTransformer().transform(xmlSource, outputTarget)
			
			XPathFactory.newInstance().newXPath().evaluate(expression, item) 
			SAXParserFactory.newInstance().newSAXParser().parse(f, dh);
			*/
			XMLEventReader reader = XMLInputUtils.getXMLEventReader("books.xml");
			while(reader.hasNext()){
				XMLEvent event = reader.nextEvent();
				if(event.isStartElement()){
					String name = event.asStartElement().getName().toString();
//					if("book".equals(name)){
//						System.out.println(reader.getElementText());
//					}
					if("title".equals(name)){
						System.out.println(reader.getElementText());
					}else if("price".equals(name)){
						System.out.println(reader.getElementText());
					}
				
				
				}
			}
			
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	@Test
	public void testXMLFilteredReader(){
		try {
			XMLEventReader reader = XMLInputUtils.getXMLEventReader("books.xml", new EventFilter() {
				@Override
				public boolean accept(XMLEvent event) {
					return false;
				}
			});
			
			while(reader.hasNext()){
				XMLEvent event = reader.nextEvent();
				if(event.isStartElement()){
					
					
					String name = event.asStartElement().getName().toString();
//					if("book".equals(name)){
//						System.out.println(reader.getElementText());
//					}
					if("title".equals(name)){
						System.out.println(reader.getElementText());
					}else if("price".equals(name)){
						System.out.println(reader.getElementText());
					}
					
					
				}
			}
			
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testXPath(){
		try {
			InputStream in = TestSTAX.class.getClassLoader().getResourceAsStream("books.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder  db = factory.newDocumentBuilder();
			Document doc = db.parse(in);
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			String expression = "//book[@category='WEB']";
			
			NodeList nodeList = (NodeList)xpath.evaluate(expression,doc,XPathConstants.NODESET);
			for(int i = 0 ; i< nodeList.getLength();i++){
				Element	 ele = (Element)(nodeList.item(i));
				NodeList tiel =   ele.getElementsByTagName("title");
				Element e  = (Element) tiel.item(0);
				System.out.println(e.getTextContent());
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testXMLStreamWriter(){
		try {
			
			XMLStreamWriter writer = XMLOutputUtils.getXMLStreamWriter(System.out);
			String ns= "http://www.song.com";
			writer.writeStartDocument("UTF-8","1.0");
			writer.setDefaultNamespace("http://defa.com");
			writer.writeDefaultNamespace("http://defa.com");
			writer.writeNamespace("ns", ns);
			writer.setPrefix("ns", ns);
			writer.writeStartElement("ns","this",ns);
			writer.writeCharacters("conten22t");
			writer.writeStartElement("ns","sdfsdfds",ns);
			writer.writeCharacters("content");
			writer.writeEndElement();
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new TestSTAX().test();
	}
	@Test
	public void test(){
		try {
			InputStream in = TestSTAX.class.getClassLoader().getResourceAsStream("books.xml");
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			XPath xPath = XPathFactory.newInstance().newXPath();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			String expression = "//book[@category='WEB']/price";
			NodeList nodelist = (NodeList)xPath.evaluate(expression, doc,XPathConstants.NODESET);
			Element price = (Element)(nodelist.item(0));
			price.setTextContent("111111111111");
			transformer.transform(new DOMSource(doc), new StreamResult(System.out));
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
