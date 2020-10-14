/*
 * @discretion	: 基于 jQuery Event Delegate 原理实现的事件驱动的通用组件
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($) {
	
	"use strict";
	
	/* 委托清除事件：实现自动绑定清除:触发按钮的click事件
	 * 
	 * 在自动绑定的元素上添加data-toggle="clearfix" data-target="#yxkcmc,#yxkch"
	 *  
	 * */
	$(document).off('click.data-api', '[data-toggle*="clearfix"]').on('click.data-api', '[data-toggle*="clearfix"]', function (event) {
		if(event.currentTarget = this){
			var target = $(this).data("target");
			if($.trim(target).length > 0 ){
				$(target).each(function() {
					var t = this.type, tag = this.tagName.toLowerCase();
					if ( (t == 'hidden' && (tag == 'input'||tag == 'textarea')) ||  t == 'text' || t == 'password' || tag == 'textarea') {
						this.value = '';
					}
					else if (t == 'checkbox' || t == 'radio') {
						this.checked = false;
					}
					else if (tag == 'select') {
						this.selectedIndex = -1;
					}
				});
			}
		}
	})
	//只能输入参数自动绑定脚本
	.off('keyup.data-api', '[data-toggle*="float"]').on('keyup.data-api', '[data-toggle*="float"]', function (e) {
		if($.trim(this.value).length>0){
			//只能输入数字和.
			this.value = this.value.replace(/[^\d|.]/g,'')||"";
			//处理连续输入多个.
			this.value = this.value.replace(/\.+/g,'.')||"";
			
			var firstIndex = this.value.indexOf('.');
			//以.开始自动在前方添加0
			if(firstIndex == 0){
				this.value = '0'+this.value;//0.
			}
			if( firstIndex > 0){
				
				//判断小数点后的内容
				var substr = this.value.substring(this.value.indexOf('.'),this.value.length);
				if($.trim(substr).length>0){
					var lastIndex	= this.value.lastIndexOf(".");
					if( firstIndex != lastIndex ){
						this.value = this.value.substr(0,lastIndex);
					}
					//获取最小值
					if($.defined($(this).data("mined"))){
						var min = parseFloat($(this).data("mined")||0);
						if(parseFloat(this.value) < min){
							this.value = min;
						}
					}
					//获取最大值
					if($.defined($(this).data("maxed"))){
						var max = parseFloat($(this).data("maxed")||0);
						if(parseFloat(this.value) > max){
							this.value = max;
						}
					}
					//获取默认保留位数
					if($.defined($(this).data("fixed"))){
						var fixed = parseInt($(this).data("fixed")||0);
						if($.trim(substr).length>fixed){
							this.value = Number(this.value).toFixed(fixed);
						}
					}
				}
			}else{ 
				//获取最大值
				if($.defined($(this).data("maxed"))){
					var max = parseFloat($(this).data("maxed")||0);
					if(parseInt(this.value) > max){
						var length = $.trim(this.value).length;
						this.value = $.trim(this.value).substring(0,(length>1?length-1:0));
					}
				}
				//获取最小值
				if($.defined($(this).data("mined"))){
					var min = parseFloat($(this).data("mined")||0);
					if(parseInt(this.value) < min){
						this.value = min;
					}
				}
				this.value = Number(this.value).toFixed(0);
			}
			
		}
	}).on('blur.data-api', '[data-toggle*="float"]', function (e) {
		if($.trim(this.value).length>0){
			var firstIndex = this.value.indexOf('.');
			//以.开始自动在前方添加0
			if(firstIndex == (this.value.length - 1)){
				this.value = this.value.substr(0,firstIndex);
			}
			this.value = Number(this.value);
		}
	})
	//阻止浏览器默认行为脚本
	.off('keydown.data-api').on('keydown.data-api', '*', function (e) {
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
				if($(this).hasClass("ui-pg-input")){
					//取消浏览器默认行为
					event.preventDefault();
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
	//解决KindEditor编辑器不可编辑问题
	.off("focus",".ke-input-text").on("focus",".ke-input-text",function(){
		return false;
	});
	 
})(jQuery);
