<form class="form-horizontal" onsubmit="return false;">
	<div class="panel panel-default advanced-query-fixed" id="${id}">
		<div class="panel-heading">
			<div class="form-group cond-bar cond-bar-selected mg margin-b0">
				<div class="col-sm-12 margin-t10">
					${inputPart.getHtml("default","${id}")}
				</div>
				<div class="col-sm-12">
					<ul class="tag-list tag-list-cond">
						<#list childList as child>
							<#assign data=child.getData() > 
							<li><a data-href="#collapse_${id}_${data.name}" class="transition-common">${data.text}<i class="caret"></i></a></li>
						</#list>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel-body rz-body" name="collapse_condition" role="tablist" aria-multiselectable="true" style="left: 0px; top: 128px;">
			<#list childList as child>
				${child.getHtml("default","${id}")}
			</#list>
		</div>
	</div>
</form>
<script type="text/javascript">
//bootstrap-hover-dropdown.js
(function(b,a,c){var d=b();b.fn.dropdownHover=function(e){d=d.add(this.parent());return this.each(function(){var k=b(this),j=k.parent(),i={delay:500,instantlyCloseOthers:true},h={delay:b(this).data("delay"),instantlyCloseOthers:b(this).data("close-others")},f=b.extend(true,{},i,e,h),g;j.hover(function(l){if(!j.hasClass("open")&&!k.is(l.target)){return true}if(f.instantlyCloseOthers===true){d.removeClass("open")}a.clearTimeout(g);j.addClass("open")},function(){g=a.setTimeout(function(){j.removeClass("open")},f.delay)});k.hover(function(){if(f.instantlyCloseOthers===true){d.removeClass("open")}a.clearTimeout(g);j.addClass("open")});j.find(".dropdown-submenu").each(function(){var m=b(this);var l;m.hover(function(){a.clearTimeout(l);m.children(".dropdown-menu").show();m.siblings().children(".dropdown-menu").hide()},function(){var n=m.children(".dropdown-menu");l=a.setTimeout(function(){n.hide()},f.delay)})})})};b(document).ready(function(){b('[data-hover="dropdown"]').dropdownHover()})})(jQuery,this);

