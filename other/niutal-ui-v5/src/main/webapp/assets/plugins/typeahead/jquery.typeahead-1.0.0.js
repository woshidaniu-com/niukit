/*
 * @discretion	: 基于Bootstrap Typeahead 改造而来的自动完成插件.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
		data-toggle	：	用于绑定组件的引用
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== Typeahead CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.Typeahead = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Typeahead.prototype = {
		constructor: $.bootui.widget.Typeahead,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
			var $this = this; 
			this.$element = $(element);
		    this.options = options;
		    this.sorter = this.options.sorter || this.sorter;
		    this.highlighter = this.options.highlighter || this.highlighter;
		    this.updater = this.options.updater || this.updater;
		    this.source = this.options.source;
		    this.$menu = $(this.options.menu);
		    this.shown = false;
			this.formatItem = this.options.formatItem || this.formatItem;
			this.setValue = this.options.setValue || this.setValue;
	    	
	    	this.$element
	        	.on('focus',    $.proxy(this.focus, this))
	        	.on('blur',     $.proxy(this.blur, this))
	        	.on('keypress', $.proxy(this.keypress, this))
	        	.on('keyup',    $.proxy(this.keyup, this));

	        if (this.eventSupported('keydown')) {
	        	this.$element.on('keydown', $.proxy(this.keydown, this));
	        }

	    	this.$menu.css({"min-width":$(element).outerWidth()})
	        	.on('click', $.proxy(this.click, this))
	        	.on('mouseenter', 'li', $.proxy(this.mouseenter, this))
	        	.on('mouseleave', 'li', $.proxy(this.mouseleave, this));
		},
		destroy : function () {
			this.$element.off('.typeahead').removeData("bootui.typeahead");
		},
		setDefaults	: function(settings){
			$.extend($.fn.typeahead.defaults, settings );
		},
		processObj:0, 
		formatItem:function(item){
			return item.toString();
		},
		setValue:function(item){
			return {"data-value":item.toString(),"real-value":item.toString()};
	    },
		select: function () {
		    var val = this.$menu.find('.active').attr('data-value');
			var realVal = this.$menu.find('.active').attr('real-value');
		    this.$element.val(this.updater(val)).attr("real-value",realVal).change();
		    return this.hide();
		},
		updater: function (item) {
		     return item;
		},
		show: function () {
		    var pos = $.extend({}, this.$element.position(), {
		        height: this.$element[0].offsetHeight
		    });
		    /*this.$menu.insertAfter(this.$element).css({
	          top: pos.top + pos.height,
	          left: pos.left
	    	}).show();*/
		    this.$menu.insertAfter(this.$element);
		    // 获取bootbox弹出,解决弹窗兼容问题
	    	var bootbox =  this.$element.closest(".bootbox");
	    	var z_index =  1010; 
	    	if($(bootbox).size() > 0){
	    		z_index =  ( parseInt(bootbox.css("z-index")||"1050") + 90) ; 
	    		this.$menu.css({
	    			'z-index' 	: z_index,
			        'top' 		: pos.top + pos.height,
			        'left' 		: pos.left
			    }).show();
	    		$(bootbox).find('.chosen-container').css('z-index',--z_index);
	    	}else{
	    		this.$menu.css({
	    			'z-index' 	: '1010',
			        'top' 		: pos.top + pos.height,
			        'left' 		: pos.left
			    }).show();
	    		this.$element.css('z-index','1010')
	    		$('.chosen-container').css('z-index',--z_index);
	    	}
		    this.shown = true;
		    return this;
		},
		hide: function () {
		    this.$menu.hide();
		    this.shown = false;
		    return this;
		},
		lookup: function (event) {
		    var items;
		    this.query = this.$element.val();
		    if (!this.query || this.query.length < this.options.minLength) {
		    	return this.shown ? this.hide() : this;
		    }
		    items = $.isFunction(this.source) ? this.source(this.query, $.proxy(this.process, this)) : this.source;
		    return items ? this.process(items) : this
		},
		process: function (items) {
		    var that = this;
		    if (!items.length) {
		        return this.shown ? this.hide() : this
		    }
		    return this.render(items.slice(0, this.options.items)).show()
	    },
		highlighter: function (item) {
	    	var that = this;
	    	item = that.formatItem(item);
		    var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
		    return item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
		        return '<strong style="color:#FF6600;">' + match + '</strong>'
		    });
		},
		render: function (items) {
			var that = this;
			items = $(items).map(function (i, item) {
		        i = $(that.options.item).attr(that.setValue(item));
		        i.find('a').html(that.highlighter(item));
		        return i[0];
		    });
			items.first().addClass('active');
			this.$menu.html(items);
			return this;
	    },
		next: function (event) {
		    var active = this.$menu.find('.active').removeClass('active') , next = active.next()
		    if (!next.length) {
		    	next = $(this.$menu.find('li')[0])
		    }
		    next.addClass('active')
	    },
	    prev: function (event) {
	    	var active = this.$menu.find('.active').removeClass('active') , prev = active.prev()
	    	if (!prev.length) {
	    		prev = this.$menu.find('li').last()
	    	}
	    	prev.addClass('active')
	    },
	    eventSupported: function(eventName) {
	    	var isSupported = eventName in this.$element;
		   	if (!isSupported) {
		       	this.$element.setAttribute(eventName, 'return;');
		       	isSupported = typeof this.$element[eventName] === 'function';
		   	}
	    	return isSupported;
		},
		move: function (e) {
			if (!this.shown) {
				return;
			}
			switch(e.keyCode) {
		        case 9: // tab
		        case 13: // enter
		        case 27: // escape
		          e.preventDefault()
		          break
		        case 38: // up arrow
		          e.preventDefault()
		          this.prev()
		          break
		        case 40: // down arrow
		          e.preventDefault()
		          this.next()
		          break
			}
			e.stopPropagation()
		},
		keydown: function (e) {
			this.suppressKeyPressRepeat = ~$.inArray(e.keyCode, [40,38,9,13,27]);
			this.move(e);
		},
		keypress: function (e) {
			if (this.suppressKeyPressRepeat) return
		    this.move(e)
		},
		keyup: function (e) {
			switch(e.keyCode) {
		        case 40: // down arrow
		        case 38: // up arrow
		        case 16: // shift
		        case 17: // ctrl
		        case 18: // alt
		          break
		        case 9: // tab
		        case 13: // enter
		          if (!this.shown) return;
		          this.select()
		          break
		        case 27: // escape
		          if (!this.shown) return;
		          this.hide();
		          break
		        default:
				  var that = this;
				  if(that.processObj){
				    clearTimeout(that.processObj)
					that.processObj = 0
				  };
				  that.processObj = setTimeout(function(){
					that.lookup()
				  },that.options.delay);
			}
		    e.stopPropagation();
		    e.preventDefault();
		},
		focus: function (e) {
			this.focused = true;
		},
		blur: function (e) {
			this.focused = false;
			if (!this.mousedover && this.shown){
				this.hide();
			}
		},
		click: function (e) {
			e.stopPropagation();
			e.preventDefault();
			this.select();
			this.$element.focus();
		},
		mouseenter: function (e) {
			this.mousedover = true;
			this.$menu.find('.active').removeClass('active');
		    $(e.currentTarget).addClass('active');
		},
		mouseleave: function (e) {
			this.mousedover = false;
		    if (!this.focused && this.shown){
		    	this.hide();
		    } 
		}
	};
	
	/* AUTOCOMPLETE PLUGIN DEFINITION  */
	
	 var old = $.fn.typeahead;
	 
	/*
	 * jQuery原型上自定义的方法
	 */
	$.fn.typeahead = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this),data = $this.data("bootui.typeahead");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true , {}, $.fn.typeahead.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.typeahead', (data = new $.bootui.widget.Typeahead(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.typeahead.defaults = {
		/*版本号*/
		version:'1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop, 
		/*其他参数*/
		source: [],
        items: 8,
        menu: '<ul class="typeahead dropdown-menu"></ul>',
        item: '<li><a href="#"></a></li>',
        minLength: 1,
        delay: 500
	};

	$.fn.typeahead.Constructor = $.bootui.widget.Typeahead;
		

	/* AUTOCOMPLETE NO CONFLICT * =================== */

	$.fn.typeahead.noConflict = function () {
		 $.fn.typeahead = old
		 return this;
	}
	  
	/*============== Typeahead DATA-API  ==============*/
	
	/*委托绑定*/
	$(document).on('focus.typeahead.data-api', '[data-provide*="typeahead"]', function (event) {
		if(event.currentTarget = this){
			var $this = $(this);
			if ($this.data('bootui.typeahead')){
				return;
			}else{
				$this.typeahead($this.data());
			}
		}
	});
	

}(jQuery));
