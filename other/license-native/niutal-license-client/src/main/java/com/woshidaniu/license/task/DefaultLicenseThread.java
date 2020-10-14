/**
 * 
 */
package com.woshidaniu.license.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.license.LicenseOps;
import com.woshidaniu.license.LicenseOpsException;
import com.woshidaniu.license.dataSync.DataSync;


/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵������Ȩ�ļ�У���߳�,���̵߳Ĺ����ǲ�����Ȩ�ļ���ÿ������һ�Σ���ʱ��++
 * <p>
 * @author <a href="#">Kangzhidong[1036]<a>
 * @version 2016��6��12������4:31:40
 */
public final class DefaultLicenseThread implements Runnable{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultLicenseThread.class);
	
	private DataSync dataSync;
	
	private LicenseOps licenseOps;
	
	@Override
	public void run() {
		try {
			if(log.isDebugEnabled()){
				log.debug("*************************ִ��License�����߳�**********************");
			}
			licenseOps.j_inceaseUsgaeCount(dataSync);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
		
	}

	
	public DefaultLicenseThread(DataSync dataSync) throws LicenseOpsException {
		super();
		//dataSync ����ΪNULL
		if(dataSync == null){
			throw new LicenseOpsException("��Ȩ��������ͬ���ӿڱ����ṩʵ����,��Ҫҵ��ϵͳ����ʵ�֣�");
		}
		this.dataSync = dataSync;
	}

	
	

	public DefaultLicenseThread(DataSync dataSync, LicenseOps licenseOps) throws LicenseOpsException {
		this(dataSync);
		this.licenseOps = licenseOps;
	}


	public DataSync getDataSync() {
		return dataSync;
	}


	public void setDataSync(DataSync dataSync) {
		this.dataSync = dataSync;
	}


	public LicenseOps getLicenseOps() {
		return licenseOps;
	}


	public void setLicenseOps(LicenseOps licenseOps) {
		this.licenseOps = licenseOps;
	}
	
}
