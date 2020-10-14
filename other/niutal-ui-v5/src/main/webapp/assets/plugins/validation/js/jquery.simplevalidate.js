/*
 * @discretion	: 考虑部分页面组件无法通过jquery.validate直接进行校验提示,特额外实现基于绑定元素指定事件的函数回调校验逻辑和提示
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	$.bootui.widget.SimpleValidate = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	
	$.bootui.widget.SimpleValidate.prototype = {
		constructor: $.bootui.widget.SimpleValidate,
		/*初始化组件参数*/
		initialize 	: function(element, options) {
			var $this = this;
			//绑定验证事件
			$.each(options.events||[],function(i,key){
				$(element).off(key+"."+options.prefix+'.widget').on(key+"."+options.prefix+'.widget',function(e){
					//判断验证结果
					var result = options.doValidated.call(this,this,e);
					var parentObj = $(this).parent().parent();
					if(result!=true){
						
						$(parentObj).removeClass(options.errorParentClass);
		    			$(parentObj).removeClass(options.warningParentClass);
		    			$(parentObj).removeClass(options.validParentClass);
		    			$(parentObj).addClass(options.errorParentClass);
						
						if(result != false && $.founded(result)){
							options.errorPlacement.call(this,options,e,result);
		    			}
		    			
					}else{
						
						$(parentObj).removeClass(options.errorParentClass);
		    			$(parentObj).removeClass(options.warningParentClass);
		    			$(parentObj).removeClass(options.validParentClass);
		    			
		    			if(result != false){
		    				options.success.call(this,options,e);
		    			}
					}
				});
			});
			
			 /*添加需要暴露给开发者的函数*/
			$.extend(true,$this,{
				// 表单对象是否验证通过
				isValid:function(){
					var isValid = false;
					$.each(events||[],function(i,key){
						isValid = $(elem).trigger(key)
						if(isValid == true){
							return false;
						}
					});
					return isValid;
				}
			});
		},
		setDefaults	: function(settings){
			$.extend($.fn.simpleValidate.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.simpleValidate.defaults;
		}
	};
	
		
	$.fn.simpleValidate = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
				
			var $this = $(this);
			var options = $.extend({}, $.fn.simpleValidate.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
			var data = $this.data(options.prefix+'.widget');
			if (!data && option == 'destroy') {return;}
			if (!data){
				 $this.data(options.prefix+'.widget', (data = new $.bootui.widget.SimpleValidate(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.simpleValidate.defaults = {
		/*版本号*/
		version:'1.0.0',
		prefix	: "simpleValidate",
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender		: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender			: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender			: $.noop,
		/*其他参数*/
		// 触发验证的事件，默认在敲击键盘和失去焦点时验证,默认:"keyup","blur"；如果希望鼠标点击时验证（一般验证checkbox,radiobox） 添加 click
		events				: ["keyup","blur"],
	    
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
		//错误提醒的方式
		errorType			: "tooltip",//title|tooltip
		
		success				: function(options,event){
			//兼容各种插件获得最终绑定tooltip的DOM元素
			if ( $.fn.tooltips && "tooltip" == String(options.errorType||"title").toLowerCase()){
				$(this).getRealElement().tooltips('destroy');
			}else{
				$(this).removeAttr("title");
			}
		},
		errorPlacement		: function(options,event,result){
			//可见才会触发气泡提示
			if ( $.fn.tooltips && "tooltip" == String(options.errorType||"title").toLowerCase() && $(this).is(":visible")){
				//绑定tooltip插件
				var options2  	= 	{
					delay			: 	0,
					html			: 	true,
					placement		:	$(this).data("placement")||"top",
					align			:	"right",
					trigger			:   "focus"
				};
				if ($(this).is(':radio') || $(this).is(':checkbox')) {
					options2["placement"] = "right";
				}
				//兼容各种插件获得最终绑定tooltip的DOM元素
				var elem = $(this).getRealElement();
				$(elem).tooltips('destroy');
				$(elem).attr({
					"title" 		: 	result
				});
				$(elem).tooltips(options2);
				$(elem).tooltips('show');
			}else{
				$(this).attr("title",result);
			}
		},
		//验证通过后的回调函数  
		doValidated		:function(e){
			return true;
		}
	};
	
	$.fn.simpleValidate.Constructor = $.bootui.widget.SimpleValidate;
	
	$.fn.errorClass = function(text,placement){
		if($.founded(text)){
			// 参数修正
	    	if($.fn.tooltips ){
	    		var parentObj = $(this).parent().parent();
				//移除样式
				$(parentObj).removeClass("has-error has-warning has-success");
				$(parentObj).addClass("has-error");
				
				//绑定tooltip插件
				var options2  	= 	{
					delay			: 	0,
					html			: 	true,
					placement		:	placement||"top",
					align			:	"right",
					trigger			:   "focus"
				};
				if ($(this).is(':radio') || $(this).is(':checkbox')) {
					options2["placement"] = "right";
				}
				//兼容各种插件获得最终绑定tooltip的DOM元素
				var element = $(this).getRealElement();
				$(element).tooltips('destroy');
				$(element).attr({
					"title" 		: 	text
				});
				$(element).tooltips(options2);
				$(element).tooltips('show');
			}else{
				$(this).attr("title",text);
			}
		}
	};
	
	$.fn.successClass = function(isTooltip){
		var parentObj = $(this).parent().parent();
		//移除样式
		$(parentObj).removeClass("has-error has-warning has-success");
		if($.fn.tooltips  && $(this).is(":visible")){
			//兼容各种插件获得最终绑定tooltip的DOM元素
			$(this).getRealElement().tooltips('destroy');
		}else {
			$(this).removeAttr("title");
		}
	};
	
}(jQuery));