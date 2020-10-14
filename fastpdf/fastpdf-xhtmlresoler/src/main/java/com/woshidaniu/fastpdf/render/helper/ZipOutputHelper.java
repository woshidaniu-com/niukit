
 /**
 * @title: ZipOutput.java
 * @package com.woshidaniu.fastpdf.render.helper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午2:04:04 
 */

package com.woshidaniu.fastpdf.render.helper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.pdf.PdfWriter;

 /**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: ZipOutput
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午2:04:04 
 */

public class ZipOutputHelper {
	private static ZipOutputHelper instance = null;
	private ZipOutputHelper(){}
	
	public static ZipOutputHelper getInstance(){
		instance = instance==null?new ZipOutputHelper():instance;
		return instance;
	}
	
	//设置左右文字 
	public void setDirectContent(PdfWriter writer){
		String FILE_DIR = ItextContext.getInstance().getStoreDir();
		try {
			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(FILE_DIR + "zipPDF.zip")); 
			 for (int i = 1; i <= 3; i++) { 
			     ZipEntry entry = new ZipEntry("hello_" + i + ".pdf"); 
			     zip.putNextEntry(entry); 
			     writer.setCloseStream(false); 
			     zip.closeEntry(); 
			 } 
			 zip.close();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} 
	}
}



