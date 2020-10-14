package org.apache.struts2.plus.config;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.util.location.LocatableProperties;

public class StrutsConfigurationProvider implements ConfigurationProvider {

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(Configuration configuration) throws ConfigurationException {
		
	}

	@Override
	public boolean needsReload() {
		return false;
	}

	@Override
	public void register(ContainerBuilder builder, LocatableProperties props)
			throws ConfigurationException {
		
	}

	@Override
	public void loadPackages() throws ConfigurationException {
	}

}
