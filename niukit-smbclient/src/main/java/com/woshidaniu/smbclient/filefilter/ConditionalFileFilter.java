package com.woshidaniu.smbclient.filefilter;

import java.util.List;

/**
 * Defines operations for conditional file filters.
 */
public interface ConditionalFileFilter {

    void addFileFilter(IOSmbFileFilter IOSmbFileFilter);

    List<IOSmbFileFilter> getFileFilters();

    boolean removeFileFilter(IOSmbFileFilter IOSmbFileFilter);

    void setFileFilters(List<IOSmbFileFilter> fileFilters);

}
