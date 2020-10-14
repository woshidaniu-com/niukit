package com.woshidaniu.smbclient.filefilter;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class SmbFileFilters {

    /**
     * Accepts all SmbFile entries, including null.
     */
    public static final IOSmbFileFilter ALL = new AbstractFileFilter() {
    	
    	@Override
        public boolean accept(SmbFile file) throws SmbException {
            return true;
        }
    	
    };

    /**
     * Accepts all non-null SmbFile entries.
     */
    public static final IOSmbFileFilter NON_NULL = new AbstractFileFilter() {
        
        @Override
        public boolean accept(SmbFile file) throws SmbException {
        	return file != null;
        }
		 
    };

    /**
     * Accepts all (non-null) SmbFile directory entries.
     */
    public static final IOSmbFileFilter DIRECTORIES = new AbstractFileFilter() {
    	
    	@Override
        public boolean accept(SmbFile file) throws SmbException {
			return file != null && file.isDirectory();
        }
    	
    };

    
}
