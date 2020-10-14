/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.events;

import org.docx4j.events.EventFinished;
import org.docx4j.events.JobIdentifier;
import org.docx4j.events.PackageIdentifier;
import org.docx4j.events.ProcessStep;
import org.docx4j.events.StartEvent;

/**
 *@类名称	: BuildEndEvent.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Dec 28, 2016 2:15:27 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BuildFinishedEvent extends EventFinished {

	public BuildFinishedEvent(StartEvent started) {
		super(started);
	}
	
	public BuildFinishedEvent(JobIdentifier job) {
		super(job);
	}

	public BuildFinishedEvent(JobIdentifier job, PackageIdentifier pkgIdentifier) {
		super(job, pkgIdentifier);
	}

	public BuildFinishedEvent(JobIdentifier job, PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(job, pkgIdentifier, processStep);
	}

	public BuildFinishedEvent(PackageIdentifier pkgIdentifier) {
		super(pkgIdentifier);
	}

	public BuildFinishedEvent(PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(pkgIdentifier, processStep);
	}
	

}
