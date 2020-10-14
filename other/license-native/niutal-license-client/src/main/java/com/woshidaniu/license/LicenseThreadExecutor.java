/**
 * 
 */
package com.woshidaniu.license;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.woshidaniu.license.task.DefaultLicenseThread;
import com.woshidaniu.license.dataSync.DataSync;

/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����license�̵߳���
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��7��6������3:27:19
 */
public class LicenseThreadExecutor {
	/**
	 * �״�����ʱ��
	 */
	private int licenseThreadDelay = 1;
	
	/**
	 * ����
	 */
	private int licenseThreadPeriod = 24;
	
	/**
	 * ʱ�䵥λ
	 */
	private TimeUnit licenseThreadTimeUnit = TimeUnit.HOURS;	
	
	/**
	 * �Ƿ���������������
	 */
	private boolean enableDEV;
	
	
	
	public int getLicenseThreadDelay() {
		return licenseThreadDelay;
	}

	public void setLicenseThreadDelay(int licenseThreadDelay) {
		this.licenseThreadDelay = licenseThreadDelay;
	}

	public int getLicenseThreadPeriod() {
		return licenseThreadPeriod;
	}

	public void setLicenseThreadPeriod(int licenseThreadPeriod) {
		this.licenseThreadPeriod = licenseThreadPeriod;
	}

	public TimeUnit getLicenseThreadTimeUnit() {
		return licenseThreadTimeUnit;
	}

	public void setLicenseThreadTimeUnit(TimeUnit licenseThreadTimeUnit) {
		this.licenseThreadTimeUnit = licenseThreadTimeUnit;
	}

	public boolean isEnableDEV() {
		return enableDEV;
	}

	public void setEnableDEV(boolean enableDEV) {
		this.enableDEV = enableDEV;
	}

	public LicenseThreadExecutor() {
		super();
	}

	public LicenseThreadExecutor(int licenseThreadDelay,
			int licenseThreadPeriod, TimeUnit licenseThreadTimeUnit) {
		super();
		this.licenseThreadDelay = licenseThreadDelay;
		this.licenseThreadPeriod = licenseThreadPeriod;
		this.licenseThreadTimeUnit = licenseThreadTimeUnit;
	}

	/**
	 * 
	 * <p>����˵����������������̨У�����<p>
	 */
	public ExecutorService createAndRunLicenseThread(DataSync dataSyncCallback,	LicenseOps ops) throws LicenseOpsException{
		DefaultLicenseThread task = new DefaultLicenseThread(dataSyncCallback,ops);
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(task, licenseThreadDelay, licenseThreadPeriod, licenseThreadTimeUnit);
		return service;
	}
}
