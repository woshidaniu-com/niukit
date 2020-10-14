package com.woshidaniu.fastdoc.render;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * 
 *@类名称:WordDocumentReader.java
 *@类描述：word文档读取对象
 *@创建人：kangzhidong
 *@创建时间：2015-3-6 上午11:00:35
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class WordDocumentBuilder {

	public static void buildHWPFDocument(InputStream in, OutputStream out) throws IOException {
		try {
			POIFSFileSystem fs = new POIFSFileSystem();
			// 对应于org.apache.poi.hdf.extractor.WordDocument
			fs.createDocument(in, "WordDocument");
			fs.writeFilesystem(out);
		} finally {
			out.flush();
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	
}
