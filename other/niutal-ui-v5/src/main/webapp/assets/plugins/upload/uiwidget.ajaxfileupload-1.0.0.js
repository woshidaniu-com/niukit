/**
 * @discretion	: default messages for the jQuery fixedbox plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/uiwidget.fixedbox-1.0.0.css
 				  3.引用效果的具体js代码文件 uiwidget.fixedbox-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").ajaxfileupload({
							
						});
						
					});
					</script>
 */
;(function($){
	
	jQuery.extend({  
	    handleError: function( options, xhr, status, e ){  
			// If a local callback was specified, fire it  
		    if ( options.error ) {options.error.call( options.context || options, xhr, status, e );}
		    // Fire the global callback  
		    if ( options.global ) {
		        (options.context ? jQuery(options.context) :         
		        jQuery.event).trigger( "ajaxError", [xhr, options, e] );
		    }
		},
		uploadHttpData : function( r, type ) {
	        var data = !type;
	        data = type == "xml" || data ? r.responseXML : r.responseText;
	        // If the type is "script", eval it in global context
	        if ( type == "script" ){
	            jQuery.globalEval( data );
	        }
	        // Get the JavaScript object, if JSON is used.
	        if ( type == "json" ){
	            try {
	            	eval( "data = \" " + data + " \" ");
				} catch (e) {
					data = jQuery.parseJSON(data.replace(/^\<[\w\-\:\;\"\'\= \(\)]+\>|\<\/\w+\>$/ig,''));
				}
	        }
	        // Get the text.
	        if ( type == "text" ){
	        	data = data.replace(/<\/?[^>]*>/g,''); //去除HTML tag
	        	data = data.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
	            //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	        	data = data.replace(/&nbsp;/ig,'');//去掉&nbsp;
	        	data = data.replace(/[\"|\']*/ig,'');//去掉"';
	        }
	        // evaluate scripts within html
	        if ( type == "html" ){
	            jQuery("<div>").html(data).evalScripts();
	        }
	        return data;
	    }
	});
	
	var defaults = {
		/*版本号*/
		version:'1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*其他参数*/
		//上传处理程序地址
		url				: null,
		//自定义参数
		data			: {},
		//当要提交自定义参数时，这个参数要设置成post
		type 			: 'post',
		//需要上传的文件域的选择器，默认:file
		fileSelector	: ":file",
		//是否启用安全提交，默认为false
		secureuri		: false,
		//服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断
		dataType		: 'text',
		//提交成功后自动执行的处理函数，参数data就是服务器返回的数据
		success			: $.noop,
		//提交失败自动执行的处理函数
		error			: $.noop
	};
	
	$.fn.ajaxfileupload = function(options){
		return this.each(function () {
			var element = $(this);
			// introduce global settings, allowing the client to modify them for all requests, not only timeout		
			options = $.extend(true,{}, jQuery.ajaxSettings, defaults , element.data(), ((typeof options == 'object' && options) ? options : {}));
			
			options.data = $.extend(true,options.data||{},{
				"sessionUserKey" : jQuery("#sessionUserKey").val(),
	 			"gnmkdmKey" 	 : jQuery("#gnmkdmKey").val()
			});
			
			function getUID(prefix) {
				do {
				  	prefix += ~~(Math.random() * 1000000);
			  	}while (document.getElementById(prefix));
			  	return prefix;
			};
			
			var form_uid = getUID(options.prefix);
			var formId = 'jUploadForm' + form_uid;
			var frameId = 'jUploadFrame' + form_uid;
			
			//删除生成的元素函数
			var removeElement = function(){
				//移除动态生成的from
				if(! $(element).is("form")){
					$("#"+formId).remove();
				}
				//移除动态生成的iframe
				$("#"+frameId).remove();
			};
			
			//===================获取或者初始化用于上传的form===============================================
			var form = element;
			//判断是否是form
			if($(element).is("form")){
				 
				options["url"] = options["url"] || $(element).attr("action");
				options["type"] = options["type"] || $(element).attr("method") ||"post";
				   
				if($.trim($(element).attr("id")||"").length > 0 ){
					formId = $(element).attr("id");
				}else{
					$(element).attr("id",formId);
				}
			}else{
				//wrap with form	
				form = $(element).wrap(function(){
					return '<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>';
				});
			}
				
			//===================获取或者初始化用于上传回显的iframe===============================================
			var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="display: none;position:absolute; top:-9999px; left:-9999px"';
			if(window.ActiveXObject){
                if(typeof options.uri== 'boolean'){
					iframeHtml += ' src="' + 'javascript:false' + '"';
                }else if(typeof options.uri== 'string'){
					iframeHtml += ' src="' + options.uri + '"';
                }	
			}
			iframeHtml += ' />';
			jQuery(iframeHtml).appendTo(document.body);
			
			//===================初始化上传===============================================
			
			// Watch for a new set of requests
	        if ( options.global && ! jQuery.active++ ){
				jQuery.event.trigger( "ajaxStart" );
			}
	        var requestDone = false;
	        // Create the request object
	        var xml = {}   
	        if ( options.global ){
	            jQuery.event.trigger("ajaxSend", [xml, options]);
	        }
	        
	        // Wait for a response to come back
	        var uploadCallback = function(isTimeout){			
				var io = document.getElementById(frameId);
	            try{				
					if(io.contentWindow){
						xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
	                	xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
					}else if(io.contentDocument){
						xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
	                	xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
					}						
	            }catch(e){
					jQuery.handleError(options, xml, null, e);
					removeElement.call(element,e);
				}
	            if ( xml || isTimeout == "timeout") {				
	                requestDone = true;
	                var status;
	                try {
	                    status = isTimeout != "timeout" ? "success" : "error";
	                    // Make sure that the request was successful or notmodified
	                    if ( status != "error" ){
	                        // process the data (runs the xml through httpData regardless of callback)
	                        var data = jQuery.uploadHttpData( xml, options.dataType );    
	                        // If a local callback was specified, fire it and pass it the data
	                        if ( options.success ){
	                        	options.success( data, status );
	                        }
	                        // Fire the global callback
	                        if( options.global ){
	                            jQuery.event.trigger( "ajaxSuccess", [xml, options] );
	                        }
	                    } else{
	                        jQuery.handleError(options, xml, status);
	                        removeElement.call(element,e);
	                    }
	                } catch(e) {
	                    status = "error";
	                    jQuery.handleError(options, xml, status, e);
	                    removeElement.call(element,e);
	                }

	                // The request was completed
	                if( options.global ){
	                    jQuery.event.trigger( "ajaxComplete", [xml, options] );
	                }
	                // Handle the global AJAX counter
	                if ( options.global && ! --jQuery.active ){
	                    jQuery.event.trigger( "ajaxStop" );
	                }
	                // Process result
	                if ( options.complete ){
	                	options.complete(xml, status);
	            	}
	                jQuery(io).unbind();

	                setTimeout(function(){	
	                	try{
							jQuery(io).remove();
							if(!$(element).is("form")){
								$(element).unwrap();
							}
						} catch(e) {
							jQuery.handleError(options, xml, null, e);
							removeElement.call(element,e);
						}									
					}, 100);
	                xml = null;
	            }
	        }
			// Timeout checker
        	if ( options.timeout > 0 ){
	            setTimeout(function(){
	                // Check to see if the request is still happening
	                if( !requestDone ) {
	                	uploadCallback( "timeout" );
	                };
	            }, options.timeout);
	        }
        	
        	try {
    			jQuery(form).attr('action', options.url);
    			jQuery(form).attr('method', 'POST');
    			jQuery(form).attr('target', frameId);
                if(form.encoding){
    				jQuery(form).attr('encoding', 'multipart/form-data');      			
                } else{	
    				jQuery(form).attr('enctype', 'multipart/form-data');			
                }		
                jQuery(form).append($.buildHiddens(options.data||{},true));
                jQuery(form).submit();
            } catch(e) {			
                jQuery.handleError(options, xml, null, e);
                removeElement.call(element,e);
            }
    		jQuery('#' + frameId).load(uploadCallback);
    		
		});
	};

}(jQuery));
