/*
 * @discretion	: 基于jQuery.validate 的校验插件 .主要是实现通用逻辑减少开发人员校验逻辑编写
 * @author    	: wandalong 
 * @version		: v1.0.2
 * @email     	: hnxyhcwdl1003@163.com
 */
var afterSuccessFunc = function(responseData,statusText){
	// responseData 可能是 xmlDoc, jsonObj, html, text, 等等;本函数中data是JSON对象
	var canClose = false;
	//字符型响应结果
	if($.type(responseData) === "string"){
		if(responseData.indexOf("成功") > -1){
			$.success(responseData,function() {
				canClose = true;
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
				canClose = true;
			});
		}else if(responseData["status"] == "error"){
			$.error(responseData["message"]);
		}else{
			$.alert(responseData["message"]);
		}
	}
	return canClose;
};

//公共的form提交函数
function submitForm(formID,callback){
	if(jQuery.isFunction(callback)){
		afterSuccessFunc = callback;
	}
	jQuery('#'+formID).submit();
}

;(function($){
	
	$.validator.auto = true;
	$.validator.selector = "#ajaxForm";
	$.validator.setDefaults( {
		debug : true
	});
	
	$.extend($.validator.prototype, {
		//重写focusInvalid方法，解决chosen无法获取焦点问题
		focusInvalid: function() {
			if ( this.settings.focusInvalid ) {
				try {
					
					var element = $(this.findLastActive() || this.errorList.length && this.errorList[0].element || []);
					if(element.hasClass("chosen-select")){
						element.css({
							"display" : "block"
						});
					}
					element.filter(":visible")
					.focus()
					// manually trigger focusin event; without it, focusin handler isn't called, findLastActive won't have anything to find
					.trigger("focusin");
					if(element.hasClass("chosen-select")){
						window.setTimeout( function () {
							element.css({
								"display" : "none"
							});
				        }, 100 );
					}
				} catch(e) {
					// ignore IE throwing errors when focusing hidden elements
				}
			}
		}
	});
	
	if($.metadata){
		$.metadata.setType("attr", "validate");
	}
	
	var cssArr = ["progress-bar-info","progress-bar-success","progress-bar-warning","progress-bar-danger"];
	 
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	$.bootui.widget.Validator = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Validator.prototype = {
		constructor: $.bootui.widget.Validator,
		/*初始化组件参数*/
		initialize 	: function(form, options) {
			var $this = this;
			//取form的url作为提交地址
	    	if($(form).is("form")&&$(form).attr("action").length!=0){
	    		options["url"] = $(form).attr("action");
	    		options["type"] = $(form).attr("method") ||"post";
	    	}
	    	//扩展每个ajaxSubmit
	    	var settings =  $.extend({}, options, {
	    		// 是否在失去焦点时验证,默认:true  
	    		onfocusout: function( element ) {
	    			var that = this;
	    			//是否失去焦点校验
	    			if(options.onblur){
	    				//元素的onfocus有指定调用函数
	    				if($.trim($(element).attr("onfocus")).length==0){
	    					//兼容My97DatePicker冲突，解决失去焦点的时候不会验证问题
	    	    			var that = this;
	    	    			window.setTimeout(function() {
	    		    			if (!that.checkable(element) && (element.name in that.submitted || !that.optional(element))) {
	    		    				that.element(element);
	    		    			}
	    	    			}, 100);
		    			}else{
		    				if ( !that.checkable(element) && (element.name in that.submitted || !that.optional(element)) ) {
		    					that.element(element);
		    				}
		    			}
	    			}
	    		},
	    		/*显示验证错误
	    	    showErrors: function (errorMap, errorArr) {
	    	        //errorMap {'name':'错误信息'}
	    	        //errorArr [{'message':'错误信息',element:({})}]
	    	        
	    	        var element =  $(errorArr[0].element);
	    	        //获取焦点
	    	        $(element).focus();
	    	       // alert(errorArr[0].message);
	    	    },*/
	    		// 要验证的元素通过验证后的动作，如果跟一个字符串，会当做一个css类，也可跟一个函数
	    		//success : "",
	    		success : function(label, element) {
	    			
	    			$(label).remove();
	    			//适配不同页面环境的提示样式元素
	    			var errorElement = $(element).getTargetElement();
	    			//移除样式
	    			$(errorElement).removeClass(options.errorParentClass);
	    			$(errorElement).removeClass(options.warningParentClass);
	    			$(errorElement).removeClass(options.validParentClass);
	    			//判断提示方式
	    			if("wrap" == options.tipMethod){
	    				// set &nbsp; as text for IE
	    				//$(label).html("&nbsp;").attr("class",options.validClass);
	    				//$(label).text("Ok!");
	    			}else if("tooltips" == options.tipMethod){
	    				
	    				//兼容各种插件获得最终绑定tooltips的DOM元素
	    				$(element).getRealElement().tooltips("destroy");
	    			}
	    			$(element).trigger("valid:success");
	    		},
	    		// 指明错误放置的位置，默认情况是：error.appendTo(element.parent());即把错误信息放在验证的元素后面
	    		errorPlacement : function(label, element) {
	    			//适配不同页面环境的提示样式元素
	    			var errorElement = $(element).getTargetElement();
	    			//调整提示样式
	    			$(errorElement).removeClass(options.errorParentClass);
	    			$(errorElement).removeClass(options.warningParentClass);
	    			$(errorElement).removeClass(options.validParentClass);
	    			$(errorElement).addClass(options.errorParentClass);
	    			//判断提示方式
	    			if("wrap" == options.tipMethod){
	    				$(label).attr("class",options.errorClass);
	    				//如果是radio或checkbox
		    			if ($(element).is(':radio') || $(element).is(':checkbox')) {
		    				$(element).parent().find("em").remove();
	    					$(label).appendTo($(element).parent());//将错误信息添加当前元素的父结点后面
		    			} else {
		    				$(element).find("em").remove();
		    				$(label).appendTo($(element).parent());
		    				label.insertAfter(element);
		    			}
	    			}else if("tooltips" == options.tipMethod){
	    				 //根据文本框调整相应提示信息的css
	    			    var bootbox = $(element).closest(".modal-body");
	    			    var jqgrid = $(element).closest(".ui-jqgrid");
	    			    var direction,bodyTop;
	    			    var elTop=$(element).offset().top;
	    			    if(bootbox.size() > 0){
	    			    	bodyTop=bootbox.offset().top;
	    			    	if(elTop-bodyTop<50){
		    			    	direction='bottom';
		    			    }else{
		    			    	direction='top';
		    			    }
	    			    }else{
	    			    	direction='top';
	    			    }
	    			    

	    			    
	    				//绑定tooltips插件
	    				var options2  	= 	{
    						delay			: 	0,
		    				html			: 	true,
		    				placement		:	$(element).data("placement")||direction,
		    				align			:	$(element).data("align")||"right",
		    				trigger			:   "hover focus",
		    				container		: 	(jqgrid.size() > 0 ? jqgrid : (bootbox.size() > 0 ? bootbox : "body"))
	    				};
	    				if ($(this).is(':radio') || $(this).is(':checkbox')) {
							options2["placement"] = "right";
						}
	    				//兼容各种插件获得最终绑定tooltips的DOM元素
	    				var newElement = $(element).getRealElement();
	    				//可见才会触发气泡提示
	    				if($(newElement).is(":visible")){
	    					
		    				$(newElement).tooltips('destroy');
			    			$(newElement).attr({
			    				"title" 		: 	$(label).html()
			    			});
			    			$(newElement).tooltips(options2);
			    			//立刻显示提示信息
			    			if(options.tipAtOnce && true == options.tipAtOnce){
			    				$(newElement).tooltips('show');
			    			}
	    				}
	    			}
	    		},
	    		submitHandler : function(form) {
				   //如果验证前的其他验证没有通过则不继续验证
				   var status = options.beforeValidated.call(form);
				   if(status){
					   //根据指定的progressElement值取进度条元素对象
					   var progressbar = $(options.progressElement);
					  
					   //options.progressURL 不存在，且开启了进度条，则模拟大概请求进度：
					   var localbar = $(progressbar).size() == 1 && !$.founded(options.progressURL);
					   var remotebar = $(progressbar).size() == 1 && $.founded(options.progressURL);
					   
					   //再次扩展options;防止用户自定义了url,type
					   var ajaxSettings = {
							target		: settings.target,       	//把服务器返回的内容放入id为output的元素中    
							clearForm	: settings.clearForm,  //布尔标志，表示如果表单提交成功是否清除所有表单元素的值  
							resetForm	: settings.resetForm,  //布尔标志，表示如果表单提交成功是否重置所有表单元素的值
							
							type		: settings.type ||"post",    //默认是form的method（get or post），如果申明，则会覆盖  
							url			: settings.url,              //默认是form的action， 如果申明，则会覆盖  
							contentType	: "application/x-www-form-urlencoded;charset=UTF-8",
							dataType	: settings.dataType ||"json", 	 
							timeout		: settings.timeout,      
							async		: settings.async,
							
							//提交前的回调函数
							beforeSubmit: function(formData,jqForm,opts){
						   		var canSubmit = settings.beforeSubmit.call(form,formData,jqForm,opts);
						   		if(canSubmit == true ){
						   			
						   			if( localbar || remotebar){
						 			   //初始化进度条样式和是否处理完成的标记
						 			   progressbar.removeClass(cssArr.join(" ")).addClass("progress-bar-info");
						 			   progressbar.closest(".progress").show();
						 			   //请求开始前移除上一次请求的结果标记
									   progressbar.removeData("successed");
									   progressbar.removeData("errored");
									   progressbar.removeData("finished");
						 		    }
						   			if(localbar == true){
									   /*
										*beforeSubmit	：让进度条 从0% 缓慢/迅速 增长到40%
										*beforeSend		：让进度条 从40% 缓慢/迅速 增长到70%
										*success		：让进度条 从70% 缓慢/迅速 增长到90%
										*error			：进度不变，改变进度条样式为失败样式
										*complete		：让进度条 从90% 缓慢/迅速 增长到100%;启动延时清理状态和进度归零
									   */
						   				progressbar.closest(".progress").fadeIn("fast",function(){
								    		//让进度条 从0% 缓慢/迅速 增长到40%
										   	progressbar.css("width","40%").text("40%").attr("aria-valuenow",40);
						   				});
							    	}
						   		}
						   		return canSubmit;
						    },
						    //请求发送时的回调函数
						    beforeSend	: function(xhr,opts){
						    	if(localbar == true){
						    		//让进度条 从40% 缓慢/迅速 增长到70%
						    		progressbar.css("width","70%").text("70%").attr("aria-valuenow",70);
						    	}else if(remotebar == true){
						    		progressbar.closest(".progress").fadeIn("fast",function(){
										$(progressbar).css("width","0%").text("0%").attr("aria-valuenow",0);
										//定时执行Ajax请求处理进度
										var tryTimes = 0;
										$(options.progressElement).progress($.extend({},options,{
											"beforeLookup" : function(percentage,finished){
												//-1表示尚不能从session中获取到进度状态，可能是进度未开始也可能是进度结束，故需要多尝试几次确定是否是未开始
												if(percentage == -1 && tryTimes <= 10){
													tryTimes += 1; 
													return true;
												}
												return percentage >= 0 && finished != true;
											}
										}));
									});
						    	}
						    	//调用扩展的请求发送时的回调函数
						   		return settings.beforeSend.call(this,xhr,opts,options);
						    },
							//请求成功后的回调函数
							success 	: function(responseData,statusText,xhr){
						    	if(localbar == true){
						    		//让进度条 从70% 缓慢/迅速 增长到90%
						    		progressbar.css("width","90%").text("90%").attr("aria-valuenow",90).removeClass(cssArr.join(" ")).addClass("progress-bar-success");
						    	}else if(remotebar == true){
						    		//字符型响应结果
						    		if($.type(responseData) === "string"){
						    			if(responseData.indexOf("成功") > -1){
						    				//标志请求成功
								    		progressbar.data("successed",true);
						    			}else if(responseData.indexOf("失败") > -1){
						    				//标志请求失败
								    		progressbar.data("errored",true);
							    		}
						    		}
						    		//JSON型响应结果
						    		else if($.isPlainObject(responseData)){
						    		   if(responseData["status"] == "success"){
						    			   //标志请求成功
						    			   progressbar.data("successed",true);
						    		   }else if(responseData["status"] == "error"){
						    			   //标志请求失败
						    			   progressbar.data("errored",true);
						    		   }
						    		}
						    	}
						    	//调用扩展的请求成功后的回调函数
						    	settings.afterSuccess.call(form,responseData,statusText,options);
					   		},
					   		//请求失败的回调函数
					   		error		: function(xhr, statusText, errMsg){
					   			//textStatus的值：null, timeout, error, abort, parsererror  
					   		    //errMsg的值：收到http出错文本，如 Not Found 或 Internal Server Error. 
					   			if(localbar == true){
					   				//进度不变，改变进度条样式为失败样式
					   				progressbar.removeClass(cssArr.join(" ")).addClass("progress-bar-danger");
						    	}else if(remotebar == true){
						    		//标志请求失败
						    		progressbar.data("errored",true);
						    	}
					   			//调用扩展的请求失败的回调函数
					   			settings.onError.call(this,xhr,statusText, errMsg,options);
					   		},
					   		//请求完成的回调函数
					   		complete	: function(xhr, statusText){
					   			//textStatus的值：success,notmodified,nocontent,error,timeout,abort,parsererror  
					   			if(statusText == "success" && localbar == true){
						    		//让进度条 从90% 缓慢/迅速 增长到100%
					   				progressbar.css("width","100%").text("100%").attr("aria-valuenow",100);
					   				//进度处理完成后进度归零
						   			window.setTimeout(function(){
						   				progressbar.closest(".progress").hide();
						   				progressbar.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(cssArr.join(" "));
									},options.progressDelay);
					   			}
					   			//标志请求结束
					   			progressbar.data("finished",true);
								//调用扩展的请求完成的回调函数
					   			settings.onComplete.call(this,xhr,statusText,options);
					   		}
					   };
					   if($(form).is("form")&&$(form).attr("action").length!=0){
						   ajaxSettings["url"] = $(form).attr("action");
						   ajaxSettings["type"] = $(form).attr("method") ||"post";
						   ajaxSettings["contentType"] = $(form).attr("enctype") || ajaxSettings["contentType"];
			 	       }
					   window.setTimeout(function(){
						// 将ajaxSettings传给ajaxSubmit:利用$.ajax提交
						   $(form).ajaxSubmit( ajaxSettings);
					   },10);
				   }
				   // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
				   return false; // 此处必须返回false，阻止常规的form提交
				}
	    	});
	    	
	    	var validator = $(form).validate(settings);
	    	
	    	var methods = {
				//把前面验证的FORM恢复到验证前原来的状态
				resetValid:function(){
	    			validator.resetForm();
				},
				//清除验证效果
				clearValid:function(){
					$(form).find(options.errorElement+"."+options.errorClass).remove();
					$(form).find(options.errorElement+"."+options.validClass).remove();
					$(form).find("."+options.errorParentClass).removeClass(options.errorParentClass);
					$(form).find("."+options.warningParentClass).removeClass(options.warningParentClass);
					$(form).find("."+options.validParentClass).removeClass(options.validParentClass);
					if($.fn.tooltips){
						$(form).find(":input").each(function(){
							$(this).getRealElement().tooltips('destroy');
						});
					}
				}
	    	};
	    	/*扩展this:组件函数*/
			$.extend(true,$this,{
				// 表单对象是否验证通过
				isValid:function(){
					var isValid = true;
					$(form).find(":input").each(function(){
						if($(this).valid() == false){
							isValid = false;
							return false;
						}
					});
					return isValid;
				},
				getRules:function(){
					return validator.rules();
				},
				// 提交表单对象
				submitForm:function(){
					$(form).submit();
				},
				// 重置表单对象
				resetForm:function(){
					//利用jquery-form重置from原始初始值
					$(form).resetForm();
				},
				//清除表单元素。该方法将所有的文本（text）输入字段、密码（password）输入字段和文本区域（textarea）字段置空，
				//清除任何select元素中的选定，以及将所有的单选（radio）按钮和多选（checkbox）按钮重置为非选定状态。
				clearForm:function(){
					$(form).clearForm();
				}
			},methods);
			
			$.each(methods,function(key,func){
				$(form).bind(key,func);
			});
		
		},
		setDefaults	: function(settings){
			$.extend($.fn.validateForm.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.validateForm.defaults;
		}
	};
	
		
	$.fn.validateForm = function(option){
		if (typeof option == 'string'){
			var $this = $(this[0]), data = $this.data('myValidator');
			if (!data && option == 'destroy') {return;}
			if (data){
				//处理后的参数
				var args = $.grep( arguments || [], function(n,i){
					return i >= 1;
				});
				//调用函数
				return data[option].apply(data, [].concat(args || []) );
			}
		}else{
			return this.each(function () {
				var $this = $(this), data = $this.data('myValidator');
				if (!data){
					var options = $.extend( true ,{}, $.fn.validateForm.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
					$this.data('myValidator', (data = new $.bootui.widget.Validator(this, options)));
				}
			});
		}
	};
	
	$.fn.validateForm.defaults = {
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender		: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender			: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender			: $.noop,
		/*其他参数*/
		onsubmit			: true,	// 是否在提交是验证,默认:true
		onkeyup				: false, // 是否在敲击键盘时验证,默认:false
		onblur				: true , // 失去焦点时是否验证,默认:true
		// 是否在敲击键盘时验证,默认:true 
	    //onkeyup :function(element) { $(element).valid(); }, 
	    // 是否在鼠标点击时验证（一般验证checkbox,radiobox） 
	    onclick				: false,	
	    //提交表单后,未通过验证的表单(第一个或提交之前获得焦点的未通过验证的表单)会获得焦点
		focusInvalid 		: true,
		//立刻提醒错误信息
		tipAtOnce 			: false,
		// 当未通过验证的元素获得焦点时,并移除错误提示
		//focusCleanup	:	false,  
		/*对某些元素不进行验证*/
		ignore				: ".ignore",
		//用什么标签标记错误，默认的是label你可以改成em
		errorElement 		: "em", 
		//用什么标签再把上边的errorELement包起来
		//wrapper："" , 
		//errorClass：验证失败时的样式，如果没有指定，默认是"error"(所以自定义的css要避免使用此名称)， 默认会添加到验证失败的 element和失败的提示信息中。
		errorClass			: "error",
		errorParentClass	: "has-error",
		
		warningClass		: "warning",
		warningParentClass	: "has-warning",
		
		//validClass：验证成功的样式，如果没有指定，默认是"valid"(所以自定义的css要避免使用此名称)， 默认会添加到验证成功的element。
		validClass			: "success",
		validParentClass	: "has-success",
		
		
		//显示提交进度的状态条元素
		progressElement		: null,
		//进度回调请求URL
		progressURL			: null,
		//进度处理完成后进度归零延时时间；单位毫秒，默认3000
		progressDelay		: 3000,
		//定时请求处理进度Ajax执行周期；单位毫秒，默认400
		progressPeriod		: 400,
		//实时进度时，完成一次百分比检查后的回调函数，默认$.noop
		progressComplete	: $.noop,
		//提示信息显示方式 tooltips|wrap
		tipMethod			: "tooltips", 
		
		//ajaxSubmit参数
		target				: '#output',        //把服务器返回的内容放入id为output的元素中
		clearForm			: false,          	//布尔标志，表示如果表单提交成功是否清除所有表单元素的值  
		resetForm			: false,          	//布尔标志，表示如果表单提交成功是否重置所有表单元素的值
		
		url					: null,             //如果绑定在form的validate则默认是form的action， 如果申明，则会覆盖  
		type				: "post",           //如果绑定在form的validate则默认是form的method（get or post），如果申明，则会覆盖  
		/**
		 * 预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断，比如XML MIME类型就被识别为XML。
		 * 在1.4中，JSON就会生成一个JavaScript对象，而script则会执行这个脚本。随后服务器端返回的数据会根据这个值解析后，传递给回调函数。可用值: 
			"xml": 返回 XML 文档，可用 jQuery 处理。
			"html": 返回纯文本 HTML 信息；包含的script标签会在插入dom时执行。
			"script": 返回纯文本 JavaScript 代码。不会自动缓存结果。除非设置了"cache"参数。'''注意：'''在远程请求时(不在同一个域下)，所有POST请求都将转为GET请求。(因为将使用DOM的script标签来加载)
			"json": 返回 JSON 数据 。
			"jsonp": JSONP 格式。使用 JSONP 形式调用函数时，如 "myurl?callback=?" jQuery 将自动替换 ? 为正确的函数名，以执行回调函数。
			"text": 返回纯文本字符串
		 */
		dataType			: "json",			//期望返回的数据类型。null、“xml”、“script”或者“json”其中之一；json(默认)
		timeout				: 60000, 			//限制请求的时间，当请求大于1分钟后，跳出请求
		async				: true, 			//(默认: true) 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。

		
		//如果指定了此属性，则验证失败后errorClass不会添加到element中。
		//highlight : function(element, errorClass) { // 针对验证的表单设置高亮
		//	$(element).addClass(errorClass);
		//},
		//如果指定了此属性，则验证成功后errorClass不会添加到element中。 
		//unhighlight : function(element, errorClass) { // 针对验证的表单设置高亮
		//	$(element).addClass(errorClass);
		//},
		
		//执行验证前的回调函数
		beforeValidated		:function(){return true;},
		/**
		 * formData: 数组对象，提交表单时，Form插件会以Ajax方式自动提交这些数据，格式如：[{name:user,value:val },{name:pwd,value:pwd}]
		 * jqForm:   jQuery对象，封装了表单的元素  
		 * options:  options对象  
		 * 只要不返回false，表单都会提交,在这里可以对表单元素进行验证   
		 */
		//提交前的回调函数
		beforeSubmit		:function(formData,jqForm,options){return true;},
		//请求发送时的回调函数
	    beforeSend:function(xhr,options,setting){return true;  },
		//请求成功后的回调函数
		afterSuccess		:function(responseData,statusText,setting){
			if(typeof afterSuccessFunc == "function"){
				afterSuccessFunc.call(this,responseData,statusText);
			}
		},
   		//请求失败的回调函数
		onError	: function(xhr, statusText, errMsg,setting){
			
		},
   		//请求完成的回调函数
   		onComplete: function(xhr, statusText,setting){
			
			
		}
	};
	
	$.fn.validateForm.Constructor = $.bootui.widget.Validator;
	
	/*============== Validator DATA-API  ==============*/
	
	/*委托验证事件：实现自动绑定验证:触发验证的代码在弹窗里面
	 * 
	 * 在验证的form上添加 data-toggle="validation"
	 * */
	$(document).on('validation.data-api','[data-toggle*="validation"]', function (event) {
		if(event.currentTarget = this){
			var validator = $(this).validateForm({
				//进行验证前的回调函数
				beforeValidated:function(){
					return true;
				},
				//提交前的回调函数
				beforeSubmit:function(formData,jqForm,options){
					return true;
				},
				//提交后的回调函数  
				afterSuccess:function(responseData,statusText){
					afterSuccessFunc.call(validator, responseData,statusText);
				}
			});
			
		}
	});
	
	$.fn.extend({
		//判断验证是否通过
		isValid:function(){
			if($(this).size() == 0){
				return false;
			}
			if ($(this[0]).is("form")) {
/*				if($.trim($(this[0]).attr("action")).length>0){
					$(this).submit();
				}
*/				var isValid = true;
				$(this[0]).find(":input").each(function(){
					if($(this).valid() == false){
						isValid = false;
						return false;
					}
				});
				return isValid;
			} else {
				return $(this).valid() !=false ;
			}
		},
		//把前面验证的FORM恢复到验证前原来的状态
		resetValid:function(){
			if ( $(this[0]).is("form")) {
				$(this[0]).validate().resetForm();
			} else {
				$(this[0].form).validate().resetForm();
			}
		},
		//清除验证效果
		clearValid : function(){
			if ( $(this[0]).is("form")) {
				$(this).find("em.error").remove();
				$(this).find("em.success").remove();
				$(this).find(".has-error").removeClass("has-error");
				$(this).find(".has-warning").removeClass("has-warning");
				$(this).find(".has-success").removeClass("has-success");
				if($.fn.tooltips){
					$(this).find(":input").each(function(){
						$(this).getRealElement().tooltips('destroy');
					});
				}
			} else {
    			//适配不同页面环境的提示样式元素
				$($(this).getTargetElement()).removeClass("has-error has-warning has-success");
				if($.fn.tooltips){
					$(this).getRealElement().tooltips('destroy');
				}
			}
		}
	});
				
}(jQuery));
