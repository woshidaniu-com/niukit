package com.woshidaniu.qa.listener;

import static com.woshidaniu.qa.utils.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.util.ResourceUtils;

import com.woshidaniu.qa.annotation.ConfigValue;
import com.woshidaniu.qa.core.ConfigValues;
import com.woshidaniu.qa.exception.ZTesterException;
/** 
* @author 作者 E-mail: 
* @version 创建时间：2017年5月12日 下午3:21:12 
* 类说明 
*/
public class ConfigValueListener implements TestExecutionListener{
	public static final String[] PROPERTIES_FILE_SUFFIX = { ".properties" };

	/* (non-Javadoc)
	 * @see org.springframework.test.context.TestExecutionListener#beforeTestClass(org.springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		ConfigValue configValueAnn = testContext.getTestClass().getAnnotation(ConfigValue.class);// 获取配置文件注解
		String className = testContext.getTestClass().getName();

		if (null != configValueAnn) {
			String path = configValueAnn.path();
			
			File configFile = null;

			try {
				configFile = ResourceUtils.getFile(path);
			} catch (FileNotFoundException e) {

				try {
					configFile = ResourceUtils.getFile(ResourceUtils.FILE_URL_PREFIX + path);
				} catch (FileNotFoundException e1) {
					new ZTesterException("The property file is not existed! The sqlFilePath is:"
							+ ResourceUtils.FILE_URL_PREFIX + path, e1);
				}

			}

			String fileName = configFile.getName().toLowerCase();
			HashMap<String, String> configValueMaps = new HashMap<String, String>();

			if (containSuffix(PROPERTIES_FILE_SUFFIX, fileName)) {
				Properties properties = null;
				try {
					properties = loadProperties(configFile);
				} catch (IOException e) {
					new ZTesterException("The property file cannot be read ", e);
				}
				for (java.util.Map.Entry<Object, Object> entry : properties.entrySet()) {
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					configValueMaps.put(key, value);
				}
			} else {
				new ZTesterException("The config file type is not supported! Only supprot properties");
			}

			if (!configValueMaps.isEmpty()) {
				ConfigValues.addConfigValue(className, configValueMaps);
			}
		}

		
	}
	
	/**
	 * 
	 * @param configFile
	 * @return
	 * @throws IOException
	 */
	private Properties loadProperties(File configFile) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(configFile);
			if (null == inputStream) {
				throw new ZTesterException("The properties cannot be readed!");
			}
			properties.load(inputStream);
		} finally {
			closeQuietly(inputStream);
		}
		return properties;
	}

	/**
	 * 判断字符串是否以指定的后缀结尾
	 * 
	 * @param suffixArray
	 * @param fileName
	 * @return
	 */
	private boolean containSuffix(String[] suffixArray, String fileName) {
		for (String suffix : suffixArray) {
			if (StringUtils.endsWithIgnoreCase(fileName, suffix))
				return true;
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see org.springframework.test.context.TestExecutionListener#prepareTestInstance(org.springframework.test.context.TestContext)
	 */
	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.test.context.TestExecutionListener#beforeTestMethod(org.springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.test.context.TestExecutionListener#afterTestMethod(org.springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.test.context.TestExecutionListener#afterTestClass(org.springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
 