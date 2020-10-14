/*
 * @discretion : 扩展添加获取元素border,margin,padding等css样式方法
 * @author : wandalong
 * @email : hnxyhcwdl1003@163.com
 */
;(function($) {

	/*
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
	$.replacement = function(value){
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
	};
	
	/* 调用方法 
		var text = "a{0}b{0}c{1}d\nqq{0}"; 
		var text2 = $.format(text, 1, 2); 
		alert(text2); 
	*/
	$.format = function (source, params) {
		source = $.replacement(source);
		if (arguments.length == 1){
	        return function () {
	            var args = $.makeArray(arguments);
	            args.unshift(source);
	            return $.format.apply(this, args);
	        };
		}
		if (arguments.length > 2 && params.constructor != Array) {
 	        params = $.makeArray(arguments).slice(1);
 	    }
		//alert("params:" + params)
		//数字
		if(!isNaN(source) && Number.prototype.format){
			/**
			 * 此时 params 应为数字格式，如： '###,###,###.######'，"#####年##月##日"
			 */
			return Number(source).format(params);
		}
		//日期
		else if($.type(source) === "date"  && Date.prototype.format){
			/**
			 * 此时 params 应为日期格式，'yyyy-MM-dd', 如："yyyy年M月d日", "yyyy-MM-dd", "MM-dd-yy", "yyyy-MM-dd q", "yyyy-MM-dd hh:mm:ss S"
			 * 为区分minute, month对应大写M
			 * y: 年, M: 月, d: 日,w: 周几,h: 时,m: 分,s: 秒 ,S: 毫秒 ,q: 季度
			 * 年份位数不全时: 1. 年份 "yyyy" --> 2011, "yyy" --> 011, "yy" --> 11; "y" --> 1;
			 */
			return source.format(params);
		}
		//字符
		else if($.type(source) === "string"){
			//alert("source:" + source);
			/**
			 * 1.支持对象格式化,此时params为对象：${y}-${m}-${d} 星期${W}  
			 * 2.支持数组格式化,此时params为数组：{0}您好！现在是{1}年{2}月{3}日 
			 */
			if(String.prototype.format){
				return new String(source).format(params);
			}else{
				if($.isArray(params)){
		            return source.replace(/\{([^}]*)\}/mg,function(regex, match) {
		                return regex = params[match]
		            });
		        }else if($.isPlainObject(params)){
		            return source.replace(/\$\{([^}]*)\}/mg, function(regex, match) {
		                return regex = params[match.trim()]
		            });
		        }else if($.type(params) === "string"){
		        	if(String.prototype.toDate){
		        		//转换日期
						var date = source.toDate();
						//日期
						if(date != null && Date.prototype.format){
							/**
							 * 此时 params应为日期格式，'yyyy-MM-dd', 如："yyyy年M月d日", "yyyy-MM-dd", "MM-dd-yy", "yyyy-MM-dd q", "yyyy-MM-dd hh:mm:ss S"
							 * 为区分minute, month对应大写M
							 * y: 年, M: 月, d: 日,w: 周几,h: 时,m: 分,s: 秒 ,S: 毫秒 ,q: 季度
							 * 年份位数不全时: 1. 年份 "yyyy" --> 2011, "yyy" --> 011, "yy" --> 11; "y" --> 1;
							 */
							return date.format(params);
						}
		        	}else{
		        		$.each($.makeArray(params), function (i, n) {
			    	        source = source.replace(new RegExp("\\{" + i + "\\}", "mg"), n);
			    	    });
		        	}
		        }
			}
		}
		return source;
	};

}(jQuery));