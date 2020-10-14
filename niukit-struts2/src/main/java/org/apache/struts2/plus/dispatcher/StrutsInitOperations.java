package org.apache.struts2.plus.dispatcher;
 
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterConfig;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.HostConfig;
import org.apache.struts2.dispatcher.ng.InitOperations;

/**
 * 
 *@类名称		: StrutsInitOperations.java
 *@类描述		： StrutsInitOperations 类似与一个Delegate 主要负责实例化Dispatcher 再把初始化操作转交给Dispatcher init处理
 *@创建人		： kangzhidong
 *@创建时间	： 2016年4月14日 上午9:59:27
 *@修改人		：
 *@修改时间	：
 *@版本号		:v1.0
 */
public class StrutsInitOperations extends InitOperations{

    public StrutsInitOperations() {
    }

    /**
	 * @param filterConfig
	 */
	public StrutsInitOperations(FilterConfig filterConfig) {
	}

	/**
     * Creates and initializes the dispatcher
     */
    public Dispatcher initDispatcher( HostConfig filterConfig ) {
    	//创建Dispatcher  
    	Dispatcher dispatcher = createDispatcher(filterConfig);
    	//核心方法  Container容器的创建   xml解析在此方法发生  
    	dispatcher.init();
        return dispatcher;
    }
    
    /**
     * Create a {@link Dispatcher}
     */
    protected Dispatcher createDispatcher( HostConfig hostConfig ) {
        Map<String, String> params = new HashMap<String, String>();
        for ( Iterator<String> e = hostConfig.getInitParameterNames(); e.hasNext(); ) {
            String name = (String) e.next();
            String value = hostConfig.getInitParameter(name);
            params.put(name, value);
        }
        parseStrutsConfig(hostConfig, params);
        return new Dispatcher(hostConfig.getServletContext(), params);
    }
  
    /**
	 * 获取所有配置文件的路径
	 */
	private void parseStrutsConfig(HostConfig hostConfig, Map<String, String> m) {
		String home = hostConfig.getServletContext().getRealPath("/");
		home.replace("//", "/");
		if (home.startsWith("/")) {
			home = home + "/";
		}
		// 初始化
		StringBuilder strutsPath = new StringBuilder("struts-default.xml,struts-plugin.xml");
		getStrutsPath(new File(home), strutsPath);
		m.put("config", strutsPath.toString());
	}

	/**
	 * 获取工程下所有的struts配置文件
	 */
	private void getStrutsPath(File file, StringBuilder pathBuilder) {
		// file为空，则不操作
		if (null == file) {
			return;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			// 如果为目录文件，递归调用本方法
			if (f.isDirectory()) {
				getStrutsPath(f, pathBuilder);
				continue;
			}
			// 解析文件名：以struts.xml结尾，则拼接到路径字符串中
			String fName = files[i].getName();
			if (fName.endsWith("struts.xml")) {
				pathBuilder.append(",");
				// 绝对路径
				pathBuilder.append(files[i].getAbsolutePath());
			}
		}
	}

}
