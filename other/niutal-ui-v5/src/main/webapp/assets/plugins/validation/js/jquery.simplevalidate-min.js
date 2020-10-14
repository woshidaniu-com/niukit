/*
 * jquery.simplevalidate 插件：详情参见：jquery.simplevalidate.js
 */
(function(a){a.bootui=a.bootui||{};a.bootui.widget=a.bootui.widget||{};a.bootui.widget.SimpleValidate=function(c,b){b.beforeRender.call(this,c);try{this.initialize.call(this,c,b)}catch(d){b.errorRender.call(this,d)}b.afterRender.call(this,c)};a.bootui.widget.SimpleValidate.prototype={constructor:a.bootui.widget.SimpleValidate,initialize:function(c,b){var d=this;a.each(b.events||[],function(f,e){a(c).off(e+"."+b.prefix+".widget").on(e+"."+b.prefix+".widget",function(i){var g=b.doValidated.call(this,this,i);var h=a(this).parent().parent();if(g!=true){a(h).removeClass(b.errorParentClass);a(h).removeClass(b.warningParentClass);a(h).removeClass(b.validParentClass);a(h).addClass(b.errorParentClass);if(g!=false&&a.founded(g)){b.errorPlacement.call(this,b,i,g)}}else{a(h).removeClass(b.errorParentClass);a(h).removeClass(b.warningParentClass);a(h).removeClass(b.validParentClass);if(g!=false){b.success.call(this,b,i)}}})});a.extend(true,d,{isValid:function(){var e=false;a.each(events||[],function(g,f){e=a(elem).trigger(f);if(e==true){return false}});return e}})},setDefaults:function(b){a.extend(a.fn.simpleValidate.defaults,b)},getDefaults:function(b){return a.fn.simpleValidate.defaults}};a.fn.simpleValidate=function(c){var b=a.grep(arguments||[],function(e,d){return d>=1});return this.each(function(){var f=a(this);var d=a.extend({},a.fn.simpleValidate.defaults,f.data(),((typeof c=="object"&&c)?c:{}));var e=f.data(d.prefix+".widget");if(!e&&c=="destroy"){return}if(!e){f.data(d.prefix+".widget",(e=new a.bootui.widget.SimpleValidate(this,d)))}if(typeof c=="string"){e[c].apply(e,[].concat(b||[]))}})};a.fn.simpleValidate.defaults={version:"1.0.0",prefix:"simpleValidate",beforeRender:a.noop,errorRender:a.noop,afterRender:a.noop,events:["keyup","blur"],errorClass:"error",errorParentClass:"has-error",warningClass:"warning",warningParentClass:"has-warning",validClass:"success",validParentClass:"has-success",errorType:"tooltip",success:function(b,c){if(a.fn.tooltips&&"tooltip"==String(b.errorType||"title").toLowerCase()){a(this).getRealElement().tooltips("destroy")}else{a(this).removeAttr("title")}},errorPlacement:function(c,e,b){if(a.fn.tooltips&&"tooltip"==String(c.errorType||"title").toLowerCase()&&a(this).is(":visible")){var f={delay:0,html:true,placement:a(this).data("placement")||"top",align:"right",trigger:"focus"};if(a(this).is(":radio")||a(this).is(":checkbox")){f.placement="right"}var d=a(this).getRealElement();a(d).tooltips("destroy");a(d).attr({title:b});a(d).tooltips(f);a(d).tooltips("show")}else{a(this).attr("title",b)}},doValidated:function(b){return true}};a.fn.simpleValidate.Constructor=a.bootui.widget.SimpleValidate;a.fn.errorClass=function(f,c){if(a.founded(f)){if(a.fn.tooltips){var d=a(this).parent().parent();a(d).removeClass("has-error has-warning has-success");a(d).addClass("has-error");var e={delay:0,html:true,placement:c||"top",align:"right",trigger:"focus"};if(a(this).is(":radio")||a(this).is(":checkbox")){e.placement="right"}var b=a(this).getRealElement();a(b).tooltips("destroy");a(b).attr({title:f});a(b).tooltips(e);a(b).tooltips("show")}else{a(this).attr("title",f)}}};a.fn.successClass=function(b){var c=a(this).parent().parent();a(c).removeClass("has-error has-warning has-success");if(a.fn.tooltips&&a(this).is(":visible")){a(this).getRealElement().tooltips("destroy")}else{a(this).removeAttr("title")}}}(jQuery));