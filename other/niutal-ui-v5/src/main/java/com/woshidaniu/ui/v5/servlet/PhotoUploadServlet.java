package com.woshidaniu.ui.v5.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.alibaba.fastjson.JSONObject;

public class PhotoUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 6675954851816403663L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", false);
		try {
			FileItemIterator files = upload.getItemIterator(req);
			
			while( files.hasNext() ){
				FileItemStream file = files.next();
				if( !file.isFormField() ) {
					BufferedInputStream	inputStream = new BufferedInputStream(file.openStream());
					File newFile = new File(req.getRealPath("/photo.jpg"));
					BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
		            Streams.copy(inputStream, outputStream, true);
					inputStream.close();
					outputStream.close();
				}
			}
			result.put("success", true);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		//返回图片的保存结果（返回内容为json字符串，可自行构造，该处使用fastjson构造）
		PrintWriter writer = resp.getWriter();
		writer.print(JSONObject.toJSONString(result));
	}

	
}
