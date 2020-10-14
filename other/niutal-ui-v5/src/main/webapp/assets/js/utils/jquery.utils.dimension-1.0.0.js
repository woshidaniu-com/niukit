;
;(function($) {

	/**
	 * 取得浏览器视图的宽度
	 */
	$.viewWidth = function() {
		var w = 0, D = document;
		if (D.documentElement && D.documentElement.clientWidth) {
			w = D.documentElement.clientWidth;
		} else if (D.body && D.body.clientWidth) {
			w = D.body.clientWidth;
		}
		return w;
	}

	/**
	 * 取得浏览器视图的高度
	 */
	$.viewHeight = function() {
		var h = 0, D = document;
		if (document.compatMode != 'CSS1Compat') {
			h = D.body.clientHeight;
		} else {
			if (D.documentElement && D.documentElement.clientHeight) {
				h = D.documentElement.clientHeight;
			} else if (D.body && D.body.clientHeight) {
				h = D.body.clientHeight;
			}
		}
		return h;
	}

	/**
	 * 取得画板的高度(即所有内容,当浏览器内容不足的时候为浏览器视图大小)
	 */
	$.canvasHeight = function() {
		var D = document, h = 0;
		h = Math.max(Math.max(D.body.scrollHeight,
				D.documentElement.scrollHeight), Math.max(D.body.offsetHeight,
				D.documentElement.offsetHeight), Math.max(D.body.clientHeight,
				D.documentElement.clientHeight));
		if ($.browser.msie && $.browser.version > 6
				&& D.body.scrollHeight < $.viewHeight()) {
			h = D.body.clientHeight;
		}
		if ($.browser.msie && document.compatMode == 'CSS1Compat'
				&& D.body.scrollHeight < $.viewHeight()) {
			if ($.browser.version > 7 && $.browser.version < 9) {
			} else if ($.browser.version > 6 && $.browser.version < 8) {
			}
			h = D.documentElement.clientHeight;
		}
		return h;
	}

	/**
	 * 取得画板的宽度(即所有内容,当浏览器内容不足的时候为浏览器视图大小)
	 */
	$.canvasWidth = function() {
		var D = document, w = D.body.scrollWidth;
		if (document.compatMode == 'CSS1Compat') {
			w = D.documentElement.scrollWidth;
		} else {
			if ($.browser.msie && $.browser.version <= 6
					&& D.body.scrollWidth > $.viewWidth()) {
				w = Math.max(Math.max(D.body.scrollWidth,
						D.documentElement.scrollWidth), Math.max(
						D.body.offsetWidth, D.documentElement.offsetWidth),
						Math.max(D.body.clientWidth,
								D.documentElement.clientWidth));
			}
		}
		return w;
	}

	/**
	 * 取得画板的宽度(即所有内容,当浏览器内容不足的时候为浏览器视图大小)
	 */
	$.scrollLeft = function() {
		if (document.compatMode != 'CSS1Compat'
				|| ($.browser.msie && $.browser.version <= 6)) {
			return Math.max($('body').scrollLeft(),
					document.documentElement.scrollLeft);
		} else {
			return $('body').scrollLeft();
		}
	}

	/**
	 * 取得画板的宽度(即所有内容,当浏览器内容不足的时候为浏览器视图大小)
	 */
	$.scrollTop = function() {
		if (document.compatMode != 'CSS1Compat'
				|| ($.browser.msie && $.browser.version <= 6)) {
			return Math.max($('body').scrollTop(),
					document.documentElement.scrollTop);
		} else {
			return $('body').scrollTop();
		}
	}

	$.extend({
		// 浏览器宽度
		clientWidth : function() {
			var clientWidth = $(window).width();// document.body.clientWidth
			var bro = $.browser;
			var binfo = "";
			if (bro.msie || bro.mozilla) {
				clientWidth = document.documentElement.clientWidth;
			}
			if (bro.safari) {
				binfo = "Apple Safari " + bro.version;
			}
			if (bro.opera) {
				clientWidth = document.body.clientWidth;
			}
			if (document.compatMode == "BackCompat") {
				clientWidth = document.body.clientWidth;
			} else { // document.compatMode == "CSS1Compat"
				clientWidth = document.documentElement.clientWidth;
			}
			// 在IE、FireFox、Opera下都可以使用
			// document.body.clientWidth
			// document.c.clientHeight
			// 即可获得，很简单，很方便。
			// 而在公司项目当中：
			// Opera仍然使用
			// document.body.clientWidth
			// document.body.clientHeight
			// 可是IE和FireFox则使用
			// document.documentElement.clientWidth
			// document.documentElement.clientHeight
			return clientWidth;
		},
		// 浏览器高度
		clientHeight : function() {
			var clientHeight = window.screen.availHeight;
			var bro = $.browser;
			var binfo = "";
			if (bro.msie || bro.mozilla) {
				clientHeight = document.documentElement.clientHeight;
			}
			if (bro.safari) {
				binfo = "Apple Safari " + bro.version;
			}
			if (bro.opera) {
				clientHeight = document.body.clientHeight;
			}
			if (document.compatMode == "BackCompat") {
				clientHeight = document.body.clientHeight;
			} else if (document.compatMode == "CSS1Compat") { // document.compatMode
																// ==
																// "CSS1Compat"
				clientHeight = document.documentElement.clientHeight;
			}
			return clientHeight;
		},
		// window对象宽度
		windowWidth : function() {
			var windowWidth = $(window).width();
			if (self.innerHeight) { // all except IE
				windowWidth = self.innerWidth;
				if (document.documentElement.clientWidth) {
					windowWidth = document.documentElement.clientWidth;
				}
			} else {
				if (document.documentElement
						&& document.documentElement.clientHeight) {
					// Explorer 6 Strict Mode
					windowWidth = document.documentElement.clientWidth;
				} else {
					if (document.body) { // other Explorers
						windowWidth = document.body.clientWidth;
					}
				}
			}
			return windowWidth;
		},
		// window对象高度
		windowHeight : function() {
			var windowHeight = $(window).height();
			if (self.innerHeight) { // all except Explorer
				windowHeight = self.innerHeight;
			} else {
				if (document.documentElement
						&& document.documentElement.clientHeight) {
					// Explorer 6 Strict Mode
					windowHeight = document.documentElement.clientHeight;
				} else if (document.body) {
					// other Explorers
					windowHeight = document.body.clientHeight;
				}
			}
			return windowHeight;
		}
	});

	// 元素高宽，尺寸，位置相关方法

	$.fn.getHeight = function() {
		var $ele = $(this), height = "auto";
		if ($ele.is(":visible")) {
			height = $ele.height();
		} else {
			var tmp = {
				position : $ele.css("position"),
				visibility : $ele.css("visibility"),
				display : $ele.css("display")
			};
			height = $ele.css({
				position : "absolute",
				visibility : "hidden",
				display : "block"
			}).height();
			$ele.css(tmp);
		}
		return height;
	};

	$.fn.getSize = function() {
		var $el = $(this);
		if ($el.css("display") !== "none") {
			return {
				width : $el.width(),
				height : $el.height()
			};
		}
		var old = {
			position : $el.css("position"),
			visibility : $el.css("visibility"),
			display : $el.css("display")
		}, tmpStyle = {
			display : "block",
			position : "absolute",
			visibility : "hidden"
		};
		$el.css(tmpStyle);
		var width = $el.width(), height = $el.height();
		$el.css(old);
		return {
			"width" : width,
			"height" : height
		};
	};

	// 获得页面尺寸
	$.getPageSize = function() {
		var xScroll, yScroll;
		if (window.innerHeight && window.scrollMaxY) {
			xScroll = window.innerWidth + window.scrollMaxX;
			yScroll = window.innerHeight + window.scrollMaxY;
		} else {
			if (document.body.scrollHeight > document.body.offsetHeight) {
				// all but Explorer Mac
				xScroll = document.body.scrollWidth;
				yScroll = document.body.scrollHeight;
			} else {
				// Explorer Mac...would also work in Explorer 6 Strict, Mozilla
				// and Safari
				xScroll = document.body.offsetWidth;
				yScroll = document.body.offsetHeight;
			}
		}
		// 获得window尺寸
		var dimension = $(window).getDimension();
		// for small pages with total height less then height of the viewport
		if (yScroll < dimension.height) {
			pageHeight = dimension.height;
		} else {
			pageHeight = yScroll;
		}
		// for small pages with total width less then width of the viewport
		if (xScroll < dimension.width) {
			pageWidth = xScroll;
		} else {
			pageWidth = dimension.width;
		}
		return {
			"width" : pageWidth,
			"height" : pageHeight
		};
	};

	$.getClientSize = function() {
		return {
			"width" : function() {
				var clientWidth = $(window).width();// document.body.clientWidth
				var bro = $.browser;
				var binfo = "";
				if (bro.msie || bro.mozilla) {
					clientWidth = document.documentElement.clientWidth;
				}
				if (bro.safari) {
					binfo = "Apple Safari " + bro.version;
				}
				if (bro.opera) {
					clientWidth = document.body.clientWidth;
				}
				if (document.compatMode == "BackCompat") {
					clientWidth = document.body.clientWidth;
				} else { // document.compatMode == "CSS1Compat"
					clientWidth = document.documentElement.clientWidth;
				}
				// 在IE、FireFox、Opera下都可以使用
				// document.body.clientWidth
				// document.c.clientHeight
				// 即可获得，很简单，很方便。
				// 而在公司项目当中：
				// Opera仍然使用
				// document.body.clientWidth
				// document.body.clientHeight
				// 可是IE和FireFox则使用
				// document.documentElement.clientWidth
				// document.documentElement.clientHeight
				return clientWidth;
			}.call(this),
			"height" : function() {
				var clientHeight = $(window).height();
				if (self.innerHeight) { // all except Explorer
					clientHeight = self.innerHeight;
				} else if (document.documentElement
						&& document.documentElement.clientHeight) { // Explorer
																	// 6 Strict
																	// Mode
					clientHeight = document.documentElement.clientHeight;
				} else if (document.body) { // other Explorers
					clientHeight = document.body.clientHeight;
				}
				return clientHeight;
			}.call(this)
		};
	}

	// 获得元素尺寸
	$.fn.getDimension = function() {
		var $element = $(this);
		var isBody = this[0].tagName ? this[0].tagName.toLocaleLowerCase() == 'body'
				: false;
		var isWindow = $.isWindow(this);
		return {
			"width" : function() {
				if (isWindow) {
					return $(window).width();
				} else if (isBody) {
					return $(document).width();
				} else {
					return Math.max($element.width(), $element.innerWidth());
				}
			}.call(this),
			"height" : function() {
				if (isWindow) {
					return $(window).height();
				} else if (isBody) {
					return $(document).height();
				} else {
					return Math.max($element.height(), $element.innerHeight());
				}
			}.call(this)
		};
	};

	$.fn.getPosition = function() {
		var $element = $(this);
		var isBody = $(this)[0].tagName ? $(this)[0].tagName
				.toLocaleLowerCase() == 'body' : false;
		var isWindow = $.isWindow(this);
		var position = $.extend(true, {},
				{
					scrollTop : isBody ? $(document).scrollTop() : $element
							.scrollTop(),
					scrollLeft : isBody ? $(document).scrollLeft() : $element
							.scrollLeft()
				}, (isWindow ? {
					"left" : 0,
					"top" : 0
				} : $element.offset()));

		// 获得尺寸
		var dimension = $element.getDimension();
		// 进一步处理
		return $.extend({}, position,
				{
					width : dimension.width,
					height : dimension.height,
					pageX : position.left + position.scrollLeft,
					pageY : position.top + position.scrollTop,
					// 取得border值
					border : isWindow ? 0 : ($element.outerWidth() - $element
							.innerWidth()),
					// 取得padding-top值
					padding : isWindow ? 0 : ($element.innerWidth() - $element
							.width()),
					// 取得margin-top值
					margin : isWindow ? 0
							: ($element.outerWidth(true) - $element
									.outerWidth())
				}, isBody ? {
					top : 0,
					left : 0
				} : $element.offset());
	};
	
	/*
	 * 扩展添加获取元素border,margin,padding等css样式方法 
	 */
	$.fn.getBoundingRect = function(){
		 var $element   = this ;
		 var isWindow 	= $.isWindow(this);
		 var border		= isWindow ? 0 : ($($element).outerWidth() - $($element).innerWidth());
		 var padding	= isWindow ? 0 : ($($element).innerWidth() - $($element).width());
		 var margin		= isWindow ? 0 : function(){
			try {
				return ($($element).outerWidth(true) - $($element).outerWidth())
			} catch (e) {
				return  0;
			}
		 }.call(this);
		 //进一步处理
		 return {
			//取得border值
			"border"		: border,
			"border-left"	: isWindow ? 0 : function(){
			 	try {
			 		var cssStr = $.trim(($($element).css("border-left")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) : border;
				} catch (e) {
					return  border;
				}
		 	}.call(this),
		 	"border-top"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("border-top")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) : border;
				} catch (e) {
					return  border;
				}
		 	}.call(this),
		 	"border-right"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("border-right")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) : border;
				} catch (e) {
					return  border;
				}
		 	}.call(this),
		 	"border-bottom"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("border-bottom")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) : border;
				} catch (e) {
					return  border;
				}
		 	}.call(this),
			//取得padding值
		 	"padding"		: padding,
			"padding-left"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("padding-left")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :padding;
				} catch (e) {
					return  padding;
				}
		 	}.call(this),
		 	"padding-top"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("padding-top")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :padding;
				} catch (e) {
					return  padding;
				}
		 	}.call(this),
		 	"padding-right"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("padding-right")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :padding;
				} catch (e) {
					return  padding;
				}
		 	}.call(this),
		 	"padding-bottom"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("padding-bottom")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :padding;
				} catch (e) {
					return  padding;
				}
		 	}.call(this),
			//取得margin值
		 	"margin"		: margin,
			"margin-left"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("margin-left")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :margin;
				} catch (e) {
					return  margin;
				}
		 	}.call(this),
		 	"margin-top"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("margin-top")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :margin;
				} catch (e) {
					return  margin;
				}
		 	}.call(this),
		 	"margin-right"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("margin-right")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :margin;
				} catch (e) {
					return  margin;
				}
		 	}.call(this),
		 	"margin-bottom"	: isWindow ? 0 :function(){
		 		try {
		 			var cssStr = $.trim(($($element).css("margin-bottom")||"").replace(/\!important/ig,"").replace('px',''));
					return $.isNumeric(cssStr) ? parseInt(cssStr) :margin;
				} catch (e) {
					return  margin;
				}
		 	}.call(this)
		};
	};

})(jQuery);