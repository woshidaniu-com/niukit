<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<form class="form-inline" role="form" onSubmit="return false;">
		  <div class="form-group">
		    <label for="user_name">姓名</label>
		    <input type="text" class="form-control" id="user_name" placeholder="">
		  </div>
		  
		  <button type="button" class="btn btn-primary btn-sm" onclick="processUserSearchResult();return false;">
			查 询
		  </button>
		</form>
		<!--查询条件  结束  -->
		<!--bootstrap table 开始 -->
		<div class="formbox">
			<table id="processUserChooserGrid"></table>
		</div>
		<!--bootstrap table 结束  -->
		[#include "/globalweb/head/bsTable.ftl" /]
		<script type="text/javascript">
			jQuery(function($){
				var options = {
					 url: _path+'/processUser/listData.zf',
					 uniqueId: "id",
					 classes: "table table-condensed",
					 striped: false,
					 height: 400,
					 columns: [
				            {checkbox: true }, 
				            {field: 'id', title: ' 用户名 ',width:'80px', sortable:false,align:'center'},
				            {field: 'firstName',title: ' 姓名 ',sortable:false,width:'100px',align:'center'}
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
				   		map["search"] = $('#user_name').val();
			          	return map;
			          }
					};
				
					$('#processUserChooserGrid').loadGrid(options);
			});
			// 查询
			function processUserSearchResult(){
				$('#processUserChooserGrid').refreshGrid();
			}
		</script>
	</body>
</html>
