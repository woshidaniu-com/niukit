;(function($){
	
	/*
		===data-api接口===：	
		
		data-max-selected	:	多选时最多可选项目数
		data-threshold		:	超出多少个元素时提供检索功能
		data-text-single	:	单选情况未选时的提示信息
		data-text-multiple	:	多选情况未选时的提示信息
		data-text-nomatch	:	使用检索功能时没有匹配结果时的提示信息
		
		例如：
		data-max-selected="10" 
		data-threshold="10" 
		data-text-single="选择一项"
		data-text-multiple=	"至少选择一项" 
		data-text-nomatch="没有匹配结果"
	*/
		
	// select自动美化脚本
	$(document).off('chosen.data-api', '.chosen-select').on('chosen.data-api', '.chosen-select', function (event) {
		var element = this;
		if(event.target = element && $(element).is("select")){
			
			var _max_selected =  $(element).data("max-selected") || 10;
			var _threshold =  $(element).data("threshold") || 10;
			var _text_single =  $(element).data("text-single") || $.i18n.chosen["text-single"];
			var _text_multiple =  $(element).data("text-multiple") || $.i18n.chosen["text-multiple"];
			var _text_nomatch =  $(element).data("text-nomatch") || $.i18n.chosen["text-nomatch"];
			
			$(element).chosen({
				//width						: $(this).outerWidth(),
				allow_single_deselect		: false,
				search_contains				: true,
				display_disabled_options	: false,
				disable_search_threshold	: _max_selected,
				//当select为多选时，最多选择个数
				max_selected_options		: _threshold,
				placeholder_text_single		: _text_single,
				placeholder_text_multiple	: _text_multiple,
				no_results_text				: _text_nomatch
			});
			
			
			var chosen = $(element).data('chosen');
			if(chosen){
				
				var bootbox_body = $(element).closest(".bootbox-body");
				
				//如果chosen绑定成功，则给当前元素的父级别绑定尺寸变化监听
				$(element).resize(function(e){
					$(element).trigger("chosen:resized");
				}).change(function(){
					$(this).focus().blur();
				});
				
				
				function _resetPosition(){
					

					//当前Chosen组件向上找到滚动条区域
					var mCustomScrollBox = bootbox_body.children(".mCustomScrollBox");
					//滚动条区域存在，表示当前元素位于模拟滚动条内
					if(mCustomScrollBox.size() == 1){
						 
						//可滚动的区域
						var mCSB_container = mCustomScrollBox.children(".mCSB_container");
						//可滚动的区域高度
						var _scrollHeight = ($.fn.actual ? mCSB_container.actual("innerHeight") : mCSB_container.innerHeight(true)) || mCSB_container.height();
						//Chosen内容区域
						var chosen_single = chosen.container.find("a.chosen-single");
						var chosen_drop = chosen.container.find("div.chosen-drop");
						
						//Chosen内容区域高度
						var _singleHeight = ($.fn.actual ? chosen_single.actual("outerHeight") : chosen_single.outerHeight(true)) || chosen_single.height();
						var _realHeight = ($.fn.actual ? chosen_drop.actual("outerHeight") : chosen_drop.outerHeight(true)) || chosen_drop.height();
						//Chosen内容区域距离可视区域的顶部距离
						var _top = (chosen_single.offset().top + _singleHeight - mCustomScrollBox.offset().top);
						//剩下用于容纳Chosen组件的空间高度
						var _bottom = _scrollHeight - Math.abs(parseInt(mCSB_container.css("top"))) - _top;
						//剩下用于容纳Chosen组件的空间高度不足以容纳Chosen组件
						console.log("_scrollHeight ： " + _scrollHeight );
						console.log("_top ： " + _top );
						console.log("_bottom ： " + _bottom );
						console.log("_realHeight ： " + _realHeight );
						
						if( _bottom < _realHeight) {
							
							console.log("top  " );
							
							chosen_drop.css({"top" : "-" +  _realHeight + "px"})
							if(!chosen.container.hasClass("chosen-container-btu")){
								chosen.container.addClass("chosen-container-btu");
							}
						}else{
							console.log("bottom  " );
							
							chosen_drop.css({"top" : "100%"})
							chosen.container.removeClass("chosen-container-btu");
						}
				    	
					}
					
					
				}
				
				function _keepPosition(){
					

					//当前Chosen组件向上找到滚动条区域
					var mCustomScrollBox = bootbox_body.children(".mCustomScrollBox");
					//滚动条区域存在，表示当前元素位于模拟滚动条内
					if(mCustomScrollBox.size() == 1){
						 
						//Chosen内容区域
						var chosen_drop = chosen.container.find("div.chosen-drop");
						//Chosen内容区域高度
						var _realHeight = ($.fn.actual ? chosen_drop.actual("outerHeight") : chosen_drop.outerHeight(true)) || chosen_drop.height();
						 
						console.log("_realHeight ： " + _realHeight );
						
						if( chosen.container.hasClass("chosen-container-btu")) {
							
							console.log("top  " );
							
							chosen_drop.css({"top" : "-" +  _realHeight + "px"})
						}else{
							console.log("bottom  " );
							
							chosen_drop.css({"top" : "100%"})
							chosen.container.removeClass("chosen-container-btu");
						}
				    	
					}
					
					
				}
				
		/*		chosen.container.bind('touchstart.chosen', function(evt) {
		    		_resetPosition();
					console.log("touchstart.chosen" );
		    	});
				
				chosen.container.bind('touchend.chosen', function(evt) {
					_keepPosition();
					console.log("touchend.chosen" );
		    	});
				
				chosen.container.bind('mousedown.chosen', function(evt) {
		    		_resetPosition();
					console.log("mousedown.chosen" );
		    	});
				
				chosen.container.bind('mouseup.chosen', function(evt) {
					_keepPosition();
					console.log("mouseup.chosen" );
		    	});
				
				chosen.search_results.bind('touchstart.chosen', function(evt) {
					console.log("touchstart.chosen" );
		    	});
				
				chosen.search_results.bind('mousedown.chosen', function(evt) {
					console.log("mousedown.chosen" );
		    	});*/
			}
		}
	});
	
}(jQuery));