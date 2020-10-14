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
			function qryList(index){
				var current_value = index;
				window.parent.refreshSelTab(1,current_value);
			}
		</script>
  </head>
  
  <body>
		<div class="formbox"> <!-- 根据主体内容选择具体的样式默认为frombox,请参考样式说明Readme.txt -->
					<div style="float: left">
					<table summary="" class="datelist nowrap" width="100%" align="left">
						<!-- <caption>教学计划列表</caption> -->
						<thead>
							<tr>
							<s:iterator value="columnlist" >
									<th>
										<s:property value="comments"/>
									</th>
							</s:iterator>
							</tr>
						</thead>
						<tbody id="tbody_info">
							<s:if test="list_array.size() > 0">
								<s:iterator value="list_array" id="temp_list">
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
						<div style="float: left;width: 800px;">
							<span class="pagination">
								<table width="100%" border="0" align="center"
									style="margin: 0px; padding: 0px;">
									<tr style="margin: 0px; padding: 0px;">
										<td align='right'>
											共有
											<font color='red'><s:property value="page.pageCount" />
											</font>页&nbsp;每页
											<font color='red'><s:property value="page.pageSize" />
											</font>条&nbsp;第
											<font color='red'><s:property value="page.currentPage" />
											</font>页&nbsp;
											<s:if test="page.currentPage > 1">
												<a onclick='qryList(1)' title='首页'>首页</a>
												<a
													onclick='qryList(<s:property value="page.previousPage"/>)'
													title='上一页'>上一页</a>
											</s:if>
											<s:if test="page.currentPage < page.pageCount">
												<a onclick='qryList(<s:property value="page.nextPage"/>)'
													title='下一页'>下一页</a>
												<a onclick='qryList(<s:property value="page.lastPage"/>)'
													title='末页'>末页</a>
											</s:if>
										</td>
										<td align='center'>
											转到
											<input name='gotopage' id='gotopage' type='text'
												class='text_nor' value='' size='4' maxlength='4' />
											页
											<img src="images/but_next.gif" width="30" height="13"
												onclick="checkpage('<s:property value="page.pageCount"/>',qryList,'gotopage')"
												style="CURSOR: hand" />
										</td>
										<td align='center'>
											每页显示
											<input name="pageSize" id="pagesize" type="text"
												class="text_nor" size="4" maxlength="4"
												value="<s:property value="page.pageSize"/>" />
											条
											<img src="images/but_next.gif" width="30" height="13"
												onclick="qryList(1)" style="CURSOR: hand" />
											<input type="hidden" name="currentPage" id="currentpage"
												value="<s:property value="page.currentPage"/>" />
										</td>
									</tr>
								</table>
						 </span>
				</div>						 
			</div>
					
	</body>
</html>
