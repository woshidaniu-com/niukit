package com.woshidaniu.smbclient.filefilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;

@SuppressWarnings("serial")
public class MagicNumberFileFilter extends AbstractFileFilter  implements Serializable {
	
	/**
	* The magic number to compare against the file's bytes at the provided
	* offset.
	*/
	private final byte[] magicNumbers;
	
	/**
	* The offset (in bytes) within the files that the magic number's bytes
	* should appear.
	*/
	private final long byteOffset;
	
	/**
	* <p>
	* Constructs a new MagicNumberFileFilter and associates it with the magic
	* number to test for in files. This constructor assumes a starting offset
	* of <code>0</code>.
	* </p>
	*
	* <p>
	* It is important to note that <em>the array is not cloned</em> and that
	* any changes to the magic number array after construction will affect the
	* behavior of this file filter.
	* </p>
	*
	* <pre>
	* MagicNumberFileFilter javaClassFileFilter =
	*     MagicNumberFileFilter(new byte[] {(byte) 0xCA, (byte) 0xFE,
	*       (byte) 0xBA, (byte) 0xBE});
	* </pre>
	*
	* @param magicNumber the magic number to look for in the file.
	*
	* @throws IllegalArgumentException if <code>magicNumber</code> is
	*         {@code null}, or contains no bytes.
	*/
	public MagicNumberFileFilter(final byte[] magicNumber) {
		this( magicNumber, 0);
	}
	
	/**
	* <p>
	* Constructs a new MagicNumberFileFilter and associates it with the magic
	* number to test for in files. This constructor assumes a starting offset
	* of <code>0</code>.
	* </p>
	*
	* Example usage:
	* <pre>
	* {@code
	* MagicNumberFileFilter xmlFileFilter =
	*     MagicNumberFileFilter("<?xml");
	* }
	* </pre>
	*
	* @param magicNumber the magic number to look for in the file.
	*        The string is converted to bytes using the platform default charset.
	*
	* @throws IllegalArgumentException if <code>magicNumber</code> is
	*         {@code null} or the empty String.
	*/
	public MagicNumberFileFilter(final String magicNumber) {
		this( magicNumber, 0);
	}
	
	/**
	* <p>
	* Constructs a new MagicNumberFileFilter and associates it with the magic
	* number to test for in files and the byte offset location in the file to
	* to look for that magic number.
	* </p>
	*
	* <pre>
	* MagicNumberFileFilter tarFileFilter =
	*     MagicNumberFileFilter("ustar", 257);
	* </pre>
	*
	* @param magicNumber the magic number to look for in the file.
	*        The string is converted to bytes using the platform default charset.
	* @param offset the byte offset in the file to start comparing bytes.
	*
	* @throws IllegalArgumentException if <code>magicNumber</code> is
	*         {@code null} or the empty String, or <code>offset</code> is
	*         a negative number.
	*/
	public MagicNumberFileFilter(final String magicNumber, final long offset) {
		if (magicNumber == null) {
		    throw new IllegalArgumentException("The magic number cannot be null");
		}
		if (magicNumber.isEmpty()) {
		    throw new IllegalArgumentException("The magic number must contain at least one byte");
		}
		if (offset < 0) {
		    throw new IllegalArgumentException("The offset cannot be negative");
		}
		this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset()); // explicitly uses the platform default
		this.byteOffset = offset;
	}
	
	/**
	* <p>
	* Constructs a new MagicNumberFileFilter and associates it with the magic
	* number to test for in files and the byte offset location in the file to
	* to look for that magic number.
	* </p>
	*
	* <pre>
	* MagicNumberFileFilter tarFileFilter =
	*     MagicNumberFileFilter(new byte[] {0x75, 0x73, 0x74, 0x61, 0x72}, 257);
	* </pre>
	*
	* <pre>
	* MagicNumberFileFilter javaClassFileFilter =
	*     MagicNumberFileFilter(new byte[] {0xCA, 0xFE, 0xBA, 0xBE}, 0);
	* </pre>
	*
	* @param magicNumber the magic number to look for in the file.
	* @param offset the byte offset in the file to start comparing bytes.
	*
	* @throws IllegalArgumentException if <code>magicNumber</code> is
	*         {@code null}, or contains no bytes, or <code>offset</code>
	*         is a negative number.
	*/
	public MagicNumberFileFilter(final byte[] magicNumber, final long offset) {
		if (magicNumber == null) {
		    throw new IllegalArgumentException("The magic number cannot be null");
		}
		if (magicNumber.length == 0) {
		    throw new IllegalArgumentException("The magic number must contain at least one byte");
		}
		if (offset < 0) {
		    throw new IllegalArgumentException("The offset cannot be negative");
		}
		this.magicNumbers = new byte[magicNumber.length];
		System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
		this.byteOffset = offset;
	}
	
	/**
	* <p>
	* Accepts the provided file if the file contains the file filter's magic
	* number at the specified offset.
	* </p>
	*
	* <p>
	* If any {@link IOException}s occur while reading the file, the file will
	* be rejected.
	* </p>
	*
	* @param file the file to accept or reject.
	*
	* @return {@code true} if the file contains the filter's magic number at the specified offset, {@code false} otherwise.
	*/
	@Override
	public boolean accept(final SmbFile file ) throws SmbException {
		if (file != null && file.isFile() && file.canRead()) {
		    RandomAccessFile randomAccessFile = null;
		    //临时文件
		    File tmpFile = new File(SystemUtils.getUserDir(),UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName()));
		    try {
		        final byte[] fileBytes = new byte[this.magicNumbers.length];
		        OutputStream output = new FileOutputStream(tmpFile);
		        InputStream input = file.getInputStream();
		        IOUtils.copy(input, output);
		        output.flush();
		        IOUtils.closeQuietly(output);
				randomAccessFile = new RandomAccessFile(tmpFile, "r");
				randomAccessFile.seek(byteOffset);
				final int read = randomAccessFile.read(fileBytes);
				if (read != magicNumbers.length) {
				    return false;
				}
				return Arrays.equals(this.magicNumbers, fileBytes);
		    } catch (final IOException ioe) {
		        // Do nothing, fall through and do not accept file
		    } finally {
		    	FileUtils.deleteQuietly(tmpFile);
		        IOUtils.closeQuietly(randomAccessFile);
		    }
		}
		
		return false;
	}
	
	/**
	* Returns a String representation of the file filter, which includes the
	* magic number bytes and byte offset.
	*
	* @return a String representation of the file filter.
	*/
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("(");
		builder.append(new String(magicNumbers, Charset.defaultCharset()));// TODO perhaps use hex if value is not
		                                                                   // printable
		builder.append(",");
		builder.append(this.byteOffset);
		builder.append(")");
		return builder.toString();
	}
}
