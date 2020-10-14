
var extendStr = {
	messages : {
        rmb_digits : [ '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' ], // 整数0～9对应的汉字零～玖
		rmb_radices : [ "", "拾", "佰" ,"仟"], // 段内位置表示
        rmb_bigRadices :["", "万", "亿"],
        rmb_decimals : ["角","分"],//圆
        rmb_suffix :["整","元"],//圆
        
        rmb_hunit : [ '拾', '佰', '仟' ], // 段内位置表示
        rmb_vunit : [ '万', '亿' ], // 段名表示
        
          
        rmb_unit :["元","角","分"],
        
        rmb_unit2 :[" ","元","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟"],
        rmb_unit3 :[ '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万']
	},
	isNumber: function(value, element) {
        return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
    },	   
    replaceAll:function(os, ns) {
        return String(this).replace(new RegExp(os,"gm"),ns);
    },         
    skipChar:function(ch) {
        if (!this || String(this).length===0) {return '';}
        if (String(this).charAt(0)===ch) {return String(this).substring(1).skipChar(ch);}
        return this;
    },
    toInt : function(base) {
	    return parseInt(this, base || 10);
	},
	toFloat : function() {
	    return parseFloat(this);
	},
	//输出16进制数的函数
		toHex:function(){
		var hexValue = "",temp_str="";
		var i,lByte;
		for(i = 0; i < 4; i++){
			lByte = (this >>(i*8))& 0xFF;
			temp_str = "0" + lByte.toString(16);
			hexValue += temp_str.substr(temp_str.length - 2, 2);
		}
		return hexValue;
	},
    equals:function(str) {
        return this==str;
    },
    append:function(str) {
        return String(this) + str;
    },
    reverse:function() {
    	return String(this).serializeArray().reverse().join("");
	},
	isContain:function(str) {
        return (String(this).indexOf(str)>-1);
    },
    isEmpty:function() {
        return (this&&String(this).length>0)?false:true;
    },
    /**
     * @discretion: 去除字符中的空白字符，返回处理后的字符
     * @method    : trim
     * @return    : {String}
     */
    trim:function(){
         return String(this).replace(/\s+/ig, '');///^s*|s*$/
    },
    trans:function() {
        return String(this).replace(/&lt;/g, '<').replace(/&gt;/g,'>').replace(/&quot;/g, '"');
    },
    trimBE : function(arg) {// 去除首尾空白字符
		return String(this).replace(/ns*r/, "");
	},
	trimHtml : function(arg) {// 去除HTML标记
		return String(this).replace(/<(S*?)[^>]*>.*?|< .*? \/>/, "");
	},
    /**
     * @discretion: 将字符串序列化成Array对象
     * @method    : serializeArray
     * @return    : {Array} 
     * @notice    : parse string to array by bit
     */
    serializeArray :function(s,bit) {
    	if(typeof this == 'string'){
    		var ret = [];
            if(s){
                ret = String(this).split(s || "|");
            }else{
                for(var i=0;i<this.length;i++){
                   ret.push(String(this).charAt(i)); 
                }
            }
		    return bit ? function(l, n){
		        for(; l--;) ret[l] = parseInt(ret[l], bit);
		        return ret;
		    }(ret.length) : ret;
    	}
    	return [];
	},
	//1901-10-03 23:35:26 (\s|\S)+ [-\/]
	//1901/10/03 23:35:26
	//1901/10/03 23:35:26
	/**
     * @discretion: 将一个日期格式的字符串格式化成日期对象
     * @method    : toDate
     * @return    : {Date} 
     * @notice    : 目前支持：1901/10/03 或 1901/10/03 23:35:26 
     						1901-10-03 或 1901-10-03 23:35:26
     						1901年10月03日 或 1901年10月03日 23:35:26
     */
	toDate:function() {
		if(/^(\d{4})[-\/\年](\d{1,2})[-\/\月](\d{1,2})(\s?|日\s?)(([0-1][0-9]|2[0-3])[:]([0-5]{1}[0-9]{1})[:]([0-5]{1}[0-9]{1}))?$/mg.test(String(this))){
		    var year = RegExp.$1, 
			    month = parseInt(RegExp.$2)-1, 
			    date = RegExp.$3, 
			    hours = RegExp.$6?RegExp.$6:0, 
			    minutes = RegExp.$7?RegExp.$7:0, 
			    seconds = RegExp.$8?RegExp.$8:0;
		   	return new Date(year,month,date,hours,minutes,seconds);
		}else{
			return null;
		}
	},
	toTime:function() {
		if(/^([0-1][0-9]|2[0-3])[:]([0-5]{1}[0-9]{1})[:]([0-5]{1}[0-9]{1})?$/mg.test(String(this))){
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
	 /**
     * @discretion: 序列化字符串 返回 JSON 数据结构数据
     * @method    : toJSON
     * @return    : {JSONObject} 
     * @notice    : parse string to json object
     */
	toJSON:function(){
		var json = {};
		String(this).replace(/(([^|&|\?](\d|[a-zA-Z]|[\.])+)=([\.\-]|\d|[a-zA-Z]|[\u00a0-\ud7ff\uf900-\ufdcf\ufdf0-\uffef])+([^&]*))+/img, function(regex, match) {
			var arr = match.split("=");
			json[arr[0].trim()] = arr[1];
        });
        //alert("json:"+json["bean.sfsc"]);
        return json;
	},
	getString:function(key){
		var reg = new RegExp("(^|\\?|&)"+ key +"=([^&]*)(\s|&|$)", "ig");
		if (reg.test(String(this))){
			return unescape(RegExp.$2.replace(/\+/g, " "));
		}
		return "";
	},
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
	replacement : function(){
		var i = 0,value = String(this);
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
	},
    /**
     * @discretion: 用当前字符格式化对象，返回格式化后的字符串
     * @method    : format
     * @param     : {Array || Object || String } params
     * @return    : {String}
     * @notice    : 1.支持对象格式化：${y}-${m}-${d} 星期${W} 
     *              2.支持数组格式化：{0}您好！现在是{1}年{2}月{3}日   
     * @example   : {String}.format(params)
     */
    format:function(params) {
		var source = String(this).replacement();
		if (arguments.length == 0){
	        return function () {
	            var args = $.makeArray(arguments);
	            args.unshift(source);
	            return source.format.apply(this, args);
	        }.call(this, arguments);
		}
		if (arguments.length > 2 && params.constructor != Array) {
			params = $.makeArray(arguments).slice(1);
 	    }
		//数字
		if(!isNaN(this) && Number.prototype.format){
			/**
			 * 此时 params应为数字格式，如： '###,###,###.######'，"#####年##月##日"
			 */
			return Number(this).format(params);
		}
		//字符
		else{
			//转换日期
			var date = source.toDate ? source.toDate() : null;
			//日期
			if(date != null && Date.prototype.format){
				/**
				 * 此时 params应为日期格式，'yyyy-MM-dd', 如："yyyy年M月d日", "yyyy-MM-dd", "MM-dd-yy", "yyyy-MM-dd q", "yyyy-MM-dd hh:mm:ss S"
				 * 为区分minute, month对应大写M
				 * y: 年, M: 月, d: 日,w: 周几,h: 时,m: 分,s: 秒 ,S: 毫秒 ,q: 季度
				 * 年份位数不全时: 1. 年份 "yyyy" --> 2011, "yyy" --> 011, "yy" --> 11; "y" --> 1;
				 */
				return date.format(params);
			}else{
				if($.isArray(params)){
	                return source.replace(/\{([^}]*)\}/mg,function(regex, match) {
	                    return regex = params[match]
	                });
	            }else if($.isPlainObject(params)){
	                return source.replace(/\$\{([^}]*)\}/mg, function(regex, match) {
	                    return regex = params[match.trim()]
	                });
	            }
			}
		}
		return source;
    },
    delFormat:function() {
        return String(this).replace(/,/g,"");
    },
    
    /**
     * @discretion: 从华氏到摄氏的转换
     * @method    : trim
     * @return    : {String}
     * @notice    : 传递一个包含数值的字符串，数值后要紧跟 "F" （例如 "Water boils at 212"）
     */
    f2c:function() {
        return String(this).replace(/(\d+(\.\d*)?)F\b/g,function($0,$1,$2) { 
            return((($1-32) * 5/9) + "C");
          }
        );
    },
    /**
     * @discretion: 将数字字符串转成大写人民币字符串
     * @method    : toRMBString
     * @return    : {String} 
     * @notice    : 
    */
    toRMBString:function() {
        //用正则表达式给使用该函数的数字做测试，是否符合[正负（可有可无）]、[数字（重复一次以上）]、[小数点（可有可无）、数字（可有可无）]的形式。
        if(!(/^(\+|\-)?(\d+)(\.\d+)?$/.test(String(this)))) { 
            return NaN; //返回Not A Number，终止。
        }
        if (Number(this) > 99999999999.99) {
            alert(this + " Too large a number to convert!");
            return "";
        }
         var rmb_digits = this.messages.rmb_digits;
         var rmb_suffix = this.messages.rmb_suffix;
         var weiShu = this.messages.rmb_unit2;
         var rmb_vunit = this.messages.rmb_vunit;
        //String(Math.round(parseFloat(this) * 100 + 0.00001));
        var str = String(parseFloat(Number(this).toFixed(2)*100 ));
        var result = "";
        for (var i = 0; i < str.length; i++) {
            var n = str.charAt(str.length - 1 - i) - '0';
            result = rmb_digits[n] + "" + this.messages.rmb_unit3[i] + result;
        }
        result = result.replace("零仟", rmb_digits[0]);
        result = result.replace("零佰", rmb_digits[0]);
        result = result.replace("零拾", rmb_digits[0]);
        result = result.replace("零亿", "亿");
        result = result.replace("零万", "万");
        result = result.replace("零元", "元");
        result = result.replace("零角", rmb_digits[0]);
        result = result.replace("零分", rmb_digits[0]);
        result = result.replace("零零", rmb_digits[0]);
        result = result.replace("零亿", "亿");
        result = result.replace("零零", rmb_digits[0]);
        result = result.replace("零万", "万");
        result = result.replace("零零", rmb_digits[0]);
        result = result.replace("零元", "元");
        result = result.replace("亿万", "亿");
        result = result.replace(/零拾/, "");
        result = result.replace(/零$/, "");
        result = result.replace(/元$/, "元整");
        return result;
    }
};

for ( var method in extendStr) {
	String.prototype[method] = extendStr[method];
};
