导出：按照每页的量分页查询，按照起始结束下标写row,每个sheet最大6行数
导出分sheet或者分workbook打包下载



////
ImExport数据导入导出组件（设计概要）
--------------------------------------------------------------------------------------------------------------------------------------------
导入：
	fileupload 	导入
	cos 		导入
--------------------------------------------------------------------------------------------------------------------------------------------
导出：
	1.单表导出：一个excel文件，自定义分页，直接文件下载
	2.多表导出：多个excel文件，自定义分页，打包下载

	方案：
		a.通过表或视图别名，条件来直接查询数据库中的表结构或视图
			
			1.优势：调用简单，直观
				
			2.风险：sql注入，存在安全隐患；视图过多
			
			3.实例：

				/*
				* 导出(超级加强版)
				* tableAlias:导出的表名或视图别名(后台必须有对应的真实名称配置),用于查询要导出的字段信息和作为导出的源数据,类型String
				* conditionList:查询条件,类型Array
				 	注:如下格式，其中的key代表着条件的名(此名如需转换,可后台配置),value代表着条件的值
					[
						{
							key://条件的=左侧名称
							value://条件的=右侧值
						}
					]
				*/
				function dataExportSuperPlus(tableAlias,conditionList) {
					var url = 'dataExpShowColumnsPlus.action?table=' + table;
					$$("body")[0].id = conditionList;
					var w =  window.open(url, "_blank", "menubar=1,resizable=1,toolbar=0,location=0");
				}
			
			
		b.通过一个service名称,原有用于查询的method名称,及与查询相同的条件，
		     从spring的context通过getBean()得到service的实例,利用反射或者ASM
		     查找对应的method调用此方法得到数据结果,进行单表或者多表导出的Excel处理

			1.优势：调用简单，安全性高
			
			2.风险：内存消耗(使用多线程解决)
			
			3.实例：
			
				/*
				* 导出(spring依赖安全版)
				* serviceID:上下文中的service层的ID,类型String
				* method:service实现执行查询的方法名,类型String
				*/
				function dataExportSpringPlus(serviceID,method,condition) {
					var url = 'dataExpShowColumnsPlus.action?table=' + table;
					$$("body")[0].id = conditionList;
					var w =  window.open(url, "_blank", "menubar=1,resizable=1,toolbar=0,location=0");
				}


动态表导入导出解决方案：

	1.用于导出的sql写在配置文件中或者数据库中
	2.通过spring上下文找的对于的service,调用指定方法解决


