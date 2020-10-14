package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@SuppressWarnings("serial")
public class FileFileFilter extends AbstractFileFilter implements Serializable {

    /** Singleton instance of file filter */
    public static final IOSmbFileFilter FILE = new FileFileFilter();

    /**
     * Restrictive consructor.
     */
    protected FileFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     * @param file  the File to check
     * @return true if the file is a file
     */
    @Override
    public boolean accept(final SmbFile file) throws SmbException {
        return file.isFile();
    }
 
}
