/**
 * jQuery.timers - Timer abstractions for jQuery
 * @version 1.0.0
 * everyTime(時間間隔, [計時器名稱], 函式名稱, [次數限制], [等待函式程序完成])
 **/
jQuery(function($) {
	
	$.multiui = $.multiui || {};
	$.multiui.widget = $.multiui.widget || {};
	$.multiui.widget.utils = $.multiui.widget.utils || {};
	
	$.multiui.widget.utils.Timer = function(target,options){
		this.targetElements = $(target);
		this.initialize.call(this,options);	//初始化工具参数
		this.buildUtil.call(this);	//构建工具
	};
	
	$.multiui.widget.utils.Timer.prototype = {
		version:'1.0.0',
		/*初始化组件参数*/
		initialize : function(options) {
			var ctx = this;
			$.extend(this,{
				"type":'delay',
				"interval":-1,
				"timerName":"default",
				"callback":$.noop,
				"times":0,
				"sleepTime":"0s",
				"infinite":Number(ctx["times"])>0?false:true,
				"global":[],
				"guid":1,
				"dataKey":"multiui.widget.timer",
				"regex":/^([1-9][0-9]*\.?[0-9]*)\s*([smhd]{1})$/,
				"powers":{
					's': 1000,//一秒
					'm': 60000,//一分钟
					'h': 360000,//一小时
					'das': 8640000 //一天
				},
				"afterRender" : $.noop
			}, options || {});
		},
		/*构建工具*/
		buildUtil : function() {
			var ctx = this;
			var timeParse = function(value) {
				if (value == undefined || value == null)
					return null;
				var result = ctx.regex.exec($.trim(value.toString()).toLowerCase());
				if(result&&result[2]) {
					var num = parseFloat(result[1]);
					var mult = ctx.powers[result[2]] || 1;
					return num * mult;
				} else {
					return value;
				}
			};
			var setTimer = function(element, interval, timerName, fn, times) {
				var counter = 0;
				interval = timeParse(interval);
				if (typeof interval != 'number' || isNaN(interval) || interval < 0){
					return;
				}
				if (typeof times != 'number' || isNaN(times) || times < 0){ 
					times = 0;
				}
				times = times || 0;
				var timers = $(element).data(ctx["dataKey"]);
				if(!timers){
					$(element).data(ctx["dataKey"], {});
					timers = $(element).data(ctx["dataKey"]);
				}
				if (!timers[timerName]){
					timers[timerName] = {};
					timers[timerName]["timerID"] = [];
				}
				fn.timerID = fn.timerID || ctx.guid++;
				var handler = function() {
					if(ctx["infinite"]){//无限次执行
						++counter
						fn.call(element, counter);
					}else{
						if ((++counter > times && times !== 0) || fn.call(element, counter) === false){
							remove(element, timerName, fn);
						}
					}
				};
				handler.timerID = fn.timerID;
				if (!timers[timerName][fn.timerID]){
					timers[timerName]["timerID"].push(fn.timerID);
					timers[timerName][fn.timerID] = window.setInterval(function() {
						handler(arguments);
					},interval);
				}
				$(element).removeData(ctx["dataKey"]).data(ctx["dataKey"],timers);
				ctx.global.push(element);
			};
			var remove = function(element, timerName) {
				var timers = $(element).data(ctx["dataKey"]),ret;
				if ( timers ) {
					if (!timerName) {
						for (var name in timers ){
							remove(element, name);
						}
					} else if (timers[timerName]) {
						$.each(timers[timerName]["timerID"],function(index,timerID){
							window.clearInterval(timers[timerName][timerID]);
							delete timers[timerName][timerID];
						});
						for (ret in timers[timerName] ) break;
						if ( !ret ) {
							ret = null;
							delete timers[timerName];
						}
					}
					$(element).removeData(ctx["dataKey"]).data(ctx["dataKey"],timers);
					for (ret in timers ) break;
					if ( !ret ){
						$.removeData(element,ctx["dataKey"]);
					}
				}
			}
			
			$(ctx.targetElements).each(function(index,targetElement){
				if(ctx["type"]=="delay"){
					setTimer($(targetElement),ctx["interval"],ctx["timerName"], ctx["callback"],1)
				}else if(ctx["type"]=="task"){
					setTimer($(targetElement),ctx["interval"],ctx["timerName"], ctx["callback"],ctx["times"])
				}else if(ctx["type"]=="remove"){
					remove.call(ctx,$(targetElement),ctx["timerName"]);
				}
			});
		}
	};
	
	$.fn.extend({
		//延时执行 delay,且执行一次
		delay:function(options){
			options = options?options:{};
			options["type"] = "delay";options["sleep"] = false;
			return new $.multiui.widget.utils.Timer(this,options);
		},
		//定时执行 task
		task:function(options){
			options = options?options:{};
			options["type"] = "task";options["sleep"] = false;
			return new $.multiui.widget.utils.Timer(this,options);
		},
		//终止执行 remove
		remove:function(options){
			options = options?options:{};options["type"] = "remove";
			return new $.multiui.widget.utils.Timer(this,options);
		}
	});

	$(window).bind("unload", function() {
		$.each($.multiui.widget.utils.Timer.global, function(index, item) {
			$(item).remove({});
		});
	});
	
});
