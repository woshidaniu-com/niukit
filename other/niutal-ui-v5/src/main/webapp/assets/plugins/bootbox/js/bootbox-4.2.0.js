
/**
 * bootbox.js [v4.2.0]
 *
 * http://bootboxjs.com/license.txt
 */

// @see https://github.com/makeusabrew/bootbox/issues/180
// @see https://github.com/makeusabrew/bootbox/issues/186
;(function (root, factory) {
  "use strict";
  if (typeof define === "function" && define.amd) {
    // AMD. Register as an anonymous module.
    define(["jquery"], factory);
  } else if (typeof exports === "object") {
    // Node. Does not work with strict CommonJS, but
    // only CommonJS-like environments that support module.exports,
    // like Node.
    module.exports = factory(require("jquery"));
  } else {
    // Browser globals (root is window)
    root.bootbox = factory(root.jQuery);
  }

}(this, function init($, undefined) {

  "use strict";

  // the base DOM structure needed to create a modal
  var templates = {
    dialog:
      "<div class='bootbox modal sl_mod' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>" +
        "<div class='modal-dialog'>" +
          "<div class='modal-content'>" +
            "<div class='modal-body'><div class='bootbox-body'></div></div>" +
          "</div>" +
        "</div>" +
      "</div>",
    header:
      "<div class='modal-header'>" +
        "<h4 class='modal-title'></h4>" +
      "</div>",
	progress:  
		"<div class='modal-progress'>" +
			"<div class='progress' >" +
				"<div class='progress-bar progress-bar-info progress-bar-striped active' role='progressbar' aria-valuenow='0' aria-valuemin='0' aria-valuemax='100'  style='width: 0%;'>0%</div>" + 
			"</div>" +
		"</div>",
    footer:  "<div class='modal-footer'></div>",
    fixed :	 "<div class='modal-fixed'></div>",
    fullScreen:"<div class='bootbox-full'><i class='fa fa-square'></i></div>",
    closeButton:'<input type="hidden" name="focusInput"/><button type="button" class="bootbox-close-button btn-sm close bootbox-close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>',
    form:
      "<form class='bootbox-form'></form>",
    inputs: {
      text:
        "<input class='bootbox-input bootbox-input-text form-control' autocomplete=off type=text />",
      textarea:
        "<textarea class='bootbox-input bootbox-input-textarea form-control'></textarea>",
      email:
        "<input class='bootbox-input bootbox-input-email form-control' autocomplete='off' type='email' />",
      select:
        "<select class='bootbox-input bootbox-input-select form-control'></select>",
      checkbox:
        "<div class='checkbox'><label><input class='bootbox-input bootbox-input-checkbox' type='checkbox' /></label></div>",
      date:
        "<input class='bootbox-input bootbox-input-date form-control' autocomplete=off type='date' />",
      time:
        "<input class='bootbox-input bootbox-input-time form-control' autocomplete=off type='time' />",
      number:
        "<input class='bootbox-input bootbox-input-number form-control' autocomplete=off type='number' />",
      password:
        "<input class='bootbox-input bootbox-input-password form-control' autocomplete='off' type='password' />"
    }
  };

  var defaults = {
    // default language
    locale: "en",
    // show backdrop or not
    backdrop: true,
    // animate the modal in/out
    animate: true,
    // additional class string applied to the top level dialog
    className: null,
    // whether or not to include a close button
    closeButton: true,
    // show the dialog immediately by default
    show: true,
    //是否需要支持全屏
    fullScreen:true,
    //是否需要进度条
    progress : false,
    //进度条ID
    progressID : "bootboxStatus",
    //是否点击按钮时，锁定按钮点击状态
    btnlock : false,
    //是否允许窗口拖拽
    draggable : false,
    // dialog container
    container: "body",
    // dialog width
    width :	"800px",
    // dialog height
    height:	undefined,
    // dialog max-height
    max_height:	undefined,
    //fixed div 
    fixedTarget:null,
    //当数据提交成功，立刻关闭窗口
    offAtOnce	: true,
    //预留的上下高度
    offsetHeight: 200,
    offsetWidth : 20,
    /*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
	beforeRender: $.noop,
	/*组件渲染完成后的回调函数*/
	afterRender: $.noop
  };
 
  // our public object; augmented after our private API
  var exports = {};

  /**
   * @private
   */
  function _t(key) {
	  var locale = locales[defaults.locale];
	  return locale ? locale[key] : locales.en[key];
  }

  function processCallback(e, dialog, options , callback) {
	  e.stopPropagation();
	  e.preventDefault();

	  // by default we assume a callback will get rid of the dialog,
	  // although it is given the opportunity to override this

	  // so, if the callback can be invoked and it *explicitly returns false*
	  // then we'll set a flag to keep the dialog active...
    
	  var thisMap = $.extend({},this,{
		  	"options"	: options,
	    	"api"		: window.api,
	    	"content"	: self || window,
	    	"dialog"	: dialog,
	    	"document"	: dialog.find(".bootbox-body"),
	    	"close"		: function(){
	    		dialog.modal("hide");
	    	},
	    	"reload"	: function(){
	    		
	    		if($.trim(options.href).length>0){
	    			var modal_body = dialog.find(".modal-body");
	    			var modal_dialog = dialog.find(".modal-dialog");
	    			    
	    	    	var loading = '<h3 class="header smaller lighter grey loading-spinner align-center"  style="min-height: 100px !important;">' +
	    				'<i class="icon-spinner icon-spin orange bigger-160"></i>' +
	    				'<span>&nbsp;&nbsp;页面正在加载中...</span>' +
	    			'</h3>';
	    			
	    	    	modal_body.find(".bootbox-body").empty().html(loading);
	    	    	
	    	    	//远程href
	    	    	if(options.href.indexOf("?")>-1){
	    	    		options.href = options.href + "&time="+ (new Date()).getTime();
	    	    	}else{
	    	    		options.href = options.href + "?time="+ (new Date()).getTime();
	    	    	}
	    	    	
	    	    	window.api = {
	    				data : options.data||{},
	    				opener : options.opener,
	    				document : dialog.find(".bootbox-body")
	    			};
	    	    	 
	    	    	var paramMap = $.extend(true,{},options.data||{});
	    	    	delete paramMap.colModel;
	    	    	window.setTimeout(function(){
	    	    		$.ajaxSetup({async: true});
	    	    		modal_body.find(".bootbox-body").empty().load(options.href, paramMap, function(){
	    	    			options.isLoaded = true;
	    	        		dialog.triggerHandler('loaded.bs.modal');
	    	        	});
	    	    		$.ajaxSetup({async: false});
	    	    	}, 200);
	    	    }
	    	},
	    	"button"	: function(opts){
	    		if(opts && opts.id){
	    			var newOpts = $.extend(true,{},opts)
	    			delete newOpts.id;
	    			dialog.find(".modal-footer").find("button[data-bb-handler='"+opts.id+"']").prop(newOpts)
	    		}
	    	}
   		});
	  
	  //判断是否启用按钮锁定
	  if( options.btnlock ){
		  var btn = $(e.target);
		  $.when($.Deferred(function(def){
			  
			  btn.button('loading');
			  
			  var preserveDialog = $.isFunction(callback) && callback.call($.extend(thisMap,{
				  	"reset" : function(){
					   // 改变deferred对象的执行状态为：已完成
					   def.resolve(); 
			  		}
			  }),e) === false;
			  // ... otherwise we'll bin it
			  if (!preserveDialog) {
				  dialog.modal("hide");
			  }
			  
		  }).promise()).always(function(){
			  btn.button('reset');
		  });
	  }else{
		  var preserveDialog = $.isFunction(callback) && callback.call($.extend(thisMap,{
			  	"reset" : function(){
		  		}
		  }),e) === false;
		  // ... otherwise we'll bin it
		  if (!preserveDialog) {
			  dialog.modal("hide");
		  }
	  }
	  
  }

  function getKeyLength(obj) {
    // @TODO defer to Object.keys(x).length if available?
    var k, t = 0;
    for (k in obj) {
      t ++;
    }
    return t;
  }

  function each(collection, iterator) {
	  var index = 0;
	  $.each(collection, function(key, value) {
		  iterator(key, value, index++);
	  });
  }

  function sanitize(options) {
	  var buttons;
	  var total;

	  if (typeof options !== "object") {
		  throw new Error("Please supply an object of options");
	  }

	  if (!options.href&&!options.message) {
		  throw new Error("Please specify a href or message");
	  }

	  // make sure any supplied options take precedence over defaults
	  options = $.extend({}, defaults, options);

	  if (!options.buttons) {
		  options.buttons = {};
	  }

    // we only support Bootstrap's "static" and false backdrop args
    // supporting true would mean you could dismiss the dialog without
    // explicitly interacting with it
    options.backdrop = options.backdrop ? "static" : false;

    buttons = options.buttons;

    total = getKeyLength(buttons);

    each(buttons, function(key, button, index) {

      if ($.isFunction(button)) {
        // short form, assume value is our callback. Since button
        // isn't an object it isn't a reference either so re-assign it
        button = buttons[key] = {
          callback: button
        };
      }

      // before any further checks make sure by now button is the correct type
      if ($.type(button) !== "object") {
        throw new Error("button with key " + key + " must be an object");
      }

      if (!button.label) {
        // the lack of an explicit label means we'll assume the key is good enough
        button.label = key;
      }

      if (!button.className) {
        if (total <= 2 && index === total-1) {
          // always add a primary to the main option in a two-button dialog
        	button.className = "btn-default";
        } else {
        	button.className = "btn-primary";
        }
      }
    });

    return options;
  }

  /**
   * map a flexible set of arguments into a single returned object
   * if args.length is already one just return it, otherwise
   * use the properties argument to map the unnamed args to
   * object properties
   * so in the latter case:
   * mapArguments(["foo", $.noop], ["message", "callback"])
   * -> { message: "foo", callback: $.noop }
   */
  function mapArguments(args, properties) {
    var argn = args.length;
    var options = {};

    if (argn < 1 || argn > 4) {
      throw new Error("Invalid argument length");
    }

    if (argn === 3 || typeof args[0] === "string") {
      options[properties[0]] = args[0];
      options[properties[1]] = args[1];
      options[properties[2]] = args[2];
    }else if (argn === 2 || typeof args[0] === "string") {
        options[properties[0]] = args[0];
        options[properties[1]] = args[1];
    } else {
    	options = args[0];
    }

    return options;
  }

  /**
   * merge a set of default dialog options with user supplied arguments
   */
  function mergeArguments(defaults, args, properties) {
    return $.extend(
      // deep merge
      true,
      // ensure the target is an empty, unreferenced object
      {},
      // the base options object for this type of dialog (often just buttons)
      defaults,
      // args could be an object or array; if it's an array properties will
      // map it to a proper options object
      mapArguments(
        args,
        properties
      )
    );
    
    //defaults, arguments, ["title", "callback"]) 
    
  }

  /**
   * this entry-level method makes heavy use of composition to take a simple
   * range of inputs and return valid options suitable for passing to bootbox.dialog
   */
  function mergeDialogOptions(className, labels, properties, args) {
    //  build up a base set of dialog properties
    var baseOptions = {
      className: "bootbox-" + className,
      buttons: createLabels.apply(null, labels)
    };

    // ensure the buttons properties generated, *after* merging
    // with user args are still valid against the supplied labels
    return validateButtons(
      // merge the generated base properties with user supplied arguments
      mergeArguments(
        baseOptions,
        args,
        // if args.length > 1, properties specify how each arg maps to an object key
        properties
      ),
      labels
    );
  }

  /**
   * from a given list of arguments return a suitable object of button labels
   * all this does is normalise the given labels and translate them where possible
   * e.g. "ok", "confirm" -> { ok: "OK, cancel: "Annuleren" }
   */
  function createLabels() {
    var buttons = {};

    for (var i = 0, j = arguments.length; i < j; i++) {
      var argument = arguments[i];
      var key = argument.toLowerCase();
      var value = argument.toUpperCase();

      buttons[key] = {
        label: _t(value)
      };
    }

    return buttons;
  }

  function validateButtons(options, buttons) {
    var allowedButtons = {};
    each(buttons, function(key, value) {
      allowedButtons[value] = true;
    });

    each(options.buttons, function(key) {
      if (allowedButtons[key] === undefined) {
        throw new Error("button key " + key + " is not allowed (options are " + buttons.join("\n") + ")");
      }
    });

    return options;
  }

  exports.alert = function() {
    var options;

    options = mergeDialogOptions("alert", ["ok"], ["title","message", "callback"], arguments);
    options.draggable = true;
    options.fullScreen = false;
    if(arguments.length==4){
        $.extend(options,arguments[3]);
	}
    if (options.callback && !$.isFunction(options.callback)) {
    	throw new Error("alert requires callback property to be a function when provided");
    }

    /**
     * overrides
     */
    options.buttons.ok.callback = options.onEscape  = options.onClose = function() {
    	if ($.isFunction(options.callback)) {
    	  return options.callback.call(this);
    	}
      	return true;
     };
     options.isLoaded = false;
     return exports.dialog(options);
  };

  exports.confirm = function() {
    var options;

    options = mergeDialogOptions("confirm", ["confirm","cancel"], ["title","message", "callback"], arguments);
    options.draggable = true;
    options.fullScreen = false;
    if(arguments.length==4){
        $.extend(true, options,arguments[3]);
	}
    /**
     * overrides; undo anything the user tried to set they shouldn't have
     */
    options.buttons.cancel.callback = options.onEscape = options.onClose = function() {
      return options.callback.call(this,false);
    };

    options.buttons.confirm.callback = function() {
      return options.callback.call(this,true);
    };

    // confirm specific validation
    if (!$.isFunction(options.callback)) {
      throw new Error("confirm requires a callback");
    }
    options.isLoaded = false;
    return exports.dialog(options);
  };

  exports.prompt = function() {
    var options;
    var defaults;
    var dialog;
    var form;
    var input;
    var shouldShow;
    var inputOptions;

    // we have to create our form first otherwise
    // its value is undefined when gearing up our options
    // @TODO this could be solved by allowing message to
    // be a function instead...
    form = $(templates.form);

    // prompt defaults are more complex than others in that
    // users can override more defaults
    // @TODO I don't like that prompt has to do a lot of heavy
    // lifting which mergeDialogOptions can *almost* support already
    // just because of 'value' and 'inputType' - can we refactor?
    defaults = {
      className: "bootbox-prompt",
      buttons: createLabels("confirm","cancel"),
      value: "",
      inputType: "text"
    };
    
    options = validateButtons(mergeArguments(defaults, arguments, ["title", "callback"]),["confirm","cancel"] );
    options.draggable = true;
    options.fullScreen = false;
	if(arguments.length==3){
        $.extend(true, options, arguments[2]);
	}
    // capture the user's show value; we always set this to false before
    // spawning the dialog to give us a chance to attach some handlers to
    // it, but we need to make sure we respect a preference not to show it
    shouldShow = (options.show === undefined) ? true : options.show;

    // check if the browser supports the option.inputType
    var html5inputs = ["date","time","number"];
    var i = document.createElement("input");
    i.setAttribute("type", options.inputType);
    if(html5inputs[options.inputType]){
      options.inputType = i.type;
    }

    /**
     * overrides; undo anything the user tried to set they shouldn't have
     */
    options.message = form;

    options.buttons.cancel.callback = options.onEscape  = options.onClose = function() {
      return options.callback.call(this,null);
    };

    options.buttons.confirm.callback = function() {
      var value;

      switch (options.inputType) {
        case "text":
        case "textarea":
        case "email":
        case "select":
        case "date":
        case "time":
        case "number":
        case "password":
          value = input.val();
          break;

        case "checkbox":
          var checkedItems = input.find("input:checked");

          // we assume that checkboxes are always multiple,
          // hence we default to an empty array
          value = [];

          each(checkedItems, function(_, item) {
            value.push($(item).val());
          });
          break;
      }

      return options.callback.call(this,value);
    };

    options.show = false;

    // prompt specific validation
    if (!options.title) {
      throw new Error("prompt requires a title");
    }

    if (!$.isFunction(options.callback)) {
      throw new Error("prompt requires a callback");
    }

    if (!templates.inputs[options.inputType]) {
      throw new Error("invalid prompt type");
    }

    // create the input based on the supplied type
    input = $(templates.inputs[options.inputType]);

    switch (options.inputType) {
      case "text":
      case "textarea":
      case "email":
      case "date":
      case "time":
      case "number":
      case "password":
        input.val(options.value);
        break;

      case "select":
        var groups = {};
        inputOptions = options.inputOptions || [];

        if (!inputOptions.length) {
          throw new Error("prompt with select requires options");
        }

        each(inputOptions, function(_, option) {

          // assume the element to attach to is the input...
          var elem = input;

          if (option.value === undefined || option.text === undefined) {
            throw new Error("given options in wrong format");
          }


          // ... but override that element if this option sits in a group

          if (option.group) {
            // initialise group if necessary
            if (!groups[option.group]) {
              groups[option.group] = $("<optgroup/>").attr("label", option.group);
            }

            elem = groups[option.group];
          }

          elem.append("<option value='" + option.value + "'>" + option.text + "</option>");
        });

        each(groups, function(_, group) {
          input.append(group);
        });

        // safe to set a select's value as per a normal input
        input.val(options.value);
        break;

      case "checkbox":
        var values   = $.isArray(options.value) ? options.value : [options.value];
        inputOptions = options.inputOptions || [];

        if (!inputOptions.length) {
          throw new Error("prompt with checkbox requires options");
        }

        if (!inputOptions[0].value || !inputOptions[0].text) {
          throw new Error("given options in wrong format");
        }

        // checkboxes have to nest within a containing element, so
        // they break the rules a bit and we end up re-assigning
        // our 'input' element to this container instead
        input = $("<div/>");

        each(inputOptions, function(_, option) {
          var checkbox = $(templates.inputs[options.inputType]);

          checkbox.find("input").attr("value", option.value);
          checkbox.find("label").append(option.text);

          // we've ensured values is an array so we can always iterate over it
          each(values, function(_, value) {
            if (value === option.value) {
              checkbox.find("input").prop("checked", true);
            }
          });

          input.append(checkbox);
        });
        break;
    }

    if (options.placeholder) {
      input.attr("placeholder", options.placeholder);
    }

    if(options.pattern){
      input.attr("pattern", options.pattern);
    }

    // now place it in our form
    form.append(input);

    form.on("submit", function(e) {
      e.preventDefault();
      // @TODO can we actually click *the* button object instead?
      // e.g. buttons.confirm.click() or similar
      dialog.find(".btn-primary").click();
    });
    options.isLoaded = false;
    dialog = exports.dialog(options);

    // clear the existing handler focusing the submit button...
    dialog.off("shown.bs.modal");

    // ...and replace it with one focusing our input, if possible
    dialog.on("shown.bs.modal", function() {
      input.focus();
      
    });

    if (shouldShow === true) {
      dialog.modal("show");
    }

    return dialog;
  };

  exports.loadBody = function (dialog,options,def){
	  if(dialog && options && $.trim(options.href).length>0){
		  var modal_body = $(dialog).find(".modal-body");
		  if(modal_body.size() > 0){
			  var loading = '<h3 class="header smaller lighter grey loading-spinner align-center"  style="min-height: 100px !important;">' +
				'<i class="icon-spinner icon-spin orange bigger-160"></i>' +
				'<span>&nbsp;&nbsp;页面正在加载中...</span>' +
				'</h3>';
			
			  modal_body.find(".bootbox-body").html(loading);
			
			  //远程href
			  if(options.href.indexOf("?")>-1){
				  options.href = options.href + "&time="+ (new Date()).getTime();
			  }else{
				  options.href = options.href + "?time="+ (new Date()).getTime();
			  }
			  window.api = {
				  data 		: options.data||{},
				  opener 	: $("div.bootbox").size()>0?$("div.bootbox").last():document.body,
				  document 	: dialog.find(".bootbox-body")
			  };
			  var paramMap = $.extend(true,{},options.data||{});
			  delete paramMap.colModel;
			  $.ajaxSetup({async: false});
			  modal_body.find(".bootbox-body").load(options.href, paramMap, function(){
				  options.isLoaded = true;
				  // 改变deferred对象的执行状态为：已完成
			      def.resolve(); 
			  });
			  $.ajaxSetup({async: true});
		  }
	  }
  };
	
  exports.dialog = function(options) {
	  
	   
	delete defaults.width;
    options = sanitize(options);
    options.opener = $("div.bootbox").size()>0?$("div.bootbox").last():document.body;
    
    options.beforeRender.call(this);
    
    var dialog = $(templates.dialog);
    var modal_body = dialog.find(".modal-body");
    var modal_dialog = dialog.find(".modal-dialog");
    var modal_header = dialog.find('.modal-header');
    var bootbox_body = dialog.find('.bootbox-body');
    var modal_footer = dialog.find('.modal-footer');
    
    //console.log("class：" + dialog.attr("class") );
    //console.log("className：" + options.className );
    
    function _isSmallModel(){
    	 return (options.className == "bootbox-alert" || options.className == "bootbox-confirm" || options.className == "bootbox-prompt");
    }
    
    function _getActualWidth(){
    	// 浏览器窗口高度
    	var _windowWidth = $(window).width();
    	var _width = $.isFunction(options.width) ? options.width.call(this) : options.width;
	    //定义初始宽度
	    if (_width && /.*%$/.test(_width)) {
	    	return {
				"windowWidth"	: _windowWidth,
				"realWidth"		: ( _windowWidth * parseFloat(_width)) / 100 
			};
	    }
	    //
	    _width = parseInt(_width);
	    //console.log("width：" + _width );
	    
	    if(_isSmallModel()){
	    	
	    	return {
				"windowWidth"	: _windowWidth,
				"realWidth"		: _width
			};
		}
	    
	    
    	var _realWidth = Math.max(Math.min(_width,_windowWidth),0);
		//初始宽度比窗口大，说明窗口进行了缩小而且缩小的宽度已经无法显示弹窗：此时以窗口宽度为弹窗宽度进行计算
		if(_width >= _windowWidth){
			_realWidth = _windowWidth - options.offsetWidth * 2;
		}else if(_windowWidth <= 768 ){
			_realWidth = _windowWidth - options.offsetWidth * 2;
		} else{
			//如果宽度大于300临界值，视为正常窗口尺寸下的弹出层
			if(_width >= 300 ){
				_realWidth = _width;
			}
			//此类弹出层基本是宽度较大，在比较窄的窗口弹出，导致计算的宽度异常
			else{
				if( _windowWidth > 1280 ){
					_realWidth = _windowWidth - options.offsetWidth * 4;
				}else if( _windowWidth > 768 && _windowWidth <= 1280){
					_realWidth = _windowWidth - options.offsetWidth * 2;
				}
			}
		}
		
		
		_realWidth = Math.max(_realWidth,300);
		
		//console.log("windowWidth：" + _windowWidth );
    	//console.log("realWidth：" + _realWidth );
    	
		return {
			"windowWidth"	: _windowWidth,
			"realWidth"		: _realWidth
		};
    }
    
    function _getDimension(){
    	
    	modal_body = dialog.find(".modal-body");
	    modal_dialog = dialog.find(".modal-dialog");
	    modal_header = dialog.find('.modal-header');
	    bootbox_body = dialog.find('.bootbox-body');
	    modal_footer = dialog.find('.modal-footer');
	    // 浏览器窗口高度
    	var _windowWidth = $(window).width();
    	// 浏览器窗口高度
    	var _windowHeight = $(window).height();
    	// 导航栏高度
    	var _navbarHeight = $.fn.actual ? $("#navbar_container").actual("outerHeight") : $("#navbar_container").outerHeight(true);
    	// 内容区域高度
    	var _containerHeight = Math.abs( _windowHeight - _navbarHeight );
    	// 弹出层高度
    	var _dialogHeight	= ($.fn.actual ? modal_dialog.actual("outerHeight") : modal_dialog.outerHeight(true)) || modal_dialog.height();
    	// 弹出层标题高度
    	var _headerHeight	= ($.fn.actual ? modal_header.actual("outerHeight") : modal_header.outerHeight(true)) || modal_header.height() ;
    	// 弹出层按钮区域高度
    	var _footerHeight	= ($.fn.actual ? modal_footer.actual("outerHeight") : modal_footer.outerHeight(true)) || modal_footer.height() ;
    	// 弹出层内容区域高度
    	var _contentHeight  = _dialogHeight - _headerHeight - _footerHeight;
    	// 弹出层内容区域允许的最大高度
    	var _maxHeight  = Math.abs(Math.abs(_containerHeight - options.offsetHeight ) - _headerHeight - _footerHeight );
    	
    	// 返回Object对象
    	return {
    		"windowWidth"		: _windowWidth,
    		"windowHeight" 		: _windowHeight,
    		"navbarHeight" 		: _navbarHeight,
    		"containerHeight" 	: _containerHeight,
    		"dialogHeight" 		: _dialogHeight,
    		"headerHeight" 		: _headerHeight,
    		"contentHeight" 	: _contentHeight,
    		"footerHeight" 		: _footerHeight,
    		"maxHeight" 		: _maxHeight,
    		"top"				: function(){
    			
    				if(_isSmallModel()){
        				return (_windowHeight - _dialogHeight)/2;
        			}
        			
        			//console.log('_containerHeight:'+_containerHeight+'_dialogHeight:'+_dialogHeight+'_navbarHeight:'+_navbarHeight);
        			
        			return (_containerHeight - _dialogHeight)/2 + _navbarHeight;
    			
    		}.call(this) 
    	};
    	
    }
    
    function resetPostion(){
    	
    	// 计算尺寸
    	var _dimension = _getDimension();
		// 计算宽度
    	var _actualWidth = _getActualWidth();
    	var _realWidth = _actualWidth["realWidth"];
    	
    	var _boxTop;
    	if((Math.max(_dimension["top"],0)<50)){
    		_boxTop=(Math.max(_dimension["top"],0));
    	}else{
    		_boxTop=(Math.max(_dimension["top"],0)-50);
    	}
    	if(options.draggable && $.fn.draggable){
    		
    		modal_dialog.addClass('modal-dialog-reset');
    		
    		var left = (_actualWidth["windowWidth"] - modal_dialog.width())/2;
    		//console.log(modal_dialog.width());
    		//初始窗口大于浏览器窗口；应以浏览器窗口为最大值
    		if( _actualWidth["windowWidth"] > 768 || _isSmallModel() ){
    			// 重置弹出层定位值
    			modal_dialog.attr({ "style": "width: "+ _realWidth +"px;"});
    			modal_dialog.css({
    				"left"			: left + "px",
    				"top"			: _boxTop + "px",
    	   			"width" 		: _realWidth + "px"
    	   		});
    		}else{
    			// 重置弹出层定位值
    			modal_dialog.css({
    				"left"			: left + "px",
    				"top"			: (_dimension["navbarHeight"])+ "px",
    	   			"width" 		: _realWidth + "px"
    			});
    			
    		}
    		
    	} else {
    		//初始窗口大于浏览器窗口；应以浏览器窗口为最大值
    		if( _actualWidth["windowWidth"] > 768 || _isSmallModel() ){
    			// 重置弹出层定位值
    			modal_dialog.attr({ "style": "width: "+ _realWidth +"px;"});
    			
    			modal_dialog.css({
    				"top"			: _boxTop + "px",
    	   			"width" 		: _realWidth + "px"
    	   		});
    			
    		}else{
    			// 重置弹出层定位值
    			modal_dialog.css({
    				"top"			: (_dimension["navbarHeight"])+ "px",
    	   			"width" 		: _realWidth + "px"
    			});
    		}
    	}
    	setBodyMaxHeight();
    }
    
    // 计算当前窗口内部内容的真实高度
	function iframeHeight() {
		try {
			var bHeight = window.contentWindow.document.body.scrollHeight;
			var dHeight = window.contentWindow.document.documentElement.scrollHeight;
			var height = Math.max(bHeight, dHeight);
			//console.log("iframeHeight:" + height);
			return height;
		} catch (ex) {
			return $(window).height();
		}
	}
    
    function setBodyMaxHeight(){
    	setTimeout(function(){
    		var windowsHeight=$(window).height();
        	var maxHeight;
    		var $bodyHeight=dialog.find('.bootbox-body').height();
    		
    		if(dialog.hasClass('box-full-screen')){
    			maxHeight=windowsHeight-140;
    		}else {
    			maxHeight = windowsHeight-200;
    		}
    		if(maxHeight>350 && $bodyHeight>300){
            	modal_dialog.find('.modal-body').css({
            		'max-height': (options.max_height ? options.max_height : maxHeight)
            		//'overflow-y':'auto',
            		//'overflow-x':'hidden'
            		
            	});
        	}else{
        		modal_dialog.find('.bootbox-body').css({
            		'max-height':'auto',
            	});
        		modal_dialog.find('.bootbox-body').removeAttr('overflow-x');
        		modal_dialog.find('.bootbox-body').removeAttr('overflow-y');
        	}

        	if (parseInt($bodyHeight)<200) {

        		dialog.find('.modal-body').css({
        			'overflow-y':'inherit'
        		});
        	}else{

        		dialog.find('.modal-body').css({
        			'overflow-y':'scroll'
        		});
        	}
        	
        	if(parseInt($bodyHeight)<100){
        		dialog.find('.modal-body').css({
        			'overflow-x':'inherit'
        		});
        	}

    		//resetPostion();
        	
    	},700);
    }
    
    
    //set data
    $(dialog).data("options",options);
    
    var buttons = options.buttons;
    var buttonStr = "";
    var callbacks = {
    	onEscape: options.onEscape || $.noop,
    	onClose : options.onClose || options.onEscape || $.noop
    };

    each(buttons, function(key, button) {
      // @TODO I don't like this string appending to itself; bit dirty. Needs reworking
      // can we just build up button elements instead? slower but neater. Then button
      // can just become a template too
      buttonStr += "<button id='btn_" + key + "' data-bb-handler='" + key + "' data-loading-text='" + button.label + "' type='button' class='btn btn-sm " + button.className + "'>" + button.label + "</button>";
      callbacks[key] = button.callback;
    });

    
    if (options.animate === true) {
    	dialog.addClass("fade");
    }
   
    //定义初始宽度
    if (options.width) {
    	//计算宽度
    	var _actualWidth = _getActualWidth();
    	var _realWidth = _actualWidth["realWidth"];
    	if(_realWidth){
    		//根据宽度自动生成class名称
        	dialog.className = dialog.className || "bootbox-" + _realWidth;
        	//设置初始宽度
    		modal_dialog.attr({ "style": "width: "+ _realWidth +"px;"});
    	}
    }
    
    if (options.height) {
    	var prop = options.height ? 'height' : 'max-height',
    		value = options.height || this.options.maxHeight;
    	if (value){
    		dialog.find('.modal-body')/*.css('overflow', 'auto')*/.css(prop, value);
		}
    }
    
    if (options.max_height) {
    	dialog.find('.modal-body').css('max-height',options.max_height);
    }else{
    	setBodyMaxHeight();
    }
    
    
    if (options.className) {
    	dialog.addClass(options.className);
    }else{
    	dialog.addClass("bootbox-dialog");
    }

    if (options.title) {
    	modal_body.before(templates.header);
    }

    if (options.closeButton) {
    	var closeButton = $(templates.closeButton);

    	if (options.title) {
    		dialog.find(".modal-header").prepend(closeButton);
    	} else {
    		closeButton.css("margin-top", "-10px").prependTo(modal_body);
    	}
    }
    
    if (options.fullScreen) {
    	var fullButton=$(templates.fullScreen);
    	dialog.find(".modal-header .bootbox-close").after(fullButton);
    	$(document).off('click','.bootbox-full').on('click','.bootbox-full',function(){
    		if(dialog.hasClass('box-full-screen')){
    			dialog.removeClass('box-full-screen');
    			dialog.find('.modal-header .fa-minus-square-o').addClass('fa-square').removeClass('fa-minus-square-o');
    		}else{
    			dialog.addClass('box-full-screen');
    			dialog.find('.modal-header .fa-square').addClass('fa-minus-square-o').removeClass('fa-square');
    		}
    		
    		$(window).trigger("resize");
    		setBodyMaxHeight();
    	});
    }
    
    if (options.modalName) {
        dialog.attr("name", options.modalName);
        dialog.attr("id", options.modalName);
    }
    
    if (options.title) {
    	dialog.find(".modal-title").html(options.title);
    }
    
    if (buttonStr.length) {
    	modal_body.after(templates.footer);
    	dialog.find(".modal-footer").html(buttonStr);
    }
    
    if (options.progress) {
    	modal_body.after(templates.progress);
    	dialog.find(".progress-bar").attr("id",options.progressID||"bootboxStatus");
    }
    
    /**
  	 * 从远端的数据源加载完数据之后触发该事件。
  	 */
    dialog.on("loaded.bs.modal", function() {
	   	//页面加载完成后的回调函数
	   	if($.isFunction(options.onLoaded)){
			options.onLoaded.call(this,dialog);
	   	}
	   //	console.log("loaded.bs.modal" );
    });
    
   	//利用JQuery的延时对象保证逻辑代码是在加载完成之后调用
	$.when($.Deferred(function(def){
		//弹窗对象记录延时对象
		dialog.data("def",def);
		/*普通的内容*/
	    if (options.message) {
	    	modal_body.find(".bootbox-body").html(options.message);
	    	// 改变deferred对象的执行状态为：已完成
	    	def.resolve(); 
	    }else{
	    	exports.loadBody(dialog,options,def);
	    }
    }).promise()).done(function(){
    	setBodyMaxHeight();
    	$(dialog).trigger('loaded.bs.modal');
    });
	
    //hide 方法调用之后立即触发该事件。
    dialog.on("hide.bs.modal", function(event) {
    	//卸载模态窗尺寸变化监控事件
    	dialog.off("resize.bs.modal").off("resized.bs.modal");
    	//事件回调
		if($.isFunction(options.onHide)){
			options.onHide.call(this,event);
	   	}
    	//console.log("hide.bs.modal" );
    });
    
    /**
     * Bootstrap event listeners; used handle extra
     * setup & teardown required after the underlying
     * modal has performed certain actions
     */
    //此事件在模态框被隐藏（并且同时在 CSS 过渡效果完成）之后被触发。
    dialog.on("hidden.bs.modal", function(event) {
	     // ensure we don't accidentally intercept hidden events triggered
	     // by children of the current dialog. We shouldn't anymore now BS
	     // namespaces its events; but still worth doing
	     if (event.target === this) {
	    	  dialog.remove();
	     }
	     
	     //使当前模态框的打开者获得焦点
	     //alert($(options.opener).next(".modal-backdrop")[0]);
	     /* 弹窗个数*/
	     if($("div.bootbox").size()==0){
	    	  $(document.body).removeClass("modal-open");
	     }
	     
	     //判断是否有固定区域逻辑
	   	 if ($.fn.fixed && options.fixedTarget && $(options.fixedTarget).size()==1) {
	   		 $(options.fixedTarget).fixed("destroy");
	   	 }
	   	 //事件回调
	   	 if($.isFunction(options.onHidden)){
	   		 options.onHidden.call(this,event);
	   	 }
	   	 //console.log("hidden.bs.modal" );
    });

    //Window窗口变化监控：改变弹窗位置和宽度
    dialog.on("resized.bs.modal", function(event) {
    	
    	//设置窗口位置
    	resetPostion();
    	
		//事件回调
		if($.isFunction(options.onResized)){
			options.onResized.call(this,event);
	   	}
		//console.log("resized.bs.modal" );
		
    });
    
    /**
     * show 方法调用之后立即触发该事件。如果是通过点击某个作为触发器的元素，
     * 则此元素可以通过事件的 relatedTarget 属性进行访问。
     */
    dialog.on("show.bs.modal", function(event) {
    	// sadly this doesn't work; show is called *just* before
    	// the backdrop is added so we'd need a setTimeout hack or
    	// otherwise... leaving in as would be nice
    	// alert("show");
    	if (options.backdrop) {
    		dialog.next(".modal-backdrop").addClass("bootbox-backdrop");
      	}
    	//事件回调
		if($.isFunction(options.onShow)){
			options.onShow.call(this,event);
	   	}
    	//console.log("show.bs.modal" );
    });
    
    /**
     * 此事件在模态框已经显示出来（并且同时在 CSS 过渡效果完成）之后被触发。
     * 如果是通过点击某个作为触发器的元素，则此元素可以通过事件的 relatedTarget 属性进行访问。
     */
    dialog.on("shown.bs.modal", function(event) {
    	
    	//设置窗口位置
    	resetPostion();
    	
    	/*兼容多个弹窗遮罩*/
      	var size = $("div.bootbox").size();
    	if(size>1){
      		var z_index = parseInt(dialog.css("z-index")||"1050");
      		var new_z_index = z_index + ((size - 1) * 100 );
      		dialog.css("z-index",new_z_index+100);
      		if (options.backdrop != false) {
      			$(document.body).find("div.modal-backdrop").last().css("z-index",new_z_index);
      		}
      	}
    	dialog.find("input[name='focusInput']").focus();
    	//dialog.find(".btn-primary:first").focus();
    	
    	//扩展增加拖拽支持
    	if(options.draggable && $.fn.draggable){
    		/*
    		containment : Selector,Element,String, Array : false    
    		强制draggable只允许在指定元素或区域的范围内移动，可选值：'parent', 'document', 'window', [x1, y1, x2, y2].   
    		初始：$('.selector').draggable({ containment: 'parent' });   
		  	获取：var containment = $('.selector').draggable('option', 'containment');   
		  	设置：$('.selector').draggable('option', 'containment', 'parent');  
    		 */
    		
    		modal_dialog.draggable({ 
    			scroll		: true,
    			containment	: modal_dialog.closest(".bootbox"),
    			handle		: '.modal-header,.modal-footer',
    			start		: function(event, ui){
    				
    				/*
    				所有的事件回调函数都有两个参数：event和ui，浏览器自有event对象，和经过封装的ui对象   
    				ui.helper - 表示被拖拽的元素的JQuery对象   
    				ui.position - 表示相对当前对象，鼠标的坐标值对象{top,left}   
    				ui.offset - 表示相对于当前页面，鼠标的坐标值对象{top,left} 
    				*/ 
    				
    				//margin-left:inherit !important;
    				//margin-right:inherit !important;
    				
    				/*ui.helper.addClass('modal-dialog-reset');*/
    				
    				var offsetLeft=ui.helper.offset().left;
//    				console.log(offsetLeft);
    				ui.helper.addClass('modal-dialog-reset');
    				/*modal_dialog.data('left',offsetLeft);
    				ui.helper.css({
    					'margin-left':offsetLeft-10,
    				});*/
    			},
    			drag		: function(event, ui){
    				/*
    				console.log(ui.offset['top']);
    				
    				ui.helper.attr('margin','');
    				var offsetLeft=ui.helper.offset().left;
    				ui.helper.css({
    					'left':modal_dialog.data('left'),
    				});*/
    			},
    			stop		: function(event, ui){
    				/*ui.helper.attr('margin','');*/
    				/*ui.helper.addClass('modal-dialog-reset');*/
    				if(ui.helper.offset().top<0){
    					modal_dialog.css({
    						'top':'0'
    					});
    				}

    				setBodyMaxHeight();
    			}
    		});
    		dialog.find('.modal-header,.modal-footer').css('cursor','move');
    	}
		
		//弹窗组件最终完成前的回调函数
    	options.afterRender.call({
	    	"api": window.api,
	    	"content": self || window,
	    	"document": dialog.find(".bootbox-body"),
	    	"close":function(){
	    		dialog.modal("hide");
	    	},
	    	"button":function(opts){
	    		if(opts && opts.id){
	    			var newOpts = $.extend(true,{},opts)
	    			delete newOpts.id;
	    			dialog.find(".modal-footer").find("button[data-bb-handler='"+opts.id+"']").prop(newOpts)
	    		}
	    	}
		});
    	
    	dialog.removeClass("hide");
    	
    	//事件回调
		if($.isFunction(options.onShown)){
			options.onShown.call(this,event);
	   	}
		//console.log("shown.bs.modal" );
		
		$(window).trigger("resize");
		setBodyMaxHeight();
		
    });
 
    /**
     * Bootbox event listeners; experimental and may not last
     * just an attempt to decouple some behaviours from their
     * respective triggers
     */

    dialog.on("escape.close.bb", function(e) {
    	if (callbacks.onEscape) {
    		processCallback(e, dialog, options , callbacks.onEscape);
    	}
    });
    
    /**
     * Standard jQuery event listeners; used to handle user
     * interaction with our dialog
     */

    dialog.on("click", ".modal-footer button", function(e) {
    	
    	var callbackKey = $(this).data("bb-handler");

      	processCallback(e, dialog, options ,callbacks[callbackKey]);

    });

    dialog.on("click", ".bootbox-close-button", function(e) {
    	// onEscape might be falsy but that's fine; the fact is
    	// if the user has managed to click the close button we
    	// have to close the dialog, callback or not
    	if (callbacks.onClose) {
            processCallback(e, dialog, options , callbacks.onClose);
        }
    	// processCallback(e, dialog, callbacks.onEscape);
    });

    dialog.on("keyup", function(e) {
    	if (e.which === 27) {
    		dialog.trigger("escape.close.bb");
    	}
    });

    // the remainder of this method simply deals with adding our
    // dialogent to the DOM, augmenting it with Bootstrap's modal
    // functionality and then giving the resulting object back
    // to our caller

    $(options.container).append(dialog);
    
    //利用JQuery的延时对象保证逻辑代码是在加载完成之后调用
	$.when(dialog.data("def")).done(function(){
    	
    	// 计算尺寸
    	var _dimension = _getDimension();
    	if( _isSmallModel() ){
	    	// 计算弹出层定位值
			modal_dialog.css({
	   			"top"			: Math.max(_dimension["top"],0) + "px",
	   		});
    	}
    	
        dialog.modal({
        	//message		:	options.message|,
        	//remote 		:	options.href||"",
        	backdrop		: 	options.backdrop,
        	keyboard		: 	true,
        	replace			: 	true,
        	show			: 	false
        });

        //卸载模态窗尺寸变化监控事件
    	dialog.off("resize.bs.modal");
    	
        
        if (options.show) {
        	dialog.modal("show");
        }
        
        //添加浏览器窗口的大小调整时，发生 resize 事件
        $(window).resize(function(event){
        	var def = modal_dialog.data("def");
			if(def){
				return false;
			}
			$.when($.Deferred(function(def){
				//弹窗对象记录延时对象
				modal_dialog.data("def",def);
				//触发尺寸变化
				dialog.triggerHandler("resized.bs.modal",[event]);
				//延时改变状态，方式重置事件太快导致卡顿
			  	window.setTimeout(function(){
			  		// 改变deferred对象的执行状态为：已完成
				    def.resolve(); 
    	    	}, 500);
		    }).promise()).done(function(){ 
	        	modal_dialog.removeData("def");
			});
        });
        
	});

   
    // @TODO should we return the raw element here or should
    // we wrap it in an object on which we can expose some neater
    // methods, e.g. var d = bootbox.alert(); d.hide(); instead
    // of d.modal("hide");

   /*
    function BBDialog(elem) {
      this.elem = elem;
    }

    BBDialog.prototype = {
      hide: function() {
        return this.elem.modal("hide");
      },
      show: function() {
        return this.elem.modal("show");
      }
    };
    */

    return dialog;

  };
  

  exports.setDefaults = function() {
	  var values = {};

	  if (arguments.length === 2) {
		  // allow passing of single key/value...
		  values[arguments[0]] = arguments[1];
	  } else {
		  // ... and as an object too
		  values = arguments[0];
	  }

	  $.extend(defaults, values);
  };

  var clearIframe = function (el) {
	  if(el){
		  var iframe = el.contentWindow;
		  try {
			  el.src = 'about:blank';
	    	   
	    	  iframe.document.write('');
	    	  iframe.document.close();
	          iframe.document.clear();
	      } catch (e) { } 
	      //以上可以清除大部分的内存和文档节点记录数了
	      //最后删除掉这个 iframe 
	      $(el).remove();
	      //document.body.removeChild();
	  }
  };
  
  exports.hideModal = function(name) {
	  //删除弹窗内的iframe,防止再次打开时无法获取焦点问题
	  $(".bootbox[name=" + name + "]").find("iframe").each(function (i, iframe) {
          if (typeof clearIframe == 'function') {
              clearIframe(iframe);
          }
      });
	  $(".bootbox[name="+name+"]").modal("hide");
  };
  
  exports.hideAll = function() {
	  //删除弹窗内的iframe,防止再次打开时无法获取焦点问题
	  $(".bootbox").find("iframe").each(function (i, iframe) {
          if (typeof clearIframe == 'function') {
              clearIframe(iframe);
          }
      });
	  $(".bootbox").modal("hide");
  };


  /**
   * standard locales. Please add more according to ISO 639-1 standard. Multiple language variants are
   * unlikely to be required. If this gets too large it can be split out into separate JS files.
   */
  var locales = {
    br : {
      OK      : "OK",
      CANCEL  : "Cancelar",
      CONFIRM : "Sim"
    },
    da : {
      OK      : "OK",
      CANCEL  : "Annuller",
      CONFIRM : "Accepter"
    },
    de : {
      OK      : "OK",
      CANCEL  : "Abbrechen",
      CONFIRM : "Akzeptieren"
    },
    en : {
      OK      : "OK",
      CANCEL  : "Cancel",
      CONFIRM : "OK"
    },
    es : {
      OK      : "OK",
      CANCEL  : "Cancelar",
      CONFIRM : "Aceptar"
    },
    fi : {
      OK      : "OK",
      CANCEL  : "Peruuta",
      CONFIRM : "OK"
    },
    fr : {
      OK      : "OK",
      CANCEL  : "Annuler",
      CONFIRM : "D'accord"
    },
    he : {
      OK      : "אישור",
      CANCEL  : "ביטול",
      CONFIRM : "אישור"
    },
    it : {
      OK      : "OK",
      CANCEL  : "Annulla",
      CONFIRM : "Conferma"
    },
    lt : {
      OK      : "Gerai",
      CANCEL  : "Atšaukti",
      CONFIRM : "Patvirtinti"
    },
    lv : {
      OK      : "Labi",
      CANCEL  : "Atcelt",
      CONFIRM : "Apstiprināt"
    },
    nl : {
      OK      : "OK",
      CANCEL  : "Annuleren",
      CONFIRM : "Accepteren"
    },
    no : {
      OK      : "OK",
      CANCEL  : "Avbryt",
      CONFIRM : "OK"
    },
    pl : {
      OK      : "OK",
      CANCEL  : "Anuluj",
      CONFIRM : "Potwierdź"
    },
    ru : {
      OK      : "OK",
      CANCEL  : "Отмена",
      CONFIRM : "Применить"
    },
    sv : {
      OK      : "OK",
      CANCEL  : "Avbryt",
      CONFIRM : "OK"
    },
    tr : {
      OK      : "Tamam",
      CANCEL  : "İptal",
      CONFIRM : "Onayla"
    },
    zh_CN : {
      OK      : "确  定",
      CANCEL  : "取  消",
      CONFIRM : "确  认"
    },
    zh_TW : {
      OK      : "OK",
      CANCEL  : "取消",
      CONFIRM : "確認"
    }
  };

  exports.init = function(_$) {
    return init(_$ || $);
  };

  return exports;
}));
