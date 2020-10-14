/*
 * @discretion	: default messages for the jQuery FileWrap plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
 		data-widget			:	元素自动引入组件标识：该组件使用 data-widget="filewrap"
 		data-width			:	组件宽度
		data-height			：	组件高度
		data-placeholder	:	组件显示文字
		data-fileType 		: '*.*',	可选择文件类型，默认全部类型；多个用;分割，如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
	 	data-maxSize 		: 1024 * 10,		单个文件最大:默认10M
	 	data-maxTotal 		: 1024 * 1024 * 100, 所有文件总共大小:默认100M；换算关系： 1G=1024M; 1M=1024K; 1K=1024B（字节）; 1M = 1024 * 1024 K
	 	data-maxCount 		: 30,				最大上传文件总数限制
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== FILEWRAP CLASS DEFINITION ====================== */
	$.bootui.widget.FileWrap = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		//try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		//} catch (e) {
		//	options.errorRender.call(this,e);
		//}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.FileWrap.prototype = {
		constructor: $.bootui.widget.FileWrap,
		/*初始化组件参数*/
		initialize : function(element, options) {
			var $this = this; 
			this.$element  = $(element);
			this.options   = options;
			
			if (!$(element).is(":file")) {
				throw new Error(' Current Element is not a file input ! ');
			} else {
				//ie浏览器
				if($.browser.msie===true && $.browser.version < 9){
					return;
				}
				//初始化成功的标识
				$this.initializedFlag = true;
				var container =  $(options.template);
				var fileInput = $(container).find(".file-input");
				var placeholder = options.placeholder || $(element).attr("placeholder")||$(element).attr("title")||"浏览...";
				$(fileInput).append("<label>"+placeholder+"</label>");
				$(container).css({
					//宽度
					"width"		: options.width||$(element).parent().innerWidth(),
					//最小高度
					"height"	: options.height||$(element).parent().innerHeight()
				});
				
				var tipElement = $(container).find("p.tips");
				if(options.message && $.trim(options.message).length > 0){
					$(tipElement).append( ($.isFunction(options.message) ? options.message.call(this) : options.message.toString()));
				}
				
				var messages = $.fn.filewrap.messages;
				var newElement = $(element).clone(true,true);
				$(newElement).change(function(e) {
					$(tipElement).removeClass("error");
					// 获取文件列表对象:这里优先使用 File API 并兼任旧的读取方式
					var files = $(e.target).getFiles() || e.dataTransfer.files;
					// 处理选择的文件
					$.filehandle(files,{
						// 可选择文件类型，默认全部类型；多个用;分割，如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
						fileType 		: options.fileType||'*.*',
						// 单个文件最大:默认10M
						maxSize 		: options.maxSize,
						// 所有文件总共大小:默认100MB；换算关系： 1GB=1024MB; 1MB=1024KB; 1KB=1024BB（字节）; 1B = 8Byte
						maxTotal 		: options.maxTotal,
						// 最大上传文件总数限制
						maxCount 		: options.maxCount,
						// 单个文件不匹配要求文件类型回调
						handleInvalidType	: function(file,message){
							$(tipElement).empty().append(message).addClass("error");
							$(newElement).clearFile();
						},
						// 文件总个数超出总限制回调
						handleOverMaxCount	: function(message){
							$(tipElement).empty().append(message).addClass("error");
							$(newElement).clearFile();
						},
						// 文件大小超出总限制回调
						handleOverTotal 	: function(message){
							$(tipElement).empty().append(message).addClass("error");
							$(newElement).clearFile();
						},
						// 单个文件大小超出限制回调：该函数需要返回true|false，决定是否继续之后的逻辑
						handleOverSize 		: function(file,message){
							$(tipElement).empty().append(message).addClass("error");
							$(newElement).clearFile();
						},
						// 每个文件的回调函数
						handleFile			: function(file,message,options){
							$(tipElement).empty().append(message);
						}
					});
				});
				
				$(fileInput).append(newElement);
				//替换样式
				$(element).replaceWith(container);
			}
		},
		initialized : function(){
			return this.initializedFlag == true;
		},
		setDefaults	: function(settings){
			$.extend($.fn.filewrap.defaults, settings );
		},
		getDefaults	: function(){
			return $.fn.filewrap.defaults;
		}
	};
	
	/* FILEWRAP PLUGIN DEFINITION  */
	
	$.fn.filewrap = function(option){
		if (typeof option == 'string'){
			var $this = $(this[0]), data = $this.data('filewrap');
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
				var $this = $(this), data = $this.data('filewrap');
				if (!data){
					var options = $.extend( true ,{}, $.fn.filewrap.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
					$this.data('filewrap', (data = new $.bootui.widget.FileWrap(this, options)));
				}
			});
		}
	};
	
	$.fn.filewrap.defaults = {
		/*版本号*/
		version			: '1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop,
		/*其他参数*/
		//宽度
		width			: null,
		//高度
		height			: "80px",
		//显示的文字
		placeholder		: "浏览...",
		//提示信息
		message			: null,
		// 可选择文件类型，默认全部类型；多个用;分割，如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
		fileType 		: '*.*',
		// 单个文件最大:默认10M
		maxSize 		: "10M",
		// 所有文件总共大小:默认100MB；换算关系： 1GB=1024MB; 1MB=1024KB; 1KB=1024BB（字节）; 1B = 8Byte
		maxTotal 		: "100MB",
		// 最大上传文件总数限制
		maxCount 		: 30,
		template		: '<div class="filewrap"><div class="placeholder"><div class="wrap"><div class="file-input"></div><p class="tips"></p></div></div></div>'
	};
	
	$.fn.filewrap.Constructor = $.bootui.widget.FileWrap;
		

	/*============== FileWrap DATA-API  ==============*/
	
	$(document).off('wrap.filewrap.widget.data-api', '[data-widget*="filewrap"]').on('wrap.filewrap.widget.data-api', '[data-widget*="filewrap"]', function (event) {
		if(event.currentTarget = this){
			if ($(this).is(":file")) {
				$(this).filewrap();
			}
		}
	});
	

}(jQuery));
