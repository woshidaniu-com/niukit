;(function($){
$.extend($,{
	search: {
		version:'5.0',
		author:'Penghui.Qu'
	}
});

$.extend($.search,{  
 	init:function(){
		//绑定输入框回车事件
		$("#search_blurValue").bind("keyup",function(e){
			if (e.keyCode == 13){
				$("#search_button").click();
			}
		});
		//模糊查询绑定事件
		$("a[name=search_blurType]").click(function(){
			$("#search_blurText").html($(this).html());
			$("#search_blurType").val($(this).attr("key"));
		});
		//删除已选条件
		var _deleteItemEvent=function(){
			var searchName = $(this).attr("searchname");
			var value = $(this).attr("itemvalue");
			//$("a[searchname="+searchName+"][optionvalue="+value+"]").removeClass("cur");
			$("a[searchname="+searchName+"][optionvalue="+value+"]").click();
			$(this).parents("li").remove();
			_checkItemUl();
		}
		//检测已选条件项
		var _checkItemUl=function(obj){
			if ($("#itemUl li").size() == 0){
				$(".panel-heading").hide();
			} else {
				$(".panel-heading").show();
			}
		}
		//已选条件绑定删除事件
		$(".glyphicon-remove").bind("click",_deleteItemEvent);
		//多选项绑定点击事件
		$("a[type=multiOption]").click(function(){
			var searchname=$(this).attr("searchname");
			var searchtext=$(this).attr("searchtext");
			var optionText=$(this).html();
			var optionValue=$(this).attr("optionvalue");
			if ($(this).hasClass("cur")){
				$("i[searchname="+searchname+"][itemvalue="+optionValue+"]").parents("li").remove();
				$(this).removeClass("cur");
			} else {
				var li = $("<li></li>");
				var a = $("<a href='#'><b class='type'>"+searchtext+"：</b>"+optionText+" </a>");
				var i = $("<i class=\"glyphicon glyphicon-remove close-icon\" searchname=\""+searchname+"\" itemvalue=\""+optionValue+"\"></i>");
				
				i.bind("click",_deleteItemEvent);
				a.append(i).appendTo(li);		
				li.appendTo($("#itemUl"));
				$(this).addClass("cur");
			}
			_checkItemUl();
		});
		//单选项绑定点击事件
		$("a[type=radioOption]").click(function(){
			var searchname=$(this).attr("searchname");
			var searchtext=$(this).attr("searchtext");
			var optionText=$(this).html();
			var optionValue=$(this).attr("optionvalue");
			
			$("i[searchname="+searchname+"]").parents("li").remove();
			
			if ($(this).hasClass("cur")){
				$(this).removeClass("cur");
			} else {
				$("a[type=radioOption][searchname="+searchname+"]").removeClass("cur");
				var li = $("<li></li>");
				var a = $("<a href='#'><b class='type'>"+searchtext+"：</b>"+optionText+" </a>");
				var i = $("<i class=\"glyphicon glyphicon-remove close-icon\" searchname=\""+searchname+"\" itemvalue=\""+optionValue+"\"></i>");
				i.bind("click",_deleteItemEvent);
				a.append(i).appendTo(li);		
				li.appendTo($("#itemUl"));
				$(this).addClass("cur");
			}
			_checkItemUl();
		});
		
		//展开、收起条件
		$("#collapse_condition").on("shown.bs.collapse", function () {
			var $a = $("[href='#"+$(this).attr("id")+"']");
			$a.children("span").text(" 收起更多条件");
			$a.children("i").addClass("glyphicon-collapse-up").removeClass("glyphicon-collapse-down");
		});
		$("#collapse_condition").on("hidden.bs.collapse", function () { // 收起更多条件后...
			var $a = $("[href='#"+$(this).attr("id")+"']");
			$a.children("span").text(" 展开更多条件");
			$a.children("i").addClass("glyphicon-collapse-down").removeClass("glyphicon-collapse-up");
		});
		
		$("a[name=search_moreOption]").click(function(){
			if ($(this).find(".fa-angle-double-down").size() > 0){
				$(this).html("<i class='fa fa-angle-double-up'></i> 收起");
				$(this).parents("div[name=search_optionDiv]").find("li[name=search_moreItem]").removeClass("hide");
				$(this).parents("div[name=search_optionDiv]").find("div[name=search_moreDiv]").removeClass("hide");
			} else {
				$(this).html("<i class='fa fa-angle-double-down'></i> 更多");
				$(this).parents("div[name=search_optionDiv]").find("li[name=search_moreItem]").addClass("hide");
				$(this).parents("div[name=search_optionDiv]").find("div[name=search_moreDiv]").addClass("hide");
			}
		});
		
		$("div[type=date]").each(function(i,n){
			var sInput = $(n).find(":input").first();
			var eInput = $(n).find(":input").last();
			var start = {
				  elem: '#'+sInput.attr("id"),
				  format: sInput.attr("format"),
				  max: '2099-06-16 23:59:59', //最大日期
				  istime: true,
				  istoday: false,
				  choose: function(datas){
				     end.min = datas; //开始日选好后，重置结束日的最小日期
				     end.start = datas //将结束日的初始值设定为开始日
				  }
			};
			var end = {
				  elem: '#'+eInput.attr("id"),
				  format: eInput.attr("format"),
				  max: '2099-06-16 23:59:59',
				  istime: true,
				  istoday: false,
				  choose: function(datas){
				    start.max = datas; //结束日选好后，重置开始日的最大日期
				  }
			};
			laydate(start);
			laydate(end);
		});
		
		//重置选项，param:单个条件区域div
		var _resetOption = function(optionDiv,options){
			//1、将该区域选项全部隐藏
			optionDiv.children(":not(.control-label)").addClass("hide");
			//2、删除临时区域
			//optionDiv.find("div[name=relationTemDiv]").remove();
			//3、生成临时区域，将级联数据重新排版布局
			var blockDiv=$('<div name="relationTemDiv"></div>');
			var div = $('<div class="col-sm-10" ></div>');
			var ul = $('<ul class="tag-list"></ul>');
			var length=0;//选项字符串长度
			var moreCount=45;//当length大于此值时使用更多
			var morePinyin=false;//是否显示更多拼音
			var tmp="";
			var index = 0;//循环索引计数器
			
			$.each(options,function(i,n){
				var pinyin = $(n).attr("pinyin");
				if (Boolean(optionDiv.attr("pinyin")) && (i == 0 || tmp != pinyin)){
					index=i;
					if (length>moreCount){
						morePinyin=true;
						return false;
					}
					tmp=pinyin;
					ul.append("<li><span class='index'>"+pinyin+"</span></li>");
					length+=pinyin.length;
				}
				var li = $('<li></li>');
				if (length>moreCount){
					li.attr("name","search_moreItem");
					li.addClass("hide");
				}
				li.append(n);
				ul.append(li);
				length+=$(n).html().length+1;
			});
			div.append(ul);
			blockDiv.append(div);
			if (length>moreCount && options.length > index){
				var label = optionDiv.find("label[name=search_moreLable]");
				label.removeClass("hide");
				label.find("a").html('<i class="fa fa-angle-double-down"></i> 更多');
				blockDiv.append(label);
				if (morePinyin){
					optionDiv.find(".clearfix").removeClass(".hide").appendTo(blockDiv);
					var moreDiv = optionDiv.find("div[name=search_moreDiv]").removeClass(".hide");
					moreDiv.appendTo(blockDiv);
					
					//更多拼音选项
					moreDiv.find(".tab-content>.tab-pane>.tag-list>li>a").addClass("hide");
					$.each(options,function(i,n){
						if (i <= index) return;
						var optionvalue = $(n).attr("optionvalue");
						moreDiv.find(".tab-content>.tab-pane>.tag-list>li>a[optionvalue="+optionvalue+"]").removeClass("hide");
					});
					//更多拼音
					moreDiv.find(".nav-tabs > li").removeClass("active").addClass("hide");
					$.each(options,function(i,n){
						if (i <= index) return;
						var pinyin = $(n).attr("pinyin");
						moreDiv.find(".nav-tabs").find("li[name="+pinyin+"]").removeClass("hide");
						moreDiv.find(".tab-content>.tab-pane").removeClass("active");
					});
					//默认显示第一组更多
					moreDiv.find(".nav-tabs>li").not(".hide").eq(0).addClass("active");
					var pinyin=moreDiv.find(".nav-tabs>li").not(".hide").eq(0).attr("name");
					moreDiv.find(".tab-content>div[pinyin="+pinyin+"]").addClass("active");
				}
			}
			optionDiv.append(blockDiv);
		};
		
		//初始化联动关系
		$("div[name=search_optionDiv][relation=true]").each(function(i,n){
			var keys = $(n).attr("relationKeys").split(",");
			$.each(keys,function(x,key){
				$("a[searchname="+key+"]").bind("click",function(){
					var i_items=[];
					
					$.each(keys,function(key_index,key_v){
						$.merge(i_items,$("#itemUl li i[searchname="+key_v+"]"));
					});
					
					if (i_items.length > 0){
						//已选条件不为空
						var options = [];
						$.each(i_items,function(x,y){
							var searchname=$(y).attr("searchname");
							var itemvalue=$(y).attr("itemvalue");
							//如果已选项有对本区域联动资格
							if ($.inArray(searchname,keys) > -1){
								//本区域的级联子数据
								var items = $(n).find("a[searchname="+$(n).attr("searchname")+"][search_"+searchname+"="+itemvalue+"]");
								if (items.size() > 0){
									$.merge(options,items);
								}
							}
						});	
						_resetOption($(n),options);
					} else {
						//已选条件为空
						var options = $(n).find("a[searchname="+$(n).attr("searchname")+"]");
						_resetOption($(n),$.merge(options,[]));
					}
				});//end bind
			});
		});
	},
	
	getSearchMap:function(){
			var map = {};
			//单选、多选
			var selectType={};
			$("#itemUl li i").each(function(i,n){
				if (selectType[$(this).attr("searchname")]){
					selectType[$(this).attr("searchname")].push($(this).attr("itemvalue"));
				} else {
					selectType[$(this).attr("searchname")]=[$(this).attr("itemvalue")];
				}
			});
			//日期
			var dateType={};
			$("div[type=date]").each(function(i,n){
				var sInput = $(n).find(":input").first();
				var eInput = $(n).find(":input").last();
				var name = $(n).attr("name");
				//兼容default版，多个日期区间
				dateType[name]=[[sInput.attr("format"),sInput.val(),eInput.val()]];
			});
		
			//模糊
			var blurType=$("#search_blurType").val();
			var blurValue = $.trim($("#search_blurValue").val()).split(/\s/);
			var inputType={};
			
			if (blurType == "-1"){
				$("a[name=search_blurType]").each(function(i,n){
					if ($(this).attr("key")!="-1")
					inputType[$(this).attr("key")]=blurValue;
				});
			}else {
				inputType[blurType]=blurValue;
			}
			map["searchModel.dateType"]=JSON.stringify(dateType);
			map["searchModel.inputType"]=JSON.stringify(inputType);
			map["searchModel.selectType"]=JSON.stringify(selectType);
			map["searchModel.inputSqlType"]="0";
			return map;
		}
	})
}(jQuery));