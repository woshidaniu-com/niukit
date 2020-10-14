package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;


@SuppressWarnings("serial")
public class FalseFileFilter implements IOSmbFileFilter , Serializable {
   
	public static final IOSmbFileFilter FALSE = new FalseFileFilter();
    public static final IOSmbFileFilter INSTANCE = FALSE;

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
    @Override
    public boolean accept(final SmbFile file) throws SmbException{
        return false;
    }
    
    /**
     * Returns false.
     *
     * @param dir  the directory to check (ignored)
     * @param name  the filename (ignored)
     * @return false
     */
	@Override
	public boolean accept(SmbFile dir, String name) throws SmbException {
		return false;
	}

}
