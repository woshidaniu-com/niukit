package com.woshidaniu.ftpclient.filefilter;

import java.io.Serializable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class DelegateFileFilter  implements FTPFileFilter,Serializable {

    private static final long serialVersionUID = -8723373124984771318L;
    /** The File filter */
    private final FTPFileFilter fileFilter;

    /**
     * Constructs a delegate file filter around an existing FileFilter.
     *
     * @param filter  the filter to decorate
     */
    public DelegateFileFilter(final FTPFileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The FileFilter must not be null");
        }
        this.fileFilter = filter;
    }

    /**
     * Checks the filter.
     *
     * @param file  the file to check
     * @return true if the filter matches
     */
    @Override
    public boolean accept(final FTPFile file) {
        if (fileFilter != null) {
            return fileFilter.accept(file);
        } else {
            return false;
        }
    }

    /**
     * Provide a String representaion of this file filter.
     *
     * @return a String representaion
     */
    @Override
    public String toString() {
        final String delegate = fileFilter != null ? fileFilter.toString() : "";
        return super.toString() + "(" + delegate + ")";
    }

}
