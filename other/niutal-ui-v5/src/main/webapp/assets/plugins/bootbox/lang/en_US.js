;(function($){

	/** Map holding bundle keys (if mode: 'bootbox') */
	$.i18n = $.i18n || {};
	$.i18n.bootbox = $.i18n.bootbox || {};
	
	$.extend($.i18n.bootbox,{
		//按钮名称
		"buttons" 		: {
			"success"	: "OK",
			"cancel"	: "Close",
			"export"	: "Export",
			"upload"	: "Upload",
			"choice"	: "Choice"
		},
		//弹窗标题
		"titles" 		: {
			"success"		: "Success Prompt",
			"alert"			: "Alert Prompt",
			"error"			: "Error Prompt",
			"confirm" 		: "Confirm Prompt",
			"prompt"		: "Message Prompt",
			"export"		: "Custom Export",
			"report"		: "Report Preview",
			"batchModify"	: "Batch Modify Data",
			"fullAvatar"	: "Full Avatar Upload",
			"showSort"		: "Sort Priority Setting"
		},
		//提示信息
		"messages" 		: {
			"open_tip"	: "Functional Opening Time Prompt"
		}
	});
	
	/**
	 * 设置bootbox内置的国际化方言
	 */
	bootbox.setDefaults({
		locale : "en"
	});
	
	/**
	 * 覆盖查看功能默认初始化参数 
	 */
	$.extend(true,viewConfig||{},{
		buttons		: {
			cancel : {
				label : $.i18n.bootbox["buttons"]["cancel"]
			}
		}
	});
	
	/**
	 * 覆盖新增功能默认初始化参数 
	 */
	$.extend(true,addConfig||{},{
		buttons		: {
			success : {
				label : $.i18n.bootbox["buttons"]["success"]
			},
			cancel : {
				label : $.i18n.bootbox["buttons"]["cancel"]
			}
		}
	});
	
	/**
	 * 覆盖修改功能默认初始化参数 
	 */
	$.extend(true,modifyConfig||{},{
		buttons		: {
			success : {
				label : $.i18n.bootbox["buttons"]["success"]
			},
			cancel : {
				label : $.i18n.bootbox["buttons"]["cancel"]
			}
		}
	});

}(jQuery));