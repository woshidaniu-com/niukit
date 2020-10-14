;(function($) {
	
	bootbox.setDefaults({
		/**
		* @optional Boolean
		* @default: true
		* whether the dialog should be shown immediately
		*/
		show: true,
		/**
	     * @optional Boolean
		 * @default: false
	     * 是否需要进度条
	     */
	    progress : false,
	    /**
	     * @optional Boolean
		 * @default: false
	     * 是否点击按钮时，锁定按钮点击状态 
	     */
	    btnlock : false,
		/**
		* @optional Boolean
		* @default: true
		* whether the dialog should be have a backdrop or not
		*/
		backdrop: false,
		
		 //是否允许窗口拖拽
	    draggable : false,
	    
		/**
		* @optional Boolean
		* @default: true
		* show a close button
		*/
		closeButton: true,
		/**
		* @optional Boolean
		* @default: true
		* animate the dialog in and out (not supported in < IE 10)
		*/
		animate: false,
		/**
		* @optional String
		* @default: null
		* an additional class to apply to the dialog wrapper
		*/
		className: "my-modal"
	});
	
})(jQuery);