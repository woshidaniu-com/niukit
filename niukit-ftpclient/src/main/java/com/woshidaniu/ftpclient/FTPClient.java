package com.woshidaniu.ftpclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.FromNetASCIIInputStream;
import org.apache.commons.net.io.ToNetASCIIOutputStream;
import org.apache.commons.net.io.Util;

import com.woshidaniu.ftpclient.io.CopyStreamProcessListener;

/**
 * 
 * @className	： FTPClient
 * @description	：继承扩展org.apache.commons.net.ftp.FTPClient，实现根据配置自定义是否实时刷新
 * @author 		：kangzhidong
 * @date		： Jan 13, 2016 1:50:45 PM
 */
public class FTPClient extends org.apache.commons.net.ftp.FTPClient {
	
	/** The default buffer size： 8M*/
	protected static final int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;
	/** The default buffer size of FileChannel： 2M*/
	protected static final int DEFAULT_CHANNEL_SIZE = 2 * 1024 * 1024;
	// buffersize for buffered data streams
	protected int bufferSize_ = DEFAULT_BUFFER_SIZE; 
	// file type
	protected int fileType_;
    // Listener used by store/retrieve methods to handle keepalive
    protected CopyStreamListener copyStreamListener_;
    // How long to wait before sending another control keep-alive message
    protected long controlKeepAliveTimeout_;
    // How long to wait (ms) for keepalive message replies before continuing
    // Most FTP servers don't seem to support concurrent control and data connection usage
    protected int controlKeepAliveReplyTimeout_ = 1000;
    // Automatic refresh cache
    protected boolean autoFlush = false;
    // The minimum threshold can be automatically refreshed when upload / download with stream 
    // Only valid when autoflush is true
    protected int autoFlushBlockSize = DEFAULT_BUFFER_SIZE;
    /** The buffer size of FileChannel Read;默认 2M */
    protected int channelReadBufferSize = DEFAULT_CHANNEL_SIZE;
	/** The buffer size of FileChannel Write;默认 2M */
	protected int channelWriteBufferSize = DEFAULT_CHANNEL_SIZE;
	
	protected String localEncoding;
    // Listener used by store/retrieve methods to handle process
	protected CopyStreamProcessListener copyStreamProcessListener;
	// FTP Client Config
	protected FTPClientConfig clientConfig;
	
    /**
     * @since 3.1
     * @param command the command to send
     * @param remote the remote file name
     * @param local the local file name
     * @return true if successful
     * @throws IOException on error
     */
    protected boolean _storeFile(String command, String remote, InputStream local) throws IOException {
        Socket socket = _openDataConnection_(command, remote);

        if (socket == null) {
            return false;
        }

        final OutputStream output;

        if (fileType_ == ASCII_FILE_TYPE) {
            output = new ToNetASCIIOutputStream(getBufferedOutputStream(socket.getOutputStream()));
        } else {
            output = getBufferedOutputStream(socket.getOutputStream());
        }

        CSL csl = null;
        if (controlKeepAliveTimeout_ > 0) {
            csl = new CSL(this, controlKeepAliveTimeout_, controlKeepAliveReplyTimeout_);
        }

        // Treat everything else as binary for now
        try {
            Util.copyStream(local, output, getBufferSize(),
                    CopyStreamEvent.UNKNOWN_STREAM_SIZE, mergeListeners_(csl),
                    autoFlush);
        } catch (IOException e){
            Util.closeQuietly(socket); // ignore close errors here
            if (csl != null) {
                csl.cleanUp(); // fetch any outstanding keepalive replies
            }
            throw e;
        }

        output.close(); // ensure the file is fully written
        socket.close(); // done writing the file
        if (csl != null) {
            csl.cleanUp(); // fetch any outstanding keepalive replies
        }
        // Get the transfer response
        boolean ok = completePendingCommand();
        return ok;
    }

    /**
     * @param command the command to get
     * @param remote the remote file name
     * @param local the local file name
     * @return true if successful
     * @throws IOException on error
     * @since 3.1
     */
    protected boolean _retrieveFile(String command, String remote, OutputStream local) throws IOException {
    	
        Socket socket = _openDataConnection_(command, remote);

        if (socket == null) {
            return false;
        }

        final InputStream input;
        if (fileType_ == ASCII_FILE_TYPE) {
            input = new FromNetASCIIInputStream(getBufferedInputStream(socket.getInputStream()));
        } else {
            input = getBufferedInputStream(socket.getInputStream());
        }

        CSL csl = null;
        if (controlKeepAliveTimeout_ > 0) {
            csl = new CSL(this, controlKeepAliveTimeout_, controlKeepAliveReplyTimeout_);
        }

        // Treat everything else as binary for now
        try {
            Util.copyStream(input, local, getBufferSize(),
                    CopyStreamEvent.UNKNOWN_STREAM_SIZE, mergeListeners_(csl),
                    autoFlush);
        } finally {
            Util.closeQuietly(input);
            Util.closeQuietly(socket);
            if (csl != null) {
                csl.cleanUp(); // fetch any outstanding keepalive replies
            }
        }

        // Get the transfer response
        boolean ok = completePendingCommand();
        return ok;
    }
  
	private OutputStream getBufferedOutputStream(OutputStream outputStream) {
        if (bufferSize_ > 0) {
            return new BufferedOutputStream(outputStream, bufferSize_);
        }
        return new BufferedOutputStream(outputStream);
    }

