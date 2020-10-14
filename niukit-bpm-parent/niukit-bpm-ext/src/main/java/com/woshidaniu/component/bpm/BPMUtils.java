package com.woshidaniu.component.bpm;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：com.woshidaniu.component.bpm.BpmUtils.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public final class BPMUtils extends StringUtils {

	public static String getMessage(String key){
		return prop.getProperty(key);
	}
	
	private static Properties prop = new Properties();

	static {
		load();
	}

	// 加载资源文件
	private static void load() {
		InputStream inStream = null;
		try {
			inStream = BPMUtils.class.getResourceAsStream("/bpm.properties");
			prop.load(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					inStream = null;
					e.printStackTrace();
				}
			}
		}
	}

	static class UniqID {

		private static final Logger logger = LoggerFactory.getLogger(UniqID.class);

		private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
				'f' };

		/* 使用静态内部类，延迟加载单例对象 */
		private static class SingleFactory {
			public static UniqID me = new UniqID();
		}

		private MessageDigest mHasher;

		private UniqID() {
			try {
				mHasher = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nex) {
				mHasher = null;
				logger.error("[UniqID]new MD5 Hasher error", nex);
			}
		}

		public static UniqID getInstance() {
			return SingleFactory.me;
		}

		/**
		 * 
		 * <p>
		 * 方法说明：生成唯一ID
		 * <p>
		 * <p>
		 * 作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
		 * <p>
		 * <p>
		 * 时间：2016年6月8日上午10:03:00
		 * <p>
		 * 
		 * @return Stirng ID
		 */
		public String getUniqID() {
			return UUID.randomUUID().toString();
		}

		/**
		 * 
		 * <p>
		 * 方法说明：生成唯一ID，使用加密算法
		 * <p>
		 * <p>
		 * 作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
		 * <p>
		 * <p>
		 * 时间：2016年6月8日上午10:02:21
		 * <p>
		 * 
		 * @return Stirng ID
		 */
		public String getUniqIDHash() {
			return hash(getUniqID());
		}

		private synchronized String hash(String str) {
			byte[] bt = mHasher.digest(str.getBytes());
			int l = bt.length;

			char[] out = new char[l << 1];

			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = digits[(0xF0 & bt[i]) >>> 4];
				out[j++] = digits[0x0F & bt[i]];
			}

			if (logger.isDebugEnabled()) {
				logger.debug("[UniqID.hash]" + (new String(out)));
			}

			return new String(out);
		}

	}

	public static String getUniqIDHash() {
		return UniqID.getInstance().getUniqIDHash();
	}

	public static void isProcessDefinationKeyNULL(String processDefinationKey) {
		if (processDefinationKey == null || processDefinationKey.trim().length() == 0) {
			throw new BPMException("BPM_EX_01", "ProcessDefinationKey can not be NULL");
		}
	}

	public static void isBusinessKeyNULL(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			throw new BPMException("BPM_EX_04", "BusinessKey can not be NULL");
		}
	}

	public static void isProcesssInstanceStarterNULL(String starter) {
		if (starter == null || starter.trim().length() == 0) {
			throw new BPMException("BPM_EX_05", "Process instance starter can not be NULL");
		}
	}

	public static void isProcessInstanceIdNULL(String processInstanceId) {
		if (processInstanceId == null || processInstanceId.trim().length() == 0) {
			throw new BPMException("BPM_EX_02", "ProcessInstanceId can not be NULL");
		}
	}

	public static void isTaskIdNULL(String taskId) {
		if (taskId == null || taskId.trim().length() == 0) {
			throw new BPMException("BPM_EX_03", "TaskId can not be NULL");
		}
	}

	public static void isTaskDefKeyNULL(String taskDefKey) {
		if (taskDefKey == null || taskDefKey.trim().length() == 0) {
			throw new BPMException("BPM_EX_06", "TaskDefKey can not be NULL");
		}
	}

	public static void setupAuthId(String authId) {
		Authentication.setAuthenticatedUserId(authId);
	}

}
