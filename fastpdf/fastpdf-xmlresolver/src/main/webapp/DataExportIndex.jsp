<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@include file="/common/head.ini"%>
		<link rel="stylesheet" href="css/comTab.css" type="text/css" media="screen"/>
		<script type="text/javascript" src="js/mootools.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/compTab.js"></script>
		<script type="text/javascript">
			 window.name = 'edusys';
			 var curIndex;
		//改变条件头
		//tab-view.js中调用此方法
		function tabClickBefore(index) {
			if ($chk(curIndex) && curIndex == index) {
				return 0;
			}
			curIndex = index;
			refreshSelTab(index);
			return 1;
		}
		
		//刷新选中的tab页面
		function refreshSelTab(index,currentPage) {
			switch(index) {
			case 0:
				var pars = "dataUpdXContent.action";
				$("frame"+index).setProperty("src", pars); 
				break;
			case 1:
				var table_name = $("table_name");
				if(table_name["value"]==''){
					alert("请选择要查询的表");
					return false;
				}
				var pars = "";
				if($chk(currentPage)){
					pars = "dataUpdMathchTable.action?table_name="+$("table_name")["value"]+"&currentPage="+currentPage;
				}else{
					pars = "dataUpdMathchTable.action?table_name="+$("table_name")["value"]+"&currentPage=0";
				}
				$("frame"+index).setProperty("src", pars);
				break;
			}
		}
		
			function showTableColumn(obj){
				var select_val = $(obj)["value"];
				//var lybzd = $("lybzd");
				var gxzdtj = $("gxzdtj");
				var gxsjzd = $("gxsjzd");
				//lybzd.empty();
				gxzdtj.empty();
				gxsjzd.empty();
				new Request.JSON({url:"dataUpdColumnMatch.action", 
				data:"table_name="+select_val,
				async:false,
				onFailure:handlerError,
				 onSuccess:function (json, text) {
				 if(json.length>0){
<%--				 	for(var i=0;i<json.length;i++){--%>
<%--					 	var op = new Element('option',{});--%>
<%--						op.set('value', json[i]['column_name']);--%>
<%--						op.set('text', json[i]['comments']);--%>
<%--						lybzd.grab(op);--%>
<%--				 	}--%>
				 	for(var i=0;i<json.length;i++){
					 	var op = new Element('option',{});
						op.set('value', json[i]['column_name']);
						op.set('text', json[i]['comments']);
						gxzdtj.grab(op); 
				 	}
				 	for(var i=0;i<json.length;i++){
					 	var op = new Element('option',{});
						op.set('value', json[i]['column_name']);
						op.set('text', json[i]['comments']);
						gxsjzd.grab(op); 
				 	}
				 }
			}}).send();
		}
		
			//P配置选择的更新条件
		function matchTj(){
			var lybzd = $("lybzd");
			var gxzdtj = $("gxzdtj");
			if(lybzd.getSelected().length==0){
				alert("请选择更新条件对应的字段");
				return false;
			}
			if(gxzdtj.getSelected().length==0){
				alert("请选择更新条件对应的字段");
				return false;
			}
			
			var left_value = lybzd.getSelected()[0].get("text");
			var left_key = lybzd.value;
			var right_value = gxzdtj.getSelected()[0].get("text");
			var right_key = gxzdtj.value;
			if(right_value.indexOf("/")!=-1){
				return false;
			}
			$(gxzdtj.getSelected()[0]).set("text",right_value+"/"+left_value);
			$(gxzdtj.getSelected()[0]).value = left_key+"!#!"+right_key;  
		}
		
		//匹配选择更新字段
		function matchSj(){
			var lybzd = $("lybzd");
			var gxsjzd = $("gxsjzd");
			if(lybzd.getSelected().length==0){
				alert("请选择更新条件对应的字段");
				return false;
			}
			if(gxsjzd.getSelected().length==0){
				alert("请选择更新条件对应的字段");
				return false;
			}
			var left_value = lybzd.getSelected()[0].get("text");
			var left_key = lybzd.value;
			var right_value = gxsjzd.getSelected()[0].get("text");
			var right_key = gxsjzd.value;
			if(right_value.indexOf("/")!=-1){
				return false;
			}
			$(gxsjzd.getSelected()[0]).set("text",right_value+"/"+left_value);
			// alert(gxsjzd.getSelected()[0].value);
			 $(gxsjzd.getSelected()[0]).value = left_key+"!#!"+right_key;
		}
		
		//取消匹配
		function restMatch(){
			var gxzdtj_options = $("gxzdtj").options;
			var gxsjzd_options = $("gxsjzd").options;
			$A(gxzdtj_options).each(function(item,index){
				if($(item).get("text").indexOf("/")!=-1){
					var temp_str = $(item).get("text");
					$(item).set("text",temp_str.substr(0,temp_str.indexOf("/")));
					$(item).value = $(item).value.substr($(item).value.lastIndexOf ("!")+1,$(item).value.length);
				}
			});
			$A(gxsjzd_options).each(function(item,index){
				if($(item).get("text").indexOf("/")!=-1){
					var temp_str = $(item).get("text");
					$(item).set("text",temp_str.substr(0,temp_str.indexOf("/")));
					$(item).value = $(item).value.substr($(item).value.lastIndexOf ("!")+1,$(item).value.length);
				}
			});
		}
		
		//判断上传的格式
		 function checkfiletype() {
		   if ($('importFile').value.trim() != '') {
		       var filename = $('importFile').value.trim().toLowerCase();
		       if (filename.lastIndexOf('.xls') > -1) {
		          $("form1").submit();
		       } else {
		           alert('此文件格式不支持，请选择.xls的文件');
		       }
		   } else {
		      alert('要导入的文件不能为空！');
		   }	
		 }	
		
			 
		 function importData() {
		    var columnmatch = $("gxzdtj"); 
		    var columnupdate = $("gxsjzd");
		    var para = "table_name=" + $("table_name").value; 
		    var array = $A(columnmatch.options);
		    var array_update = $A(columnupdate.options);
		    if(!checkConditon(array)){
		    	alert("更新条件不能为空");
		    	return false;
		    }
		    if(!checkConditon(array_update)){
		    	alert("更新字段不能为空");
		    	return false;
		    }
		    if(!checkPrimay(array)){
		    	alert("缺少唯一条件");
		    	return false;
		    }
		    array.each(function(item,index){
		    	if($(item).value.indexOf("#")!=-1){
		    		para += '&data=' + item.value;
		    	}
		    });
		     array_update.each(function(item,index){
		    	if($(item).value.indexOf("#")!=-1){ 
		    		para += '&data_upd=' + item.value;
		    	}
		    });
		      new Request.JSON({
		    	url: 'dataUpdImport.action',
		    	data: para,
		    	async: false,
		    	onFailure: handlerError,
				onSuccess: function(json, text) {
					alert(text);
				}
		    }).send();
         }
         
         //判断是否有条件
         function  checkConditon(obj){
         	var temp_count = 0;
         	obj.each(function(item,index){
         		if($(item).value.indexOf("#")!=-1){
         			temp_count++;
         		}
         	});
         	if(temp_count>0){
         		return true;
         	}else{
         		return false;  
         	}
         }
         
         //判断是否主键已经选择
         function checkPrimay(obj){
         	var table_name = $("table_name").value; 
         	var url="dataUpdMathchTablePrkey.action";
         	var pars = "table_name="+table_name;
         	var temp_count = 0;
         	new Request({
					url:url,
					async:false,
					data: pars,
					onSuccess: function(text,xml){
						obj.each(function(item,index){
							if($(item).value.indexOf("!#!")!=-1){
								if($(item).value.indexOf(text)!=-1){
									temp_count++;
								}
							}
						});
					}
				}).send();
			if(temp_count>0){
				return true;
			}else{
				return false;
			}
         }
		</script>
		<style>
