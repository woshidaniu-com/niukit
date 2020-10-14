<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>导入导出测试</title>

<script type="text/javascript">
$(function(){
	
	$("#import").click(function(){
		
		window.open("imexportRedirectUI.action?options.", "_blank", "menubar=1,resizable=1,toolbar=0,location=0");
		
	});
	
	$("#export").click(function(){
		window.open("", "_blank", "menubar=1,resizable=1,toolbar=0,location=0");
	});
	
	
});
</script>
</head>
<body> 

<button type="button" id="import">导入</button>
<button type="button" id="export">导出</button>
</body>
</html>