//search_default.js
(function($){
	$.search = $.search || {
		version:'5.0',
		author:'Penghui.Qu',
		theme:'default'
	};
	
	$.fn.extend({
		searchInit:function(){
			var $panel = $(this);
			
			//设置输入框为tags input
			//绑定输入框回车事件
			var search_tags = $(":hidden[name=search_tags]",$panel);
			search_tags.data("search", []);
			search_tags.data("_index", 0);
			
			//解析item获得实际数据
			function parseItem(item){
			
				var blurType=$(":hidden[name=search_blurType]",$panel).val();
				var sqlType=$(":input[name=search_sqlType]",$panel).val();
				
				var blurText = "";
				
				if(sqlType == "8" || sqlType == "9"){
					var parsedBlurType = blurType;
					if(blurType.indexOf(",") >0){
						parsedBlurType = blurType.replace(",","_");
					}
					if(sqlType == "8"){//为空
						var val = parsedBlurType+"_IS_NULL";
						item = val;
					}else if(sqlType == "9"){//不为空
						var val = parsedBlurType+"_IS_NOT_NULL";
						item = val;
					}
					blurText = $(":button[name=search_blurText]",$panel).text() + "[" + $(":button[name=search_sqlText]",$panel).text() + "]";
				}else{
					blurText = $(":button[name=search_blurText]",$panel).text() + "[" + $(":button[name=search_sqlText]",$panel).text() + "]:" + item;
				}
				
				return {
					"blurType":blurType, 
					"blurValue":item,
					"sqlType":sqlType,
					"blurText":blurText
				}
			}
			
			//tagsinput初始化
			$panel.find(":input[name=search_blurValue]").tagsinput({
  				"trimValue"	: true,
  				"maxTags"  	: 10,
  				"maxChars" 	: 50,
  				"tagClass" 	: "label label-tag",
  				"itemText"	: function(item){
  									var parseItemVal = parseItem(item);
  									return parseItemVal.blurText;
  								}
			});
			
			//当点击选择8或9类型时，需要直接构建tagsinput标签
			$panel.find(".dropdown_search_sqlType .dropdown-menu li").on("click",function(event){
				var $li = $(this);
				var $a = $li.find("a");
				var sqlType = $a.attr("key");
				
				if(sqlType == "8" || sqlType == "9"){
					var blurType=$(":hidden[name=search_blurType]",$panel).val();
					var parsedBlurType = blurType 
					if(blurType.indexOf(",") >0){
						parsedBlurType = blurType.replace(",","_");
					}
					
					if(sqlType == "8"){//为空
						var val = parsedBlurType+"_IS_NULL";
						$panel.find(":input[name=search_blurValue]").tagsinput('add',val);
					}else if(sqlType == "9"){//不为空
						var val = parsedBlurType+"_IS_NOT_NULL";
						$panel.find(":input[name=search_blurValue]").tagsinput('add',val);
					}
				}
			});
			
			//当被添加到tagsinput
			$panel.find(":input[name=search_blurValue]").on("beforeItemAdd", function(event){
			
				var parseItemVal = parseItem(event.item);
				
				//如果之前存在，则不能继续追加
				if(parseItemVal.sqlType == "8" || parseItemVal.sqlType == "9"){
					//如果之前存在过，则不继续添加
					var array = search_tags.data("search");
					if(array && array.length > 0){
						var i = 0;
						for(i=0;i<array.length;i++){
							var val = array[i];
							//xm
							var existsBlurType = val["blurType"];
							//xm_IS_NOT_NULL
							var existsBlurValue = val["blurValue"];
							if(parseItemVal.blurType == existsBlurType && parseItemVal.blurValue == existsBlurValue){
								//若之前存在，则取消添加
								event.cancel = true;
							}
						}
					}
					//类型8和9互斥，不可同时存在
					//TODO
				}
			});
			
			//当添加到tagsinput之后
			$panel.find(":input[name=search_blurValue]").on("itemAdded", function(event){
			
	  			var parseItemVal = parseItem(event.item);
	  			
				var index = search_tags.data("_index");
				
	  			search_tags.data("search").push({
	  				"index":index,
					"blurType":parseItemVal.blurType, 
					"blurValue":parseItemVal.blurValue,
					"sqlType":parseItemVal.sqlType
				});
				
				var tagEl = event.options['tagElement'];
				if(tagEl){
					tagEl.data("search-index", search_tags.data("_index"));
					search_tags.data("_index", search_tags.data("_index")+1);
				}
			});
			
			//移除tagsinput之后
			$panel.find(":input[name=search_blurValue]").on("beforeItemRemove", function(event){
			
				var tagEl = event.options['tagElement'];
				search_tags.data("search", $.grep(search_tags.data("search"), function(n,i){
					return n["index"] != tagEl.data("search-index");
				}));
			});
			
			$panel.find(":input[name=search_blurValue]").on("allItemsRemoved", function(event){
				search_tags.data("search", []);
				search_tags.data("_index", 0);
			});
			
			//清空输入框内容
			$(":input[name=search_blurValue]",$panel).next().bind("click",function(){
				$(":input[name=search_blurValue]",$panel).val("");
			});
			//模糊查询绑定事件
			$(":checkbox[name=searchType]",$panel).click(function(){
				if ($(":checkbox[name=searchType]:checked",$panel).size() > 0){
					var t = [],v=[];
					$(":checkbox[name=searchType]:checked",$panel).each(function(i,n){
						t.push($(n).parent().text());
						v.push($(n).val());
					});
					$(":button[name=search_blurText]",$panel).html(t.join(',')).append('<span class="caret"></span>');
					$(":input[name=search_blurType]",$panel).val(v.join(','));
				} else {
					$(":button[name=search_blurText]",$panel).html('全部').append('<span class="caret"></span>');
					$(":input[name=search_blurType]",$panel).val("-1");
				}
			});
			//模糊查询类型切换
			$("a[name=search_sqlType]",$panel).click(function(e){
				e.preventDefault();
				$(":button[name=search_sqlText]",$panel).html($(this).html()).append('<span class="caret"></span>');
				$(":input[name=search_sqlType]",$panel).val($(this).attr("key"));
			});
			var condTimer;
			//显示条件项
			var _showCondByMouseover = function (t) {
				if(condTimer){
					clearTimeout(condTimer);					
				}
			    var $this = $(t);
			    var $clps = $($this.data('href')); // .collapse
			    var $pb = $clps.parents('.panel-body'); 
			    if ($clps.is(':animated')) {
			        $clps.finish();
			    }
			    if ($clps.is(':hidden')) {
			        $pb.find('.collapse').hide();
			        var $this_v = $this.parents('.tag-list-cond').find('a[data-href="' + $this.data('href') + '"]:visible');
			        var a_offset = $this_v.offset();
			        $pb.offset(a_offset);
			        var t = $pb.css('top');
			        $pb.css({
			            left: '0',
			            top: function () {
			                return parseFloat(t) + $this_v.outerHeight() + 7;
			            }
			        });
	
			        $clps.off();
			        $clps.hover(function (e) {//鼠标移动进入clps
			            clearTimeout(condTimer);
			        },function (e) {//鼠标移动离开clps的区域范围
			        	//clps的区域范围
			        	var $new_clps = $($this.data('href'));
			        	
			        	var offset = $new_clps.offset();
			        	
			        	var width = $new_clps.width();
			        	var height = $new_clps.height();
			        	
			        	var left = offset.left;
			        	var top = offset.top;
			        	
			        	var minX = left;
			        	var maxX = left + width;
			        	var minY = top;
			        	var maxY = top + height;
			        	
			        	var x = e.clientX;
			        	var y = e.clientY;
			        	
			        	if(minX <= x && x <= maxX && minY <= y && y <= maxY){
			        		//在范围内，忽略
			        	}else{
			        		condTimer = setTimeout(function () {
			        			$this.parents('.tag-list-cond').find('a[data-href="' + $this.data('href') + '"]').parent('li').removeClass('in');
			        			$clps.slideUp(150);
			        		},150);			        		
			        	}
			        });
	
			        $clps.slideDown(200);
			        _adjustCaret($clps);
			        $this.parents('.tag-list-cond').find('li').removeClass('in');
			        $this.parents('.tag-list-cond').find('a[data-href="' + $this.data('href') + '"]').parent('li').addClass('in');
			    }
			}
			var _hideCond = function (t) {
			    var $this = $(t);
			    var $clps = $($this.data('href')); // .collapse
			    if ($clps.is(':animated')) {
			        $clps.finish();
			    }
			    var $pb = $clps.parents('.panel-body'); //
			    condTimer = setTimeout(function () {
			        $this.parents('.tag-list-cond').find('a[data-href="' + $this.data('href') + '"]').parent('li').removeClass('in');
			        $clps.slideUp(150);
			    },200);
			}
			
			//处理点击选中或取消选择
			var _toggleCur = function (t, singleType) {
			    if ($(t).hasClass('all-cond')) {
			        $(t).parent('li').parent('.tag-list').children('li').children('a:not(.all-cond)').removeClass('cur');
			    } else {
			        if (singleType == 0) {
			            $(t).parent('li').parent('.tag-list').children('li').children('a').not($(t)[0]).removeClass('cur');
			        } else {
			            $(t).parent('li').parent('.tag-list').children('li').children('a.all-cond').removeClass('cur');
			        }
			    }
			    $(t).toggleClass('cur');
			}
	
			// 初始化头部条件栏，及已选的条件；clps:条件所在的collapse
			var _initConditions = function (clps,panel,trim) { 
			    var $cbs = $('.cond-bar-selected .tag-list-cond',panel); // 获取头部条件栏
			    $('.cond-bar', clps).each(function () {
			        var $tag_list = $(this).parent('.collapse').find('.tag-list:not(.tag-list-cond-except)'); // 获取当前collapse用户可选条件栏
			        var $clps_tg = $cbs.find('li:not(.selected)>a[data-href="#' + clps.attr('id') + '"]'); // 头部可选条件
			        var conds = new Array(); // 获取当前collapse下已经选中的条件
			        $tag_list.find('li>a.cur').each(function () { // 将已选择条件放入数组
			        	var text = $(this).text();
			        	var trimText = "";
			        	if(trim){
			        		trimText = text.replace(/ /g, "");
			        	}else{
			        		trimText = text;
			        	}
			        	conds.push(trimText);			        		
			        });
			        var $clps_tg_slcd = $cbs.find('li.selected>a[data-href="#' + clps.attr('id') + '"]').parent('li'); // 获取头部条件栏selected状态下的li，与当前选中条件对应
			        if (conds.length > 0) { // 如果有已选中的条件
			            if ($clps_tg_slcd.length == 0) { // 头部条件栏不存在该条件
			                $clps_tg_slcd = $clps_tg.parent('li').clone(); // 拷贝
			                $clps_tg_slcd.addClass('selected in');
			                $clps_tg_slcd.find('a').attr('data-href', '#'+clps.attr('id')); // 设置自定义属性
			            }
			            $clps_tg_slcd.find('a').attr('title', $clps_tg.text() + '：' + conds.join(',')); // 设置title属性
	
			            var strTmp = '<b>' + $clps_tg.text() + '：</b>';
			            strTmp += conds.join(',') + '<i class="fa fa-remove close-icon"></i>';
			            $clps_tg_slcd.find('a').html(strTmp);
			            $clps_tg_slcd.find('a i.close-icon').off();
			            $clps_tg_slcd.find('a i.close-icon').click(function () {
			                _removeCondition(this);
			                return false;
			            });
			            $clps_tg_slcd.find('a').off();
			            
			            $clps_tg_slcd.find('a').click(function () {
			            	var id = $(this).data('href'); // .collapse
			            	if ($(id).is(":hidden")){
			            		_showCondByMouseover(this);
			            	} else {
			            		_hideCond(this);
			            	}
			            });
			            $cbs.prepend($clps_tg_slcd);
			            $clps_tg.parent('li').hide();
			        } else {
			            $clps_tg_slcd.remove();
			            $clps_tg.parent('li').show();
			        }
			        _adjustCaret(clps);
			    });
			}
	
			var _removeCondition = function (t) { // 删除条件
			    var $clps = $($(t).parent('a').data('href'));
			    $clps.find('.tag-list>li').show();//联动隐藏的选项显示出来
			    $clps.find('.nav-tabs>li>a').show();//联动隐藏的拼音显示出来
			    $clps.find('.tag-list>li>.cur').removeClass('cur'); // 去除选中效果
			    $clps.find('.tab-pane .tag-list:not(.tag-list-cond-except)>li').remove(); // 去除“已选”页签中的条件
			    $clps.find('.nav_tag>li>.cur').removeClass('cur'); // 去除“已选”之外页签中的选中效果
 			    $clps.find('.tag-list.tag-list-cond>li>a').remove(); // 去除日期中的条件
			    $clps.find('.tag-list.tag-list-cond').siblings('.tag-list-cond-blank').show();//日期类型下的"[未选择条件]"显示
			    
			    // 显示头部备选条件（即头部已选的条件之外的条件）
			    $(t).parent('a').parent('li').siblings('li').find('a[data-href="' + $(t).parent('a').data('href') + '"]').parent('li').show(); 
			    $(t).parent('a').parent('li').remove(); // 去除头部已选的条件
			    _adjustCaret($clps);
			}
	
			var _adjustCaret = function (clps) { // 调整条件顶部箭头； clps:条件所在的collapse
			    var $caret = clps.children('.cond-caret');
			    var $this_v = $('.cond-bar-selected .tag-list-cond',$panel).find('a[data-href="#' + clps.attr('id') + '"]:visible');
			    var a_offset = $this_v.offset();
			    var p_offset = clps.parents('.panel-body').offset();
			    if (a_offset==undefined) return;
			    $caret.css({
			        left:function () {
			            return a_offset.left + $this_v.outerWidth()/2 - p_offset.left - $caret.outerWidth()/2;
			        }
			    });
			}
	
			// 删除条件（出生日期）
			var _removeConditionBar = function (t) { 
			    var $clps = $(t).parents('.collapse');
			    //$(t).parent('a').removeClass('cur');
			    _initConditions($clps,$panel,true);
			    $(t).parent('a').parent('li').remove();
			    if ($clps.find('.tag-list.tag-list-cond>li').length == 0) {
			        $clps.find('.tag-list.tag-list-cond').siblings('.tag-list-cond-blank').show();
			    }
			}
			
			var _removeTagCond = function () {
			    t = $(this)
			    var data_id = $(t).data('id');
				$(t).parents('.tab-content').find('.tag-list>li>a[data-id="'+data_id+'"]:last').click();
			}
			
			var _initTabpanel = function (tls) { // 初始化tabs
			    var arrCur = new Array();
			    var tab_pane_id_done;
			    
			    $(tls).each(function () {
			        var $tab_pane = $(this).parents('.tab-pane');
			        var tab_pane_id = $tab_pane.attr('id');
			        var arr_t = tab_pane_id.split('_');
			        tab_pane_id_done = arr_t.slice(0, arr_t.length - 1).join('_') + '_done';
			        $(this).find('li').each(function () {
			            if ($(this).children('a').hasClass('cur')) {
			                var $tmp = $(this).clone();
			                $tmp.find('a').removeAttr('href');
			                $tmp.find('a').attr('title','点击删除');
			                arrCur.push($tmp);
			            }
			        });
	
			        if ($(this).find('li>a.cur').length == 0) {
			            $tab_pane.parent().siblings('.nav_tag').find('a[href="#' + tab_pane_id + '"]').removeClass('cur');
			        } else {
			            $tab_pane.parent().siblings('.nav_tag').find('a[href="#' + tab_pane_id + '"],a[href="#' + tab_pane_id_done + '"]').addClass('cur');
			            $(this).parents('.tab-content').find('.tab-pane:first>.tag-list').empty();
			            for (var i in arrCur) {
			                $(this).parents('.tab-content').find('.tab-pane:first>.tag-list').append(arrCur[i]);
			            }
			        }
			    });
			    tls.parents('.tab-content').find('.tab-pane:first>.tag-list').empty();
			    for (var i in arrCur) {
			        tls.parents('.tab-content').find('.tab-pane:first>.tag-list').append(arrCur[i]);
			    }
			    
			    tls.parents('.tab-content').find('.tab-pane:first>.tag-list>li>a').bind('click',_removeTagCond)
			    
			    if (arrCur.length == 0) { // 所有tab无选中条件
			        tls.parents('.tab-pane').parent().siblings('.nav_tag').find('a[href="#' + tab_pane_id_done + '"]').removeClass('cur');
			    }
			}
			$('a').addClass('transition-common'); // 加通用动画
			
			var inputTimer;
			$('.input-group-btn.input-group-input .form-control',$panel).on('keyup focus',function () {
	            clearTimeout(inputTimer);
			    var $this = $(this);
			    if($this.val() != ''){
	                $this.siblings('.input-group-input-remove').show();
	                $this.siblings('.input-group-input-remove').click(function () {
	                    $this.val('');
	                });
				}
	        }).blur(function () {
	            var $this = $(this);
	            inputTimer = setTimeout(function () {
	                $this.siblings('.input-group-input-remove').off('click');
	                $this.siblings('.input-group-input-remove').hide();
	            },150);
	        });
	        
	        $('.cond-bar-selected .tag-list-cond>li>a',$panel).click(function () {
		    	var id = $(this).data('href'); // .collapse
            	if ($(id).is(":hidden")){
            		_showCondByMouseover(this);
            	} else {
            		_hideCond(this);
            	}
		    });
	
		    $('.cond-bar-selected .tag-list-cond>li>a1',$panel).click(function (e) {
		        e.preventDefault();
		        showCondByClick(this);
		    });
	        
	        $('.panel-body .laydate-icon-zfcolor',$panel).focus(function () {	
		        $('.layui-laydate',$panel).off();
		        $('.layui-laydate',$panel).hover(function () {
		            clearTimeout(condTimer);
		        },function () {
		            clearTimeout(condTimer);
		        });
		    });
		    
		    $panel.find('.cond-bar:not(.cond-bar-selected)').each(function () { // 遍历，用于绑定点击事件
		        var singleType = 0;
		        if ($(this).hasClass('cond-bar-single')) { // 包含.cond-bar-single，表示条件单选
		            singleType = 0; // 单选
		        } else {
		            singleType = 1; // 多选
		        }
		        
		        $(this).find('.tag-list:not(.tag-list-cond)>li>a').click(function (e) { // 绑定点击事件
		            e.preventDefault();
		            _toggleCur(this, singleType);
		            var $tls = $(this).parents('.tab-content').find('.tag-list.tag-list-cond-except');
		            _initTabpanel($tls);
		            var $clps = $(this).parents('.collapse'); // 获取条件所在的collapse
		            _initConditions($clps,$panel,true);
		        });
		    });
	
		    $(':button[name=all_s]',$panel).click(function () { // 绑定全选
		        var $clps = $(this).parents('.collapse');
		        $clps.find('.tag-list>li>a').addClass('cur');
		        _initConditions($clps,$panel,true);
		    });
	
		    $(':button[name=inverse_s]',$panel).click(function () { // 绑定反选
		        var $clps = $(this).parents('.collapse');
		        $clps.find('.tag-list>li>a').toggleClass('cur');
		        _initConditions($clps,$panel,true);
		    });
	
			// 绑定添加条件，数字范围
		    // 绑定添加条件，时间范围，两个时间，开始和结束,仅支持laydate 1
		    $(":button[name=addDateBtn],:button[name=addNumberBtn]",$panel).click(function () { // 绑定添加条件，数字范围
		        var str = '';
		        var strHtml = '<li class="selected"><a class="cur transition-common"></a></li>';
		        var $fg = $(this).parent('.form-group');
		        var sv = $fg.find('input:first').val();
		        var iv = $fg.find('input:last').val();
		        if (sv == '' && iv == '') {
		           return false;
		        }
		        str = sv + '~' + iv;
		        var $strHtml = $(strHtml);
		        $strHtml.find('a').attr('title', str);
		        $strHtml.find('a').html(str + ' <i class="fa fa-remove close-icon"></i>');
		        $strHtml.data("begin",sv);
		        $strHtml.data("end",iv);
		        
		        var $clps = $(this).parents('.collapse'); // 获取条件所在的collapse
		        $clps.find('.tag-list.tag-list-cond').append($strHtml);
		        $clps.find('.tag-list.tag-list-cond').siblings('.tag-list-cond-blank').hide();
		        $strHtml.find('a').find('i.close-icon').click(function () {
		            _removeConditionBar(this);
		            _initConditions($clps,$panel,false);
		            return false;
		        });
		        _initConditions($clps,$panel,false);
		    });
		    
		    //仅支持laydate 5
		    $(":button[name=addDateRangeBtn]",$panel).click(function () { // 绑定添加条件，时间范围，一个时间，分割成两个时间
		        var str = '';
		        var strHtml = '<li class="selected"><a class="cur transition-common"></a></li>';
		        var $fg = $(this).parent('.form-group');
		        var sv_and_iv = $fg.find('input:first').val();
		        if (sv_and_iv == '') {
		           return false;
		        }
		        var arr = sv_and_iv.split(" ~ ");
		        var sv = arr[0];
		        var iv = arr[1];
		        str = sv_and_iv;
		        var $strHtml = $(strHtml);
		        $strHtml.find('a').attr('title', str);
		        $strHtml.find('a').html(str + ' <i class="fa fa-remove close-icon"></i>');
		        $strHtml.data("begin",sv);
		        $strHtml.data("end",iv);
		        
		        var $clps = $(this).parents('.collapse'); // 获取条件所在的collapse
		        $clps.find('.tag-list.tag-list-cond').append($strHtml);
		        $clps.find('.tag-list.tag-list-cond').siblings('.tag-list-cond-blank').hide();
		        $strHtml.find('a').find('i.close-icon').click(function () {
		            _removeConditionBar(this);
		            _initConditions($clps,$panel,false);
		            return false;
		        });
		        _initConditions($clps,$panel,false);
		    });
	
			$('.nav_tag_all,.nav_tag_cancel',$panel).click(function () {
		        var $t = $(this);
		        var $tls = $(this).parents('.cond-bar').find('.tag-list.tag-list-cond-except');
		        $tls.each(function () {
		        	//$(this).find('li>a').click();
		            if ($t.hasClass('nav_tag_all')) {
		                $(this).find('li>a').addClass('cur');
		            } else if ($t.hasClass('nav_tag_cancel')) {
		                $(this).find('li>a').removeClass('cur');
		            }
		        });
		        _initTabpanel($tls);
		        var $clps = $(this).parents('.collapse'); // 获取条件所在的collapse
		        _initConditions($clps,$panel,true);
		    });
	
		    $('a[name=clear_all_cond]',$panel).click(function (e) { // 清空所有条件
		        e.preventDefault();
		        $('.cond-bar-selected .tag-list-cond>.selected>a>.close-icon',$panel).each(function () {
		            $(this).click();
		        });
		        $panel.find(":input[name=search_blurValue]").tagsinput('removeAll');
		    });
		    
		    //选项搜索按钮
		    $("div[name=searchItem]>button",$panel).click(function(){
		    	var content = $(this).prev().val();
		    	//search ".tag-list>li"
		    	$(this).parents(".form-group").find(".tag-list").children().each(function(i,n){
		    		if ($(n).find("a").html().indexOf($.trim(content)) > -1){
		    			$(n).show();
		    		} else {
		    			$(n).hide();
		    		}
		    	})
		    	$(this).parents(".form-group").find(".nav-tabs").each(function(i,n){
		    		$(".transition-common:not([aria-controls*=_done])",$(n)).each(function(x,y){
		    			var id =  $(y).attr("href");
		    			var childSize = $("ul>li:not([style='display: none;'])", $(id)).size();
		    			if (childSize == 0){
		    				$(y).hide();
		    			} else {
		    				$(y).show();
		    			}
		    		});
		    		$(".transition-common:not([aria-controls*=_done]):visible",$(n)).first().click();
		    	})
		    });
		    //选项搜索框
		    $("div[name=searchItem]>input",$panel).keydown(function(event) {
				  if (event.which == 13 ) {
				     $(this).next().click();
				  }
		    });
		    
    		var _resetOptions = function(options,div,hasSelected){
    		
    			if (!hasSelected){
    				$(".tag-list>li",$(div)).show();
    				$("div[name=searchItem]",$(div)).show();
    			} else {
    				$(".tag-list>li>.transition-common",$(div)).each(function(i,n){
			    		if ($.inArray(n,options) > -1){
			    			$(n).parent().show();
			    		} else {
			    			if ($(n).hasClass("cur")){
			    				$(n).click();
			    			}
			    			$(n).parent().hide();
			    		}
			    	});
			    	$("div[name=searchItem]",$(div)).hide();
    			}
    		
		    	$(".nav-tabs",$(div)).each(function(i,n){
		    		$(".transition-common:not([aria-controls*=_done])",$(n)).each(function(x,y){
		    			var id =  $(y).attr("href");
		    			var childSize = $("ul>li:not([style='display: none;'])", $(id)).size();
		    			if (childSize == 0){
		    				$(y).hide();
		    			} else {
		    				$(y).show();
		    			}
		    		});
		    		$(".transition-common:not([aria-controls*=_done]):visible",$(n)).first().click();
		    	})
		    }
		    
		    //初始化联动关系
			$("div[relation=true]",$panel).each(function(i,n){
				var $this = this;
				var keys = $(n).attr("relationKeys").split(",");
				$.each(keys,function(x,key){
					$("a[searchname="+key+"]",$panel).bind("click",function(){
						var i_items=[];
						$.each(keys,function(key_index,key_v){
							$.merge(i_items,$("ul:not(ul[name=seleted])>li>.cur[searchname="+key_v+"]",$panel));
						});
						//已选条件不为空
						var options = [];
						var tmp=[];
						var tmpKey;
						var b = true;
						$.each(i_items,function(index,y){
							var searchname=$(y).attr("searchname");
							var itemvalue=$(y).attr("itemvalue");
							//如果已选项有对本区域联动资格
							if ($.inArray(searchname,keys) > -1){
								//本区域的级联子数据
								var items = $("a[search_"+searchname+"="+itemvalue+"]",$($this));
								//相同key取合集，不同key取并集
								if ((tmpKey == searchname || index == 0) && b){
									$.merge(options,items);
								} else {
									if (tmpKey != searchname && !b && tmp.length > 0){
										options=tmp;
										tmp=[];
									} 
									b = false;
									$.each(options, function(k,l){
										if ($("a[search_"+searchname+"="+itemvalue+"][itemvalue="+$(l).attr("itemvalue")+"]",$($this)).size() > 0){
											tmp.push(l);	
										}
									});
								}
								tmpKey=searchname;
							}
						});
						
						if (i_items.length > 0){
							if (tmp.length>0){
								_resetOptions(tmp,$this,true);	
							} else {
								_resetOptions(options,$this,true);	
							}
						} else {
							_resetOptions(options,$this,false);	
						}
					});
				});
			});
			
			//初始化默认选中
		    $(".collapse",$panel).each(function(){
		    	if ($(this).find(".tag-list>li>.cur").size()>0){
		    		_initConditions($(this),$panel,true);
		    	}
		    });
		    $('a[data-default=true]',$panel).each(function(i,n){
		    	$(n).click();
	    	})
		},
		getSearchMap:function(){
			var $panel = $(this);
			var map = {};
			//单选、多选
			var selectType={};
			$(".panel-body .cur[type=select]",$panel).each(function(i,n){
				if (selectType[$(n).attr("searchname")]){
					if ($.inArray($(n).attr("itemvalue"),selectType[$(n).attr("searchname")])==-1){
						selectType[$(n).attr("searchname")].push($(n).attr("itemvalue"));
					}
				} else {
					selectType[$(n).attr("searchname")]=[$(n).attr("itemvalue")];
				}
			});
			
			//日期
			var dateType={};
			$("ul[type=date]",$panel).each(function(i,n){
				var searchname = $(n).attr("searchname");
				var format = $(n).attr("format");
				
				$(n).find("li:not(:empty)").each(function(x,y){
					var arr = [format,$(y).data("begin"),$(y).data("end")];					
					if (dateType[$(n).attr("searchname")]){
						dateType[$(n).attr("searchname")].push(arr);
					} else {
						dateType[$(n).attr("searchname")]=[arr];
					}
				})
			});
			
			var numberType={};
			$("ul[type=number]",$panel).each(function(i,n){
				var searchname = $(n).attr("searchname");
				
				$(n).find("li:not(:empty)").each(function(x,y){
					var arr = [$(y).data("begin"),$(y).data("end")];					
					if (numberType[$(n).attr("searchname")]){
						numberType[$(n).attr("searchname")].push(arr);
					} else {
						numberType[$(n).attr("searchname")]=[arr];
					}
				})
			});
			
			//模糊
			/*
			var blurType=$(":hidden[name=search_blurType]",$panel).val();
			var sqlType=$(":input[name=search_sqlType]",$panel).val();
			var blurValue = $.trim($(":input[name=search_blurValue]",$panel).val()).split(/\s/);
			var inputType={};
			if (blurType == "-1"){
				$(":checkbox[name=searchType]",$panel).each(function(i,n){
					inputType[$(this).val()]=blurValue;
				});
			}else {
				var types = blurType.split(",");
				$.each(types,function(i,n){
					inputType[n]=blurValue;
				})
			}*/
			
			var searchTags = $(":hidden[name=search_tags]",$panel).data("search") || [];
			var multipleInputType=[];
			//{"index": blurType + "-" + item + "-" + sqlType, "blurType":blurType, "blurValue":item,"sqlType":sqlType}
			$.each(searchTags, function(i,n){
				var blurType = n['blurType'];
				var blurValue = n['blurValue'];
				var sqlType = n['sqlType'];
				var index = n['index'];
				
				//全部
				if (blurType == "-1"){
					var _blur_type = "";
					
					var $checkbox_search_types = $(":checkbox[name=searchType]",$panel);
					var i = 0;
					var length = $checkbox_search_types.length;
					for(i=0;i< length;i++){
					
						var $checkbox_search_type = $($checkbox_search_types.get(i));
						var _blur_type_sub = $checkbox_search_type.val();
						
						var last = ( i == length - 1);
						if(last){
							_blur_type += _blur_type_sub;
						}else{
							_blur_type += _blur_type_sub + ",";  
						}
					}
					var inputType_element = {"blurType":_blur_type, "blurValue": blurValue, "sqlType": sqlType, "index":index};
					multipleInputType.push(inputType_element);
				}else {//不是全部
					multipleInputType.push(n);
				}
			});

			map["searchModel.multipleInputType"]=JSON.stringify(multipleInputType);

			map["searchModel.selectType"]=JSON.stringify(selectType);
			map["searchModel.dateType"]=JSON.stringify(dateType);
			map["searchModel.numberType"]=JSON.stringify(numberType);
			return map;
		},
		getSearchHtml:function(){
			var map = $(this).getSearchMap();
			var html = [];
			for(var key in map){
				var val = map[key];
				html.push("<input type='hidden' name='"+ key +"' value='"+ val +"'/>");
			}
			return html.join(" ");
		}
	});
	
	$.extend($.search,{
		getSearchMap:function(){
			return $('#defaultId').getSearchMap();
		},
		getSearchHtml:function(){
			return $('#defaultId').getSearchHtml();
		}
	})
}(jQuery));

$('#${id}').searchInit();
</script>