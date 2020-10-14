package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@SuppressWarnings("serial")
public class TrueFileFilter implements IOSmbFileFilter, Serializable {
	
    public static final IOSmbFileFilter TRUE = new TrueFileFilter();
    public static final IOSmbFileFilter INSTANCE = TRUE;

    /**
     * Restrictive constructor.
     */
    protected TrueFileFilter() {
    }

    /**
     * Returns true.
     *
     * @param file  the file to check (ignored)
     * @return true
     */
	@Override
	public boolean accept(SmbFile file) throws SmbException {
		 return true;
	}

	@Override
	public boolean accept(SmbFile dir, String name) throws SmbException {
		 return true;
	}

}
