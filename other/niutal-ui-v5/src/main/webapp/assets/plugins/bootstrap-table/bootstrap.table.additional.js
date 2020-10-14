/**
 * bootstrap table 使用工具类
 * 
 * author:zhangxiaobin[1036]
 */
(function($) {
	'use strict';
	
	/**
	 * 默认参数
	 */
	var _default_parameters_ = {
			classes:			'table table-hover',
			height:				'auto',
			undefinedText:		'',
			striped:			true,
			mobileResponsive:	true,
			//url:				'',
			method: 			'post', 
			contentType:		'application/x-www-form-urlencoded;charset=UTF-8',
			ajaxOptions:		{},
			cache: 				false,
			pagination:			true,
			pageNumber:			1,
			pageSize:			15,
			pageList:			[15,30,50,100,500,1000],
			trimOnSearch:		true,
			sortable: 			true,                     
	        sortOrder: 			'asc',
	        showMultiSort:		true,
	        queryParamsType:	'',
	        //queryParams:		,
	        //searchParams:     , //自定义查询条件，可以是对象或则函数
	        //responseHandler:	,
	        sidePagination: 	'server', 
	        strictSearch:		true,
	        showColumns:		true,
	        showRefresh:		true,
	        minimumCountColumns:1,
	        clickToSelect:		true,
	        showToggle:			true,
	        //smartDisplay:		true,
	        escape:				true,
	        columns:			[],
	        cookieUrl:'bsTableCookie.zf'
	};

	/**
	 * 默认事件处理
	 */
	var _defaule_events_ = {
			onLoadError:	function(status){
				//会话超时状态已全局处理
				if (status != 901){
					$.error("数据加载失败");
				}
			}		//数据加载失败
			//onAll:			function(name, args){},	//所有事件都会触发的函数
			//onLoadSuccess:	function(data){}, 		//数据加载成功
			//onClickRow:		function(row, $element, field){}, //当用户点击某一行的时候触发
			//onDblClickRow:	function(row, $element, field){},//当用户双击某一行的时候触发
			//onClickCell:	function(field, value, row, $element){},//当用户点击某一列的时候触发
			//onDblClickCell: function(field, value, row, $element){},//当用户双击某一列的时候触发
			//onSort:			function(name, order){},//Fires when user sort a column
			//onCheck:		function(row, $element){},//Fires when user check a row
			//onUncheck:		function(row, $element){},//Fires when user uncheck a row
			//onCheckAll:		function(rows){},//Fires when user check all rows
			//onUncheckAll:	function(rows){},//Fires when user uncheck all rows
			//onCheckSome:	function(rows){},//Fires when user check some rows
			//onUncheckSome:	function(rows){},//Fires when user uncheck some rows
			//onColumnSwitch:	function(field, checked){},//Fires when switch the column visible
			//onColumnSearch:	function(field, text){},//Fires when search by column
			//onPageChange:	function(number, size){},//Fires when change the page number or page size
			//onSearch:		function(text){},//Fires when search the table
			//onToggle:		function(cardView){},//Fires when toggle the view of table
			//onPreBody:		function(data){},//Fires before the table body is rendered
			//onPostBody:		function(){},//Fires after the table body is rendered and available in the DOM
			//onPostHeader:	function(){},//Fires after the table header is rendered and availble in the DOM
			//onExpandRow:	function(ndex, row, $detail){},//当点击详细图标展开详细页面的时候触发
			//onCollapseRow:	function(index, row){},//当点击详细图片收起详细页面的时候触发
			//onRefreshOptions:	function(options){},//Fires after refresh the options and before destroy and init the table
			//onRefresh:			function(params){}//Fires after the click the refresh button
	};
	
	$.extend($.fn.bootstrapTable.defaults, {
		/* 用来设定如何解析从Server端发回来的json数据*/
		jsonReader	: {      
			/* json中代表实际模型数据的入口 */
			root		: "items",
			pagesize    : "pageSize",
			/* json中代表当前页码的数据 */
			page		: "currentPage",
			/* json中代表页码总数的数据 */
			total		: "totalPage",
			/* json中代表数据行总数的数据 */
			records		: "totalResult",  
			/* 如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定 */
			repeatitems : false      
		}
    });
	
	$.fn.extend({ 
		
		/*加载Bootstrap Table组件*/
		loadGrid:function(options){
			var $this = this;
			//复制queryParams，searchParams
			var tmpOptions = $.extend(true , {},{
				"queryParams" 		: options.queryParams||$.noop,
				"searchParams" 		: options.searchParams||$.noop,
				"responseHandler"	: options.responseHandler||$.noop
			});
			var _cardView = $.isMobile() && (options['mobileResponsive'] == undefined || options['mobileResponsive']);
			var opts = $.extend(true,{cardView: _cardView},_default_parameters_,_defaule_events_,options||{},{
				//最终执行的参数回调函数
				queryParams : function(params){
					var requestMap = tmpOptions.searchParams.call($this) || {};
					var queryMap =  tmpOptions.queryParams.call(this,params) || {};
						queryMap["queryModel.multiSort"] = (function(){
							var _sorts = [];
							if(params["sortName"]){
								_sorts.push({sortName:params["sortName"], sortOrder:params["sortOrder"]});
							}
							var _finalSorts = $.merge(_sorts, (params.multiSort||$.noop)()||[]);
							params.multiSort = $.noop;
							return _finalSorts;
						})();
						queryMap["queryModel.showCount"] = params["pageSize"];
						queryMap["queryModel.currentPage"] = params["pageNumber"];
					return $.extend({send_by_bootstrap_table:'true'},requestMap,queryMap);
				},
				responseHandler : function(data){
					data["total"] = data[this["jsonReader"]["records"]];
					data["totalPage"] = data[this["jsonReader"]["total"]];
					data["pageSize"] = data[this["jsonReader"]["pagesize"]];
					data["pageNumber"] = data[this["jsonReader"]["page"]];
					data["rows"] = data[this["jsonReader"]["root"]];
					return data;
				}
			});
			
			return this.each(function(){
				$(this).bootstrapTable(opts);
			});
		},
		
		/*重建表格*/
		rebuildGrid:function(options){
			$(this).bootstrapTable("destroy");
			$(this).loadGrid(options);
		},
		
		/*重新加载表格数据*/
		reloadGrid:function(queryParams){
			//需要加上用户自定义查询参数
			$(this).bootstrapTable("refresh",{query:queryParams||{}});
		},
		
		/*刷新数据*/
		refreshGrid:function(queryParams){
			//需要加上用户自定义查询参数
			$(this).bootstrapTable("refresh",{query:queryParams||{}});
		},
		
		/*重新加载数据，并定位到首页*/
		searchGrid:function(queryParams){
			$(this).bootstrapTable("refresh",{query:queryParams||{}});
		},
		
		/*根据id删除某行数据*/
		removeRow:function(id){
			$(this).bootstrapTable("removeByUniqueId", id);
		},
		
		/*删除行数据*/
		removeRows:function(field,values){
			$(this).bootstrapTable("remove",{field:field,values:values});
		},
		
		/*删除所有行数据*/
		removeAll:function(){
			$(this).bootstrapTable("removeAll");
		},
		
		/*向表中新增一行数据*/
		insertRow:function(index,row){
			$(this).bootstrapTable("insertRow",{index:index,row:row});
		},
		
		/*当前页追加新数据*/
		appendRow:function(row){
			$(this).bootstrapTable("insertRow",{index:$(this).getRowCount(),row:row});
		},
		
		/*当前页最前加入一行数据*/
		prependRow:function(row){
			$(this).bootstrapTable("insertRow",{index:0,row:row});
		},
		
		/*更新行数据*/
		updateRow:function(index,row){
			$(this).bootstrapTable("insertRow",{index:index,row:row});
		},
		
		/*获取当前页的行数*/
		getRowCount:function(){
			return $(this).bootstrapTable("getData",true).length;
		},
		
		/*获取选中行的数组，注意：如果用户指定了id列，返回id列值数组，否则返回每行数据数组*/
		getKeys:function(){
			var keys = [];
			var id_filed = $(this).bootstrapTable("getOptions")["uniqueId"];
			var multiselect = $(this).bootstrapTable("getSelections");
			if(id_filed == undefined)
				return multiselect;
			if(multiselect){
				$.each(multiselect,function(i,n){
					keys.push(n[id_filed]);
				});
			}
			return keys;
		},
		
		/*是否有选中*/
		isChecked:function(){
			return $(this).bootstrapTable("getSelections").length > 0;  
		},
		
		/*根据id列值获取行数据*/
		getRow:function(key){
			return $(this).bootstrapTable("getRowByUniqueId", key);
		},
		
		/*根据选中行数据*/
		getRows:function(){
			return $(this).bootstrapTable("getSelections");  
		},
		
		/*根据所有行数据*/
		getAllRows:function(){
			return $(this).bootstrapTable("getData",true);  
		},
		
		/*返回数据的id*/
		getDataIDs:function(){
			var ids = [];
			var uniqueid = $(this).bootstrapTable("getOptions")&&$(this).bootstrapTable("getOptions").uniqueId;
			if(!uniqueid){
				return ids;
			}
			var rows = $(this).bootstrapTable("getData",true);
			$.each(rows,function(i,row){
				ids.push(row[uniqueid]);
			});
			return ids;
		},
		
		/*显示列*/
		showColumn:function(field){
			return $(this).bootstrapTable("showColumn",field);  
		},
		
		/*隐藏列*/
		hideColumn:function(field){
			return $(this).bootstrapTable("hideColumn",field);  
		},
		
		/*切换显示模式*/
		toggleView:function(){
			return $(this).bootstrapTable("toggleView");  
		},
		
		/*切换分页显示模式*/
		togglePagination:function(){
			return $(this).bootstrapTable("togglePagination");  
		},
		
		/*定位页*/
		gotoPage:function(page){
			return $(this).bootstrapTable("selectPage",page);  
		},
		
		/*上一页*/
		prevPage:function(){
			return $(this).bootstrapTable("prevPage");  
		},
		
		/*下一页*/
		nextPage:function(){
			return $(this).bootstrapTable("nextPage");  
		},
		
		/*重置表格*/
		reset:function(options){
			return $(this).bootstrapTable("resetView",options);  
		},
		
		/*获取参数*/
		getOpts:function(){
			return $(this).bootstrapTable("getOptions");
		},
		
		/*删除Bootstrap Table组件*/
		delGrid:function(){
			$(this).bootstrapTable("destroy");
		},
		
		/*批量操作*/
		plcz:function(url,msg){
			var $this = this;
			var keys = $(this).getKeys();
			if (keys.length == 0){
				$.alert('请选择您要'+msg+'的记录！');
			} else {
				$.confirm('您确定要'+msg+'选择的记录吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						jQuery.post(url,{"ids":keys.join(",")},function(responseText){
							setTimeout(function(){
								if(responseText.status == 'success'){
									$.success(responseText.message,function() {
										$($this).refreshGrid();
									});
								}else if(responseText.status == 'fail'){
									$.error(responseText.message);
								} else{
									$.alert(responseText.message);
								}
							},1);
							
						},'json');
						jQuery.ajaxSetup({async:true});
					}
				});
			}
		}
		
	});
	
}(jQuery));