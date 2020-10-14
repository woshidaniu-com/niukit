<!DOCTYPE html>
<html>
	<head>
		<style>
			.list-group .list-group-item-sm{
				position: relative;
				display: block;
				padding: 5px 10px;
				margin-bottom: -1px;
				background-color: #fff;
				border: 1px solid #ddd;
				font-size:12px
			}
			
			.process-candidates .btn-group{
				width:100%;
				margin-bottom:-1px;
			}
			
			.process-candidates button{
				width:100%;
				border-radius:0;
			}
			
			.process-candidates .dropdown-toggle{
				text-align:left;
				position: relative;
				text-overflow: ellipsis;
			    overflow: hidden;
			    white-space: nowrap;
			    padding-right:15px;
			}
			.process-candidates .dropdown-toggle .caret{
				    position: absolute;
				    top: 10px;
				    right: 10px;
			}
			
			#candidate_groups_container .btn-group:first-child{
				margin-left:-1px;
			}
			
		</style>
	</head>
	<body>
		<div class="lcgl">
		
			<div class="row">
				<div class="col-md-12 col-sm-12">
				<div style="max-height:500px;overflow-y:scroll;">
				<table class="table" id="process_defination_tab">
			      <thead>
			        <tr>
			          <th width="50%"> 流 程 名 称 </th>
			          <th width="20%"> 类 别 </th>
			          <th width="15%"> 版 本 </th>
			          <th width="10%">  </th>
			        </tr>
			      </thead>
			      <tbody>
			      	[#list processDefinitionList as model]
			        <tr>
			          <td>${model.name}</td>
			          <td>${model.category}</td>
			          <td>${model.version}</td>
			          <td>
			          	<button type="button" data-id="${model.id}" name="process_defination_view_btn" class="btn btn-primary btn-sm">查看流程图</button>
			          </td>
			        </tr>
			        [/#list]
			      </tbody>
			    </table>
			    </div>
			    </div>
			</div>
		</div>		
		<script type="text/javascript" src="${base}/js/processManagement/list-versions.js?ver=${messageUtil("niutal.jsVersion")}"></script>
	</body>
</html>
