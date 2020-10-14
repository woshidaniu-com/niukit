/**
 * @discretion	: default messages for the jQuery SiftBox plugin.
 * @author    	: wandalong 
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/uiwidget.siftbox-1.0.0.css
 				  3.引用效果的具体js代码文件 uiwidget.siftbox-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").siftbox({
							afterRender : function(){
								//这个方法是初始化后的回调函数，在需要做一些事情的时候重写即可
							
							}
						});
						
					});
					</script>
 */
jQuery(function($) {
	
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	$.bootui.widget.ListNav = function(selector,options){
		this.initialize.call(this,selector,options);	//初始化组件参数
	};
	
	$.bootui.widget.ListNav.prototype = {
		/*组件参数*/
		options: {
			version:'1.0.0',/*版本号*/
			/*回调函数*/
			/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
			beforeRender: $.noop,
			/*组件渲染出错后的回调函数*/
			errorRender: $.noop,
			/*组件渲染完成后的回调函数*/
			afterRender: $.noop, 
			/*加载远程数据前的回调函数：返回true则继续调用远程数据查询*/
			beforeRequest:function(letter){return true; },
			/*数据加载完后的回调函数*/
			afterRequest:$.noop,
			/*字母被点击的回调函数*/
			onLetterClick: $.noop,
			/*元素项被选择的回调函数*/
			onSelectItem: $.noop,
			/*数据请求参数*/
			url:"",
			postData:{},
			/*JSON数据处理参数*/
			mapper:null,//{"pinyin":"pinyin","key":,"text":,}
			formatter:null,
			/*组件参数*/
			index:"listnav",
			/*是否显示【全部】选项*/
	        includeAll: true,
	        allText:'全部',
	        /*是否显示【0-9】选项*/
	        includeNums: true,
	        /*是否显示【其他】选项*/
	        incudeOther: false,
	        otherText:"其他",
	        /*是否显示【已选】选项*/
	        incudeMatch: true,
	        matchText: "已选",
	        /*默认的选项*/
	        initLetter: 'all',
		    filterSelector: '.selector-name',//用于排序的引用
	        noMatchText: '没有数据...',
	        showCounts: true,
	        cookieName: null
		},
		/*初始化组件参数*/
		initialize : function(selector,_options) {
			var $this = this;
			
			if($.founded($this.options.mapper)){
				mapper.pinyin = mapper.pinyin||"pinyin";
			}
			/*初始化默认行为参数*/
			$.extend(true,$this.options,_options||{},{
				letters : ['_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-','matched'],
			    firstClick : false,
			    items:[],
		        //detect if you are on a touch device easily.
		        clickEventType:((document.ontouchstart!==null)?'click':'touchstart'),
		        getWidgetUI:function(){
					var context = new Array();
					context.push('<div class="ln-letters"><ul>');
	                for (var i = 1; i < $this.options.letters.length; i++) {
	                    if (i === 1) {
	                    	context.push('<li class="all" href="#">'+ $this.options.allText + '</li><li class="_" href="#">0-9</li>');
	                    }
	                    var text = (($this.options.letters[i] === '-') ? $this.options.otherText : (($this.options.letters[i] === 'matched') ? $this.options.matchText : $this.options.letters[i].toUpperCase()) );
	                    context.push('<li class="' + $this.options.letters[i] + '" href="#">' + text + '</li>');
	                }
	                context.push('</ul></div>' + (($this.options.showCounts) ? '<div class="ln-letter-count listNavHide">0</div>' : ''));
	                // Remove inline styles, replace with css class
	                // Element will be repositioned when made visible
					return context.join("");
				},
				//======================添加样式============================================
                // Add the appropriate letter class to the current element
				addLetterClass:function(firstChar, $el, isPrefix) {
                    if ( /\W/.test(firstChar) ) {
                        firstChar = '-'; // not A-Z, a-z or 0-9, so considered "other"
                    }
                    if ( !isNaN(firstChar) ) {
                        firstChar = '_'; // use '_' if the first char is a number
                    }
                    $el.addClass('ln-' + firstChar);
                    if ( $this.counts[firstChar] === undefined ) {
                    	$this.counts[firstChar] = 0;
                    }
                    $this.counts[firstChar]+=1;
                    if (!isPrefix) {
                    	$this.allCount+=1;
                    }
                },
                getLetterCount:function(el) {
                    if ($(el).hasClass('all')) {
                        return $this.allCount;
                    } else {
                        var count = $this.counts[$(el).attr('class').split(' ')[0]];
                        return (count !== undefined) ? count : 0; // some letters may not have a count in the hash
                    }
                },
                // adds a class to each LI that has text content inside of it (ie, inside an <a>, a <div>, nested DOM nodes, etc)
                changeClasses:function() {
                	$this.counts = {};
                	$this.allCount = 0;
                    var str, spl, $this_item,
                        firstChar = '',
                        hasFilterSelector = $.founded($this.options.filterSelector);
                    // Iterate over the list and set a class on each one and use that to filter by
                    $this.$list.each(function (i,item) {
                    	$this_item = $(item);
                        // I'm assuming you didn't choose a filterSelector, hopefully saving some cycles
                        if ( !hasFilterSelector ) {
                            //Grab the first text content of the LI, we'll use this to filter by
                            str = $.trim($this_item.text()).toLowerCase();
                        } else {
                            // You set a filterSelector so lets find it and use that to search by instead
                            str = $.trim($this_item.find($this.options.filterSelector).text()).toLowerCase();
                        }
                        
                        // This will run only if there is something to filter by, skipping over images and non-filterable content.
                        if (str !== '') {
                            // Find the first letter in the LI
                            firstChar = str.charAt(0);
                            // Doesn't send true to function, which will ++ the All count on prefixed items
                            $this.options.addLetterClass.call($this,firstChar, $this_item);
                        }
                    });
                    $this.counts["matched"] = $this.options.items.length;
                    $.each($this.options.letters,function(i,letter){
                    	var count = $this.counts[letter];
                    	if ( !($.founded(count)) || ($.founded(count) && Number(count) == 0) ) {
                            $('.' + $this.options.letters[i], $this.$letters).addClass('ln-disabled');
                        }else{
                        	 $('.' + $this.options.letters[i], $this.$letters).removeClass('ln-disabled');
                        }
                    });
                },
                changeMatchedClasses:function() {
                	var count = $this.options.items.length;
                    $this.counts["matched"] = count;
                	if ( Number(count) == 0 ) {
                		$('li.matched', $this.$letters).addClass('ln-disabled');
                    }else{
                    	$('li.matched', $this.$letters).removeClass('ln-disabled');
                    }
                },
                /*判断对象数组$this.options.items中是否存在itemObj对象*/
    			hasItem:function(itemObj){
    				var ishave = false;
    				$.each($this.options.items||[],function(i,item){
    					if(item.index==itemObj.index){
    						ishave = true;
    						return false;
    					}else{
    						ishave = false;
    					}
    				});
    				return ishave;
    			},
    			 /*获得$this.options.items中的元素*/
				getItem:function(index){
    				var itemObj = null;
					$.each($this.options.items||[],function(i,item){
						if(index==item.index){
							itemObj = item;
							return false;
						}
					});
					return itemObj;
				},
                /*移除$this.options.items中的元素*/
				removeItem:function(rowObj){
					$.each($this.options.items||[],function(i,itemObj){
						if(rowObj.index==itemObj.index){
							$this.options.items.splice(i, 1);
							$this.options.changeMatchedClasses();
							return false;
						}
					});
				},
				/*向对象$this.options.items数组添加一个对象*/
				addItem:function(itemObj){
					//循环当前itemObject的items数组判断是否添加的元素已经存在
					if(!$this.options.hasItem(itemObj)){
						$this.options.items.push(itemObj);
						$this.options.changeMatchedClasses();
					}
				},
				/*构建一个内置元素对象*/
				buildItem:function(itemObj,mapper){
					var indexStr = ($this.options.index||"")+"_"+itemObj[mapper.key];
					var pinyin = itemObj[mapper.pinyin];
					var key = itemObj[mapper.key];
					var text = itemObj[mapper.text];
					var checked = itemObj.checked || false;
					return {"index":indexStr,"pinyin":pinyin,"checked":checked,"key":key,"text":text};
				},
				bindItemEvent:function(li_item,rowObj,type){
					//绑定选择取消文字显示状态
					var isInputClick = false;
					//查找所有的checkbox
					$(li_item).unbind($this.options.clickEventType).bind($this.options.clickEventType, function (e) {
						if(!isInputClick){
							var checkbox = $(this).find(":checkbox:eq(0)")[0];
				 			if(!checkbox.checked){
								checkbox.checked  = true;
								if(type==1){
									$(this).find("span").addClass("highlight");
									$(li_item).addClass("checked");
									//添加选中明细
									$this.options.addItem(rowObj);
								}
							}else{
								checkbox.checked  = false;
								$(this).find("span").removeClass("highlight");
								$(li_item).removeClass("checked");
								//移除选中明细
								$this.options.removeItem(rowObj);
								if(type==1){
									$($this.container).find("ul.listmatched li").filter("li[index="+rowObj.index+"]").remove();
								}else if(type==2){
									var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
									$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
									$(list_item).find(".highlight").removeClass("highlight");
									$(li_item).hide("slow").remove();
								}
							}
							//调用样式和数量处理行数
			                $this.options.changeClasses();
			                $this.options.onSelectItem.call($this,rowObj,checkbox.checked);
						}
						isInputClick = false;
					});
					
					//查找所有的checkbox
					$(li_item).find(":checkbox:eq(0)").unbind($this.options.clickEventType).bind($this.options.clickEventType, function (e) {
						isInputClick = true;
						var checked = $(this)[0].checked;
						//已经选择：
						if(checked){
							if(type==1){
								$(this).next("span").addClass("highlight");
								$(li_item).addClass("checked");
								//添加选中明细
								$this.options.addItem(rowObj);
							}
						}else{
							$(this).next("span").removeClass("highlight");
							$(li_item).removeClass("checked");
							//移除选中明细
							$this.options.removeItem(rowObj);
							if(type==1){
								$($this.container).find("ul.listmatched li").filter("li[index="+rowObj.index+"]").remove();
							}else if(type==2){
								var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
								$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
								$(list_item).find(".highlight").removeClass("highlight");
								$(li_item).hide("slow").remove();
							}
						}
						//调用样式和数量处理行数
		                $this.options.changeClasses();
		                $this.options.onSelectItem.call($this,rowObj,checked);
					});
				},
				buildItemHtml:function(i,settings,itemObj){
					//如果formatter属性存在，且是函数，则获得此func,否则设置默认函数
					var formatter = ($.founded(settings.formatter)&&$.isFunction(settings.formatter))?settings.formatter:function(index,rowObj,mapper){
						var pinyin = rowObj.pinyin,
							key = rowObj.key,
							text = rowObj.text;
						var slectedItem = $this.options.getItem(rowObj.index);
						
						var html = [];
						//是否选中str
				    	var checkedStr = "";
				    	if(rowObj.checked==true||$.defined(slectedItem)){
				    		checkedStr = ' checked="checked" ';
				    	}
				    	html.push('<p class="selector-name">'+pinyin+'</p>');
						html.push('<table><tr>')
						html.push("<td>");
						html.push("<input name='"+mapper.key+"' value='"+key+"' text='"+text+"' type='checkbox' "+checkedStr+"/>");
						
						if(rowObj.checked==true||$.defined(slectedItem)){
							html.push('<span class="highlight">');
				    	}else{
				    		html.push("<span>");
				    	}
						html.push(text+"</span></td>");
						html.push('</tr></table>');
						return html.join("");
					};
					//组织html
					var item_html = [];
					item_html.push('<li index="'+itemObj.index+'">');
					item_html.push(formatter.call($this,i,itemObj,settings.mapper));
					item_html.push("</li>");
					return item_html.join("");
				},
                loadRemoteData:function(settings){
					if($.founded(settings.url)&&$.founded(settings.mapper)&&$this.options.beforeRequest.call($this,$this.letter)){
						jQuery.ajaxSetup({async:false});
						//远程获取数据
						var paramMap = $.extend({},settings.postData||{},{"letter_text":settings.mapper.text});
						$.getJSON(settings.url,paramMap, function(data){
							$($this.container).empty();
							$this.options.data = data;
							if($.founded(data)){
								//组织html
								var html = [];
								html.push('<ul class="listnav">');
								$.each(data,function(i,rowObj){
									var tmpObj = $this.options.buildItem(rowObj,settings.mapper);
									//组织每个元素html
									html.push($this.options.buildItemHtml.call(i,$this,settings,tmpObj));
								});
								html.push("</ul>");
								html.push('<ul class="listmatched"></ul>');
								//将拼装后的html放置在数据范围区域显示
								$($this.container).append(html.join(""));
							}
							$($this.container).append('<div class="ln-no-match listNavHide">' + $this.options.noMatchText + '</div>');
							if($($this.container).find("ul.listnav li").size()==0){
								$($this.container).find(".ln-no-match").show();
							}
							//绑定选择事件
							$($this.container).find("ul.listnav li").each(function(i,li_item){
								$(li_item).css(settings.mapper.style||{});
								var tmpObj = $this.options.buildItem(data[i],settings.mapper);
                 				$this.options.bindItemEvent.call($this,li_item,tmpObj,1);
                 			});
							//获得每个元素
                        	$this.$list = $($this.container).find("ul.listnav li");
	        				$this.$list.addClass("listNavHide").removeClass("listNavShow");
	        				//重新调用样式和数量处理行数:默认全部隐藏
	        				$this.options.changeClasses();
	        				$this.options.afterRequest.call($this,data,$this.letter)
						});
                        jQuery.ajaxSetup({async:true});
					}
				}
			});
			/*添加需要暴露给开发者的函数*/
			$.extend(true,$this,{
				/*获取组件options中的任意参数*/
				getParameter:function(key){return $this.options[key];},
				/*设置组件options中的任意参数*/
				setParameter:function(key,value){
					var obj = {};obj[key] = value;
					$.extend(true,$this.options,obj);
				},
				reload:function(paramMap){
					//扩展参数
					$this.reloadData = true;
					//删除原有参数
					$.each(paramMap, function (k, v) { 
			            delete $this.options.postData[k];
			        });
					$.extend($this.options.postData,paramMap||{});
					//触发当前字母的点击事件;进行重新加载数据
					$('.' + $this.letter , $this.$letters).trigger($this.options.clickEventType);
				},
				resetSelection:function(key){
					$.each($this.options.data,function(i,itemObj){
						var tmpObj = $this.options.buildItem(itemObj,$this.options.mapper);
						if($.founded(key)&&tmpObj.key==key){
							$this.options.removeItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
							$(list_item).find("span").removeClass("highlight");
							return false;
						}else{
							$this.options.removeItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
							$(list_item).find("span").removeClass("highlight");
						}
					});
				},
				setSelection:function(key){
					$.each($this.options.data,function(i,itemObj){
						var tmpObj = $this.options.buildItem(itemObj,$this.options.mapper);
						if(tmpObj.key==key){
							$this.options.addItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = true;
							$(list_item).find("span").addClass("highlight");
							return false;
						}
					});
				},
				getChecked:function(){
					return $this.options.items;
				}
			});
			
			$this.options.beforeRender.call($this);	//渲染前的函数回调
			try {
				$this.buildComponents.call($this,selector);	//组装组件html元素
				$this.renderComponents.call($this,selector);//渲染后组装好后的组件,添加css,绑定事件,填充内容
			} catch (e) {
				$this.options.errorRender.call($this,e);
			}
			$this.options.afterRender.call($this);	//渲染后的函数回调
		},
		/*构建组件UI*/
		buildComponents : function(selector) {
			var $this = this;
			$(selector).each(function(index,container){
				$this.container = $(container);
				//异常上次绑定的元素，并添加样式
				$(container).addClass("listnavBox");
				$(container).prev("div.listNav").remove();
				$(container).find(".ln-no-match").remove();
				
				//扩展每个基础setting
				var settings = $.extend(true,$this.options,$(container).getWidget());
				if ( $.cookie && ($this.options.cookieName !== null) ) {
                    var cookieLetter = $.cookie($this.options.cookieName);
                    if ( cookieLetter !== null ) {
                    	$this.options.initLetter = cookieLetter;
                    }
                }
				
				//======================添加组件元素============================================
				$(container).before('<div id="listNav-nav" class="listNav"/>');
				$(container).append('<div class="ln-no-match listNavHide">' + $this.options.noMatchText + '</div>');
				$this.$wrapper =  $(container).prev('.listNav');
	            /*将生成的html添加到页面*/
				$this.$wrapper.empty().append($this.options.getWidgetUI.call($this));
	            //取得当前元素前面的a-z区域
				$this.$letters = $('.ln-letters', $this.$wrapper).slice(0, 1);
				//取得当前元素显示总数的div
                if ($this.options.showCounts ) {
                	$this.$letterCount = $('.ln-letter-count', $this.$wrapper).slice(0, 1);
                }
                
                //====================处理组件元素===================================================
                // remove nav items we don't need
                if ( !$this.options.includeAll ) {
                    $('.all', $this.$letters).remove();
                }
                if ( !$this.options.includeNums ) {
                    $('._', $this.$letters).remove();
                }
                if ( !$this.options.includeOther ) {
                    $('.-', $this.$letters).remove();
                }
                if ( !$this.options.incudeMatch ) {
                    $('.matched', $this.$letters).remove();
                }
                $(':last', $this.$letters).addClass('ln-last');
                
                $(container).show();
              //返回false终止循环，保存只取选择器的第一个元素
				return false;
			});
		},
		/*渲染后组装好后的组件,添加css,绑定事件,填充内容*/
		renderComponents: function(selector) {
			var $this = this;
			$(selector).each(function(index,container){
				
				//扩展每个基础setting
				var settings = $.extend(true,$this.options,$(container).getWidget());
				
				//定义变量
				$this.loadAll = false;
				$this.prevLetter = '';
				$this.counts = {};
            	$this.allCount = 0;
				var isAll = true,firstClick = false;
				
                //======================添加数据元素============================================
                //程数据模式
                if($.founded(settings.url)&&$.founded(settings.mapper)){
					//初次加载，什么不做
                }else{
                	//本地数据模式
                	$this.$list = $(container).find("ul.listnav li");
	                //调用样式和数量处理行数
	                $this.options.changeClasses();
                }
                //=======================绑定事件===========================================
                
                // click handler for letters: shows/hides relevant LI's
                $('li', $this.$letters).unbind($this.options.clickEventType).bind($this.options.clickEventType, function (e) {
                	e.preventDefault(); 
                	
                	var $this_a = $(this),
                     	noMatches = $(container).find('.ln-no-match');
                 		$this.letter = $this_a.attr('class').split(' ')[0];
                 	//远程数据模式	
                 	if($.founded(settings.url)&&$.founded(settings.mapper)){
        				//未加载过全部数据
                    	if(($this.loadAll == false && $this.letter != 'matched' && $this.letter != '-')	|| $this.reloadData == true){
                    		
                    		//加载点击字符对应的数据；构建内容
                        	settings.postData["letter"] = $this.letter;
            				$this.options.loadRemoteData.call($this,settings);
            				
            				$this.reloadData = false;
                    	}
                	}
                 	//获得上次选中和本次选中的元素
                 	var prevList = $this.$list.filter('.ln-' + $this.prevLetter);
                 	var thisList = $this.$list.filter('.ln-' + $this.letter);
                 	var listnavList = $(container).find("ul.listnav");
                 	var matchedList	= $(container).find("ul.listmatched");
                 	
                 	
                	//=================相同处理逻辑区域=====================================
                    //判断是否点击新的字符
                    if ( $this.prevLetter !== $this.letter  ) {
                    	// Only to run this once for each click, won't double up if they clicked the same $this.letter
                    	// Won't hinder firstRun
                        $('li.ln-selected', $this.$letters).removeClass('ln-selected');
                        //隐藏所有元素
                        $this.$list.addClass("listNavHide").removeClass("listNavShow");
                        //删除选中区域的元素
                        $(matchedList).hide();
                        $(matchedList).empty();
                        
                        //所有【全部】选项
                        if ( $this.letter === 'all' ) {
                        	//设置加载过全部数据标识
                        	$this.loadAll = true;
                        	$this.$list.addClass("listNavShow").removeClass("listNavHide"); // Show ALL
                            noMatches.addClass("listNavHide").removeClass("listNavShow"); // Hide the list item for no matches
                            isAll = true; // set this to quickly check later
                            $(listnavList).show();
                        } else if ( $this.letter === 'matched' ) {
                        	//点击【已选】选项
                        	$(listnavList).hide();
                        	if($this.options.items.length==0){
            		    		noMatches.addClass("listNavShow").removeClass("listNavHide");
            		    	}else{
            		    		noMatches.addClass("listNavHide").removeClass("listNavShow");
            		    		//循环已选元素对象
                     			$.each($this.options.items||[],function(j,itemObj){
                     				itemObj.checked = true;
                 					//组织选中却不存在于当前条件的元素html
                 					var selectElement = $this.options.buildItemHtml.call(j,$this,settings,itemObj);
                 					$(selectElement).addClass("listNavShow").removeClass("listNavHide");
                 					$(matchedList).append(selectElement);
                     			});
                     			//绑定选择事件
                     			$($this.container).find("ul.listmatched li").each(function(i,li_item){
                     				$(li_item).css(settings.mapper.style||{});
                     				$this.options.bindItemEvent.call($this,li_item,$this.options.items[i],2);
                     			});
                     			//显示已选元素
                     			$(matchedList).show();
            		    	}
                        }else {
                        	//隐藏上一次匹配的元素
                            if ( isAll ) {
                            	// 如果上次点击的是全部
                                // since you clicked ALL last time:
                            	$this.$list.addClass("listNavHide").removeClass("listNavShow");
                                isAll = false;
                            } else if ($this.prevLetter !== '') {
                            	$this.$list.filter('.ln-' + $this.prevLetter).addClass("listNavHide").removeClass("listNavShow");
                            }
                            //显示本次匹配的元素
                            $this.$list.filter('.ln-' + $this.letter).addClass("listNavShow").removeClass("listNavHide");
                            var count = $this.options.getLetterCount(this);
                            if (count > 0) {
                                noMatches.addClass("listNavHide").removeClass("listNavShow"); // in case it's showing
                            } else {
                                noMatches.addClass("listNavShow").removeClass("listNavHide");
                            }
                            $(listnavList).show();
                        }
                        //设置上次点击字母
                        $this.prevLetter = $this.letter;
                        if ($.cookie && ($this.options.cookieName !== null)) {
                            $.cookie($this.options.cookieName, $this.letter, {
                                expires: 999
                            });
                        }
                        //添加当前字母选中效果
                        $this_a.addClass('ln-selected');
                        $this_a.blur();
                        // end if prevLetter !== $this.letter
                    }else{
                    	//所有【全部】选项
                        if ( $this.letter === 'all' ) {
                        	//设置加载过全部数据标识
                        	$this.loadAll = true;
                        	$this.$list.addClass("listNavShow").removeClass("listNavHide"); // Show ALL
                            noMatches.addClass("listNavHide").removeClass("listNavShow"); // Hide the list item for no matches
                            isAll = true; // set this to quickly check later
                        }else {
                        	//隐藏上一次匹配的元素
                            if ( isAll ) {
                            	// 如果上次点击的是全部
                                // since you clicked ALL last time:
                            	$this.$list.addClass("listNavHide").removeClass("listNavShow");
                                isAll = false;
                            } else if ($this.prevLetter !== '') {
                            	$this.$list.filter('.ln-' + $this.prevLetter).addClass("listNavHide").removeClass("listNavShow");
                            }
                            //显示本次匹配的元素
                            $this.$list.filter('.ln-' + $this.letter).addClass("listNavShow").removeClass("listNavHide");
                            var count = $this.options.getLetterCount(this);
                            if (count > 0) {
                                noMatches.addClass("listNavHide").removeClass("listNavShow"); // in case it's showing
                            } else {
                                noMatches.addClass("listNavShow").removeClass("listNavHide");
                            }
                        }
                    } 
                    
                    if ($.founded($this.options.onLetterClick)) {
                    	$this.options.onLetterClick.call($this,$this.letter);
                    }
                    firstClick = false; //return false;
                }); // end click()
                
                if ( $this.options.includeAll ) {
                	
                }
            	win_alert($this.options.initLetter);

                //=======================决定先显示的内容：触发事件===========================================
                // 如果有默认字母  initLetter ;先显示默认的字母
                if ( $this.options.initLetter !== '' ) {
                    firstClick = true;
                    // click the initLetter if there was one
                    $('.' + $this.options.initLetter.toLowerCase(), $this.$letters).slice(0, 1).trigger($this.options.clickEventType);
                } else {
                    // 如果有默认字母；但是设置了显示全部；则显示全部的元素
                    if ( $this.options.includeAll ) {
                        // 调用【全部】选项的click事件,实际上不真正的点击它
                        $('.all', $this.$letters).addClass('ln-selected');
                        //加载远程数据模式
                        if($.founded(settings.url)&&$.founded(settings.mapper)){
                        	//设置加载过全部数据标识
                        	$this.loadAll = true;
                         	//加载点击字符对应的数据；构建内容
                         	settings.postData["letter"] = "";
             				$this.options.loadRemoteData.call($this,settings);
                        }
                    } else {
                    	//未设置显示全部，则显示第一个有对应内容的字母
                        for ( var i = (($this.options.includeNums) ? 0 : 1); i < $this.options.letters.length; i+=1) {
                        	//远程数据模式	
                        	win_alert($this.options.letters[i]);
                         	if($.founded(settings.url)&&$.founded(settings.mapper)){
                         		firstClick = true;
                                $('.' + $this.options.letters[i], $this.$letters).slice(0, 1).trigger($this.options.clickEventType);
                                break;
                         	}else{
                         		 if ( $this.counts[$this.options.letters[i]] > 0 ) {
                                     firstClick = true;
                                     $('.' + $this.options.letters[i], $this.$letters).slice(0, 1).trigger($this.options.clickEventType);
                                     break;
                                 }
                         	}
                        }
                    }
                }
                //如果显示记录数
                if ($this.options.showCounts) {
                    // sets the top position of the count div in case something above it on the page has resized
                	$this.$wrapper.mouseover(function () {
                    	// we're going to need to subtract this from the top value of the wrapper to accomodate changes in font-size in CSS.
                        var letterCountHeight = $this.$letterCount.outerHeight();
                        $this.$letterCount.css({
                            top: $('li:first', $this.$wrapper).slice(0, 1).position().top - letterCountHeight
                            // we're going to grab the first anchor in the list
                            // We can no longer guarantee that a specific letter will be present
                        });
                    });
                    
                    //shows the count above the letter
                    $('.ln-letters li', $this.$wrapper).unbind("mouseover").mouseover(function (e) {
                    	var $thisEl = $(this);
                        var left = $(this).position().left,
                            width = ($(this).outerWidth()) + 'px',
                            letter = $thisEl.attr('class').split(' ')[0];
                        var count = $this.options.getLetterCount(this);
                    	$this.$letterCount.css({
                            left: left,
                            width: width
                        }).text(count).addClass("letterCountShow").removeClass("listNavHide"); // set left position and width of letter count, set count text and show it
                    }).unbind("mouseout").mouseout(function () { // mouseout for each letter: hide the count
                    	$this.$letterCount.addClass("listNavHide").removeClass("letterCountShow");
                    });
                }
               //返回false终止循环，保存只取选择器的第一个元素
               return false;
			});
		}
	};
	
	$.fn.extend({
		listnav:function(options){
			return new $.bootui.widget.ListNav($(this).selector, options);
		}
	});
	
});