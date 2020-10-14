<div class="row margin-t10">
	<div class="input-group col-md-6 col-md-offset-3 col-xs-10 col-xs-offset-1">
		<div class="input-group-btn">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
				<span id="search_blurText">全部 </span>
				<span class="caret"></span>
			</button>
			${inputPart.getHtml("zfstyle5")}
		</div>
		<input type="text" id="search_blurValue" class="form-control" placeholder="请输入关键词">
		<span class="input-group-btn">
			<button type="button" class="btn btn-primary" id="search_button">查询</button>
		</span>
	</div>
</div>
<div class="clearfix"><br></div>
<#if (childList?size>0)>
	<form class="form-horizontal">
		<div class="panel panel-warning">
			<div class="panel-heading " <#if (defaultItem?size==0)>style="display:none;"</#if>>
				<div class="form-group margin-b0">
					<label class="col-sm-1 control-label padding-l10">已选条件</label>
					<div class="col-sm-10">
						<ul class="tag-list tag-list1" id="itemUl">
							<#if (defaultItem?size>0)>
								<#list defaultItem as option>
									<#list option.getDefaultItem() as item>
										<li>
											<a href="javascript:void(0);"><b class="type">${option.text}：</b>${item.value} 
											<i class="glyphicon glyphicon-remove close-icon" searchname="${option.name}" itemvalue="${item.key}"></i></a>
										</li>
									</#list>
								</#list>
							</#if>
						</ul>
					</div>
				</div>
			</div>
			<div class="panel-body panel-collapse collapse" id="collapse_condition">
				<#list childList as child>
					${child.getHtml("zfstyle5")}
					<#if child_has_next>
						<hr class="margin-tb10">
					</#if>
				</#list>
			</div>
			<div class="more-condition-bar panel-footer text-center">
				<a class="collapsed" data-toggle="collapse" href="#collapse_condition" aria-expanded="false" aria-controls="collapse_condition">
					<i class="glyphicon glyphicon-collapse-down"></i><span> 展开更多条件</span>
				</a>
			</div>
		</div>
	</form>
