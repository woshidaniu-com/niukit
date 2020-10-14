package com.woshidaniu.fastpdf.render.render;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public abstract class DocumentRender {

	
	public abstract <T> ByteArrayInputStream render(String documentID,List<T> datas) throws Exception;
	
	public abstract <T> ByteArrayInputStream render(String documentID,Map<String,String> attrs,List<T> datas) throws Exception;
	
	public abstract <T> ByteArrayInputStream render(String documentID,T data) throws Exception;
	
	public abstract <T> ByteArrayInputStream render(String documentID,Map<String,String> attrs,T data) throws Exception;
}



