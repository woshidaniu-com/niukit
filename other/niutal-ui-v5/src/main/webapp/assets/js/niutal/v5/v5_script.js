/**
 * 应用于v5框架的一些插件
 */

// @ sourceURL=v5_script.js
// 针对checkbox,radio的优化
;
(function($) {

//	$(document).off('iCheck.data-api',
//			'.form-group input[type="checkbox"],input[type="radio"]').on(
//			'iCheck.data-api',
//			'.form-group input[type="checkbox"],input[type="radio"]',
//			function() {
//				// $(this).iCheck({
//				// checkboxClass: 'icheckbox_square-blue',
//				// radioClass: 'iradio_square-blue',
//				// increaseArea: '1%' // optional
//				// });
//				// $(this).on('ifChecked', function(event) {
//				// $(this).attr('checked', 'checked')
//				// });
//				// $(this).on('ifUnchecked', function(event) {
//				// $(this).removeAttr("checked");
//				// });
//
//			}).off('click', '.fixed-table-body tr').on('click',
//			'.fixed-table-body tr', function() {
//
//				// if($(this).hasClass('selected')){
//				// $(this).find('.icheckbox_square-blue').removeClass('checked');
//				// }else{
//				// $(this).find('.icheckbox_square-blue').addClass('checked');
//				// }
//
//			}).ajaxComplete(function(event, xhr, options) {
//		/* 设置Ajax请求完成后的处理函数【失败或者成功都会调用】 */
//		// $('input[type="checkbox"],input[type="radio"]').trigger("iCheck");
//	});

	setPageHeight();
	function setPageHeight() {
		// 设置子页面最小高度
		var pageHeight = $(window).height() - $('#tabs').height()
				- $('#footerID').height() - 30;
		$('.container-func.sl_all_bg').css({
			'min-height' : pageHeight,
			'height' : pageHeight
		});
	}
	var flag = true;
	$(window).resize(function() {
		// 设置子页面最小高度
		setPageHeight();
		if (flag == true) {
			// 滚动条插件
			$('.container-func.sl_all_bg').mCustomScrollbar({
				theme: "inset-dark",
				autoHideScrollbar	: false,
				autoExpandScrollbar	: false,
				horizontalScroll 	: false,
				scrollInertia	 	: 80,
				advanced : {
					updateOnBrowserResize : true,
					updateOnContentResize : true,
					updateOnSelectorChange: false,
					autoExpandHorizontalScroll : true
				},
				scrollButtons:{ 
					scrollSpeed:10000,
					scrollAmount:500
				},
				callbacks:{
					onOverflowY:function(){
						$(window).trigger("resize");
					},
					onOverflowYNone:function(){
						$(window).trigger("resize");
					}
				}
			});
		}
		
		flag=false;

	});
	
}(jQuery));
