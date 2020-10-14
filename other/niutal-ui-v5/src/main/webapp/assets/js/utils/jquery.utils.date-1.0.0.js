var extendDate = {
	messages:{
        //公历天数集合
        days:[31,28,31,30,31,30,31,31,30,31,30,31],
        /**
         * @discretion: 公历季节月份划分
         * @notice    : 在公历(格里历)一般以１月份为最冷月，７月份为最热月，
         *              故以公历 3月-5月是春季，6月～8月为夏季，9月～11月为秋季，12月～2月为冬季
         */
        quarters:{"1":[3,4,5],"2":[6,7,8],"3":[9,10,11],"4":[12,1,2]},
        quarterNames:["春季","夏季","秋季","冬季"],
        number:['零','一','二','三','四','五','六','七','八','九','十','十一','腊'],
        weekNames:["周日","周一","周二","周三","周四","周五","周六"]
    },
	//--------获取基本时间的方法---------------------------------------------------------------
	/**
     * @discretion: 获取此实例的日期部分的Date对象
     * @method    : date
     * @return    : {y: 年,M: 月, d: 日,w: 星期(数字),W: 星期(中文),h: 时,m: 分,s: 秒,S: 毫秒,t: 时间戳,q:季度(数字),Q:季度(中文) ,QM:季度名称 }
     */
	date:function() {
		return {
            y	: this.year(),                               //年(数字)
            M	: this.month(),                                  //月(数字)
            d	: this.day(),                                   //日(数字)
            w	: this.week(),                                  //星期（数字）
            W	: this.messages.weekNames[this.week()],         //星期（中文）
            h	: this.hours(),                                  //时(数字)
            m	: this.minutes(),                                //分(数字)
            s	: this.seconds(),                                //秒(数字)
            S	: this.milliseconds(),                           //毫秒(数字)
            t	: this.time(),                                   //时间戳(数字)
            
            q   : this.quarter()+1,                               //公历季度(数字)
            Q   : this.messages.number[this.quarter()],           //公历季度(中文)
            QM  : this.messages.quarterNames[this.quarter()-1],   //公历季度名称 
            F	: this.feast(),                                   //公历节日
		    SF  : this.weekFeast()                             //公历月周节日
		    
        };
	},
    /**
     * @discretion: 获取此实例所表示日期的年份部分
     * @method    : year
     * @return    : {Number}
     * @notice    : 参数为四位格式的公历年
     */
    year:function() {return this.getFullYear();},
    /**
     * @discretion: 获取此实例所表示日期的所在季度
     * @method    : quarter
     * @return    : {Number} [1-4]
     * @notice    : 1:春季,2:夏季,3:秋季,4:冬季
     */
    quarter:function() {
        var ctx = this;
        var quarterVal = 1;
        $.each(this.messages.quarters,function(quarter,months){
             var noStop = true;
             $.each(months,function(index,month){
                if(month == ctx.month()){
                    quarterVal = quarter;
                    noStop =  false;
                }
                return noStop;
            });
            return noStop;
        });
        return quarterVal;
    },
    /**
     * @discretion: 获取此实例所表示日期的月份部分
     * @method    : month
     * @return    : {Number}
     * @notice    : 返回公历月份1-12
     */
	month:function(){ return this.getMonth() + 1; },
    /**
     * @discretion: 获取此实例所表示的日期是星期几
     * @method    : week
     * @return    : {Number}
     * @notice    : getDay 方法所返回的值是一个处于 0 到 6 之间的整数，它代表了一周中的某一天
     * @notice    : 返回数字0-6
     * @notice    : 0:星期天,1:星期一,2:星期二,3:星期三,4:星期四,5:星期五,6:星期六 
     */
    week:function() { return this.getDay(); },
    /**
     * @discretion: 获取此实例所表示的日期是这个月的第几天
     * @method    : day
     * @return    : {Number}
     * @notice    : 返回数字1-31
     */
	day:function(){ return this.getDate(); },
	/**
     * @discretion: 获取此实例所表示日期的小时部分
     * @method    : hours
     * @return    : {Number}
     * @notice    : 返回数字1-24
     */
	hours:function() { return this.getHours(); },
	/**
     * @discretion: 获取此实例所表示日期的分钟部分
     * @method    : minutes
     * @return    : {Number}
     * @notice    : 返回数字1-60
     */
	minutes:function() { return this.getMinutes(); },
	/**
     * @discretion: 获取此实例所表示日期的秒部分
     * @method    : minutes
     * @return    : {Number}
     * @notice    : 返回数字1-60
     */
	seconds:function() { return this.getSeconds(); },
	/**
     * @discretion: 获取此实例所表示日期的毫秒部分
     * @method    : milliseconds
     * @return    : {Number}
     * @notice    : 返回数字0-1000
     */
	milliseconds:function() { return this.getMilliseconds(); },
	/**
     * @discretion: 获取此实例所表示日期的时间戳
     * @method    : time
     * @return    : {Number}
     * @notice    : 返回数字
     */
    time:function() { return this.getTime(); },
    //---------此实例所表示日期对应的节日计算方法-----------------------------------------------------------
    /**
     * @discretion:获取此实例所表示日期的对应节日名称
     * @method: feast
     * @return: {String} || ""
     */
    feast: function() { return this.getFeast(this.month(),this.day()); },
     /**
     * @discretion:获取此实例所表示日期的对应月周节日名称
     * @method: weekFeast
     * @return: {String} || ""
     */
    weekFeast:function() { return this.getWeekFeast(this.month(),this.day()); },
    /**
     * @discretion: 获取此实例所表示日期对应的十二星座
     * @method    : zodiac
     * @return    : {String}|| ""
     */
    zodiac:function(){ return this.getZodiac(this.year(),this.month(),this.day()); },
    //---------时间长度计算-----------------------------------------------------------
    /**
     * @discretion: 获取某公历年的天数
     * @method    : yearDays
     * @return    : {Number}
     */
    yearDays:function() { return this.getYearDays(this.year()); },
    /**
     * @discretion: 获取当前时间实例所在年公历第几季度的天数
     * @method    : quarterDays
     * @return    : {Number}
     */
    quarterDays:function() { return this.getQuarterDays(this.year(),this.quarter()); },
    /**
     * @discretion: 获取指定年月的公历天数,如果未传值则取当前时间实例所在的年月
     * @method    : monthDays
     * @return    : {Number}
     */
    monthDays:function() { return this.getMonthDays(this.year(),this.month()); },
    /**
     * @discretion: 获取此实例的所在当月1日是星期几
     * @method    : firstWeek
     * @return    : {Number}
     */
    firstWeek:function() { return this.getFirstWeek(this.year(),this.month()); },
	//--------时间加减操作--------------------------------------------------------------
    /**
     * @discretion: 将指定的年份数加到此实例的值上,并返回改变后的Date对
     * @method    : addYears
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addYears:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setFullYear(this.year() + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的月份数加到此实例的值上,并返回改变后的Date对
     * @method    : addMonths
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addMonths:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setMonth(this.month() - 1  + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的天数加到此实例的值上,并返回改变后的Date对
     * @method    : addDays
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addDays:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n)) ? function(){
			var result = this.clone();
            result.setDate(this.day() + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的小时数加到此实例的值上,并返回改变后的Date对
     * @method    : addHours
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addHours:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setHours(this.hours() + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的分钟数加到此实例的值上,并返回改变后的Date对
     * @method    : addMinutes
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addMinutes:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setMinutes(this.minutes() + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的秒数加到此实例的值上,并返回改变后的Date对
     * @method    : addSeconds
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addSeconds:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setSeconds(this.seconds() + n);
			return result;
        }.call(this):NaN;
	},
	/**
     * @discretion: 将指定的毫秒数加到此实例的值上,并返回改变后的Date对
     * @method    : addMilliseconds
     * @param	  : {Number}
     * @return    : {Date}
     * @notice    : 参数必须为数字
     */
	addMilliseconds:function(n) {
		return (/^(\+|\-)?(\d+)?$/.test(n))?function(){
			var result = this.clone();
            result.setMilliseconds(this.milliseconds() + n);
			return result;
        }.call(this):NaN;
	},
    //-------时间长度计算-----------------------------------------------------------
    /**
     * @discretion: 获取某公历年的天数
     * @method    : getYearDays
     * @param     : {Number} y  4位年份
     * @return    : {Number}
     */
    getYearDays:function(y) {
        return (/^\d{4}$/.test(y))?function(){
            return new Date(y,12,31,0,0,0).differDays(new Date(y,1,1,0,0,0));
        }.call(this):NaN;
    },
    /**
     * @discretion: 获取当前时间实例所在年公历第几季度的天数
     * @method    : getQuarterDays
     * @param     : {Number} y 4位的年份
     * @param     : {Number} q [1-4]的数字
     * @return    : {Number}
     */
    getQuarterDays:function(y,n) {
        return (/^\d{4}$/.test(y)&&/^[1234]$/.test(n))?function(){
            var months = this.messages.quarters[n],days=0;
            for (var index = 0; index < months.length; index++) {
                days += this.getMonthDays(y,months[index]);
            }
            return days;
        }.call(this):NaN;
    },
    /**
     * @discretion:获取指定公历年月天数
     * @method    : getMonthDays
     * @param     : {Number} y 4位的年份
     * @param     : {Number} m [1-12]的数字
     * @return    : {Number}
     */
    getMonthDays:function(y,m) {
        return (/^\d{4}$/.test(y)&&/^\d{1,2}$/.test(m))?function(){
            return m == 2 ? this.isLeapYear(y) ? 29 : 28 : this.messages.days[m-1];
        }.call(this):NaN;
    },
    /**
     * @discretion: 获取指定公历年月当月1日是星期几
     * @method    : getFirstWeek
     * @param     : {Number} y  4位年份
     * @param     : {Number} m  月
     * @return    : {Number}
     */
    getFirstWeek:function(y,m) {
        return (/^\d{4}$/.test(y)&&/^\d{1,2}$/.test(m))?function(){
            return new Date(y,m,1,0,0,0,0).getDay();
        }.call(this):NaN;
    },
    /**
     * @discretion: 获取此实例所表示的日期是这个月的第几周
     * @method    : weekOfMonth
     * @return    : {Number}
     * @notice    : 返回数字1-7
     */
    getWeekOfMonth:function(y,m,d) {
        return (/^\d{4}$/.test(y)&&/^\d{1,2}$/.test(m)&&/^\d{1,2}$/.test(d))?function(){
        	var d1 =  new Date(y,m,1);
        	return (Math.ceil((Number(d) - d1.week())/7) + (d1.week()>0?1:0));
        }.call(this):NaN;
    },
    /**
     * @discretion:返回公历日期对应节日名
     * @method: getFeast
     * @param: {Number} month
     * @param: {Number} day
     * @return:{"name":节日名称,"holiday":true|false 是否放假};
     */
    getFeast: function(m, d) {
        return (/^\d{1,2}$/.test(m)&&/^\d{1,2}$/.test(d))?function(){
             var feasts = this.messages.feast,name="";
             for ( var index in feasts) {
             	if(feasts[index].match(/^(\d{2})(\d{2})([\s*])(.+)$/)){
             		 if(Number(RegExp.$1)== m && Number(RegExp.$2)== d) {
				        name =RegExp.$4;
				        break;
				    }
			    }
             }
             return {"name":name,"holiday":$.trim(RegExp.$3).length>0?true:false};
        }.call(this):NaN;
     },
    /**
     * @discretion:返回公历某月的第几周星期几的节日
     * @method: getWeekFeast
     * @param: {Number} month
     * @param: {Number} n
     * @param: {Number} week 
     * @return:{"name":节日名称,"holiday":true|false 是否放假};
     */
    getWeekFeast:function(m,n,w) {
     	return (/^\d{1,2}$/.test(m)&&/^\d$/.test(n)&&/^[0-6]$/.test(w))?function(){
            var name='',
		    	week_feast = this.messages.week_feast,
		    	firstWeek = this.firstWeek(this.year(),m);
		    for(var index in week_feast){
		    	if(week_feast[index].match(/^(\d{2})(\d)(\d)([\s*])(.+)$/)){
		   	 		if((Number(RegExp.$1) == m) && (Number(RegExp.$2) == n && (Number(RegExp.$3) == w))) {
				      	name = RegExp.$5;
			        }
			    }
		    }
		    return {"name":name,"holiday":$.trim(RegExp.$4).length>0?true:false};
        }.call(this):NaN;
	},
	//-------常用Date时间对象获取方法--------------------------------------------------------------------------
    /**
     * @discretion:  获取本周第n天的Date对象
     * @method    : getDayAtWeek
     * @param	  : {Number} n 指定第几天
     * @return    : {Date}
     * @notice    : n为[1-7]的数字
     * @notice    : [1-7]对应：Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday
     */
    getDateAtWeek:function(w) {
    	return (/^[1-7]$/.test(w))?function(){
            return this.addDays(-this.week()+n); 
        }.call(this):NaN;
    },
    /**
     * @discretion:  获取上一周第n天的Date对象
     * @method    : getDateAtPreviousWeek
     * @param	  : {Number} n 指定第几天
     * @return    : {Date}
     * @notice    : n为[1-7]的数字
     * @notice    : [1-7]对应：Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday
     */
    getDateAtPreviousWeek:function(w) {
    	return (/^[1-7]$/.test(w))?function(){
            return this.addDays(-7-this.week()+n); 
        }.call(this):NaN;
    },
    /**
     * @discretion:  获取下一周第n天的Date对象
     * @method    : getDateAtNextWeek
     * @param	  : {Number} n 指定第几天
     * @return    : {Date}
     * @notice    : n为[1-7]的数字
     * @notice    : [1-7]对应：Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday
     */
    getDateAtNextWeek:function(w) {
    	return (/^[1-7]$/.test(w))?function(){
            return this.addDays(7-this.week()+n); 
        }.call(this):NaN;
    },
    /**
     * @discretion:  获取上一年第n天的Date对象
     * @method    : getDateOfPreviousYear
     * @param	  : {Number} n 指定第几天
     * @return    : {Date}
     * @notice    : n为[1-7]的数字
     * @notice    : [1-7]对应：Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday
     */
    getDateAtYear:function(y,n) {
    	return (/^\d{4}$/.test(y)&&/^[1-366]$/.test(n))?function(){
            return new Date(y,0,1).addDays(n);
        }.call(this):NaN;
    },
	//-------时间差计算-----------------------------------------------------------
	/**
     * @discretion: 计算当前时间与要比较的时间的毫秒差
     * @method    : differMilliseconds
     * @param	  : {Date}
     * @return    : {Number}
     * @notice    : 参数为Date类型
     */
	differMilliseconds:function(other) {
        return other?(this.getTime() - other.getTime()):0;
    },
	/**
     * @discretion: 计算当前时间与要比较的时间的秒差
     * @method    : differMinutes
     * @param	  : {Date}
     * @return    : {Number}
     * @notice    : 参数为Date类型
     */
    differMinutes:function(other) {
        return Math.ceil(this.differMilliseconds(other)/ 1000);
    },
    /**
     * @discretion: 计算当前时间与要比较的时间的分钟差
     * @method    : differSeconds
     * @param	  : {Date}
     * @return    : {Number}
     * @notice    : 参数为Date类型
     */
    differSeconds:function(other) {
        return Math.ceil(this.differMilliseconds(other)/ (60 * 1000));
    },
    /**
     * @discretion: 计算当前时间与要比较的时间的小时差
     * @method    : differHours
     * @param	  : {Date}
     * @return    : {Number}
     * @notice    : 参数为Date类型
     */
    differHours:function(other) {
        return Math.ceil(this.differMilliseconds(other)/ (60 * 60 * 1000));
    },
    /**
     * @discretion: 计算当前时间与要比较的时间的天数差
     * @method    : differDays
     * @param	  : {Date}
     * @return    : {Number}
     * @notice    : 参数为Date类型
     */
    differDays:function(other) {
        return Math.ceil(this.differMilliseconds(other)/ (24 * 60 * 60 * 1000));
    },
	//---------Date相关属性判断----------------------------------------
	/**
     * @discretion: 返回指定的年份是否为闰年
     * @method    : isLeapYear
     * @param     : {Number} y 年
     * @return    : {Boolean} true|false
     */
    isLeapYear:function(y) {
        return (/^\d{4}$/.test(y))?((y%4 == 0) && (y%100 != 0) || (y%400 == 0)):NaN;
    },
    //---------Date对象复制----------------------------------------
    /**
     * @discretion: 返回一个数值相同的新Date对象
     * @method    : clone
     * @param	  : {"y":year,"M":nonth,"d":day,"h":hours,"m":21,"s":21,"S":21}
     * @return    : {Date}
     */
	clone:function(t) {
        if(t&&$.isPlainObject(t) ){
            var year = (t["y"] || t["y"] == 0)?t["y"]:this.getFullYear(),
            	month = ( t["M"] ||  t["M"] == 0)? t["M"]:this.getMonth(),
            	day = (t["d"] || t["d"] == 0)?t["d"]:this.getDate(),
            	hours = ( t["h"] ||  t["h"] == 0)? t["h"]:this.getHours(),
            	min = (t["m"] || t["m"] == 0)?t["m"]:this.getMinutes(),
            	sec = (t["s"] || t["s"] == 0)?t["s"]:this.getSeconds(),
            	millisec = (t["S"] || t["S"] == 0)?t["S"]:this.getMilliseconds();
            return new Date(year, month, day,hours,min,sec,millisec);
        }else{
        	//new Date(year, month, day, hours, minutes, seconds, microseconds)
        	/*Date对象构造函数参数说明
        	milliseconds - 距离JavaScript内部定义的起始时间1970年1月1日的毫秒数
        	datestring - 字符串代表的日期与时间。此字符串可以使用Date.parse()转换
        	year - 四位数的年份，如果取值为0-99，则在其之上加上1900
        	month - 0(代表一月)-11(代表十二月)之间的月份
        	day - 1-31之间的日期
        	hours - 0(代表午夜)-23之间的小时数
        	minutes - 0-59之间的分钟数
        	seconds - 0-59之间的秒数
        	microseconds - 0-999之间的毫秒数*/
        	return new Date(this.getFullYear(), this.getMonth(), this.getDate(), this.getHours(), this.getMinutes(),this.getSeconds(),this.getMilliseconds());
        }
	},
    //---------时间比较----------------------------------------
	/**
     * @discretion: 返回一个值，该值指示 Date 的是否是传入Date实例后的日期。
     * @method    : after
     * @param	  : {Date} other
     * @return    : {Boolean}
     */
	after:function(other) {
		return this.compareTo(other) == 1;
	},
	/**
     * @discretion: 返回一个值，该值指示 Date 的是否是传入Date实例前的日期。
     * @method    : before
     * @param	  : {Date} other
     * @return    : {Boolean}
     */
	before:function(other) {
		return this.compareTo(other) == -1;
	},
	/**
     * @discretion: 返回一个值，该值指示 Date 的两个实例是否相等。
     * @method    : equals
     * @param	  : {Date} other
     * @return    : {Boolean}
     */
	equals:function(other) {
		return this.compareTo(other) == 0;
	},
    /**
     * @discretion: 将此实例的值与指定的 Date 值相比较，确定是否: a<= 此实例 <= b
     * @method    : between
     * @param     : {Date} a
     * @param     : {Date} b
     * @return    : {Boolean} true|false
    */
    between:function(a,b) {
        return this.compareTo(a) >= 0 && this.compareTo(b) <= 0;
    },
	/**
	 * @discretion:	将此实例的值与指定的 Date 值相比较，并指示此实例是早于、等于还是晚于指定的 Date 值
	 * @method	  : compareTo
	 * @param     : {Date} other
	 * @return    : 1|-1|0
     * @notice    : 如果此实例小于比较的值则返回-1，大于则返回1，相等则返回0
    */
	compareTo:function(other) {
		if(other instanceof Date){
			var internalTicks = other.getTime();
			var num2 = this.time();
			if (num2 > internalTicks) {
				return 1;
			}
			if (num2 < internalTicks) {
				return -1;
			}
			return 0;
        }
        alert("other is not a Date Object!");
		return -1;
	},
	//---------字符串输出格式化----------------------------------------
	/**
	 * @discretion:	将当前 Date 对象的值转换为其等效的字符串表示形式
	 * @method	  : toString
	 * @param     : {String} pattern
	 * @return    : {String}
    */
	toString:function() {
		return this.format("yyyy-MM-dd HH:mm:ss S");
	},
	/**
	 * @discretion:	将当前 Date 对象的值转换为其等效的短日期字符串表示形式
	 * @method	  : toShortDateString
	 * @return    : {String}
     */
	toShortDateString:function() {
		return this.format("yyyy-MM-dd");
	},
	/**
	 * @discretion:	将当前 Date 对象的值转换为其等效的短时间字符串表示形式
	 * @method	  : toShortTimeString
	 * @return    : {String}
     */
	toShortTimeString:function() {
		return this.format("HH:mm:ss");
	},
    /**
     * @discretion: 格式化日期对象
     * @method    : format
     * @param     : {String} || 'yyyy-MM-dd', 如："yyyy年M月d日", "yyyy-MM-dd", "MM-dd-yy", "yyyy-MM-dd q", "yyyy-MM-dd HH:mm:ss S"
     * @return    : {String} 
     * @notice    : 为区分minute, month对应大写M
     * @notice    : y: 年, M: 月, d: 日,w: 周几,h: 时,m: 分,s: 秒 ,S: 毫秒 ,q: 季度
     * @notice    : 年份位数不全时: 1. 年份 "yyyy" --> 2011, "yyy" --> 011, "yy" --> 11; "y" --> 1;
    */
    format:function(pattern) {
    	pattern = pattern || 'yyyy-MM-dd HH:mm:ss S';
        var dateObj = {  
            //"y+" : this.getFullYear(),  //year 在第一个判断中处理
            "M+" : this.month(), //month  
            "d+" : this.day(),      //day  
            "H+" : this.hours(),     //hour  
            "m+" : this.minutes(),   //minute  
            "s+" : this.seconds(),   //second  
            "w+" : "1234567".charAt(this.week()),   //week  
            "q+" : this.quarter(),  	//quarter  
            "S"  : this.milliseconds() //millisecond  
        }
        if (/(y+)/ig.test(pattern)) {  
            pattern = pattern.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length));  
        }
        for(var k in dateObj){  
            if (new RegExp("("+ k +")").test(pattern)){  
                pattern = pattern.replace(RegExp.$1, RegExp.$1.length == 1 ? dateObj[k] : ("00" + dateObj[k]).substr(("" + dateObj[k]).length));  
            }  
        }
        return pattern;  
    }
};
for ( var method in extendDate) {
	Date.prototype[method] = extendDate[method];
};