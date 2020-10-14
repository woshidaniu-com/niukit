;(function($) {
	"use strict";
	//方便js调用中获得调用方法的event对象
	$.event.get = function(){
		if(!$.browser.msie){
			if (window.event) {
				return window.event;
			}
			var o = arguments.callee.caller;
			var e;
			var n = 0;
			while (o != null && n < 40) {
				e = o.arguments[0];
				if (e && (e.constructor == Event || e.constructor == MouseEvent)) {
					return $.event.fix(e);
				}

				n++;
				o = o.caller
			}
			return $.event.fix(e);

		}else{
			return $.event.fix(window.event);
		}
	};
	
	// 粘贴事件监控
	$.fn.pasteEvents = function( delay ) {
	    if (delay == undefined) delay = 10;
	    return $(this).each(function() {
	        var $el = $(this);
	        $el.on("paste", function() {
	            $el.trigger("prepaste");
	            setTimeout(function() { $el.trigger("postpaste"); }, delay);
	        });
	    });
	};
	
	/**
	 * 需要自定义按钮状态处理时需要指定 data ：
	 * 1、指定状态自动解除时间，单位：毫秒
	 * {"statusDelay":时间}
	 * 2、指定手动解除状态
	 * {"resolveType":"custom"}
	 * 
	 */
	$.each( ("click dblclick ").split(" "), function( i, name ) {
		// Handle event binding
		$.fn[ name ] = function( data, fn ) {
			fn = fn || data;
			data = $.isFunction(data) ? {} : data;
			return arguments.length > 0 ? this.on( name, null, data , function(e){
				var btn = this;
				/**
				 * 扩展元素在不解绑事件的情况下禁用click和dblclick
				 * 比如在禁用按钮不可点击采用如下设置 ：
				 * $(btn).prop("disabled",true).addClass("disabled").data("disabled",false);
				 * 即便用户在页面对元素的disabled和样式进行了修改，事件依然不会被触发：
				 * 解除禁用：
				 * $(btn).prop("disabled",false).removeClass("disabled").removeData("disabled");
				 */
				var disabled = $.data(btn,"disabled");
				if(disabled != undefined && disabled === true){
					return;
				}
				// is button
				var isButton = $(btn).is("button") || $.trim($(btn).attr("role")||"") == "button" || $(btn).hasClass("btn");
				if(isButton && $.trim($(btn).attr("role")||"") != "tab"){
					$.when($.Deferred(function(dtd){
						$(btn).disabled(); 
						if($.isFunction(fn)){
							fn.call(btn,e);
						}
						
						//auto,custom
						if(data["resolveType"] == "custom"){
							$(btn).off("btn:enabled").on("btn:enabled",function(){
								// 改变deferred对象的执行状态为：已完成
								dtd.resolve();
							});
						}else{
							var delay = parseInt(data["statusDelay"] || 800);
							//防止回调函数是异步代码时，按钮状态快速恢复，这里添加延时
							window.setTimeout(function(){
								// 改变deferred对象的执行状态为：已完成
								dtd.resolve();
							}, delay);
						}
					}).promise()).always(function(){
						$(btn).enabled();
					});
				}else{
					if($.isFunction(fn)){
						fn.call(btn,e);
					}
				}
			}) : this.trigger( name );
		};
	});
	
	$.fn.triggerEvent =  function( type, data , callback ) {
		return this.each(function() {
			jQuery.event.trigger( type, data, this );
			if($.isFunction(data)){
				callback = data;
			}
			if($.isFunction(callback)){
				callback.call(this,type, data);
			}
		});
	};

})(jQuery);