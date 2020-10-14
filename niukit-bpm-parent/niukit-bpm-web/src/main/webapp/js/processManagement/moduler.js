jQuery(function($){
	
	var options = {
			 url: 'listData.zf',
			 uniqueId: "id",
			 toolbar:  '#but_ancd',
			 classes:'table table-condensed',
			 striped: false,
			 columns: [
	            {checkbox: true }, 
	            {field: 'id', title: 'ID',width:'80px', sortable:false,align:'center',visible:false},
	            {field: 'name',title: ' 模 型 名 称 ',sortable:false,width:'100px',align:'center'},
	            {field: 'category', title: ' 编 辑 器 类 别 ',sortable:false,width:'80px',align:'center',formatter:function(value,row,index){
	            	var ret;
	            	if(value == 'simple'){
	            		 ret = '简单设计器';	
	            	}else if(value == 'advanced'){
	            		 ret = '高级设计器';
	            	}
	            	return ret;
	            }}, 
	            {field: 'createTime',title: ' 创 建 时 间  ',align:'center',sortable:false,width:'200px', formatter:function(value,row,index){
	            	var d = new Date(value);    //根据时间戳生成的时间对象
	            	var date = (d.getFullYear()) + "-" + 
	            	           (d.getMonth() + 1) + "-" +
	            	           (d.getDate()) + " " + 
	            	           (d.getHours()) + ":" + 
	            	           (d.getMinutes()) + ":" + 
	            	           (d.getSeconds());
	            	return date;
	            }},
	            {field: 'lastUpdateTime',title: ' 最 后 更 新 时 间 ',align:'center',sortable:false,width:'200px',formatter:function(value,row,index){
	            	var d = new Date(value);    //根据时间戳生成的时间对象
	            	var date = (d.getFullYear()) + "-" + 
	            	           (d.getMonth() + 1) + "-" +
	            	           (d.getDate()) + " " + 
	            	           (d.getHours()) + ":" + 
	            	           (d.getMinutes()) + ":" + 
	            	           (d.getSeconds());
	            	return date;
	            }},
	            {field: 'deploymentId',title: ' 部 署 状 态 ', align:'center',sortable:false,width:'80px',formatter:function(value,row,index){
	            	var ret;
	            	if($.founded(value)){
	            		 ret = '已部署';	
	            	}else{
	            		 ret = '未部署';
	            	}
	            	return ret;
	            }}
           ],
           queryParams:function(params){
	        	var queryMap = {};
				queryMap["showCount"] = params["pageSize"];
				queryMap["currentPage"] = params["pageNumber"];
				return queryMap;
	        },
           searchParams:function(){
    	    var map = {};
    		map["searchModelName"] = jQuery('#searchModelName').val();
    		map["searchEditorType"] = jQuery('#searchEditorType').val();
    		map["searchDeploymentState"] = jQuery('#searchDeploymentState').val();
           	return map;
           }
		};
		$('#tabGrid').loadGrid(options);
	
	/* ====================================================绑定按钮事件==================================================== */
	
		
		
		var processModulerConfig = {
				width		: "800px",
				modalName	: "processModulerModal",
				formName	: "ajaxForm",
				gridName	: "tabGrid",
				offAtOnce	: true,
				buttons		: {
					success : {
						label : "保 存",
						className : "btn-primary",
						callback : function() {
							var $this = this;
							submitForm("ajaxForm",function(responseText,statusText){
								$this.reset();
								
								if(responseText["status"] == "success"){
									$('#tabGrid').refreshGrid();
									$.closeModal('processModulerModal');
									var editor = responseText['editor'];
									var modelId = responseText['modelId'];
									var editorUrl = "editModuler.zf?editor=simple&modelId="+modelId;;
									if(editor == 'simple'){
										$.showDialog(editorUrl,'编辑模型[简单]',$.extend({},addConfig,{"width":"900",buttons:{
											success : {
												label : "保存",
												className : "btn-primary",
												callback : function() {
													var $this = this;
													var opts = $this["options"]||{};
													if(!$('#ajaxForm').valid()){
														return false;
													}
													/*var validate = true;
													//校验表单是否合法
													$('#ajaxForm #process_defination_task_tab tbody tr', $('#addModal')).each(function(i, row){
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
													}*/
													
													//设置表单元素名称前缀
													$('#ajaxForm #process_defination_task_tab tbody tr', $('#addModal')).each(function(i, row){
														$(row).find(':text').each(function(j, ipt){
															var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name');
															$(ipt).attr('name', name);
														});
														$(row).find('#candidate_users_container input[type="hidden"]').each(function(j, ipt){
															var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name') + '[' + j + ']';
															$(ipt).attr('name', name);
														});
														$(row).find('#candidate_groups_container input[type="hidden"]').each(function(k, ipt){
															var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name') + '[' + k + ']';
															$(ipt).attr('name', name);
														});
													});
													submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
														$this.reset();
														
														if(responseText["status"] == "success"){
															$.success(responseText["message"],function() {
																$('#tabGrid').refreshGrid();
																$.closeModal(opts.modalName);
															});
														}else if(responseText["status"] == "fail"){
															$.error(responseText["message"]);
														}else{
															$.alert(responseText["message"]);
														}
														
													});
													return false;
												}
											}
										}}));

									}else{
										var editorUrl = "editModuler.zf?editor=advanced&modelId="+modelId;
										$.showDialog(editorUrl,'编辑模型[高级]',$.extend({},viewConfig,{"width":'900'}));
									}

								}else if(responseText["status"] == "fail"){
									$.error(responseText["message"]);
								}else{
									$.alert(responseText["message"]);
								}
								
							});
							return false;
						}
					},
					cancel : {
						label : "关 闭",
						className : "btn-default"
					}
				}
			};	
		
		
	// 绑定增加事件
	jQuery("#btn_zj").click(function () {
		var url = _path + "/processManagement/moduler/addModuler.zf";
		$.showDialog(url,'新增模型',$.extend({},processModulerConfig,{}));
	});	
		
	
	// 绑定查看事件
	jQuery("#btn_ck").click(function () {
		var ids = $("#tabGrid").getKeys();
		if (ids.length != 1) {
			$.alert('请选择您要修改的记录！');
			return false;
		}
		var rowData = $('#tabGrid').getRow(ids[0]);
		if(rowData['category'] == 'simple' ){
			var editorUrl = "viewSimpleModuler.zf?editor=simple&modelId="+ids[0];
			$.showDialog(editorUrl,'查看模型[简版]',$.extend({},viewConfig,{"width":'700'}));

		}else{
			$.alert("暂时不支持高级流程定义查看");
			//var editorUrl = "viewAdvancedModuler.zf?editor=advanced&modelId="+ids[0];
			//$.showDialog(editorUrl,'查看模型[高级]',$.extend({},viewConfig,{"width":($(window).width()*0.9)}));
		}
	});
	
	// 绑定修改事件
	jQuery("#btn_xg").click(function () {
		var ids = $("#tabGrid").getKeys();
		if (ids.length != 1) {
			$.alert('请选择一条您要修改的流程！');
			return false;
		}
		var rowData = $('#tabGrid').getRow(ids[0]);
		if(rowData['category'] == 'simple' ){
			var editorUrl = "editModuler.zf?editor=simple&modelId="+ids[0];
			$.showDialog(editorUrl,'编辑模型[简版]',$.extend({},addConfig,{"width":'900',buttons:{
				success : {
					label : "保存",
					className : "btn-primary",
					callback : function() {
						var $this = this;
						var opts = $this["options"]||{};
						if(!$('#ajaxForm').valid()){
							return false;
						}
						/*var validate = true;
						//校验表单是否合法
						$('#ajaxForm #process_defination_task_tab tbody tr', $('#addModal')).each(function(i, row){
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
						*/
						//设置表单元素名称前缀
						$('#ajaxForm #process_defination_task_tab tbody tr', $('#addModal')).each(function(i, row){
							$(row).find(':text').each(function(j, ipt){
								var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name');
								$(ipt).attr('name', name);
							});
							$(row).find('#candidate_users_container input[type="hidden"]').each(function(j, ipt){
								var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name') + '[' + j + ']';
								$(ipt).attr('name', name);
							});
							$(row).find('#candidate_groups_container input[type="hidden"]').each(function(k, ipt){
								var name = 'taskDefinitions[' + i + '].' + $(ipt).attr('name') + '[' + k + ']';
								$(ipt).attr('name', name);
							});
						});
						submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
							$this.reset();
							
							if(responseText["status"] == "success"){
								$.success(responseText["message"],function() {
									$('#tabGrid').refreshGrid();
									$.closeModal(opts.modalName);
								});
							}else if(responseText["status"] == "fail"){
								$.error(responseText["message"]);
							}else{
								$.alert(responseText["message"]);
							}
							
						});
						return false;
					}
				}
			}}));

		}else{
			var editorUrl = "editModuler.zf?editor=advanced&modelId="+ids[0];
			$.showDialog(editorUrl,'编辑模型[高级]',$.extend({},viewConfig,{"width":'900'}));
		}
	});	
	
	// 绑定删除事件
	jQuery("#btn_sc").click(function() {
		var ids = $("#tabGrid").getKeys();
		if (ids.length == 0) {
			$.alert('请选择您要删除流程模型！');
			return false;
		}
		var canDelete = true;
		$.each(ids, function(i,n){
			var rowData = $('#tabGrid').getRow(ids[0]);
			if($.founded(rowData['deploymentId'])){
				canDelete = false;
				return false;
			}
		});
		if(!canDelete){
			$.alert("所选的流程模型中存在已部署数据，请确认！");
			return false;
		}
		$.confirm('您确定要删除选择的记录吗？',function(result) {
			if(result){
				jQuery.post('delModuler.zf', {
					"modelIds" : ids.join(",")
				}, function(responseText) {
					if(responseText["status"] == "success"){
						$.success(responseText["message"],function() {
							if($("#tabGrid").size() > 0){
								$("#tabGrid").reloadGrid();
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"]);
					} else{
						$.alert(responseText["message"]);
					}
				}, 'json');
			}
		});
	});
	
	// 绑定copy事件
	jQuery("#btn_fz").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var rowData = $('#tabGrid').getRow(ids[0]);
		var url = _path + "/processManagement/moduler/" + ids[0] + "/copyModuler.zf";
		$.showDialog(url,'模型复制',$.extend({},modifyConfig,{"width":600}));

	});
	
	// 绑定dr事件
	jQuery("#btn_dr").click(function () {
		var url = _path + "/processManagement/moduler/uploadModuler.zf";
		$.showDialog(url,'模型导入',$.extend({},modifyConfig,{"width":500}));

	});
	
	
	// 绑定部署事件
	jQuery("#btn_bs").click(function () {
		var ids = $('#tabGrid').getKeys();
		if (ids.length != 1 ) {
			$.alert('请选定一条记录!');
			return;
		}
		var rowData = $('#tabGrid').getRow(ids[0]);
		var serverData = {'id': ids[0]};
		var url = ids[0] + "/deployModulerData.zf";
		$.confirm('您确定部署？',function(result) {
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

	
});

// 查询
function searchResult(){
	$('#tabGrid').refreshGrid();
}

//回车键查询
$('#searchModelName').bind("keydown", "return", function (ev) {
	searchResult()   
})
