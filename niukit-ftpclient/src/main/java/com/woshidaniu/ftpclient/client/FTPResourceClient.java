package com.woshidaniu.ftpclient.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.FTPClientBuilder;
import com.woshidaniu.ftpclient.utils.FTPClientUtils;
import com.woshidaniu.ftpclient.utils.FTPConnectUtils;
 
/**
 * 
 * @className	： FTPResourceClient
 * @description	：基于ThreadLocal多线程对象复用的FTPClient资源服务客户端实现
 * @author 		：kangzhidong
 * @date		： Jan 11, 2016 11:36:00 AM
 */
public class FTPResourceClient implements IFTPClient{
	
	private ThreadLocal<FTPClient> clientThreadLocal = new ThreadLocal<FTPClient>();   
	private FTPClientBuilder clientBuilder  = null;
	/** ftp服务器地址 */
	private String host;
	/** ftp服务器端口 */
	private int port;
	/** ftp服务器用户名 */
	private String username;
	/** ftp服务器密码 */
	private String password;
	
	public FTPResourceClient(){
	}
	
	public FTPResourceClient(FTPClientBuilder builder){
		 this.clientBuilder = builder;
		 this.username = builder.getUsername();
		 this.password = builder.getPassword();
		 this.host = builder.getHost();
		 this.port = builder.getPort();
	}
	
	@Override
	public boolean makeRootDir(String targetDir) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			//在当前工作目录下新建子目录
			return FTPClientUtils.makeRootDir(ftpClient, targetDir);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean makeDir(String parentDir,String targetDir) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			//在当前工作目录下新建子目录
	        return FTPClientUtils.makeDirectory(ftpClient, parentDir, targetDir);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	public void downloadToDir(String ftpDir,String localDir) throws Exception{
		this.downloadToDir(ftpDir, new File(localDir));
	}
	
	public void downloadToDir(String ftpDir,File localDir) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToDir(ftpClient, ftpDir, localDir);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public void downloadToFile(String ftpFileName, String localFile)throws Exception {
		this.downloadToFile(ftpFileName, new File(localFile));
	}

