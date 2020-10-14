<!DOCTYPE html>
<html>
	<head>
		<style>
			.advanced-editor{
				height: 700px;
				width:  100%;
				overflow-y: scroll;
			}
		</style>
	</head>
	<body>
		<div class="advanced-editor">
			<iframe allowtransparency="true" 
				src="${base}/modeler.html?modelId=${modelId}&context=${base}" name="advanced-model-editor-iframe" width="100%" height="98%" frameborder="0">
			
			</iframe>
		</div>
	</body>
</html>
