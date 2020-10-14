/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
(function($) {
	
	$.extend($.validator.messages, {
		"accept"				: $.validator.format("请选择有效的文件类型的文件（文件后缀匹配{0}）"),
		"alphanumeric"			: "只能输入字母、数字、下划线或它们的组合",
		"bankaccountNL"			: "请输入有效的荷兰银行账号（不是转账账号）最大9位数字,允许出现空格，格式如： 123456789 或 12 34 56 789",
		"bankorgiroaccountNL"	: "请输入有效的荷兰银行账号或转账账号",
		"begin"					: $.validator.format("必须是以{0}开头的字符"),
		"bic" 					: "请输入有效的商业标识代码",			
		"chrnum"				: "只能输入数字、字母或它们的组合",
		"chinese"				: "只能输入汉字",
		"cifES" 				: "请输入有效的西班牙纳税人识别码",
		"creditcardtypes" 		: "请输入有效的信用卡号",
		"creditcard"			: "请输入有效的信用卡号",
		"currency"				: "请输入有效货币符号;如：£,$,RM",
		"date"					: "请输入正确格式的日期",
		"dateITA"				: "请输入正确格式的日期 (格式为:dd/mm/yyyy)",
		"dateNL"				: "请输入有效的荷兰日期",
		"dateISO"				: "请输入正确格式的日期(ISO)，例如：2009-06-23，1998/01/22 ",
		"dateTime"				: "请输入正确格式的日期 (格式为:yyyy-mm-dd HH:mm)",
		"dateTimeSecond"		: "请输入正确格式的日期(格式为:yyy-mm-dd HH:mm:ss)",
		"decimal"				: "验证值小数位数不能超过两位",
		"digits"				: "请输入整数",
		"email"					: "请输入正确格式的电子邮件",
		"email2"				: "请输入正确格式的电子邮件",
		"equalTo"				: "两次输入的内容必须相同",//$.validator.format("请输入与{0}元素值相同的值"),
		"extension"				: $.validator.format("请输入有效的后缀"),
		"english"				: "只能输入英文字母(不区分大小写)",
		"fixed"					: function(param,element){
			return "请输入["+$.makeArray( param ).join(",")+"]中的任意值或组合";
		},
		"fileRequest"				: function(param,element){
			if(!$.founded($(element).val())){
				return "必须选择文件";
			}else{
				return $(element).data("message");
			}
		},
		"float"					: "请输入有效的小数",
		"floatNumber"			: "请输入大于0的小数；格式如：0.5",
		"fourNumber"			: "必须是4位以内非负正整数",
		"fourInteger"			: "请输入0或长度小于4位的正整数",
		"giroaccountNL"			: "请输入有效的荷兰转账账号（不是银行账号）最大7位数字",
		"gt"					: $.validator.format("输入值必须大于{0}"),
		"iban"					: "请输入有效的国际银行帐户号码(IBAN编号规定包括国别代码+银行代码+地区+账户人账户+校验码)",
		"idCard"				: "请输入有效的18位身份证号",
		"IDCard"				: "请输入拥有合法后缀名的字符",
		"integer"				: "请输入有效的整数(不包含0的正整数或负整数)",
		"ipv4"					: "请输入有效的IPv4地址；格式如：127.0.0.1",
		"ipv6"					: "请输入有效的IPv6地址",
		"lettersonly"			: "只能输入字母",
		"letterswithbasicpunc"	: "只能输入字母或符号",
		"LetterNumberUnderline"	: "请输入长度为5-16个字符以字母开头包含数字和下划线的字符",
		"max"					: $.validator.format("输入值不能大于 {0}"),
		"maxlength"				: $.validator.format("最多输入{0}个字符(汉字算一个字符)"),
		"maxWords"				: $.validator.format("最多输入{0}个的文字(不包含html标签,空格符,标点符号;汉字算一个字符)"),
		"min"					: $.validator.format("输入值不能小于 {0}"),
		"minlength"				: $.validator.format("最少输入 {0}个字符(汉字算一个字符)"),
		"minWords"				: $.validator.format("最少输入 {0}个的文字(不包含html标签,空格符,标点符号;汉字算一个字符)"),
		"mobile"				: "请输入有效的电话号码(包含固定电话和手机号码，格式为： 010-00000000|00000000|13700000000)",
		"mobileASGN"			: "请输入有效手机号码(必须是运营商已分配的号码段)或固定电话号码(格式为： 010-00000000或00000000)",
		"mobileCN"				: "请输入有效的中国手机号码(必须是运营商[移动|联通|电信]已分配的号码段)",
		"mobileCMC"				: "请输入有效的中国移动手机号码",
		"mobileCUT"				: "请输入有效的中国联通手机号码",
		"mobileCTC"				: "请输入有效的中国电信手机号码",
		"mobileNL"				: "请输入有效的荷兰移动号码",
		"mobileUK"				: "请输入有效的英国移动号码",
		"NegativeInteger"		: "请输入小于0的整数",
		"NegativeFloat"			: "请输入小于0的小数",
		"nieES"					: "请输入有效的西班牙非公民识别代码",
		"nifES"					: "请输入有效的西班牙个人征税识别代码 ",
		"notEqualTo"			: "两次输入不能相同",
		"notEqual"				: $.validator.format("输入值不允许为{0}"),
		"nowhitespace"			: "不能输入空格",
		"number"				: "请输入合法的数字(负数，小数)",
		"pattern"				: "无效的正则格式",
		"phone"					: "请输入11位有效的手机号码",
		"phoneNL"				: "请输入有效的手机号码（荷兰）",
		"phoneUK"				: "请输入有效的手机号码（英国）",
		"phoneUS"				: "请输入有效的手机号码（美国）",
		"phonesUK"				: "请输入有效的英国电话号码",
		"postalcodeNL"			: "请输入有效的荷兰邮政编码",
		"postcodeUK"			: "请输入有效的英国邮政编码",
		"PositiveInteger"		: "只能输入正整数",
		"positiveIntegerFloat"	: "只能输入正整数和正浮点数",
		"PositiveFloat"			: "请输入大于0的整数或小数",
		"positiveNumber"		: "必须是正数或0",
		"pingyin"				: "只能输入[A-Z]或[a-z]的字母或它们的组合",
		"range"					: $.validator.format("输入值必须介于 {0} 至 {1} 之间"),
		"rangelength"			: $.validator.format("请输入长度为 {0} 至 {1} 之间的字符(汉字算一个字符)"),
		"rangeWords"			: $.validator.format("Please enter between {0} and {1} words"),
		"required"				: function(param,element){
			if($(element).is("select")){
				return "必选字段";
			}else{
				return "必填字段";
			}
		},
		"require_from_group"	: $.validator.format("请至少填写这些字段中的{0}个字段"),
		"remote"				: "请修正该字段",
		"skip_or_fill_minimum"	: $.validator.format("请输入长度为 {0} 至 {1} 之间的文字(不包含html标签,空格符,标点符号;汉字算一个字符)"),
		"strippedminlength"		: $.validator.format("请输入至少 {0} 个字符."),
		"stringRangeLength"		: $.validator.format("请输入长度为 {0} 至 {1} 之间的字符(汉字算两个字符)"),
		"stringMaxLength"		: $.validator.format("最多输入 {0} 个字符(汉字算两个字符)"),
		"stringMinLength"		: $.validator.format("最少输入 {0} 个字符(汉字算两个字符)"),
		"tel"					: "请输入有效的固定电话号码(格式为： 010-00000000或00000000)",
		"ThreeNum"				: "只能是数字[0-2]的三位组合",
		"time"					: "请输入范围在00:00和23:59之间的24小时制时间(格式为: HH:mm; 例如：23:59)",
		"time12h"				: "请输入正确的12小时制时间(格式为: hh:mm am/pm; 例如：9:15 am)",
		"url"					: "请输入正确格式的网址",
		"url2"					: "请输入正确格式的网址", 
		"UnPositiveInteger"		: "只能输入非正整数（负整数 + 0）",
		"UnNegativeInteger"		: "只能输入非负整数（正整数 + 0）",
		"UnNegativeFloat"		: "只能输入非负浮点数（正浮点数 + 0）",
		"vinUS"					: "无效的美国车辆识别码（VIN）",
		"words"					: "只能输入中文、字母、下划线或组合",
		"xfPositiveInteger"		: "请输入有效的学分(非负整数或浮点数，例如*.5,*.0)",
		"zipcodeUS" 			: "无效的美国邮政编码",
		"ziprange"				: "请输入 902xx-xxxx 至 905-xx-xxxx 范围内的邮政编码 ",
		"zipCode"				: "请输入格式正确的邮政编码，如：100000",
		"zhxsPositiveInteger"	: "请输入有效的学时(正整数和正浮点数，如*.5)",
		"zpxs"					: "请输入正确的照片像素（高*宽）要求，如:144*172"
	});
	
}(jQuery));
