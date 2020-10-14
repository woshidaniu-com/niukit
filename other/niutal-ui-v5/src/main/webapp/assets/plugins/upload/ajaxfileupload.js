jQuery.extend({

	handleError: function( options, xhr, status, e ){  
			// If a local callback was specified, fire it  
		    if ( options.error ) {options.error.call( options.context || options, xhr, status, e );}
		    // Fire the global callback  
		    if ( options.global ) {
		        (options.context ? jQuery(options.context) :         
		        jQuery.event).trigger( "ajaxError", [xhr, options, e] );
		    }
	},

	createUploadIframe: function(d, b) {
		var a = "jUploadFrame" + d;
		var c = '<iframe id="' + a + '" name="' + a + '" style="position:absolute; top:-9999px; left:-9999px"';
		if (window.ActiveXObject) {
			if (typeof b == "boolean") {
				c += ' src="javascript:false"'
			} else {
				if (typeof b == "string") {
					c += ' src="' + b + '"'
				}
			}
		}
		c += " />";
		jQuery(c).appendTo(document.body);
		return jQuery("#" + a).get(0)
	},
	createUploadForm: function(g, b) {
		var e = "jUploadForm" + g;
		var a = "jUploadFile" + g;
		var d = jQuery('<form  action="" method="POST" name="' + e + '" id="' + e + '" enctype="multipart/form-data"></form>');
		var c = jQuery("#" + b);
		var f = jQuery(c).clone();
		jQuery(c).attr("id", a);
		jQuery(c).before(f);
		jQuery(c).appendTo(d);
		jQuery(d).css("position", "absolute");
		jQuery(d).css("top", "-1200px");
		jQuery(d).css("left", "-1200px");
		jQuery(d).appendTo("body");
		return d
	},
	ajaxFileUpload: function(k) {
		k = jQuery.extend({}, jQuery.ajaxSettings, k);
		var a = new Date().getTime();
		var b = jQuery.createUploadForm(a, k.fileElementId);
		var i = jQuery.createUploadIframe(a, k.secureuri);
		var h = "jUploadFrame" + a;
		var j = "jUploadForm" + a;
		if (k.global && !jQuery.active++) {
			jQuery.event.trigger("ajaxStart")
		}
		var c = false;
		var f = {};
		if (k.global) {
			jQuery.event.trigger("ajaxSend", [f, k])
		}
		var d = function(l) {
				var p = document.getElementById(h);
				try {
					if (p.contentWindow) {
						f.responseText = p.contentWindow.document.body ? p.contentWindow.document.body.innerHTML : null;
						f.responseXML = p.contentWindow.document.XMLDocument ? p.contentWindow.document.XMLDocument : p.contentWindow.document
					} else {
						if (p.contentDocument) {
							f.responseText = p.contentDocument.document.body ? p.contentDocument.document.body.innerHTML : null;
							f.responseXML = p.contentDocument.document.XMLDocument ? p.contentDocument.document.XMLDocument : p.contentDocument.document
						}
					}
				} catch (o) {
					jQuery.handleError(k, f, null, o)
				}
				if (f || l == "timeout") {
					c = true;
					var m;
					try {
						m = l != "timeout" ? "success" : "error";
						if (m != "error") {
							var n = jQuery.uploadHttpData(f, k.dataType);
							if (k.success) {
								k.success(n, m)
							}
							if (k.global) {
								jQuery.event.trigger("ajaxSuccess", [f, k])
							}
						} else {
							jQuery.handleError(k, f, m)
						}
					} catch (o) {
						m = "error";
						jQuery.handleError(k, f, m, o)
					}
					if (k.global) {
						jQuery.event.trigger("ajaxComplete", [f, k])
					}
					if (k.global && !--jQuery.active) {
						jQuery.event.trigger("ajaxStop")
					}
					if (k.complete) {
						k.complete(f, m)
					}
					jQuery(p).unbind();
					setTimeout(function() {
						try {
							jQuery(p).remove();
							jQuery(b).remove()
						} catch (q) {
							jQuery.handleError(k, f, null, q)
						}
					}, 100);
					f = null
				}
			};
		if (k.timeout > 0) {
			setTimeout(function() {
				if (!c) {
					d("timeout")
				}
			}, k.timeout)
		}
		try {
			var b = jQuery("#" + j);
			jQuery(b).attr("action", k.url);
			jQuery(b).attr("method", "POST");
			jQuery(b).attr("target", h);
			if (b.encoding) {
				jQuery(b).attr("encoding", "multipart/form-data")
			} else {
				jQuery(b).attr("enctype", "multipart/form-data")
			}
			jQuery(b).submit()
		} catch (g) {
			jQuery.handleError(k, f, null, g)
		}
		jQuery("#" + h).load(d);
		return {
			abort: function() {}
		}
	},
	uploadHttpData : function( r, type ) {
	        var data = !type;
	        data = type == "xml" || data ? r.responseXML : r.responseText;
	        // If the type is "script", eval it in global context
	        if ( type == "script" ){
	            jQuery.globalEval( data );
	        }
	        // Get the JavaScript object, if JSON is used.
	        if ( type == "json" ){
	            try {
	            	eval( "data = \" " + data + " \" ");
				} catch (e) {
					data = jQuery.parseJSON(data.replace(/^\<[\w\-\:\;\"\'\= \(\)]+\>|\<\/\w+\>$/ig,''));
				}
	        }
	        // Get the text.
	        if ( type == "text" ){
	        	data = data.replace(/<\/?[^>]*>/g,''); //去除HTML tag
	        	data = data.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
	            //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	        	data = data.replace(/&nbsp;/ig,'');//去掉&nbsp;
	        	data = data.replace(/[\"|\']*/ig,'');//去掉"';
	        }
	        // evaluate scripts within html
	        if ( type == "html" ){
	            jQuery("<div>").html(data).evalScripts();
	        }
	        return data;
	    }
});