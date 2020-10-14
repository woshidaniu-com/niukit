package com.woshidaniu.ftpclient.filefilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;


@SuppressWarnings("serial")
public class OrFileFilter implements FTPFileFilter , ConditionalFileFilter, Serializable {
	
	/** The list of file filters. */
	private final List<FTPFileFilter> fileFilters;
	
	/**
	* Constructs a new instance of <code>OrFileFilter</code>.
	*
	* @since 1.1
	*/
	public OrFileFilter() {
		this.fileFilters = new ArrayList<FTPFileFilter>();
	}
	
	/**
	* Constructs a new instance of <code>OrFileFilter</code>
	* with the specified filters.
	*
	* @param fileFilters  the file filters for this filter, copied, null ignored
	* @since 1.1
	*/
	public OrFileFilter(final List<FTPFileFilter> fileFilters) {
		if (fileFilters == null) {
		    this.fileFilters = new ArrayList<FTPFileFilter>();
		} else {
		    this.fileFilters = new ArrayList<FTPFileFilter>(fileFilters);
		}
	}
	
	/**
	* Constructs a new file filter that ORs the result of two other filters.
	* 
	* @param filter1  the first filter, must not be null
	* @param filter2  the second filter, must not be null
	* @throws IllegalArgumentException if either filter is null
	*/
	public OrFileFilter(final FTPFileFilter filter1, final FTPFileFilter filter2) {
		if (filter1 == null || filter2 == null) {
		    throw new IllegalArgumentException("The filters must not be null");
		}
		this.fileFilters = new ArrayList<FTPFileFilter>(2);
		addFileFilter(filter1);
		addFileFilter(filter2);
	}
	
	/**
	* {@inheritDoc}
	*/
	public void addFileFilter(final FTPFileFilter FTPFileFilter) {
		this.fileFilters.add(FTPFileFilter);
	}
	
	/**
	* {@inheritDoc}
	*/
	public List<FTPFileFilter> getFileFilters() {
		return Collections.unmodifiableList(this.fileFilters);
	}
	
	/**
	* {@inheritDoc}
	*/
	public boolean removeFileFilter(final FTPFileFilter FTPFileFilter) {
		return this.fileFilters.remove(FTPFileFilter);
	}
	
	/**
	* {@inheritDoc}
	*/
	public void setFileFilters(final List<FTPFileFilter> fileFilters) {
		this.fileFilters.clear();
		this.fileFilters.addAll(fileFilters);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public boolean accept(final FTPFile file) {
		for (final FTPFileFilter fileFilter : fileFilters) {
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
