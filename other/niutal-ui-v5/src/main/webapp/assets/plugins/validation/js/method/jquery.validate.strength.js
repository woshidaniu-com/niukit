/*
 * @discretion	: 基于jquery.validate插件的密码强度校验方法
 * @author    	: wandalong 
 * @version		: v1.0.0
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){

	// bitTotal函数
	// 计算出当前密码当中一共有多少种模式
	function bitTotal(num) {
		var modes = 0;
		for ( var i = 0; i < 4; i++) {
			if (num & 1){
				modes++;
			}
			num >>>= 1;
		}
		return modes;
	}

	// CharMode函数
	// 测试某个字符是属于哪一类.
	function CharMode(iN) {
		if (iN >= 48 && iN <= 57){ // 数字
			return 1;
		}if (iN >= 65 && iN <= 90){ // 大写字母
			return 2;
		}if (iN >= 97 && iN <= 122){ // 小写
			return 4;
		}else{
			return 8; // 特殊字符
		}
	}

	// checkStrong函数
	// 返回密码的强度级别
	function checkStrong(sPW) {
		if (sPW.length < 6){
			return 0; // 密码太短
		}
		var Modes = 0;
		for (i = 0; i < sPW.length; i++) {
			// 测试每一个字符的类别并统计一共有多少种模式.
			Modes |= CharMode(sPW.charCodeAt(i));
		}
		return bitTotal(Modes);
	}
	
	
	var tipMessage = "密码强度较弱!";
	// 密码强度验证
	$.validator.addMethod("strength", function(value, element, param) {
		var isNotnulll = true;
		var S_level = checkStrong(value);
		switch (S_level) {
			case 0:{
				tipMessage = "密码长度为6~16位，至少包含数字、特殊符号、英文字母(大、小写)中的两类!";
				isNotnulll =  false;
				break;
			};
			case 1:{
				tipMessage = "密码强度较弱!"
				isNotnulll =  false;
				break;
			};
//			case 2:
//				showErrMsg("密码强度中等,请及时修改!");
//				strong = false;
//				break;
			default:{
				isNotnulll =  true;
			}
		};
		return this.optional(element) || isNotnulll;
	},function(param,element){
		return tipMessage;
	});
	
}(jQuery));