    private InputStream getBufferedInputStream(InputStream inputStream) {
        if (bufferSize_ > 0) {
            return new BufferedInputStream(inputStream, bufferSize_);
        }
        return new BufferedInputStream(inputStream);
    }
    
    // @since 3.0
    private static class CSL implements CopyStreamListener {

        protected final FTPClient parent;
        protected final long idle;
        protected final int currentSoTimeout;

        protected long time = System.currentTimeMillis();
        protected int notAcked;

        CSL(FTPClient parent, long idleTime, int maxWait) throws SocketException {
            this.idle = idleTime;
            this.parent = parent;
            this.currentSoTimeout = parent.getSoTimeout();
            parent.setSoTimeout(maxWait);
        }

//        @Override
        public void bytesTransferred(CopyStreamEvent event) {
            bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
        }

//        @Override
        public void bytesTransferred(long totalBytesTransferred,
                int bytesTransferred, long streamSize) {
            long now = System.currentTimeMillis();
            if (now - time > idle) {
                try {
                    parent.__noop();
                } catch (SocketTimeoutException e) {
                    notAcked++;
                } catch (IOException e) {
                    // Ignored
                }
                time = now;
            }
        }

        void cleanUp() throws IOException {
            try {
                while(notAcked-- > 0) {
                    parent.__getReplyNoReport();
                }
            } finally {
                parent.setSoTimeout(currentSoTimeout);
            }
        }

    }

    /**
     * Merge two copystream listeners, either or both of which may be null.
     * @param local the listener used by this class, may be null
     * @return a merged listener or a single listener or null
     * @since 3.0
     */
    public CopyStreamListener mergeListeners_(CopyStreamListener local) {
        if (local == null) {
            return copyStreamListener_;
        }
        if (copyStreamListener_ == null) {
            return local;
        }
        // Both are non-null
        CopyStreamAdapter merged = new CopyStreamAdapter();
        merged.addCopyStreamListener(local);
        merged.addCopyStreamListener(copyStreamListener_);
        return merged;
    }
    
    /**
     * Merge two copystream listeners, either or both of which may be null.
     * @param local the listener used by this class, may be null
     * @return a merged listener or a single listener or null
     * @since 3.0
     */
    public CopyStreamListener getMergedCopyListeners() {
    	  // Both are non-null
        CopyStreamAdapter merged = new CopyStreamAdapter();
    	try {
			CSL csl = null;
			if (controlKeepAliveTimeout_ > 0) {
			    csl = new CSL(this, controlKeepAliveTimeout_, controlKeepAliveReplyTimeout_);
			}
			
			if (csl != null) {
				merged.addCopyStreamListener(csl);
			}
			if (copyStreamListener_ != null) {
				merged.addCopyStreamListener(copyStreamListener_);
			}
			if (copyStreamProcessListener != null) {
				merged.addCopyStreamListener(copyStreamProcessListener);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
        return merged;
    }

    public void setAutoFlush(boolean autoflush) {
		this.autoFlush = autoflush;
	}
	
	public boolean isAutoFlush() {
		return autoFlush;
	}
	
	public CopyStreamProcessListener getCopyStreamProcessListener() {
		return copyStreamProcessListener;
	}

	public void setCopyStreamProcessListener(CopyStreamProcessListener copyStreamProcessListener) {
		this.copyStreamProcessListener = copyStreamProcessListener;
	}

	public int getAutoFlushBlockSize() {
		return autoFlushBlockSize;
	}

	public void setAutoFlushBlockSize(int autoFlushBlockSize) {
		this.autoFlushBlockSize = autoFlushBlockSize;
	}

	public int getChannelReadBufferSize() {
		return channelReadBufferSize;
	}

	public void setChannelReadBufferSize(int channelReadBufferSize) {
		this.channelReadBufferSize = channelReadBufferSize;
	}

	public int getChannelWriteBufferSize() {
		return channelWriteBufferSize;
	}

	public void setChannelWriteBufferSize(int channelWriteBufferSize) {
		this.channelWriteBufferSize = channelWriteBufferSize;
	}

	public String getLocalEncoding() {
		return localEncoding;
	}

	public void setLocalEncoding(String localEncoding) {
		this.localEncoding = localEncoding;
	}

	public int getBufferSize_() {
		return bufferSize_;
	}

	public void setBufferSize_(int bufferSize) {
		bufferSize_ = bufferSize;
	}

	public int getFileType_() {
		return fileType_;
	}

	public void setFileType_(int fileType) {
		fileType_ = fileType;
	}

	public CopyStreamListener getCopyStreamListener_() {
		return copyStreamListener_;
	}

	public void setCopyStreamListener_(CopyStreamListener copyStreamListener) {
		copyStreamListener_ = copyStreamListener;
	}

	public long getControlKeepAliveTimeout_() {
		return controlKeepAliveTimeout_;
	}

	public void setControlKeepAliveTimeout_(long controlKeepAliveTimeout) {
		controlKeepAliveTimeout_ = controlKeepAliveTimeout;
	}

	public int getControlKeepAliveReplyTimeout_() {
		return controlKeepAliveReplyTimeout_;
	}

	public void setControlKeepAliveReplyTimeout_(int controlKeepAliveReplyTimeout) {
		controlKeepAliveReplyTimeout_ = controlKeepAliveReplyTimeout;
	}

	/**
	 * @return the clientConfig
	 */
	public FTPClientConfig getClientConfig() {
		return clientConfig;
	}

	/**
	 * @param clientConfig the clientConfig to set
	 */
	public void setClientConfig(FTPClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}
    
} 