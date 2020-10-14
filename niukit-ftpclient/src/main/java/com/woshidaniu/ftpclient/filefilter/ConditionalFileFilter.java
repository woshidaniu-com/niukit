package com.woshidaniu.ftpclient.filefilter;

import java.util.List;

import org.apache.commons.net.ftp.FTPFileFilter;

/**
 * Defines operations for conditional file filters.
 */
public interface ConditionalFileFilter {

    void addFileFilter(FTPFileFilter FTPFileFilter);

    List<FTPFileFilter> getFileFilters();

    boolean removeFileFilter(FTPFileFilter FTPFileFilter);

    void setFileFilters(List<FTPFileFilter> fileFilters);

}
