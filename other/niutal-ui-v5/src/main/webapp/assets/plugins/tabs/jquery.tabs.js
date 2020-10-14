//@ sourceURL=jquery.tabs.js 

//bootStrap-addTabs
/**
 * Website: http://git.oschina.net/hbbcs/bootStrap-addTabs
 *
 * Version : 1.0
 *
 * Created by joe on 2016-2-4.
 */
;
(function($) {

	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};

	/*====================== Tabs CLASS DEFINITION ====================== */

	$.bootui.widget.Tabs = function(element, options) {
		options.beforeRender.call(this, element); //渲染前的函数回调
		try {
			this.initialize.call(this, element, options);
		} catch(e) {
			options.errorRender.call(this, e);
		}
		options.afterRender.call(this, element); /*渲染后的函数回调*/
	};

	$.bootui.widget.Tabs.prototype = {
		constructor: $.bootui.widget.Tabs,
		/*初始化组件参数*/
		initialize: function(element, options) {
			//定义变量
			var $this = this;
			var skin;

			function getUID(prefix) {
				do {
					prefix += ~~(Math.random() * 1000000);
				} while (document.getElementById(prefix));
				return prefix;
			};
			var tabs_uid = getUID(options.prefix);
			var iframeHeight = $.isFunction(options.iframeHeight) ? options.iframeHeight.call(this) : options.iframeHeight;
			// the base DOM structure needed to create a tabs
			var templates = {
				container: function() {
					var html = [];
					html.push('<div class="nav-container">');
					html.push('<!-- Nav tabs -->');
					html.push('<ul class="nav nav-tabs" role="tablist">');
					html.push('<li role="presentation" class="active">');
					html.push('<a href="#home" aria-controls="home" role="tab" data-toggle="tab">');
					html.push('<span class="fa fa-fw fa-home fa-sm" aria-hidden="true"></span>');
					html.push('主页');
					html.push('</a>');
					html.push('</li>');
					html.push('</ul>');
					html.push('<!-- Tab panes -->');
					html.push('<div class="tab-content">');
					html.push('<div role="tabpanel" class="tab-pane active" id="tab_home">');
					html.push('</div>');
					html.push('</div>');
					html.push('</div>');
					return html.join("");
				},
				tabtitle: function(opts) {
					var id = 'tab_' + opts.id;
					var title = $('<li>', {
						'role': 'presentation',
						'id': 'tab_' + id
					}).append(
						$('<a>', {
							'href': '#' + id,
							'aria-controls': id,
							'role': 'tab',
							'data-toggle': 'tab',
							'aria-url':opts.url,
							'class':'button button--ujarak button--border-thin button--text-thick'
						}).append( opts.icon ? $('<i>',{
								'class': opts.icon	
							}) : ""
						).append(
							$('<span>',{
								
							}).html(opts.title)
						)
					);
					//是否允许关闭
					if(options.closeable) {
//						title.find('a').append(
//							$('<div>', {
//									'class': 'close-tab-content',
//									'style': 'display:none'
//								})
//						).append(
//							$('<i>', {
//								'class': 'close-tab glyphicon glyphicon-remove',
//								'style': 'display:none'
//							})
//						);
						var html="";
						html +='<div class="close-tab-content">';
//							html +='<div style="position:relative;">';
//								html +='<i class="close-tab glyphicon glyphicon-remove">';
//								html +='</i>';
//							html +='</div>';
						html +='</div>'
						title.find('a').after(html);
					}

					return title;
				},
				tabpanel: function(opts) {
					var id = 'tab_' + opts.id;
					var content = $('<div>', {
						'class': 'tab-pane',
						'id': id,
						'role': 'tabpanel'
					});

					//是否指定TAB内容
					if(opts.content) {
						content.append(opts.content);
					} else if(options.iframeUse && !opts.ajax) { //没有内容，使用IFRAME打开链接
						
						content.append(
							//newURL为布局参数处理逻辑，请勿删除
							'<iframe id="iframe_' + id + '" class="iframeClass embed-responsive-item" height="' + iframeHeight + '" src="' + newURL(opts, true) + '" style="height:'+ iframeHeight +'px" />'
						);
					} else {
						$.get(opts.url, function(data) {
							content.append(data);
						});
					}

					return content;
				},
				tabdrop: function() {
					var html = [];
					html.push('<li class="dropdown pull-right tabdrop no-sort" id="tabs_dropdown">');
					html.push('<a href="javascript:void(0);" data-toggle="dropdown" class="dropdown-toggle">');
					html.push('<i class="glyphicon glyphicon-align-justify"></i>');
					html.push('<b class="caret"></b>');
					html.push('</a>');
					html.push('<ul class="dropdown-menu"></ul>');
					html.push('</li>');
					return html.join("");
					//创建下拉标签
					var dropdown = $('<li>', {
						'class': 'dropdown pull-right hide tabdrop'
					}).append(
						$('<a>', {
							'class': 'dropdown-toggle',
							'data-toggle': 'dropdown',
							'href': 'javascript:void(0);'
						}).append(
							$('<i>', {
								'class': "glyphicon glyphicon-align-justify"
							})
						).append(
							$('<b>', {
								'class': 'caret'
							})
						)
					).append(
						$('<ul>', {
							'class': "dropdown-menu"
						})
					)
				},
				// changePageSize: function(opts) {
				// 	var html = [];
				// 	html.push('<div class="change-page-size">');
				// 	html.push('<i class="glyphicon glyphicon glyphicon-resize-full"></i>');
				// 	html.push('</div>');
				// 	return html.join('');
				// },
				shrinkPageSize: function() {
					var html = [];
					html.push('<div class="shrink-title">');
					html.push('<span class="title"></span>');
					html.push('<i class="glyphicon glyphicon-resize-small"></i>');
					html.push('</div>');
					return html.join('');
				}
			}
			
			$(options.monitor).off('click', '[data-addtab]').on('click', '[data-addtab]', function() {
				$this.builTab({
					id: $(this).data('addtab'),
					title: $(this).attr('title') ? $(this).attr('title') : $(this).html(),
					content: options.content ? options.content : $(this).attr('content'),
					url: $(this).data('src'),
					ajax: $(this).attr('ajax') ? true : false,
					tablayout: $(this).data('tab-layout'),
					funclayout: $(this).data('blank-layout'),
					data: $(this).data('request') || {},
					icon: $(this).data('icon')
				});
			});

			if (options.contextmenu) {
		        //obj上禁用右键菜单
				$(element).on('contextmenu', 'li[role=presentation]', function (event) {
		           var id = $(this).children('a').attr('aria-controls');
		            
		           if(id && id != 'home'){
		        	   $this.pop(id, $(this),event);		        	   
		           }
		           
		           return false;
		       });

		        //刷新页面
				$(element).on('click', 'ul.rightMenu a[data-right=refresh]', function () {
		            var id = $(this).parent('ul').attr("aria-controls").substring(4);
		            var url = $(this).parent('ul').attr('aria-url');
		            $this.builTab({'id': id, 'url': url, 'refresh': true});
		            $('#popMenu').fadeOut();
		        });

		        //关闭自身
				$(element).on('click', 'ul.rightMenu a[data-right=remove]', function () {
		            var id = $(this).parent("ul").attr("aria-controls");
		            if (id.substring(0, 4) != 'tab_') return;
		            $this.close(id);
		            $this.drop();
		            $('#popMenu').fadeOut();
		        });

		        //关闭其他
				$(element).on('click', 'ul.rightMenu a[data-right=remove-circle]', function () {
		            var tab_id = $(this).parent('ul').attr("aria-controls");
		            $(element).find('ul.nav').find('li').each(function () {
		                var id = $(this).attr('id');
		                if (id && id != 'tab_' + tab_id) {
		                	$this.close($(this).children('a').attr('aria-controls'));
		                }
		            });
		            $this.drop();
		            $('#popMenu').fadeOut();
		        });

		        //关闭左侧
				$(element).on('click', 'ul.rightMenu a[data-right=remove-left]', function () {
		            var tab_id = $(this).parent('ul').attr("aria-controls");
		            $('#tab_' + tab_id).prevUntil().each(function () {
		                var id = $(this).attr('id');
		                if (id && id != 'tab_' + tab_id) {
		                	$this.close($(this).children('a').attr('aria-controls'));
		                }
		            });
		            $this.drop();
		            $('#popMenu').fadeOut();
		        });
				
		        //关闭右侧
				$(element).on('click', 'ul.rightMenu a[data-right=remove-right]', function () {
		            var tab_id = $(this).parent('ul').attr("aria-controls");
		            $('#tab_' + tab_id).nextUntil().each(function () {
		                var id = $(this).attr('id');
		                if (id && id != 'tab_' + tab_id) {
		                	$this.close($(this).children('a').attr('aria-controls'));
		                }
		            });
		            $this.drop();
		            $('#popMenu').fadeOut();
		        });
		    }
			

			$(element).off('click', '.close-tab-content').on('click', '.close-tab-content', function() {
				var id = $(this).prev("a").attr("aria-controls");
				$this.close(id);
				//$('body').prop('scrollTop','0');
			}).off('mouseover', 'li:not(' + options.selector + ' li.dropdown)').on('mouseover', 'li:not(' + options.selector + ' li.dropdown)', function() {
				$(this).find('.close-tab').show();
			}).off('mouseleave', 'li').on('mouseleave', 'li', function() {
				$(this).find('.close-tab').hide();
			}).off('click', '.tabdrop li a').on('click', '.tabdrop li a', function() {
				//折叠菜单点击位置换到最前
				var $prev = $('.tabdrop').prev();
				$('.tabdrop').find('.dropdown-menu').append($prev);
				$('#tab_tab_home').after($(this).closest('li'));
			});
			
			//放大页面至全屏
			// $(document).off('click', '.change-page-size').on('click', '.change-page-size', function() {
			// 	$('body').addClass('full-page');
			// 	var text=$('li[role="presentation"].active').text();
			// 	var title = $((templates.shrinkPageSize()));
			// 	title.find('.title').text(text);
			// 	$('body').find('.zf-global-bg').prepend(title);

			// 	//添加esc退出
			// 	$(document).unbind("keydown", "esc").bind("keydown", "esc", function(ev) {
			// 		$('body').removeClass('full-page');
			// 		$('.shrink-title').remove();
			// 		return false;
			// 	})
				
			// }).off('click', '.shrink-title i').on('click', '.shrink-title i', function() {

			// 	$('body').removeClass('full-page');
			// 	$('.shrink-title').remove();
				
			// }).off('click','#tab_tab_home').on('click','#tab_tab_home',function(){
				
			// 	$('.tab-pane').removeClass('active');
			// 	$('#tab_home').addClass('active');
			// });


			$(document).off('click','#tab_tab_home').on('click','#tab_tab_home',function(){
				
				$('.tab-pane').removeClass('active');
				$('#tab_home').addClass('active');
			})

			$(window).resize(function() {
				var paneHeight = $(window).height() - $('#tabs').outerHeight(true) - $('#footer').outerHeight(true) - $('#navbar').outerHeight(true);
				 
				iframeHeight=paneHeight;
				

				$('#tab-general .tab-pane').css('height',paneHeight+'px');

				var iframes=document.getElementsByTagName("iframe");
				
				for(var i=0;i<iframes.length;i++)
				{
					$('#'+iframes[i].id).height(iframeHeight);
					$('#'+iframes[i].id).css({'min-height':'300px'});
				}
				
				$this.drop();
			});

			var container = $(element).find("div.nav-container");
			if(container.size() == 0) {
				container = $(templates.container());
				$(element).empty().append(container);
			}
			$(container).attr("id", tabs_uid);

			//newURL为布局参数处理逻辑，请勿删除
			function newURL(opts, isTab) {
				var url = opts.url;
				var parmas = [];				
				$.each($.extend(opts.data || {}, {
					// 添加默认请求参数
					"layout": (isTab ? opts.tablayout : (opts.funclayout || "func-layout"))
				}), function(key, val) {
					if($.isArray(val)) {
						$.each(val, function(i, item) {
							parmas.push(key + "=" + val);
						});
					} else if($.isFunction(val)) {
						parmas.push(key + "=" + val.call(this));
					} else {
						parmas.push(key + "=" + val);
					}
				});
				parmas.push("_t=" + jQuery.now());
				parmas.push("skin=" + skin);

				return url.indexOf("?")>-1 ? (url + "&" + parmas.join("&")) : (url + "?" + parmas.join("&"))
			}
			//=======================绑定事件===========================================

			/*添加需要暴露给开发者的函数*/
			$.extend(true, $this, {
				destroy: function() {
					$(element).off('.' + options.prefix).removeData('bootui.tabs');
					$("#" + tabs_uid).remove();
				},
				builTab: function(opts) {
					var id = 'tab_' + opts.id;
					var url = opts.url;

					$('li[role="presentation"].active').removeClass('active');
					$('div[role="tabpanel"].active').removeClass('active');
					
					if (!$("#" + id).length) {
						
						//创建新TAB的title
						var title = $(templates.tabtitle(opts));
						
						title.data("opts", opts);
	
						title.dblclick(function() {
							var opts = $(this).data("opts");
							//newURL为布局参数处理逻辑，请勿删除
							window.open(newURL(opts,false));
							//window.open(url);
						});
						
						var str=title.find('a').attr('aria-controls');
						var _id=str.slice(4);
						addOpenedMark();
						
						title.click(function(){
							addOpenedMark();
						});
						
						function addOpenedMark(){
							$('.zf-left-nav-panel').find('a').each(function(){
								var addtab=$(this).attr('data-addtab');
								if(addtab==_id){
									$('.current-root-mark').removeClass('current-root-mark');
									$('.current-mark').removeClass('current-mark');
									$(this).closest('.left-nav-root').find('>a').addClass('current-root-mark');
									$(this).addClass('current-mark');
									$('.current-mark').closest('ul').parent('li').addClass('active');
								}
							});
						}
						
						// //创建tabs时创建放大页面按钮
						// var change = $((templates.changePageSize(opts)));
						// if($('body').find('.change-page-size').size() == 0) {
						// 	$('.tabs-below').append(change);
						// }
	
						//创建新TAB的内容
						var content = $(templates.tabpanel(opts));
						
						
						//加入TABS
						$(element).find('.nav-tabs').find('#tab_tab_home').after(title);
						$('body').find("#tab-general>.tab-content").prepend(content);
						$('.nav-tabs').show();
						//激活TAB
						$("#tab_" + id).addClass('active');
						$("#" + id).addClass("active");
						$this.drop();
					}else if (!opts.refresh) {
			            $('#tab_' + id).addClass('active');
			            $('#' + id).addClass('active');
//			            $(element).drop();
						//判断如果在折叠部分则显示到最前
						if($('#tab_' + id).closest('ul').hasClass('dropdown-menu')){
							var $prev = $('.tabdrop').prev();
							$('.tabdrop').find('.dropdown-menu').append($prev);
							$('#tab_tab_home').after($('#tab_' + id).closest('li'));
						}
			            return;
			        } else {
			        	//TODO 刷新标签页
			            var content = $('#' + id);
			            content.html('');
			        }
					

					if(options.sortable && $.fn.sortable) {
						$('ul[role="tablist"]').sortable({
							axis: 'x',
							cancel: '.no-sort',
							distance: 20,
							items :"> li:not('#tab_tab_home,#tabs_dropdown')"
						});
						$('ul[role="tablist"]').disableSelection();
					}

					$('.zf-home-page').hide();
					$('#tab-general').show();
				},
				pop: function (id, e) {
			        $('body').find('#popMenu').remove();
			        var popHtml = $('<ul>', {
			            'aria-controls': id,
			            'class': 'rightMenu list-group',
			            id: 'popMenu',
			            'aria-url': e.find('>a').attr('aria-url'),
			        }).css('width','150px').append(
			        	//暂时隐藏刷新本标签页
			            //'<a href="javascript:void(0);" class="list-group-item" data-right="refresh"><i class="glyphicon glyphicon-refresh"></i> 刷新此标签</a>' +
			            '<a href="javascript:void(0);" class="list-group-item" data-right="remove"><i class="glyphicon glyphicon-remove"></i> 关闭此标签</a>' +
			            '<a href="javascript:void(0);" class="list-group-item" data-right="remove-circle"><i class="glyphicon glyphicon-remove-circle"></i> 关闭其他标签</a>' +
			            '<a href="javascript:void(0);" class="list-group-item" data-right="remove-left"><i class="glyphicon glyphicon-chevron-left"></i> 关闭左侧标签</a>' +
			            '<a href="javascript:void(0);" class="list-group-item" data-right="remove-right"><i class="glyphicon glyphicon-chevron-right"></i> 关闭右侧标签</a>'
			        );
			        //上下文菜单定位
			        var left = event.pageX - 10;
			        var top = event.pageY-10;
				    if (top + popHtml.height() >= $(window).height()) {
				        top -= popHtml.height();
				    }
				    if (left + popHtml.width() >= $(window).width()) {
				        left -= popHtml.width();
				    }
				    popHtml.css({zIndex:1000001, left:left, top:top,position:'absolute'});
			        
			        popHtml.appendTo($(element)).fadeIn();
			        popHtml.mouseleave(function () {
			        	popHtml.hide();
			        });
			    },
				close: function(id) {
					//如果关闭的是当前激活的TAB，激活他的前一个TAB
					if($(element).find("li.active").attr('id') == "tab_" + id) {

						$("#tab_" + id).prev().addClass('active');
						$("#" + id).prev().addClass('active');
						
//						if($('#tab_tab_home').parent().find('>li.active').size()==0){
//							$('#tab_tab_home').addClass('active');
//							$('#tab_home').addClass('active');
//						}
					}
					
					$('.left-nav-root').removeClass('active');
					
					if($('#tab_tab_home').hasClass('active')){
						$('#tab_home').addClass('active');
					}
					
					//iframe删除逻辑代码，解决ie内存泄漏问题
					var el = document.getElementById("iframe_" + id);
					if(el) {
						var iframe = el.contentWindow;
						el.src = 'about:blank';
						try {
							iframe.document.write('');
							iframe.document.clear();
						} catch(e) {};
						try {

							//以上可以清除大部分的内存和文档节点记录数了
							//最后删除掉这个 iframe 
							document.body.removeChild(el);
						} catch(e) {

						};
					}
					//关闭TAB
					$("#tab_" + id).remove();
					$("#" + id).remove();
					$this.drop();
					options.callback();
				},
				closeAll: function() {
					$.each($(element).find('li[id]'), function() {
						var id = $(this).children('a').attr('aria-controls');
						$("#tab_" + id).remove();
						$("#" + id).remove();
					});
					$(element).find('li[role = "presentation"]').first().addClass('active');
					var firstID = $(element).find('li[role = "presentation"]').first().children('a').attr('aria-controls');
					$('#' + firstID).addClass('active');
					$this.drop();
				},
				drop: function() {
					var tabs = $(element).find('.nav-tabs');

					//创建下拉标签
					var dropdown = $(templates.tabdrop());
					//检测是否已增加
					if(tabs.find('.tabdrop').size() == 0) {
						dropdown.appendTo(tabs);
					} else {
						dropdown = tabs.find('.tabdrop');
					}

					var collection = 0;
					//检查超过一行的标签页
					tabs.append(dropdown.find('li')).find('>li').not('.tabdrop').each(function () {
		                if (this.offsetTop > 0 || tabs.width() - $(this).position().left - $(this).width() < 130 && tabs.find('>li').size() > 2) {
		                    dropdown.find('ul').prepend($(this));
		                    collection++;
		                }
		            });
					
					//如果有超出的，显示下拉标签
					if(collection > 0) {
						dropdown.removeClass('hide');
						if(dropdown.find('.active').length == 1) {
							dropdown.addClass('active');
						} else {
							dropdown.removeClass('active');
						}
					} else {
						dropdown.addClass('hide');
					}

					$('#tabs_dropdown').outerClick(function() {
						$(this).removeClass('open');
					});
				}
			});

		},
		setDefaults: function(settings) {
			$.extend($.fn.tabs.defaults, settings);
		},
		getDefaults: function(settings) {
			return $.fn.tabs.defaults;
		}
	}

	/* Tabs PLUGIN DEFINITION  */

	$.fn.tabs = function(option) {
		//处理后的参数
		var args = $.grep(arguments || [], function(n, i) {
			return i >= 1;
		});
		var selector = $(this).selector || options.selector;
		return this.each(function() {
			var $this = $(this);
			var data = $this.data('bootui.tabs');
			if(!data && option == 'destroy') {
				return;
			}
			if(!data) {
				var options = $.extend(true, {
					"selector": selector
				}, $.fn.tabs.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.tabs', (data = new $.bootui.widget.Tabs(this, options)));
			}
			if(typeof option == 'string') {
				//调用函数
				data[option].apply(data, [].concat(args || []));
			}
		});
	};

	$.fn.tabs.defaults = {
		/*版本号*/
		version: '1.0.0',
		prefix: "tabs",
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop,
		/*其他参数*/
		content: '', //直接指定所有页面TABS内容
		closeable: true, //是否可以关闭
		sortable: true, //标题是否可拖拽
		monitor: 'body', //监视的区域
		obj: $(this),
		iframeUse: true, //使用iframe还是ajax
		contextmenu: false,//是否使用右键菜单
		//固定TAB中IFRAME高度,根据需要自己修改
		iframeHeight: function() {
			return $(window).height()-91;
		},
		method: 'init',
		//关闭后回调函数
		callback: $.noop,
		//detect if you are on a touch device easily.
		clickEventType: ((document.ontouchstart !== null) ? 'click' : 'touchstart')
	};

	$.fn.tabs.Constructor = $.bootui.widget.Tabs;

}(jQuery));