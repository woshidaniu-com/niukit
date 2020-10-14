<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@include file="/common/head.ini"%>
		<script type="text/javascript" src="${ base }js/mootools.js"></script>
		<script type="text/javascript" src="${ base }js/common.js"></script>
		<script type="text/javascript">
		 window.name = "edusys";
		 //判断文件格式是否xls结尾
		 function checkfiletype() {
		   if ($("importFile").value.trim() != "") {
		       var filename = $("importFile").value.trim().toLowerCase();
		       if (filename.lastIndexOf(".xls") > -1) {
		           $("__waiting").setStyle("visibility", "visible");
		           $("form1").submit();
		       } else {
		           alert("此文件格式不支持，请选择.xls的文件");
		       }
		   } else {
		      alert("要导入的文件不能为空！");
		   }
		 }
		 /*自动匹配*/
		 function autopp() {
		 	var yzd = $("yzd").getChildren(), mbzd = $("mbzd").getChildren(), columnmatch = $("columnmatch");
		 	columnmatch.empty();
		 	for (var i = 0;i < yzd.length;i++) {
		 		var _option_0 = yzd[i];
		 		for (var j = 0;j < mbzd.length;j++) {
		 			var _option_1 = mbzd[j];
		 			var yzdmc = _option_0["lang"].replace("(必填,唯一)", "").replace("(必填)", "").replace("(唯一)", "");
		 			if (yzdmc == _option_1["lang"]) {
		 				var option = new Element("option", {});
       					option.set("value", _option_0.value + "!#!" + _option_1.value);
       					option.set("text", _option_0["lang"] + " --> " + _option_1["lang"]);
       					option.requisite = _option_1.requisite;
       					if (option.requisite == 1) {
		           			option.setStyle("color","#FF0000");
		       			}
       					columnmatch.grab(option, "bottom");
		 			}
		 		}
		 	}
		 	return false;
		 }
		 
		 /*根据表名更改目标字段*/
		 function changeColumn(table, id, id2) {
		 	var mbzd = $(id), ele = $(id2);
		 	mbzd.empty();
		 	ele.empty();
		 	if (table != '') {
		 		new Request.JSON({
		 			url: 'dataImpColumnMatch.action',
		 			data: {'primarykey': table},
		 			async: false,
		 			onFailure: handlerError,
		 			onSuccess: function(json, text) {
		 				if (json) {
		 					json.each(function(item){
		 						var opt = new Element('option',{"value":item["column_name"], 
		 							"text":item["comments"],"lang":item["comments"]});
		 						if ($chk(item['requisite']) && item['requisite'] == '1') {
		 							opt.setStyle('color','#FF0000');
		 							opt.requisite = 1;
		 						} else {
		 							opt.requisite = 0;
		 						}
		 						this.grab(opt, "bottom");
		 					}, mbzd);
		 				}
		 			}
		 		}).send();
		 	}
		 }
		 
		 //增加字段匹配
		 function addData() {
		   var yzd = $('yzd'), mbzd = $('mbzd'), columnmatch = $('columnmatch');
		   if (yzd.value != '' && mbzd.value != '') {
		       var option = new Element('option', {});
       		   option.set('value', yzd.value + '!#!' + mbzd.value);
		       option.set('text', yzd.getSelected()[0]['lang'] + ' --> ' + mbzd.getSelected()[0]['lang']);
		       option.requisite = mbzd.getSelected()[0].requisite;
		       if (option.requisite == 1) {
		           option.setStyle('color','#FF0000');
		       }
		       columnmatch.grab(option, 'bottom');
		        var columnNames = columnCheck();
				if(columnNames.length>0){
					$("unUniqueColumns_em1").setStyle("color", "red");
					$("unUniqueColumns_em1").set("text", "提示：存在重复冲突字段!");
					$("unUniqueColumns_em2").set("text", columnNames.join(","));
				}else{
					$("unUniqueColumns_em1").setStyle("color", "green");
					$("unUniqueColumns_em1").set("text", "提示：无重复冲突字段，已可进行导入操作！");
					$("unUniqueColumns_em2").set("text", "");
				}
		   }
		}
		
		//删除字段匹配
		function removeData() {
		    var option = $('columnmatch').getSelected()[0];
		    if ($chk(option)) {
		       	option.destroy();
		       	var columnNames = columnCheck();
				if(columnNames.length>0){
					$("unUniqueColumns_em2").set("text", columnNames.join(","));
				}else{
					$("unUniqueColumns_em1").setStyle("color", "green");
					$("unUniqueColumns_em1").set("text", "提示：无重复冲突字段，已可进行导入操作！");
					$("unUniqueColumns_em2").set("text", "");
				}
		    }else{
		    	alert("请选择要删除的字段!");
		    }
		}
		
		/*重置匹配字段*/
		function resetcolumnmatch() {
		   	$('columnmatch').empty();
		   	$("rownum").set("text", '0');
			$("addedrownum").set("text",  '0');
			$("notaddedrownum").set("text", '0');
		   	$$(".importResultMsg").setStyle("display", "none");
		   	$("unUniqueColumns_em1").set("text", "");
		    //$("sureBt").setStyle("disabled", ""); 
		}
		
		function columnCheck() {
			var array = $("columnmatch").getChildren();
			var columnDms = new Array();
			var columnNames = new Array();
	    	array.each(function(item, index){
	    		var columnDm = item.value.split("!#!")[1];
	    		var columnName = item.text.split("-->")[0];
	    		var isRepet = false;
	    		for(var i=0;i<columnDms.length;i++){
		    		if(columnDms[i]==columnDm){
		    			isRepet = true;
	    			}
	    		}
	    		if(isRepet){
	    			var isHave = false;
		    		for(var i=0;i<columnNames.length;i++){
			    		if(columnNames[i]==columnName){
			    			isHave = true;
		    			}
		    		}
		    		if(!isHave){
		    			columnNames.push(columnName);
		    		}
	    		}else{
	    			columnDms.push(columnDm);
	    		}
	    	});
	    	return columnNames;
		}
		
		/*最后导入数据*/
		function importData() {
			//先判断字段匹配列表是否为空
			if ($("columnmatch").getChildren().length == 0) {
				alert("\u5b57\u6bb5\u5339\u914d\u5217\u8868\u4e0d\u80fd\u4e3a\u7a7a\uff01");
				return false;
			}
			var columnNames = columnCheck();
			if(columnNames.length>0){
				alert("【"+columnNames.join(",")+"】字段重复，请先进行处理！");
				return;
			}
			$("unUniqueColumns_em1").set("text", "提示：正在导入，请稍等……！");
    		$("unUniqueColumns_em1").setStyles({"display":"block","color":"black"});
    		//$("sureBt").setStyle("disabled", "disabled");
    		setTimeout(function(){
				if (checkBefore()) {
					var para = "table_name=" + euc($("table_name").value), columnmatch = $("columnmatch");
					array = columnmatch.getChildren();
			    	array.each(function(item, index){
			    		para += "&column_name=" + euc(item.value);
			    	});
			    	para += "&main_column=" + euc($("main_column").value);
			    	new Request.JSON({
				    	url: "${base}dataImport.action",
				    	data: para,
				    	async: true,
				    	onFailure: handlerError,
				    	onRequest:function(instance){
				    		$$(".button").each(function(bt,index){
				    			$(bt).set("disabled","disabled");
				    		});
				    	},
						onSuccess: function(json, text) {
							$("closeBt").set("disabled","");
							//判断是否有必填字段为空现象
							if($chk(json["requiredNumeric"])){
								var requiredNumerics = [];
								for(var key in json["requiredNumeric"]){
									requiredNumerics.push(json["requiredNumeric"][key])
								}
								$("unUniqueColumns_em1").setStyle("color", "red");
								$("unUniqueColumns_em1").set("text", "提示：必须匹配字段【"+requiredNumerics.join(",")+"】存在空值，请检查Excel文件数据！");
								alert( "提示：必须匹配字段【"+requiredNumerics.join(",")+"】存在空值，请检查Excel文件数据！");
							}else{
								//判断是否有违反唯一约束现象
								if(json["unUniqueNum"]&&Number(json["unUniqueNum"])>0){
									$("unUniqueColumns_em1").setStyle("color", "red");
									$("unUniqueColumns_em1").set("text", "提示：部分数据在数据库已经存在或者违反唯一约束，请删除Excel文件中重复数据或者删除数据库原有数据！");
								}else{
									$("unUniqueColumns_em1").setStyle("color", "green");
									$("unUniqueColumns_em1").set("text", "提示：数据导入操作完成，以下为导入结果信息！");
								}
								$("rownum").set("text", json["rownum"]);
								$("addedrownum").set("text", json["addedrownum"]);
								$("notaddedrownum").set("text", json["notaddedrownum"]);
								$$(".importResultMsg").setStyle("display", "");
								if(Number(json["notaddedrownum"])!=0){
									if (confirm("是否要查看未导入的信息？")) {
										window.open("${base}dataImpFailuredRecords.action");
									}
								}
							}
						}
				    }).send();
				} else {
					$("unUniqueColumns_em1").setStyle("color", "red");
					$("unUniqueColumns_em1").set("text", "提示：红色的字段必须匹配！");
					alert("红色的字段必须匹配！");
				}
			},1000);
		    /*var para = 'table_name=' + euc($('table_name').value), columnmatch = $('columnmatch');
		    var array = columnmatch.getChildren();
		    array.each(function(item, index){
		    	para += '&column_name=' + euc(item.value);
		    });
		    para += '&main_column=' + $('main_column').value;
		    new Request.JSON({
		    	url: 'dataImport.action',
		    	data: para,
		    	async: false,
		    	onFailure: handlerError,
				onSuccess: function(json, text) {
					$('drxx').setStyle('display', '');
					var text = '总共有' + json['rownum'] + '条数据，成功导入了' + json['notaddedrownum'] + '条数据';
					$$('#drxx td')[0].set('text', text);
					if (json['notAddedDataList'].length > 0) {
						$$('#wdrsj')[0].setStyle('display', '');
						var html = [];
						for (var i = 0, l = json['notAddedDataList'].length;i < l;i++) {
							html.push('<tr>');
							var data_array = json['notAddedDataList'][i];
							for (var k = 0,le = data_array.length;k < le;k++) {
								html.push('<td>');
								html.push(data_array[k]);
								html.push('</td>');
							}
							html.push('</tr>');
						}
						$$('#wdrsj tbody')[0].set('html', html.join(''));
					} else {
						$$('#wdrsj')[0].setStyle('display', 'none');
					}
				}
		    }).send();*/
		}
		function checkBefore() {
			var count_1 = 0, count_2 = 0;
			$('mbzd').getChildren().each(function(item){
				if (item.requisite == 1) {
					count_1++;
				}
			});
			$('columnmatch').getChildren().each(function(item){
				if (item.requisite == 1) {
					count_2++;
				}
			});
			return count_1 == count_2;
		}
	</script>
	<style type="text/css">
		.download_a{float: left; margin-left:5xp;text-indent: 1em; width: auto; line-height: 25px; height: 25px;text-decoration: underline;color: blue;}
	</style>
