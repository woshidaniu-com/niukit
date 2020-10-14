;(function($) {
	
	$.getURL = function(requestURL,data){
		data = data || {};
		//自动添加功能代码
		if(jQuery("#gnmkdmKey").size() == 1 && jQuery("#gnmkdmKey").founded() ){
			if(!$.defined(data["gnmkdm"]) && $.defined(requestURL) && requestURL.indexOf("gnmkdm") == -1){
				//在url上追加
				if(requestURL.indexOf("?") > -1){
					requestURL = requestURL + "&gnmkdm=" + jQuery("#gnmkdmKey").val();
				}else{
					requestURL = requestURL + "?gnmkdm=" + jQuery("#gnmkdmKey").val();
				}
			}
		}
		//自动添加用户名
		if(jQuery("#sessionUserKey").size() == 1 && jQuery("#sessionUserKey").founded() ){
			if(!$.defined(data["su"]) && $.defined(requestURL) && requestURL.indexOf("su") == -1){
				//在url上追加
				if(requestURL.indexOf("?") > -1){
					requestURL = requestURL + "&su=" + jQuery("#sessionUserKey").val();
				}else{
					requestURL = requestURL + "?su=" + jQuery("#sessionUserKey").val();
				}
			}
		}
		//alert("requestURL:" + requestURL);
		return requestURL;
	};
	
	$.openWin = function(requestURL){
		//alert("requestURL:" + requestURL);
		top.window.open($.getURL(requestURL));
	};
	
	/*
	 * 打开一个新的窗口 弹出无模式对话框
	 * @param url 
	 * @param w 可以为空,默认400
	 * @param h 可以为空
	 * @param scrollbar 可以为空
	 */
	$.showWin = function(url, w, h, scro, ie) {
		var info = "";
		if(scro == null){
			info = "Status:YES;dialogWidth:" + w + "px;dialogHeight:" + h + "px;help:no;scroll:no";
		}else{
			info = "Status:YES;dialogWidth:" + w + "px;dialogHeight:" + h + "px;help:no;scroll:yes";
		}
		if(ie){
			window.showModalDialog($.getURL(requestURL), window, info);
		}else{
			window.open($.getURL(url),"","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=yes,width="+w+",height="+h+",left=100,top=100,screenX=0,screenY=0");
		}
	}
	
	$.clearIframe = function(id){
		 var el = document.getElementById(id),
		  	 iframe = el.contentWindow;
		 if(el){
			 try {
				el.src = 'about:blank';
				try {
					iframe.document.write('');
					iframe.document.clear();
				} catch (e) {
				}
				//以上可以清除大部分的内存和文档节点记录数了
				//最后删除掉这个 iframe 
				document.body.removeChild(el);
			} catch (e) {
				
			}
		 }
	}
	
	$.fn.resetIndex = function (callback){
		return $(this).each(function () {
			$(this).find("tr.jqgrow").each(function(i,tr){
				$(this).find(":text,select,:hidden,textarea").each(function(){
					var name = $(this).attr("name");
					if($.founded(name)){
						$(this).attr("name",name.replace(/\[\d+\]/, "[" + i + "]"));
					}
				});
				if($.isFunction(callback)){
					callback.call($this,i,tr);
				}
			});
		});
	};
	
	$.fn.resetOrdinal = function (callback){
		return $(this).each(function () {
			var $this = this;
		    //循环行
			$($this).find("tbody tr.jqgrow").each(function(i,tr){
				$(this).find("td.detail-rownum").text(i + 1);
				if($.isFunction(callback)){
					callback.call($this,i,tr);
				}
			});
		});
	};
	
	$.fn.clearTitle = function (callback){
		return $(this).each(function () {
			var $this = this;
			var colModel = jQuery("#"+$this.id).jqGrid('getGridParam','colModel'); 
			//处理formatter导致的td上title显示异常
		    $.each(colModel ||[],function(i,item){
			   //判断有格式函数
			   if($.isFunction(item["formatter"])){
				   //循环数据行
				   $($this).find("tr.jqgrow").each(function(i,tr){
					   $(this).find("td[aria-describedby$='_"+item["name"]+"']").attr("title","");
					   if($.isFunction(callback)){
							callback.call($this,i,tr);
					   }
				   });
			   }
		   });
		});
	};
	
	$.fn.newTab = function(options){
		options = options||{};
		top.$('#tabs').tabs("builTab",{
			id: $(this).data('addtab'),
			title: $(this).attr('title') ? $(this).attr('title') : $(this).html(),
			content: options.content ? options.content : $(this).attr('content'),
			url: $(this).data('src'),
			ajax: $(this).attr('ajax') ? true : false,
			tablayout: $(this).data('tab-layout'),
			funclayout: $(this).data('blank-layout'),
			data: $(this).data('request') || {}
		});
	}

	$.closeTab = function(id){
		top.$('#tabs').tabs("close",id);
	}
	
	// 3:预览打印 
	/*$.openReport({
		"reportID" 		: "cjdjxbdy.cpt",
		"jxb_id"		: ids[0]
	})*/
	$.openReport = function(options){
		
		if($("#statusModal").size() > 0){return;}
		options = options||{};
		var reportID = options["reportID"];
		if(!$.founded(reportID)){
			throw new Error("reportID 不能为空 !");
		}
		delete options["reportID"];
		
		var requestMap = {};
		//拼装查询参数：因为可能有中文，这里需要转码
		$.each(options||{}, function(key, val){
			requestMap["mapRow.row."+key] = encodeURIComponent(val ||"");
		});
		
		//构建form,报表预览
		$.buildForm("reportViewForm", _path+"/design/viewReport_cxFineReportViewIndex.html?reportID=" + reportID + "&_t"+new Date().getTime(),requestMap).submit();
		
	}
	
	
})(jQuery);