/**
 * @discretion	: adapter for the jQuery bootstrap modal plugin.
 * @author    	: wandalong 
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($) {
	
	$.extend({
		message:function(title,content,callbackFunc,options){
			callbackFunc = callbackFunc || $.noop;
			options = options ||{};
			$.dialog($.extend(true,{
				"title"		: 	title||'提示信息',
				"message"	: 	content,
				"width"		:	options["width"]||"800px",
				"modalName"	:	options["modalName"],
				//是否使用模拟滚动条
			    customScrollbar : false,
				"buttons"	: {
					cancel : {
						label : "确 定",
						className : "btn-default",
						callback : function() {
							if($.isFunction(callbackFunc)){
								return callbackFunc.call(this);
							}else{
								return true;
							}
						}
					}
				}
			},options||{}));
		},
		/*弹出一个含有“确定”按钮的窗口，这个时候script会暂停运行，直到点击“确定”按钮*/
		alert:function(content,callbackFunc,options){
			//兼容未开放时间的提示
			if(content && content.indexOf("timeSettingInfo") > 10 ){
				options = $.extend(true,{},{"width":"700px","modalName":"timeSettingModal"},options||{});
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				$.message('功能时间开放提示',content,callbackFunc,options);
			}else{
				var length = $.getTextLength(content||"");
				var beishu = Math.ceil(length%50/50) + parseInt(length/50);
				var width = 300 + (beishu > 0 ? (beishu-1)* 30 : 0);
				
				options = $.extend(true,{},{
					width			: width+"px",
					modalName		: "alertModal",
					//是否使用模拟滚动条
				    customScrollbar : false,
				},options||{});
				
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				
				bootbox.alert("提示信息",'<div class="alert alert-modal"><p>'+content+'</p></div>', function(result) {
					if($.isFunction(callbackFunc)){
						return callbackFunc.call(this,result);
					}else{
						return true;
					}
				},options );
			}
		},
		/*弹出一个含有“确定”按钮的窗口，这个时候script会暂停运行，直到点击“确定”按钮*/
		error:function(content,callbackFunc,options){
			//兼容未开放时间的提示
			if(content && content.indexOf("timeSettingInfo") > 10 ){
				options = $.extend(true,{},{"width":"700px","modalName":"timeSettingModal"},options||{});
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				$.message('功能时间开放提示',content,callbackFunc,options);
			}else{
				var length = $.getTextLength(content||"")
				var beishu = Math.ceil(length%50/50) + parseInt(length/50);
				var width = 300 + (beishu > 0 ? (beishu-1)*30 : 0);

				options = $.extend(true,{},{
					width			: width+"px",
					modalName		: "errorModal",
					//是否使用模拟滚动条
				    customScrollbar : false
				},options||{});
				
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				bootbox.alert("错误提示",'<div class="error error-modal"><p>'+content+'</p></div>', function(result) {
					if($.isFunction(callbackFunc)){
						return callbackFunc.call(this,result);
					}else{
						return true;
					}
				},options);
			}
		},
		/*弹出一个含有“确定”按钮的窗口，这个时候script会暂停运行，直到点击“确定”按钮*/
		success:function(content,callbackFunc,options){
			//兼容未开放时间的提示
			if(content && content.indexOf("timeSettingInfo") > 10 ){
				options = $.extend(true,{},{"width":"700px","modalName":"timeSettingModal"},options||{});
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				$.message('功能时间开放提示',content,callbackFunc,options);
			}else{
				var length = $.getTextLength(content||"")
				var beishu = Math.ceil(length%50/50) + parseInt(length/50);
				var width = 300 + (beishu > 0 ? (beishu-1)*30 : 0);
				
				options = $.extend(true,{},{
					width			: width+"px",
					modalName		: "successModal",
					//是否使用模拟滚动条
				    customScrollbar : false,
				},options||{});
				
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				bootbox.alert("成功提示",'<div class="success success-modal"><p>'+content+'</p></div>', function(result) {
					if($.isFunction(callbackFunc)){
						return callbackFunc.call(this,result);
					}else{
						return true;
					}
				},options);
			}
		},
		/*弹出含有“确定”和“取消”的窗口，点击“确定”返回true,点击“取消”返回false*/
		confirm:function(content,callbackFunc,options){
			//兼容未开放时间的提示
			if(content && content.indexOf("timeSettingInfo") > 10 ){
				options = $.extend(true,{},{"width":"700px","modalName":"timeSettingModal"},options||{});
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				$.message('功能时间开放提示',content,callbackFunc,options);
			}else{
				var length = $.getTextLength(content||"");
				var beishu = Math.ceil(length%50/50) + parseInt(length/50);
				var width = 300 + (beishu > 0 ? (beishu-1)*30 : 0);
				
				options = $.extend(true,{},{
					width			: width+"px",
					modalName		: "confirmModal",
					//是否使用模拟滚动条
				    customScrollbar : false,
				},options||{});
				
				if($("#"+options["modalName"]).size() > 0){
					return;
				}
				bootbox.confirm("警告窗口",'<div class="alert confirm-modal"><p>'+content+'</p></div>', function(result) {
					if($.isFunction(callbackFunc)){
						return callbackFunc.call(this,result);
					}else{
						return true;
					}
				},options); 
			}
		},
		/*返回带有输入框以及“确定”和“取消”的按钮的一个窗口。*/
		prompt:function(callbackFunc,options){
			
			options = $.extend(true,{},{
				width			: "300px",
				modalName		: "promptModal",
				//是否使用模拟滚动条
			    customScrollbar : false,
			}, options||{});

			if($("#"+options["modalName"]).size() > 0){
				return;
			}
			
			bootbox.prompt("提示窗口",function(result) {
				if($.isFunction(callbackFunc)){
					return callbackFunc.call(this,result);
				}else{
					return true;
				}
			},options); 
		},
		showDialog:function(href,title,options){
			options = options||{};
			$.dialog($.extend({},options||{},{
				"title": title,
				"href": href||"",
				"data": options.data||{}
			}));
		},
		dialog:function(options){
			var defaults = {
				title 		: "提示窗口",
				/*path 默认值：false	data-remote	使用 jQuery.load 方法，为模态框的主体注入内容。如果添加了一个带有有效 URL 的 href，则会加载其中的内容。*/
				href		: "",
				/*远程请求URL时的post数据*/
				data		: {},
				/*普通的文字内容或者html*/
				message		: "", 
				 // default language
			    locale		: "zh_CN",
			    /*boolean 或 string 'static' 默认值：true	data-backdrop	指定一个静态的背景，当用户点击模态框外部时不会关闭模态框。*/
				backdrop	: "static",
				/*boolean 默认值：true	data-keyboard	当按下 escape 键时关闭模态框，设置为 false 时则按键无效。*/
				keyboard	: true,
				//当数据提交成功，立刻关闭窗口
			    offAtOnce	: true,
			    //是否使用模拟滚动条
			    customScrollbar : true,
			    //是否允许窗口拖拽
			    draggable : true,
				/*页面加载完后的回调*/
				loaded		: function(){
					/*触发验证事件：实现自动绑定验证*/
					$('[data-toggle*="validation"]').trigger("validation");
					$('[data-toggle*="fixed"]').trigger("fixed");
					if($.fn.tooltip){
						$('[data-toggle*="tooltip"]').tooltip({container:'body'});
					}
				},
				onClose		: null,
				onEscape	: null,
				onHidden	: null
			}
			var dialogOptions = $.extend({},defaults,options);
			//处理路径参数问题--data:{'ckUrl':ckUrl,'shUrl':shUrl},
			if($.founded(dialogOptions["data"]["ckUrl"])){
				dialogOptions["data"]["ckUrl"] = encodeURIComponent(dialogOptions["data"]["ckUrl"]||"");
			}
			if($.founded(dialogOptions["data"]["shUrl"])){
				dialogOptions["data"]["shUrl"] = encodeURIComponent(dialogOptions["data"]["shUrl"]||"");
			}
			bootbox.dialog(dialogOptions);
			/*远程加载
			if($.founded(dialogOptions.remote)) {
				jQuery.ajaxSetup({ async: false});
				jQuery.get(dialogOptions.remote, dialogOptions.data||{}, function(html){
					$.extend(dialogOptions,{
						message : html
					});
					bootbox.dialog(dialogOptions);
				});
				jQuery.ajaxSetup({ async: true});
			}else if($.founded(dialogOptions.message)) {
				bootbox.dialog(dialogOptions);
			}*/
		},
		/*关闭指定名称的modal窗口*/
		closeModal:function(name){
			bootbox.hideModal(name);
		},
		/*关闭所有的modal窗口*/
		closeAllModal:function(){
			bootbox.hideAll();
		}
	});
	
	//重新加载一个弹窗中的内容
	$.fn.reloadDialog = function(){
		return this.each(function() {
			var dialog = $(this), options = $(dialog).data("options");
			if (!options) {
				return;
			}
			if($(dialog).hasClass("bootbox")){
				bootbox.loadBody(dialog,options);
			}
		});
	}
	
	
	//数据导出弹窗
	$.exportDialog = function(href,dcclbh,requestMap,colModel,gridId,callback){
		if(!$.founded(dcclbh)){
			throw new Error("dcclbh 不能为空 !");
		}
		//初始化导出配置
		var initURL = _path + '/niutal/drdc/export_exportInitDcpz.html';
		var initRequestMap = {"dcclbh":dcclbh};
		//组装表头
		if(colModel && colModel.length > 0){
			var index = 0;
			for(var i=0;i<colModel.length;i++){
				var col = colModel[i];
				if($.trim(col.label).length>0&&$.trim(col.index).length>0&&col.hidden!=true){
					initRequestMap["colConfig["+index+"].zd"] = col.name;
					initRequestMap["colConfig["+index+"].zdmc"] = col.label;
					initRequestMap["colConfig["+index+"].xssx"] = index;
					index ++;
				}
			}
		}
		jQuery.ajaxSetup({async:false});
		$.post(initURL,initRequestMap);
		jQuery.ajaxSetup({async:true});
		
		//弹出初始化处理后的字段选择界面
		requestMap = requestMap||{};
		requestMap["dcclbh"] = dcclbh;
		
		if($.founded(gridId) && $(gridId).size() > 0 ){
			//排序字段
			var sortName = $(gridId).jqGrid("getGridParam","sortname");
			var sortOrder = $(gridId).jqGrid("getGridParam","sortorder");
			if($.defined(sortName)){
				requestMap["queryModel.sortName"] = sortName;
			}
			if($.defined(sortOrder)){
				requestMap["queryModel.sortOrder"] = sortOrder;
			}
		}
		//构建form
		var form = $.buildForm("drdcForm",href,requestMap);
		$.dialog({
			"title"		: '自定义导出',
			"href"		: _path + '/niutal/drdc/export_exportConfig.html',
			"data"		: {"dcclbh":dcclbh},
			"width"		: "900px",
			"modalName"	: "ExportModal",
			/*"afterRender":function(){
				if($(this.document).find("#exportForm").size() == 0){
					this.button({
	                    id		:'bcdc',
	                    disabled: true
	                });
				}
			},*/
			"buttons"	: {
				bcdc : {
					label : "导出",
					className : "btn-primary",
					callback : function() {
						var closeable = this.content.saveConfig(form,callback);
						if($.defined(closeable)){
							return closeable;
						}else{
							return true;
						}
					}
				},
				cancel : {
					label : "取 消",
					className : "btn-default"
				}
			}
		});
	};
	
	/**
	 * 字段排序弹窗
	 */
	$.showSortDialog = function(ywsj_id,gnmkdm,callbackFunc){
		if(!$.founded(ywsj_id)){
			throw new Error("业务数据ID不能为空 !");
		}
		if(!$.founded(gnmkdm)){
			throw new Error("功能模块代码不能为空 !");
		}
		callbackFunc = ($.defined(callbackFunc)&&jQuery.isFunction(callbackFunc))?callbackFunc:$.noop;
		var ywsjSortMap = {
			"ywsj_id"	: ywsj_id,
			"gnmkdm"	: gnmkdm
		};
		$.dialog({
			"title"		: '设置排序优先级',
			"href"		: _path + '/xtgl/ywsjPxxx_cxYwsjPxxx.html',
			"data"		: ywsjSortMap,
			"width"		: "500px",
			"modalName"	: "sortModal",
			"buttons"	: {
				success : {
					label : "确 定",
					className : "btn-primary",
					callback : function() {
						var sortArr = [];
						$("#sort_table_body").find("tr.sort-item").each(function(i,tr){
							var orderStr = $(tr).data("order");
							//组织排序回调参数
							sortArr.push({
								"yxj"	: (i+1),
								"pxfs"	:  ($.founded(orderStr) ? orderStr : "asc"),
								"zdmc"	: $(tr).data("sort")
							});
							//组织排序更新数据
							ywsjSortMap["list["+i+"].yxj"] = (i+1);
					        ywsjSortMap["list["+i+"].pxfs"] = sortArr[i]["pxfs"];
			                ywsjSortMap["list["+i+"].zdmc"] = sortArr[i]["zdmc"];
						});
						//更新排序信息，并调用回调函数
						$.ajaxSetup({async:false});
						$.post(_path + '/xtgl/ywsjPxxx_cxYwsjPxxxForUpdate.html', ywsjSortMap , function(data){
							//关闭窗口
							$.closeModal("sortModal");
							//调用回调函数
							callbackFunc.call(this,sortArr);
						});
						$.ajaxSetup({async:true});
						return false;
					}
				},
				cancel : {
					label : "取 消",
					className : "btn-default"
				}
			}
		});
	};
	 
	//富头像上传弹窗
	$.fullAvatarDialog = function(title,_options,callback){
		_options["upload_url"] = encodeURIComponent(_options["upload_url"]||"");
		_options["src_url"] = encodeURIComponent(_options["src_url"]||"");
		$.dialog({
			"title"		: title||'富头像上传',
		    "draggable"	: false,//窗口是否可拖拽
			"href"		: _path + '/editor/fullAvatar_cxFullAvatarUpload.html',
			"data"		: _options||{},
			"width"		: "700px",
			"modalName"	: "FullAvatarUpload",
			"onHidden"	: function(){
				if($.isFunction(callback)){
					callback.call(this);
			   	}
			},
			"buttons"	: {
				success : {
					label : "上 传",
					className : "btn-primary",
					callback : function() {
						//点击上传按钮的事件
						if(swf_object){
							swf_object.call('upload');
						}
				        //$.closeModal("xszpModal");
						return false;
					}
				},
				reelect : {
					label : "选 择",
					className : "btn-primary",
					callback : function() {
						//点击重选按钮的事件
						if(swf_object){
							swf_object.call('changepanel', "upload");
						}
						return false;
					}
				},
				cancel : {
					label : "关 闭",
					className : "btn-default",
					callback : function() {
						
					}
				}
			}
		});
	};
	
	//富头像上传弹窗
	$.fullAvatarURLDialog = function(paramURL,title,requestMap,_options,callback){
		if(!$.founded(paramURL)){
			throw new Error("paramURL 不能为空 !");
		}
		//后台查询初始化富头像上传参数
		$.post(paramURL,requestMap,function(data){
			$.fullAvatarDialog(title,$.extend({},_options||{},data||{}),callback);
		});
	};
	
	//统一报表弹窗
	$.reportDialog = function(options,callback){
		options = options||{};
		if(!$.founded(options["reportID"])){
			throw new Error("reportID 不能为空 !");
		}
		var requestMap = $.extend(true,{},options,{
			"reportID" 		: encodeURIComponent(options["reportID"]||""),
			"viewType" 		: "dialog",
			"searchType" 	: (options["searchType"]||""),
			"gnmkdm" 		: (options["gnmkdm"]||$("#gnmkdmKey").val())
		});
		
		//弹窗
		$.dialog({
			"title"		: options["title"] || '报表预览',
			"href"		: _path + '/report/report_cxReportIndex.html',
			"data"		: requestMap,
			"width"		: (options["width"]||$("#yhgnPage").innerWidth() +"px"),
			"modalName"	: "ReportModal",
			"onHidden"	: function(){
				if($.isFunction(callback)){
					callback.call(this);
			   	}
			},
			"buttons"	: {
				cancel : {
					label : "取 消",
					className : "btn-default"
				}
			}
		});
	};
	
	//批量修改弹窗
	$.batchModifyDialog = function(options,callback){
		options = options||{};
		var requestMap = $.extend(true,{},options,{
			"gnmkdm" 		: (options["gnmkdm"]||$("#gnmkdmKey").val())
		});
		if(!$.founded(requestMap["gnmkdm"])){
			throw new Error("gnmkdm 不能为空 !");
		}
		if(!$.founded(requestMap["plxgURL"])){
			throw new Error("plxgURL 不能为空[该路径为最终执行批量修改操作的请求路径] !");
		}
		//弹窗
		$.dialog($.extend({},addConfig,{
			"title"		: options["title"] || '批量修改数据',
			"href"		: _path+'/xtgl/plxg_cxPlxgSettings.html',
			"data"		: requestMap,
			"width"		: (options["width"]||"600px"),
			"modalName"	: "ReportModal",
			"onHidden"	: function(){
				if($.isFunction(callback)){
					callback.call(this);
			   	}
			}
		}));
	};
	
	$.fn.clearElements = function(){
		return this.each(function() {
			if($(this).is('input,select,textarea')){
				$(this).clearFields();
				$(this).trigger("chosen:updated");
			}else{
				//筛选要清除数据的元素
				var elements = $(this).find('input,select,textarea').filter(function(index) {
					 return !$(this).hasClass("ignore");
				});
				var elementFilters = $(elements).filter('[data-clear="true"]');
				if(elementFilters.size() > 0){
					$(elementFilters).clearFields();
					$(elementFilters).trigger("chosen:updated");
				}else{
					$(elements).clearFields();
					$(elements).trigger("chosen:updated");
				}
			}
		});
	};
	
})(jQuery);


