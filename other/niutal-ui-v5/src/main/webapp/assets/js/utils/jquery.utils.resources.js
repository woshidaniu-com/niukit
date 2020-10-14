/**
 * @discretion	: default messages for the jQuery UIPainter plugin.
 * @author    	: wandalong 
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/uiwidget.UIPainter-1.0.0.css
 				  3.引用效果的具体js代码文件 uiwidget.UIPainter-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").UIPainter({
							afterRender : function(){
								//这个方法是初始化后的回调函数，在需要做一些事情的时候重写即可
							
							}
						});
						
					});
					</script>
 */
;(function($){
	
	var defaults = {
		media	:	"all",
  		charset	:	"utf-8",
  		ignore  :	true, //是否忽略单个文件加载异常;默认true
  		cache	:   true, //是否进行缓存;默认true
  		cacheURL:   true, //是否进行URL缓存，如果缓存，则在同一个document对象下,不会多次加载
  		async	:   false,  //是否异步请求;默认false
  		progress:   $.noop, //单个加载过程中的回调函数
  		complete:   $.noop  //单个加载完成的回调函数
	};
	
	$.extend(Array.prototype, {
		/**
		 * @discretion: 测试指定项是否在数组中存在
		 * @method : contains
		 * @param :
		 *            item (object) 要进行测试的项
		 * @param :
		 *            from (number, 可选: 默认值为0) 在数组中开始搜索的起始位索引
		 * @return : (boolean) 如果数组包含给出的项,则返回true; 否则返回false
		 */
		contains : function(item, from) {
			var bl = false, from = from || 0;
			for (var index = from; index < this.length; index++) {
				if (item == this[index]) {
					bl = true;
					break;
				}
			}
			return bl;
		}
	});
		
	$.extend({
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
		
	function styleOnload(node, callback) {
	    // for IE6-9 and Opera
	    if (node.attachEvent) {
	    	node.attachEvent('onload', callback);
	      // NOTICE:
	      // 1. "onload" will be fired in IE6-9 when the file is 404, but in
	      // this situation, Opera does nothing, so fallback to timeout.
	      // 2. "onerror" doesn't fire in any browsers!
	    }
	    // polling for Firefox, Chrome, Safari
	    else {
	      setTimeout(function() {
	    	  poll(node, callback);
	      }, 0); // for cache
	    }
	}
	
	function poll(node, callback) {
	    if (callback.isCalled) {
	    	return;
	    }
	    var isLoaded = false;
	    if (/webkit/i.test(navigator.userAgent)) {//webkit
	    	if (node['sheet']) {
	    		isLoaded = true;
	    	}
	    }
	    // for Firefox
	    else if (node['sheet']) {
	    	try {
	    	  	if (node['sheet'].cssRules) {
	    	  		isLoaded = true;
	        	}
	      	} catch (ex) {
	    	  // NS_ERROR_DOM_SECURITY_ERR
	    	  if (ex.code === 1000) {
	    		  isLoaded = true;
	    	  }
	      	}
	    }
	    if (isLoaded) {
	    	// give time to render.
	    	setTimeout(function() {
	    		callback();
	    	}, 1);
	    }
	    else {
	    	setTimeout(function() {
	    		poll(node, callback);
	    	}, 1);
	   }
	}
	  
	/**
	 * 
	 * $(selector).include(url,{
	 * 		media	:	"all",
	 * 		charset	:	"utf-8",
	 * 		ignore  :	true|false, 是否忽略单个文件加载异常;默认true
	 * 		cache	:   true|false, 是否进行缓存;默认true
	 * 		cacheURL:   true|false, 是否进行URL缓存，如果缓存，则在同一个document对象下,不会多次加载
	 * 		async	:   true|false  是否异步请求;默认false
	 * }).done(function(){ 
	 * 		
	 * }).fail(function(){
	 * 	
	 * }).always(function(){
	 * 		
	 * });
	 * 
	 */
	$.fn.include = function(_url,option){
		var $this = $(this[0]);
		var urlsArray = $.data($this,"cacheURL")||[];
		//利用延时对象按顺序加载资源
        return $.when($.Deferred(function(dtd){
        	if($.matchURL(_url)){
        		//如果资源已经下载到了本地 
				if(urlsArray.contains(_url)){
					// 改变deferred对象的执行状态为：已完成
					dtd.resolve(); 
				}else{
					
					try {
						
						//扩展参数
						var options = $.extend( true ,{},  defaults , $($this).data(), ((typeof option == 'object' && option) ? option : {}), { 
		    				dataType: "text", 
		    				url		: _url
		    			});
						
						var getUID = function(prefix) {
							do {
							  	prefix += ~~(Math.random() * 1000000);
						  	}while (document.getElementById(prefix));
						  	return prefix;
						};
						 //设置是否启用缓存和是否异步
			            $.ajaxSetup({
			            	async	: options.async,
		    				cache	: options.cache
						});
						//匹配url
						if($.matchJS(_url)){
							var script = document.createElement( "script" );
							//设置属性
					            script.language = "javascript";
					            script.type = "text/javascript";
					            script.id = getUID("script");
				            //Ajax请求js文件；追加到script标签中
							$.ajax($.extend(true,options , { 
								success: function(responseText){
								 	try{
						                //IE8以及以下不支持这种方式，需要通过text属性来设置
						                script.appendChild(document.createTextNode(responseText));
						            }catch (ex){
						                script.text = responseText;
						            }
						            // 改变deferred对象的执行状态为：已完成
									dtd.resolve(); 
							   	},
							   	error:function(){
							   		document.removeChild(script);
							   		//是否忽略错误项
							   		if(options.ignore && options.ignore != true){
									   // 改变Deferred对象的执行状态为：已失败
									   dtd.reject(); 
							   		}
							   	}
							}));  
							//添加子节点
			                $this[0].appendChild(script);  
						}else if($.matchCss(_url)){
							var style = document.createElement("style");
							//设置属性
								style.id = getUID("css");
								style.media = options.media || "all";
							//Ajax请求css文件；追加到style标签中
							$.ajax($.extend(true,options , { 
								success: function(responseText){
								 	if (style.styleSheet) { //for ie
								 		style.styleSheet.cssText = responseText;
								 	} else {//for w3c
								 		style.appendChild(document.createTextNode(responseText));
									}
								 	// 改变deferred对象的执行状态为：已完成
									dtd.resolve(); 
							   },
							   error:function(){
								   document.removeChild(style);
								   //是否忽略错误项
							   	   if(options.ignore && options.ignore != true){
									   // 改变Deferred对象的执行状态为：已失败
									   dtd.reject(); 
								   }
							   	}
							}));
							//添加子节点
			                $this[0].appendChild(style);  
						}
						//还原是否启用缓存和是否异步默认值
						$.ajaxSetup({ 
							async	: true, 
		    				cache	: true 
						});
						//如果缓存则将url加入本地Array以便标识已经加载过
		                if(options.cacheURL == true){
		                	urlsArray.push(_url);
		                	$.data($this,"cacheURL",urlsArray)
		                }
		                
					}catch (e) {
						// 改变Deferred对象的执行状态为：已失败
						dtd.reject();
						throw new Error(e);
					}
				}
    		}else{
    			// 改变deferred对象的执行状态为：已完成
				dtd.resolve(); 
    		}
        }).promise()); // 返回promise对象
	};
	
	/**
	 * 
	 * $(selector).includes(urls,{
	 * 		media	:	"all",
	 * 		charset	:	"utf-8",
	 * 		ignore  :	true|false, 是否忽略单个文件加载异常;默认true
	 * 		cache	:   true|false, 是否进行缓存;默认true
	 * 		cacheURL:   true|false, 是否进行URL缓存，如果缓存，则在同一个document对象下,不会多次加载
	 * 		async	:   true|false  是否异步请求;默认false
	 * }).done(function(){ 
	 * 		
	 * }).fail(function(){
	 * 	
	 * }).always(function(){
	 * 		
	 * });
	 * 
	 */
	$.fn.includes = function(urls,options){
		var $this = $(this);
		//过滤url
		urls = $.grep($.makeArray(urls || []),function(url,i){
			return $.matchURL(url);
		},false)
		var includes = [];
        for (var i = 0; i < urls.length; i++) {
        	includes[i] = $.Deferred(function(dtd){
        		$($this).include(urls[i],options).fail(function(){
        			//是否忽略错误项
			   	   	if(options.ignore && options.ignore != true){
			   	   		// 改变Deferred对象的执行状态为：已失败
			   	   		dtd.reject(); 
			   	   	}
        		}).always( function(){ 
        			// 改变deferred对象的执行状态为：已完成
					dtd.resolve(); 
        		});
        	}).promise();// 返回promise对象
        };
        //利用延时对象按顺序加载资源
        return $.when(includes);
	};
	
	/**
	 * 
	 * $(selector).loadResource(url,{
	 * 		media	:	"all",
	 * 		charset	:	"utf-8",
	 * 		ignore  :	true|false, 是否忽略单个文件加载异常;默认true
	 * 		cache	:   true|false, 是否进行缓存;默认true
	 * 		cacheURL:   true|false, 是否进行URL缓存，如果缓存，则在同一个document对象下,不会多次加载
	 * 		async	:   true|false  是否异步请求;默认false
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
	$.fn.loadResource = function(_url,option){
		var $this = $(this[0]);
		var urlsArray = $.data($this,"cacheURL")||[];
    	//利用延时对象按顺序加载资源
        return $.when($.Deferred(function(dtd){
        	
			if($.matchURL(_url)){
				//如果资源已经下载到了本地 
				//alert("contains:" + urlsArray.contains(_url) );
				if(urlsArray.contains(_url)){
					// 改变deferred对象的执行状态为：已完成
					dtd.resolve(); 
				}else{
					
					try {
						//扩展参数
						var options = $.extend( true ,{},  defaults , $($this).data(), ((typeof option == 'object' && option) ? option : {}));
						var getUID = function(prefix) {
							do {
							  	prefix += ~~(Math.random() * 1000000);
						  	}while (document.getElementById(prefix));
						  	return prefix;
						};
						//匹配url
						if($.matchJS(_url)){
							/*
							任何以添加 script 节点(例如 appendChild(scriptNode) ) 的方式引入的js文件都是异步执行的 (scriptNode 需要插入document中，只创建节点和设置 src 是不会加载 js 文件的，这跟 img 的预加载不能类比 )
		                    html文件中的<script>标签中的代码或src引用的js文件中的代码是同步加载和执行的
		                    html文件中的<script>标签中的代码使用document.write()方式引入的js文件是异步执行的
		                    html文件中的<script>标签src属性所引用的js文件的代码内再使用document.write()方式引入的js文件是同步执行的 
		                    */
							//同步加载js
							if(options.async==false){
								//设置是否启用缓存和是否异步
					            $.ajaxSetup({
					            	async	: options.async,
				    				cache	: options.cache
								});
								$.getScript(_url).done(function() {
									if($.isFunction(options.complete)){
		                    			//执行回调函数
		                        		options.complete.call(this);
		                        	}
									// 改变deferred对象的执行状态为：已完成
									dtd.resolve();
									
									//如果缓存则将url加入本地Array以便标识已经加载过
					                if(options.cacheURL == true){
					                	urlsArray.push(_url);
					                	$.data($this,"cacheURL",urlsArray)
					                }
					                
								}).fail(function() {
									//是否忽略错误项
							   		if(options.ignore && options.ignore != true){
									   // 改变Deferred对象的执行状态为：已失败
									   dtd.reject(); 
							   		}
								});
								//还原是否启用缓存和是否异步默认值
								$.ajaxSetup({ 
									async	: true, 
				    				cache	: true 
								});
							}else{
								
								//异步加载js
								var script	= document.createElement('script');
									script.setAttribute('id', getUID("script"));
									script.setAttribute('type', 'text/javascript');
									script.setAttribute('media', options.media || "all");
									script.setAttribute('charset', options.charset || "utf-8");
				                    script.setAttribute('src', _url);
				                    //同步异步判断
				                    if(options.async==true){
				                    	script.setAttribute('async', true);
				                    	script.setAttribute('defer', true);
				                    }else{
				                    	script.setAttribute('async', false);
				                    }
				                    
			                	// 先把js/css加到页面；如果script节点没有添加到DOM树中，在chrome和firefox中不会响应script的load事件
				                $this[0].appendChild(script);  
				                
								//释放占用资源
			                    var freeSpace = function(){
			                    	//防止IE内存泄露  
			                    	script.onload = script.onreadystatechange = script.onerror = null;
			                    	if ($this[0] && script.parentNode) { 
			                    		 $this[0].removeChild(script);
			                    		 script=null;
			                    	}
			                    }
			                    
								//加载完成后的回调函数
			                    var loading = function(eventMethod){
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
			    						
			    						//如果缓存则将url加入本地Array以便标识已经加载过
						                if(options.cacheURL == true){
						                	urlsArray.push(_url);
						                	$.data($this,"cacheURL",urlsArray)
						                }
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
			                    	if(script){
			                    		if( (!script.readyState)|| (/loaded|complete/.test(script.readyState)) ){
			                                window.clearInterval(timer);
			                            }else{
			                            	if($.isFunction(options.progress)){
			                            		options.progress.call(script);
			                            	}
			                            }
			                    	}else{
			                    		window.clearInterval(timer);
			                    	}
			                    },100);
								
				                //解决 onload和onreadystatechange都生效时；回调两次的问题
								var isLoaded = false;
								/*if(script_link.attachEvent)  {//IE5+
			                    	script_link.attachEvent("onreadystatechange", function() { 
			                    		loading.call(this);
			                    	}); 
			                    	script_link.attachEvent("onerror", function() { 
			                    		clear.call(this);
			                    	});
			                    }else if (script_link.addEventListener) {//W3C标准
			                    	script_link.addEventListener('load', function(){
			                    		loading.call(this);
			                    	}, false);
			                    	script_link.addEventListener('error', function () {
			                    		clear.call(this);
			                    	}, false);
			                    }else {*/
									
			                    //}
								//script 标签，IE 下有 onreadystatechange 事件, w3c 标准有 onload 事件
								//这些 readyState 是针对IE8及以下的，W3C 标准的 script 标签没有 onreadystatechange 和 this.readyState ,
								//文件加载不成功 onload 不会执行，
								//(!this.readyState) 是针对 W3C标准的, IE 9 也支持 W3C标准的 onload 
								/**
		                    	 * onreadystatechange: 支持: IE6-9/OP, 不支持: FF/Webkit (这里有区别，OP支持js创建的js元素，readyState为loaded)
		                    	 * 兼容写法：script.onload = script.onreadystatechange
		                    	 *  	onload为IE6-9/OP下创建CSS的时候，或IE9/OP/FF/Webkit下创建JS的时候
								 * 		onreadystatechange为IE6-9/OP下创建CSS或JS的时候
		                    	 */
		                    	script.onload = script.onreadystatechange = function () {  
		                    		if (isLoaded) { return; }
		                    		loading.call(this);
			                    	isLoaded = true;
			                    };
			                    script.onerror = function(){
			                    	clear.call(this);
			                    }
							}
						}else if($.matchCss(_url)){
							var link 			= window.document.createElement('link');
								link.setAttribute('id', getUID("css"));
								link.setAttribute('type', 'text/css');
								link.setAttribute('rel', 'stylesheet');
								link.setAttribute('media', options.media || "all");
								link.setAttribute('charset', options.charset || "utf-8");
								link.setAttribute('href', _url);
							// 先把css加到页面
							$this[0].appendChild(link); 
							//轮训判断css加载完毕
						    styleOnload(link,function(){
						    	// 改变deferred对象的执行状态为：已完成
								dtd.resolve();
								//如果缓存则将url加入本地Array以便标识已经加载过
				                if(options.cacheURL == true){
				                	urlsArray.push(_url);
				                	$.data($this,"cacheURL",urlsArray)
				                }
						    });
						}
						
					} catch (e) {
						// 改变Deferred对象的执行状态为：已失败
						dtd.reject();
						throw new Error(e);
					}
				}
			}else{
				// 改变deferred对象的执行状态为：已完成
				dtd.resolve(); 
			}
        }).promise());// 返回promise对象  
	}
	
	/**
	 * 
	 * $(selector).loadResources([url_1,url_2,...],{
	 * 		media	:	"all",
	 * 		charset	:	"utf-8",
	 * 		ignore  :	true|false 是否忽略单个文件加载异常;默认true
	 * 		cache	:   true|false, 是否进行缓存;默认true
	 * 		cacheURL:   true|false, 是否进行URL缓存，如果缓存，则在同一个document对象下,不会多次加载
	 * 		async	:   true|false  是否异步请求;默认false
	 * }).done(function(){ 
	 * 		
	 * }).fail(function(){
	 * 	
	 * }).always(function(){
	 * 		
	 * });
	 * 
	 */
	$.fn.loadResources = function(urls,options){
		var $this = $(this);
		//过滤url
		urls = $.grep($.makeArray(urls || []),function(url,i){
			return typeof url == "string" && $.trim(url).length > 0 && /(?:\S+\.(?:js|css)(?:\?ver=\S+)?)/.test($.trim(url).toLowerCase());
		},false)
		var resources = [];
        for (var i = 0; i < urls.length; i++) {
        	resources[i] = $.Deferred(function(dtd){
    			$($this).loadResource(urls[i],options).fail(function(){
        			//是否忽略错误项
			   	   	if(options.ignore && options.ignore != true){
			   	   		// 改变Deferred对象的执行状态为：已失败
			   	   		dtd.reject(); 
			   	   	}
        		}).always( function(){ 
        			// 改变deferred对象的执行状态为：已完成
					dtd.resolve(); 
        		});
    		}).promise();// 返回promise对象
        };
        //利用延时对象按顺序加载资源
        return $.when(resources);
	};
	
	
}(jQuery));