.opencon {
	background: #F3F7F6;
}

.formlist01 {
	width: 98% !important;
	margin: 2px auto;
}

.formlist01 caption {
	padding: 0.5em 0.6em;
	text-align: left;
	font-weight: bold;
	color: #5F7485;
	border: 1px solid #C1D1DB;
	background: #DDEFF3;
}

.formlist01 th {
	border: 1px solid #C1D1DB;
	background: #f6fafb;
	color: #5F7485;
	padding: 0.5em 0.6em;
}

.formlist01 td {
	color: #5F7485;
}

.openchoose01 {
	border-collapse: collapse;
	border: 1px solid #C1D1DB
}

.openchoose01 caption {
	text-align: center;
}

.openchoose01 td,.openchoose01 th {
	text-align: left;
	border: 1px solid #BAD4C8;
}

.openchoose01 th,.openchoose01 td {
	border-top: 0;
	padding: 0.5em 0.6em;
	font-weight: normal;
}

.openchoose01 p {
	padding: 3px 0px 3px 15px;
}

.opendiv {
	width: 80%;
	margin: 20px auto;
	min-height: 280px;
	_height: 280px;
	border: 1px solid #7F9DB9;
	background: #fff;
}

.cholist {
	padding: 3px 0;
	text-align: left;
}
</style>
	</head>
	<body>
		<div class="tab">
			<p class="location">
				<em>当前位置 - 系统维护 - 基础数据更新 - 数据更新</em>
			</p>
		</div>
		<div class="open_win">
			<span class="opencon">
				<table align="center" class="formlist01" border="0">
					<tr>
						<td align="center" valign="top" style="width: 230px">
							<s:form target="edusys" id="form1" action="dataUpdColumnCompare" enctype="multipart/form-data" method="post" theme="simple">
							<table align="center" class="openchoose01" width="98%"
								style="margin: 0 auto;">
								<caption>
									数据来源表
								</caption>
								<tr style="width:140px;">
									<th align="left">
										<p class="cholist">
											<input type="file" name="importFile" id="importFile" style="width:50px;" onchange="checkfiletype()"/>
										</p>
								    </th>
								 </tr>
								 <tr>
								 	<th>
										<p class="cholist" style="width: 140px;">
											<label style="width: 140px;">
												来源字段/字段名称
											</label>
										</p>
									</th>
								</tr>
								<tr>
									<td>
										<div class="opendiv">
											 <select id="lybzd" name="mbzd" size="18" style="width:100%;height:100%;">
           						  			 	 <s:iterator value="filecolumnname">
								                  <option value="<s:property value="index"/>"><s:property value="content"/></option>
								                </s:iterator>
           						  			 </select>
										</div>
									</td>
								</tr>
							</table>
							</s:form>
						</td>
						<td align="center" valign="middle" style="width: 100px;">
							<div class="btn_choose">
								<button class="allright" onclick="matchTj()">匹配条件</button>
								<br />
								<br />
								<br />
								<br />
								<button class="right" onclick="matchSj()">匹配数据</button>
								<br />
								<br />
								<br />
								<br />
								<button class="left" onclick="restMatch()">取消匹配</button>
								<br />
								<br />
								<br />
								<br />
								<button class="allleft"  onclick="importData()">更新数据</button>
								<br />
								<br />
								<br />
								<br />
							</div>
						</td>
						<td align="center" valign="top" height="100px;">
							<table align="center" class="openchoose01" width="98%" 
								style="margin: 0 auto;">
								<caption>
									数据更新表
								</caption>
								<tr>

									<th align="left">
										<p class="cholist">
											<s:select list="columnlist" listKey="column_name" 
											listValue="comments" headerKey="" headerValue="请选择要更新的表"
											 onchange="showTableColumn(this)" cssStyle="width:140px;" id="table_name">
											</s:select>
										</p>
									</th>
								</tr>
								<tr>
									<th>
										<p class="cholist">
											<label>
												更新字段   源字段/对应字段
											</label>
										</p>
									</th>

								</tr>
								<tr>
									<td>
										<div class="opendiv_" style="height: 140px;">
											 <select id="gxzdtj" name="gxzdtj" size="18" style="width:100%;height:100%;">
           									  </select>
										</div>
									</td>
								</tr>
								<tr>
									<th>
										<p class="cholist">
											<label>
												数据字段   源字段/对应字段
											</label>
										</p>
									</th>

								</tr>
								<tr>
									<td height="100px;">
										<div class="opendiv_" style="height:140px;">
											 <select id="gxsjzd" name="gxsjzd" size="18"  style="width:100%;height:100%;" >
           									  </select>
										</div>
									</td>
								</tr>
							</table>
						</td>
							<td align="center" valign="top" colspan="2">
							<table align="center" class="openchoose01" width="98%"
								style="margin: 0 auto;">
								<caption>
									查看信息数据
								</caption>
								<tr>
									<td>
										<div class="compTab">
											<div class="comp_title">
								  				<ul>
								  					<li class="ha"><a><span>来源表数据</span></a></li>
								  					<li><a><span>更新表数据</span></a></li>
								  				</ul>
								  			</div>
								  			
								  			<div class="comp_con" style="height: 370px">
								  				<iframe id="frame0" class="rightcontiframe" frameborder="0" marginwidth="0" marginheight="0" width="100%" height="320" scrolling="auto"
																	style="overflow-x: hidden;z-index: -999" src=""></iframe>
								  			</div>
								  			<div class="comp_con" style="height: 370px;display: none;">
								  				<iframe id="frame1" class="rightcontiframe" frameborder="0" marginwidth="0" marginheight="0" width="100%" height="320" scrolling="auto"
																	style="overflow-x: hidden;" src="">
												</iframe>
								  			</div>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table> </span>
				<script type="text/javascript">
						//initTabs('init_frame',Array('专业班级设置','班级代码规则设置','学号规则设置','班级名称简称规则设置','班级名称规则设置'),'0','100%','330');
						initTabs(); 
						tabClickBefore(0);
				</script>
		</div>
	</body>
</html>