//弹出窗口默认行为

var viewConfig = {
	width		: "500px",
	modalName	: "viewModal",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
			}
		}
	}
};

var addConfig = {
	width		: "900px",
	modalName	: "addModal",
	formName	: "ajaxForm",
	gridName	: "tabGrid",
	offAtOnce	: false,
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				//this.close();
				submitForm(opts["formName"]||"ajaxForm",function(responseData,statusText){
					$this.reset();
					// responseData 可能是 xmlDoc, jsonObj, html, text, 等等...
					// statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
					//字符型响应结果
					if($.type(responseData) === "string"){
						if(responseData.indexOf("成功") > -1){
							$.success(responseData,function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//清除元素数据
								$($this["document"]).clearElements();
								var tabGrid = $("#" + opts["gridName"]||"tabGrid");
								//清除页面元素
								if($(tabGrid).size() > 0){
									$(tabGrid).reloadGrid();
								}
							});
						}else if(responseData.indexOf("失败") > -1){
							$.error(responseData,function() {
								
							});
						} else{
							$.alert(responseData,function() {
								
							});
						}
					}
					//JSON型响应结果
					else if($.isPlainObject(responseData)){
					   if(responseData["status"] == "success"){
							$.success(responseData["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//清除元素数据
								$($this["document"]).clearElements();
								var tabGrid = $("#" + opts["gridName"]||"tabGrid");
								//清除页面元素
								if($(tabGrid).size() > 0){
									$(tabGrid).reloadGrid();
								}
							});
						}else if(responseData["status"] == "error"){
							$.error(responseData["message"]);
						}else{
							$.alert(responseData["message"]);
						}
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


var modifyConfig = {
	width		: "900px",
	modalName	: "modifyModal",
	formName	: "ajaxForm",
	gridName	: "tabGrid",
	offAtOnce	: true,
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				submitForm(opts["formName"]||"ajaxForm",function(responseData,statusText){
					$this.reset();
					// responseData 可能是 xmlDoc, jsonObj, html, text, 等等...
					// statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
					//字符型响应结果
					if($.type(responseData) === "string"){
						if(responseData.indexOf("成功") > -1){
							$.success(responseData,function() {
								if(opts.offAtOnce){
									$.closeModal(opts["modalName"]||"modifyModal");
								}
								var tabGrid = $("#" + opts["gridName"]||"tabGrid");
								//清除页面元素
								if($(tabGrid).size() > 0){
									$(tabGrid).reloadGrid();
								}
							});
						}else if(responseData.indexOf("失败") > -1){
							$.error(responseData,function() {
								
							});
						} else{
							$.alert(responseData,function() {
								
							});
						}
					}
					//JSON型响应结果
					else if($.isPlainObject(responseData)){
					   if(responseData["status"] == "success"){
							$.success(responseData["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts["modalName"]||"modifyModal");
								}
								var tabGrid = $("#" + opts["gridName"]||"tabGrid");
								//清除页面元素
								if($(tabGrid).size() > 0){
									$(tabGrid).reloadGrid();
								}
							});
						}else if(responseData["status"] == "error"){
							$.error(responseData["message"]);
						}else{
							$.alert(responseData["message"]);
						}
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

//审核弹窗配置
var shConfig = {
	width:"900px",
	modalName:"shModal",
	fixedTarget:"#fixtop",
	buttons:{}
};