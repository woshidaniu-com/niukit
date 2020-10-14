(function($) {
	
	$.i18n = $.i18n || {};
	
	$.i18n.setLanguageCookie = function (language) {
		var date = new Date();
	    	date.setTime(date.getTime() + (30 * 24 * 60 * 60 * 1000));
		if($.cookie){
			$.cookie( "language", escape(language),{
				"expires"	: date ,
				"path"		: "/"
			});
		}else{
			document.cookie = "language=" + escape(language) + "; " + "expires=" + date.toUTCString() + "; path=/";
		}
	};
	
	$.i18n.getLanguageCookie = function(){
		if($.cookie){
			return $.cookie("language");
		}else{
			var lan = null;
			var arrStr = document.cookie.split("; ");
			for (var i = 0; i < arrStr.length; i++) {
			    var temp = arrStr[i].split("=");
			    if (temp[0] == 'language') {
			        lan = unescape(temp[1]);
			    }
			}
			return lan;
		}
	};
	
	$.i18n.browserLang = function(){
		return $.i18n.getLanguageCookie() || navigator.language || navigator.userLanguage;
	};
	
	/**
	 * 加载当前功能国际化资源,并对包含属性data-localize的元素进行国际化处理
	 * 如：<label for="" class="col-sm-5 control-label" data-localize="kkbm">开课部门</label>
	 */
	$.i18n.localize = function(settings){
		settings = settings || {};
		$.i18n.properties($.extend(settings,{ 
			path 	: settings.path || _path + "/i18n/", // 资源文件路径
			name	: settings.name || "message",  // 资源文件名称
			cache	: true,  // 指定浏览器是否对资源文件进行缓存，默认为 false
			checkAvailableLanguages: true,
			mode	: settings.mode || "map",// 用 Map 的方式使用资源文件中的值
			language: settings.language || $.i18n.browserLang(),
			encoding: settings.encoding || 'UTF-8',
			callback: function() {
		      	$("[data-localize]").each(function() {
			        var elem = $(this),localizedValue = jQuery.i18n.map[elem.data("localize")];
			        if (elem.is("input[type=text]") || elem.is("input[type=password]") || elem.is("input[type=email]")) {
			        	elem.attr("placeholder", localizedValue);
			        } else if (elem.is("input[type=button]") || elem.is("input[type=submit]")) {
			        	elem.attr("value", localizedValue);
			        } else {
			        	elem.text(localizedValue);
			        }
		      	});
		      	if(jQuery.isFunction(settings.callback)){
		      		settings.callback.call(this);
		      	}
		    }
		}));
	};
	
	$.i18n.extend = function(obj,i18nLabel,i18nKey){
		if($.isArray(obj)){
			$.each( obj || [], function(i, item){
				$.i18n.extend(item,i18nLabel,i18nKey);
			});
		}else if($.isPlainObject(obj)){
			if(obj["colModel"]){
				//{label:'',name:'kcdmbbb_id', index: 'kcdmbbb_id',hidden:true,key:true,align:'center'},
				i18nLabel 	= "label";
				$.each(obj["colModel"], function(i, item){
					i18nKey		= item["index"] || item["name"];
					//对grid列的label值进行国际化
					item[i18nLabel] = $.i18n.prop(i18nKey);
				});
			}else{
				$.each(obj, function(i, item){
					//对指定label值进行国际化
					item[i18nLabel] = $.i18n.prop(i18nKey);
				});
			}
		}
	};
	
	$.fn.extend({
		i18n:function(prefix){
			var i18nKey = ( prefix||"" ) + ($(this).attr("name") || $(this).attr("id"));
			
			
		}
	});
	
}(jQuery));