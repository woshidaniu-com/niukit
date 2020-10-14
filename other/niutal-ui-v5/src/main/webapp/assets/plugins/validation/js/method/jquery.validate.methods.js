/*
 * @discretion	: 基于jquery.validate插件的通用校验方法
 * @author    	: wandalong 
 * @version		: v1.0.0
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){

	// 验证包含中文的函数：中文字两个字节
	$.validator.addMethod("stringRangeLength", function(value, element, param) {
		var length = value.length;
		for ( var i = 0; i < value.length; i += 1) {
			if (value.charCodeAt(i) > 127) {
				length += 1;
			}
		}
		return  (length >= param[0] && length <= param[1]) || this.optional(element);
	});

	// 字符最小长度验证（一个中文字符长度为2）
	$.validator.addMethod("stringMinLength", function(value, element, param) {
		var length = value.length;
		for ( var i = 0; i < value.length; i++) {
			if (value.charCodeAt(i) > 127) {
				length++;
			}
		}
		return  (length >= param) || this.optional(element);
	});

	// 字符最大长度验证（一个中文字符长度为2）
	$.validator.addMethod("stringMaxLength", function(value, element, param) {
		var length = value.length;
		for ( var i = 0; i < value.length; i++) {
			if (value.charCodeAt(i) > 127) {
				length++;
			}
		}
		return  (length <= param) || this.optional(element);
	});
	
	// 必须以特定字符串开头验证
	$.validator.addMethod("begin", function(value, element, param) {
		var begin = new RegExp("^" + param);
		return (begin.test(value));
	});

	// 验证两次输入值是否不相同
	$.validator.addMethod("notEqualTo", function(value, element, param) {
		return value != $(param).val();
	});

	// 验证值不允许与特定值等于
	$.validator.addMethod("notEqual", function(value, element, param) {
		return value != param;
	});

	// 验证值必须大于特定值(不能等于)
	$.validator.addMethod("gt", function(value, element, param) {
		return value > param ;
	});

	// 固定值验证
	$.validator.addMethod("fixed", function(value, element, param) {
		param = $.isArray(param) ? param : [].concat(param);
		var re = new RegExp(value.replace("/","\/"),"ig"); 
		var isEquals = re.test(param.join("\/")) || re.test(param.join("\,")) || re.test(param.join("\|"));
		if(!isEquals){
			$.each(param||[],function(i,item){
				isEquals = ((item||"".toLowerCase()) == value.toLowerCase());
				return !isEquals;
			});
		}
		return isEquals || this.optional(element);
	});
	
	//filestyle插件文件选择校验
	$.validator.addMethod("fileRequest", function(value, element, param) {
		var value = $(element).val();
		if(!$.founded(value)){
			return false;
		}
		var message = $(element).data("message");
		if($.founded(message)){
			return false;
		}
		return  true;
	});
	

}(jQuery));