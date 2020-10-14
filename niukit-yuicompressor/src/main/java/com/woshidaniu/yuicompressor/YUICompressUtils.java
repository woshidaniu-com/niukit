package com.woshidaniu.yuicompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import com.yahoo.platform.yui.compressor.org.mozilla.javascript.ErrorReporter;
import com.yahoo.platform.yui.compressor.org.mozilla.javascript.EvaluatorException;

/**
 * 
 *@类名称:YUICompressUtils.java
 *@类描述：该程序使用了yahoo的YUI组件， 下载地址：http://yuilibrary.com/downloads/#yuicompressor，
 *                         英文帮助文档地址：http://developer.yahoo.com/yui/compressor/，
 *                         所以测试该代码的时候请先去下载该组件
 *@创建人：kangzhidong
 *@创建时间：2015-4-28 上午11:53:08
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public  class YUICompressUtils {

	public static final Logger LOG = LoggerFactory.getLogger(YUICompressUtils.class);
	public static int linebreakpos = -1;
	public static boolean munge = true;
	public static boolean verbose = false;
	public static boolean preserveAllSemiColons = false;
	public static boolean disableOptimizations = false;
	
	public static void main(String[] args) throws Exception {
		//listFiles(dir);

		/*
		Writer out = new OutputStreamWriter(new FileOutputStream(new File("D:\\resources\\new1-min.js")),"utf-8");
		YUICompressUtils.compressJs(FileUtils.readFileToString(new File("D:\\resources\\new1.js"), "utf-8"), out);
		
		*/
		//FileUtils.readFileToString(new File("D:\\java\\services\\apache-tomcat-6.0.30-niutal\\webapps\\niutal-web\\js\\dynamic\\N0125-cx.js")
		File srcFile = new File("D:\\java\\services\\apache-tomcat-6.0.30-niutal\\webapps\\niutal-web\\js\\dynamic\\N0125-cx.js");
		String charset = "utf-8";
		Reader	in  = new InputStreamReader(new FileInputStream(srcFile),charset);
		Writer	out = new OutputStreamWriter(new FileOutputStream(new File(srcFile.getParentFile(),"N0125-cx-min.js")),charset);

		YUICompressUtils.compressJs(in, out);
		
		//YUICompressUtils.compressFile(new File("D:\\resources\\new1.js"), new File("D:\\resources\\new1-min.js"),"utf-8");
		
	}
 

	
	/**
	 * 
	 *@描述：根据Reader获取JavaScriptCompressor对象
	 *@创建人:kangzhidong
	 *@创建时间:2015-4-28下午12:21:58
	 *@param in
	 *@return
	 *@throws EvaluatorException
	 *@throws IOException
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static JavaScriptCompressor getJavaScriptCompressor(Reader in) throws EvaluatorException, IOException {
		JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {

			public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
				if (line < 0) {
					System.err.println("\n[WARNING] " + message);
				} else {
					System.err.println("\n[WARNING] " + line + ':' + lineOffset + ':' + message);
				}
			}

			public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
				if (line < 0) {
					System.err.println("\n[ERROR] " + message);
				} else {
					System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
				}
			}

			public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
				error(message, sourceName, line, lineSource, lineOffset);
				//return new EvaluatorException(message);
				return null;
			}
		});
        return compressor;
	}

	/**
	 * 
	 *@描述：根据Reader获取CssCompressor对象
	 *@创建人:kangzhidong
	 *@创建时间:2015-4-28下午12:23:27
	 *@param in
	 *@return
	 *@throws IOException
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static CssCompressor getCssCompressor(Reader in) throws IOException {
		 CssCompressor compressor = new CssCompressor(in);
		return compressor;
	}

	
	public static void compressJs(String jsText, Writer out) throws EvaluatorException, IOException {
		compressJs(jsText, out, "utf-8");
	}
	
	
	public static void compressJs(String jsText, Writer out,String charset)throws EvaluatorException, IOException {
		Reader in = null;
		try {
			in = new InputStreamReader(IOUtils.toInputStream(jsText,charset),charset);
			
			JavaScriptCompressor compressor  = YUICompressUtils.getJavaScriptCompressor(in);
			compressor.compress(out, null, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
			//compressor.compress(out,linebreakpos, munge, verbose, preserveAllSemiColons,disableOptimizations);
			// 刷新writer到文件
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}
	
	
	public static void compressJs(Reader in, Writer out) throws EvaluatorException, IOException{
		try {
			YUICompressUtils.getJavaScriptCompressor(in).compress(out,null,linebreakpos, munge, verbose,preserveAllSemiColons, disableOptimizations);
			// 刷新writer到文件
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}
	
	
	public static void compressCss(String cssText, Writer out)throws EvaluatorException, IOException {
		compressCss(cssText, out, "utf-8");
	}
	
	
	public static void compressCss(String cssText, Writer out,String charset) throws EvaluatorException, IOException{
		Reader in = null;
		try {
			in = new InputStreamReader(IOUtils.toInputStream(cssText,charset),charset);
			YUICompressUtils.getCssCompressor(in).compress(out, linebreakpos);
			// 刷新writer到文件
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	public static void compressCss(Reader in, Writer out) throws EvaluatorException, IOException {
		try {
			YUICompressUtils.getCssCompressor(in).compress(out,linebreakpos);
			// 刷新writer到临时文件
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	public static void compressFile(File file, Writer out) throws EvaluatorException, IOException {
		compressFile(file, out, "utf-8");
	}
	
	public static void compressFile(File file, Writer out,String charset) throws EvaluatorException, IOException {
		if (file != null && file.exists() && file.isFile()) {
			Reader in = null;
			try {
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(".js") == false && fileName.endsWith(".css") == false) {
					return;
				}
				in = new InputStreamReader(new FileInputStream(file), charset);
				if (fileName.endsWith(".js")) {
					YUICompressUtils.getJavaScriptCompressor(in).compress(out,null,linebreakpos, munge, verbose,preserveAllSemiColons, disableOptimizations);
				} else if (fileName.endsWith(".css")) {
					YUICompressUtils.getCssCompressor(in).compress(out,linebreakpos);
				}
				// 刷新writer到临时文件
				out.flush();
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(in);
			}
		}
	}

	public static void compressFile(File file, String suffix) throws EvaluatorException, IOException{
		compressFile(file, suffix, "utf-8");
	}

	public static void compressFile(File file, String suffix,String charset) throws EvaluatorException, IOException {
		if (file != null && file.exists() && file.isFile()) {
			String fileName = file.getName().toLowerCase();
			if (fileName.endsWith(".js") == false&& fileName.endsWith(".css") == false) {
				return;
			}
			Reader in = null;
			Writer out = null;
			try {
				in = new InputStreamReader(new FileInputStream(file), charset);
				// 临时文件
				File tempFile = new File(file.getAbsolutePath() + ".temp");
				
				out = new OutputStreamWriter(new FileOutputStream(tempFile), charset);
				// 判断文件类型
				if (fileName.endsWith(".js")) {
					YUICompressUtils.getJavaScriptCompressor(in).compress(out,null,linebreakpos, munge, verbose,preserveAllSemiColons, disableOptimizations);
				} else if (fileName.endsWith(".css")) {
					YUICompressUtils.getCssCompressor(in).compress(out,linebreakpos);
				}
				// 刷新writer到临时文件
				out.flush();
				// 判断是否不后缀不为空
				if (suffix == null || suffix.trim().length() == 0) {
					suffix = "min";
				}
				tempFile.renameTo(new File(file.getParentFile().getAbsolutePath() + file.getName() + suffix + FilenameUtils.getExtension(file.getAbsolutePath())));
				tempFile.delete();
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(in);
			}
		}
	}
	
	public static void compressFile(File srcFile,File desFile) throws EvaluatorException, IOException{
		compressFile(srcFile, desFile,"utf-8");
	}
	
	public static void compressFile(File srcFile,File desFile,String charset) throws EvaluatorException, IOException{
		if (srcFile != null && srcFile.exists() && srcFile.isFile()) {
			compressFile(srcFile,new FileOutputStream(desFile),charset);
		}
	}
	
	public static void compressFile(File srcFile,OutputStream outFile,String charset) throws EvaluatorException, IOException{
		if (srcFile != null && srcFile.exists() && srcFile.isFile() && outFile != null ) {
			String fileName = srcFile.getName().toLowerCase();
			if (fileName.endsWith(".js") == false && fileName.endsWith(".css") == false) {
				return;
			}
			Reader in = null;
			Writer out = null;
			try {
				in  = new InputStreamReader(new FileInputStream(srcFile),charset);
				out = new OutputStreamWriter(outFile,charset);
				// 判断文件类型
				if (fileName.endsWith(".js")) {
					YUICompressUtils.getJavaScriptCompressor(in).compress(out,null,linebreakpos, munge, verbose,preserveAllSemiColons, disableOptimizations);
				} else if (fileName.endsWith(".css")) {
					YUICompressUtils.getCssCompressor(in).compress(out,linebreakpos);
				}
				// 刷新writer到临时文件
				out.flush();
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(in);
			}
		}
	}

}
