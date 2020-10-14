/**
 * 
 */
package com.woshidaniu.license.integration;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.license.LicenceStatic;
import com.woshidaniu.license.LicenseOps;
import com.woshidaniu.license.LicenseOpsException;
import com.woshidaniu.license.LicenseThreadExecutor;
import com.woshidaniu.license.Status;
import com.woshidaniu.license.WhoAmI;
import com.woshidaniu.license.dataSync.DataSync;
import com.woshidaniu.license.dataSync.FileDataSync;

/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����license�������
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��6��21������2:50:34
 */
public class LicenseContextListener implements ServletContextListener {
	
	private static final Logger log = LoggerFactory.getLogger(LicenseContextListener.class);
	/**
	 * license ������
	 */
	private LicenseOps ops = LicenseOps.getInstance();
	
	/**
	 * license ����ͬ��
	 */
	private DataSync dataSync = new FileDataSync();
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		try {
			final ServletContext servletContext = sce.getServletContext();
			
			if(dataSync instanceof ServletContextAware){
				((ServletContextAware)dataSync).setServletContext(servletContext);
			}
			
//			String license_file_path = servletContext.getInitParameter("license_file_path");
//			if(license_file_path == null || license_file_path.trim().length() == 0){
//				license_file_path = servletContext.getRealPath("/WEB-INF/classes/license.auth");	
//			}
//			ops.setLicenseFilePath(license_file_path);
			
			//------�����Ȩ�ļ��Ƿ����
			//File licenseFile = FileUtils.getFile(license_file_path);
			//boolean licenseFileexists = licenseFile.exists();
			//ops.setLicenseFileExist(licenseFileexists);
			
//			if(!licenseFileexists) {
//				servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.NO_LICENSE_FILE);
//				throw new LicenseOpsException("**********��Ȩ�ļ�������***********");
//			}
			//------�����Ȩ�ļ��Ƿ����
			
			String enable = servletContext.getInitParameter("_dev_license_enable");
			/**
			 * ����رգ���������Ȩģ��
			 */
			log.info("************��ʼ��ʼ����Ȩģ��********************");
			if(enable != null && (!Boolean.parseBoolean(enable))){
				ops.setEnable(false);
				
				if(log.isDebugEnabled()){
					log.debug("**********��Ȩģ��δ����***********");
				}
				
				return;
			}
			
			//------�����Ȩ�ļ���ҵ��ϵͳ���ƺ͵�ǰ���е�ҵ��ϵͳ�����Ƿ�ƥ��---------//
			String productNameProvider = servletContext.getInitParameter("product_name_provider");
			if(productNameProvider == null || productNameProvider.trim().length() == 0){
				servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
				throw new LicenseOpsException("**********û������ʵ��com.woshidaniu.license.WhoAmI�ӿڵ�ʵ���࣬��Ȩģ���޷�ʶ��ҵ��ϵͳ***********");
			}
			
			try {
				Class<?> whoAmIClazz = Class.forName(productNameProvider);
				WhoAmI whoAmIInstance = (WhoAmI) whoAmIClazz.newInstance();
				String productName = dataSync.getProductName(whoAmIInstance);
				String licenseProductName = ops.getProductName();
				
				if(log.isDebugEnabled()){
					log.debug("ProductName={}, LicenseProductName={}", new Object[]{productName,licenseProductName});
				}
				
				//TODO �Ƚ���Ȩ�ļ��е�ϵͳ���ƺ�ϵͳ�ṩ�������Ƿ�ƥ��
				if(productName == null || licenseProductName == null || (!productName.equals(licenseProductName))){
					servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
					throw new LicenseOpsException("**********��Ȩ�ļ��Ƿ���ҵ��ϵͳ���Ʋ�ƥ��***********");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
				throw new LicenseOpsException("**********" + productNameProvider+ ",�޷��ҵ����࣬��Ȩģ���޷�ʶ��ҵ��ϵͳ***********", e);
			} catch(ClassCastException ex){
				ex.printStackTrace();
				servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
				throw new LicenseOpsException("**********" + productNameProvider+ "û��ʵ��com.woshidaniu.license.WhoAmI�ӿڣ���Ȩģ���޷�ʶ��ҵ��ϵͳ***********", ex);
			} catch(Exception ex){
				ex.printStackTrace();
				servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
				throw new LicenseOpsException("**********" + productNameProvider+ "�޷���ʵ��������Ȩģ���޷�ʶ��ҵ��ϵͳ***********", ex);
			}
			
			//------�����Ȩ�ļ���ҵ��ϵͳ���ƺ͵�ǰ���е�ҵ��ϵͳ�����Ƿ�ƥ��---------//
			
			//У����Ȩ�ļ��Ƿ�Ϊ��ʼ��״̬
			if(ops.j_checkStatusIntial()){
				String authId = ops.getAuthId();
				String authDate = ops.getAuthDate();
				
				String oAuthId = null;
				String oAuthDate = null;
				try {
				//��ʼ��֮ǰ����Ҫ�жϸ���Ȩ�ļ��Ƿ���ã�
				//�ж��߼��ǣ��������Ȩ�ļ���ID���ļ����ں���ʷ��¼�໥ƥ�䣬˵�����ظ�ʹ�ã������ֹ�û�ʹ�ã������ƥ�䣬�µ���Ȩ�ļ����ڱ��������ʷ��¼ʱ�䣬ȷ����Ȩ�ļ�ʹ�õ������µ��ļ���
				String hisData = dataSync.getHisData();
				if(hisData != null && hisData.trim().length() > 0){
					String[] dd = new String(new Base64().decode(hisData.getBytes()) , "utf-8").split(",");
					if(dd.length >= 2){
						oAuthId = dd[0];
						oAuthDate = dd[1];
					}
					//���id���ļ����ڶ���ͬ����ʾ�ͻ��ظ�ʹ����Ȩ�ļ��������ֹ������
					if(oAuthId != null && oAuthId.equals(authId) && oAuthDate != null && oAuthDate.equals(authDate)){
						servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
						throw new LicenseOpsException("**********��Ȩ�ļ��Ƿ��������ظ�������Ȩ�ļ�***********");
					//�����Ȩ����С����ʷ ��¼���ڣ���ʾ�ͻ�ʹ��֮ǰ����Ȩ�ļ���Ҳ�����ֹ�����뱣֤�û�ʹ�õ���Ȩ�ļ������µģ�����
					}else if(oAuthDate.compareTo(authDate) > 0){
						servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, Status.INVALID);
						throw new LicenseOpsException("**********��Ȩ�ļ��Ƿ�������ʹ���ϵ���Ȩ�ļ�***********");
					}
				
				}else{
					
					//���û����Ȩ��ʷ��¼�������κ����ƣ�����
				}
				
				
					String newhisData = new Base64().encodeAsString((authId + "," + authDate).getBytes("utf-8"));
					dataSync.initLicenseData(ops.getEncryptSHA(),newhisData);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new LicenseOpsException("**********��Ȩģ���޷���������֧��UTF-8����***********", e);
				}
				
			}
			/**
			 * �����߳�
			 * 
			 */
			LicenseThreadExecutor licenseThreadExecutor = new LicenseThreadExecutor();
			String devMode = ops.getDevMode();
			
			/**
			 * ��鵱ǰ��Ȩ�����Ƿ���Կ�������ģʽ�����������������߳�ִ������
			 */
			if(devMode != null && devMode.equals("1")){
				licenseThreadExecutor.setEnableDEV(true);
			}else{
				licenseThreadExecutor.setEnableDEV(false);
			}
			
			if(licenseThreadExecutor.isEnableDEV()){
				String _dev_license_thread_delay = servletContext.getInitParameter("_dev_license_thread_delay");
				String _dev_license_thread_period = servletContext.getInitParameter("_dev_license_thread_period");
				String _dev_license_thread_timeunit = servletContext.getInitParameter("_dev_license_thread_timeunit");
				
				if(_dev_license_thread_delay != null){
					licenseThreadExecutor.setLicenseThreadDelay(Integer.parseInt(_dev_license_thread_delay));
				}
				if(_dev_license_thread_period != null){
					licenseThreadExecutor.setLicenseThreadPeriod(Integer.parseInt(_dev_license_thread_period));
				}
				
				if(_dev_license_thread_timeunit != null && _dev_license_thread_timeunit.equalsIgnoreCase("SECONDS")){
					licenseThreadExecutor.setLicenseThreadTimeUnit(TimeUnit.SECONDS);
				}
				if(_dev_license_thread_timeunit != null && _dev_license_thread_timeunit.equalsIgnoreCase("MINUTES")){
					licenseThreadExecutor.setLicenseThreadTimeUnit(TimeUnit.MINUTES);
				}
				if(_dev_license_thread_timeunit != null && _dev_license_thread_timeunit.equalsIgnoreCase("HOURS")){
					licenseThreadExecutor.setLicenseThreadTimeUnit(TimeUnit.HOURS);
				}
				if(_dev_license_thread_timeunit != null && _dev_license_thread_timeunit.equalsIgnoreCase("DAYS")){
					licenseThreadExecutor.setLicenseThreadTimeUnit(TimeUnit.DAYS);
				}
				
			}
			
			ExecutorService createAndRunLicenseThread = licenseThreadExecutor.createAndRunLicenseThread(dataSync, ops);
			
			servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_INCREASE_EXECUTOR_SERVICE, createAndRunLicenseThread);
			if(log.isDebugEnabled()){
				log.debug("*************************������license�����߳�**********************");
			}
			/**
			 * У���߳�
			 */
			Runnable checkStatusTask = new Runnable() {
				@Override
				public void run() {
					int status = ops.j_checkLicenseStatus(dataSync);
					if(log.isDebugEnabled()){
						log.debug("*************************��ѯlicense״̬:[{}]**********************", status);
					}
					servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_STATUS, status);
				}
			};

			
			String license_check_period = servletContext.getInitParameter("license_check_period");
			Integer license_check_period_int = 60;
			if(license_check_period != null && license_check_period.trim().length() > 0){
				license_check_period_int = Integer.valueOf(license_check_period);
			}
			
			ScheduledExecutorService validationScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
			validationScheduledExecutor.scheduleAtFixedRate(checkStatusTask, 0, license_check_period_int, TimeUnit.MINUTES);
			servletContext.setAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_EXECUTOR_SERVICE, validationScheduledExecutor);
			if(log.isDebugEnabled()){
				log.debug("*************************������license����߳�**********************");
			}
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ExecutorService service = (ExecutorService) sce.getServletContext().getAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_INCREASE_EXECUTOR_SERVICE);
		if(service != null){
			service.shutdown();
		}
		sce.getServletContext().removeAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_INCREASE_EXECUTOR_SERVICE);
		System.out.println("*************************�ѹر�license�����߳�**********************");
		
		
		ScheduledExecutorService validate =  (ScheduledExecutorService) sce.getServletContext().getAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_EXECUTOR_SERVICE);
	
		if(validate != null){
			validate.shutdown();
		}
		sce.getServletContext().removeAttribute(LicenceStatic.SERVLET_CONTEXT_LICENSE_CHECK_EXECUTOR_SERVICE);
		System.out.println("*************************�ѹر�license����߳�**********************");
	}

	
	public LicenseOps getOps() {
		return ops;
	}

	public void setOps(LicenseOps ops) {
		this.ops = ops;
	}

	

}
