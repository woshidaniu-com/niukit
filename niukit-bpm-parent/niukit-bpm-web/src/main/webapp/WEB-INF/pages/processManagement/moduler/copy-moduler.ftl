<!DOCTYPE html>
<html>
	<head>
		
	</head>
	<body>
		<div class="lcgl">
		<form id="ajaxForm" method="post" data-toggle="validation" action="${model.id}/copyModulerData.zf" theme="simple" class="form-horizontal sl_all_form">
			<div class="row">
			        <div class="form-group">
			          <label for="" class="col-sm-3 control-label"><span style="color:red;">*</span>模型名称</label>
			          <div class="col-sm-9">
			            	<input type="text" id="name" name="name" maxlength="100" value="${model.name}(拷贝)"
			           		class="form-control" validate="{required:true}" />
			          </div>
			        </div>
			</div>
			<div class="row">
			        <div class="form-group">
			          <label for="" class="col-sm-3 control-label">模型描述</label>
			          <div class="col-sm-9">
			            	<textarea type="text" id="description" name="description" maxlength="200" rows="2" style="height:initial;" class="form-control"></textarea>
			          </div>
			        </div>
			</div>
		</form>
		</div>
	</body>
</html>
