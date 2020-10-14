<#if type=="radioOption">
<div class="panel margin-b0">
	<div class="collapse" role="tabpanel" id="collapse_${panelId}_${name}" style="display: none;">
		<div class="form-group cond-bar cond-bar-single mg margin-b0">
			<label class="col-sm-1 control-label option-label">${text}：</label>
			<div class="col-sm-11">
				<ul class="tag-list">
					<#list optionList as item>
						<li class="">
							<a href="#" type="select" searchname="${name}" itemvalue="${item.key}" data-id="${name}_${item.key}" 
								 class="transition-common <#if defaultValue==item.key>cur</#if>">${item.value}</a>
						</li>
					</#list>
				</ul>
			</div>
		</div>
		<div class="cond-caret" style="left: 563px;"></div>
	</div>
</div>
<#else>
<div class="panel margin-b0">
	<div class="collapse" role="tabpanel" id="collapse_${panelId}_${name}" style="display: none;">
		<div class="form-group cond-bar margin-b0">
			<label class="col-sm-1 control-label option-label">${text}：</label>
			<div class="col-sm-11">
				<div class="form-inline">
					<#if optionList?size &gt; 10> 
						<div class="form-group form-group-sm">
	                        <div class="btn-group">
	                            <button type="button" class="btn zf-btn btn-default btn-sm" name="all_s">全选</button>
	                            <button type="button" class="btn zf-btn btn-default btn-sm" name="inverse_s">反选</button>
	                        </div>
						</div>
						<div class="form-group form-group-sm" name="searchItem">
							<input type="text" class="form-control" placeholder="输入${text}">
							<button type="button" class="btn zf-btn btn-primary btn-sm" >搜索</button>
						</div>
					</#if>
				</div>
				<#if pinyin>
					<ul class="nav nav-tabs nav_many nav_tag" role="tablist">
						<li role="presentation"><a href="#tab_${panelId}_${name}_done" data-toggle="tab" aria-controls="tab_${panelId}_${name}_done" class="transition-common">已选</a></li>
						<#list optionList as item>
							<#if item_index==0 || c!=item.pinyin>
								<#assign c="${item.pinyin}">
								<li role="presentation" <#if item_index==0>class="active"</#if>>
									<a href="#tab_${panelId}_${name}_${c}" data-toggle="tab" aria-controls="tab_${panelId}_${name}_${c}" class="transition-common">${c}</a>
								</li>
							</#if>
						</#list>
					</ul>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane fade" id="tab_${panelId}_${name}_done">
							<ul class="tag-list" name="seleted">
							</ul>
						</div>
						<#assign c=''>
						<#assign t=0>
						<#assign begin=true>
						
						<#list optionList as item>
							<#if c!=item.pinyin && t != 0>		
								<#assign begin=true>
									</ul>
								</div>
							</#if>
							<#assign c="${item.pinyin}">
							<#if t==0 || begin>
								<div role="tabpanel" class="tab-pane <#if item_index==0>active</#if> fade in" id="tab_${panelId}_${name}_${c}">
									<ul class="tag-list tag-list-cond-except">
								<#assign t=1>
								<#assign begin=false>
							</#if>
							<li>
								<a href="#" type="select" searchname="${name}" itemvalue="${item.key}" 
									 <#if defaultValue?? &&defaultValue?seq_contains(item.key)>data-default="true"</#if>
									 data-id="${name}_${item.key}" class="transition-common">${item.value}</a>
							</li>
							<#if optionList?size-1==item_index>		
									</ul>
								</div>
							</#if>
						</#list>
					</div>
				<#else>
					<ul class="tag-list">
						<#if hasBlank>
							<li class="">
								<a href="#" type="select" searchname="${name}" itemvalue="" data-id="${panelId}_${name}" class="transition-common"><b>空值</b></a>
							</li>
						</#if>
						<#list optionList as item>
							<li class="">
								<a href="#" type="select" searchname="${name}" itemvalue="${item.key}" data-id="${panelId}_${name}_${item.key}" 
									 class="transition-common <#if defaultValue?? && defaultValue?seq_contains(item.key)>cur</#if>">${item.value}</a>
							</li>
						</#list>
					</ul>
				</#if>
			</div>
		</div>
		<div class="cond-caret" style="left: 116px;"></div>
	</div>
</div>
</#if>