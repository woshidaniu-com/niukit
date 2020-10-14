package com.woshidaniu.ftpclient.filefilter;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class TrueFileFilter  implements FTPFileFilter {
	
	/**
     * Singleton instance of true filter.
     * @since 1.3
     */
    public static final FTPFileFilter TRUE = new TrueFileFilter();
    /**
     * Singleton instance of true filter.
     * Please use the identical TrueFileFilter.TRUE constant.
     * The new name is more JDK 1.5 friendly as it doesn't clash with other
     * values when using static imports.
     */
    public static final FTPFileFilter INSTANCE = TRUE;

    /**
     * Restrictive constructor.
     */
    protected TrueFileFilter() {
    }

    /**
     * Returns true.
     *
     * @param file  the file to check (ignored)
     * @return true
     */
    public boolean accept(final FTPFile file) {
        return true;
    }

}
