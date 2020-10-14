<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@include file="/common/head.ini"%>
		<link rel="stylesheet" href="css/tab-view.css" type="text/css" media="screen"/>
		<script src="<%=request.getContextPath()%>/js/mootools.js"></script>
		<script src="<%=request.getContextPath() %>/js/common.js"></script>
		<script type="text/javascript">
			
		</script>
  </head>
  
  <body>
		<div class="formbox"> <!-- 根据主体内容选择具体的样式默认为frombox,请参考样式说明Readme.txt -->
					<table summary="" class="datelist nowrap" width="100%" align="left">
						<!-- <caption>教学计划列表</caption> -->
						<thead>
						</thead>
						<tbody id="tbody_info">
							<s:if test="filecontents.size() > 0">
							<s:iterator value="filecontents" id="temp_list" >
								<tr>
									<s:iterator value="temp_list">
									<td>
										<s:property value="content"/>
									</td>	
									</s:iterator>		
								</tr>
							</s:iterator>
							</s:if>
							<s:else>
								<jsp:include page="/jwglxt/common/EmptyTable.jsp"></jsp:include>
							</s:else>
						</tbody>
					</table>
		</div>
		
	</body>
</html>
