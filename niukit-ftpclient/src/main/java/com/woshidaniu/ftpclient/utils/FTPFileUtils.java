package com.woshidaniu.ftpclient.utils;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPFileFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.filefilter.FalseFileFilter;
import com.woshidaniu.ftpclient.filefilter.FileFileFilter;
import com.woshidaniu.ftpclient.filefilter.SuffixFileFilter;
import com.woshidaniu.ftpclient.filefilter.TrueFileFilter;

public class FTPFileUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FTPFileUtils.class);
	//路径分割符
	protected static String separator = FTPConfigurationUtils.SLASHES;
	
	//-----------------------------------------------------------------------
    /**
     * Finds files within a given directory (and optionally its
     * subdirectories). All files found are filtered by an FTPFileFilter.
     *
     * @param files                 the collection of files found.
     * @param directory             the directory to search in.
     * @param filter                the filter to apply to files and directories.
     * @param includeSubDirectories indicates if will include the subdirectories themselves
     * @throws IOException 
     */
    private static void innerListFiles(FTPClient ftpClient, final Collection<FTPFile> files, final FTPFile directory,
                                       final FTPFileFilter filter, final boolean includeSubDirectories) throws IOException {
    	//假设传入的目录 为 a,为了能找到a中文件
    	ftpClient.changeToParentDirectory();
    	FTPFile[] found = ftpClient.listFiles(directory.getName(), filter);
        if (found != null) {
            for (final FTPFile file : found) {
                if (file.isDirectory()) {
                    if (includeSubDirectories) {
                        files.add(file);
                    }
                    
                    
                    
        			String workDir = ftpClient.printWorkingDirectory() + separator + file.getName();
        			LOG.debug("List Dir ["+ workDir + separator + file.getName() + "]");	
        			ftpClient.changeWorkingDirectory(workDir);
                    innerListFiles(ftpClient, files, file, filter, includeSubDirectories);
                } else {
                    files.add(file);
                }
            }
        }
    }
    
	//-----------------------------------------------------------------------
	
    /**
     * Converts an array of file extensions to suffixes for use
     * with FTPFileFilters.
     *
     * @param extensions an array of extensions. Format: {"java", "xml"}
     * @return an array of suffixes. Format: {".java", ".xml"}
     */
    public static String[] toSuffixes(final String[] extensions) {
        final String[] suffixes = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            suffixes[i] = "." + extensions[i];
        }
        return suffixes;
    }
    
    public static Collection<FTPFile> listFiles(final FTPFile[] files, final String[] extensions) throws IOException {
        //Find files
        final Collection<FTPFile> file_collections = new java.util.LinkedList<FTPFile>();
        if(files != null && files.length > 0){
        	FTPFileFilter filter;
            if (extensions == null) {
                filter = TrueFileFilter.INSTANCE;
            } else {
                final String[] suffixes = toSuffixes(extensions);
                filter = new SuffixFileFilter(suffixes);
            }
            filter = FTPFileFilterUtils.and(filter, FileFileFilter.FILE);
			//循环文件
			for(FTPFile ftpFile :files){
				if(filter.accept(ftpFile)){
					file_collections.add(ftpFile);
				}
			}
		}
        return file_collections;
    }
    
    public static Collection<FTPFile> listFiles(final FTPFile[] files,  final FTPFileFilter fileFilter) throws IOException {
        //Find files
        final Collection<FTPFile> file_collections = new java.util.LinkedList<FTPFile>();
        if(files != null && files.length > 0){
        	FTPFileFilter filter;
        	if (fileFilter == null) {
                filter = TrueFileFilter.INSTANCE;
            } else {
                filter = fileFilter;
            }
			//循环文件
			for(FTPFile ftpFile :files){
				if(filter.accept(ftpFile)){
					file_collections.add(ftpFile);
				}
			}
		}
        return file_collections;
    }
    
    public static Collection<FTPFile> listFiles(FTPClient ftpClient, final FTPFile directory, final String[] extensions, final boolean recursive) throws IOException {
    	FTPFileFilter filter;
        if (extensions == null) {
            filter = TrueFileFilter.INSTANCE;
        } else {
            final String[] suffixes = toSuffixes(extensions);
            filter = new SuffixFileFilter(suffixes);
        }
        return listFiles(ftpClient, directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }
    
    public static Collection<FTPFile> listFiles( FTPClient ftpClient, final FTPFile directory, final FTPFileFilter fileFilter, final boolean recursive) throws IOException {
    	FTPFileFilter filter;
        if (fileFilter == null) {
            filter = TrueFileFilter.INSTANCE;
        } else {
            filter = fileFilter;
        }
    	return listFiles(ftpClient, directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }
    	
    public static Collection<FTPFile> listFiles( FTPClient ftpClient, final FTPFile directory, final FTPFileFilter fileFilter, final FTPFileFilter dirFilter) throws IOException {
        
    	validateListFilesParameters(directory, fileFilter);
    	separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());

        final FTPFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
        final FTPFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);

        //Find files
        final Collection<FTPFile> files = new java.util.LinkedList<FTPFile>();
        innerListFiles(ftpClient, files, directory, FTPFileFilterUtils.or(effFileFilter, effDirFilter), false);
        return files;
    }
    
    /**
     * Validates the given arguments.
     * <ul>
     * <li>Throws {@link IllegalArgumentException} if {@code directory} is not a directory</li>
     * <li>Throws {@link NullPointerException} if {@code fileFilter} is null</li>
     * </ul>
     *
     * @param directory  The File to test
     * @param fileFilter The FTPFileFilter to test
     */
    private static void validateListFilesParameters(final FTPFile directory, final FTPFileFilter fileFilter) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
        }
        if (fileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }
    }
    

    /**
     * Returns a filter that accepts files in addition to the {@link File} objects accepted by the given filter.
     *
     * @param fileFilter a base filter to add to
     * @return a filter that accepts files
     */
    private static FTPFileFilter setUpEffectiveFileFilter(final FTPFileFilter fileFilter) {
        return FTPFileFilterUtils.and(fileFilter, FTPFileFilterUtils.notFileFilter(FTPFileFilters.DIRECTORIES));
    }

    /**
     * Returns a filter that accepts directories in addition to the {@link File} objects accepted by the given filter.
     *
     * @param dirFilter a base filter to add to
     * @return a filter that accepts directories
     */
    private static FTPFileFilter setUpEffectiveDirFilter(final FTPFileFilter dirFilter) {
        return dirFilter == null ? FalseFileFilter.INSTANCE : FTPFileFilterUtils.and(dirFilter, FTPFileFilters.DIRECTORIES);
    }
    
    /**
     * Tests if the specified <code>File</code> is newer than the specified
     * time reference.
     *
     * @param file       the <code>File</code> of which the modification date must
     *                   be compared, must not be {@code null}
     * @param timeMillis the time reference measured in milliseconds since the
     *                   epoch (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified after
     * the given time reference.
     * @throws IllegalArgumentException if the file is {@code null}
     */
    public static boolean isFileNewer(final FTPFile file, final long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        return file.getTimestamp().getTimeInMillis() > timeMillis;
    }

}
