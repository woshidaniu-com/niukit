<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<form class="form-inline" role="form" onSubmit="return false;">
		  <div class="form-group">
		    <label for="user_name">角色名称</label>
		    <input type="text" class="form-control" id="group_name" placeholder="">
		  </div>
		  
		  <button type="button" class="btn btn-primary btn-sm" onclick="processGroupSearchResult();return false;">
			查 询
		  </button>
		</form>
		<!--查询条件  结束  -->
		<!--JQGrid 开始 -->
		<div class="formbox">
			<table id="processGroupChooserGrid"></table>
		</div>
		<!--JQGrid 结束  -->
		[#include "/globalweb/head/bsTable.ftl" /]
		<script type="text/javascript">
			jQuery(function($){
				var options = {
					 url: _path+'/processGroup/listData.zf',
					 uniqueId: "id",
					 classes:'table table-condensed',
					 striped: false,
					 height: 400,
					 columns: [
				            {checkbox: true }, 
				            {field: 'id', title: '角色ID', visible:false},
				            {field: 'name',title: ' 角色名称 ',sortable:false,width:'100px',align:'center'}
			          ],
			        showToggle:false,
			        showColumns:false,
	        		showRefresh:false,
			        pagination:true,
			        pageSize:15,
			        pageList:[15, 25, 50, 100],
			        queryParams:function(params){
			        	var queryMap = {};
						queryMap["showCount"] = params["pageSize"];
						queryMap["currentPage"] = params["pageNumber"];
						return queryMap;
			        },
			        searchParams:function(){
				   	    var map = {};
				   		map["search"] = $('#group_name').val();
			          	return map;
			          }
					};
				
					$('#processGroupChooserGrid').loadGrid(options);
			});
			// 查询
			function processGroupSearchResult(){
				$('#processGroupChooserGrid').refreshGrid();
			}
		</script>
	</body>
</html>
