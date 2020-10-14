<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	  <div class="panel panel-default">
	    <div class="panel-heading" role="tab" id="headingTwo">
	      <h4 class="panel-title">
	        <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
	          	流程跟踪信息
	        </a>
	      </h4>
	    </div>
	    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
	      <div class="panel-body">
	       		<div style="height:400px;">
				<iframe allowtransparency="true" 
					src="${base}/diagram-viewer/index.html?processDefinitionId=${processDefinitionId}&processInstanceId=${processInstnceId}" name="process-diagram-view-iframe" width="100%" height="100%" frameborder="0"></iframe>
				</div>
	      </div>
	    </div>
	  </div>
	  
	</div>
	</body>
</html>