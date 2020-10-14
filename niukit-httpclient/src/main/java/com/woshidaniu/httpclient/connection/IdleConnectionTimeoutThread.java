package com.woshidaniu.httpclient.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * A utility class for periodically closing idle connections.
 * @see org.apache.http.conn.HttpClientConnectionManager#closeIdleConnections(long)
 */
public class IdleConnectionTimeoutThread extends Thread {
    
    private List<HttpClientConnectionManager> connectionManagers = new ArrayList<HttpClientConnectionManager>();
    
    private boolean shutdown = false;
    
    private long timeoutInterval = 1000;
    
    private long connectionTimeout = 3000;
    
    public IdleConnectionTimeoutThread() {
        setDaemon(true);
    }
    
    /**
     * Adds a connection manager to be handled by this class.  
     * {@link HttpClientConnectionManager#closeIdleConnections(long)} will be called on the connection
     * manager every {@link #setTimeoutInterval(long) timeoutInterval} milliseconds.
     * 
     * @param connectionManager The connection manager to add
     */
    public synchronized void addConnectionManager(HttpClientConnectionManager connectionManager) {
        if (shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionManagers.add(connectionManager);
    }
    
    /**
     * Removes the connection manager from this class.  The idle connections from the connection
     * manager will no longer be automatically closed by this class.
     * 
     * @param connectionManager The connection manager to remove
     */
    public synchronized void removeConnectionManager(HttpClientConnectionManager connectionManager) {
        if (shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionManagers.remove(connectionManager);
    }
    
    /**
     * Handles calling {@link HttpClientConnectionManager#closeIdleConnections(long) closeIdleConnections()}
     * and doing any other cleanup work on the given connection mangaer.
     * @param connectionManager The connection manager to close idle connections for
     */
    protected void handleCloseIdleConnections(HttpClientConnectionManager connectionManager) {
        // 关闭失效的连接
		connectionManager.closeExpiredConnections();
		// 关闭空闲超过so_timeout * 2 指定值(单位 秒)的连接
		connectionManager.closeIdleConnections(connectionTimeout * 2, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Closes idle connections.
     */
    public synchronized void run() {
        while (!shutdown) {
            Iterator<HttpClientConnectionManager> iter = connectionManagers.iterator();
            while (iter.hasNext()) {
                HttpClientConnectionManager connectionManager = (HttpClientConnectionManager) iter.next();
                handleCloseIdleConnections(connectionManager);
            }
            
            try {
                this.wait(timeoutInterval);
            } catch (InterruptedException e) {
            }
            
        }
        // clear out the connection managers now that we're shutdown
        this.connectionManagers.clear();
    }
    
    /**
     * Stops the thread used to close idle connections.  This class cannot be used once shutdown.
     */
    public synchronized void shutdown() {
        this.shutdown = true;
        this.notifyAll();
    }
    
    /**
     * Sets the timeout value to use when testing for idle connections.
     * 
     * @param connectionTimeout The connection timeout in milliseconds
     * 
     * @see HttpClientConnectionManager#closeIdleConnections(long)
     */
    public synchronized void setConnectionTimeout(long connectionTimeout) {
        if (shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionTimeout = connectionTimeout;
    }
    
    /**
     * Sets the interval used by this class between closing idle connections.  Idle 
     * connections will be closed every <code>timeoutInterval</code> milliseconds.
     *  
     * @param timeoutInterval The timeout interval in milliseconds
     */
    public synchronized void setTimeoutInterval(long timeoutInterval) {
        if (shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.timeoutInterval = timeoutInterval;
    }
    
}

