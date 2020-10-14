package com.woshidaniu.smbclient.utils;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.smbclient.filefilter.FalseFileFilter;
import com.woshidaniu.smbclient.filefilter.FileFileFilter;
import com.woshidaniu.smbclient.filefilter.IOSmbFileFilter;
import com.woshidaniu.smbclient.filefilter.SmbFileFilters;
import com.woshidaniu.smbclient.filefilter.SuffixFileFilter;
import com.woshidaniu.smbclient.filefilter.TrueFileFilter;

public class SMBFileUtils {

	protected static Logger LOG = LoggerFactory.getLogger(SMBFileUtils.class);
	
	//-----------------------------------------------------------------------
    /**
     * Finds files within a given directory (and optionally its
     * subdirectories). All files found are filtered by an SmbFileFilter.
     *
     * @param files                 the collection of files found.
     * @param directory             the directory to search in.
     * @param filter                the filter to apply to files and directories.
     * @param includeSubDirectories indicates if will include the subdirectories themselves
     * @throws IOException 
     */
    private static void innerListFiles(final Collection<SmbFile> files, final SmbFile directory,
                                       final SmbFileFilter filter, final boolean includeSubDirectories) throws IOException {
    	
    	SmbFile[] found = directory.listFiles(filter);
        if (found != null) {
            for (final SmbFile file : found) {
                if (file.isDirectory()) {
                    if (includeSubDirectories) {
                        files.add(file);
                    }
                    innerListFiles(files, file, filter, includeSubDirectories);
                } else {
                    files.add(file);
                }
            }
        }
    }
    
	//-----------------------------------------------------------------------
	
    /**
     * Converts an array of file extensions to suffixes for use
     * with SmbFileFilters.
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
    
    public static Collection<SmbFile> listFiles(final SmbFile[] files, final String[] extensions) throws IOException {
        //Find files
        final Collection<SmbFile> file_collections = new java.util.LinkedList<SmbFile>();
        if(files != null && files.length > 0){
        	IOSmbFileFilter filter;
            if (extensions == null) {
                filter = TrueFileFilter.INSTANCE;
            } else {
                final String[] suffixes = toSuffixes(extensions);
                filter = new SuffixFileFilter(suffixes);
            }
            filter = SMBFileFilterUtils.and(filter, FileFileFilter.FILE);
			//循环文件
			for(SmbFile SmbFile :files){
				if(filter.accept(SmbFile)){
					file_collections.add(SmbFile);
				}
			}
		}
        return file_collections;
    }
    
    public static Collection<SmbFile> listFiles(final SmbFile[] files,  final IOSmbFileFilter fileFilter) throws IOException {
        //Find files
        final Collection<SmbFile> file_collections = new java.util.LinkedList<SmbFile>();
        if(files != null && files.length > 0){
        	SmbFileFilter filter;
        	if (fileFilter == null) {
                filter = TrueFileFilter.INSTANCE;
            } else {
                filter = fileFilter;
            }
			//循环文件
			for(SmbFile SmbFile :files){
				if(filter.accept(SmbFile)){
					file_collections.add(SmbFile);
				}
			}
		}
        return file_collections;
    }
    
    public static Collection<SmbFile> listFiles(final SmbFile directory, final String[] extensions, final boolean recursive) throws IOException {
    	IOSmbFileFilter filter;
        if (extensions == null) {
            filter = TrueFileFilter.INSTANCE;
        } else {
            final String[] suffixes = toSuffixes(extensions);
            filter = new SuffixFileFilter(suffixes);
        }
        return listFiles( directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }
    
    public static Collection<SmbFile> listFiles( final SmbFile directory, final IOSmbFileFilter fileFilter, final boolean recursive) throws IOException {
    	IOSmbFileFilter filter;
        if (fileFilter == null) {
            filter = TrueFileFilter.INSTANCE;
        } else {
            filter = fileFilter;
        }
    	return listFiles(directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }
    	
    public static Collection<SmbFile> listFiles( final SmbFile directory, final IOSmbFileFilter fileFilter, final IOSmbFileFilter dirFilter) throws IOException {
        
    	validateListFilesParameters(directory, fileFilter);

        final IOSmbFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
        final IOSmbFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);

        //Find files
        final Collection<SmbFile> files = new java.util.LinkedList<SmbFile>();
        innerListFiles(files, directory, SMBFileFilterUtils.or(effFileFilter, effDirFilter), false);
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
     * @param fileFilter The SmbFileFilter to test
     * @throws SmbException 
     */
    private static void validateListFilesParameters(final SmbFile directory, final IOSmbFileFilter fileFilter) throws SmbException {
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
    private static IOSmbFileFilter setUpEffectiveFileFilter(final IOSmbFileFilter fileFilter) {
        return SMBFileFilterUtils.and(fileFilter, SMBFileFilterUtils.notFileFilter(SmbFileFilters.DIRECTORIES));
    }

    /**
     * Returns a filter that accepts directories in addition to the {@link File} objects accepted by the given filter.
     *
     * @param dirFilter a base filter to add to
     * @return a filter that accepts directories
     */
    private static IOSmbFileFilter setUpEffectiveDirFilter(final IOSmbFileFilter dirFilter) {
        return dirFilter == null ? FalseFileFilter.INSTANCE : SMBFileFilterUtils.and(dirFilter, SmbFileFilters.DIRECTORIES);
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
    public static boolean isFileNewer(final SmbFile file, final long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        return file.getLastModified() > timeMillis;
    }

    
}
