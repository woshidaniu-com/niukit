/*
 * @discretion	: 基于jquery.validate插件的日期时间类校验方法
 * @author    	: wandalong 
 * @version		: v1.0.0
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){

	/*  验证当前文本的日期在对比日期之后：参数需是日期格式：如：
		new Date("yyyy/mm/dd HH:mm:ss");  
	    new Date("month dd,yyyy HH:mm:ss");  
	    new Date("month dd,yyyy");  
	    new Date(yyyy,mth,dd,HH,mm,ss);  
	    new Date(yyyy,mth,dd);  
	    new Date(ms);   
	*/
	$.validator.addMethod("afterDate", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return new String(value).toDate().after(new String($(param).val()).toDate()) ;
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能早于或等于{1}!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});
	
	/* 验证当前文本的日期在对比日期之前：参数如afterDate*/
	$.validator.addMethod("beforeDate", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return new String(value).toDate().before(new String($(param).val()).toDate()) ;
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能晚于或等于{1}!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});
    
	/* 验证当前文本的日期在对比日期相同：参数如afterDate*/
	$.validator.addMethod("equalsDate", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return new String(value).toDate().equals(new String($(param).val()).toDate()) ;
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能与{1}相同!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});
	

	// 验证当前文本的时间在对比时间之后：参数需是时间格式：如：HH:mm:ss  
	$.validator.addMethod("afterTime", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return  new String($(element).val()).toTime().after(new String($(param).val()).toTime());
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能早于或等于{1}!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});
	
	// 验证当前文本的时间在对比时间之前：参数需是时间格式：如：HH:mm:ss 
	$.validator.addMethod("beforeTime", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return  new String($(element).val()).toTime().before(new String($(param).val()).toTime());
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能晚于或等于{1}!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});
	
	// 验证当前文本的时间在对比时间相同：参数需是时间格式：如：HH:mm:ss 
	$.validator.addMethod("equalsTime", function(value, element, param) {
		if($(param).founded()&&$(element).founded()){
			return  new String($(element).val()).toTime().equals(new String($(param).val()).toTime());
		}else{
			return false;
		}
	},function(param,element){
		if($(param).founded()&&$(element).founded()){
			return $.validator.format("{0}不能与{1}相同!",[$(element).attr("text"),$(param).attr("text")]);
		}else if(!$(param).founded()){
			return $(param).attr("text")+"不能为空！";
		}else if(!$(element).founded()){
			return $(element).attr("text")+"不能为空！";
		}
	});

}(jQuery));