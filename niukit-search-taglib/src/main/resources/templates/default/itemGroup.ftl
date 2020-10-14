<div class="panel margin-b0">
	<div class="collapse" role="tabpanel" id="collapse_${panelId}_${name}" style="display: none;">
		<#list childList as child>
			<#if child.pinyin>
				<div class="form-group cond-bar <#if child.type=="radioOption">cond-bar-single</#if> margin-b0"
					<#if child.relation?exists>
					 	<#assign relationStr='' >
					 	<#list child.relation?keys as key> 
					 		<#assign relationKey=child.relation[key] > 
					 		<#assign relationStr=relationStr+relationKey>
					 		<#if key_index != child.relation?size-1>
					 			<#assign relationStr=relationStr+','>
					 		</#if>
					    </#list>
					    relation="true"
					    relationKeys="${relationStr}"
				 </#if>
				>
					<label class="col-sm-1 control-label">${child.text}：</label>
					<div class="col-sm-11">
						<div role="tabpanel">
							<#if child.optionList?size &gt; 10> 
								<div class="form-inline">
									<div class="form-group form-group-sm" name="searchItem">
										<input type="text" class="form-control" placeholder="输入${child.text}">
										<button type="button" class="btn zf-btn btn-primary btn-sm">搜索</button>
									</div>
								</div>
							</#if>
							<ul class="nav nav-tabs nav_many nav_tag" role="tablist">
								<li role="presentation">
									<a href="#tab_${child.name}_done" data-toggle="tab" aria-controls="tab_${child.name}_done" class="transition-common">已选</a>
								</li>
								
								<#list child.optionList as item>
									<#if item_index==0 || c!=item.pinyin>
										<#assign c="${item.pinyin}">
										<li role="presentation" <#if item_index==0>class="active"</#if>>
											<a href="#tab_${panelId}_${child.name}_${c}" data-toggle="tab" aria-controls="tab_${panelId}_${child.name}_${c}" class="transition-common">${c}</a>
										</li>
									</#if>
								</#list>
								<li role="presentation">
									<button type="button" class="btn btn-primary nav_tag_all">全选</button>
									<button type="button" class="btn btn-primary nav_tag_cancel">取消</button>
								</li>
							</ul>
							<div class="tab-content">
								<div role="tabpanel" class="tab-pane fade" id="tab_${child.name}_done">
									<ul class="tag-list" name="seleted">
									</ul>
								</div>
								
								<#assign c=''>
								<#assign t=0>
								<#assign begin=true>
								
								<#list child.optionList as item>
									<#if c!=item.pinyin && t != 0>		
										<#assign begin=true>
											</ul>
										</div>
									</#if>
									<#assign c="${item.pinyin}">
									<#if t==0 || begin>
										<div role="tabpanel" class="tab-pane <#if item_index==0>active</#if> fade in" id="tab_${panelId}_${child.name}_${c}">
											<ul class="tag-list tag-list-cond-except">
										<#assign t=1>
										<#assign begin=false>
									</#if>
									<li>
										<a href="#" type="select" searchname="${child.name}" itemvalue="${item.key}" 
											<#if child.relation?exists>
												<#list child.relation?keys as key> 
													<#assign relationKey=child.relation[key] > 
													<#assign relationValue=item[key] > 
													search_${relationKey}="${relationValue}"
											   </#list>
											</#if>
											 <#if child.defaultValue?? && child.type=="radioOption" && child.defaultValue==item.key>data-default="true"</#if>
											 <#if child.defaultValue?? && child.type!="radioOption" && child.defaultValue?seq_contains(item.key) >data-default="true"</#if>
											 data-id="${child.name}_${item.key}" class="transition-common">${item.value}</a>
									</li>
									<#if child.optionList?size-1==item_index>		
											</ul>
										</div>
									</#if>
								</#list>
							</div>
						</div>
					</div>
				</div>
			<#else>
				<div class="form-group cond-bar <#if child.type=="radioOption">cond-bar-single</#if> mg margin-b0"
					<#if child.relation?exists>
					 	<#assign relationStr='' >
					 	<#list child.relation?keys as key> 
					 		<#assign relationKey=child.relation[key] > 
					 		<#assign relationStr=relationStr+relationKey>
					 		<#if key_index != child.relation?size-1>
					 			<#assign relationStr=relationStr+','>
					 		</#if>
					    </#list>
					    relation="true"
					    relationKeys="${relationStr}"
				 </#if>
				>
					<label class="col-sm-1 control-label">${child.text}：</label>
					<div class="col-sm-11">
						<ul class="tag-list">
							<#if child.hasBlank>
								<li class="">
									<a href="#" type="select" searchname="${child.name}" itemvalue="" data-id="${child.name}_" class="transition-common">空</a>
								</li>
							</#if>
							<#list child.optionList as item>
								<li>
									<a href="#" type="select" searchname="${child.name}"  
										<#if child.relation?exists>
											<#list child.relation?keys as key> 
												<#assign relationKey=child.relation[key] > 
												<#assign relationValue=item[key] > 
												search_${relationKey}="${relationValue}"
										   </#list>
										</#if>
										<#if child.defaultValue?? && child.type=="radioOption"&& child.defaultValue==item.key >data-default="true"</#if>
										<#if child.defaultValue?? && child.type!="radioOption" && child.defaultValue?seq_contains(item.key) >data-default="true"</#if>
										 itemvalue="${item.key}" data-id="${child.name}_${item.key}" 
										 class="transition-common">${item.value}</a>
								</li>
							</#list>
						</ul>
					</div>
				</div>
			</#if>
			<#if child_has_next>
				<hr class="hr-solid-gray">
			</#if>
		</#list>
		<div class="cond-caret" style="left: 161px;"></div>
	</div>
</div>