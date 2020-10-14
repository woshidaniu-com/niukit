$(function() {

    var process_defination_tab = $('#process_defination_task_tab');
    var process_defination_task_tab_row;
    initialProcessDefinationTable();
    var modelId = $('#modelId').val();
    if ($.founded(modelId)) {
        var url = "getSimpleModelData.zf";
        $.getJSON(url, {
            'modelId': modelId
        },
        function(data) {
            if ($.founded(data['model'])) {
                var model = data['model'];
                if ($.founded(model['name'])) {
                    $('#ajaxForm #name').val(model['name']);
                }
                if ($.founded(model['description'])) {
                    $('#ajaxForm #description').text(model['description']);
                }
                if ($.founded(model['category'])) {
                    $('#ajaxForm #category').val(model['category']);
                }
                // 设置task列表
                var taskDefinitions = model['taskDefinitions'];
                if ($.founded(taskDefinitions)) {
                    $('#process_defination_task_tab tbody tr:first').remove();
                    $.each(taskDefinitions,
                    function(i, n) {
                        var taskRow = createProcessTaskDefinationRow(process_defination_task_tab_row.clone());
                        taskRow.find("input[name='taskName']").val(n['taskName']);
                        taskRow.find("input[name='taskDesc']").val(n['taskDesc']);
                        var candidateUsers = n['candidateUsers'];
                        var candidateGroups = n['candidateGroups'];
                        if ($.founded(candidateUsers)) {
                            $.each(candidateUsers,
                            function(j, m) {
                                taskRow.find('#candidate_users_container').append(createProcessDefinationTaskCandidateUserEl(m['userId'], m['userName']));
                            });
                        }
                        if ($.founded(candidateGroups)) {
                            $.each(candidateGroups,
                            function(j, m) {
                                taskRow.find('#candidate_groups_container').append(createProcessDefinationTaskCandidateGroupEl(m['groupId'], m['groupName']));
                            });
                        }
                        $('#process_defination_task_tab tbody').append(taskRow);
                    });

                }
            }
        });
    }

    $('#category').typeahead({
        highlight: false,
        minLength: 0,
        delay: 0,
        source: function(query, process) {
            $.post("queryProcessDefinitionCategoryData.zf", {},
            function(respData) {
                return process(respData);
            });
        },
        formatItem: function(item) {
            return item;
        },
        setValue: function(item) {
            return {
                'data-value': item,
                'real-value': item
            };
        }

    });

    function createProcessDefinationTaskCandidateUserEl(id, name) {
        var el = $('<div>').addClass('btn-group').prop("title", name);
        var hidden = $('<input type="hidden" name="candidateUsers" value="' + id + '|' + name + '">');
        el.append('<button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" style="border-left:0px;border-right:0px;">' + '<i class="fa fa-user" aria-hidden="true" style="padding-right:5px;"></i>' + name + '[' + id + ']<span class="glyphicon glyphicon-remove"></span></button>');
        var ul = $('<ul class="dropdown-menu" role="menu"></ul>');
        var action = $('<a href="#" id="del-candidate-user"> 删 除 </a>').click(function() {
            el.remove();
        });
        $('<li>').append(action).appendTo(ul);
        el.append(hidden).append(ul);
        return el;
    }

    function createProcessDefinationTaskCandidateGroupEl(id, name) {
        var el = $('<div>').addClass('btn-group').prop("title", name);
        var hidden = $('<input type="hidden" name="candidateGroups" value="' + id + '|' + name + '">');
        el.append('<button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown">' + '<i class="fa fa-users" aria-hidden="true"></i>' + name + '<span class="caret"></span></button>');
        var ul = $('<ul class="dropdown-menu" role="menu"></ul>');
        var action = $('<a href="#" id="del-candidate-group"> 删 除 </a>').click(function() {
            el.remove();
        });
        $('<li>').append(action).appendTo(ul);
        el.append(hidden).append(ul);
        return el;
    }

    var processUserChooserConfig = {
        width: 600,
        modalName: "processUserChooserModal",
        formName: "ajaxForm",
        gridName: "processUserChooserGrid",
        offAtOnce: false,
        buttons: {
            success: {
                label: "确 定",
                className: "btn-primary",
                callback: function() {
                    var $this = this;
                    var opts = $this["options"] || {};
                    var process_users_container = opts['candidate_users_container'];
                    var grid = $('#' + opts['gridName']);
                    $.each(grid.getRows(),
                    function(i, g) {
                        if (process_users_container.find('input[name="candidateUsers"][value="' + g['id'] + '|' + g['firstName'] + '"]').length == 0) {
                            process_users_container.append(createProcessDefinationTaskCandidateUserEl(g['id'], g['firstName']));
                        }
                    });

                    this.close();
                    return false;
                }
            }
        }
    };

    var processGroupChooserConfig = {
        width: 600,
        modalName: "processGroupChooserModal",
        formName: "ajaxForm",
        gridName: "processGroupChooserGrid",
        offAtOnce: false,
        buttons: {
            success: {
                label: "确 定",
                className: "btn-primary",
                callback: function() {
                    var $this = this;
                    var opts = $this["options"] || {};
                    var candidate_groups_container = opts['candidate_groups_container'];
                    var grid = $('#' + opts['gridName']);
                    $.each(grid.getRows(),
                    function(i, g) {
                        if (candidate_groups_container.find('input[name="candidateGroups"][value="' + g['id'] + '|' + g['name'] + '"]').length == 0) {
                            candidate_groups_container.append(createProcessDefinationTaskCandidateGroupEl(g['id'], g['name']));
                        }
                    });

                    this.close();
                    return false;
                }
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
            $('#add_task_btn', el).click(function() {
                $(el).after(createProcessTaskDefinationRow(process_defination_task_tab_row.clone()));
            });

            $('#del_task_btn', el).click(function() {
                if (tbody.find('tr').length > 1) {
                    $(el).remove();
                }
            });

            $('#candidate_users_choose_btn', el).click(function() {
                $.showDialog(_path + "/processUser/list.zf", '流程用户', $.extend({
                    'candidate_users_container': $('.candidate_users_container', el)
                },
                processUserChooserConfig));
            });

            $('#candidate_groups_choose_btn', el).click(function() {
                $.showDialog(_path + "/processGroup/list.zf", '流程用户组', $.extend({
                    'candidate_groups_container': $('#candidate_groups_container', el)
                },
                processGroupChooserConfig));
            });
        });
        process_defination_task_tab_row = $(rows[0]).clone();
    }

    function createProcessTaskDefinationRow(row) {
        var tbody = process_defination_tab.find('tbody');
        if (row) {
            $('#add_task_btn', row).click(function() {
                row.after(createProcessTaskDefinationRow(process_defination_task_tab_row.clone()));
            });

            $('#del_task_btn', row).click(function() {
                if (tbody.find('tr').length > 1) {
                    row.remove();
                }
            });

            $('#candidate_users_choose_btn', row).click(function() {
                $.showDialog(_path + "/processUser/list.zf", '流程用户', $.extend({
                    'candidate_users_container': $('#candidate_users_container', row)
                },
                processUserChooserConfig));
            });

            $('#candidate_groups_choose_btn', row).click(function() {
                $.showDialog(_path + "/processGroup/list.zf", '流程用户组', $.extend({
                    'candidate_groups_container': $('#candidate_groups_container', row)
                },
                processGroupChooserConfig));
            });
            return row;
        }
        return null;
    }

});