--------------------------------------------------------------------------------------------------------------------------------------------
配置：
		全部以 List<Map<String,String>> 可转换形式存在于配置或者数据库
		
		1.基于spring-util-2.5.xsd模板可配置xml
		<!-- uiwidget.imexport.js 组件中所需要的一些初始参数 -->
		<util:map id="uiwidget_options" map-class="java.util.HashMap" scope="session">
			<entry key="upload_method" value="cos"></entry> 默认cos
			<entry key="sql_convert_method" value="xml、properties、jdbc"></entry> 默认xml,如果不能从xml配置文件中得到，继续查询properties，没得到则继续执行jdbc的操作
			<entry key="width" value="800px"></entry>
			......
		</util:list>
		<!-- 用于导入导出的表所属的组列表 -->
		<util:list id="table_group_list" list-class="java.util.ArrayList" scope="session">
			<value>group1</value>
			<value>group2</value>
			<value>group3</value>
			<value>group4</value>
			<value>group5</value>
		</util:list>
		<!-- 用于导入导出的表或视图列表 -->
		<util:list id="tables_list" list-class="java.util.ArrayList" scope="session">
			<map>
				<entry key="table_group" value=""></entry> 所属的组
				<entry key="table_alias" value=""></entry> 表别名
				<entry key="table_name" value=""></entry> 表名称(视图或者表名，用于导出或者导入)
				<entry key="comments" value=""></entry> 表描述信息
				<entry key="dynamic" value="1"></entry> 是否动态表  1|0 ,默认 0
				<entry key="prefix" value=""></entry> 动态表名前缀(字符串|beanID#method|sql)
				<entry key="suffix" value=""></entry> 动态表名后缀(字符串|beanID#method|sql)
			</map>
		</util:list>
		<!-- 导入导出的表或视图对应的字段映射关系列表 -->
		<util:map id="table_columns_map" map-class="java.util.HashMap" scope="session">
			<entry key="table_alias" value-ref="table_alias_column_list"></entry>
		</util:map>
		<!-- 导入导出的表或视图对应的字段列表 -->
		<util:list id="table_alias_column_list" list-class="java.util.ArrayList" scope="session">
			<map>
				<entry key="primary_key" value="0"></entry>是否主键 1|0 ,默认 0
				<entry key="column_name" value="0"></entry>列的数据库列名
				<entry key="comments" value="学年||第一学年"></entry>字段描述信息（多个名称时候以||分割，导入会对每个进行匹配，导出则取第一个）
				<entry key="requisite" value="0"></entry> 是否必填，1是，0不是，默认0
				<entry key="unique" value="0"></entry> 是否唯一  1|0 ,默认 0
			</map>
		</util:list>
		<!-- 要进行转换的字段列表 -->
		<util:list id="transform_column_list" list-class="java.util.ArrayList" scope="session">
			<map>
				<entry key="column_name" value=""></entry>要进行转换的列名
				<entry key="convert_sql" value=""></entry> 进行查询转换的sql
				<entry key="comments" value=""></entry> 描述信息
			</map>
		</util:list>
		<!-- (导出查询源数据)涉及多用户间数据查询的sql转换列表 -->
		<util:list id="transform_table_list" list-class="java.util.ArrayList" scope="session">
			<map>
				<entry key="table_name" value=""></entry>要进行转换的表或者视图
				<entry key="table_sql" value=""></entry> 进行查询转换的sql
				<entry key="comments" value=""></entry> 描述信息
			</map>
		</util:list>
		<!-- 导出文件的模板列表 -->
		<util:list id="template_list" list-class="java.util.ArrayList" scope="session">
			<map>
				<entry key="table_alias" value=""></entry>所属的表别名
				<entry key="template_path" value=""></entry> 模板所在的路径
				<entry key="comments" value=""></entry> 描述信息
			</map>
		</util:list>
				

		2.基于数据库spring的jdbctemplete的数据库配置
			
			依赖：JdbcTemplete,数据库表
			
			table :  
				<!-- uiwidget.imexport.js 组件中所需要的一些初始参数,在系统启动的时候加载到 System.setProperty(key, value)-->
				ImExportOptions : uiwidget_options;
								{
									key 参数名
									value 参数值
									comments 描述信息
								} 
				<!-- 用于导入导出的表所属的组 -->
				ImExportGroups  : table_groups_list
								{
									name 所属的组名称
									comments 表描述信息
								} 
				<!-- 用于导入导出的表或视图 -->
				ImExportTables  : tables_list
								{
									table_group 所属的组
									table_alias 表别名
									table_name 表名称(视图或者表名，用于导出或者导入)
									comments 表描述信息
									dynamic 是否动态表  1|0 ,默认 0
									prefix 动态表名前缀(字符串|beanID#method|sql)
									suffix 动态表名后缀(字符串|beanID#method|sql) 
								} 
				<!-- 导入导入的表或视图对应的字段映射关系与对应的字段 -->
				ImExportTableColumns :table_alias_column_list
								{
									table_alias		所属的表
									primary_key 	是否主键 1|0 ,默认 0
									column_name 	列的数据库列名
									comments		列的描述
									requisite		是否必须  1|0 ,默认 0
									unique 			是否唯一  1|0 ,默认 0
								}
				<!-- 要进行转换的字段 -->
				ImExportTransformColumns: transform_column_list
								{
									column_name	  要进行转换的列名
									convert_sql 进行查询转换的sql
									comments	描述信息
								}
				<!-- (导出查询源数据)涉及多用户间数据查询的sql转换 -->
				ImExportTransformTables :transform_table_list
								{
									table_name		要进行转换的表或者视图
									table_sql 	进行查询转换的sql
									comments		描述信息
								}
				ImExportTemplates:template_list
								{
									table_alias		所属的表别名
									template_path 	模板所在的路径
									comments		描述信息
								}