	@Override
	public void downloadToFile(String ftpFileName, File localFile) throws Exception {
		OutputStream output = null;
		try {
			if(!localFile.exists()){
				File dir = localFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				localFile.setReadable(true);
				localFile.setWritable(true);
				localFile.createNewFile();
			}
			//创建文件输出流
			output = new BufferedOutputStream(new FileOutputStream(localFile));
			//下载文件到指定的输出流
			this.downloadToStream(ftpFileName, output);
        } finally {
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	@Override
	public void downloadToFile(String ftpDir, String ftpFileName,String localFile) throws Exception {
		this.downloadToFile(ftpDir, ftpFileName, new File(localFile));
	}
	
	@Override
	public void downloadToFile(String ftpDir, String ftpFileName, File localFile) throws Exception {
		OutputStream output = null;
		try {
			if(!localFile.exists()){
				File dir = localFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				localFile.setReadable(true);
				localFile.setWritable(true);
				localFile.createNewFile();
			}
			//创建文件输出流
			output = new BufferedOutputStream(new FileOutputStream(localFile));
			//下载文件到指定的输出流
			this.downloadToStream(ftpDir, ftpFileName, output);
        } finally {
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	@Override
	public void downloadToFileByChannel(String ftpFileName, String localFile)throws Exception {
		this.downloadToFileByChannel(ftpFileName, new File(localFile));
	}

	@Override
	public void downloadToFileByChannel(String ftpFileName, File localFile) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToFile(ftpClient, ftpFileName, localFile);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public void downloadToFileByChannel(String ftpDir, String ftpFileName,String localFile) throws Exception {
		this.downloadToFileByChannel(ftpDir, ftpFileName, new File(localFile));
	}
	
	@Override
	public void downloadToFileByChannel(String ftpDir, String ftpFileName, File localFile) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToFile(ftpClient, ftpDir, ftpFileName, localFile);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	
	@Override
	public void downloadToStream(String ftpDir, String ftpFileName,OutputStream output) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToStream(ftpClient, ftpDir, ftpFileName, output);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public void downloadToStream(String ftpFileName,OutputStream output) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToStream(ftpClient, ftpFileName, output);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public void downloadToResponse(String ftpDir, String ftpFileName,ServletResponse response) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToResponse(ftpClient, ftpDir, ftpFileName, response);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public void downloadToResponse(String ftpFileName,ServletResponse response) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			FTPClientUtils.retrieveToResponse(ftpClient, ftpFileName, response);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public FTPFile getFile(String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public FTPFile getFile(String ftpDir,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.getFTPFile(ftpClient, ftpDir, ftpFileName);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	public InputStream getFileStream(String ftpFilePath) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return ftpClient.retrieveFileStream(ftpFilePath);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public InputStream getFileStream(String ftpDir,String ftpFileName) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			//
			return FTPClientUtils.getInputStream(ftpClient, ftpDir, ftpFileName, 0);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public String[] listNames(String ftpDir) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			 return FTPClientUtils.listNames(ftpClient, ftpDir);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public List<FTPFile> listFiles(String ftpDir) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.listFiles(ftpClient, ftpDir);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public List<FTPFile> listFiles(String ftpDir, String[] extensions) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.listFiles(ftpClient, ftpDir, extensions , false );
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public List<FTPFile> listFiles(String ftpDir, String[] extensions, boolean recursion) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.listFiles(ftpClient, ftpDir, extensions , recursion );
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public List<FTPFile> listFiles(String ftpDir, FTPFileFilter filter) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.listFiles(ftpClient, ftpDir, filter , false );
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public List<FTPFile> listFiles(String ftpDir, FTPFileFilter filter, boolean recursion) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.listFiles(ftpClient, ftpDir, filter , recursion );
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	

	@Override
	public boolean remove(String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.remove(ftpClient, ftpFileName);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean remove(String[] ftpFiles) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
	        return FTPClientUtils.remove(ftpClient, ftpFiles);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean remove(String ftpDir, String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.remove(ftpClient, ftpDir, ftpFileName);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean remove(String ftpDir, String[] ftpFiles) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.remove(ftpClient, ftpDir, ftpFiles);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean removeDir(String ftpDir) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.removeDirectory(ftpClient, ftpDir, true);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean removeDir(String ftpDir, boolean isAll) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try { 
			return FTPClientUtils.removeDirectory(ftpClient, ftpDir, isAll);
		} finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public boolean upload(byte[] bytes,String ftpFileName) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		InputStream input = null;
		try {
			// 包装字节输入流  
			input = ByteArrayOutputStream.toBufferedInputStream(new ByteArrayInputStream(bytes));
			return FTPClientUtils.storeStream(ftpClient, ftpFileName, input, true);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean upload(byte[] bytes ,String ftpDir,String ftpFileName) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		InputStream input = null;
		try {
			// 包装字节输入流  
			input = ByteArrayOutputStream.toBufferedInputStream(new ByteArrayInputStream(bytes));
			return FTPClientUtils.storeStream(ftpClient, ftpDir, ftpFileName, input, true);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean upload(File localFile,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeFile(ftpClient, ftpFileName, localFile, true);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public boolean upload(File localFile,String ftpDir,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeFile(ftpClient, ftpDir, ftpFileName, localFile, true);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean upload(InputStream input,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeStream(ftpClient,  ftpFileName, input, true);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean upload(InputStream input,String ftpDir,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeStream(ftpClient, ftpDir, ftpFileName, input, true);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean upload(String localFile,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeFile(ftpClient, ftpFileName, new File(localFile), true);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public boolean upload(String localFile,String ftpDir,String ftpFileName) throws Exception {
		return this.upload(new File(localFile) ,ftpDir, ftpFileName );
	}
	
	@Override
	public boolean upload(StringBuilder fileContent ,String ftpFileName) throws Exception {
		StringReader reader = null;
		try {
			reader = new StringReader(fileContent.toString());
			return this.upload(IOUtils.toByteArray(reader) , ftpFileName);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(reader);
        }
	}
	
	@Override
	public boolean upload(StringBuilder fileContent,String ftpDir, String ftpFileName) throws Exception {
		StringReader reader = null;
		try {
			reader = new StringReader(fileContent.toString());
			return this.upload(IOUtils.toByteArray(reader) , ftpDir,ftpFileName);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(reader);
        }
	}
	
	@Override
	public boolean uploadByChannel(File localFile,String ftpFileName) throws Exception{
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeFileChannel(ftpClient, localFile, ftpFileName);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public boolean uploadByChannel(File localFile,String ftpDir,String ftpFileName) throws Exception {
		//获得一个活动连接的FTPClient
		FTPClient ftpClient = getFTPClient();
		try {
			return FTPClientUtils.storeFileChannel(ftpClient, localFile, ftpDir, ftpFileName);
        } finally {
			//释放连接  
			releaseClient(ftpClient);
        }
	}
	
	@Override
	public void sendCommand(String args) throws Exception{
		FTPClient ftpClient = getFTPClient();
		try {
            ftpClient.sendSiteCommand(args);
        } finally {
        	//释放连接  
			releaseClient(ftpClient);
        }
	}

	@Override
	public FTPClient getFTPClient() throws Exception {
		if (clientBuilder == null) {
			throw new IllegalArgumentException("clientBuilder is null.");
		}
		if (clientThreadLocal.get() != null && clientThreadLocal.get().isConnected()) {  
            return clientThreadLocal.get();  
        } else {
        	//构造一个FtpClient实例  
        	FTPClient ftpClient = getClientBuilder().build();
			//连接FTP服务器
        	boolean isConnected = FTPConnectUtils.connect(ftpClient, getHost(), getPort(), getUsername(), getPassword());
        	if(isConnected){
				//初始化已经与FTP服务器建立连接的FTPClient
				clientBuilder.initConnectedSocket(ftpClient);
				clientBuilder.initConnectionMode(ftpClient);
			}
    		clientThreadLocal.set(ftpClient);
    		return ftpClient;
        }
	}
 
	@Override
	public void releaseClient(FTPClient ftpClient) throws Exception{
		//断开连接  
		FTPConnectUtils.releaseConnect(ftpClient);
	}
	
	public FTPClientBuilder getClientBuilder() {
		return clientBuilder;
	}

	public void setClientBuilder(FTPClientBuilder clientBuilder) {
		this.clientBuilder = clientBuilder;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
