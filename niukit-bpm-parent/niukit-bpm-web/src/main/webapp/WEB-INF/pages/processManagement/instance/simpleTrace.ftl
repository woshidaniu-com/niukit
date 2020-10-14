<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	
	<div class="panel panel-default">
	    <div class="panel-heading" role="tab" id="headingOne">
	      <h4 class="panel-title">
	        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
	          	图形
	        </a>
	      </h4>
	    </div>
	    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
	      <div class="panel-body">
				<div style="height:400px;">
					<iframe allowtransparency="true" 
						src="${base}/diagram-viewer/index.html?processDefinitionId=${processDefinitionId}&processInstanceId=${processInstnceId}" name="process-diagram-view-iframe" width="100%" height="100%" frameborder="0"></iframe>
				</div>
	      </div>
	    </div>
	  </div>
	
	  <div class="panel panel-default">
	    <div class="panel-heading" role="tab" id="headingTwo">
	      <h4 class="panel-title">
	        <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
	          	表格
	        </a>
	      </h4>
	    </div>
	    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
	      <div class="panel-body">
	       <table id="processInfoList" class="table">
				<thead>
					<tr>
						<th>#</th>
						<th> 环 节 名 称 </th>
						<th> 参 与 人 </th>
						<th> 开 始 时 间 </th>
						<th> 结 束 时 间 </th>
						<th> 审 核 信 息 </th>
						<th> 详 细 信 息 </th>
					</tr>
				</thead>
				<tbody>
					[#list traceProcess as item]
				 		<tr>
				 			<td>${item_index+1}</td>
				 			<td>${item['P_ACT_NAME']}</td>
				 			<td>${item.P_USER_ID}</td>
				 			<td>${item.P_START_TIME}</td>
				 			<td>${item.P_END_TIME}</td>
				 			<td><strong>${item.P_TASK_COMMENT}</strong></td>
				 			<td>${item.P_TASK_FULL_COMMENT}</td>
				 		</tr>
				 	[/#list]
				</tbody>
			</table>
	      </div>
	    </div>
	  </div>
	  
	</div>
	</body>
</html>
