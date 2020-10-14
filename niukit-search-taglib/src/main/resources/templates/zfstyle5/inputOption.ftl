<input type="hidden" value="-1"  id="search_blurType">
<ul class="dropdown-menu" role="menu">
	<li><a href="javascript:void(0);" name="search_blurType" key="-1">全部</a></li>
	<#list optionList as item>
		<li><a href="javascript:void(0);" name="search_blurType" key="${item.key}">${item.value}</a></li>
	</#list>
</ul>