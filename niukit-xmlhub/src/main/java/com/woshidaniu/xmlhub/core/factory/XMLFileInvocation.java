package com.woshidaniu.xmlhub.core.factory;

import java.io.File;
import java.io.FileDescriptor;

public interface XMLFileInvocation extends XMLEventInvocation {
	
	String getFileName();
	
	File getFile();
	
	FileDescriptor getFileDescriptor();
}
