/*
 * @discretion	: default messages for the jQuery filehandle plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	$.filehandle = function(files,options){
		var $this = this;
		if (files){
			options = $.extend( true ,{}, $.filehandle.defaults, ((typeof options == 'object' && options) ? options : {}));
			if(files){
				var selectedCount = 0;
				var total = 0;
				var isPass = true;
				var messages = $.filehandle.messages;
				//循环file数组
				$.each(files||[],function(i,file){
					// 筛选允许的文件
					if ($.fileMatch(options.fileType,file.name,file.type)) {
						total += (file.fileSize||file.size);
						selectedCount += 1;
					}else{
						isPass = false;
						options.handleInvalidType.call($this,file,$.filehandle.format(messages["needFileType"],options.fileType));
						return false;
					};
				});
				//通过文件类型检查才会继续
				if(isPass){
					//判断文件个数
					if(selectedCount > options.maxCount){
						options.handleOverMaxCount.call($this,$.filehandle.format(messages["overMaxCount"],options.maxCount));
						return false;
					}
					//判断总大小是否超过限制
					if(total > $.capacity(options.maxTotal)){
						options.handleOverTotal.call($this,$.filehandle.format(messages["overMaxTotal"],options.maxTotal));
						return false;
					}
					$.each(files||[],function(i,file){
						// 单个文件超过最大限制:默认10M
						if((file.fileSize||file.size) > $.capacity(options.maxSize)){
							return options.handleOverSize.call($this,file,$.filehandle.format(messages["overMaxSize"],options.maxSize));
							return false;
						}else{
							//处理拖拽事件
							options.handleFile.call($this,file,$.filehandle.format(messages["successTip"],[selectedCount,$.capacity(total,"KB")]),{
								"selectedCount"	: selectedCount,
								"total"			: total
							});
						}
					});
				}
			}
		}
	};
	
	$.filehandle.format =  function(source,params) {
		if ( arguments.length == 1 ) 
			return function() {
				var args = $.makeArray(arguments);
				args.unshift(source);
				return source.apply( this, args );
			};
		if ( arguments.length > 2 && params.constructor != Array  ) {
			params = $.makeArray(arguments).slice(1);
		}
		if ( params.constructor != Array ) {
			params = [ params ];
		}
		$.each(params, function(i, n) {
			source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
		});
		return source;
	};
	
	$.filehandle.defaults = {
		// 可选择文件类型，默认全部类型；多个用;分割，如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
		fileType 		: '*.*',
		// 单个文件最大:默认10M
		maxSize 		: "10M",
		// 所有文件总共大小:默认100MB；换算关系： 1GB=1024MB; 1MB=1024KB; 1KB=1024BB（字节）; 1B = 8Byte
		maxTotal 		: "100MB",
		// 最大上传文件总数限制
		maxCount 		: 30,
		// 单个文件不匹配要求文件类型回调
		handleInvalidType	: function(file,message){},
		// 文件总个数超出总限制回调
		handleOverMaxCount	: function(message){},
		// 文件大小超出总限制回调
		handleOverTotal 	: function(message){},
		// 单个文件大小超出限制回调：该函数需要返回true|false，决定是否继续之后的逻辑
		handleOverSize 		: function(file,message){},
		// 处理拖拽事件中每个文件的回调函数
		handleFile			: function(file,message,options){}
	};
	

}(jQuery));
