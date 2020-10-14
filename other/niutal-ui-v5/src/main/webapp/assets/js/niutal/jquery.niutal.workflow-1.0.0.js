;(function($) {

	//判断审批业务是否配置过审批流程
	$.hasWorkFlow = function(workID){
		//true为已配置过 false为未配置过
		var flag = false;
		jQuery.ajaxSetup({async:false});
		jQuery.post( _path + '/query/query_cxSpYwdmExist.html',{"ywdm":workID},function(data){
			flag = data;
		},'json');
		jQuery.ajaxSetup({async:true});
		return flag;
	};
	
	//选择审批流程
	$.chooseWorkFlow = function(workID,callbackFunc){
		$.showDialog(_path + '/sp/spSetting_cxSelectBusiness.html',$.i18n.niutal["workFlow"]["chooseWorkFlow"],{
			width		: "900px",
			modalName	: "splModal",
			data 		: {"ywdm":workID},
			buttons:{
				success : {	
					label : $.i18n.bootbox["buttons"]["success"],
					className : "btn-primary",
					callback : function() {
						if($.isFunction(callbackFunc)){
							return callbackFunc.call(this) || false;
						}else{
							return true;
						}
					}
				},
				cancel : {
					label : $.i18n.bootbox["buttons"]["cancel"],
					className : "btn-primary"
				}
			}
		});
	};
	
	//查看审核流程进度信息
	$.viewWorkFlow = function(rowid){
		$.showDialog( _path + '/sp/spSetting_cxLcgz.html?id=' + rowid ,$.i18n.niutal["workFlow"]["viewWorkFlow"],$.extend(true,{},viewConfig,{
			width:'700px'
		}));
	};
	
})(jQuery);