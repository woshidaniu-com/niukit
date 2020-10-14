package com.woshidaniu.smbclient.filefilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;


@SuppressWarnings("serial")
public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
	
	/** The list of file filters. */
	private final List<IOSmbFileFilter> fileFilters;
	
	/**
	* Constructs a new instance of <code>OrFileFilter</code>.
	*
	* @since 1.1
	*/
	public OrFileFilter() {
		this.fileFilters = new ArrayList<IOSmbFileFilter>();
	}
	
	/**
	* Constructs a new instance of <code>OrFileFilter</code>
	* with the specified filters.
	*
	* @param fileFilters  the file filters for this filter, copied, null ignored
	* @since 1.1
	*/
	public OrFileFilter(final List<IOSmbFileFilter> fileFilters) {
		if (fileFilters == null) {
		    this.fileFilters = new ArrayList<IOSmbFileFilter>();
		} else {
		    this.fileFilters = new ArrayList<IOSmbFileFilter>(fileFilters);
		}
	}
	
	/**
	* Constructs a new file filter that ORs the result of two other filters.
	* 
	* @param filter1  the first filter, must not be null
	* @param filter2  the second filter, must not be null
	* @throws IllegalArgumentException if either filter is null
	*/
	public OrFileFilter(final IOSmbFileFilter filter1, final IOSmbFileFilter filter2) {
		if (filter1 == null || filter2 == null) {
		    throw new IllegalArgumentException("The filters must not be null");
		}
		this.fileFilters = new ArrayList<IOSmbFileFilter>(2);
		addFileFilter(filter1);
		addFileFilter(filter2);
	}
	
	/**
	* {@inheritDoc}
	*/
	public void addFileFilter(final IOSmbFileFilter FTPFileFilter) {
		this.fileFilters.add(FTPFileFilter);
	}
	
	/**
	* {@inheritDoc}
	*/
	public List<IOSmbFileFilter> getFileFilters() {
		return Collections.unmodifiableList(this.fileFilters);
	}
	
	/**
	* {@inheritDoc}
	*/
	public boolean removeFileFilter(final IOSmbFileFilter FTPFileFilter) {
		return this.fileFilters.remove(FTPFileFilter);
	}
	
	/**
	* {@inheritDoc}
	*/
	public void setFileFilters(final List<IOSmbFileFilter> fileFilters) {
		this.fileFilters.clear();
		this.fileFilters.addAll(fileFilters);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public boolean accept(final SmbFile file ) throws SmbException  {
		for (final IOSmbFileFilter fileFilter : fileFilters) {
		    if (fileFilter.accept(file)) {
		        return true;
		    }
		}
		return false;
	}
	
   /**
	* Provide a String representaion of this file filter.
	*
	* @return a String representaion
	*/
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(super.toString());
		buffer.append("(");
		if (fileFilters != null) {
		    for (int i = 0; i < fileFilters.size(); i++) {
		        if (i > 0) {
		            buffer.append(",");
		        }
		        final Object filter = fileFilters.get(i);
		        buffer.append(filter == null ? "null" : filter.toString());
		    }
		}
		buffer.append(")");
		return buffer.toString();
	}
	
}
