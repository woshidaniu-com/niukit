package com.woshidaniu.ftpclient.filefilter;

import java.io.Serializable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

@SuppressWarnings("serial")
public class SymbolicLinkFileFilter implements FTPFileFilter , Serializable {

    /** Singleton instance of file filter */
    public static final FTPFileFilter SYMBOLIC_LINK = new SymbolicLinkFileFilter();

    /**
     * Restrictive consructor.
     */
    protected SymbolicLinkFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     * @param file  the File to check
     * @return true if the file is a file
     */
    @Override
    public boolean accept(final FTPFile file) {
        return file.isSymbolicLink();
    }
    
}
