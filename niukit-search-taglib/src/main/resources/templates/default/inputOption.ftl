<div class="input-group col-md-6 col-sm-8 col-xs-12 pull-left">
	<div class="input-group-btn dropdown dropdown-multiple">
		<button type="button" class="btn btn-default dropdown-toggle"  data-hover="dropdown" aria-expanded="false" name="search_blurText">
			全部 <span class="caret"></span>
		</button>
		<input type="hidden" value="-1"  name="search_blurType">
		<ul class="dropdown-menu" role="menu">
			<#list optionList as item>
				<li>
					<label><input type="checkbox"  name="searchType" value="${item.key}"> ${item.value}</label>
				</li>
			</#list>
		</ul>
	</div>
	<div class="input-group-btn dropdown">
		<button type="button" class="btn btn-default dropdown-toggle" data-hover="dropdown" aria-expanded="false" name="search_sqlText">
			包含 <span class="caret"></span>
		</button>
		<input type="hidden" value="0"  name="search_sqlType">
		<ul class="dropdown-menu" role="menu">
			<li><a href="#" class="transition-common" name="search_sqlType" key="0">包含</a></li>
			<li><a href="#" class="transition-common" name="search_sqlType" key="1">不包含</a></li>
			<li><a href="#" class="transition-common" name="search_sqlType" key="2">等于</a></li>
			<li><a href="#" class="transition-common" name="search_sqlType" key="3">不等于</a></li>
		</ul>
	</div>
	<div class="input-group-btn input-group-input">
		<input type="text" class="form-control" placeholder="请输入关键词"  name="search_blurValue">
		<span class="fa fa-remove input-group-input-remove"></span>
	</div>
	<span class="input-group-btn"><button type="button" class="btn btn-primary" name="search_button">查询</button></span>
</div>
<a class="btn zf-btn btn-default pull-left transition-common" name="clear_all_cond">清空条件</a>
<div class="clearfix"></div>