package com.woshidaniu.smbclient.filefilter;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;
import jcifs.smb.SmbFilenameFilter;

public interface IOSmbFileFilter extends SmbFileFilter, SmbFilenameFilter {

    /**
     * Checks to see if the File should be accepted by this filter.
     * <p>
     * Defined in {@link jcifs.smb.SmbFileFilter}.
     *
     * @param file  the SmbFile to check
     * @return true if this file matches the test
     */
	public boolean accept( SmbFile file ) throws SmbException;

    /**
     * Checks to see if the File should be accepted by this filter.
     * <p>
     * Defined in {@link jcifs.smb.SmbFilenameFilter}.
     *
     * @param dir  the directory File to check
     * @param name  the filename within the directory to check
     * @return true if this SmbFile matches the test
     */
    public boolean accept( SmbFile dir, String name ) throws SmbException;

}