</head>
<body>
<div class="tab">
	<span class="opencon"> 
		<s:if test="firstLoad">
			<s:form target="edusys" id="form1" action="dataImpColumnCompare" enctype="multipart/form-data" method="post" theme="simple">
				<s:hidden name="main_column" id="main_column" />
				<s:iterator value="table" var="tn">
					<s:hidden name="table" value="%{tn}" />
				</s:iterator>
				<table class="formlist" style="width: 100%;">
					<thead>
						<tr>
							<td colspan="2">
								<span>数据导入</span>
							</td>
						</tr>
					</thead>
					<tr>
						<td align="right" style="font-weight: bolder; font-size: 14px;width: 30%;">
							<b>数据模板：</b>
						</td>
						<td align="left" width="">
							<s:iterator value="table_list" >
								<a href="dataImpTemplateDownload.action?table_name=<s:property value="key"/>" class=" download_a">【<s:property value="value"/>】导入模板</a>
							</s:iterator>
						</td>
					</tr>
					<tr>
						<td align="right" style="font-weight: bolder; font-size: 14px;width:30%;">
							<b>选择文件：</b>
						</td>
						<td align="left" >
							<s:file id="importFile" name="importFile" cssClass="text_nor" cssStyle="width:500px;line-height:25px;height:25px;"></s:file>
						</td>
					</tr>
					<tr style="visibility: hidden;" id="__waiting">
						<td align="center" style="height: 300px" colspan="2">
							<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
								width="100" height="100" id="FlashID" title="loading">
								<param name="movie" value="style/jwglxt/images/loading02.swf" />
								<param name="quality" value="high" />
								<param name="wmode" value="transparent" />
								<param name="swfversion" value="8.0.35.0" />
								<!-- 此 param 标签提示使用 Flash Player 6.0 r65 和更高版本的用户下载最新版本的 Flash Player。如果您不想让用户看到该提示，请将其删除。 -->
								<!-- 下一个对象标签用于非 IE 浏览器。所以使用 IECC 将其从 IE 隐藏。 -->
								<!--[if !IE]>-->
								<object type="application/x-shockwave-flash"
									data="style/jwglxt/images/loading02.swf" width="100"
									height="100">
									<!--<![endif]-->
									<param name="quality" value="high" />
									<param name="wmode" value="transparent" />
									<param name="swfversion" value="8.0.35.0" />
									<!-- 浏览器将以下替代内容显示给使用 Flash Player 6.0 和更低版本的用户。 -->
									<div>
									</div>
									<!--[if !IE]>-->
								</object>
								<!--<![endif]-->
							</object>
							<p>
								正在上传，请稍等……
							</p>
						</td>
					</tr>
					<tfoot>
						<tr>
							<td colspan="2">
								<div class="btn">
									<button onclick="javascript:checkfiletype()" type="button">
										上传
									</button>
									<button onclick="javascript:window.close()" type="button">
										关闭
									</button>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</s:form>
		</s:if> 
		<s:else>
			<s:hidden name="main_column" id="main_column" />
			<table class="formlist" width="100%;">
				<tr>
					<th style="width: 33%">
						源字段
					</th>
					<th colspan="2">
						目标字段
						<em class="red">（红色表示必须匹配的字段）</em>
					</th>
					<th style="width: 33%">
						字段匹配列表
					</th>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						数据表
					</td>
					<td>
						<s:select id="table_name"
							onchange="changeColumn(this.value,'mbzd','columnmatch')"
							list="tablecolumnname" listKey="key" listValue="value"
							emptyOption="true" />
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="center">
						<select id="yzd" name="yzd" size="18" style="width: 15em"
							ondblclick="addData()">
							<s:iterator value="filecolumnname">
								<option value="<s:property value="key"/>"
									lang="<s:property value="value"/>">
									<s:property value="value" />
								</option>
							</s:iterator>
						</select>
					</td>
					<td align="center" colspan="2">
						<select id="mbzd" name="mbzd" size="18" style="width: 15em"
							ondblclick="addData()">

						</select>
					</td>
					<td align="center">
						<select id="columnmatch" name="columnmatch" size="18"
							style="width: 15em">
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left">
						<button onclick="javascript:addData();" type="button" id="addBt">
							+
						</button>
						<button onclick="javascript:removeData()" type="button"
							id="deleteBt">
							-
						</button>
						<button onclick="javascript:autopp()" type="button"
							id="autoPipeiBt">
							自动匹配
						</button>
					</td>
				</tr>
				<tr align="left" id="unUniqueColumns_tr">
					<td colspan="4" align="left" style="text-align: left;">
						<em style="color: red;" id="unUniqueColumns_em1"> 
						<s:if test="unUniqueColumns!=null && unUniqueColumns.size()>0">
	    					提示：上传的数据中有重复的列字段！重复列名：
	    				</s:if> 
	    				</em>
						<em style="color: blue; font-weight: bold;"
							id="unUniqueColumns_em2"> <s:iterator
								value="unUniqueColumns" status="userStatus">
								<s:property value="value" />
								<s:if test="!#userStatus.last">,</s:if>
							</s:iterator> </em>
					</td>
				</tr>
				<tr class="importResultMsg" style="display: none; width: 100%;">
					<td width="150">
						数据总记录数：
					</td>
					<td colspan="3" id="rownum"></td>
				</tr>
				<tr class="importResultMsg" style="display: none; width: 100%;">
					<td>
						成功导入记录数：
					</td>
					<td colspan="3" id="addedrownum"></td>
				</tr>
				<tr class="importResultMsg" style="display: none; width: 100%;">
					<td>
						导入失败记录数：
					</td>
					<td colspan="3" id="notaddedrownum"></td>
				</tr>
				<tfoot>
					<tr>

						<td colspan="4">
							<div class="btn" style="width: 100%; text-align: right">
								<button onclick="javascript:importData();" class="button"
									type="button" id="sureBt">
									导入
								</button>
								<button onclick="javascript:resetcolumnmatch();"
									class="button" type="button" id="resetBt">
									重置
								</button>
								<button onclick="javascript:window.close();" class="button"
									type="button" id="closeBt">
									关闭
								</button>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		<div style="overflow: auto; height: 250px; display: none" id="wdrsj">
			<table class="datelist nowrap">
				<caption>
					未导入数据
				</caption>
				<thead>
					<tr>
						<s:iterator value="filecolumnname">
							<th>
								<s:property value="content" />
							</th>
						</s:iterator>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<script type="text/javascript">
	   var tab = $$('.tab')[0].getSize();
	   $$('.importResultMsg').setStyle('width', tab.x - 20);
	   </script>
	</s:else> 
	</span>
</div>
</body>
</html>
