/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;

import java.io.File;
import java.io.FileFilter;

public class OutputDirFilterHandler implements FileFilter {

	protected String outdir;
	
	public OutputDirFilterHandler(String outdir) {
		this.outdir = outdir;
	}
	
	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() && pathname.getName().equals(outdir);
	}
	 
}