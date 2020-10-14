/*
 * jquery.browser 插件：详情参见：jquery.browser.js
 */
;(function(f,e,h){var a,d;f.uaMatch=function(k){k=k.toLowerCase();var j=/(opr)[\/]([\w.]+)/.exec(k)||/(chrome)[ \/]([\w.]+)/.exec(k)||/(version)[ \/]([\w.]+).*(safari)[ \/]([\w.]+)/.exec(k)||/(webkit)[ \/]([\w.]+)/.exec(k)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(k)||/(msie) ([\w.]+)/.exec(k)||k.indexOf("trident")>=0&&/(rv)(?::| )([\w.]+)/.exec(k)||k.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(k)||[];var i=/(ipad)/.exec(k)||/(iphone)/.exec(k)||/(android)/.exec(k)||/(windows phone)/.exec(k)||/(win)/.exec(k)||/(mac)/.exec(k)||/(linux)/.exec(k)||/(cros)/i.exec(k)||[];return{browser:j[3]||j[1]||"",version:j[2]||"0",platform:i[0]||""}};a=f.uaMatch(e.navigator.userAgent);d={};if(a.browser){d[a.browser]=true;d.version=a.version;d.versionNumber=parseInt(a.version)}if(a.platform){d[a.platform]=true}if(d.android||d.ipad||d.iphone||d["windows phone"]){d.mobile=true}if(d.cros||d.mac||d.linux||d.win){d.desktop=true}if(d.chrome||d.opr||d.safari){d.webkit=true}if(d.rv){var g="msie";a.browser=g;d[g]=true}if(d.opr){var c="opera";a.browser=c;d[c]=true}if(d.safari&&d.android){var b="android";a.browser=b;d[b]=true}d.name=a.browser;d.platform=a.platform;f.browser=d})(jQuery,window);
/*************************************************************************/
/*
 * jquery.cookie 插件：详情参见：jquery.cookie.js
 */
;(function(a){a.Cookie=a.cookie=function(c,k,n){if(typeof k!="undefined"){n=n||{};if(k===null){k="";n.expires=-1}var f="";if(n.expires&&(typeof n.expires=="number"||n.expires.toUTCString)){var g;if(typeof n.expires=="number"){g=new Date();g.setTime(g.getTime()+(n.expires*24*60*60*1000))}else{g=n.expires}f="; expires="+g.toUTCString()}var m=n.path?"; path="+(n.path):"";var h=n.domain?"; domain="+(n.domain):"";var b=n.secure?"; secure":"";document.cookie=[c,"=",encodeURIComponent(k),f,m,h,b].join("")}else{var e=null;if(document.cookie&&document.cookie!=""){var l=document.cookie.split(";");for(var j=0;j<l.length;j++){var d=jQuery.trim(l[j]);if(d.substring(0,c.length+1)==(c+"=")){e=decodeURIComponent(d.substring(c.length+1));break}}}return e}}}(jQuery));
/*************************************************************************/
/*
 * jquery.actual 插件：详情参见：jquery.actual.js
 */
;(function(b){b.fn.addBack=b.fn.addBack||b.fn.andSelf;b.fn.extend({actual:function(v,a){if(!this[v]){throw'$.actual => The jQuery method "'+v+'" you called does not exist'}var r={absolute:false,clone:false,includeMargin:false};var o=b.extend(r,a);var s=this.eq(0);var p,n;if(o.clone===true){p=function(){var c="position: absolute !important; top: -1000 !important; ";s=s.clone().attr("style",c).appendTo("body")};n=function(){s.remove()}}else{var q=[];var t="";var u;p=function(){u=s.parents().addBack().filter(":hidden");t+="visibility: hidden !important; display: block !important; ";if(o.absolute===true){t+="position: absolute !important; "}u.each(function(){var c=b(this);var d=c.attr("style");q.push(d);c.attr("style",d?d+";"+t:t)})};n=function(){u.each(function(c){var d=b(this);var e=q[c];if(e===undefined){d.removeAttr("style")}else{d.attr("style",e)}})}}p();var m=/(outer)/.test(v)?s[v](o.includeMargin):s[v]();n();return m}})})(jQuery);
/*
 * jquery.progress 插件：详情参见：jquery.progress.js
 */
