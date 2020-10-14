;(function($){
	
	// bitTotal函数
	// 计算出当前密码当中一共有多少种模式
	function bitTotal(num) {
		var modes = 0;
		for ( var i = 0; i < 4; i++) {
			if (num & 1){
				modes++;
			}
			num >>>= 1;
		}
		return modes;
	}

	// CharMode函数
	// 测试某个字符是属于哪一类.
	function CharMode(iN) {
		if (iN >= 48 && iN <= 57){ // 数字
			return 1;
		}if (iN >= 65 && iN <= 90){ // 大写字母
			return 2;
		}if (iN >= 97 && iN <= 122){ // 小写
			return 4;
		}else{
			return 8; // 特殊字符
		}
	}

	// checkStrong函数
	// 返回密码的强度级别
	function checkStrong(sPW) {
		if (sPW.length < 6){
			return 0; // 密码太短
		}
		var Modes = 0;
		for (i = 0; i < sPW.length; i++) {
			// 测试每一个字符的类别并统计一共有多少种模式.
			Modes |= CharMode(sPW.charCodeAt(i));
		}
		return bitTotal(Modes);
	}
	
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	$.bootui.widget.Strength = function(target,options){
		this.initialize.call(this,target,options);	//初始化组件参数
	};
	
	$.bootui.widget.Strength.prototype = {
		/*组件参数*/
		options: {
			version:'1.0.0',/*版本号*/
			target:"",
			strongKey:"",
			afterRender: $.noop/*组件渲染完成后的回调函数*/
		},
		/*初始化组件参数*/
		initialize : function(target,_options) {
			var ctx = this;
			/*初始化默认行为参数*/
			$.extend(true,ctx.options,_options||{},{
				
			});
			
			
			
			/*初始化组件函数*/
			$.extend(true,ctx,{
				getContainer:function(){
					var ctx = this,context = new Array();
					/***********密码强度***********/
					context.push('<table cellSpacing="0" cellPadding="0" border="0" class="resetCss" style="float: left;">');
					context.push('<tbody><tr>');
					context.push('<td style="padding: 3px 0;">' + $.i18n.niutal["utils"]["strength"]["label"] +'：</td>');
					context.push('<td style="color: #808080; font-weight: bold"><font id="passwdRating"></font></td>');
					context.push('</tr><tr><td colSpan="2">');
					context.push('<table id="passwdBar" cellSpacing="0" cellPadding="0" width="160" bgColor="#ffffff" border="0">');
					context.push('<tbody><tr><td id="posBar" width=0% bgColor="#e0e0e0" height="4"></td>');
					context.push('<td id="negBar" width="100%;" bgColor="#e0e0e0" height="4"></td></tr></tbody></table>');
					context.push('</td></tr></tbody></table>');
					/***********密码强度***********/
					return context.join("");
				},
				gff:function (str, pfx) {
					var i = str.indexOf(pfx);
					if (i != -1) {
						var v = parseFloat(str.substring(i + pfx.length));
						if (!isNaN(v)) {
							return v;
						}
					}
					return null;
				},
				compatible:function(){
					/* Checks Browser Compatibility */
					var agt = navigator.userAgent.toLowerCase();
					var is_op = (agt.indexOf("opera") != -1);
					var is_ie = (agt.indexOf("msie") != -1) && document.all && !is_op;
					var is_mac = (agt.indexOf("mac") != -1);
					var is_gk = (agt.indexOf("gecko") != -1);
					var is_sf = (agt.indexOf("safari") != -1);
					var is_mo = (agt.indexOf("mobile") != -1);

					if (is_ie && !is_op && !is_mac) {
						var v = ctx.gff(agt, "msie ");
						if (v != null) {
							return (v >= 6.0);
						}
					}
					if (is_gk && !is_sf) {
						var v = ctx.gff(agt, "rv:");
						if (v != null) {
							return (v >= 1.4);
						} else {
							v = ctx.gff(agt, "galeon/");
							if (v != null) {
								return (v >= 1.3);
							}
						}
					}
					if (is_sf || is_mo) {
						var v = ctx.gff(agt, "applewebkit/");
						if (v != null) {
							return (v >= 124);
						}
					}
					return false;
				}
			});
			this.buildComponents.call(this,target);	//组装组件html元素
			this.renderComponents.call(this,target);//渲染后组装好后的组件,添加css,绑定事件,填充内容
			this.options.afterRender.call(this);	//渲染后的函数回调
		},
		/*构建组件UI*/
		buildComponents : function(target) {
			var ctx = this;
			$(target).each(function(index,container){
				/*do something ...*/
				if($.trim(ctx.options.target).length>0){
					$(ctx.options.target).append(ctx.getContainer.call(ctx));
				}else{
					$(container).after(ctx.getContainer.call(ctx));
				}
			});
		},
		/*渲染后组装好后的组件,添加css,绑定事件,填充内容*/
		renderComponents: function(target) {
			var ctx = this;
			
			var isBrowserCompatible = ctx.compatible();

			//密码强度验证// 假如出现无法检测的状况
			var ratingMsgs = $.i18n.niutal["utils"]["strength"]["ratings"];
			var ratingMsgColors = ["#aa0033","#aa0033",/*"#f5ac00",*/"#6699cc","#093","#093","#676767"];
			var barColors = ["#aa0033","#aa0033",/*"#ffcc33",*/"#6699cc","#093","#093","#676767"];
			
			function DrawBar(rating) {
				var posbar = $('#posBar');
				var negbar = $('#negBar');
				var barLength = $('#passwdBar').outerWidth();
				if (rating >= 0 && rating <= 4) { // We successfully got a rating
					if (rating == 0) {
						posbar.width(barLength / 4 + "px");
						negbar.width(barLength / 4 * (3 - rating) + "px");
					} else {
						posbar.width(barLength / 4 * rating + "px");
						negbar.width(barLength / 4 * (4 - rating) + "px");
					}
				} else {
					posbar.width("0px");
					negbar.width(barLength + "px");
					rating = 5; // Not rated Rating
				}
				posbar.css("background",barColors[rating]);
				$('#passwdRating').html("<font color='" + ratingMsgColors[rating] + "'>" + ratingMsgs[rating] + "</font>");
				$('#strongKey').val(rating);
			}
			
			$(target).each(function(index,container){
				
				var che = 0;
				var min_passwd_len = 6; 
				
				/*do something ...*/
				$(container).keyup(function(e){
					if (!isBrowserCompatible) {
						return;
					}
					if (!$(this).val()){
						return false;
					}
					if ($(this).val().length < min_passwd_len) {
						if ($(this).val().length > 0) {
							DrawBar.call(container,0);
						} else {
							// Resets the password strength bar back to its initial state without any
							// message showing.
							var posbar = getElement('posBar');
							var negbar = getElement('negBar');
							var passwdRating = getElement('passwdRating');
							var barLength = getElement('passwdBar').width;
							posbar.style.width = "0px";
							negbar.style.width = barLength + "px";
							passwdRating.innerHTML = "";
							
						}
					} else {
						// 在长度检测后，检测密码组成复杂度
						rating = checkStrong($(this).val());
						che = rating;
						DrawBar.call(container,rating);
					}
				});
			});
		}
	};
	
	$.fn.extend({
		strength:function(options){
			var strongKey = $.trim(options.strongKey||"yhmmdj");
			$(this).after('<input type="hidden" name="'+strongKey+'" id="strongKey" />');
			return new $.bootui.widget.Strength(this, options);
		},
		pwdStrong:function(){
			var val = $.trim($(this).val());
			return (val && val.length > 0) ? checkStrong(val) : "1";
		}
	});
	
})(jQuery);