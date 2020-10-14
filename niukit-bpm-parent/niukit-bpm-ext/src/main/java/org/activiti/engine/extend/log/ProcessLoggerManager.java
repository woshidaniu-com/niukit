package org.activiti.engine.extend.log;

public interface ProcessLoggerManager {

	void log(ProcessLog processLog);

	void init();

}
