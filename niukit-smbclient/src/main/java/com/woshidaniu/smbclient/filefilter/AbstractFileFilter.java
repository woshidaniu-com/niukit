package com.woshidaniu.smbclient.filefilter;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class AbstractFileFilter  implements IOSmbFileFilter {

    /**
     * Checks to see if the File should be accepted by this filter.
     * 
     * @param file  the File to check
     * @return true if this file matches the test
     */
	public boolean accept(final SmbFile file ) throws SmbException {
        return true;
    }

    /**
     * Checks to see if the File should be accepted by this filter.
     * 
     * @param dir  the directory File to check
     * @param name  the filename within the directory to check
     * @return true if this file matches the test
     */
	public boolean accept( SmbFile dir, String name ) throws SmbException {
        try {
			return accept(new SmbFile(dir, name));
		} catch (Exception e) {
			e.printStackTrace();
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
        return getClass().getSimpleName();
    }

}
