<div class="form-group margin-b0" name="search_optionDiv"  searchname="${name}" pinyin="${pinyin?c}"
 <#if relation?exists>
 	<#assign relationStr='' >
 	<#list relation?keys as key> 
 		<#assign relationKey=relation[key] > 
 		<#assign relationStr=relationStr+relationKey>
 		<#if key_index != relation?size-1>
 			<#assign relationStr=relationStr+','>
 		</#if>
    </#list>
    relation="true"
    relationKeys="${relationStr}"
 </#if>
>
	<label class="col-sm-1 control-label padding-l10" name="searchText">${text}</label>
	<div class="col-sm-10">
		<ul class="tag-list">
			<#assign moreCount=45>
			<#assign length=0><!-- 选项字符串长度,长度达到45个字符显示更多-->
			<#assign morePinyin=false><!-- 更多拼音项，只在pinyin=true是才起效-->
			<#assign tmp=0>
			
			<#list optionList as item>
				<#if pinyin>
					<#if item_index==0 || c!=item.pinyin>
						<#assign c="${item.pinyin}">
						
						<#if length &gt; moreCount>
							<#assign tmp=item_index>
							<#assign morePinyin=true>
							<#break>
						</#if>
						
						<li <#if length &gt; moreCount>name="search_moreItem" class="hide"</#if>><span class="index">${c}</span></li>
						<#assign length=length+c?length>
					</#if>
				</#if>
				
				<li <#if length &gt; moreCount> name="search_moreItem" class="hide"</#if>>
					<a href="javascript:void(0);" type="${type}" optionvalue="${item.key}" searchname="${name}" searchtext="${text}" pinyin="${item.pinyin}"
					 <#if defaultValue?? && defaultValue?seq_contains(item.key)> class="cur"</#if>
					 <#if relation?exists>
					 	<#list relation?keys as key> 
					 		<#assign relationKey=relation[key] > 
					 		<#assign relationValue=item[key] > 
					 		search_${relationKey}="${relationValue}"
		                </#list>
					 </#if>
					 >${item.value}</a>
				</li>
				<#assign length=length+(item.value)?length+1>
			</#list>
		</ul>
	</div>
	<#if length &gt; moreCount>
		<label class="col-sm-1" name="search_moreLable"><a href="javascript:void(0);" name="search_moreOption"><i class="fa fa-angle-double-down"></i> 更多</a></label>
	</#if>
	
	<#if morePinyin>
		<div class="clearfix"></div>
		<div class="col-md-11 col-md-offset-1 padding-t10 hide" name="search_moreDiv">
			<div role="tabpanel">
				<ul class="nav nav-tabs nav_many" role="tablist">
					<#assign c=''>
					<#assign t=0>
					
					<#list optionList as item>
						<#if item_index &gt; tmp>
							<#if c='' || c!=item.pinyin>
								<#assign c="${item.pinyin}">
								<li role="presentation" <#if t==0>class="active"</#if> name="${c}"><a href="#search_tab_${name}_${c}" data-toggle="tab" aria-controls="tab_${name}_${c}">${c}</a></li>
								<#assign t=1>
							</#if>
						</#if>
					</#list>
				</ul>
				<div class="tab-content">
					<#assign c=''>
					<#assign t=0>
					<#assign begin=true>
					
					<#list optionList as item>
						<#if item_index &gt; tmp>
							<#if c!=item.pinyin && t != 0>		
								<#assign begin=true>
									</ul>
								</div>
							</#if>
							<#assign c="${item.pinyin}">
							<#if t==0 || begin>
								<div role="tabpanel" class="tab-pane <#if t==0>active</#if>" id="search_tab_${name}_${item.pinyin}" pinyin="${item.pinyin}">
									<ul class="tag-list">
								<#assign t=1>
								<#assign begin=false>
							</#if>
							<li>
								<a href="javascript:void(0);" type="${type}" optionvalue="${item.key}" pinyin="${item.pinyin}"
									<#if defaultValue?? && defaultValue?seq_contains(item.key)> class="cur" </#if>
									 <#if relation?exists>
									 	<#list relation?keys as key> 
									 		<#assign relationKey=relation[key] > 
									 		<#assign relationValue=item[key] > 
									 		search_${relationKey}="${relationValue}"
						                </#list>
									 </#if>
									 searchname="${name}" searchtext="${text}">${item.value}</a>
							</li>
							<#if optionList?size-1==item_index>		
									</ul>
								</div>
							</#if>
						</#if>
					</#list>
				</div>
			</div>
		</div>
	</#if>
</div>
