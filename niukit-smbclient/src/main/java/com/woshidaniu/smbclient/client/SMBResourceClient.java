package com.woshidaniu.smbclient.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletResponse;

import jcifs.smb.SmbFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.SMBClientBuilder;
import com.woshidaniu.smbclient.filefilter.IOSmbFileFilter;
import com.woshidaniu.smbclient.utils.SMBClientUtils;
 
/**
 * 
 *@类名称		: SMBResourceClient.java
 *@类描述		: 基于ThreadLocal多线程对象复用的SMBClient共享文件资源服务客户端实现
 *@创建人	 	: kangzhidong
 *@创建时间	: Jan 22, 2016 3:06:39 PM
 *@修改人		: 
 *@修改时间	: 
 *@版本号		: v1.0
 */
public class SMBResourceClient implements ISMBClient{
	
	private ThreadLocal<SMBClient> clientThreadLocal = new ThreadLocal<SMBClient>();  
	private SMBClientBuilder clientBuilder;
	/** 【共享文件】服务器地址 */
	protected String host;
	/** 【共享文件】服务器用户名 */
	protected String username;
	/** 【共享文件】服务器密码 */
	protected String password;
	/** 【共享文件】服务器共享目录*/
	protected String sharedDir;
	
	public SMBResourceClient(){
	}
	
	public SMBResourceClient(SMBClientBuilder builder){
		 this.clientBuilder = builder;
		 this.username = builder.getUsername();
		 this.password = builder.getPassword();
		 this.host = builder.getHost();
		 this.sharedDir = builder.getSharedDir();
	}
	
	@Override
	public boolean makeDir(String targetDir) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//当前目录
			SmbFile currentDir = new SmbFile(smbClient,targetDir);
			if(!currentDir.exists()){
				currentDir.mkdirs();
			}
			return true;
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	public void downloadToDir(String sharedDir,String localDir) throws Exception{
		this.downloadToDir(sharedDir, new File(localDir));
	}
	
