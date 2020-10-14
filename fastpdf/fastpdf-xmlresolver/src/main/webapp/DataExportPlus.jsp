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
		function selectAll(obj,arg0) {
		    var checkboxlist = $$('input[type=checkbox][name=column_name]');
		    checkboxlist.each(function(item, index){
		        switch(arg0) {
		           case 1 :
		             item.checked = $(obj).checked; 
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
	 <s:form action="dataExportPlus" method="post" name="exp_form" target="_self" id="fieldset_content">
	 <s:hidden type="text" value="%{#parameters.table[0]}" name="table" />
	   <table class="formlist" border="0" width="100%">
	   	<thead>
	   		<tr>
		   		<td colspan="2">
		   			<span>导出数据</span>
		   		</td>
	   		</tr>
	   	</thead>
	   	<tbody>
		   	<tr>
		   		<th>标题</th>
		   		<td>
		   			<s:textfield name="title" id="title" cssClass="text_nor"/>
		   		</td>
		   	</tr>
	   	</tbody>
	   </table>
	   <div class="formbox" style="height: 350px;overflow-y:auto;overflow-x:hidden" >
	     <table class="datelist" width="100%" summary="" align="">
	       <thead>
	         <tr>
	         	<th width="25%">是否导出</th>
	         	<th width="60%">字段名</th>
	         </tr>
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
	       <%--<tfoot>
	       <tr>
	         <td colspan="2">
				<div class="bz">
				<input type="checkbox"  value="全 选" name="全 选" onclick="selectAll(this,1)"/> 全 选  
	            <input type="checkbox"  value="反 选" name="反 选" onclick="selectAll(this,3)"/> 反 选
	     		</div>
	     		<div class="btn">
	     			 <button onclick="javascript: exportData()" type="button">确 定</button>
	            	 <button onclick="javascript: window.close()" type="button">取 消</button>
	     		</div>
	         </td>
	       </tr>
	       </tfoot>--%>
	     </table>
	   </div>
	   <div class="open_foot">
	   		<div class="open_btn">
	   			<button onclick="javascript: exportData()" type="button">确 定</button>
	            <button onclick="javascript: window.close()" type="button">取 消</button>
	  		</div>
	   </div>
	 </s:form>
	 <script type="text/javascript">
	 //<![CDATA[
	 var b = window.opener.$$("body")[0];
	 //alert(b.id);
	 var input = new Element('input', {'type':'hidden','value':b["id"],'name':'condition'});
	 $('fieldset_content').grab(input);
	 //]]>
	 </script>
 </body>
</html>
