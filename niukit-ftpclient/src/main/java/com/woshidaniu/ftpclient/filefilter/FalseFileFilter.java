package com.woshidaniu.ftpclient.filefilter;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;


public class FalseFileFilter implements FTPFileFilter {
	 /**
     * Singleton instance of false filter.
     * @since 1.3
     */
    public static final FTPFileFilter FALSE = new FalseFileFilter();
    /**
     * Singleton instance of false filter.
     * Please use the identical FalseFileFilter.FALSE constant.
     * The new name is more JDK 1.5 friendly as it doesn't clash with other
     * values when using static imports.
     */
    public static final FTPFileFilter INSTANCE = FALSE;

    /**
     * Restrictive consructor.
     */
    protected FalseFileFilter() {
    }

    /**
     * Returns false.
     *
     * @param file  the file to check (ignored)
     * @return false
     */
    public boolean accept(final FTPFile file) {
        return false;
    }

}
