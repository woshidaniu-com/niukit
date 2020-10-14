/*
 * The outerClick event is fired when an element outside of the target element is clicked.
 * 
 * Usage:
 * $(selector).bind("outerClick", fn);   // Bind the function fn to the outerClick event on each of the matched elements.
 * $(selector).outerClick(fn);           // Bind the function fn to the outerClick event on each of the matched elements.
 * $(selector).trigger("outerClick");    // Trigger the outerClick event on each of the matched elements.
 * $(selector).outerClick();             // Trigger the outerClick event on each of the matched elements.
 * $(selector).unbind("outerClick", fn); // Unbind the function fn from the outerClick event on each of the matched elements.
 * $(selector).unbind("outerClick");     // Unbind all outerClick events from each of the matched elements.
 */
 
/*global jQuery */
;(function ($) {
	var
		Outerclick,
		$html = $('html'),
		handler = typeof $().on ? 'on' : 'bind';

	Outerclick = function ($elementTarget, functionCall) {
		this.$elementTarget = $elementTarget;
		this.functionCall = functionCall;

		this.init();
	};
	Outerclick.prototype = {
		init: function () {
			this.bindEvent();
		},
		bindEvent: function () {
			$html[handler]('click', $.proxy(function (e) {
				this.checkOuterclick(e);
			}, this));
		},
		checkOuterclick: function (e) {
			var
				clickX = e.pageX,
				clickY = e.pageY,
				offsetElementTarget = this.$elementTarget.offset(),
				offsetTopElementTarget = offsetElementTarget.top,
				offsetBottomElementTarget = offsetTopElementTarget + this.$elementTarget.height(),
				offsetLeftElementTarget = offsetElementTarget.left,
				offsetRightElementTarget = offsetLeftElementTarget + this.$elementTarget.width();

			if (!(
				clickY > offsetTopElementTarget &&
				clickY < offsetBottomElementTarget &&
				clickX > offsetLeftElementTarget &&
				clickX < offsetRightElementTarget
			)) {
				this.functionCall(e);
			}
		}
	};
	$.fn.outerClick = function (functionCall) {
		this.each(function () {
			new Outerclick($(this), functionCall);
		});
	};
})(jQuery);