	public void downloadToDir(String sharedDir,File localDir) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//当前共享目录
			SMBClient currentDir = new SMBClient(smbClient, sharedDir.endsWith("/") ? sharedDir : sharedDir + "/");
			//复制共享目录到指定的本地目录
			SMBClientUtils.retrieveToDir(currentDir, localDir);
        } finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public void downloadToFile(String filepath, File localFile) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//源文件
			SMBClient smbFile = new SMBClient( smbClient, filepath);
			//下载共享文件至输出流
			SMBClientUtils.retrieveToFile(smbFile, localFile);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public void downloadToFile(String filepath, String localFile) throws Exception {
		this.downloadToFile(filepath, new File(localFile));
	}
	
	@Override
	public void downloadToFile(String sharedDir,String fileName, String localFile) throws Exception{
		//解析共享文件路径
		String filepath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		this.downloadToFile(filepath, localFile);
	}
	
	@Override
	public void downloadToFile(String sharedDir,String fileName, File localFile) throws Exception{
		//解析共享文件路径
		String filepath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		this.downloadToFile(filepath, localFile);
	}

	@Override
	public void downloadToStream(String filepath,OutputStream output) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//源文件
			SMBClient smbFile = new SMBClient( smbClient, filepath);
			//下载共享文件至输出流
			SMBClientUtils.retrieveToStream(smbFile, output);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	public void downloadToStream(String sharedDir,String fileName,OutputStream output) throws Exception{
		//解析共享文件路径
		String filepath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		this.downloadToStream(filepath, output);
	}
	
	@Override
	public void downloadToResponse(String filepath,ServletResponse response) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//源文件
			SMBClient smbFile = new SMBClient( smbClient, filepath);
			//下载共享文件至输出流
			SMBClientUtils.retrieveToResponse(smbFile, response);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	public void downloadToResponse(String sharedDir,String fileName,ServletResponse response) throws Exception{
		//解析共享文件路径
		String filepath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		this.downloadToResponse(filepath, response);
	}

	@Override
	public SmbFile getFile(String filepath) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//源文件
			return new SMBClient( smbClient, filepath);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public SmbFile getFile(String sharedDir,String fileName) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//源文件
			return new SMBClient( smbClient, sharedDir + "/" + fileName);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public InputStream getInputStream(String filepath) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			return SMBClientUtils.getInputStream(smbClient, filepath);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public String[] listNames(String sharedDir) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
	        return SMBClientUtils.listNames(smbClient, sharedDir);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}

	@Override
	public SMBClient[] listFiles(String sharedDir) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir);
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public SMBClient[] listFiles(String sharedDir,boolean recursion) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir, recursion);
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}

	@Override
	public SMBClient[] listFiles(String sharedDir, String[] extensions) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir, extensions, false);
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public SMBClient[] listFiles(String sharedDir, String[] extensions,boolean recursion) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir, extensions , recursion);
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public SMBClient[] listFiles(String sharedDir, IOSmbFileFilter filter) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir, filter , false );
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
		
	@Override
	public SMBClient[] listFiles(String sharedDir, IOSmbFileFilter filter,boolean recursion) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//列出当前工作目录的文件信息
			List<SMBClient> list = SMBClientUtils.listFiles(smbClient, sharedDir, filter , recursion);
	        return list.toArray(new SMBClient[list.size()] );
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}

	@Override
	public boolean remove(String filepath) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			return SMBClientUtils.remove(smbClient, filepath);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean remove(String[] filepaths) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
	        return SMBClientUtils.remove(smbClient, filepaths);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean remove(String sharedDir, String fileName) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//当前共享目录
			SMBClient currentDir = new SMBClient(smbClient,sharedDir);
			//删除【共享文件】服务器上的一个指定文件
			return SMBClientUtils.remove(currentDir, fileName);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean remove(String sharedDir, String[] fileNames) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			//当前共享目录
			SMBClient currentDir = new SMBClient(smbClient,sharedDir);
			//删除【共享文件】服务器上的多个指定文件
			return SMBClientUtils.remove(currentDir, fileNames);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean removeDir(String sharedDir) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			return SMBClientUtils.removeDir(smbClient, sharedDir);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}

	public boolean rename(String filepath,String fileName) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			return SMBClientUtils.rename(smbClient, filepath, fileName);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	public boolean renameTo(String filepath,String destpath) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try { 
			return SMBClientUtils.renameTo(smbClient, filepath, destpath);
		} finally {
			//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean upload(byte[] bytes,String destpath) throws Exception{
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(bytes);
			return this.upload(ByteArrayOutputStream.toBufferedInputStream(input),destpath);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        }
	}
	
	@Override
	public boolean upload(byte[] bytes,String sharedDir,String fileName) throws Exception{
		//解析共享文件路径
		String destpath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		return this.upload(bytes, destpath);
	}
	
	@Override
	public boolean upload(File localFile,String destpath) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//目标文件
			SMBClient sharedFile = new SMBClient(smbClient,destpath);
			//存储本地文件到【文件共享服务器】
			return SMBClientUtils.storeFile(localFile , sharedFile);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean upload(File localFile,String sharedDir,String fileName) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			//解析共享文件路径
			String destpath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
			//存储本地文件到【文件共享服务器】
			return SMBClientUtils.storeFile(localFile, smbClient, destpath, true);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean upload(InputStream input,String destpath) throws Exception {
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			return SMBClientUtils.storeStream(input ,smbClient, destpath);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean upload(InputStream input,String sharedDir,String fileName) throws Exception {
		//解析共享文件路径
		String destpath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		return this.upload(input, destpath);
	}
	
	@Override
	public boolean upload(String filepath,String destpath) throws Exception {
		return this.upload(new File(filepath),destpath);
	}

	@Override
	public boolean upload(String filepath,String sharedDir,String fileName) throws Exception {
		return this.upload(new File(filepath),sharedDir, fileName);
	}

	@Override
	public boolean upload(StringBuilder fileContent,String destpath) throws Exception {
		StringReader reader = null;
		try {
			reader = new StringReader(fileContent.toString());
			return this.upload(IOUtils.toByteArray(reader),destpath);
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(reader);
        }
	}
	
	@Override
	public boolean upload(StringBuilder fileContent,String sharedDir, String fileName) throws Exception {
		//解析共享文件路径
		String destpath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		return this.upload(fileContent, destpath);
	}
	
	@Override
	public boolean uploadByChannel(File localFile,String destpath) throws Exception{
		//获得一个SMBClient对象
		SMBClient smbClient = getSMBClient();
		try {
			return SMBClientUtils.storeFileChannel(localFile ,smbClient, destpath);
        } finally {
        	//释放对象  
			releaseClient(smbClient);
        }
	}
	
	@Override
	public boolean uploadByChannel(File localFile,String sharedDir,String fileName) throws Exception{
		//解析共享文件路径
		String destpath = FilenameUtils.getFullPath(sharedDir + "/" + fileName) + fileName;
		//存储本地文件到【文件共享服务器】
		return this.uploadByChannel(localFile, destpath);
	}
	
	@Override
	public SMBClient getSMBClient() throws Exception {
		if (clientBuilder == null) {
			throw new IllegalArgumentException("clientBuilder is null.");
		}
		if (clientThreadLocal.get() != null && !clientThreadLocal.get().getDoInput() && !clientThreadLocal.get().getDoOutput()) {  
            return clientThreadLocal.get();  
        } else {
        	//构造一个SMBClient实例  
        	SMBClient smbClient = getClientBuilder().build();
        	//尝试连接 ;SmbFile的connect()方法可以尝试连接远程文件夹，如果账号或密码错误，将抛出连接异常
        	smbClient.connect(); 
    		clientThreadLocal.set(smbClient);
    		return smbClient;
        }
	}
 
	@Override
	public void releaseClient(SMBClient smbClient) throws Exception{
		//断开连接  
		//SMBConnectUtils.releaseConnect(smbClient);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getSharedDir() {
		return sharedDir;
	}

	public void setSharedDir(String sharedDir) {
		this.sharedDir = sharedDir;
	}

	public void setClientBuilder(SMBClientBuilder clientBuilder) {
		this.clientBuilder = clientBuilder;
	}

	public SMBClientBuilder getClientBuilder() {
		return clientBuilder;
	}
	
}
