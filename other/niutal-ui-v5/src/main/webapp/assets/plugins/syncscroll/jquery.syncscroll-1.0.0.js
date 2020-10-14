/*
 * @discretion	: default messages for the jQuery syncscroll plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
		data-toggle	：	用于绑定组件的引用
 		data-widget	:	组件元素上绑定的参数 {}
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	/*====================== SyncScroll CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.SyncScroll = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		//try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		//} catch (e) {
			//options.errorRender.call(this,e);
		//}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.SyncScroll.prototype = {
		constructor: $.bootui.widget.SyncScroll,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
			if(!options.reference || $(options.reference).size() == 0){
				throw new Error(" reference must be a document element ! ");
			}
		
			var $this = this; 
			this.$element  	 = $(element);
			this.options   	 = options;
			
			function getUID(prefix) {
				do {
				  	prefix += ~~(Math.random() * 1000000);
			  	}while (document.getElementById(prefix));
			  	return prefix;
			};
			
			this.syncscroll_uid = getUID("syncscroll");
			
			
			if($(options.reference).innerWidth() < $(element).outerWidth() ){
				
				
				var $container = $(options.container);
				
				var offset = $(options.reference).offset();
				alert($(options.reference).innerWidth());
				$($container).attr("id",this.syncscroll_uid).css({
					"width"		:	$(options.reference).innerWidth(),
					"position"	: 	"fixed",
					"left"		: 	offset.left,
					"z-index"	:	options.z_index||$(options.reference).css("z-index"),
					"bottom"	: 	0
				});
				
				var children = $($container).children("div:eq(0)");
				$(children).width($(element).innerWidth());
				
				$($container).appendTo("body").show();
				//判断滚动模拟目标是否滚动
				$(options.reference).scroll(function(){
				    $($container).scrollTop($(this).scrollTop());
				    $($container).scrollLeft($(this).scrollLeft());
				});
				$($container).scroll(function(){
				    $(options.reference).scrollTop($(this).scrollTop());
				    $(options.reference).scrollLeft($(this).scrollLeft());
				});
			}
			
		},
		destroy : function () {alert(2222);
			this.$element.off('syncscroll.data-api').removeData("bootui.syncscroll");
			$("#"+this.syncscroll_uid).remove();
		},
		setDefaults	: function(settings){
			$.extend($.fn.syncscroll.defaults, settings );
		}
	};
	
	/* SyncScroll PLUGIN DEFINITION  */
	
	/*
	 * jQuery原型上自定义的方法
	 */
	$.fn.syncscroll = function(option){
		if (typeof option == 'string'){
			var $this = $(this[0]), data = $this.data('bootui.syncscroll');
			if (!data && option == 'destroy') {return;}
			if (data){
				//处理后的参数
				var args = $.grep( arguments || [], function(n,i){
					return i >= 1;
				});
				//调用函数
				var result = data[option].apply(data, [].concat(args || []) );
				//返回调用函数返回结果或者
				return (typeof result != 'undefined') ? result : $this;
			}
		}else{
			return this.each(function () {
				var $this = $(this), data = $this.data('bootui.syncscroll');
				if (!data){
					var options = $.extend( true ,{}, $.fn.syncscroll.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
					$this.data('bootui.syncscroll', (data = new $.bootui.widget.SyncScroll(this, options)));
				}
			});
		}
	};
	
	$.fn.syncscroll.defaults = {
		/*版本号*/
		version		: '1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender	: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender	: $.noop, 
		/*其他参数*/
		delay		: 500,
		"z_index"	: null,
		"reference"	: null,
		"container"	:'<div class="col-sm-12 col-md-12 " style="line-height: 16px;height: 16px;overflow-x: scroll;"><div style="padding: 16px;line-height: 16px;">&nbsp;</div></div>'
	};

	$.fn.syncscroll.Constructor = $.bootui.widget.SyncScroll;
		

	

}(jQuery));
