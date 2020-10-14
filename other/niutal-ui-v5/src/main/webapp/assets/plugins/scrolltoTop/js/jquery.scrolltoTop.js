/**
 * @discretion	: default messages for the jQuery scrolltoTop plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/scrolltoTop-1.0.0.css
 				  3.引用效果的具体js代码文件 jquery.scrolltoTop-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").scrolltoTop({
							
						});
						
					});
					</script>
 */
;(function($){
	
	/*
 		===data-api接口===：	
		data-toggle	：	用于绑定组件的引用
 		data-widget	:	组件元素上绑定的参数 {}
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	/*====================== ScrollToTop CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.ScrollToTop = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		//try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		//} catch (e) {
			//options.errorRender.call(this,e);
		//}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.ScrollToTop.prototype = {
		constructor: $.bootui.widget.ScrollToTop,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
			this.$element  = $(element);
			
			$(document.body).prepend('<div id="scrolltoTop" class="scrolltoTop"><a>返回</a></div>');
			
			var $top = $("#scrolltoTop");
			var $ta = $("#scrolltoTop a");
			$(element).scroll(function(){
				var scrolltop = $(this).scrollTop();		
				if(scrolltop >=options.showHeight){				
					$top.show();
				}else{
					$top.hide();
				}
			});	
			$ta.hover(function(){ 		
				$(this).addClass("cur");	
			},function(){			
				$(this).removeClass("cur");		
			});	
			$top.click(function(){
				$("html,body").animate({scrollTop: 0}, options.speed);	
			});
		},
		destroy : function () {
			$(this.$element).unbind().removeData('widget.scrolltoTop');
			$("#scrolltoTop").remove();
		},
		setDefaults	: function(settings){
			$.extend($.fn.scrolltoTop.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.scrolltoTop.defaults;
		}
	};
	
	/* ScrollToTop PLUGIN DEFINITION  */
	
	/*
	 * jQuery原型上自定义的方法
	 */
	$.fn.scrolltoTop = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this), data = $this.data('widget.scrolltoTop');
			if (!data && option == 'destroy') {return;}
			var options = $.extend( true ,{}, $.fn.scrolltoTop.defaults, $this.data(),((typeof option == 'object' && option) ? option : {}));
			if (!data){
				 $this.data('widget.scrolltoTop', (data = new $.bootui.widget.ScrollToTop(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.scrolltoTop.defaults = {
		/*版本号*/
		version:'1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop, 
		/*其他参数*/
		showHeight : 150,
		speed : 1000
	};

	$.fn.scrolltoTop.Constructor = $.bootui.widget.ScrollToTop;
		

	

}(jQuery));
