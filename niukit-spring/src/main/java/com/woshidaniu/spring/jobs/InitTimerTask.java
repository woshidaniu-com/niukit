package com.woshidaniu.spring.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * 初始化定时任务
 * @author Penghui.Qu
 *
 */
public class InitTimerTask implements InitializingBean{

	private TimerServer timerServer;
	private List<Task> taskList;
	private static Logger log = LoggerFactory.getLogger(InitTimerTask.class);
	
	
	public void setTimerServer(TimerServer timerServer) {
		this.timerServer = timerServer;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("准备加载定时任务了...");
		
		for (Task task : taskList){
			try {
				timerServer.registerTask(task);
			} catch (Exception e) {
				log.error("启动创建定时任务失败....");
			}
		}
	}
}
