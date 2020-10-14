<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<!--按钮 开始 -->
		[#include "/globalweb/comm/buttons.ftl" /]
		<!--查询条件  开始 -->
		<form class="form-horizontal sl_all_form simple-search-form">
				<div class="row">
					<div class="col-lg-3 col-md-3  col-sm-6">
						<div class="form-group">
							<label for="" class="control-label">
								名称
							</label>
							<div class="width-70 inline">
								<input class="form-control" id="name" name="name
									maxlength="20"/>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3  col-sm-6">
						<div class="form-group">
							<label for="" class=" control-label">
								类别
							</label>
							<div class="width-70 inline">
								<select class="form-control input-sm span3 chosen-select" name="category" id="category">
									<option value=""> 全 部 </option>
									[#list categoryList as item]
								 		<option value="${item}"> ${item} </option>
								 	[/#list]
								</select>
								<SCRIPT type="text/javascript">
						    		jQuery('#category').trigger("chosen");
			 			    	</SCRIPT>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3  col-sm-6">
						<div class="form-group">
							<label for="" class="control-label">
								状态
							</label>
							<div class="width-70 inline">
								<select class="form-control input-sm span3 chosen-select" name="state" id="state">
									<option value=""> 全 部 </option>
									<option value="1"> 启 用 </option>
									<option value="2"> 停 用 </option>
								</select>
								<SCRIPT type="text/javascript">
						    		jQuery('#state').trigger("chosen");
			 			    	</SCRIPT>
							</div>
						</div>
					</div>
					
					<div class="col-lg-3 col-md-3 col-sm-6">
					  	<div class="search-btn">
					  		<button type="button" class="btn btn-primary btn-sm" id="search_go"	onclick="searchResult();return false;"> 查 询 </button>
					  	</div>
					</div>
				</div>
			</form>
		<!--查询条件  结束  -->
		<!--JQGrid 开始 -->
		<div class="formbox">
			<table id="tabGrid"></table>
		</div>
		<!--JQGrid 结束  -->
		[#include "/globalweb/head/bsTable.ftl" /]
		<script type="text/javascript" src="${base}/js/processManagement/definition.js?ver=${messageUtil("niutal.jsVersion")}"></script>
	</body>
</html>
