;(function($) {

	 $.isHTMLElement = function(elem){
    	//首先要对HTMLElement进行类型检查，因为即使在支持HTMLElement
        //的浏览器中，类型却是有差别的，在Chrome,Opera中HTMLElement的
        //类型为function，此时就不能用它来判断了
    	return (( typeof HTMLElement === 'object' ) ? function(){
	            return elem instanceof HTMLElement;
	        } :
	        function(){
	            return elem && typeof elem === 'object' && elem.nodeType === 1 && typeof elem.nodeName === 'string';
        }).call(this);
    };
    
    $.fn.isHTMLElement = function(){
    	return $.isHTMLElement(this[0]);
    };
                
    // adding :visible and :hidden to zepto
    // https://github.com/jquery/jquery/blob/73e120116ce13b992d5229b3e10fcc19f9505a15/src/css/hiddenVisibleSelectors.js
    var _is = $.fn.is, _filter = $.fn.filter;
    function visible(elem) {
        elem = $(elem);
        if(elem.isHTMLElement()){
        	return !!(elem.width() || elem.height()) && elem.css("display") !== "none";
        }
        return true;
    }
    
    $.fn.is = function(sel) {
        if (sel === ":visible") {
            return visible(this);
        }
        if (sel === ":hidden") {
            return !visible(this);
        }
        return _is.call(this, sel);
    };
    
    $.fn.filter = function(sel) {
        if (sel === ":visible") {
        	 return $([].filter.call(this, function(elem) {
                 return visible(elem);
             }));
        }
        if (sel === ":hidden") {
            return $([].filter.call(this, function(elem) {
                return !visible(elem);
            }));
        }
        return _filter.call(this, sel);
    };
    
	$.extend({
		//对参数进行编码encodeURIComponent
		encode:function(text) {
			return encodeURIComponent(encodeURIComponent(text));
		},
		defined : function(obj) {
			return ( typeof obj != 'undefined' && obj != 'undefined' && obj != null && obj != 'null');
		},
		founded: function(obj) {
			if($.defined(obj)){
				// "boolean", "string", "number","object", "function" ,"undefined"
				if(typeof obj == 'boolean'){
					return obj;
				}else if(typeof obj == 'string'){
					return $.trim(obj).length > 0;
				}else if(jQuery.isArray(obj)){//"array",
					return jQuery.merge([],obj).length > 0;
				}else if($.isPlainObject(obj)){
					return !$.isEmptyObject(obj);
				}else{
					//number,object,function
					return true;
				}
			}else{ 
				return false;
			}
		},
		buildForm:function(formID,action,options,target){
			var form = $("#"+(formID||"drdcForm"));
			if($(form).size() > 0 && $(form).is("form")){
				form = $(form).empty();
			}else{
				form = jQuery('<form/>');
				$(form).css('display','none').appendTo("body");
			}
			$(form).attr({
				"id"		: (formID||"drdcForm"),
				'action'	: action,
				'method'	: 'post',
				'enctype'	: 'application/x-www-form-urlencoded; charset=UTF-8',
				'target'	: target||'_blank'
			});
			//添加默认请求参数
			$(form).append($.buildHiddens({
				"gnmkdmKey"			: jQuery("#gnmkdmKey").val(),
				"sessionUserKey" 	: jQuery("#sessionUserKey").val()
			},true));
			$(form).append($.buildHiddens(options||{},true));
			return $(form);
		},
		buildHiddens:function(options,deep){
			var html = [];
			$.each(options||{},function(key,val){
				if($.isPlainObject(val)&&true==deep){
					html.push($.buildHiddens(val));
				}else if($.isArray(val)){
					$.each(val,function(i,item){
						html.push('<input type="hidden" name="'+key+"["+i+"]"+'" value="'+item+'">');
					});
				}else if($.isFunction(val)){
				}else{
					html.push('<input type="hidden" name="'+key+'" value="'+val+'">');
				}
			});
			return html.join("");
		},
		cloneObj:function(source){
			return $.extend(true,{},source||{});
		},
		mergeObj:function(){
			var newObj = {};
			$.each(arguments,function(i,argument){
				$.extend(true,newObj,argument||{});
			});
			return newObj;
		},
		getTextLength:function(value){
			var length = value.length;
			for ( var i = 0; i < value.length; i += 1) {
				if (value.charCodeAt(i) > 127) {
					length += 1;
				}
			}
			return length;
		},
		toBoolean : function(obj){
			if($.defined(obj) ){
				if($.type(obj) == "boolean"){
					return obj;
				}else if($.type(obj) == "string"){
					obj = $.trim(obj).toLowerCase();
					if("yes" == obj ||"true" == obj || "1" == obj){
						return true;
					}else if("no"== obj || "false" == obj || "0" == obj ){
						return false;
					}else{
						return Boolean(obj);
					}
				}else{
					return Boolean(obj);
				}
			}else{
				return false;
			}
		},
		toTime:function(text) {
			if(/^([0-1][0-9]|2[0-3])[:]([0-5]{1}[0-9]{1})[:]([0-5]{1}[0-9]{1})?$/mg.test(text)){
			    var hour = RegExp.$1, 
			    	min = RegExp.$2?RegExp.$2:0, 
			    	sec = RegExp.$3?RegExp.$3:0;
			    var time = new Date();
			    	time.setHours(hour, min, sec, 0);
			   	return time;
			}else{
				return null;
			}
		},
		convertID:function(id){
		   return id.replace(/(:|\(|\)|\{|\}|\-|\.|\#|\@|\$|\%|\^|\&|\*|\!)/g,'\\$1');
		},
		getUID : function (prefix) {
			prefix = prefix||"UID_";
			do {
			  	prefix += ((Math.random() * 1000000) + new Date().getTime());
		  	}while (document.getElementById(prefix));
		  	return (prefix + "").replace(".","");
		},
		matchURL : function(_url){
			var testURL = $.trim(_url||"").toLowerCase();
			return typeof _url == "string" && testURL.length > 0 && /(?:\S+\.(?:js|css|properties)(?:\?ver=\S+)?)/.test(testURL);
		},
		matchJS : function(_url){
			var testURL = $.trim(_url||"").toLowerCase();
			return typeof _url == "string" && testURL.length > 0 && /(?:\S+\.js(?:\?ver=\S+)?)/.test(testURL);
		},
		matchCss : function(_url){
			var testURL = $.trim(_url||"").toLowerCase();
			return typeof _url == "string" && testURL.length > 0 && /(?:\S+\.css(?:\?ver=\S+)?)/.test(testURL);
		}
	});
	
	$.extend(Array.prototype, {
		/**
		 * @discretion: 返回元素在数组的索引，没有则返回-1。与string的indexOf方法差不多
		 * @method : indexOf
		 * @return : {Number}
		 */
		indexOf : function(el, start) {
			var start = start || 0;
			for (var i = 0; i < this.length; ++i) {
				if (this[i] === el) {
					return i;
				}
			}
			return -1;
		},
		remove:function(indexOrKey,keyName){
			var array = [].concat(this);
			if($.isNumeric(indexOrKey)){
				array.splice(indexOrKey,1);
			}else{
				$.each(this,function(i,item){
					if(item[keyName] == indexOrKey){
						array.splice(i,1);
						return false;
					}
				});
			}
			return array;
		}
	});
	
	$.fn.getUID = function (prefix){
		var id = $(this).attr("id");
		if(! $.founded(id) ){
			id = prefix||"UID_";
			do {
				id += ~~((Math.random() * 1000000) + new Date().getTime());
		  	}while (document.getElementById(id));
		}
		return id;
	}
	 
	$.fn.killFocus = function () {
		return $(this).each(function(){
			 this.onmousedown = function() {
			    this.blur(); // most browsers
			    this.hideFocus = true; // ie
			    this.style.outline = 'none'; // mozilla
			  }
			 this.onmouseout = this.onmouseup = function() {
			    this.blur(); // most browsers
			    this.hideFocus = false; // ie
			    this.style.outline = null; // mozilla
			 }
		});
	}
	
	$.fn.extend({
		defined : function() {
			return $(this).size() != 0;
		},
		founded: function() {
			var val = $(this).is("select") ?  $(this).getSelected().val() : $(this).val();
			return $(this).defined() && $.founded(val);
		},
		//禁用
		disabled: function() {
			return $(this).filter(":input").each(function(){
				$(this).prop("disabled",true).addClass("disabled").data("disabled",true);
			});
		},
		//是否禁用
		isDisabled: function() {
			return !$(this).isEnabled();
		},
		//解除禁用
		enabled: function() {
			return $(this).filter(":input").each(function(){
				$(this).prop("disabled",false).removeClass("disabled").removeData("disabled");
			});
		},
		//是否可用
		isEnabled: function() {
			return ($(this).prop("disabled") == false && !$(this).hasClass("disabled") && !$(this).data("disabled") === true)
		},
		attrs: function(){
			var attrs = $(this)[0].attributes;
			var attributes = {};
			for(var i = 0; i < attrs.length ; i+=1){   
				var att = attrs[i];    
				if(att.specified)  {  
					attributes[att.name] = att.value;
				}
			}
			return attributes;
		},
		//禁止右键菜单
		disableContextMenu : function(){
			$(this).each(function(index,element){
				$(element).bind("contextmenu",function(){
			        return false;   
			    });
			});
		},
		getWidget:function(){
			if (!$.metadata){
				return {};
			}else{
				//$.parseJSON(widget.replace(/'/g,'\"'))
				return $(this).metadata({"single":"widget"});
			}
		},
		getValueLength:function(){
			return $.getTextLength($(this).val());
		},
		getTextLength:function(value){
			$.getTextLength($(this).text())
		},
		getVisualWidth:function(font_size){
			$("#visualLength_ruler").remove();
			$(document.body).append('<span id="visualLength_ruler">test</span>');
			var ruler = $("#visualLength_ruler");
			$(ruler).css({
				"visibility": "hidden",
				"display": "none",
			 	"white-space": "nowrap",
			 	"font-size": font_size||"14px"
			});
			if ($(this).is(':radio') || $(this).is(':checkbox')) {
				var text = $(this)[0].nextSibling.nodeValue;
				ruler.text(this);
				return $(this).outerWidth() + ruler[0].offsetWidth;
			}else{
				return $(this).outerWidth();
			}
		},
		//对参数进行编码encodeURIComponent
		encode:function() {
			return encodeURIComponent(encodeURIComponent($(this).val()));
		},
		/**
		 * 移除下拉框select中指定索引或者指定值的option
		 */
		removeOption:function(indexOrVal){
			if($(this).is("select")){
				if(typeof indexOrVal == 'string'){
					$(this).find("option").each(function(i,option){
						if($(option).val() == indexOrVal){
							$(option).remove();
						}
					});
				}else if(typeof indexOrVal == 'number'){
					$(this).find("option").eq(indexOrVal).remove();
				}
			}
		},
		/**
		 * 根据数据生成option对象
		 */
		buildOptions:function(data,needEmpty,defalut){
			if($(this).is("select")){
				var html = [];
				if(needEmpty&&needEmpty==true){
					html.push('<option value="">--请选择--</option>');
				}
				$.each(data||[],function(i,rowObj){
					var selectedStr = "";
					if($.founded(defalut)&&defalut==rowObj.value){
						selectedStr = ' selected="selected" ';
					}
					html.push('<option ');
					$(rowObj).each(function(k,v){
						if(k!="value"){
							html.push(k+'="'+v+'"');
						}
					});
					html.push(' value="'+rowObj.value+'" '+selectedStr+'>'+rowObj.text+'</option>');
				});
				$(this).append(html.join(""));
			}
		},
		/**
		 * 选中下拉框select中指定索引或者指定值的option
		 */
		setSelected:function(indexOrVal){
			return this.each(function(){
				if($(this).is("select")){
					if(typeof indexOrVal == 'string'){
						$(this).find("option").each(function(i,option){
							if($(option).val() == indexOrVal){
								$(option).prop("selected",true);
							}
						});
					}else if(typeof indexOrVal == 'number'){
						$(this)[0].selectedIndex = indexOrVal;
					}
				}
			});
		},
		/**
		 * 获得下拉框select中选中的option的DOM对象
		 */
		getSelected:function(){
			if($(this).is("select")){
				return $(this).find("option:selected");
			}
		},
		/**
		 * 获得下拉框select中选中的option的value和text等属性组成的JSON对象
		 */
		getSelectedList:function(){
			var $this = this;
			//获取选中项的option
			var selecteds = $(this).getSelected();
			if($(selecteds).size()>1){
				var result = new Array();
				$(selecteds).each(function(i,selected){
					result.push($.extend({},$($this).attrs(),$(selected).attrs(),{"key":$(selected).val(),"value":$(selected).val(),"text":$(selected).text()}));
				});
				return result;
			}else{
				return $.extend({},$(this).attrs(),$(selecteds[0]).attrs(),{"key":$(selecteds[0]).val(),"value":$(selecteds[0]).val(),"text":$(selecteds[0]).text()});
			}
		},
		/**
		 * 设置多选框checkbox,单选组radio指定索引或者指定值的项
		 */
		setChecked:function(indexOrVal){
			return this.each(function(){
				if($(this).is("input")&&$(this).is(function(){
					var type = $.trim($(this).attr("type"));
					return (type==="radio"||type==="checkbox");
				})){
					if(typeof indexOrVal == 'string'){
						$(this).each(function(i,input){
							if($(input).val() == indexOrVal){
								$(input)[0].checked = true;
							}
						});
					}else if(typeof indexOrVal == 'number'){
						$(this).get(indexOrVal).checked = true;
					}
				}
			});
		},
		/**
		 * 获得多选框checkbox,单选组radio选择的DOM对象
		 */
		getChecked:function(){
			if($(this).is(":radio") || $(this).is(":checkbox")){
				//获取选中项的input
				var checkeds = $(this).filter(":checked");
				if($(checkeds).size()>1){
					var result = new Array();
					$(checkeds).each(function(i,checked){
						result.push($(checked));
					});
					return result;
				}else{
					return $(checkeds[0]);
				}
			}
		},
		/**
		 * 获得多选框checkbox,单选组radio选择的DOM对象的value和text等属性组成的JSON对象
		 */
		getCheckedList:function(){
			var $this = this;
			if($(this).is(":radio") || $(this).is(":checkbox")){
				//获取选中项的input
				var checkeds = $($this).filter(":checked");
				if($(checkeds).size()>1){
					var result = new Array();
					$(checkeds).each(function(i,checked){
						result.push($.extend({},$($this).attrs(),$(checked).attrs(),{"key":$(checked).val(),"value":$(checked).val(),"text":$(checked)[0].nextSibling.nodeValue}));
					});
					return result;
				}else{
					return $.extend({},$($this).attrs(),$(checkeds[0]).attrs(),{"key":$(checkeds[0]).val(),"value":$(checkeds[0]).val(),"text":$(checkeds[0])[0].nextSibling.nodeValue})
				}
			}
		},
		getPosition : function () {
			  var $element   = this , el     = this[0];
			  var isBody = el.tagName == 'BODY';
			  return $.extend({}, (typeof el.getBoundingClientRect == 'function') ? el.getBoundingClientRect() : null, {
				  scroll: isBody ? document.documentElement.scrollTop || document.body.scrollTop : $element.scrollTop(),
				  width:  isBody ? $(window).width()  : $element.outerWidth(),
				  height: isBody ? $(window).height() : $element.outerHeight()
			  }, isBody ? { top: 0, left: 0 } : $element.offset());
		},
		//因为有些拼接出的ID不能被JQuery直接使用，需要进行转换
		getElementID : function(){
			return $.convertID($.trim($(this).attr("id")));
		}
	});
	
	//获得指定元素关联的真实事件元素，这里指使用chosen后的模拟下拉元素和提示气泡所在的元素
	$.fn.getRealElement = function(){
		var newElement = this;
		var elementID = $(this).getElementID();
		var chosen_id = "#"+ elementID + "_chosen";
		//判断是否调用了chosen美化插件
		if($.defined($(this).data("chosen")) || $(chosen_id).size() > 0 ){
			newElement = $(chosen_id);
		}else if($(this).prev('div.bootstrap-tagsinput').size() > 0){
			newElement =  $(this).prev('div.bootstrap-tagsinput');
		}else if($(this).parent().hasClass('input-group-tooltips')){
			newElement = $(this).parent();
		}else{
			if ($(this).is(':radio') || $(this).is(':checkbox')) {
				newElement = $("input[name='"+$(this).attr("name")+"']").first();
			}
		}
		return $(newElement);
	};
	
	//兼容提示信息的位置对象获取
	$.fn.getTargetElement = function(){
		//移除样式
		var formTd = $(this).closest("td");
		var formGroup = $(this).closest(".form-group");
		var inputGroup = $(this).closest(".input-group");
		//适配不同页面环境的提示样式元素
		if(formTd.size() > 0){
			return formTd;
		}else if(formGroup.size() > 0){
			return formGroup;
		}else if(inputGroup.size() > 0){
			return inputGroup;
		}else {
			return $(this).parent().parent();
		}
	};
	
	/**
	 * 
	 * $(selector).loadIframe(url,{
	 * 		progress:   function(){}, 单个加载过程中的回调函数
	 * 		complete:   function(){}  单个加载完成的回调函数
	 * }).done(function(){ 
	 * 		
	 * }).fail(function(){
	 * 	
	 * }).always(function(){
	 * 		
	 * });
	 * 
	 */
	$.fn.loadIframe = function(_url,option){
		var defaults = {
	  		progress:   $.noop, //单个加载过程中的回调函数
	  		complete:   $.noop  //单个加载完成的回调函数
		};
		//iframe 对象
		var iframe = $(this[0]);
		if (!$(iframe).is("iframe")) {
			throw new Error(' Current Element is not an iframe ! ');
		} else {
	    	//利用延时对象按顺序加载资源
	        return $.when($.Deferred(function(dtd){
	        	var testURL = $.trim(_url).toLowerCase();
				if(typeof _url == "string" && testURL.length > 0 ){
					//如果资源已经下载到了本地 
					try {
						var options = $.extend( true ,{},  defaults , $(iframe).data(), ((typeof option == 'object' && option) ? option : {}));
		                
						//释放占用资源
	                    var freeSpace = function(){
	                    	//防止IE内存泄露  
	                    	iframe.onload = iframe.onreadystatechange = iframe.onerror = null;
	                    }
	                    
						//加载完成后的回调函数
	                    var loading = function(){
	                    	// !this.readyState 为不支持onreadystatechange的情况，或者OP下创建CSS的情况
	                        // this.readyState === "loaded" 为IE/OP下创建JS的时候
	                        // this.readyState === "complete" 为IE下创建CSS的时候
	                    	if( (!this.readyState)|| (/loaded|complete/.test(this.readyState)) ){
	                    		if($.isFunction(options.complete)){
	                    			//执行回调函数
	                        		options.complete.call(this);
	                        	}
	                            //防止IE内存泄露  
	                        	freeSpace();
	                            // 改变deferred对象的执行状态为：已完成
	    						dtd.resolve(); 
	                    	}
	                    }
	                    	
	                    //加载失败后的回调函数
	                    var clear = function(){
	                    	 //防止IE内存泄露  
	                    	freeSpace();
	                        //是否忽略错误项
					   		if(options.ignore && options.ignore != true){
							   // 改变Deferred对象的执行状态为：已失败
							   dtd.reject(); 
					   		}
	                    }
	                    //添加一个加载监听器，方便回调加载过程方便添加特效
	                    var timer	= window.setInterval(function(){
	                    	if(iframe){
	                    		if( (!iframe.readyState)|| (/loaded|complete/.test(iframe.readyState)) ){
	                                window.clearInterval(timer);
	                            }else{
	                            	if($.isFunction(options.progress)){
	                            		options.progress.call(iframe);
	                            	}
	                            }
	                    	}else{
	                    		window.clearInterval(timer);
	                    	}
	                    },100);
						
	                    if (iframe.attachEvent){    
						    iframe.attachEvent("onload", function(){        
						        alert("Local iframe is now loaded.");   
						        if (isLoaded) { return; }
		                		loading.call(this);
		                    	isLoaded = true;
						    });
						    
						    iframe.attachEvent("onerror", function(){
						    	clear.call(this);
						    });
						    
						} else {    
						    //解决 onload和onreadystatechange都生效时；回调两次的问题
							var isLoaded = false;
							/**
		                	 * onreadystatechange: 支持: IE6-9/OP, 不支持: FF/Webkit (这里有区别，OP支持js创建的js元素，readyState为loaded)
		                	 * 兼容写法：iframe.onload = iframe.onreadystatechange
		                	 *  	onload为IE6-9/OP下创建CSS的时候，或IE9/OP/FF/Webkit下创建JS的时候
							 * 		onreadystatechange为IE6-9/OP下创建CSS或JS的时候
		                	 */
		                	iframe.onload = iframe.onreadystatechange = function () {  
		                		alert("Local iframe is now loaded.");   
		                		if (isLoaded) { return; }
		                		loading.call(this);
		                    	isLoaded = true;
		                    };
		                    iframe.onerror = function(){
		                    	clear.call(this);
		                    }
						}
	                    
	                    iframe.src = _url;
	                    
					} catch (e) {
						throw new Error(e);
					}
				}else{
					// 改变deferred对象的执行状态为：已完成
					dtd.resolve(); 
				}
	        }));
		}
	};
	 
	//form序列化为JSON对象
	$.fn.serializeJSON = function(encode){
		var serializeMap = {};
		
		$(this).find("input[type!='button'][type!='file'],select,textarea").each(function(i,element){
			//定义参数名称
			var field_name = $(this).attr("name"),field_values;
			//判断当前字段是否不存在:主要目的是排除单选和多选此类同名元素多次被处理问题
			if(!serializeMap[field_name]){
				//取值
				if($(this).is("select")){
					field_values = $(this).find("option:selected").val();
				}else if($(this).is(":radio")){
					//获取选中项的input
					var checkeds =  $("input[name='"+field_name+"']").filter(":checked");
					field_values = $(checkeds[0]).val();
				}else if($(this).is(":checkbox") ){
					//获取选中项的input
					var checkeds = $("input[name='"+field_name+"']").filter(":checked");
					if($(checkeds).size()>1){
						field_values = new Array();
						$(checkeds).each(function(i,checked){
							field_values.push($(checked).val());
						});
					}else{
						field_values = $(checkeds[0]).val();
					}
				}else{
					field_values = $(this).val();
				}
				//判断当前字段值是否经存在
				if(field_values){
					//判断当前结果是否是数组
					if($.isArray(field_values)){
						var fieldArr = [];
						//循环Array
						$.each(field_values||[],function(i,field_value){
							fieldArr.push((encode != false ? encodeURIComponent(field_value) : field_value));
						});
						serializeMap[field_name].push(fieldArr.join(","));
					}else{
						//非数组类型直接赋值
						serializeMap[field_name] = encode != false ? encodeURIComponent(field_values) : field_values;
					}
				}else{
					serializeMap[field_name] = "";	
				}
				/*//判断当前字段值是否经存在
				if(serializeMap[field_name]){
					if($.isArray(serializeMap[field_name])){
						//判断当前结果是否是数组
						if($.isArray(field_values)){
							//循环array
							$.each(field_values||[],function(i,field_value){
								serializeMap[field_name].push(encode != false ? encodeURIComponent(field_value) : field_value);
							});
						}else{
							serializeMap[field_name].push(encode != false ? encodeURIComponent(field_values) : field_values);
						}
					}else{
						//判断当前结果是否是数组
						if($.isArray(field_values)){
							//自身变成Array
							serializeMap[field_name] = [serializeMap[field_name]];
							//循环array
							$.each(field_values||[],function(i,field_value){
								serializeMap[field_name].push(encode != false ? encodeURIComponent(field_value) : field_value);
							});
						}else{
							serializeMap[field_name] = [serializeMap[field_name],(encode != false ? encodeURIComponent(field_values): field_values)];
						}
					}
				}else{
					serializeMap[field_name] = encode != false ? encodeURIComponent(field_values): field_values;	
				}*/
			}
		});
		
		return serializeMap;
	};
	
	$.delay = function(funcs,callback){
		funcs = funcs || $.noop; 
		callback = callback || $.noop;
		var timeID = null;
		$.when($.Deferred(function(def){
			
			timeID = window.setInterval(function(){
				var inited = true;
				$.each([].concat(funcs.call()), function(i,obj){
					if(!obj){
						inited = false;
					}
					return inited;
				});
				
				if(inited && def){
					// 改变deferred对象的执行状态为：已完成
	        		def.resolve(); 
	        		window.clearInterval(timeID);
				}
			}, 1000);
	    }).promise()
	    
		).done(callback);
		
	};

})(jQuery);