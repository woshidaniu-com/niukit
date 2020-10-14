package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;

@SuppressWarnings("serial")
public class DelegateFileFilter  extends AbstractFileFilter implements Serializable {

    /** The File filter */
    private final SmbFileFilter fileFilter;

    /**
     * Constructs a delegate file filter around an existing FileFilter.
     *
     * @param filter  the filter to decorate
     */
    public DelegateFileFilter(final SmbFileFilter filter) {
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
    public boolean accept(final SmbFile file) throws SmbException {
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
