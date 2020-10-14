$(function() {

	var process_defination_tab = $('#process_defination_task_tab');
	initialProcessDefinationTable();
	var processDefinitionId = $('#processDefinitionId').val();
	if ($.founded(processDefinitionId)) {
		var url = _path + "/processDefinition/assignment/" + processDefinitionId + "/getAssignment.zf";
		$.getJSON(url,{},function(data) {
			if ($.founded(data['assignment'])) {
				var assignment = data['assignment'];
				$.each(assignment,function(i, n) {
					var taskRow = process_defination_tab.find('#'+n['taskDefinitionId']);
					var userId = n['userId'];
					var groupId = n['groupId'];
					var userName = n['userName'];
					var groupName = n['groupName'];
					if ($.founded(userId)) {
						taskRow.find('#candidate_users_container').append(
								createProcessDefinationTaskCandidateUserEl(userId,userName));
					}else if($.founded(groupId)){
						taskRow.find('#candidate_groups_container').append(
								createProcessDefinationTaskCandidateGroupEl(groupId,groupName));
					}
				});
			}
		});
	}

	$('#show_process_diagram_link').click(function(){
		if($('#process_diagram_el').attr('data-status') == 'initial'){
			var processDefinitionId = $(this).attr('data-process-definition-id');
			var diagram_url = _path + '/diagram-viewer/index.html?processDefinitionId=' + processDefinitionId + '&processInstanceId=';
			var diagram_iframe = $('<iframe allowtransparency="true" name="process-diagram-view-iframe" width="100%" height="100%" frameborder="0"></iframe');
			diagram_iframe.attr('src', diagram_url);
			$('#process_diagram_el').attr('data-status' , 'loaded');
			$('#process_diagram_el').css('display', 'block');
			$('#process_diagram_el').append(diagram_iframe);
		}else{
			$('#process_diagram_el').toggle();
		}
	});
	
	function createProcessDefinationTaskCandidateUserEl(id, name) {
		var el = $('<div>').addClass('btn-group').prop("title", name);
		var hidden = $('<input type="hidden" name="candidateUsers" value="' + id + '">');
		el.append('<button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown">'
						+ '<i class="fa fa-user" aria-hidden="true"></i>'
						+ name
						+ '['
						+ id
						+ ']<span class="caret"></span></button>');
		var ul = $('<ul class="dropdown-menu" role="menu"></ul>')
		var action = $('<a href="#" id="del-candidate-user"> 删 除 </a>').click(
				function() {
					el.remove();
				});
		$('<li>').append(action).appendTo(ul);
		el.append(hidden).append(ul);
		return el;
	}

	function createProcessDefinationTaskCandidateGroupEl(id, name) {
		var el = $('<div>').addClass('btn-group').prop("title", name);
		var hidden = $('<input type="hidden" name="candidateGroups" value="'+ id + '">');
		el.append('<button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown">'
						+ '<i class="fa fa-users" aria-hidden="true"></i>'
						+ name + '<span class="caret"></span></button>');
		var ul = $('<ul class="dropdown-menu" role="menu"></ul>')
		var action = $('<a href="#" id="del-candidate-group"> 删 除 </a>').click(
				function() {
					el.remove();
				});
		$('<li>').append(action).appendTo(ul);
		el.append(hidden).append(ul);
		return el;
	}

	var processUserChooserConfig = {
		width : 600,
		modalName : "processUserChooserModal",
		formName : "ajaxForm",
		gridName : "processUserChooserGrid",
		offAtOnce : false,
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"] || {};
					var process_users_container = opts['candidate_users_container'];
					var grid = $('#' + opts['gridName']);
					$.each(
						grid.getRows(),
						function(i, g) {
							if (process_users_container
									.find('input[name="candidateUsers"][value="'
											+ g['id']
											+ '|'
											+ g['firstName'] + '"]').length == 0) {
								process_users_container.append(createProcessDefinationTaskCandidateUserEl(
												g['id'],
												g['firstName']));
							}
						});
					this.close();
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

	var processGroupChooserConfig = {
		width : 600,
		modalName : "processGroupChooserModal",
		formName : "ajaxForm",
		gridName : "processGroupChooserGrid",
		offAtOnce : false,
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"] || {};
					var candidate_groups_container = opts['candidate_groups_container'];
					var grid = $('#' + opts['gridName']);
					$.each(
						grid.getRows(),
						function(i, g) {
							if (candidate_groups_container
									.find('input[name="candidateGroups"][value="'
											+ g['id']
											+ '|'
											+ g['name'] + '"]').length == 0) {
								candidate_groups_container
										.append(createProcessDefinationTaskCandidateGroupEl(
												g['id'], g['name']));
							}
						});

					this.close();
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

	/**
	 * 初始化任务定义表格
	 * 
	 * @returns
	 */
	function initialProcessDefinationTable() {
		var tbody = process_defination_tab.find('tbody');
		var rows = tbody.find('tr');
		rows.each(function(index, el) {
					$('#candidate_users_choose_btn', el)
							.click(
									function() {
										$.showDialog(_path+ "/processUser/list.zf",'流程用户',
												$.extend({'candidate_users_container' : $('#candidate_users_container',el)},processUserChooserConfig));
									});

					$('#candidate_groups_choose_btn', el)
							.click(function() {$.showDialog(_path+ "/processGroup/list.zf",'流程用户组',$.extend({'candidate_groups_container' : $('#candidate_groups_container',el)},processGroupChooserConfig));});
				});
	}

});