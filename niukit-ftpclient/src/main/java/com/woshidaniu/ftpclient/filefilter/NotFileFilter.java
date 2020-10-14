package com.woshidaniu.ftpclient.filefilter;

import java.io.Serializable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

@SuppressWarnings("serial")
public class NotFileFilter  implements FTPFileFilter , Serializable {

    /** The filter */
    private final FTPFileFilter filter;

    /**
     * Constructs a new file filter that NOTs the result of another filter.
     *
     * @param filter  the filter, must not be null
     * @throws IllegalArgumentException if the filter is null
     */
    public NotFileFilter(final FTPFileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter must not be null");
        }
        this.filter = filter;
    }

    /**
     * Returns the logical NOT of the underlying filter's return value for the same File.
     *
     * @param file  the File to check
     * @return true if the filter returns false
     */
    @Override
    public boolean accept(final FTPFile file) {
        return ! filter.accept(file);
    }

    /**
     * Provide a String representaion of this file filter.
     *
     * @return a String representaion
     */
    @Override
    public String toString() {
        return super.toString() + "(" + filter.toString()  + ")";
    }
}
