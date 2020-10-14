(function($) {
	/**
	 * 注册证件验证--摘自互联网。
	 * 支持证件类型 02：身份证,08:护　照,12:户口簿,04:军官证,
	 * 09:警官证,10:港澳居民来往内地通行证,11:台湾居民来往大陆通行证
	 */
	//校验身份证号码
	function checkIdCard(selectValue, varInput) {
		//判断是否为身份证
		if(selectValue == '02') {
			var ret = "";
			if(varInput == "") {
				return false;
			} else {
				ret = checkIdcard2(varInput);
			}
			if(ret.indexOf("验证通过") == -1) {
				return ret;
			}
			return false;
		} else {
			return checkOtherCard(selectValue, varInput);
		}
	}
	
	//其他证件验证
	function checkOtherCard(selectValue, varInput) {
		var flag = false;
		switch(selectValue) {
			//户口本
			case "12":
				flag = (checkIdcard2(varInput).indexOf("验证通过") == -1);
				break;
				//港澳居民来往内地通行证
			case "10":
				flag = CheckInfoUtils.checkByReg(varInput, regUrl.passForInlandReg);
				break;
				//台湾居民来往大陆通行证
			case "11":
				flag = CheckInfoUtils.checkByReg(varInput, (varInput.length == 8 ? regUrl.passForMainlandReg8 : regUrl.passForMainlandReg11));
				break;
			case "04":
				flag = !CheckInfoUtils.lenB(varInput, 5);
				break;
			case "08":
				flag = !CheckInfoUtils.lenB(varInput, 5);
				break;
			case "09":
				flag = !CheckInfoUtils.lenB(varInput, 5);
				break;
			default:
				flag = true;
				break;
		}
		return flag;
	}
	
	function checkIdcard2(idcard) {
		var Errors = new Array(
			"验证通过!",
			"请输入18位身份证号!",
			"身份证号码出生日期超出范围或含有非法字符!",
			"身份证号码校验错误!",
			"身份证地区非法!"
		);
		var area = {
			11: "北京",
			12: "天津",
			13: "河北",
			14: "山西",
			15: "内蒙古",
			21: "辽宁",
			22: "吉林",
			23: "黑龙江",
			31: "上海",
			32: "江苏",
			33: "浙江",
			34: "安徽",
			35: "福建",
			36: "江西",
			37: "山东",
			41: "河南",
			42: "湖北",
			43: "湖南",
			44: "广东",
			45: "广西",
			46: "海南",
			50: "重庆",
			51: "四川",
			52: "贵州",
			53: "云南",
			54: "西藏",
			61: "陕西",
			62: "甘肃",
			63: "青海",
			64: "宁夏",
			65: "新疆",
			71: "台湾",
			81: "香港",
			82: "澳门",
			91: "国外"
		};
		var idcard, Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		//地区检验 
		if(area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
		//身份号码位数及格式检验 
		switch(idcard.length) {
			case 18:
				//18位身份号码检测 
				//出生日期的合法性检查 
				//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9])) 
				//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8])) 
				if(parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合法性正则表达式 
				} else {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式 
				}
				if(ereg.test(idcard)) { //测试出生日期的合法性 
					//计算校验位 
					S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 +
						(parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 +
						(parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 +
						(parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 +
						(parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 +
						(parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 +
						(parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 +
						parseInt(idcard_array[7]) * 1 +
						parseInt(idcard_array[8]) * 6 +
						parseInt(idcard_array[9]) * 3;
					Y = S % 11;
					M = "F";
					JYM = "10X98765432";
					M = JYM.substr(Y, 1); //判断校验位 
					if(M == idcard_array[17]) return Errors[0]; //检测ID的校验位 
					else return Errors[3];
				} else return Errors[2];
				break;
			default:
				return Errors[1];
				break;
		}
	}
	
	var CheckInfoUtils = {
		checkByReg: function(txt, reg) {
			return reg.test(txt);
		},
		checkChineseLenContain: function(txt, num) {
			var n = 0;
			for(var i = 0, len = txt.length; i < len; i++) {
				if(regUrl.pubChineseReg.test(txt.charAt(i))) n++;
			}
			return n >= num;
		},
		checkChineseLenOnly: function(txt) {
			for(var i = 0, len = txt.length; i < len; i++) {
				if(!regUrl.pubChineseReg.test(txt.charAt(i))) return false;
			}
			return true;
		},
		checkLenContain: function(Str, num) {
			var escStr = escape(Str);
			var numI = 0;
			var escStrlen = escStr.length;
			for(var i = 0; i < escStrlen; i++)
				if(escStr.charAt(i) == '%')
					if(escStr.charAt(++i) == 'u')
						numI++;
			return Str.length + numI > num;
		},
		lenB: function(Str, num) {
			return arguments[0].replace(/[^\x00-\xff]/g, "**").length > num;
		}
	};
	
	var regUrl = {
		pubChineseReg: /[\u4E00-\u9FA5]/,
		netWorkCustNameReg: /^[\u4E00-\u9FA5]{2,}$/,
		allNumReg: /^[0-9]*$/,
		netWorkCustPassPortReg: /^(?!^\d+$)([a-zA-Z\d]{3,}|[\u4E00-\u9FA5]{2,})$/,
		passForInlandReg: /^(H|M)(\d{10}|\d{11})$/,
		passForMainlandReg11: /^\d{10}(\([A-Za-z0-9]{1}\)|[A-Za-z0-9]{1}|\uFF08[A-Za-z0-9]{1}\uFF09)$/,
		passForMainlandReg8: /^\d{8}$/,
		passPortReg: /^[0-9a-zA-Z]{6,}$/,
		emailReg: /\w+((-w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/,
		endReg: ""
	};
	
	// 其他证件的验证
	$.validator.addMethod("otherCard", function(value, element, param) {
		var type = $(param).is("select") ? $(param).find("option:selected").data("type") : $(param).data("type");
		return this.optional(element) || checkOtherCard(type, value);
	}, "请正确填写您的证件号码");

}(jQuery));