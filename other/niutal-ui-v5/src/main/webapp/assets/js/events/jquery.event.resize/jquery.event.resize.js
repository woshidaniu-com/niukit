//resize of div
(function ($, obj, EVENT_TYPE , c) {
	
	var array = $( []), options = $.resize = $.extend($.resize, {}), task, 
		dataKey = EVENT_TYPE + "-special-event",funcKey = "setTimeout",optKey = "throttleWindow";
	/*判断调用周期*/
	options["delay"] = 250;
	/*是否调整窗口*/
	options[optKey] = true;
	
	function taskFunc() {
		task = obj[funcKey](function() {
			array.each(function() {
				var $this = $(this), 
					w = $this.width(), 
					h = $this.height(),
					old = $.data( this, dataKey);
				if (w !== old.w || h !== old.h) {
					$this.trigger("resize", [ old.w = w, old.h = h ]);
				}
			});
			taskFunc();
		}, options["delay"]);
	}
	
	$.event.special[EVENT_TYPE] = {
		//事件安装
		setup : function() {
			if (!options[optKey] && this[funcKey]) {
				return false;
			}
			var $this = $(this);
			array = array.add($this);
			$.data(this, dataKey , {
				w : $this.width(),
				h : $this.height()
			});
			if (array.length === 1) {
				taskFunc();
			}
		},
		//事件卸载
		teardown : function() {
			if (!options[optKey] && this[funcKey]) {
				return false;
			}
			var $this = $(this);
			array = array.not($this);
			$this.removeData(dataKey);
			if (!array.length) {
				window.clearTimeout(task);
			}
		},
		add : function(param) {
			if (!options[optKey] && this[funcKey]) {
				return false;
			}
			var func = $.noop;
			function init(s, o, p) {
				var $this = $(this), data = $.data(this, dataKey );
				data.w = (o !== c) ? o : $this.width();
				data.h = (p !== c) ? p : $this.height();
				func.apply(this, arguments);
			}
			if ($.isFunction(param)) {
				func = param;
				return init;
			} else {
				func = param.handler;
				param.handler = init;
			}
		}
	};
	
	
})(jQuery, this, 'resize');