package com.woshidaniu.compress;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public abstract class CompressUtils {
	
	protected static final String BASE_DIR = "";  
	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	protected static final ArchiveStreamFactory FACTORY = new ArchiveStreamFactory();
	
}
