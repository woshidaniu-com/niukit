jQuery(function($){
	
	var options = {
			 url: 'listData.zf',
			 uniqueId: "id",
			 toolbar:  '#but_ancd',
			 classes:'table table-condensed',
			 striped: false,
			 columns: [
	            {checkbox: true }, 
	            {field: 'id', title: 'ID',width:'80px', sortable:false,align:'center',visible:false, hidden:true},
	            {field: 'name',title: ' 名 称 ',sortable:false,width:'100px',align:'center'},
	            {field: 'key',title: ' 流 程 标 识 ',align:'center',sortable:false,width:'100px'},
	            {field: 'category', title: ' 类 别 ',sortable:false,width:'80px',align:'center'}, 
	            {field: 'version', title: ' 版 本 ', sortable:false, width:'50px'}, 
	            {field: 'description',title: ' 描 述 ',align:'center',sortable:false,width:'200px'},
	            
	            {field: 'state',title: ' 状 态 ',align:'center',sortable:false,width:'50px',formatter:function(value,row,index){
	            	var ret;
	            	if(value == '1'){
	            		 ret = '<span class="label label-success"> 启  用 </span>';	
	            	}else if(value == '2'){
	            		 ret = '<span class="label label-danger"> 停  用 </span>';
	            	}
	            	return ret;
	            }},
	            {field: 'deploymentCategory', title: '部署类别', sortable:false,align:'center',visible:false,hidden:true},
	            {field: 'deploymentId',title: '流 程 部 署 ID',visible:false,hidden:true}
           ],
           searchParams:function(){
    	    var map = {};
    		map["name"] = jQuery('#name').val();
    		map["category"] = jQuery('#category').val();
    		map["state"] = jQuery('#state').val();
           	return map;
           }
		};
		$('#tabGrid').loadGrid(options);
	
	/* ====================================================绑定按钮事件==================================================== */
	
	// 绑定查看事件
	jQuery("#btn_ck").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		
		var rowData = $('#tabGrid').getRow(ids[0]);
//		if(rowData['deploymentCategory'] == 'simple' ){
//			$.alert('调用简单版本');
//			return;
//		}else{
//			var url = _path + "/diagram-viewer/index.zf?processDefinitionId=" + ids[0];
//			$.showDialog(url,'流程图查看',$.extend({},viewConfig,{"width":1000,data:{}}));
//		}
		
		//var url = "listVersions.zf";
		//$.showDialog(url,'流程版本信息',$.extend({},viewConfig,{"width":600,data:{'processDefinitionKey': rowData['key']}}));
		var url = _path + "/diagram-viewer/index.zf" ;
		$.showDialog(url,'流程图查看  [版本:'+rowData['version']+']',$.extend({},viewConfig,{"width":($(window).width()*0.9),data:{'processDefinitionId':ids[0]}}));
	});

	
	jQuery("#btn_qy").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var serverData = {'processDefinitionId': ids[0]};
		var url = "active.zf";
		$.confirm('您确定启用？',function(result) {
			if(result){
				jQuery.post(url, serverData, function(responseText) {
					if(responseText["status"] == "success"){
						$.success(responseText["message"],function() {
							$('#tabGrid').refreshGrid();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"]);
					}else{
						$.alert(responseText["message"]);
					}
				}, 'json');
			}
		});
	});
	
	jQuery("#btn_ty").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var serverData = {'processDefinitionId': ids[0]};
		var url = "suspend.zf";
		$.confirm('您确定停用？',function(result) {
			if(result){
				jQuery.post(url, serverData, function(responseText) {
					if(responseText["status"] == "success"){
						$.success(responseText["message"],function() {
							$('#tabGrid').refreshGrid();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"]);
					}else{
						$.alert(responseText["message"]);
					}
				}, 'json');
			}
		});
	});
	
	jQuery("#btn_sc").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var serverData = {'processDefinitionId': ids[0]};
		var url = "del.zf";
		$.confirm('您确定删除？',function(result) {
			if(result){
				jQuery.post(url, serverData, function(responseText) {
					if(responseText["status"] == "success"){
						$.success(responseText["message"],function() {
							$('#tabGrid').refreshGrid();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"]);
					}else{
						$.alert(responseText["message"]);
					}
				}, 'json');
			}
		});
	});
	
	jQuery('#btn_assignment').click(function(){
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var rowData = $('#tabGrid').getRow(ids[0]);
		var url = _path + "/processDefinition/assignment/" + ids[0] + "/setup.zf" ;
		$.showDialog(url,'办理人设置  '+rowData['name'] + ':' + rowData['version'],
				$.extend({},modifyConfig,
				{
					"width":($(window).width()*0.9),
					data:{},
					buttons:{
						success : {
							label : "保存",
							className : "btn-primary",
							callback : function() {
								var $this = this;
								var opts = $this["options"]||{};
								if(!$('#ajaxForm').valid()){
									return false;
								}
								var validate = true;
								//校验表单是否合法
								$('#ajaxForm #process_defination_task_tab tbody tr', $('#modifyModal')).each(function(i, row){
									if($(row).find('#candidate_users_container :hidden').size() == 0
											&&
									   $(row).find('#candidate_groups_container :hidden').size() == 0){
										$.alert("任务候选人或候选组至少设置一个！");
										validate = false;
										return validate;
									}
								});
								
								if(!validate){
									return false;
								}
								var processDefinitionId = $('#ajaxForm', $('#modifyModal')).find('#processDefinitionId').val();
								var taskAssignments = [];
								//设置表单元素名称前缀
								$('#ajaxForm #process_defination_task_tab tbody tr', $('#modifyModal')).each(function(i, row){
									var taskAssignment = {};
									var taskDefinitionId = $(row).find('input[name="taskDefinitionId"]').val();
									var taskAssignment_users = [];
									var taskAssignment_groups = [];
									$(row).find('#candidate_users_container input[type="hidden"]').each(function(j, ipt){
										taskAssignment_users.push($(ipt).val());
									});
									$(row).find('#candidate_groups_container input[type="hidden"]').each(function(k, ipt){
										taskAssignment_groups.push($(ipt).val());
									});
									taskAssignment['taskDefinitionId'] = taskDefinitionId;
									taskAssignment['taskCandidateUsers'] = taskAssignment_users;
									taskAssignment['taskCandidateGroups'] = taskAssignment_groups;
									taskAssignments.push(taskAssignment);
								});
								var save_url = _path + "/processDefinition/assignment/" + processDefinitionId + "/save.zf" ;
								
								$.ajax({
							        url: save_url,
							        method: 'post',
							        contentType: 'application/json',
							        data: JSON.stringify(taskAssignments),
							        success: function(responseText) {
							        	if(responseText["status"] == "success"){
											$.success(responseText["message"],function() {
												$('#tabGrid').refreshGrid();
											});
										}else if(responseText["status"] == "fail"){
											$.error(responseText["message"]);
										}else{
											$.alert(responseText["message"]);
										}
							        },
							        error: function(responseText) {
							        	if(responseText["status"] == "success"){
											$.success(responseText["message"],function() {
												$('#tabGrid').refreshGrid();
											});
										}else if(responseText["status"] == "fail"){
											$.error(responseText["message"]);
										}else{
											$.alert(responseText["message"]);
										}
							        }
							    });
								
								return false;
							}
						}
					}
				}
				)
			);
	});
	
});

//查询
function searchResult(){
	$('#tabGrid').refreshGrid();
}

//回车键查询
$('#name').bind("keydown", "return", function (ev) {
	searchResult()   
})