//;(function(b){b.oldAjax=b.ajax;var a=["progress-bar-info","progress-bar-success","progress-bar-warning","progress-bar-danger"];b.fn.progress=function(c){b.extend(c||{},{beforeLookup:c.beforeLookup||b.noop,progressURL:c.progressURL,progressDelay:c.progressDelay||3000,progressPeriod:c.progressPeriod||400,progressComplete:c.progressComplete||b.noop});return b(this).each(function(){var d=b(this);var g=d.data("finished")||false;if(g==true){var f=function(){var h=d.data("errored")||false;var i=d.data("successed")||false;if(i==true){if(!d.hasClass("progress-bar-success")){d.removeClass(a.join(" ")).addClass("progress-bar-success")}d.css("width","100%").text("100%").attr("aria-valuenow",100);window.setTimeout(function(){d.closest(".progress").hide();d.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(a.join(" "))},c.progressDelay)}else{if(h==true){d.removeClass(a.join(" ")).addClass("progress-bar-danger")}}};var e=d.data("def");if(e){b.when(e).done(f)}else{f.call(this)}}else{b.ajax({type:"POST",url:c.progressURL,async:true,dataType:"json",success:function(i){i=parseFloat(i);if(c.beforeLookup.call(this,i,g)){if(i>=0){var h=d.closest(".progress");if(h.is(":hidden")){h.show()}i=Math.min(i,100);d.css("width",i+"%").text(i+"%").attr("aria-valuenow",i)}if(i>=90&&!d.hasClass("progress-bar-success")){d.removeClass(a.join(" ")).addClass("progress-bar-success")}if(i>=100){d.data("finished",true)}window.setTimeout(function(){b(d).progress(c)},c.progressPeriod)}else{if(g==true){window.setTimeout(function(){d.closest(".progress").hide();d.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(a.join(" "))},c.progressDelay)}}c.progressComplete.call(this,i)}})}})};b.fn.progress.reset=function(c){return b(this).progress(b.extend({},c,{beforeLookup:function(d,e){return d>0&&e!=true}}))};jQuery.ajax=jQuery.wrapAjax=function(e){if(!(jQuery.type(e)==="string")&&b.founded(e.progressElement)&&b(e.progressElement).size()>0){b.extend(e||{},{progressElement:e.progressElement,progressURL:e.progressURL,progressDelay:e.progressDelay||3000,progressPeriod:e.progressPeriod||400,beforeLookup:e.beforeLookup||b.noop,progressComplete:e.progressComplete||b.noop});var c=b.extend({},{beforeSend:e.beforeSend||b.noop,success:e.success||b.noop,error:e.error||b.noop,complete:e.complete||b.noop});var d=b(e.progressElement);var g=b(d).size()==1&&!b.founded(e.progressURL);var f=b(d).size()==1&&b.founded(e.progressURL);if(g||f){d.removeClass(a.join(" ")).addClass("progress-bar-info");d.closest(".progress").show();d.removeData("successed");d.removeData("errored");d.removeData("finished")}if(g==true){d.closest(".progress").fadeIn("fast",function(){d.css("width","40%").text("40%").attr("aria-valuenow",40)})}b.Deferred(function(h){d.data("def",h)}).promise();return b.oldAjax(b.extend(e,{beforeSend:function(i,h){d.closest(".progress").fadeIn("fast",function(){if(g==true){d.css("width","70%").text("70%").attr("aria-valuenow",70)}else{if(f==true){b(d).css("width","0%").text("0%").attr("aria-valuenow",0);var j=0;b(e.progressElement).progress(b.extend({},e,{beforeLookup:function(k,l){if(k==-1&&j<=10){j+=1;return true}return k>=0&&l!=true}}))}}});return c.beforeSend.call(this,i,h)},success:function(h,j,k){if(g==true){d.css("width","90%").text("90%").attr("aria-valuenow",90).removeClass(a.join(" ")).addClass("progress-bar-success")}else{if(f==true){if(b.type(h)==="string"){if(h.indexOf("\u6210\u529f")>-1){d.data("successed",true)}else{if(h.indexOf("\u5931\u8d25")>-1){d.data("errored",true)}}}else{if(b.isPlainObject(h)){if(h.status=="success"){d.data("successed",true)}else{if(h.status=="error"){d.data("errored",true)}}}}}}var i=d.data("def");if(i){i.resolve()}c.success.call(this,h,j,k)},error:function(j,i,h){if(g==true){d.removeClass(a.join(" ")).addClass("progress-bar-danger")}else{if(f==true){d.data("errored",true)}}c.error.call(this,j,i,h)},complete:function(i,h){if(h=="success"&&g==true){d.css("width","100%").text("100%").attr("aria-valuenow",100);window.setTimeout(function(){d.closest(".progress").hide();d.css("width","0%").text("0%").attr("aria-valuenow",0).removeClass(a.join(" "))},e.progressDelay)}d.data("finished",true);c.complete.call(this,i,h)}}))}else{return b.oldAjax(e)}};b.each(["get","post"],function(c,d){b[d]=function(f,h,i,g,e){if(b.isFunction(h)){g=g||i;i=h;h=undefined}if(b.defined(e)==true&&!(b.type(e)==="string")&&b.founded(e.progressElement)==true&&b(e.progressElement).size()>0){return b.ajax(b.extend({url:f,type:d,dataType:g,data:h,success:i},e))}else{return b.oldAjax({url:f,type:d,dataType:g,data:h,success:i})}}})})(jQuery);
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
/*
 * jquery.form 插件：详情参见：jquery.form.js
 */
