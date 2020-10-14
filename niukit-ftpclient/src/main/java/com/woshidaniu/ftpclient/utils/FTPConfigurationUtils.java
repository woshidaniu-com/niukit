package com.woshidaniu.ftpclient.utils;

import org.apache.commons.net.ftp.FTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.ftpclient.FTPClientConfig;

public class FTPConfigurationUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FTPConfigurationUtils.class);
	/**
	 * Windows 系统路径分割符号 \
	 */
	protected static String SLASHES = "\\";
	/**
	 * Linux，Unix 系统路径分割符号 /
	 */
	protected static String BACKSLASHES = "/";
	
	public static String getFTPStyle(FTPClientConfig configuration){
		//服务端系统类型：unix,unix-trim,vms,nt,os2,os400,as400,mvs,l8,netware,macos;默认 unix
		if( !BlankUtils.isBlank(configuration.getFtpStyle())){
			if("unix".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_UNIX;
	    	}else if("unix-trim".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_UNIX_TRIM_LEADING;
	    	}else if("vms".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_VMS;
	    	}else if("nt".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_NT;
	    	}else if("os2".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_OS2;
	    	}else if("os400".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_OS400;
	    	}else if("as400".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_AS400;
	    	}else if("mvs".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_MVS;
	    	}else if("l8".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_L8;
	    	}else if("netware".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_NETWARE;
	    	}else if("macos".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return FTPClientConfig.SYST_MACOS_PETER;
	    	}
		}
		return FTPClientConfig.SYST_UNIX;
	}
	
	public static int getFileTyle(FTPClientConfig configuration){
		//文件类型：ascii,ebcdic,binary,local;默认 ascii
		if( !BlankUtils.isBlank(configuration.getFileType())){
			if("ascii".equalsIgnoreCase(configuration.getFileType())){
	    		return FTP.ASCII_FILE_TYPE;
	    	}else if("ebcdic".equalsIgnoreCase(configuration.getFileType())){
	    		return FTP.EBCDIC_FILE_TYPE;
	    	}else if("binary".equalsIgnoreCase(configuration.getFileType())){
	    		return FTP.BINARY_FILE_TYPE;
	    	}else if("local".equalsIgnoreCase(configuration.getFileType())){
	    		return FTP.LOCAL_FILE_TYPE;
	    	}
		}
		return FTP.BINARY_FILE_TYPE;
	}
	
	
	public static String getFileSeparator(FTPClientConfig configuration){
		//服务端系统类型：unix,unix-trim,vms,nt,os2,os400,as400,mvs,l8,netware,macos;默认 unix
		if( !BlankUtils.isBlank(configuration.getFtpStyle())){
			if("unix".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("unix-trim".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("vms".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("nt".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return SLASHES;
	    	}else if("os2".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("os400".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("as400".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("mvs".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("l8".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("netware".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}else if("macos".equalsIgnoreCase(configuration.getFtpStyle())){
	    		return BACKSLASHES;
	    	}
		}
		return BACKSLASHES;
	}
	
	public static int getFileStructure(FTPClientConfig configuration){
		//文件结构：file,record,page
		if("record".equalsIgnoreCase(configuration.getFileStructure())){
    		return FTP.STREAM_TRANSFER_MODE;
    	}else if("page".equalsIgnoreCase(configuration.getFileStructure())){
    		return FTP.BLOCK_TRANSFER_MODE;
    	}
		return FTP.FILE_STRUCTURE;
	}
	
	public static int getFileFormat(FTPClientConfig configuration){
		//文件格式：telnet,carriage_control,non_print
		if("telnet".equalsIgnoreCase(configuration.getFileFormat())){
    		return FTP.TELNET_TEXT_FORMAT;
    	}else if("carriage_control".equalsIgnoreCase(configuration.getFileFormat())){
    		return FTP.CARRIAGE_CONTROL_TEXT_FORMAT;
    	}
		return FTP.NON_PRINT_TEXT_FORMAT;
	}

	public static int getFileTransferMode(FTPClientConfig configuration){
		//文件传输模式 ：stream,block,compressed
		if("block".equalsIgnoreCase(configuration.getFileTransferMode())){
    		return FTP.BLOCK_TRANSFER_MODE;
    	}else if("compressed".equalsIgnoreCase(configuration.getFileTransferMode())){
    		return FTP.COMPRESSED_TRANSFER_MODE;
    	}
		return FTP.STREAM_TRANSFER_MODE;
	}
	 
}
