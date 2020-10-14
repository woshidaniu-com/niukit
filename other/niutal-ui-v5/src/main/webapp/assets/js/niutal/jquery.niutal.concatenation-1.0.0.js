;(function($){
	
	function clearStyle(element){
		if($.fn.simpleValidate){
			$(element).getRealElement().successClass();
		}
	}
	
	function clearNull(map,mapper,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id){
		//部门ID存在
		if($.founded(jg_id)&&$(jg_id).defined()){
			map[getKey(jg_id,mapper)] = $(jg_id).val();
		}else{
			map[getKey(jg_id,mapper)] = null;
		}
		//系ID存在
		if($.founded(xx_id)&&$(xx_id).defined()){
			map[getKey(xx_id,mapper)] = $(xx_id).val();
		}else{
			map[getKey(xx_id,mapper)] = null;
		}
		//专业ID存在
		if($.founded(zy_id)&&$(zy_id).defined()){
			map[getKey(zy_id,mapper)] = $(zy_id).val();
		}else{
			map[getKey(zy_id,mapper)] = null;
		}
		//专业方向ID存在
		if($.founded(zyfx_id)&&$(zyfx_id).defined()){
			map[getKey(zyfx_id,mapper)] = $(zyfx_id).val();
		}else{
			map[getKey(zyfx_id,mapper)] = null;
		}
		//班级ID存在
		if($.founded(bh_id)&&$(bh_id).defined()){
			map[getKey(bh_id,mapper)] = $(bh_id).val();
		}else{
			map[getKey(bh_id,mapper)] = null;
		}
		//年级ID存在
		if($.founded(njdm_id)&&$(njdm_id).defined()){
			map[getKey(njdm_id,mapper)] = $(njdm_id).val();
		}else{
			map[getKey(njdm_id,mapper)] = null;
		}
	};
	
	function getKey(element,mapper){
		var name = $(element).attr("name");
		return $.founded(mapper[name])?mapper[name]:name;
	}
	
	function funcFilter(mapObj,mapper){
		var newMap = {};
		$.each(mapObj||{}, function(key, val){
			if($.founded(key)&&$.founded(val)&&(!$.isNumeric(key))){
				newMap[key] = val;
			}
		});
		/*delete not need params*/
		$.each(["mapper","selectAttr","height","title","width"], function(i, key){
			delete newMap[key];
		});
		return newMap;
	};
	
	function initDefaultMap(map,params,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id){
		var newMap = {};
		if($.founded(params)){
			var mapper = params.mapper||{};
			//学院ID存在
			if($.founded(jg_id)&&$(jg_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(jg_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			//系ID存在
			if($.founded(xx_id)&&$(xx_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(xx_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			//专业ID存在
			if($.founded(zy_id)&&$(zy_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(zy_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			//专业方向ID存在
			if($.founded(zyfx_id)&&$(zyfx_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(zyfx_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			//班级ID存在
			if($.founded(bh_id)&&$(bh_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(bh_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			//年级ID存在
			if($.founded(njdm_id)&&$(njdm_id).defined()){
				//取得此ID名称对应的属性值
				var name = getKey(njdm_id,mapper);
				var val = params[name];
				//判断值存在
				if($.defined(val)){
					delete params[name];
					params[name] = val;
				}
			}
			$.each(params,function(key,val){
				map[key] = val;
				newMap[key] = val;
			});
		}
		return newMap;
	};
	
	$.extend({
		/**
		 * 描述：绑定学院，专业，班级，年级级联事件
		 * 参数：
		 * 		jg_id	： 学院下拉框ID,
		 * 		xx_id	： 系下拉框ID,
		 * 		zy_id	： 专业下拉框ID,
		 * 		zyfx_id	： 专业方向下拉框ID,
		 * 		bh_id	： 班级下拉框ID,	
		 * 		njdm_id	： 年级下拉框ID,
		 * 		params	： 额外的请求参数,可传默认值
		 * 		func	： 回调函数
		 * 
		 * */
		bindChangeEvent:function(jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id,params,func,headerValue){
			var $this = this;
			var dataMap = {};
			params 		= $.defined(params)?params:{};
			func 		= ($.defined(func)&& $.isFunction(func)) ?func:$.noop;
			headerValue = $.defined(headerValue)?headerValue:"全部";
			var mapper = params.mapper||{};
			//初始化默认参数
			var defaultMap = initDefaultMap(dataMap,params,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id);
			//加载系
			function loadXx(){
				if($.founded(xx_id)&&$(xx_id).defined()){
					clearNull(dataMap,mapper,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id);
					var html = [];
				    html.push('<option value="">'+headerValue+'</option>');
					//远程获取数据
				    jQuery.ajaxSetup({async:false});
					$.getJSON(_path+"/xtgl/comm_cxXxdmList.html",funcFilter.call(this,dataMap,mapper), function(data){
						if($.founded(data)){
							$.each(data,function(i,rowObj){
								//jg_id; // 机构id
								//jgdm; // 机构代码
								//jgmc; // 机构名称
								var selectedStr = "",name =  getKey(xx_id,mapper); 
								if($.founded(defaultMap[name])&&defaultMap[name]==rowObj.jg_id){
									selectedStr = ' selected="selected" ';
									loadBj();
								}
								html.push('<option value="'+rowObj.jg_id+'" '+selectedStr+'>'+rowObj.jgmc+'</option>');
							});
						}
					});
					$(xx_id).empty().append(html.join("")).trigger("chosen:updated");
					jQuery.ajaxSetup({async:true});
				}
			};
			//加载专业
			function loadZy(){
				//专业
				if($.founded(zy_id)&&$(zy_id).defined()&&$(zy_id).is("select")){
					//清除专业方向原有选项
					if($.founded(zyfx_id)&&$(zyfx_id).defined()&&$(zyfx_id).is("select")){
						$(zyfx_id).find("option:gt(0)").remove();
					}
					//清除班级原有选项
					if($.founded(bh_id)&& $(bh_id).defined()&&$(bh_id).is("select")){
						$(bh_id).find("option:gt(0)").remove();
					}
					clearNull(dataMap,mapper,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id);
					var html = [];
				    html.push('<option value="">'+headerValue+'</option>');
					//远程获取数据
				    jQuery.ajaxSetup({async:false});
				    $.getJSON(_path+"/xtgl/comm_cxZydmList.html",funcFilter.call(this,dataMap,mapper), function(data){
						if($.founded(data)){
							$.each(data,function(i,rowObj){
								//zyh_id; // 专业号ID
								//zyh; // 专业代码
								//zymc; // 专业名称
								var selectedStr = "",name = getKey(zy_id,mapper); 
								if($.founded(defaultMap[name])&&defaultMap[name]==rowObj.zyh_id){
									selectedStr = ' selected="selected" ';
									loadZyfx();
									loadBj();
								}
								html.push('<option value="'+rowObj.zyh_id+'" '+selectedStr+'>'+rowObj.zymc+'</option>');
							});
						}
					});
					$(zy_id).empty().append(html.join("")).trigger("chosen:updated");
					jQuery.ajaxSetup({async:true});
				}
			};
			//加载专业方向
			function loadZyfx(){
				//专业方向
				if($.founded(zyfx_id)&&$(zyfx_id).defined()&&$(zyfx_id).is("select")){
					clearNull(dataMap,mapper,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id);
					var html = [];
				    html.push('<option value="">'+headerValue+'</option>');
					//远程获取数据
				    jQuery.ajaxSetup({async:false});
				    $.getJSON(_path+"/xtgl/comm_cxZyfxList.html",funcFilter.call(this,dataMap,mapper), function(data){
						if($.founded(data)){
							$.each(data,function(i,rowObj){
								//zyfx_id; // 专业方向ID
								//zyfxmc; // 专业方向名称
								var selectedStr = "",name =  getKey(zyfx_id,mapper);
								if($.founded(defaultMap[name])&&defaultMap[name]==rowObj.zyfx_id){
									selectedStr = ' selected="selected" ';
									loadBj();
								}
								html.push('<option value="'+rowObj.zyfx_id+'" '+selectedStr+'>'+rowObj.zyfxmc+'</option>');
							});
						}
					});
					$(zyfx_id).empty().append(html.join("")).trigger("chosen:updated");
					jQuery.ajaxSetup({async:true});
				}
			};
			//加载班级
			function loadBj(){
				//班级
				if($.founded(bh_id)&&$(bh_id).defined()&&$(bh_id).is("select")){
					clearNull(dataMap,mapper,jg_id,xx_id,zy_id,zyfx_id,bh_id,njdm_id);
					var html = [];
				    html.push('<option value="">'+headerValue+'</option>');
					//远程获取数据
				    jQuery.ajaxSetup({async:false});
					$.getJSON(_path+"/xtgl/comm_cxBjdmList.html",funcFilter.call(this,dataMap,mapper), function(data){
						if($.founded(data)){
							$.each(data,function(i,rowObj){
								//bh_id; // 班号ID
								//bh; // 班号
								// bj; // 班级
								var selectedStr = "",name =  getKey(bh_id,mapper); 
								if($.founded(defaultMap[name])&&defaultMap[name]==rowObj.bh_id){
									selectedStr = ' selected="selected" ';
								}
								html.push('<option value="'+rowObj.bh_id+'" '+selectedStr+'>'+rowObj.bj+'</option>');
							});
						}
					});
					$(bh_id).empty().append(html.join("")).trigger("chosen:updated");
					jQuery.ajaxSetup({async:true});
				}
			};
			
			//判断年级是否存在
			if($.founded(njdm_id)&& $(njdm_id).defined()&&$(njdm_id).is("select")){
				var name = getKey(njdm_id,mapper);
				//当年级有默认值
				if($.founded($(njdm_id).val())){
					dataMap[name] = $(njdm_id).val();
				}
				//绑定年级切换事件
				$(njdm_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
					loadBj();
					func.call($this,funcFilter.call(this,dataMap,mapper));
				});
			}
			//判断学院是否存在
			if($.founded(jg_id)&& $(jg_id).defined()&&$(jg_id).is("select")){
				var name = getKey(jg_id,mapper);
				//当学院有默认值
				if($.founded($(jg_id).val())){
					dataMap[name] = $(jg_id).val();
					loadXx();
					loadZy();
				}
				//绑定学院切换事件
				$(jg_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
					loadXx();
					loadZy();
					loadZyfx();
					loadBj();
					func.call($this,funcFilter.call(this,dataMap,mapper));
				});
			}
			//判断系是否存在
			if($.founded(xx_id)&& $(xx_id).defined()&&$(xx_id).is("select")){
				var name = getKey(xx_id,mapper);
				//当系有默认值
				if($.founded($(xx_id).val())){
					dataMap[name] = $(xx_id).val();
					loadZy();
				}
				//绑定系切换事件
				$(xx_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
				});
			}
			//判断专业是否存在
			if($.founded(zy_id)&& $(zy_id).defined()&&$(zy_id).is("select")){
				var name = getKey(zy_id,mapper);
				//当专业有默认值
				if($.founded($(zy_id).val())){
					dataMap[name] = $(zy_id).val();
					loadZyfx();
					loadBj();
				}
				//绑定专业切换事件
				$(zy_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
					loadZyfx();
					loadBj();
					func.call($this,funcFilter.call(this,dataMap,mapper));
				});
			}
			//判断专业方向是否存在
			if($.founded(zyfx_id)&& $(zyfx_id).defined()&&$(zyfx_id).is("select")){
				var name = getKey(zyfx_id,mapper);
				//当专业方向有默认值
				if($.founded($(zyfx_id).val())){
					dataMap[name] = $(zyfx_id).val();
				}
				//绑定专业方向切换事件
				$(zyfx_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
					func.call($this,funcFilter.call(this,dataMap,mapper));
				});
			}
			//判断班级是否存在
			if($.founded(bh_id)&& $(bh_id).defined()&&$(bh_id).is("select")){
				var name = getKey(bh_id,mapper);
				//当班级有默认值
				if($.founded($(bh_id).val())){
					dataMap[name] = $(bh_id).val();
				}
				//绑定班级切换事件
				$(bh_id).unbind("change").change(function(){
					if($.founded($(this).val())){
						dataMap[name] = $(this).val();
						clearStyle(this);
					}else{
						dataMap[name] = null;
					}
					func.call($this,funcFilter.call(this,dataMap,mapper));
				});
			}
		}
	});
	
}(jQuery));