;(function(a){if(typeof define==="function"&&define.amd){define(["jquery"],a)}else{a((typeof(jQuery)!="undefined")?jQuery:window.Zepto)}}(function(f){var c={};c.fileapi=f("<input type='file'/>").get(0).files!==undefined;c.formdata=window.FormData!==undefined;var e=!!f.fn.prop;f.fn.attr2=function(){if(!e){return this.attr.apply(this,arguments)}var g=this.prop.apply(this,arguments);if((g&&g.jquery)||typeof g==="string"){return g}return this.attr.apply(this,arguments)};f.fn.ajaxSubmit=function(j){if(!this.length){d("ajaxSubmit: skipping submit process - no element selected");return this}var i,C,m,o=this;if(typeof j=="function"){j={success:j}}else{if(j===undefined){j={}}}i=j.type||this.attr2("method");C=j.url||this.attr2("action");m=(typeof C==="string")?f.trim(C):"";m=m||window.location.href||"";if(m){m=(m.match(/^([^#]+)/)||[])[1]}j=f.extend(true,{url:m,success:f.ajaxSettings.success,type:i||f.ajaxSettings.type,iframeSrc:/^https/i.test(window.location.href||"")?"javascript:false":"about:blank"},j);var u={};this.trigger("form-pre-serialize",[this,j,u]);if(u.veto){d("ajaxSubmit: submit vetoed via form-pre-serialize trigger");return this}if(j.beforeSerialize&&j.beforeSerialize(this,j)===false){d("ajaxSubmit: submit aborted via beforeSerialize callback");return this}var n=j.traditional;if(n===undefined){n=f.ajaxSettings.traditional}var s=[];var E,F=this.formToArray(j.semantic,s);if(j.data){j.extraData=j.data;E=f.param(j.data,n)}if(j.beforeSubmit&&j.beforeSubmit(F,this,j)===false){d("ajaxSubmit: submit aborted via beforeSubmit callback");return this}this.trigger("form-submit-validate",[F,this,j,u]);if(u.veto){d("ajaxSubmit: submit vetoed via form-submit-validate trigger");return this}var y=f.param(F,n);if(E){y=(y?(y+"&"+E):E)}if(j.type.toUpperCase()=="GET"){j.url+=(j.url.indexOf("?")>=0?"&":"?")+y;j.data=null}else{j.data=y}var H=[];if(j.resetForm){H.push(function(){o.resetForm()})}if(j.clearForm){H.push(function(){o.clearForm(j.includeHidden)})}if(!j.dataType&&j.target){var l=j.success||function(){};H.push(function(q){var k=j.replaceTarget?"replaceWith":"html";f(j.target)[k](q).each(l,arguments)})}else{if(j.success){H.push(j.success)}}j.success=function(K,q,L){var J=j.context||this;for(var I=0,k=H.length;I<k;I++){H[I].apply(J,[K,q,L||o,o])}};if(j.error){var z=j.error;j.error=function(J,k,q){var I=j.context||this;z.apply(I,[J,k,q,o])}}if(j.complete){var h=j.complete;j.complete=function(I,k){var q=j.context||this;h.apply(q,[I,k,o])}}var D=f("input[type=file]:enabled",this).filter(function(){return f(this).val()!==""});var p=D.length>0;var B="multipart/form-data";var x=(o.attr("enctype")==B||o.attr("encoding")==B);var w=c.fileapi&&c.formdata;d("fileAPI :"+w);var r=(p||x)&&!w;var v;if(j.iframe!==false&&(j.iframe||r)){if(j.closeKeepAlive){f.get(j.closeKeepAlive,function(){v=G(F)})}else{v=G(F)}}else{if((p||x)&&w){v=t(F)}else{v=f.ajax(j)}}o.removeData("jqxhr").data("jqxhr",v);for(var A=0;A<s.length;A++){s[A]=null}this.trigger("form-submit-notify",[this,j]);return this;function g(K){var L=f.param(K,j.traditional).split("&");var q=L.length;var k=[];var J,I;for(J=0;J<q;J++){L[J]=L[J].replace(/\+/g," ");I=L[J].split("=");k.push([decodeURIComponent(I[0]),decodeURIComponent(I[1])])}return k}function t(q){var k=new FormData();for(var I=0;I<q.length;I++){k.append(q[I].name,q[I].value)}if(j.extraData){var L=g(j.extraData);for(I=0;I<L.length;I++){if(L[I]){k.append(L[I][0],L[I][1])}}}j.data=null;var K=f.extend(true,{},f.ajaxSettings,j,{contentType:false,processData:false,cache:false,type:i||"POST"});if(j.uploadProgress){K.xhr=function(){var M=f.ajaxSettings.xhr();if(M.upload){M.upload.addEventListener("progress",function(Q){var P=0;var N=Q.loaded||Q.position;var O=Q.total;if(Q.lengthComputable){P=Math.ceil(N/O*100)}j.uploadProgress(Q,N,O,P)},false)}return M}}K.data=null;var J=K.beforeSend;K.beforeSend=function(N,M){if(j.formData){M.data=j.formData}else{M.data=k}if(J){J.call(this,N,M)}};return f.ajax(K)}function G(af){var L=o[0],K,ab,V,ad,Y,N,Q,O,P,Z,ac,T;var ai=f.Deferred();ai.abort=function(aj){O.abort(aj)};if(af){for(ab=0;ab<s.length;ab++){K=f(s[ab]);if(e){K.prop("disabled",false)}else{K.removeAttr("disabled")}}}V=f.extend(true,{},f.ajaxSettings,j);V.context=V.context||V;Y="jqFormIO"+(new Date().getTime());if(V.iframeTarget){N=f(V.iframeTarget);Z=N.attr2("name");if(!Z){N.attr2("name",Y)}else{Y=Z}}else{N=f('<iframe name="'+Y+'" src="'+V.iframeSrc+'" />');N.css({position:"absolute",top:"-1000px",left:"-1000px"})}Q=N[0];O={aborted:0,responseText:null,responseXML:null,status:0,statusText:"n/a",getAllResponseHeaders:function(){},getResponseHeader:function(){},setRequestHeader:function(){},abort:function(aj){var ak=(aj==="timeout"?"timeout":"aborted");d("aborting upload... "+ak);this.aborted=1;try{if(Q.contentWindow.document.execCommand){Q.contentWindow.document.execCommand("Stop")}}catch(al){}N.attr("src",V.iframeSrc);O.error=ak;if(V.error){V.error.call(V.context,O,ak,aj)}if(ad){f.event.trigger("ajaxError",[O,V,ak])}if(V.complete){V.complete.call(V.context,O,ak)}}};ad=V.global;if(ad&&0===f.active++){f.event.trigger("ajaxStart")}if(ad){f.event.trigger("ajaxSend",[O,V])}if(V.beforeSend&&V.beforeSend.call(V.context,O,V)===false){if(V.global){f.active--}ai.reject();return ai}if(O.aborted){ai.reject();return ai}P=L.clk;if(P){Z=P.name;if(Z&&!P.disabled){V.extraData=V.extraData||{};V.extraData[Z]=P.value;if(P.type=="image"){V.extraData[Z+".x"]=L.clk_x;V.extraData[Z+".y"]=L.clk_y}}}var U=1;var R=2;function S(al){var ak=null;try{if(al.contentWindow){ak=al.contentWindow.document}}catch(aj){d("cannot get iframe.contentWindow document: "+aj)}if(ak){return ak}try{ak=al.contentDocument?al.contentDocument:al.document}catch(aj){d("cannot get iframe.contentDocument: "+aj);ak=al.document}return ak}var J=f("meta[name=csrf-token]").attr("content");var I=f("meta[name=csrf-param]").attr("content");if(I&&J){V.extraData=V.extraData||{};V.extraData[I]=J}function aa(){var ar=o.attr2("target"),an=o.attr2("action"),al="multipart/form-data",ao=o.attr("enctype")||o.attr("encoding")||al;L.setAttribute("target",Y);if(!i||/post/i.test(i)){L.setAttribute("method","POST")}if(an!=V.url){L.setAttribute("action",V.url)}if(!V.skipEncodingOverride&&(!i||/post/i.test(i))){o.attr({encoding:"multipart/form-data",enctype:"multipart/form-data"})}if(V.timeout){T=setTimeout(function(){ac=true;X(U)},V.timeout)}function ap(){try{var at=S(Q).readyState;d("state = "+at);if(at&&at.toLowerCase()=="uninitialized"){setTimeout(ap,50)}}catch(au){d("Server abort: ",au," (",au.name,")");X(R);if(T){clearTimeout(T)}T=undefined}}var aq=[];try{if(V.extraData){for(var ak in V.extraData){if(V.extraData.hasOwnProperty(ak)){if(f.isPlainObject(V.extraData[ak])&&V.extraData[ak].hasOwnProperty("name")&&V.extraData[ak].hasOwnProperty("value")){aq.push(f('<input type="hidden" name="'+V.extraData[ak].name+'">').val(V.extraData[ak].value).appendTo(L)[0])}else{aq.push(f('<input type="hidden" name="'+ak+'">').val(V.extraData[ak]).appendTo(L)[0])}}}}if(!V.iframeTarget){N.appendTo("body")}if(Q.attachEvent){Q.attachEvent("onload",X)}else{Q.addEventListener("load",X,false)}setTimeout(ap,15);try{L.submit()}catch(am){var aj=document.createElement("form").submit;aj.apply(L)}}finally{L.setAttribute("action",an);L.setAttribute("enctype",ao);if(ar){L.setAttribute("target",ar)}else{o.removeAttr("target")}f(aq).remove()}}if(V.forceSync){aa()}else{setTimeout(aa,10)}var ag,ah,ae=50,M;function X(ap){if(O.aborted||M){return}ah=S(Q);if(!ah){d("cannot access response document");ap=R}if(ap===U&&O){O.abort("timeout");ai.reject(O,"timeout");return}else{if(ap==R&&O){O.abort("server abort");ai.reject(O,"error","server abort");return}}if(!ah||ah.location.href==V.iframeSrc){if(!ac){return}}if(Q.detachEvent){Q.detachEvent("onload",X)}else{Q.removeEventListener("load",X,false)}var an="success",ar;try{if(ac){throw"timeout"}var am=V.dataType=="xml"||ah.XMLDocument||f.isXMLDoc(ah);d("isXml="+am);if(!am&&window.opera&&(ah.body===null||!ah.body.innerHTML)){if(--ae){d("requeing onLoad callback, DOM not available");setTimeout(X,250);return}}var at=ah.body?ah.body:ah.documentElement;O.responseText=at?at.innerHTML:null;O.responseXML=ah.XMLDocument?ah.XMLDocument:ah;if(am){V.dataType="xml"}O.getResponseHeader=function(aw){var av={"content-type":V.dataType};return av[aw.toLowerCase()]};if(at){O.status=Number(at.getAttribute("status"))||O.status;O.statusText=at.getAttribute("statusText")||O.statusText}var aj=(V.dataType||"").toLowerCase();var aq=/(json|script|text)/.test(aj);if(aq||V.textarea){var ao=ah.getElementsByTagName("textarea")[0];if(ao){O.responseText=ao.value;O.status=Number(ao.getAttribute("status"))||O.status;O.statusText=ao.getAttribute("statusText")||O.statusText}else{if(aq){var ak=ah.getElementsByTagName("pre")[0];var au=ah.getElementsByTagName("body")[0];if(ak){O.responseText=ak.textContent?ak.textContent:ak.innerText}else{if(au){O.responseText=au.textContent?au.textContent:au.innerText}}}}}else{if(aj=="xml"&&!O.responseXML&&O.responseText){O.responseXML=W(O.responseText)}}try{ag=k(O,aj,V)}catch(al){an="parsererror";O.error=ar=(al||an)}}catch(al){d("error caught: ",al);an="error";O.error=ar=(al||an)}if(O.aborted){d("upload aborted");an=null}if(O.status){an=(O.status>=200&&O.status<300||O.status===304)?"success":"error"}if(an==="success"){if(V.success){V.success.call(V.context,ag,"success",O)}ai.resolve(O.responseText,"success",O);if(ad){f.event.trigger("ajaxSuccess",[O,V])}}else{if(an){if(ar===undefined){ar=O.statusText}if(V.error){V.error.call(V.context,O,an,ar)}ai.reject(O,"error",ar);if(ad){f.event.trigger("ajaxError",[O,V,ar])}}}if(ad){f.event.trigger("ajaxComplete",[O,V])}if(ad&&!--f.active){f.event.trigger("ajaxStop")}if(V.complete){V.complete.call(V.context,O,an)}M=true;if(V.timeout){clearTimeout(T)}setTimeout(function(){if(!V.iframeTarget){N.remove()}else{N.attr("src",V.iframeSrc)}O.responseXML=null},100)}var W=f.parseXML||function(aj,ak){if(window.ActiveXObject){ak=new ActiveXObject("Microsoft.XMLDOM");ak.async="false";ak.loadXML(aj)}else{ak=(new DOMParser()).parseFromString(aj,"text/xml")}return(ak&&ak.documentElement&&ak.documentElement.nodeName!="parsererror")?ak:null};var q=f.parseJSON||function(aj){return window["eval"]("("+aj+")")};var k=function(ao,am,al){var ak=ao.getResponseHeader("content-type")||"",aj=am==="xml"||!am&&ak.indexOf("xml")>=0,an=aj?ao.responseXML:ao.responseText;if(aj&&an.documentElement.nodeName==="parsererror"){if(f.error){f.error("parsererror")}}if(al&&al.dataFilter){an=al.dataFilter(an,am)}if(typeof an==="string"){if(am==="json"||!am&&ak.indexOf("json")>=0){an=q(an)}else{if(am==="script"||!am&&ak.indexOf("javascript")>=0){f.globalEval(an)}}}return an};return ai}};f.fn.ajaxForm=function(g){g=g||{};g.delegation=g.delegation&&f.isFunction(f.fn.on);if(!g.delegation&&this.length===0){var h={s:this.selector,c:this.context};if(!f.isReady&&h.s){d("DOM not ready, queuing ajaxForm");f(function(){f(h.s,h.c).ajaxForm(g)});return this}d("terminating; zero elements found by selector"+(f.isReady?"":" (DOM not ready)"));return this}if(g.delegation){f(document).off("submit.form-plugin",this.selector,b).off("click.form-plugin",this.selector,a).on("submit.form-plugin",this.selector,g,b).on("click.form-plugin",this.selector,g,a);return this}return this.ajaxFormUnbind().bind("submit.form-plugin",g,b).bind("click.form-plugin",g,a)};function b(h){var g=h.data;if(!h.isDefaultPrevented()){h.preventDefault();f(h.target).ajaxSubmit(g)}}function a(k){var j=k.target;var h=f(j);if(!(h.is("[type=submit],[type=image]"))){var g=h.closest("[type=submit]");if(g.length===0){return}j=g[0]}var i=this;i.clk=j;if(j.type=="image"){if(k.offsetX!==undefined){i.clk_x=k.offsetX;i.clk_y=k.offsetY}else{if(typeof f.fn.offset=="function"){var l=h.offset();i.clk_x=k.pageX-l.left;i.clk_y=k.pageY-l.top}else{i.clk_x=k.pageX-j.offsetLeft;i.clk_y=k.pageY-j.offsetTop}}}setTimeout(function(){i.clk=i.clk_x=i.clk_y=null},100)}f.fn.ajaxFormUnbind=function(){return this.unbind("submit.form-plugin click.form-plugin")};f.fn.formToArray=function(x,g){var w=[];if(this.length===0){return w}var l=this[0];var z=this.attr("id");var q=x?l.getElementsByTagName("*"):l.elements;var A;if(q&&!/MSIE [678]/.test(navigator.userAgent)){q=f(q).get()}if(z){A=f(':input[form="'+z+'"]').get();if(A.length){q=(q||[]).concat(A)}}if(!q||!q.length){return w}var r,p,o,y,m,t,k;for(r=0,t=q.length;r<t;r++){m=q[r];o=m.name;if(!o||m.disabled){continue}if(x&&l.clk&&m.type=="image"){if(l.clk==m){w.push({name:o,value:f(m).val(),type:m.type});w.push({name:o+".x",value:l.clk_x},{name:o+".y",value:l.clk_y})}continue}y=f.fieldValue(m,true);if(y&&y.constructor==Array){if(g){g.push(m)}for(p=0,k=y.length;p<k;p++){w.push({name:o,value:y[p]})}}else{if(c.fileapi&&m.type=="file"){if(g){g.push(m)}var h=m.files;if(h.length){for(p=0;p<h.length;p++){w.push({name:o,value:h[p],type:m.type})}}else{w.push({name:o,value:"",type:m.type})}}else{if(y!==null&&typeof y!="undefined"){if(g){g.push(m)}w.push({name:o,value:y,type:m.type,required:m.required})}}}}if(!x&&l.clk){var s=f(l.clk),u=s[0];o=u.name;if(o&&!u.disabled&&u.type=="image"){w.push({name:o,value:s.val()});w.push({name:o+".x",value:l.clk_x},{name:o+".y",value:l.clk_y})}}return w};f.fn.formSerialize=function(g){return f.param(this.formToArray(g))};f.fn.fieldSerialize=function(h){var g=[];this.each(function(){var m=this.name;if(!m){return}var k=f.fieldValue(this,h);if(k&&k.constructor==Array){for(var l=0,j=k.length;l<j;l++){g.push({name:m,value:k[l]})}}else{if(k!==null&&typeof k!="undefined"){g.push({name:this.name,value:k})}}});return f.param(g)};f.fn.fieldValue=function(m){for(var l=[],j=0,g=this.length;j<g;j++){var k=this[j];var h=f.fieldValue(k,m);if(h===null||typeof h=="undefined"||(h.constructor==Array&&!h.length)){continue}if(h.constructor==Array){f.merge(l,h)}else{l.push(h)}}return l};f.fieldValue=function(g,o){var j=g.name,u=g.type,w=g.tagName.toLowerCase();if(o===undefined){o=true}if(o&&(!j||g.disabled||u=="reset"||u=="button"||(u=="checkbox"||u=="radio")&&!g.checked||(u=="submit"||u=="image")&&g.form&&g.form.clk!=g||w=="select"&&g.selectedIndex==-1)){return null}if(w=="select"){var p=g.selectedIndex;if(p<0){return null}var r=[],h=g.options;var l=(u=="select-one");var q=(l?p+1:h.length);for(var k=(l?p:0);k<q;k++){var m=h[k];if(m.selected){var s=m.value;if(!s){s=(m.attributes&&m.attributes.value&&!(m.attributes.value.specified))?m.text:m.value}if(l){return s}r.push(s)}}return r}return f(g).val()};f.fn.clearForm=function(g){return this.each(function(){f("input,select,textarea",this).clearFields(g)})};f.fn.clearFields=f.fn.clearInputs=function(g){var h=/^(?:color|date|datetime|email|month|number|password|range|search|tel|text|time|url|week)$/i;return this.each(function(){var j=this.type,i=this.tagName.toLowerCase();if(h.test(j)||i=="textarea"){this.value=""}else{if(j=="checkbox"||j=="radio"){this.checked=false}else{if(i=="select"){this.selectedIndex=-1}else{if(j=="file"){if(/MSIE/.test(navigator.userAgent)){f(this).replaceWith(f(this).clone(true))}else{f(this).val("")}}else{if(g){if((g===true&&/hidden/.test(j))||(typeof g=="string"&&f(this).is(g))){this.value=""}}}}}}})};f.fn.resetForm=function(){return this.each(function(){if(typeof this.reset=="function"||(typeof this.reset=="object"&&!this.reset.nodeType)){this.reset()}})};f.fn.enable=function(g){if(g===undefined){g=true}return this.each(function(){this.disabled=!g})};f.fn.selected=function(g){if(g===undefined){g=true}return this.each(function(){var h=this.type;if(h=="checkbox"||h=="radio"){this.checked=g}else{if(this.tagName.toLowerCase()=="option"){var i=f(this).parent("select");if(g&&i[0]&&i[0].type=="select-one"){i.find("option").selected(false)}this.selected=g}}})};f.fn.ajaxSubmit.debug=false;function d(){if(!f.fn.ajaxSubmit.debug){return}var g="[jquery.form] "+Array.prototype.join.call(arguments,"");if(window.console&&window.console.log){window.console.log(g)}else{if(window.opera&&window.opera.postError){window.opera.postError(g)}}}}));
/*
 * jquery.mobile 插件：详情参见：jquery.utils.mobile.js
 */
;(function(a){a.extend(a,{isMobile:function(){var b=a(window).width();return b<=768}})}(jQuery));
