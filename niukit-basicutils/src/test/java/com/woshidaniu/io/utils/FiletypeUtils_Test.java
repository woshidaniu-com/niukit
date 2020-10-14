package com.woshidaniu.io.utils;

import java.io.File;

import junit.framework.TestCase;

public class FiletypeUtils_Test extends TestCase {

	
	public void testname() throws Exception {
		
		
		assertSame(FiletypeUtils.getFileType(new File("D://cos.jar")), "jar");
		
	}
	
}
