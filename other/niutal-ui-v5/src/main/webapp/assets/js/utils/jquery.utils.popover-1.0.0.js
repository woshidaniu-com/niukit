/*
 * 对popover在bootbox中做兼容处理
 */
;$(function(){
	$('.popover').on('shown.bs.popover', function () {
		var _this=$(this);
		var t=parseInt($(this).css('top'));
		var bootbox=$('.bootbox');
			if(t<100 && bootbox.size()>0){
				_this.addClass('topmost');
			}
	})
});

