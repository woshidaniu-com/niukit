$(function() {
	var skin=localStorage.lastname;
	if(skin !=undefined){
		$('.zf-global-bg').attr('class','').addClass('zf-global-bg '+skin+'');
	}
	
	//调用tips插件
	$('.zf-nav.top-nav [data-toggle="tooltip"]').tooltip();

	$(document).off('click', '#zf-sy-search').on('click', '#zf-sy-search', function() {

		//搜索框
		$(this).addClass('open');
	}).off('blur', '#zf-sy-search').on('blur', '#zf-sy-search', function() {

		$(this).removeClass('open');

	}).off('mouseenter', '.zf-logo-lg').on('mouseenter', '.zf-logo-lg', function() {

		$(this).addClass('animated fadeInDown');

	}).off('click', '.zf-shade').on('click', '.zf-shade', function() {
		
		$('.zf-shade').hide();

		$('body').attr('class', '');
		
		$(window).off('resize.bootstrap-table');

		if($('.zf-top-logo').hasClass('animated fadeInDown')) {
			$('.zf-top-logo').removeClass('animated fadeInDown');
		}

		if($('.zf-left-nav').hasClass('open')) {
			$('.zf-left-nav').removeClass('open');
		}

		if($('.zf-left-nav-panel').find('.active')) {
			$('.zf-left-nav-panel').find('.active').removeClass('active');
			var li=$('.current-mark').closest('ul').parent('li');
			li.addClass('active');
			if(li.hasClass('left-nav-root')){
				li.removeClass('active');
			}
			
		}
		
		if($('.zf-top-nav-content').find('.active')) {
			$('.zf-top-nav-content').find('.active').removeClass('active');
		}
		
		clearNav();

		$('.zf-character').popover('hide');
		$('.zf-user').popover('hide');
		
		//换肤
		var _skin=$('.zf-global-bg').attr('data-skin');
		$('body').addClass(_skin);

	}).off('click', '.top.nav-toggle').on('click', '.top.nav-toggle', function() {

		//头部导航开关
		if(!$('body').hasClass('top-header')) {
			$('body').addClass('top-header');
			$('.zf-shade').show();

			clearNav();

			setPosition();
			$(window).resize(function() {
				setPosition();
			});
			$('.top-nav-close').removeClass('hide');
		}
	}).off('click', '.zf-top-nav .zf-nav-multistep-list li').on('click', '.zf-top-nav .zf-nav-multistep-list li', function() {

		var next = $(this).next();
		next.siblings('ul').removeClass('active');
		if(next.is('ul')) {
			next.addClass('active');
		}
	}).off('mouseover', '.zf-top-nav li').on('mouseover', '.zf-top-nav li', function() {

		var thisWidth = $(this).width();

		$(window).resize(function() {
			thisWidth = $(this).width();
		});

		var $ul = $(this).closest('ul');
		$ul.find('>li:first').removeClass('hover');

		$(this).addClass('hover');

		//判断在此之前有几个ul
		var prevSize = 0;
		$(this).prevAll().each(function() {
			if($(this).is('ul')) {
				prevSize++;
			}
		});

		var $index = $(this).index();
		var $top = ($index - prevSize) * 38 + 15;
		if($ul.find('>.zf-top-nav-hover').size() == 0) {

			$ul.append('<div class="zf-top-nav-hover"></div>');
		}
		$ul.find('>.zf-top-nav-hover').css({
			'top': $top,
			'width': thisWidth,
			'left': '15px'
		});

	}).off('mouseleave', '.zf-top-nav li').on('mouseleave', '.zf-top-nav li', function() {

		//定位头部导航的hover

		$(this).removeClass('hover');

		$ul = $(this).closest('ul');
		var $hover = $ul.find('>.zf-top-nav-hover');

	}).off('click', '.zf-top-nav li').on('click', '.zf-top-nav li', function() {
		$(this).siblings('.current').removeClass('current');
		$(this).addClass('current');

		setPosition();

	}).off('click', '.top-header>.glyphicon-remove').on('click', '.top-header>.glyphicon-remove', function() {
		$('body').removeClass('top-header');
		$(this).remove();

		$('.zf-nav-item-list').find('.zf-top-nav-hover').remove();

		$('.zf-shade').hide();
		//		$('.zf-content').css('margin-top', '80px');

	}).off('click', '.zf-search').on('click', '.zf-search', function() {

		//搜索框
		$('body').addClass('top-search');
		$('.zf-search-block input').focus();
		$('.zf-shade').show();

	}).off('click', '.zf-search-block i').on('click', '.zf-search-block i', function() {

		//隐藏搜索框
		$('body').removeClass('top-search');
		$('.zf-shade').hide();

	}).off('click', '.zf-lately-use').on('click', '.zf-lately-use', function() {

		//最近使用
		if($('body').hasClass('left-nav')){
			$('body').removeClass('left-nav');
		}
		 $(this).siblings().popover('hide');
		
		$('body').addClass('top-lately');
		$('.zf-shade').show();
		$('#tab-general').show();
	}).off('click', '.zf-phone-toggle').on('click', '.zf-phone-toggle', function() {

		//手机端导航

		var phoneNav = $('.zf-nav.top-nav').height();
		$('body').toggleClass('top-nav');
		var nav = $('.zf-phone-nav');
		if(!nav.hasClass('open')) {
			nav.addClass('open');
//			nav.css({
//				'margin-top': phoneNav + 20
//			});
		} else {
			nav.removeClass('open');
//			nav.css({
//				'margin-top': -phoneNav
//			});
		}

	}).off('click touchend', '.zf-phone-nav a').on('click touchend', '.zf-phone-nav a', function() {

		var next = $(this).next();
		var closestLi = $(this).closest('li');
		var closestUl = $(this).closest('ul');
		if(next.is('ul')) {
			next.addClass('active');
			closestUl.addClass('zf-phone-nav-hide');
			closestLi.addClass('active');
		}
	}).off('click touchend', '.zf-phone-nav-back').on('click touchend', '.zf-phone-nav-back', function() {
		var closestUl = $(this).closest('ul');
		var closestUlul = closestUl.parent().parent();

		closestUlul.removeClass('zf-phone-nav-hide');
		closestUl.removeClass('active');
	}).off('click', '.zf-lately-use-block .title>i').on('click', '.zf-lately-use-block .title>i', function() {
		$('.zf-shade').hide();
		$('body').removeClass('top-lately');
	})
//	.off('click', '.zf-left-nav-title>li').on('click', '.zf-left-nav-title>li', function() {
//		var panel = $(this).attr('data-content');
//		if($.trim(panel).length > 0) {
//			if(!$('.' + panel + '').hasClass('active')) {
//				$('.zf-left-nav>div').removeClass('active');
//				$('.' + panel + '').addClass('active');
//				$('body').addClass('left-nav');
//				$('.zf-shade').show();
//				if(!$('.zf-left-nav').hasClass('open')) {
//					$('.zf-left-nav').addClass('open');
//				}
//			} else {
//				$('.zf-left-nav>div').removeClass('active');
//				$('body').removeClass('left-nav');
//				$('.zf-shade').hide();
//				$('.zf-left-nav').removeClass('open');
//			}
//		}
//
//	})
	.off('click', '.zf-left-nav li a').on('click', '.zf-left-nav li a', function() {

		//左侧导航
		if(! $('body').hasClass('left-nav')){
			$('body').addClass('left-nav');
			$(window).off('resize.bootstrap-table');
			$('.zf-left-nav').addClass('open');
			if(typeof($(this).attr("data-src"))!="undefined"){
				$('.current-root-mark').removeClass('current-root-mark');
			}
			$('.zf-shade').show();
		}
		$(this).closest('.zf-left-nav-ul').prev('.total-nav-title').addClass('active');
		
		var next = $(this).next();
		var closeLi = $(this).closest('li');
		if(next.is('ul')) {
			if(!closeLi.hasClass('active')) {
				closeLi.addClass('active');
			} else {
				closeLi.removeClass('active');
			}
		}
		
		$('.zf-left-nav').mCustomScrollbar('update');

		
	}).off('click','.total-nav-title').on('click','.total-nav-title',function(){
		
		
		if(! $('body').hasClass('left-nav')){
			$('body').addClass('left-nav');
			$('.zf-left-nav').addClass('open');
			if(typeof($(this).attr("data-src"))!="undefined"){
				$('.current-root-mark').removeClass('current-root-mark');
			}
			$('.zf-shade').show();
		}
		
		$('.total-nav-title').removeClass('active');
		$(this).addClass('active');
		
	}).off('click', '.mCSB_container li').on('click', '.mCSB_container li', function() {

		var $this = $(this);
		$('.zf-left-nav').mCustomScrollbar('scrollTo', $this);
		
	}).off('click', '.zf-skin-change-panel .item').on('click', '.zf-skin-change-panel .item', function() {
		
		//切换皮肤
		skin = $(this).find('>a').attr('data-skin-type');
		$('.zf-global-bg').attr('class','').addClass('zf-global-bg '+skin+'');
		$('.zf-global-bg').attr('data-skin',skin);
		localStorage.lastname= skin;
		
		try{
			$("iframe").contents().find("body").attr('class','').addClass(skin);
		}catch(e){
			
		}
		
	}).off('mouseleave', '.zf-nav.top-nav .popover').on('mouseleave', '.zf-nav.top-nav .popover', function() {
		
		setTimeout(function(){
			$('.zf-character').popover('hide');
			$('.zf-user').popover('hide');
			$('.zf-skin-change').popover('hide');
		},200);
		
	}).off('click','.top-nav .navbar-nav>li').on('click','.top-nav .navbar-nav>li',function(){
		
		if($('.left-nav-root').hasClass('active')){
			$('.left-nav-root').removeClass('active')
		}
	}).off('click','.mCSB_container li a[data-src]').on('click','.mCSB_container li a[data-src]',function(){
		
		var $ul=$(this).closest('ul');
		$ul.find('.current-mark').removeClass('current-mark');
		$(this).addClass('current-mark');
		
	}).off('mouseenter','.navbar-nav>li').on('mouseenter','.navbar-nav>li',function(){
		$(this).tooltip('show');
	}).off('click','.nav-tabs').on('click','.nav-tabs',function(){
		$('.left-nav-root').removeClass('active');
	});

	
	//打开页面时给data-skin赋值
	$('.zf-global-bg').attr('data-skin',skin);
	
	//弹出框
	$('.zf-user').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{ 
			show: 0, 
			hide: 100 
		},
		content: function() {
			var html = [];
			html.push($('.zf-user-module').html());
			return html.join("");
		}
	});
	
	
	

	$('.zf-character').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{ 
			show: 0, 
			hide: 100 
		},
		content: function() {
			var html = [];
			html.push($('.zf-character-module').html());
			return html.join("");
		}
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
        $(this).siblings().popover('hide');
    });
	
	$('.zf-skin-change').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{ 
			show: 0, 
			hide: 100 
		},
		content: function() {
			var html = [];
			html.push($('.zf-skin-change-module').html());
			return html.join("");
		}
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
        $(this).siblings().popover('hide');
    });
	
	$('.zf-user').on('shown.bs.popover', function() {
		$('.zf-shade').show();
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
        $(this).siblings().popover('hide');
    });

	$('.zf-character').on('shown.bs.popover', function() {
		$('.zf-shade').show();
	});

	//导航滚动条插件
	$('.zf-left-nav').mCustomScrollbar({
//		theme: "rounded-dots",
//		scrollButtons: {
//			enable: false,
//			scrollType: "continuous",
//			scrollSpeed: 20,
//			scrollAmount: 40
//		},
		advanced: {
			updateOnBrowserResize: true,
			updateOnContentResize: true,
			autoExpandHorizontalScroll: true
		}
	});
	
	$('#tab-general>.tab-content>.tab-pane').mCustomScrollbar({
		theme: "inset-dark"
	});


	$('#tabs').tabs({
		monitor: '.zf-nav-item-list,.zf-lately-use-block,.zf-left-nav-panel',
		contextmenu:false
	});
	
	setLeftNavHeight();
	function setLeftNavHeight() {
		$windowHeight = $(window).height();
		$('.zf-left-area').height($windowHeight);
	}

	//设置iframe区域最小高度
	setIframeHeight();
	var windowsHeight=$(window).height();
	function setIframeHeight(){
		var minHeight=$(window).height()-91;
		var _height=minHeight-15;
		$('#tab-general>.tab-content').css({
			'min-height':minHeight,
		});
		$('#tab-general>.tab-content>.tab-pane').css({
			'height':_height,
		});
	}
	
	$(window).resize(function() {
		setLeftNavHeight();
		setIframeHeight();
		$('.zf-top-nav-hover').remove();
		var windowsWidth=$(window).width();
		if(windowsWidth < 768){
			parent.location.reload();
		}
	});
	
	//给头部导航的每层加标识
	$('.zf-nav-item-list').attr('data-level', 'dqcj-1');
	var erji = $('.zf-nav-item-list').find('>ul');
	var sanji = erji.find('>ul');
	if(erji) {
		erji.each(function() {
			$(this).attr('data-level', 'dqcj-2');
		});
	}
	if(sanji) {
		sanji.each(function() {
			$(this).attr('data-level', 'dqcj-3');
		});
	}

	function clearNav() {
		var nav = $('.zf-nav-item-list');
		if(nav.find('.current')) {
			nav.find('.current').removeClass('current');
		}
		if(nav.find('.active')) {
			nav.find('.active').removeClass('ative');
		}
		if(nav.find('.zf-top-nav-hover')) {
			nav.find('.zf-top-nav-hover').remove();
		}

		var $first = $('.zf-nav-item-list>li:first');
		var $ul = $first.next('ul');
		var $second = $ul.find('>li:first');
		var $third = $second.next('ul').find('>li:first');
		$first.addClass('current');
		$second.addClass('current');
		$third.addClass('current');
		$ul.addClass('active');
		$second.next('ul').addClass('active');

		$(window).resize(function() {
			setPosition();
		});

		if($('body').find('.top-nav-close').size() == 0) {
			$('body').prepend('<i class="glyphicon glyphicon-remove top-nav-close hide"></i>');
		}

		var navheight = $('.zf-top-nav').outerHeight();
		//		$('.zf-content').css('margin-top', navheight);
	}

	function setPosition() {
		//头部导航定位
		var first = $('.zf-top-nav .zf-nav-item-list');
		var second = $('ul [data-level="dqcj-2"]');
		var third = $('ul [data-level="dqcj-3"]');
		var level = $('.zf-top-nav .zf-nav-multistep-list');

		second.css({
			'left': '0px'
		});

		third.css({
			'left': '0px'
		});

		var firstWidth = first.width();
		second.css({
			'left': firstWidth,
			'top': '0px',
			'width': firstWidth,
		});

		third.css({
			'left': firstWidth,
			'top': '0px',
			'width': firstWidth,
		});

		var h_max = 0;
		var h_first = first.outerHeight();
		first.find('ul.active').each(function() {
			var h = $(this).outerHeight();
			h_max = h > h_max ? h : h_max;
		});
		h_first = h_max > h_first ? h_max : h_first;
		first.css({
			'height':h_first
		});
		second.css({
			'height':h_first
		});
		third.css({
			'height':h_first
		});
		


	}
	setPhonenav();

	$(window).resize(function() {
		setPhonenav();
		setIframeHeight();
	});
	

	function setPhonenav() {

		//手机端导航
		if(windowsHeight<767){
			//$('.zf-shade').hide();
			//销毁tooltip事件
			$('.zf-nav.top-nav [data-toggle="tooltip"]').tooltip("destroy");
		}
		
		var phoneNav = $('.zf-nav.top-nav').height();
		$('.zf-phone-nav').css({
			//'margin-top': phoneNav
		});
	}
});