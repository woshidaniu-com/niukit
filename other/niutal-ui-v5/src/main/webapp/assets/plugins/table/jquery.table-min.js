(function(a){a.bootui=a.bootui||{};a.bootui.widget=a.bootui.widget||{};a.bootui.widget.Table=function(c,b){b.beforeRender.call(this,c);try{this.initialize.call(this,c,b)}catch(d){b.errorRender.call(this,d)}b.afterRender.call(this,c)};a.bootui.widget.Table.prototype={constructor:a.bootui.widget.Table,initialize:function(e,l){var g=this;function h(m){do{m+=~~(Math.random()*1000000)}while(document.getElementById(m));return m}var d=h(l.prefix);var c=null;a.each(l.colModel||[],function(n,m){if(m.key==true){c=m.index||null;return false}});var k={container:function(){return'<div class="table-responsive"><table class="table table-bordered table-striped table-condensed table-hover tab-bor-col-1 '+l.classes+'" style="text-align:center"><thead></thead><tbody></tbody></table></div>'},thead:function(){var m=[];m.push("<tr>");if(l.rownumbers==true){m.push('<td  width="'+(l.rownumWidth||"35px")+'" >&nbsp;</td>')}if(l.multiselect==true){m.push('<td  width="'+(l.multiselectWidth||"35px")+'" ><label><input type="checkbox" name="all_'+d+'"/></label></td>')}else{m.push('<td  width="'+(l.multiselectWidth||"35px")+'" style="display: none;">&nbsp;</td>')}a.each(l.colModel||[],function(o,n){m.push('<td class="align-center bolder" '+(n.width?'  width="'+n.width+'" ':"")+((n.hidden==true)?' style="display: none;" ':"")+" >");m.push(n.label||"");m.push("</td>")});m.push("</tr>");return m.join("")},row:function(o,n){var m=[];m.push('<tr index="'+n[c]+'">');if(l.rownumbers==true){m.push('<td class="align-center" data-mager="rownumber_'+n[c]+'_1" width="'+(l.rownumWidth||"35px")+'" >'+(o+1)+"</td>")}if(l.multiselect==true){m.push('<td data-mager="input_'+n[c]+'_2" ><label><input type="checkbox" name="'+d+'" value="'+n[c]+'"/></label></td>')}else{m.push('<td  data-mager="input_'+n[c]+'_2"  style="display: none;"><label><input type="radio" name="'+d+'"  value="'+n[c]+'"/></label></td>')}a.each(l.colModel||[],function(s,r){var q=r.mergeable==true?' data-mager="'+n[c]+"_"+s+'" ':"";var u='class="align-'+(r.align||"center")+'" ';var t=(r.hidden==true?' style="display: none;" ':"");var p=a.isFunction(r.attr)?(r.attr(s,n[r.index],o,n)||""):(r.attr||"");m.push("<td "+q+u+t+p+">");if(a.isFunction(r.formatter)){m.push(r.formatter(s,n[r.index],o,n)||"")}else{m.push(n[r.index])}m.push("</td>")});m.push("</tr>");return m.join("")},empty:function(){var m=0;if(l.rownumbers==true){m+=1}if(l.multiselect==true){m+=1}return'<tr><td class="align-center" colspan="'+(l.colModel.length+m)+'">'+l.emptyText+"</td></tr>"}};var b=a(k.container());a(e).empty().append(b);a(b).attr("id",d);function j(){a(b).find(" tbody > tr ").each(function(n,o){a(this).find("td").each(function(p,r){var q=a(this).data("mager");if(a.trim(q).length>0){a(this).attr("rowspan",a("td[data-mager='"+q+"']").size());a("td[data-mager='"+q+"']:gt(0)").remove()}})});if(l.rownumbers==true){var m=1;a(b).find(" tbody > tr ").each(function(n,p){var o=a(this).find("td[data-mager='rownumber_"+a(p).attr("index")+"_1']");if(o.size()>0){a(o).text(m);m+=1}})}}function f(){a(b).find("thead").empty().html(k.thead());if(a.trim(l.href).length>0&&l.datatype=="json"){a(b).find("tbody").empty().html(l.loadingHtml);jQuery.ajaxSetup({async:false});jQuery.get(l.href,l.postData||{});jQuery.post(l.href,l.postData||{});a.getJSON(l.href,l.postData||{},function(o){l.data=o||[];l.loadComplete.call(b,o||[]);var n=[];a.each(l.data||[],function(p,q){n.push(k.row(p,q))});a(b).find("tbody").empty().html(n.join(""));j();l.tableComplete.call(b,l.data)});jQuery.ajaxSetup({async:true})}else{alert(l.data.length);if(l.data.length>0){var m=[];a.each(l.data||[],function(n,o){m.push(k.row(n,o))});a(b).find("tbody").empty().html(m.join(""));j()}else{a(b).find("tbody").empty().html(k.empty())}l.tableComplete.call(b,l.data)}}function i(){a("input[name='all_"+d+"']").off(l.clickEventType).on(l.clickEventType,function(o){var m=a(this).prop("checked");if(l.multiselect==true){a("input[name='"+d+"']").prop("checked",m)}var n=[];a(b).find("tbody > tr ").each(function(p,q){n.push(a(q).attr("index"))});l.onSelectAll.call(b,n,m)});a(b).find("tbody > tr ").each(function(m,n){a(n).find("input[name='"+d+"']").off(l.clickEventType).on(l.clickEventType,function(o){o.stopPropagation();l.onSelectRow.call(b,a(this).val(),a(this).prop("checked"))});a(this).off(l.clickEventType).on(l.clickEventType,function(q){var p=a(this).find("input[name='"+d+"']");var o=o=true;if(l.multiselect==true){o=!a(p).prop("checked")}a(p).prop("checked",o);l.onSelectRow.call(b,a(p).val(),o);q.stopPropagation()})})}a.extend(true,g,{destroy:function(){a(e).off("."+l.prefix).removeData("bootui.table");a("#"+d).remove()},reload:function(m){a.each(m||{},function(o,n){delete l.postData[o]});a.extend(l.postData,m||{});f();i()},resetSelection:function(){a(b).find("tbody > tr ").each(function(m,n){a(this).find("input[name='"+d+"']").prop("checked",false)})},setSelection:function(n,m){var o=a(b).find("tr[index='"+n+"']");if(m){a(o).trigger(l.clickEventType)}else{a(o).find("input[name='"+d+"']").prop("checked",true)}},getKeys:function(){var n=new Array();var m=a("input[name='"+d+"']").filter(":checked");a(m).each(function(o,p){n.push(a(p).val())});return n},getRow:function(m){var n=[];a.each(l.data||[],function(o,p){if(p[c]==m){n.push(a.extend({},p))}});return n.length>1?n:n[0]},getRows:function(){return l.data||[]},addRow:function(m){a(b).find("tbody").append(k.row(l.data.length,m));l.data.push(m);i()},setRow:function(m,n){a.each(l.data||[],function(o,p){if(p[c]==m){l.data.splice(o,1,n);a(b).find("tr[index='"+m+"']").replaceWith(k.row(o,n));i();return false}})},deleteRow:function(m){a.each(l.data||[],function(n,o){if(o[c]==m){l.data.splice(n,1,o);a(b).find("tr[index='"+m+"']").remove();return false}})}});f();i()},setDefaults:function(b){a.extend(a.fn.table.defaults,b)},getDefaults:function(b){return a.fn.table.defaults}};a.fn.table=function(c){var b=a.grep(arguments||[],function(e,d){return d>=1});return this.each(function(){var f=a(this);var e=f.data("bootui.table");if(!e&&c=="destroy"){return}if(!e){var d=a.extend(true,{},a.fn.table.defaults,f.data(),((typeof widget==="object")?widget:{}),((typeof c=="object"&&c)?c:{}));f.data("bootui.table",(e=new a.bootui.widget.Table(this,d)))}if(typeof c=="string"){e[c].apply(e,[].concat(b||[]))}})};a.fn.table.defaults={version:"1.0.0",prefix:"table",beforeRender:a.noop,errorRender:a.noop,afterRender:a.noop,rownumbers:false,rownumWidth:35,multiselect:true,multiselectWidth:20,striped:true,bordered:true,condensed:true,classes:"  tab-td-padding-10 ",datatype:"local",emptyText:"\u65e0\u6570\u636e...",loadingHtml:'<p class="text-center header smaller lighter loading"><i class="icon-spinner icon-spin orange  bigger-300"></i></br> \u6570\u636e\u6b63\u5728\u8f7d\u5165\u6570\u636e\u4e2d....</p>',postData:{},href:"",colModel:[],loadComplete:function(b,c){},tableComplete:function(b){},onSelectAll:function(c,b){jQuery.each(c,function(d,e){})},onSelectRow:function(c,b){},data:[],clickEventType:((document.ontouchstart!==null)?"click":"touchstart")};a.fn.table.Constructor=a.bootui.widget.Table}(jQuery));