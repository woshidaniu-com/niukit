package org.activiti.engine.extend.diagram;

import java.io.InputStream;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

public class CustomProcessDiagramGenerator extends DefaultProcessDiagramGenerator {

	protected String activityFontName;
	protected String labelFontName;
	protected String annotationFontName;

	public String getActivityFontName() {
		return activityFontName;
	}

	public void setActivityFontName(String activityFontName) {
		this.activityFontName = activityFontName;
	}

	public String getLabelFontName() {
		return labelFontName;
	}

	public void setLabelFontName(String labelFontName) {
		this.labelFontName = labelFontName;
	}

	public String getAnnotationFontName() {
		return annotationFontName;
	}

	public void setAnnotationFontName(String annotationFontName) {
		this.annotationFontName = annotationFontName;
	}

	public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities,
			List<String> highLightedFlows) {
		return generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows, getActivityFontName(),
				getLabelFontName(), getAnnotationFontName(), null, 1.0);
	}

	public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities,
			List<String> highLightedFlows, double scaleFactor) {
		return generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows, getActivityFontName(),
				getLabelFontName(), getAnnotationFontName(), null, scaleFactor);
	}
}