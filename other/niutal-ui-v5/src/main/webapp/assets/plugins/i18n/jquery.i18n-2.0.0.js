/**
 * 
1、国际化信息设值、取值
$.i18n.set("key",param1)
$.i18n.get("key")
$.i18n.prop("key",param1,param2,param2,...)
$.i18n.prop("key",[params])
2、国际化信息删除
$.i18n.remove("key")
$.i18n.clear()
3、格式化信息
$.i18n.format(source,[param1,param2,param2,...])
4、元素的国际化
$(selector).localize()
$.i18n.localize()
5、grid国际化
	//是否进行grid国际化：需要 jquery.i18n插件的支持
	i18nable	: true
$.i18n.grid(options)
 * 
 */
;(function($) {
	
	
	$.fn.localize = function(){
		return $(this).each(function(){
			var elem = $(this);
	        var localize = elem.data("localize");
	        if($.founded(localize)){
	        	var localizedValue = $.i18n.get(localize);
		        if (elem.is("input[type=text]") || elem.is("input[type=password]") || elem.is("input[type=email]")) {
		        	elem.attr("placeholder", localizedValue);
		        } else if (elem.is("input[type=button]") || elem.is("input[type=submit]")) {
		        	elem.attr("value", localizedValue);
		        } else {
		        	elem.text(localizedValue);
		        }
	        }
		});
	};
	
	$.i18n = $.i18n || {};
	
	/** Map holding bundle keys (if mode: 'map') */
	$.i18n.map = $.i18n.map || {};
	
	
	$.i18n.set = function(i18nKey,i18nValue){
		$.i18n.map[i18nKey] = i18nValue;
	};
	
	$.i18n.get = function(i18nKey){
		var i18nValue = $.i18n.map[i18nKey];
		if(!i18nValue){
			return null;
		}
		return i18nValue;
	};
	
	$.i18n.remove = function(i18nKey){
        if(i18nKey){
			$.i18n.map[i18nKey] = null;
			delete $.i18n.map[i18nKey];
		}                 
	};
	
	$.i18n.clear = function(){
		$.i18n.map = {};
	};
	
	/**
	 * 加载当前功能国际化资源,并对包含属性data-localize的元素进行国际化处理
	 * 如：<label for="" class="col-sm-5 control-label" data-localize="kkbm">开课部门</label>
	 */
	$.i18n.localize = function(){
		//处理默认国际化
		$("[data-localize]").each(function() {
			$(this).localize();
      	});
	};
	
	// http://jqueryvalidation.org/jQuery.validator.format/
	$.i18n.format = function( source, params ) {
		if ( arguments.length === 1 ) {
			return function() {
				var args = $.makeArray(arguments);
				args.unshift(source);
				return $.i18n.format.apply( this, args );
			};
		}
		if ( arguments.length > 2 && params.constructor !== Array  ) {
			params = $.makeArray(arguments).slice(1);
		}
		if ( params.constructor !== Array ) {
			params = [ params ];
		}
		
		$.each(params, function( i, n ) {
			source = $.i18n.replacement(source).replace( new RegExp("\\{" + i + "\\}", "g"), function() {
				return n;
			});
		});
		return source;
	};
	
	/**
     * Tested with:
     *   test.t1=asdf ''{0}''
     *   test.t2=asdf '{0}' '{1}'{1}'zxcv
     *   test.t3=This is \"a quote" 'a''{0}''s'd{fgh{ij'
     *   test.t4="'''{'0}''" {0}{a}
     *   test.t5="'''{0}'''" {1}
     *   test.t6=a {1} b {0} c
     *   test.t7=a 'quoted \\ s\ttringy' \t\t x
     *
     * Produces:
     *   test.t1, p1 ==> asdf 'p1'
     *   test.t2, p1 ==> asdf {0} {1}{1}zxcv
     *   test.t3, p1 ==> This is "a quote" a'{0}'sd{fgh{ij
     *   test.t4, p1 ==> "'{0}'" p1{a}
     *   test.t5, p1 ==> "'{0}'" {1}
     *   test.t6, p1 ==> a {1} b p1 c
     *   test.t6, p1, p2 ==> a p2 b p1 c
     *   test.t6, p1, p2, p3 ==> a p2 b p1 c
     *   test.t7 ==> a quoted \ s	tringy 		 x
     */
	$.i18n.replacement = function(value){
		var i;
	    if (typeof(value) == 'string') {
	    	// Handle escape characters. Done separately from the tokenizing loop below because escape characters are
	    	// active in quoted strings.
	    	i = 0;
	    	while ((i = value.indexOf('\\', i)) != -1) {
		        if (value.charAt(i + 1) == 't'){
		          value = value.substring(0, i) + '\t' + value.substring((i++) + 2); // tab
		        }else if (value.charAt(i + 1) == 'r'){
		          value = value.substring(0, i) + '\r' + value.substring((i++) + 2); // return
		        }else if (value.charAt(i + 1) == 'n'){
		          value = value.substring(0, i) + '\n' + value.substring((i++) + 2); // line feed
		        }else if (value.charAt(i + 1) == 'f'){
		          value = value.substring(0, i) + '\f' + value.substring((i++) + 2); // form feed
		        }else if (value.charAt(i + 1) == '\\'){
		          value = value.substring(0, i) + '\\' + value.substring((i++) + 2); // \
		        }else{
		          value = value.substring(0, i) + value.substring(i + 1); // Quietly drop the character
		        }
	    	}
	    	// Lazily convert the string to a list of tokens.
	    	var arr = [], j, index;
	    	i = 0;
	    	while (i < value.length) {
	    		if (value.charAt(i) == '\'') {
	    			// Handle quotes
	    			if (i == value.length - 1){
	    				value = value.substring(0, i); // Silently drop the trailing quote
	    			}else if (value.charAt(i + 1) == '\''){
	    				value = value.substring(0, i) + value.substring(++i); // Escaped quote
	    			}else {
	    				// Quoted string
	    				j = i + 2;
	    				while ((j = value.indexOf('\'', j)) != -1) {
	    					if (j == value.length - 1 || value.charAt(j + 1) != '\'') {
	    						// Found start and end quotes. Remove them
	    						value = value.substring(0, i) + value.substring(i + 1, j) + value.substring(j + 1);
	    						i = j - 1;
	    						break;
	    					}
	    					else {
				                // Found a double quote, reduce to a single quote.
				                value = value.substring(0, j) + value.substring(++j);
	    					}
	    				}

	    				if (j == -1) {
	    					// There is no end quote. Drop the start quote
	    					value = value.substring(0, i) + value.substring(i + 1);
	    				}
	    			}
	    		} else{
	    			i++;
	    		}
	    	}
	    }
	    return value;
	}
	
	$.i18n.prop = function (i18nKey /* Add parameters as function arguments as necessary  */) {
		if (!i18nKey){
	    	return null;
	    }
		//当前语言对应的国际化信息
		var i18nValue = $.i18n.get(i18nKey);
		if (!i18nValue){
	    	return null;
	    }
	    var params = [];
	    if (arguments.length == 2 && $.isArray(arguments[1])){
	    	// An array was passed as the only parameter, so assume it is the list of place holder values.
	    	params = arguments[1];
	    }else if (arguments.length >= 2 && !$.isArray(arguments[1])) {
 	        params = $.makeArray(arguments).slice(1);
 	    }
	    // Place holder replacement
	    i18nValue = $.i18n.replacement(i18nValue);
	    if (i18nValue.length == 0){
	    	return "";
	    }else{
	    	return $.i18n.format(i18nValue,params);
	    }
    };
	
	$.i18n.grid = function(options){
		if($.isPlainObject(options) && options["i18nable"] === true){
			var colModel = options["colModel"];
			if(colModel){
				//{label:'',name:'kcdmbbb_id', index: 'kcdmbbb_id',hidden:true,key:true,align:'center'},
				$.each(colModel, function(i, item){
					if(!(item["hidden"] === true || item["key"] === true)){
						var i18nValue 	= $.i18n.get(item["label"]);
						if($.founded(i18nValue)){
							//对grid列的label值进行国际化
							item["label"] = i18nValue;
						} else{
							var i18nKey		= item["index"] || item["name"];
							var i18nValue 	= $.i18n.get(i18nKey);
							/*if(console){
								console.log(i18nKey + ' : ' + i18nValue );
							}*/
							if($.founded(i18nValue)){
								//对grid列的label值进行国际化
								item["label"] = i18nValue;
							}
						}
					}
				});
			}
		}
		return options;
	};
	
}(jQuery));