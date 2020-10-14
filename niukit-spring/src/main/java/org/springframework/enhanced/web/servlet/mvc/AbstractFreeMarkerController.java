package org.springframework.enhanced.web.servlet.mvc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.enhanced.web.servlet.view.freemarker.FreeMarker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.AbstractController;

import freemarker.template.TemplateException;

/**
 * 
 * @className	： AbstractFreeMarkerController
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午8:50:42
 * @version 	V1.0
 */
public abstract class AbstractFreeMarkerController extends AbstractController {

	private FreeMarker freeMarker = null;
	public AbstractFreeMarkerController() {
		super();
	}
	
	protected ResponseEntity<String> getFreeMark(String name,Map<String,Object> data) throws IOException{
		try {
			data = buildData(data);
			if(freeMarker==null){
				freeMarker = new FreeMarker(this.getRequest(),this.getClass());
			}
			freeMarker.setSessionId(this.getSession().getId());
			return freeMarker.getFreeMark(name, data);
		} catch (TemplateException e) {
			throw new IOException(e);
		} 
	}
	
	private Map<String,Object> buildData(Map<String,Object> data) {
		if(data==null){
			data = new HashMap<String,Object>();
		}
		HttpServletRequest request = this.getRequest();
		Enumeration<String> e = request.getAttributeNames();
		while(e.hasMoreElements()){
			String element = e.nextElement();
			data.put(element, request.getAttribute(element));
		}
		return data;
	}
	
	protected HttpServletRequest getRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
	}
	
	protected HttpSession getSession(){
		return getRequest().getSession();
	}
	
	/*protected ServletContext getServletContext(){
		return getRequest().getSession().getServletContext();
	}*/
	
}
