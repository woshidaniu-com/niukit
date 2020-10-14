/*
 * @discretion	: 业务框架基于jquery.validate插件的校验方法
 * @author    	: wandalong 
 * @version		: v1.0.0
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){

	// 唯一性验证
	$.validator.addMethod("unique", function(value, element, param) {
		//判断是否可选验证，实际上是提前验证是否必填 
		if ( this.optional(element) ) {
			return "dependency-mismatch";
		}
		if(!$.isArray(param)){
			throw new Error(" 参数必须是数组! ");
		}
		if($.trim(value).length==0){
			return false;
		}
		var paramMap = {
			"filed_name"		: $(element).data("namemapper") || $(element).attr("name"),
			"filed_value"		: value,
			"old_filed_value"	: "",
			"table"				: param[0]||""
		};
		if(param.length>=2){
			paramMap["old_filed_value"] = param[1]||"";
		}
		var isUnique = false;
		$.ajax({
			type	: "POST",
		   	url		: _path+"/xtgl/validate_cxUnique.html",
		   	async	: false ,
		   	data	: paramMap,
			success	: function(data){
				if(parseInt(data||"0") == 1){
					isUnique = true;
				}else{
					isUnique = false;
				}
			}
		});
		return isUnique ;
	}, function(param,element){
		if($.trim($(element).val()).length==0){
			return "不能为空!";
		}else{
			if(param.length==3){
				return param[2];
			}else{
				return "已经存在；违反唯一约束!";
			}
		}
	});
	
	/*
	 * 组合唯一性验证
	 * 
	 * multiUnique:['V001',['#id1','#id2'],['001','002'],'违反唯一!']
	 * 
	 * */ 
	$.validator.addMethod("multiUnique", function(value, element, param) {
		//判断是否可选验证，实际上是提前验证是否必填 
		if ( this.optional(element) ) {
			return "dependency-mismatch";
		}
		if(!$.isArray(param)){
			throw new Error(" 参数必须是数组! ");
		}
		if(param.length<=1){
			throw new Error(" 数组至少两个参数! ");
		}
		var isNotEmpty = true;
		if($.isArray(param[1])){
			$.each(param[1],function(i,selector){
				if($.trim($(selector).val()).length<1){
					isNotEmpty = false;
					return false;
				}
			})
		}else{
			if($.trim($(param[1]).val()).length<1){
				isNotEmpty = false;
			}
		}
		if(!isNotEmpty){
			return true;
		}
		var paramMap = {
			"table":param[0]||""
		};
		//如果是多个级联关系，则是数组
		if($.isArray(param[1])){
			$.each(param[1],function(i,selector){
				if($(selector).founded()){
					paramMap["filed_list["+i+"].key"] 	= $(selector).data("namemapper") || $(selector).attr("name");
					paramMap["filed_list["+i+"].value"] = $(selector).val();
				}
			})
		}else{
			if($(param[1]).founded()){
				paramMap["filed_list[0].key"] = $(param[1]).data("namemapper") || $(param[1]).attr("name");
				paramMap["filed_list[0].value"] = $(param[1]).val();
			}
		}
		
		if(param.length==4){
			if($.isArray(param[1])&&$.isArray(param[2])){
				if(param[1].length!=param[2].length){
					throw new Error(" 验证元素个数与原始值个数不相同! ");
				}
				$.each(param[1],function(i,selector){
					if($(selector).founded()){
						paramMap["old_filed_list["+i+"].key"] = $(selector).data("namemapper") || $(selector).attr("name");
						paramMap["old_filed_list["+i+"].value"] = param[2][i];
					}
				})
			}else{
				if($(param[2]).founded()){
					paramMap["old_filed_list[0].key"] = $(param[2]).data("namemapper") || $(param[2]).attr("name");
					paramMap["old_filed_list[0].value"] = param[2][0];
				}
			}
		}
		var isUnique = false;
		$.ajax({
			type	: "POST",
		   	url		: _path+"/xtgl/validate_cxUnique.html",
		   	async	: false ,
		   	data	: paramMap,
			success	: function(data){
				if(parseInt(data||"0") == 1){
					isUnique = true;
				}else{
					isUnique = false;
				}
			}
		});
		return isUnique ;
	}, function(param,element){
		if($.isArray(param)){
			var str = "";
			if(param.length==3){
				str = param[2];
			}else if(param.length==4){
				str = param[3];
			}
			return str;
		}else{
			return "已经存在；违反唯一约束!";
		}
	});
	
	/*
	 * 教师冲突校验
	 * jsConflict:['#xnm','#xqm','#jgh_id','#jxb_id','#zcd','#skdjd']
	 */ 
	$.validator.addMethod("jsConflict", function(value, element, param) {
		if(!$.isArray(param)){
			throw new Error(" 参数必须是数组! ");
		}
		var xnm = $(param[0]).val();
		var xqm = $(param[1]).val();
		var jxb_id = $(param[2]).val();
		var jgh_id = $(param[3]).val();
		var zc   = [];
		$(param[4]).find("td.ui-selected").each(function(i,td){
			zc.push($(td).data("zc")||$(td).text());	
		});
		//去除重复的周次
		zc  = zc.sort(function(a, b) {return a - b;});
		var zcBit = 0;
		$.each(zc,function(i,val){
			zcBit += Math.pow(2,val-1);
		});
		//[{"xqh_id":"7","xnm":"16","xqm":"3","xqj":"2","jc":"1-2"},{"xqh_id":"7","xnm":"16","xqm":"3","xqj":"2","jc":"6-7"}]
		var skdjd = $.parseJSON($(param[5]).data("skdjd")||"[]");
		var jc = null;
		$.each(skdjd,function(i,item){
			//{"xqh_id":"7","xnm":"16","xqm":"3","xqj":"2","jc":"1-2"}
			var jcArr = item["jc"].split("-");
			for (var i = parseInt(jcArr[0]); i <= parseInt(jcArr[1]); i++) {
				jc += Math.pow(2,i-1);
			}
		});
		var isUnique = false;
		$.ajax({
			type	: "POST",
		   	url		: _path+'/jxjhgl/commonPk_cxJsctValidate.html',
		   	async	: false ,
		   	data	: {
				"xnm"		: xnm || "",
				"xqm"		: xqm || "",
				"jgh_id"	: jgh_id || "",
				"xqj"		: xqj || "",
				"jc"		: jc || "",
				"zcd"		: zcBit || "",
				"jxb_id"	: jxb_id || ""
			},
			success	: function(data){
				if($.founded(data)){
					$(element).data("message",data);
					isUnique = false;
				}else{
					isUnique = true;
				}
			}
		});
		return isUnique ;
	}, function(param,element){
		var message = $(element).data("message");
		$(element).data("message",'');
		return  '与' + message + "冲突!";
	});
	
	// 验证更域名是否存在
	$.validator.addMethod("validEmail", function(value, element, param) {
		//判断是否可选验证，实际上是提前验证是否必填 
		if ( this.optional(element) ) {
			return "dependency-mismatch";
		}
		var result = false;
		$.ajax({
			type	: "POST",
		   	url		: _path + "/xtgl/validate_cxValidEmail.html",
		   	async	: false ,
		   	data	: {"yjzh": value},
			success	: function(data){
				if("1" == data){
					result = true;
				}
			}
		});
		return result;
	}, $.validator.format("邮箱类型不在规定类型内"));

}(jQuery));