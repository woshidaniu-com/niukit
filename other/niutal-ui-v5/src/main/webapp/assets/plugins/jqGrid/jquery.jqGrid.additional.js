/*
 * @discretion	: 扩展$.jqGrid组件$方法.
 * @author    	: wandalong 
 * @version		: v1.0.2
 * @email     	: hnxyhcwdl1003@163.com
 */
(function($) {
	
	function getTipText(options){
		var tipText = "";
		if((options.datatype && options.datatype == "local")){
			//进行国际化处理
		    if($.i18n){
		    	tipText = $.i18n.prop("needCondition");
		    }
		    if(!tipText){
		    	tipText = "请选择筛选条件!";
		    }
		}else{
			//进行国际化处理
		    if($.i18n){
		    	tipText = $.i18n.prop("emptyRecord");
		    }
		    if(!tipText){
		    	tipText = "没有符合条件记录!";
		    }
		}
		return tipText;
	}
	
	function gridAdapter(selector){
			
		var table = this;
		var options = this.p;
		var gviewSelector = "#gview_"+$(selector).attr("id");
		// 如果数据不存在，提示信息
	    var rowNum = $(selector).jqGrid('getGridParam','records');
	    var bdiv = $(selector).closest("div.ui-jqgrid-bdiv");
	    if (rowNum==0){
	    	 bdiv.find("div.auto-hdiv").remove();
	    	 var columnArray = $(selector).jqGrid('getGridParam','colModel'); 
	    	 if ($(selector).find(".emptyrow").size()==0){
	    		 var minHeight 	= 80;
	    		 if(!$.founded(options.height) || "auto" == String(options.height).toLowerCase() ){
	    			 if( options.minHeight && "auto" != String(options.minHeight).toLowerCase() ){
	    				 minHeight =  parseInt(options.minHeight||80);
	    			 }
	    		 }
	    		 $(selector).find("tbody").append("<tr name='norecords' class='emptyrow' align='center' valign='middle' style='height:"+minHeight+"px;'><td colspan='"+columnArray.length+"' align='center' valign='middle' style='line-height:"+minHeight+"px;text-align: center;font-size:12px;' >"+getTipText(options)+"</td><tr>");
	    	 }
	    	 $(selector).find("tr[name=norecords]").show();
	    	 if((!$.founded(options.minHeight)&&!$.founded(options.height)) || "auto" == String(options.height).toLowerCase() ){
		    	 /*设置高度自动调整*/
		    	 $(selector).setGridHeight("auto");
	    	 }else{
	    		//3：开发者自定义了高度，这里只做数据不足时候的补全
	    		$(selector).setGridHeight(Math.max(parseInt(options.minHeight||80),parseInt(options.height||80)));
	    	}
	    } else { 
	    	//1：未设置高度  2：自动高度适应
	    	if(!$.founded(options.height) || "auto" == String(options.height).toLowerCase() ){
	    		
	    		if( options.minHeight && "auto" != String(options.minHeight).toLowerCase() ){
	    			/*设置默认高度*/
			    	var innerHeight = Math.max($(table).outerHeight(),($(table).actual("height")||0));
			    	var minHeight 	= parseInt(options.minHeight)||80;
			    	bdiv.find("div.auto-hdiv").remove();
					if( innerHeight < minHeight){
						bdiv.append('<div class="auto-hdiv" style="height:'+(minHeight-innerHeight)+'px;">&nbsp;</div>');
					}
	    		}
	    		/*设置高度自动调整*/
		    	$(selector).setGridHeight("auto");
	    	}else{
	    		//3：开发者自定义了高度，这里只做数据不足时候的补全
	    		var innerHeight = Math.max($(table).outerHeight(),($(table).actual("height")||0));
	    		var minHeight 	= Math.max(parseInt(options.minHeight||80),parseInt(options.height||80));
	    		bdiv.find("div.auto-hdiv").remove();
				if( innerHeight < minHeight){
					bdiv.append('<div class="auto-hdiv" style="height:'+(minHeight - innerHeight)+'px;">&nbsp;</div>');
				}
	    	}
	    	if(options.autoheight){
	    		/*设置默认高度*/
		    	$(selector).setGridHeight("auto");
	    	}
	    	// 如果存在记录，则隐藏提示信息。
	    	$(selector).find("tr[name=norecords]").hide();
	    }
	    
	    if(options.resizeHandle){
	    	$(selector).jqGrid().setGridWidth($(options.resizeHandle).actual('innerWidth'));
	    }
	    if(options.resizeHandle&&$.resize){
			$(options.resizeHandle).unbind("resize").resize(function(){
				$(selector).jqGrid('setGridWidth',$(options.resizeHandle).actual('innerWidth'));
			});
		}
	    
	    //处理formatter导致的td上title显示异常
	    $.each(options["colModel"]||[],function(i,item){
		   //判断有格式函数
		   if($.isFunction(item["formatter"])){
			   //循环数据行
			   $(table).find("tr.jqgrow").each(function(){
				   $(this).find("td[aria-describedby$='_"+item["name"]+"']").attr("title","");
			   });
		   }
	    });
	    
	    setTimeout(function(){
			//styleCheckbox(table);
			//updateActionIcons(table);
			//updatePagerIcons(table);
			//enableTooltips(table);
			
			//replace icons with FontAwesome icons like above
			var replacement = {
				'ui-icon-seek-first' : 'glyphicon glyphicon-step-backward',
				'ui-icon-seek-prev' : 'glyphicon glyphicon-chevron-left',
				'ui-icon-seek-next' : 'glyphicon glyphicon-chevron-right',
				'ui-icon-seek-end' : 'glyphicon glyphicon-step-forward'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				if($class in replacement) icon.attr('class', 'ui-icon '+ replacement[$class]);
			});
			
			if($.fn.tooltip){
				 
				$(table).find('[data-toggle*="tooltip"]').tooltip('destroy').tooltip({container:'body'});
				$('.navtable .ui-pg-button').tooltip('destroy').tooltip({container:'body'});
				$(table).find('.ui-pg-div').tooltip('destroy').tooltip({container:'body'});
				
			}
			
			/*if($.fn.mCustomScrollbar){
				$(gviewSelector).mCustomScrollbar({
					autoHideScrollbar:true,
					advanced:{
					    updateOnBrowserResize: true,
					    updateOnContentResize: true,
					    autoExpandHorizontalScroll: true
					}
				});
		   }*/
		}, 0);
		
		if(!options.edit&&!options.add&&!options.del&&!options.search&&!options.refresh&&!options.view){
			$(selector+"_left").remove();
		}
		
		//ie浏览器
		if($.browser && $.browser.msie===true && $.browser.version <= 9){
			$("table.ui-jqgrid-btable").find("td").attr("unselectable","on");
		}
		
		$(this).data("inited",true);
	}
	
	$.fn.extend({
		getJqGridOptions:function(options,pagerId){
			options = options||{};
			var selector = $(this).selector||options.selector;
			//复制loadComplete，gridComplete
			var tmpOptions = $.extend(true , {},{
				"loadComplete" : options.loadComplete||$.noop,
				"gridComplete" : options.gridComplete||$.noop
			});
			//进行国际化处理
		    if($.i18n){
		    	options =  $.i18n.grid(options);
		    }
			var finalOptions = {};
			if(options.datatype&&options.datatype!="local"){
				finalOptions =  $.extend({},options||{},{
					pager: pagerId, //分页工具栏
					loadComplete: function (xhr) {
						//jqGrid兼容处理
						gridAdapter.call(this, selector);
						//调用传过来的loadComplete
						return tmpOptions.loadComplete.call(this,xhr);
					}
				});
			}else{
				finalOptions =  $.extend({},options,{
					pager: pagerId, //分页工具栏
					gridComplete: function () { 
						//jqGrid兼容处理
						gridAdapter.call(this, selector);
						//调用传过来的gridComplete
						return tmpOptions.gridComplete.call(this);
						
					}
				});
			}
		    return finalOptions;
		},
		loadJqGrid:function(options){
			var opts = $.extend(true,{},options||{},{"selector":$(this).selector});
			return this.each(function(){
				var options = $(this).getJqGridOptions(opts,opts.pager);
				$(this).jqGrid(options).navGrid(options.pager||"#no_pager", {
					edit: false,
					editicon : 'icon-pencil blue',
					add: false,
					addicon : 'icon-plus-sign purple',
					del: false,
					delicon : 'icon-trash red',
					search: false,
					searchicon : 'icon-search orange',
					refresh: false,
					refreshicon : 'icon-refresh green',
					view: false,
					viewicon : 'icon-zoom-in grey'
				}, {}, {}, {}, {
					multipleSearch : false
				});
			});
		},
		/*重新加载JQGrid组件*/
		reloadJqGrid:function(options){
			var selector = $(this).selector;
			$(selector).GridUnload();
			$(selector).loadJqGrid(options);
		},
		/*刷新结果集*/
		reloadGrid:function(){
			$(this).jqGrid().trigger('reloadGrid');
		},
		/*刷新结果集*/
		refershGrid:function(jsonMap){
			if(jsonMap){
				$(this).jqGrid('setGridParam',{  
					 datatype: 'json',
				     postData : jsonMap ||{},
				     page	  : 1
				 }).trigger('reloadGrid');
			}else{
				$(this).jqGrid().trigger('reloadGrid');
			}
		},
		/*刷新结果集*/
		refershLocalGrid:function(localData){
			 $(this).clearGridData();  
			 $(this).jqGrid().clearGridData();  
			 $(this).jqGrid('setGridParam',{  
				 datatype	: "local",  
			     data		: localData||[],
				 rowNum 	: (localData||[]).length, // 每页显示记录数
			     page	  	: 1
			 }).trigger('reloadGrid', [{page:1}]); 
		},
		searchGrid:function(jsonMap){
			if(jsonMap){
				$(this).jqGrid('setGridParam',{  
					 datatype: 'json',
				     postData : jsonMap ||{},
				     page	  : 1
				 }).trigger('reloadGrid');
			}else{
				$(this).jqGrid().trigger('reloadGrid');
			}
		},
		getRowCount:function(){
			return $(this).getGridParam("reccount");
		},
		getKeys:function(){
			var multiselect = $(this).jqGrid('getGridParam','multiselect');
			if(multiselect){
				return $(this).jqGrid('getGridParam','selarrrow');
			}else{
				var selectedKey = $(this).jqGrid("getGridParam", "selrow");
				if($.founded(selectedKey)){
					return [selectedKey];
				}else{
					return [];
				}
			}
		},
		getRow:function(key){
			return $(this).jqGrid().getRowData(key) || $(this).jqGrid('getRowData', key);
		},
		setRow:function(rowid,rowdata){
			return $(this).jqGrid().setRowData(rowid,rowdata);
		},
		getRows:function(){
			return $(this).jqGrid('getRowData');  
		},
		getSelectedRows:function(){
			var $this = this;
			var rows = [];
			$.each($($this).getKeys(),function(index,key){
				var row = $($this).jqGrid('getRowData', key);
				rows.push(row);
			});
			return rows;
		},
		setButton:function(options){
			/*添加自定义按钮*/
			$(this).navButtonAdd(options.pager, {
				id:options.id||"colChoice",/*string类型，按钮id*/ 
				caption:" ", /*按钮名称，可以为空，string类型 */
				buttonicon:options.buttonicon||"icon-gear", /*按钮的图标，string类型，必须为UI theme图标 */
				onClickButton:function(){
					options.onClickButton.call(this);
				}, /*按钮事件，function类型，默认null */
				position: options.position||"first", /*first或者last，按钮位置 */
				title: options.title||"", /*string类型，按钮的提示信息*/
				cursor: "pointer"/*string类型，光标类型，默认为pointer */
			});
		}
	});
	
	//只能输入参数自动绑定脚本
	$(document).off('keyup', '.ui-jqgrid-pager input[name="showCount"]').on('keyup', '.ui-jqgrid-pager input[name="showCount"]', function (e) {
		if($.trim(this.value).length>0){
			//只能输入数字
			this.value = this.value.replace(/[^\d]/g,'')||"";
			//超出最大页码时使用最大页码
			var totalPage = $.trim($(this).next("span").text());
			if(/^[1-9]\d*$/.test(totalPage) && parseInt(this.value) > parseInt(totalPage)){
				this.value  = totalPage;
			}
		}else{
			this.value  = 1;
		}
	});
	
}(jQuery));