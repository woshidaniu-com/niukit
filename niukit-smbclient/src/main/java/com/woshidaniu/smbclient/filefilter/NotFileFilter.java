package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@SuppressWarnings("serial")
public class NotFileFilter extends AbstractFileFilter implements Serializable {

    /** The filter */
    private final IOSmbFileFilter filter;

    /**
     * Constructs a new file filter that NOTs the result of another filter.
     *
     * @param filter  the filter, must not be null
     * @throws IllegalArgumentException if the filter is null
     */
    public NotFileFilter(final IOSmbFileFilter filter) {
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
    public boolean accept(final SmbFile file ) throws SmbException {
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