</#if>
<script type="text/javascript">
	(function(a){a.extend(a,{search:{version:"5.0",author:"Penghui.Qu"}});a.extend(a.search,{init:function(){a("#search_blurValue").bind("keyup",function(f){if(f.keyCode==13){a("#search_button").click()}});a("a[name=search_blurType]").click(function(){a("#search_blurText").html(a(this).html());a("#search_blurType").val(a(this).attr("key"))});var d=function(){var e=a(this).attr("searchname");var f=a(this).attr("itemvalue");a("a[searchname="+e+"][optionvalue="+f+"]").click();a(this).parents("li").remove();c()};var c=function(e){if(a("#itemUl li").size()==0){a(".panel-heading").hide()}else{a(".panel-heading").show()}};a(".glyphicon-remove").bind("click",d);a("a[type=multiOption]").click(function(){var h=a(this).attr("searchname");var l=a(this).attr("searchtext");var k=a(this).html();var j=a(this).attr("optionvalue");if(a(this).hasClass("cur")){a("i[searchname="+h+"][itemvalue="+j+"]").parents("li").remove();a(this).removeClass("cur")}else{var e=a("<li></li>");var f=a("<a href='#'><b class='type'>"+l+"\uff1a</b>"+k+" </a>");var g=a('<i class="glyphicon glyphicon-remove close-icon" searchname="'+h+'" itemvalue="'+j+'"></i>');g.bind("click",d);f.append(g).appendTo(e);e.appendTo(a("#itemUl"));a(this).addClass("cur")}c()});a("a[type=radioOption]").click(function(){var h=a(this).attr("searchname");var l=a(this).attr("searchtext");var k=a(this).html();var j=a(this).attr("optionvalue");a("i[searchname="+h+"]").parents("li").remove();if(a(this).hasClass("cur")){a(this).removeClass("cur")}else{a("a[type=radioOption][searchname="+h+"]").removeClass("cur");var e=a("<li></li>");var f=a("<a href='#'><b class='type'>"+l+"\uff1a</b>"+k+" </a>");var g=a('<i class="glyphicon glyphicon-remove close-icon" searchname="'+h+'" itemvalue="'+j+'"></i>');g.bind("click",d);f.append(g).appendTo(e);e.appendTo(a("#itemUl"));a(this).addClass("cur")}c()});a("#collapse_condition").on("shown.bs.collapse",function(){var e=a("[href='#"+a(this).attr("id")+"']");e.children("span").text(" \u6536\u8d77\u66f4\u591a\u6761\u4ef6");e.children("i").addClass("glyphicon-collapse-up").removeClass("glyphicon-collapse-down")});a("#collapse_condition").on("hidden.bs.collapse",function(){var e=a("[href='#"+a(this).attr("id")+"']");e.children("span").text(" \u5c55\u5f00\u66f4\u591a\u6761\u4ef6");e.children("i").addClass("glyphicon-collapse-down").removeClass("glyphicon-collapse-up")});a("a[name=search_moreOption]").click(function(){if(a(this).find(".fa-angle-double-down").size()>0){a(this).html("<i class='fa fa-angle-double-up'></i> \u6536\u8d77");a(this).parents("div[name=search_optionDiv]").find("li[name=search_moreItem]").removeClass("hide");a(this).parents("div[name=search_optionDiv]").find("div[name=search_moreDiv]").removeClass("hide")}else{a(this).html("<i class='fa fa-angle-double-down'></i> \u66f4\u591a");a(this).parents("div[name=search_optionDiv]").find("li[name=search_moreItem]").addClass("hide");a(this).parents("div[name=search_optionDiv]").find("div[name=search_moreDiv]").addClass("hide")}});a("div[type=date]").each(function(f,k){var g=a(k).find(":input").first();var h=a(k).find(":input").last();var j={elem:"#"+g.attr("id"),format:g.attr("format"),max:"2099-06-16 23:59:59",istime:true,istoday:false,choose:function(i){e.min=i;e.start=i}};var e={elem:"#"+h.attr("id"),format:h.attr("format"),max:"2099-06-16 23:59:59",istime:true,istoday:false,choose:function(i){j.max=i}};laydate(j);laydate(e)});var b=function(o,q){o.children(":not(.control-label)").addClass("hide");var l=a('<div name="relationTemDiv"></div>');var f=a('<div class="col-sm-10" ></div>');var m=a('<ul class="tag-list"></ul>');var g=0;var h=45;var n=false;var i="";var k=0;a.each(q,function(t,u){var s=a(u).attr("pinyin");if(Boolean(o.attr("pinyin"))&&(t==0||i!=s)){k=t;if(g>h){n=true;return false}i=s;m.append("<li><span class='index'>"+s+"</span></li>");g+=s.length}var r=a("<li></li>");if(g>h){r.attr("name","search_moreItem");r.addClass("hide")}r.append(u);m.append(r);g+=a(u).html().length+1});f.append(m);l.append(f);if(g>h&&q.length>k){var p=o.find("label[name=search_moreLable]");p.removeClass("hide");p.find("a").html('<i class="fa fa-angle-double-down"></i> \u66f4\u591a');l.append(p);if(n){o.find(".clearfix").removeClass(".hide").appendTo(l);var e=o.find("div[name=search_moreDiv]").removeClass(".hide");e.appendTo(l);e.find(".tab-content>.tab-pane>.tag-list>li>a").addClass("hide");a.each(q,function(s,t){if(s<=k){return}var r=a(t).attr("optionvalue");e.find(".tab-content>.tab-pane>.tag-list>li>a[optionvalue="+r+"]").removeClass("hide")});e.find(".nav-tabs > li").removeClass("active").addClass("hide");a.each(q,function(s,t){if(s<=k){return}var r=a(t).attr("pinyin");e.find(".nav-tabs").find("li[name="+r+"]").removeClass("hide");e.find(".tab-content>.tab-pane").removeClass("active")});e.find(".nav-tabs>li").not(".hide").eq(0).addClass("active");var j=e.find(".nav-tabs>li").not(".hide").eq(0).attr("name");e.find(".tab-content>div[pinyin="+j+"]").addClass("active")}}o.append(l)};a("div[name=search_optionDiv][relation=true]").each(function(e,g){var f=a(g).attr("relationKeys").split(",");a.each(f,function(h,i){a("a[searchname="+i+"]").bind("click",function(){var k=[];a.each(f,function(m,l){a.merge(k,a("#itemUl li i[searchname="+l+"]"))});if(k.length>0){var j=[];a.each(k,function(l,p){var n=a(p).attr("searchname");var o=a(p).attr("itemvalue");if(a.inArray(n,f)>-1){var m=a(g).find("a[searchname="+a(g).attr("searchname")+"][search_"+n+"="+o+"]");if(m.size()>0){a.merge(j,m)}}});b(a(g),j)}else{var j=a(g).find("a[searchname="+a(g).attr("searchname")+"]");b(a(g),a.merge(j,[]))}})})})},getSearchMap:function(){var g={};var b={};a("#itemUl li i").each(function(h,j){if(b[a(this).attr("searchname")]){b[a(this).attr("searchname")].push(a(this).attr("itemvalue"))}else{b[a(this).attr("searchname")]=[a(this).attr("itemvalue")]}});var e={};a("div[type=date]").each(function(j,m){var k=a(m).find(":input").first();var l=a(m).find(":input").last();var h=a(m).attr("name");e[h]=[[k.attr("format"),k.val(),l.val()]]});var c=a("#search_blurType").val();var d=a.trim(a("#search_blurValue").val()).split(/\s/);var f={};if(c=="-1"){a("a[name=search_blurType]").each(function(h,j){if(a(this).attr("key")!="-1"){f[a(this).attr("key")]=d}})}else{f[c]=d}g["searchModel.dateType"]=JSON.stringify(e);g["searchModel.inputType"]=JSON.stringify(f);g["searchModel.selectType"]=JSON.stringify(b);g["searchModel.inputSqlType"]="0";return g}})}(jQuery));
	$.search.init();
</script>