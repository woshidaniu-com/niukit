
function refRightContent(url){
	jQuery.ajaxSetup ({cache: true });		
	if (jQuery("#rightContent").length == 0) {
		window.location.href = url;// 若无rightContent的div，直接做跳转
	} else {
		jQuery.get(url, function(html){
			jQuery("#rightContent").html(html);
		});
		//jQuery("#rightContent").load(url);
	}
}

function onClickMenu(dyym,gnmkdm,options){
	//重新设置gnmkdm
	jQuery("#gnmkdmKey").val(gnmkdm);
	//计算位置
	var margin_top	=	(jQuery("#yhgnPage").innerHeight() - 200)/2;
		margin_top	= 	(margin_top>0) ? margin_top : 0;
	//加载页面
	jQuery("#yhgnPage").html('<p id="loading_status" class="text-center header smaller lighter" style="margin-top:'+margin_top+'px;"><i class="icon-spinner icon-spin orange  bigger-500"></i></br> <span class="bigger-160">网页正在载入数据中.请等待....</span></p>');
	//这里的czdmKey=00；在功能描述控制页面：表示所以的页面	
	//启用缓存：这个地方必须使用true，否则在使用load加载的html中的script标签会自动加上随机数导致无法有效利用浏览器缓存 
	jQuery.ajaxSetup({cache	: true });
	var tmpURL =  $.defined(dyym) ? ((dyym||"") + ((dyym||"").indexOf("?") > -1 ? "&" : "?" ) +  ("_t=" + jQuery.now() )) : (dyym||"");
	jQuery("#yhgnPage").load(_path + tmpURL,$.extend({},$(document).data()||{},{"gnmkdm":gnmkdm,"czdmKey":"00"},options||{}),function(responseText, textStatus, xmlhttprequest){
		if(textStatus == "success"){
			//隐藏一个当前功能页面的功能模块代码
			jQuery("#gnmkdmKey").remove();
			jQuery("#requestMap").append('<input type="hidden" id="gnmkdmKey" value="'+gnmkdm+'" />');
		}
		//this;在这里this指向的是当前的DOM对象，即$(".ajax.load")[0] 
		//alert(responseText);//请求返回的内容
		//alert(textStatus);//请求状态：success，error
		//alert(XMLHttpRequest);//XMLHttpRequest对象
		//var _allHeight=document.documentElement.clientHeight;
		//$('.sl_all_bg').css('min-height',_allHeight-110);
	},"html");
	
	jQuery("#navbar-nav").find("li").removeClass("active");
	jQuery(this).parent("li").addClass("active");
}

//查看审批流
function ckWorkFlow(rowid){
	jQuery("#tabGrid").resetSelection();
	jQuery("#tabGrid").setSelection(rowid,false);
	jQuery.viewWorkFlow(rowid);
}

function ckWorkFlow2(rowid){
	jQuery.viewWorkFlow(rowid);
}