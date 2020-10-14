<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript" src="${messageUtil("system.stylePath")}js/plugins/filestyle/jquery.filestyle-min.js?ver=${messageUtil("niutal.jsVersion")}" charset="utf-8"></script> 
	</head>
	<body>
		<div class="lcgl">
		<form id="ajaxForm" method="post" enctype="multipart/form-data"
				data-toggle="validation" 
				action="uploadModulerData.zf" theme="simple" class="form-horizontal sl_all_form">
			<div class="form-group">
		    	<input type="file" name="file" id="bpm-upload-file" class="filestyle" data-buttonName="btn-primary">
		    	<p class="help-block">文件格式为[.bpmn20.xml]或[.bpmn]</p>
		  </div>
		</form>
		</div>
		<script type="text/javascript">
			$("#bpm-upload-file").filestyle({buttonName: "btn-primary",buttonText: "选择文件"});
		</script>
	</body>
</html>
