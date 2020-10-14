/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.events;

import org.docx4j.events.JobIdentifier;
import org.docx4j.events.PackageIdentifier;
import org.docx4j.events.ProcessStep;
import org.docx4j.events.StartEvent;

/**
 *@类名称	: BuildStartEvent.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Dec 28, 2016 2:14:10 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BuildStartEvent extends StartEvent {

	public BuildStartEvent(JobIdentifier job) {
		super(job);
	}

	public BuildStartEvent(JobIdentifier job, PackageIdentifier pkgIdentifier) {
		super(job, pkgIdentifier);
	}

	public BuildStartEvent(JobIdentifier job, PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(job, pkgIdentifier, processStep);
	}

	public BuildStartEvent(PackageIdentifier pkgIdentifier) {
		super(pkgIdentifier);
	}

	public BuildStartEvent(PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(pkgIdentifier, processStep);
	}

}
