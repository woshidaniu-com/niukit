package com.woshidaniu.ftpclient.filefilter;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class SizeFileFilter  implements FTPFileFilter {
	 /** The size threshold. */
    private final long size;
    /** Whether the files accepted will be larger or smaller. */
    private final boolean acceptLarger;

    /**
     * Constructs a new size file filter for files equal to or 
     * larger than a certain size.
     *
     * @param size  the threshold size of the files
     * @throws IllegalArgumentException if the size is negative
     */
    public SizeFileFilter(final long size) {
        this(size, true);
    }

    /**
     * Constructs a new size file filter for files based on a certain size
     * threshold.
     *
     * @param size  the threshold size of the files
     * @param acceptLarger  if true, files equal to or larger are accepted,
     * otherwise smaller ones (but not equal to)
     * @throws IllegalArgumentException if the size is negative
     */
    public SizeFileFilter(final long size, final boolean acceptLarger) {
        if (size < 0) {
            throw new IllegalArgumentException("The size must be non-negative");
        }
        this.size = size;
        this.acceptLarger = acceptLarger;
    }

    @Override
    public boolean accept(final FTPFile file) {
        final boolean smaller = file.getSize() < size;
        return acceptLarger ? !smaller : smaller;
    }

    /**
     * Provide a String representaion of this file filter.
     *
     * @return a String representaion
     */
    @Override
    public String toString() {
        final String condition = acceptLarger ? ">=" : "<";
        return super.toString() + "(" + condition + size + ")";
    }
}
