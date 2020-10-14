package com.woshidaniu.spring.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

import com.woshidaniu.basicutils.uid.Sequence;

public class EnhancedSequenceFactory extends EnhancedBeanFactory implements FactoryBean<Sequence> {

	protected long workerId = -1;

	/* 数据标识id部分 */
	protected long datacenterId = -1;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public Sequence getObject() throws Exception {
		if (workerId > 0 && datacenterId > 0 ) {
			return new Sequence(workerId , datacenterId);
		}
		return new Sequence();
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}
	

}
