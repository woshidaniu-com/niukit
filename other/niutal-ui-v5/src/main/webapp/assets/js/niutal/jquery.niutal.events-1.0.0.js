;(function($) {
	
	//阻止浏览器默认行为脚本
	$(document).off('keydown.data-api').on('keydown.data-api', '*', function (e) {
		var event = $.event.fix(e);
		//处理删除键导致的提交问题
		if(event.keyCode == 8){
			//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外    
			if($(this).is('input')||$(this).is('textarea')){
				//当敲Backspace键时，事件源类型为密码或单行、多行文本的，并且readOnly属性为true或disabled属性为true的，则退格键失效
				if ( $(this).prop("readonly") == true || $(this).prop("disabled") == true  ) {
					//取消浏览器默认行为
					event.preventDefault();
				}
			}else{
				//当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效 
				//取消浏览器默认行为
				event.preventDefault();
				
			}
			//取消事件冒泡
			event.stopPropagation();
		}else if( event.keyCode == 13){
			if($(this).is('input') || $(this).is('textarea')){
				//兼容jgGrid页码框回车事件；并阻止其他的浏览器默认事件
				if(!$(event.target).hasClass("ui-pg-input")){
					//取消浏览器默认行为
					event.preventDefault();
				}
				//文本框输完按回车键自动查询
				if($("form.sl_all_form").size() > 0 && jQuery.contains($("form.sl_all_form")[0],event.target)){
					//点击查询
					$("#search_go").trigger("click");
				}
			}
		}else if( event.keyCode == 27){
			//esc 按键
			//取消浏览器默认行为
			event.preventDefault();
			//取消事件冒泡
			event.stopPropagation();
		}
	})
	//语言版本切换
	.off("change","#localChange").on("change","#localChange",function(){
		$.ajaxSetup({async:false});
		$.post( _path + "/xtgl/init_changeLocal.html",{"language":$(this).val()} ,function(responseText){
			if(1 == parseInt(responseText)){
				if($("#topButton").size() > 0){
					$("#topButton").click(); 
				}else{
					window.location.reload();
				}
			}
		},'json');
		$.ajaxSetup({async:true});
	});
	/* //每秒中做一次垃圾回收.IE可控制回收,FF过几十秒自动回收一次,不用调度,看效果要等  
    window.setInterval(function gc(){  
        if(document.all){ CollectGarbage();
        	$(document.body).append(1);
        }
    }, 1000);  */
	
	// 使用
	/*$("input[type='text']").on("postpaste", function() {
	    // code...
	}).pasteEvents();
	*/
	
	/*
	 * 
	 * 在当用户离开页面时，会发生 unload 事件。
	 * 具体来说，当发生以下情况时，会发出 unload 事件：
	 * 	
	 * 	点击某个离开页面的链接 
	 * 	在地址栏中键入了新的 URL 
	 * 	使用前进或后退按钮 
	 * 	关闭浏览器 
	 * 	重新加载页面 
	 * 
	 * 
	$(window).unload(function(){
		
		
	});*/
})(jQuery);
