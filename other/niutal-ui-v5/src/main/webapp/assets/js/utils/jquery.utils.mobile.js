;(function($){
	$.extend($, {
		//为true时是手机端，为false时是PC端
		 isMobile : function() {
			var width = $(window).width();
			return width <= 768;
		}
	});
}(jQuery));
