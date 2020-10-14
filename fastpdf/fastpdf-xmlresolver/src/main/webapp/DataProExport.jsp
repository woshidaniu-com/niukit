 <%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@include file="/common/head.ini"%>
		<script type="text/javascript" src="js/mootools.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript">
		//self.name="jwgl";
		//<![CDATA[
		function selectAll(arg0) {
		    var checkboxlist = $$('input[type=checkbox][name=column_name]');
		    checkboxlist.each(function(item, index){
		        switch(arg0) {
		           case 1 :
		             item.checked = true;
		             break;
		           case 2 :
		             item.checked = false;
		             break;
		           case 3:
		             item.checked = !(item.checked);
		             break;
		        }
		    });
		}
		function exportData() {
     		if ($('title').value.trim() == '') {
     			alert('请输入标题！');
     			return false;
     		}
     		var array = getArrayOfCheckedBoxValue("column_name");
     		if (array.length == 0) {
     			alert("请选择要导出的字段！");
     			return false;
     		} else {
     			if (confirm("确定要导出所选字段的数据")) {
     				document.exp_form.submit();
     			}
     		}
 		}
		//]]>
		</script>
	</head>
	<body class="open_body">
	 <s:form action="dataProExport" method="post" name="exp_form" target="_self">
	 <s:hidden value="%{#parameters.table[0]}" name="table" />
	 
	 <fieldset style="margin: 0px 5px;height:420px;" id="fieldset_content">
	   <legend>导出数据</legend>
	   <table class="formlist">
	   	<tr>
	   		<th>标 题</th>
	   		<td>
	   			<s:textfield name="title" id="title" cssClass="text_nor"></s:textfield>
	   		</td>
	   	</tr>
	   </table>
	   <div style="height: 82%;overflow-y: scroll">
	     <table class="datelist" style="width: 100%">
	       <thead>
	         <tr><th width="25%">是否导出</th><th width="75%">字段名</th></tr>
	       </thead>
	       <tbody>
	         <s:iterator value="columnlist">
	           <tr onmouseover="this.bgColor='#DDEAFB'" onmouseout="this.bgColor=''">
	             <td>
	               <s:if test="comments == null">
	                 <input checked="checked" type="checkbox" id="<s:property value="column_name"/>" name="column_name" value="<s:property value="column_name"/>" />
	               </s:if>
	               <s:else>
	                 <input checked="checked" type="checkbox" id="<s:property value="comments"/>" name="column_name" value="<s:property value="column_name"/>" />
	               </s:else>
	             </td>
	             <td>
	               <s:if test="comments == null">
	                  <s:property value="column_name"/>
	               </s:if>
	               <s:else>
	                  <s:property value="comments"/>
	               </s:else>
	             </td>
	           </tr>
	         </s:iterator>
	       </tbody>
	     </table>
	   </div>
	   <div>
	     <table width="100%">
	       <tr>
	         <td style="text-align: left;">
	            <a href="#" style="color: #11449e;" onclick="selectAll(1)">全选</a>
	     		<label style="color: #11449e;">&nbsp;-&nbsp;</label>
	     		<a href="#" style="color: #11449e;" onclick="selectAll(2)">取消</a>
	     		<label style="color: #11449e;">&nbsp;-&nbsp;</label>
	     		<a href="#" style="color: #11449e;" onclick="selectAll(3)">反选</a>
	         </td>
	         <td align="right">
	            <button onclick="javascript: exportData()" type="button">确定</button>
	            <button onclick="javascript: window.close()" type="button">取消</button>
	         </td>
	       </tr>
	     </table>
	   </div>
	 </fieldset>
	 </s:form>
	 <script type="text/javascript">
	 var conCos = '<s:property value="#request['conditions']" />';
	 var typeCos = '<s:property value="#request['types']" />';
 	var varray = $A(conCos.split(","));
 	var typeArr = $A(typeCos.split(","));
 	var fm = $('fieldset_content');
 	for (var i = 0; i < varray.length; i++) {
 		var input = new Element('input', {'type':'hidden','value':varray[i].trim(),'name':'conditions'});
 		fm.grab(input);
 		var input = new Element('input', {'type':'hidden','value':typeArr[i].trim(),'name':'types'});
 		fm.grab(input);
 	}
	 </script>
 </body>
</html>
