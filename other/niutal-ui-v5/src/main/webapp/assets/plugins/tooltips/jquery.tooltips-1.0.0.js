;(function ($) {
  
	
  	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== TOOLTIP CLASS DEFINITION ====================== */
	
	$.bootui.widget.Tooltips = function(element, options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Tooltips.prototype = {
		constructor: $.bootui.widget.Tooltips,
		/*初始化组件参数*/
		initialize 	: function(element,options) {
			
			var $this = this;
			this.timeout    =	null;
		    this.hoverState =	null;
		    this.$element   =	null;
			this.enabled   	= 	true;
			this.type      	= 	'tooltips';
			this.$element 	= 	$(element);
			this.options   	= 	this.getOptions(options);
			
			this.$viewport = this.options.viewport && $(this.options.viewport.selector || this.options.viewport);
			
		    if(this.options.trigger&&$.trim(this.options.trigger).length>0){
		    	
		    	var triggers = this.options.trigger.split(' ');
		    	
		        for (var i = triggers.length; i--;) {
		        	var trigger = triggers[i];
		        	if (trigger == 'click') {
		        		this.$element.on('click.' + this.type, this.options.selector, $.proxy(this.toggle, this));
		        	} else if (trigger != 'manual') {
		        		var eventIn  = trigger == 'hover' ? 'mouseenter' : 'focusin';
		        		var eventOut = trigger == 'hover' ? 'mouseleave' : 'focusout';
		        		//绑定事件
		        		//$.proxy 其中有false，达到阻止冒泡的功能
		        		this.$element.on(eventIn  + '.' + this.type, this.options.selector, $.proxy(this.enter, this));
		        		this.$element.on(eventOut + '.' + this.type, this.options.selector, $.proxy(this.leave, this));
		        	}
		        }
		    }
		    //没有默认参数执行
		    this.options.selector ? (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) : this.fixTitle();
		    
		    /*扩展this:组件函数*/
			$.extend(true,$this,{
				
			});
		    
		},
		setDefaults	: function(settings){
			$.extend($.fn.tooltips.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.tooltips.defaults;
		},
		/*
	    * 添加默认项
	    * */
		getOptions	: function (options) { 
			var widget = this.$element.data("widget")||{};
				options = $.extend({}, this.getDefaults(), this.$element.data(), ((typeof widget=== 'object')?widget:{}),options);
			if (options.delay && typeof options.delay == 'number') {
				options.delay = {
					show: options.delay,
					hide: options.delay
				}
			}
			return options
		},
		getDelegateOptions : function () {
		    var options  = {}
		    var defaults = this.getDefaults()

		    this._options && $.each(this._options, function (key, value) {
		      if (defaults[key] != value){ 
		    	  options[key] = value;
		      }
		    });
		    return $.extend({},this.options,options)
		},
		/*
		 * 目的调用show方法
		 * */
		enter : function (obj) {
		    var self = obj instanceof this.constructor ? obj : $(obj.currentTarget).data('bootui.' + this.type);
		    //e.currentTarget获取当前点击对象
		    if (!self) {
		    	self = new this.constructor(obj.currentTarget, this.getDelegateOptions());
		    	$(obj.currentTarget).data('bootui.' + this.type, self)
		    }
		    
		    clearTimeout(self.timeout)

		    self.hoverState = 'in'

		    if (!self.options.delay || !self.options.delay.show) {
		    	//鼠标移上去执行show方法
		    	return self.show();
		    } else {
			    self.timeout = setTimeout(function () {
			      if (self.hoverState == 'in') self.show()
			    }, self.options.delay.show);
		    }
		},
		leave : function (obj) {
		    var self = obj instanceof this.constructor ?
		    		obj : $(obj.currentTarget).data('bootui.' + this.type)

		    if (!self) {
		    	self = new this.constructor(obj.currentTarget, this.getDelegateOptions());
		     	$(obj.currentTarget).data('bootui.' + this.type, self)
		    }

		    clearTimeout(self.timeout)

		    self.hoverState = 'out'

		    if (!self.options.delay || !self.options.delay.hide) return self.hide()

		    self.timeout = setTimeout(function () {
		      if (self.hoverState == 'out') self.hide()
		    }, self.options.delay.hide)
		},
		/*
	    * 显示提示框
	    * */
		show : function () {
	        var e = $.Event('show.' + this.type);
	        if (this.hasContent() && this.enabled) {
	        	this.$element.trigger(e);
	        	var inDom = $.contains(document.documentElement, this.$element[0]);
	        	if (e.isDefaultPrevented() || !inDom) {
	        		return;
	        	}
        		var that = this;
        		//获取提示框的jQuery对象
        		var $tip = this.tip();
        		var tipId = this.getUID(this.type);
        		//给提示框赋值，初始化提示框的样式
        		this.setContent();
        		$tip.attr('id', tipId);
        		this.$element.attr('aria-describedby', tipId);
        		//提示框拥有运动效果，加入fade类
        		if (this.options.animation){ 
        			$tip.addClass('fade');
        		}
        		var placement = typeof this.options.placement == 'function' ? this.options.placement.call(this, $tip[0], this.$element[0]) : this.options.placement;

	            var autoToken = /\s?auto?\s?/i;
	            var autoPlace = autoToken.test(placement)
	            //控制提示框的显示方位，默认为top
	            if (autoPlace) placement = placement.replace(autoToken, '') || 'top';
	            
	            /*先将提示框从文档中删除，获取其返回对象，即本身，加入样式，最后再重新插回文档中，这么写主要考虑in的情况
	             * 符合一种操作规范，如果添加样式，操作dom过于复杂，可以先将节点从dom中取出，经过一系列装饰后，再加入dom中，第一
	             * 便于浏览器渲染，二来也符合dom操作规范
	             * */
	            $tip.detach().css({ top: 0, left: 0, display: 'block' }).addClass(placement).data('bootui.' + this.type, this);

	            this.options.container ? $tip.appendTo(this.options.container) : $tip.insertAfter(this.$element);
	            
	            //获取被点击对象的位置和本身尺寸
	            var pos          = this.getPosition();
	            //获取提示框的宽度和高度
	            var actualWidth  = $tip[0].offsetWidth;
	            var actualHeight = $tip[0].offsetHeight;

	            if (autoPlace) {
	            	var orgPlacement = placement;
	            	var $parent      = this.$element.parent();
	            	var parentDim    = this.getPosition($parent);
	            	
		            placement = placement == 'bottom' && pos.top   + pos.height       + actualHeight - parentDim.scroll > parentDim.height ? 'top'    :
		                        placement == 'top'    && pos.top   - parentDim.scroll - actualHeight < 0                                   ? 'bottom' :
		                        placement == 'right'  && pos.right + actualWidth      > parentDim.width                                    ? 'left'   :
		                        placement == 'left'   && pos.left  - actualWidth      < parentDim.left                                     ? 'right'  :
		                        placement;

		           $tip.removeClass(orgPlacement).addClass(placement)
	            }

	            var calculatedOffset = this.getCalculatedOffset(placement, pos, actualWidth, actualHeight)

	            this.applyPlacement(calculatedOffset, placement)

	            var complete = function () {
	            	that.$element.trigger('shown.' + that.type);
	            	that.hoverState = null;
	            }

	            $.support.transition && this.$tip.hasClass('fade') ? $tip.one('bsTransitionEnd', complete).emulateTransitionEnd(150) : complete();
	        }
	      },
	      applyPlacement : function (offset, placement) {
	    	  
	    	  var $tip   = this.tip();
	    	  var width  = $tip[0].offsetWidth;
	    	  var height = $tip[0].offsetHeight;

	    	  // manually read margins because getBoundingClientRect includes difference
	    	  var marginTop = parseInt($tip.css('margin-top'), 10);
	    	  var marginLeft = parseInt($tip.css('margin-left'), 10);

	    	  // we must check for NaN for ie 8/9
	    	  if (isNaN(marginTop))  marginTop  = 0;
	    	  if (isNaN(marginLeft)) marginLeft = 0;

	    	  offset.top  = offset.top  + marginTop;
	    	  offset.left = offset.left + marginLeft;

	    	  // $.fn.offset doesn't round pixel values
	    	  // so we use setOffset directly with our own function B-0
	    	  $.offset.setOffset($tip[0], $.extend({
	    		  using: function (props) {
	    		  	$tip.css({
	    		  		top: Math.round(props.top),
	    		  		left: Math.round(props.left)
		            })
		          }
		      }, offset), 0);
		      $tip.addClass('in');

		      // check to see if placing tip in new offset caused the tip to resize itself
		      var actualWidth  = $tip[0].offsetWidth;
		      var actualHeight = $tip[0].offsetHeight;

		      if (placement == 'top' && actualHeight != height) {
		    	  offset.top = offset.top + height - actualHeight;
		      }

		      var delta = this.getViewportAdjustedDelta(placement, offset, actualWidth, actualHeight);

		      if (delta.left) {
		    	  offset.left += delta.left;
		      }else{
		    	  offset.top += delta.top;
		      }
		      var z_index			= 1000;
		      //根据文本框调整相应提示信息的css
		      var bootbox = this.$element.closest(".bootbox");
		      var container = $("#yhgnPage");
		      //兼容bootbox弹窗
		      if(bootbox.size() > 0 ){
		    	  z_index		= this.$element.closest(".bootbox").css("z-index");
		    	  container		= bootbox;
		      }
		      if(container.size() > 0){
		    	  //兼容chosen下拉美化插件
		    	  if($(container).find("select.chosen-select").size() > 0 ){
		    		  var chosen_z_index	= 1005;
		    		  //循环弹窗中的需要美化的select元素
		    		  $(container).find("select.chosen-select").each(function(){
		    			  if($.defined($(this).data("chosen"))){
		    					var chosen_id = "#"+$.trim($(this).attr("id")).replace(/(:|\(|\)|\{|\}|\-|\.|\#|\@|\$|\%|\^|\&|\*|\!)/g,'\\$1')+"_chosen";
		    					if($(chosen_id).size() > 0){
		    						chosen_z_index = $(chosen_id).css("z-index");
		    						//取最大的z-index
		    						z_index = !isNaN(z_index)&&!isNaN(chosen_z_index) ? Math.max(z_index,chosen_z_index) : z_index;		    						
		    					}
		    				}
		    		  });
		    	  }
		      }
		      if(!isNaN(z_index)){
		    	  $("#"+this.$element.attr('aria-describedby')).css({
			    	  "z-index"	: z_index + 5
			      });
		      }
		      var arrowDelta          = delta.left ? delta.left * 2 - width + actualWidth : delta.top * 2 - height + actualHeight;
		      var arrowPosition       = delta.left ? 'left'        : 'top';
		      var arrowOffsetPosition = delta.left ? 'offsetWidth' : 'offsetHeight';

		      $tip.offset(offset);
		      this.replaceArrow(arrowDelta, $tip[0][arrowOffsetPosition], arrowPosition);
	      },
	      replaceArrow : function (delta, dimension, position) {
	    	  this.arrow().css(position, delta ? (100 * (1 - delta / dimension) + '%') : '')
	      },
	      setContent : function () {
	    	 var $tip  = this.tip();
	    	 var title = this.getTitle();

	    	 $tip.find('.tooltips-inner')[this.options.html ? 'html' : 'text'](title);
	         $tip.removeClass('fade in top bottom left right');
	      },
	      hide : function () {
	    	  var that = this;
	    	  var $tip = this.tip();
	    	  var e    = $.Event('hide.' + this.type);

	    	  this.$element.removeAttr('aria-describedby');

	    	  function complete() {
	    		  if (that.hoverState != 'in'){
	    			  $tip.detach()
	    		  }
	    		  that.$element.trigger('hidden.' + that.type);
	    	  }

	    	  this.$element.trigger(e);
	    	  if (e.isDefaultPrevented()){ 
	    		  return;
	    	  }
	    	  $tip.removeClass('in');
	    	  $.support.transition && this.$tip.hasClass('fade') ?$tip.one('bsTransitionEnd', complete).emulateTransitionEnd(150) :complete();
	    	  this.hoverState = null;
	    	  return this;
	      },
		  toggle : function (e) {
			  var self = this
			  if (e) {
				  self = $(e.currentTarget).data('bootui.' + this.type);
				  if (!self) {
					  self = new this.constructor(e.currentTarget, this.getDelegateOptions());
					  $(e.currentTarget).data('bootui.' + this.type, self);
				  }
			  }
			  self.tip().hasClass('in') ? self.leave(self) : self.enter(self);
		  },
		  destroy : function () {
			  $("#"+this.$element.attr('aria-describedby')).remove();
			  clearTimeout(this.timeout);
			  this.hide().$element.off('.' + this.type).removeData('bootui.' + this.type);
		  },
		  /*
		   * 将被点击标签的title属性值转给data-original-title属性，最后删除title属性
		   * */
		  fixTitle : function () {
			  var $e = this.$element
			  if ($e.attr('title') || typeof ($e.attr('data-original-title')) != 'string') {
				  $e.attr('data-original-title', $e.attr('title') || '').removeAttr('title', '');
			  }
		  },
		  hasContent : function () {
			  return this.getTitle();
		  },
		  getPosition : function ($element) {
			  $element   = $element || this.$element;
			  var el     = $element[0];
			  var isBody = el.tagName == 'BODY';
			  return $.extend({}, (typeof el.getBoundingClientRect == 'function') ? el.getBoundingClientRect() : null, {
				  scroll: isBody ? document.documentElement.scrollTop || document.body.scrollTop : $element.scrollTop(),
				  width:  isBody ? $(window).width()  : $element.getVisualWidth(),/*$element.outerWidth()*/
				  height: isBody ? $(window).height() : $element.outerHeight()
			  }, isBody ? { top: 0, left: 0 } : $element.offset());
		  },
		  getUID : function (prefix) {
			  do {
				  prefix += ~~(Math.random() * 1000000);
			  }while (document.getElementById(prefix));
			  return prefix;
		  },
		  getCalculatedOffset : function (placement, pos, actualWidth, actualHeight) {
			  
			  var width  = pos.width / 2 - actualWidth / 2;
			  var height = pos.height / 2 - actualHeight / 2;
			  /*align		: "left",//left|center|right
		    	valign		: "top",//top|middle|bottom vertical-align:
			   */			  
			  var align = this.options.align;
	    	  var valign = this.options.valign;
	    	 
	    	  width = ( align == 'left') ? 0 : ( ( align == 'center') ?  width : ( ( align == 'right') ?  pos.width - actualWidth  : width ) );
	    	  height = ( valign == 'top') ? 0 : ( ( valign == 'middle') ?  height : ( ( valign == 'bottom') ?  pos.height - actualHeight  : height ) );
	    	  alert("pos.left:" + pos.left + ",pos.width:" + pos.width);
		    return placement == 'bottom' ? { top: pos.top + pos.height,   left: pos.left + width  } :
		           placement == 'top'    ? { top: pos.top - actualHeight, left: pos.left + width  } :
		           placement == 'left'   ? { top: pos.top + height, left: pos.left - actualWidth } :
		        /* placement == 'right' */ { top: pos.top + height, left: pos.left + pos.width   }

		  },
		  getViewportAdjustedDelta : function (placement, pos, actualWidth, actualHeight) {
			  var delta = { top: 0, left: 0 }
			  if (!this.$viewport) {
				  return delta;
			  }
			  var viewportPadding = this.options.viewport && this.options.viewport.padding || 0;
			  var viewportDimensions = this.getPosition(this.$viewport);

			  if (/right|left/.test(placement)) {
				  var topEdgeOffset    = pos.top - viewportPadding - viewportDimensions.scroll;
				  var bottomEdgeOffset = pos.top + viewportPadding - viewportDimensions.scroll + actualHeight;
				  if (topEdgeOffset < viewportDimensions.top) { // top overflow
					  delta.top = viewportDimensions.top - topEdgeOffset;
				  } else if (bottomEdgeOffset > viewportDimensions.top + viewportDimensions.height) { // bottom overflow
					  delta.top = viewportDimensions.top + viewportDimensions.height - bottomEdgeOffset;
				  }
			  } else {
				  var leftEdgeOffset  = pos.left - viewportPadding;
				  var rightEdgeOffset = pos.left + viewportPadding + actualWidth;
				  if (leftEdgeOffset < viewportDimensions.left) { // left overflow
					  delta.left = viewportDimensions.left - leftEdgeOffset;
				  } else if (rightEdgeOffset > viewportDimensions.width) { // right overflow
					  delta.left = viewportDimensions.left + viewportDimensions.width - rightEdgeOffset;
				  }
			  }
			  return delta
		  },
		  getTitle : function () {
			  var title;
			  var $e = this.$element;
			  var o  = this.options;
			  title = $e.attr('data-original-title') || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title);
			  return title
		  },
		  tip : function () {
			  return (this.$tip = this.$tip || $(this.options.template));
		  },
		  arrow : function () {
			  return (this.$arrow = this.$arrow || this.tip().find('.tooltips-arrow'));
		  },
		  validate : function () {
			  if (!this.$element[0].parentNode) {
				  this.hide();
				  this.$element = null;
				  this.options  = null;
			  }
		  },
		  enable : function () {
			  this.enabled = true
		  },
		  disable : function () {
			  this.enabled = false
		  },
		  toggleEnabled : function () {
			  this.enabled = !this.enabled
		  }
	};
	
	/* Tooltips PLUGIN DEFINITION  */
	
	var old = $.fn.tooltips;
	
	$.fn.tooltips = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this), data = $this.data('bootui.tooltips');
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true , {}, $.fn.tooltips.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.tooltips', (data = new $.bootui.widget.Tooltips(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.tooltips.Constructor = $.bootui.widget.Tooltips;
	
	// TOOLTIP NO CONFLICT ===================//

	$.fn.tooltips.noConflict = function () {
	    $.fn.tooltips = old
	    return this
	};
	  
	$.fn.tooltips.defaults = {
		/*版本号*/
		version:'1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop, 
		/*其他参数*/
		animation	: false,
	    placement	: 'top',
	    selector	: false,
	    template	: '<div class="tooltips" role="tooltip"><div class="tooltips-arrow"></div><div class="tooltips-inner"></div></div>',
	    trigger		: 'hover focus',
	    title		: '',
	    delay		: 0,
	    html		: false,
	    container	: false,
	    align		: "center",//left|center|right
	    valign		: "middle",//top|middle|bottom vertical-align:
	    viewport	: {
	      	selector: 'body',
	      	padding: 0
	    }
	};
  

}(jQuery));