<!DOCTYPE html>
<html>
	<head>
		
	</head>
	<body>
		<div class="lcgl">
		<form id="ajaxForm" method="post" data-toggle="validation" action="addModulerData.zf" theme="simple" class="form-horizontal sl_all_form">
			<div class="row">
			        <div class="form-group">
			          <label for="" class="col-sm-3 control-label"><span style="color:red;">*</span>模型名称</label>
			          <div class="col-sm-9">
			            	<input type="text" id="name" name="name" maxlength="100"
			           		class="form-control" validate="{required:true}" placeholder="请输入模型名称"/>
			          </div>
			        </div>
			</div>
			<div class="row">
			        <div class="form-group">
			          <label for="" class="col-sm-3 control-label">模型描述</label>
			          <div class="col-sm-9">
			            	<textarea type="text" id="description" name="description" placeholder="请输入模型描述" maxlength="200" rows="2" style="height:initial;" class="form-control"/>
			          </div>
			        </div>
			</div>
			<div class="row">
			        <div class="form-group">
			          <label for="" class="col-sm-3 control-label">编辑器</label>
			          <div class="col-sm-9">
			            	<div class="list-group">
			            	  <input type="hidden" name="editor" id="editor" value="simple"> 
			            	  <a href="#" class="list-group-item active" name="editor-link" data="simple">
							    <h4 class="list-group-item-heading">简单设计器</h4>
							    <p class="list-group-item-text">一个直观的、基于表格样式的编辑器，使用它可以快速开发简单流程。</p>
							  </a>
							  <a href="#" class="list-group-item" name="editor-link" data="advanced">
							    <h4 class="list-group-item-heading">高级设计器</h4>
							    <p class="list-group-item-text">BPMN2.0图形化建模环境，选择它可以用拖拽的方式使用组件。</p>
							  </a>
							</div>
			          </div>
			        </div>
			</div>
		</form>
		</div>
		<SCRIPT type="text/javascript">
			$('a[name="editor-link"]').click(function(){
				$('#ajaxForm #editor').val($(this).attr("data"));
				$('a[name="editor-link"]').removeClass('active');
				$(this).addClass('active');
			});
		</SCRIPT>
	</body>
</html>
