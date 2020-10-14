;(function($) {

	$.oldAjax = $.ajax;
	var cssArr = ["progress-bar-info","progress-bar-success","progress-bar-warning","progress-bar-danger"];
	
	/**
	 * 较耗时的业务处理需要调用服务接口获取实时进度 
	 * @param options{
	 * 		progressElement	: 显示提交进度的状态条元素的选择器{String}
	 * 		progressURL		: 获取进度的URL{String}
	 * 		progressDelay	: 进度处理完成后进度归零延时时间；单位毫秒，默认3000{Number}
	 * 		progressPeriod	: 定时请求处理进度Ajax执行周期；单位毫秒，默认400{Number}
	 * 		progressComplete: 实时进度时，完成一次百分比检查后的回调函数{func}
	 * }
	 * @param func		: 判断条件
	 * @return
	 */
	$.fn.progress = function(options){
		//初始化默认值
		$.extend(options||{},{
			 //进度完成前的检查
			 beforeLookup		: options["beforeLookup"] || $.noop,
			 //进度回调请求URL
			 progressURL		: options["progressURL"],
			 //进度处理完成后进度归零延时时间；单位毫秒，默认2000
			 progressDelay		: options["progressDelay"] || 3000,
			 //定时请求处理进度Ajax执行周期；单位毫秒，默认400
			 progressPeriod		: options["progressPeriod"] || 400,
			 //实时进度时，完成一次百分比检查后的回调函数，默认$.noop
			 progressComplete	: options["progressComplete"] || $.noop
		});	
		return $(this).each(function(){
			//当前进度条
			var progressbar = $(this);
			var finished 	= progressbar.data("finished")||false;
			
			//主体的请求已经完成
			if(finished == true){
				
				var callback = function(){
					var errored 	= progressbar.data("errored")||false;
					var successed 	= progressbar.data("successed")||false;
					//主体的请求成功
					if(successed == true){
						if(!progressbar.hasClass("progress-bar-success") ){
							progressbar.removeClass(cssArr.join(" ")).addClass("progress-bar-success");
						}
						//让进度条 从90% 缓慢/迅速 增长到100%
						progressbar.css("width","100%").text("100%").attr("aria-valuenow",100);
						//进度处理完成后进度归零
			   			window.setTimeout(function(){
			   				progressbar.closest(".progress").hide();
			   				progressbar.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(cssArr.join(" "));
						},options["progressDelay"]);
					}else if(errored == true){
						//主体的请求失败;进度不变，改变进度条样式为失败样式
		   				progressbar.removeClass(cssArr.join(" ")).addClass("progress-bar-danger");
					}
				}
				var def = progressbar.data("def");
				if(def){
					$.when(def).done(callback);
				}else{
					callback.call(this);
				}
			}else{
				$.ajax({
				   type		: "POST",
				   url		: options["progressURL"],
				   async	: true,
				   dataType	: "json",
				   success	: function(percentage){
						percentage = parseFloat(percentage);
						//percentage >= 0 && finished != true
						if(options["beforeLookup"].call(this,percentage,finished) ){
							if(percentage >= 0 ){
								var progressDiv = progressbar.closest(".progress");
								if(progressDiv.is(":hidden")){
									progressDiv.show();
								}
								//实时更新进度条的刻度值
								percentage = Math.min(percentage,100);
								progressbar.css("width",percentage + "%").text(percentage + "%").attr("aria-valuenow",percentage);
							}
							if(percentage >= 90 && !progressbar.hasClass("progress-bar-success")){
								progressbar.removeClass(cssArr.join(" ")).addClass("progress-bar-success");
							}
							if(percentage >= 100 ){
								progressbar.data("finished",true);
							}
							//定时执行Ajax请求处理进度
							window.setTimeout(function(){
								$(progressbar).progress(options);
							}, options["progressPeriod"]);
						} else if(finished == true){
							
							//进度处理完成后进度归零
				   			window.setTimeout(function(){
				   				progressbar.closest(".progress").hide();
				   				progressbar.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(cssArr.join(" "));
							},options["progressDelay"]);
						}
						//调用回调函数
						options["progressComplete"].call(this,percentage);
				   	}
				});
			}
		});
	};
	
	//resetRemoteProcessbar
	$.fn.progress.reset 	= function(options){
		return $(this).progress($.extend({},options,{
			"beforeLookup" : function(percentage,finished){
				return percentage > 0 && finished != true;
			}
		}));
	};
	
	
	/**
	 * 
	 * 扩展Ajax请求，增加进度条支持
	 * @param options{
	 * 		progressElement	: 显示提交进度的状态条元素的选择器{String}
	 * 		progressURL		: 获取进度的URL{String}
	 * 		progressPeriod	: 两次调用请求的间隔时间，单位毫秒{String}
	 * 		progressDelay	: 进度处理完成后进度归零延时时间；单位毫秒，默认3000{Number}
	 * 		progressPeriod	: 定时请求处理进度Ajax执行周期；单位毫秒，默认400{Number}
	 * 		progressComplete: 实时进度时，完成一次百分比检查后的回调函数{func}
	 * }
	 */
	jQuery.ajax = jQuery.wrapAjax = function(options){
		if(!(jQuery.type(options) === "string") && $.founded(options.progressElement) && $(options.progressElement).size() > 0 ){
			
			//初始化默认值
			$.extend(options||{},{
				 //显示提交进度的状态条元素
				 progressElement	: options["progressElement"],
				 //进度回调请求URL
				 progressURL		: options["progressURL"],
				 //进度处理完成后进度归零延时时间；单位毫秒，默认2000
				 progressDelay		: options["progressDelay"] || 3000,
				 //定时请求处理进度Ajax执行周期；单位毫秒，默认400
				 progressPeriod		: options["progressPeriod"] || 400,
				 //远程进度请求时候,每次请求前的检查回调函数，默认$.noop
				 beforeLookup		: options["beforeLookup"] || $.noop,
				 //实时进度时，完成一次百分比检查后的回调函数，默认$.noop
				 progressComplete	: options["progressComplete"] || $.noop
			});	
			//复制默认事件
			var tmpOptions = $.extend({},{
				"beforeSend" 	: options.beforeSend||$.noop,
				"success" 		: options.success||$.noop,
				"error" 		: options.error||$.noop,
				"complete" 		: options.complete||$.noop
			});
			//根据指定的progressElement值取进度条元素对象
			var progressbar = $(options.progressElement);
			//options.progressURL 不存在，且开启了进度条，则模拟大概请求进度：
			var localbar = $(progressbar).size() == 1 && !$.founded(options.progressURL);
			var remotebar = $(progressbar).size() == 1 && $.founded(options.progressURL);
			
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
				 *事件触发			：让进度条 从 0% 缓慢/迅速 增长到 40%
				 *beforeSend	：让进度条 从40% 缓慢/迅速 增长到70%
				 *success		：让进度条 从70% 缓慢/迅速 增长到90%
				 *error			：进度不变，改变进度条样式为失败样式
				 *complete		：让进度条 从90% 缓慢/迅速 增长到100%;启动延时清理状态和进度归零
				 */
				progressbar.closest(".progress").fadeIn("fast",function(){
		    		//让进度条 从0% 缓慢/迅速 增长到40%
				   	progressbar.css("width","40%").text("40%").attr("aria-valuenow",40);
   				});
			}
			
			//定义延时对象
			$.Deferred(function(def){
				//弹窗对象记录延时对象
				progressbar.data("def",def);
		    }).promise();
			
			return $.oldAjax($.extend(options,{
				//请求发送时的回调函数
			    beforeSend	: function(xhr,opts){
	    			progressbar.closest(".progress").fadeIn("fast",function(){
				    	if(localbar == true){
				    		//让进度条 从40% 缓慢/迅速 增长到70%
				    		progressbar.css("width","70%").text("70%").attr("aria-valuenow",70);
				    	}else if(remotebar == true){
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
				    	}
			    	});
			    	//调用扩展的请求发送时的回调函数
			   		return tmpOptions.beforeSend.call(this,xhr,opts);
			    },
				//请求成功后的回调函数
				success 	: function(responseData,statusText,xhr){
					// responseData 可能是 xmlDoc, jsonObj, html, text, 等等...
					// statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
					if(localbar == true){
			    		//让进度条 从70% 缓慢/迅速 增长到90%
			    		progressbar.css("width","90%").text("90%").attr("aria-valuenow",90).removeClass(cssArr.join(" ")).addClass("progress-bar-success");
			    	}else if(remotebar == true){
			    		//字符型响应结果
						if($.type(responseData) === "string"){
							if(responseData.indexOf("成功") > -1){
								//标志业务成功
					    		progressbar.data("successed",true);
							}else if(responseData.indexOf("失败") > -1){
								//标志业务失败
					    		progressbar.data("errored",true);
							}
						}
						//JSON型响应结果
						else if($.isPlainObject(responseData)){
							if(responseData["status"] == "success"){
							   //标志业务成功
							   progressbar.data("successed",true);
							}else if(responseData["status"] == "error"){
								//标志业务失败
					    		progressbar.data("errored",true);
							}
						}
			    	}
					var def = progressbar.data("def");
					if(def){
						// 改变deferred对象的执行状态为：已完成
				    	def.resolve();
					}
			    	//调用扩展的请求成功后的回调函数
			    	tmpOptions.success.call(this,responseData,statusText,xhr);
		   		},
		   		//请求失败的回调函数
		   		error		: function(xhr, statusText, errorThrown){
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
		   			tmpOptions.error.call(this,xhr,statusText, errorThrown);
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
		   			tmpOptions.complete.call(this,xhr,statusText);
		   		}
			}));
		}else{
			return $.oldAjax(options);
		}
	};
	
	//重写get和post函数
	$.each( [ "get", "post"], function( i, method ) {
		$[ method ] = function( url, data, callback, type , options ) {
			// shift arguments if data argument was omitted
			if ( $.isFunction( data ) ) {
				type = type || callback;
				callback = data;
				data = undefined;
			}
			if( $.defined(options) == true && !($.type(options) === "string") && $.founded(options.progressElement)  == true  && $(options.progressElement).size() > 0 ){
				return $.ajax($.extend({
					url: url,
					type: method,
					dataType: type,
					data: data,
					success: callback
				},options));
			}else{
				return $.oldAjax({
					url: url,
					type: method,
					dataType: type,
					data: data,
					success: callback
				});
			}
		};
	});

})(jQuery);