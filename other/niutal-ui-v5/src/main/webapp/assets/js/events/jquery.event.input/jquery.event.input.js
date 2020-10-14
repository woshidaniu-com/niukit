/*
 * The input event is fired when an element value is changed.
 * 
 * Usage:
 * $(selector).bind("input", fn);   // Bind the function fn to the input event on each of the matched elements.
 * $(selector).input(fn);           // Bind the function fn to the input event on each of the matched elements.
 * $(selector).trigger("input");    // Trigger the input event on each of the matched elements.
 * $(selector).input();             // Trigger the input event on each of the matched elements.
 * $(selector).unbind("input", fn); // Unbind the function fn from the input event on each of the matched elements.
 * $(selector).unbind("input");     // Unbind all input events from each of the matched elements.
 * jQuery input event
 * Author: tangbin
 * Blog: http://www.planeart.cn
 * Date: 2011-08-18 15:15
 */
(function ($, elements, EVENT_TYPE) {
	
	// IE6\7\8不支持input事件，但支持propertychange事件
	if ('onpropertychange' in document) {
        // 检查是否为可输入元素
        var rinput = /^INPUT|TEXTAREA$/,
            isInput = function (elem) {
               return rinput.test(elem.nodeName);
            };
               
        $.event.special[EVENT_TYPE] = {
            setup: function () {
                var elem = this;
                if (!isInput(elem)) return false;
                $.data(elem, '@oldValue', elem.value);
                $.event.add(elem, 'propertychange', function (event) {
                    // 元素属性任何变化都会触发propertychange事件
                    // 需要屏蔽掉非value的改变，以便接近标准的onput事件
                    if ($.data(this, '@oldValue') !== this.value) {
                        $.event.trigger('input', null, this);
                    };
                    $.data(this, '@oldValue', this.value);
                });
            },
            teardown: function () {
                var elem = this;
                if (!isInput(elem)) return false;
                $.event.remove(elem, 'propertychange');
                $.removeData(elem, '@oldValue');
            }
        };
	};
	
	/**
	 * Event helper input
	 * 
	 * @param  {Function} [fn]  A function to bind to the outerClick event on each of the matched elements.
	 *                          If fn is omitted the event is triggered.
	 * @return {jQuery}         Returns the jQuery object.
	 */
	$.fn[EVENT_TYPE] = function (fn) {
		return fn ? this.bind(EVENT_TYPE, fn) : this.trigger(EVENT_TYPE);
	};

})(jQuery, [], 'input');