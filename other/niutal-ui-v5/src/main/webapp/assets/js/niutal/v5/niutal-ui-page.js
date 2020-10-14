jQuery(function($) {

	// 地址栏传递的高度：即第一次打开iframe窗口的高度
	var search = window.location.search;
	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}
	var tabHeight = GetQueryString("th") || 0;

	// 计算当前窗口内部内容的真实高度
	function iframeHeight() {
		//console.log("iframeHeight222:");
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

	function setPageHeight(height) {
		//console.log("tabHeight ：" + height);
		$('#bodyContainer').css({
			'min-height' : height + 'px',
			'height' : height + 'px'
		});
	}

	/*setPageHeight(tabHeight);

	// 整合延时对象来初始化虚拟滚动条, 解决在初始化时窗口resize事件的影响
	$.when($.Deferred(function(def) {
		//console.log("def：" + def.state());
		// 初始化虚拟滚动条
		$('#bodyContainer').mCustomScrollbar({
			theme : "inset-dark",
			autoHideScrollbar : true,
			autoExpandScrollbar : true,
			horizontalScroll : false,
			scrollInertia : 80,
			advanced : {
				updateOnBrowserResize : true,
				updateOnContentResize : true,
				updateOnSelectorChange : false,
				autoExpandHorizontalScroll : true
			},
			mouseWheel : {
				enable : true,
				scrollAmount : 400
			},
			scrollButtons : {
				scrollAmount : 400
			},
			callbacks : {
				onInit : function() {
					//console.log("Scrollbars initialized");
					// 改变deferred对象的执行状态为：已完成
					def.resolve();
				},
				onOverflowY : function() {
					//console.log("Vertical scrolling required");
					// $(window).trigger("resize");
					$(window).trigger('resize.bootstrap-table');
				},
				onOverflowYNone : function() {
					//console.log("Vertical scrolling is not required");
					// $(window).trigger("resize");
					$(window).trigger('resize.bootstrap-table');

				}
			}
		});
		// 改变deferred对象的执行状态为：已完成
		def.resolve();
	}).promise()).done(function() {
		// 虚拟滚动条加载完成后，绑定窗口变化监听
		$(window).resize(function() {
			// 设置子页面最小高度
			setPageHeight(iframeHeight());
			//console.log("window resize");
			//$('#bodyContainer').mCustomScrollbar("update");

		});
	});*/

}(jQuery));
