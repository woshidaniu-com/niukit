;(function($) {

	/*
	   1KB(Kilobyte 千字节)=1024B， 
	　　 1MB(Megabyte 兆字节 简称“兆”)=1024KB， 
	　　 1GB(Gigabyte 吉字节 又称“千兆”)=1024MB， 
	　　 1TB(Trillionbyte 万亿字节 太字节)=1024GB， 
	　　 1PB（Petabyte 千万亿字节 拍字节）=1024TB， 
	　　 1EB（Exabyte 百亿亿字节 艾字节）=1024PB， 
	　　 1ZB(Zettabyte 十万亿亿字节 泽字节)= 1024 EB， 
	　　 1YB(Yottabyte 一亿亿亿字节 尧字节)= 1024 ZB， 
	　　 1BB(Brontobyte 一千亿亿亿字节)= 1024 YB 
	　　 注意：MiB和MB，KiB和KB等的区别： 
	　　 1KiB(kilobyte)=1024byte 
	　　 1KB(kibibyte)=1000byte 
	　　 1MiB(megabyte)=1048576byte 
	　　 1MB(mebibyte)=1000000byte
	*/
	var defaults = {
		"regex":/^([1-9]\d*|[1-9]\d*.\d*|0.\d*[1-9]\d*)(B|KB|MB|GB|TB|PB|EB|ZB|YB|BB)$/,
		"powers":{
			'B' : 1,
			//1KB(Kilobyte 千字节)=1024B
			'KB': 1024,	
			//1MB(Megabyte 兆字节 简称“兆”)=1024KB
			'MB': 1024 * 1024,					
			//1GB(Gigabyte 吉字节 又称“千兆”)=1024MB
			'GB': 1024 * 1024 * 1024,
			//1TB(Trillionbyte 万亿字节 太字节)=1024GB
			'TB': 1024 * 1024 * 1024 * 1024/*,
			//1PB（Petabyte 千万亿字节 拍字节）=1024TB
			'PB': 1024 * 1024 * 1024 * 1024 * 1024,
		　　//1EB（Exabyte 百亿亿字节 艾字节）=1024PB
			'EB': 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
		　　//1ZB(Zettabyte 十万亿亿字节 泽字节)= 1024 EB
			'ZB': 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
		　　//1YB(Yottabyte 一亿亿亿字节 尧字节)= 1024 ZB
			'YB': 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
		　　//1BB(Brontobyte 一千亿亿亿字节)= 1024 YB 
			'BB': 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024*/
		}
	};
	
	//获得容量换算后的值
	$.capacity = function(size,unit){
		if (!$.founded(size)) {
			return 0;
		}
		size =  $.trim(size.toString());
		if(!$.founded(unit)){
			size =  size.toUpperCase();
			var result = defaults.regex.exec(size);
			if(result&&result[2]) {
				var num = parseFloat(result[1]);
				var mult = defaults.powers[result[2]] || 1;
				return num * mult;
			} else {
				return size;
			}
		}else if($.isNumeric(size)){
			size = parseFloat(size);
			if(size < 1024){
				return size + 'B';
			}else{
				var unit_val = defaults.powers[unit.toUpperCase()];
				return (Math.round(size * 100 / unit_val) / 100).toString() + unit.toUpperCase();
			}
		}
		return 0;
	}
	
	/**
	 * 文件筛选；"*.*"；多个用;分割，如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
	 * 兼容正则写法： /.js/
	 * 
	 * */
    $.fileMatch = function(matchRule,filename,filetype) {
    	//alert("matchRule:"+matchRule+",filename:"+filename+",filetype:"+filetype);
		var type = filetype&&$.trim(filetype).length>0 ? $.trim(filetype) :"";
		var type_split = type.split('/');
			type = type_split[type_split.length - 1].toLowerCase();
			filename = (filename||"").toLowerCase();
    	if($.type(matchRule) === "regexp"){
    		if(matchRule.test(filename)||matchRule.test(type)){
    			return true;
    		}
    		return false;
    	}else{
    		//如果不是数组,变成数组重新调用过滤函数
    		if(!$.isArray(matchRule)){
    			return  $.fileMatch($.makeArray(matchRule),filename,filetype)
    		}
    		//是否匹配的标识
    		var isMatch = false;
    		//循环规则数组
    		$.each(matchRule,function(i,rule){
    			if($.type(rule) === "regexp"){//数组元素是正则表达式
    				isMatch = $.fileMatch(rule,filename,filetype)
    			}else if($.type(rule) === "string" && $.trim(rule).length > 0){//数组元素是普通字符串
    				//分割字符串；兼容多个规则一条字符串；如：*.gif;*.png;*.jpg;*.jpeg;*.bmp
    				var rule_arr = rule.split(';');
	        		//循环分割后的字符串规则
    				$.each(rule_arr,function(j,rule_item){
    					rule_item = $.trim(rule_item||"");
    					if(rule_item.length > 0 && rule_item.indexOf(".") > -1){
    						//规则单项如： *.* 或  .js 或 zzzz.* 或  *.gif 或者 *xxx.*
    						if(rule_item=="*.*"){
		        				isMatch = true;
		        			}else{
		        				if(rule_item.indexOf(".")!=0){
		        					rule_item = rule_item.replace("\.","\\.");
		        				}
			        			var regex = "";
			        			if(rule_item.indexOf("*")==0){
			        				rule_item = rule_item.substring(1);
			        				if(rule_item.indexOf(".")!=0){
			        					regex += ".";
			        				}
			        			}
			        			if(rule_item.lastIndexOf("*")==(rule_item.length - 1)){
			        				rule_item = rule_item.substring(0,(rule_item.length - 1));
			        			}
			        			regex += rule_item + "$";
			        			//alert("matchRegExp:"+regex);
		        				//创建正则
			        			var matchRegExp =  new RegExp(regex, "g");
			        			if(matchRegExp.test(filename)||matchRegExp.test(type)){
			        				isMatch = true;
			        			}
		        			}
    					}
        				//匹配一个即表示匹配给出的规则；不需要继续循环判断
            			if(isMatch){
            				return false;
            			}
	        		});
    			}
    			//匹配一个即表示匹配给出的规则；不需要继续循环判断
    			if(isMatch){
    				return false;
    			}
    		});
	    	return isMatch;
    	}
	};
	
	
	
	$.fn.extend({
		elementBlur:function(){
			/*
			 在IE9下，如果file控件获得焦点，则document.selection.createRange()拒绝访问，因此，只需要在this.select()后面加一句this.blur()即可
			但是，如果当前页面被嵌在框架中，则this.blur()之后，file控件中原本被选中的文本将会失去选中的状态，因此，不能使用this.blur()。
			可以让当前页面上的其他元素，如div，button等获得焦点即可，如div_view.focus()。
			注意，如果是div，则要确保div有至少1像素的高和宽，方可获得焦点。
			*/
			if(self.frameElement.tagName=="IFRAME"){
				// 页面在iframe中时处理
				//在页面中添加  
				var focusDiv = self.document.getElementById("focusDiv");
				if(!focusDiv){
					$('<div id="focusDiv" style="width: 1px; height: 1px;"></div>').appendTo(self.document.body)
					focusDiv = self.document.getElementById("focusDiv");
				}
				$(focusDiv).focus(); 
			}else{
				this.blur();
			}
		},
		/*
		 * 获得File列表对象集合；这里优先使用 File API 兼容旧的读取方式
		 * 
		 * 文件对象属性：
		 * 
		 * 	 name //文件名称
		 * 	 type //文件类型
		 * 	 size // 文件大小（单位：字节）
		 *   lastModifiedDate  // 上次修改时间
		 *   
		 * IE浏览器特有属性：
		 * 	datecreated // 创建时间
		 * 	dateLastAccessed //上次访问时间
		 * 	dateLastModified // 上次修改时间
		 * 	path //文件路径
		 *  parentFolder //父目录
		 *  rootFolder // 根目录 
		 *  
		 */
		getFiles:function(){
			if ($(this[0]).is(":file")) {
				var files = this[0].files;
				if(!files){
					//进行非html5浏览器的兼容处理
					files = function(){
						//ie浏览器
						if($.browser.msie===true){
							try{
				    			var fileSystem = new ActiveXObject("Scripting.FileSystemObject"); 
				    			var file = null;
				    			if($.browser.version <= 6){// ie6以下版本
					    			file = fileSystem.GetFile(this.value)
				    			}else if($.browser.version >= 7 ){// >= ie7
				    				this.select();
				    				//兼容IE9失去焦点
				    				$(this).elementBlur();
				    				file = fileSystem.GetFile(document.selection.createRange().text); 
				    			}
							}catch(e){
			 			        alert("请修改IE浏览器ActiveX安全设置为启用~！");
			 			    } 
			    			return [file];
						}
		    		}.call(this[0]);
				}
				return files;
			}
			return [];
		},
		getFileCount:function(){
			return $(this).getFiles().length;
		},
		/**
		 * 获得文件大小：（单位：字节）;如果是多文件，则返回结果是数组
		 */
		getFileSize:function(){
			if ($(this[0]).is(":file")) {
				var files = $(this).getFiles();
				if(files.length > 0 ){
					if(files.length > 1 ){
						var filesizes = [];
						$.each(this.files||[],function(i,file){
							try {
								filesizes.push(files.item(i).fileSize);
							} catch (e) {
								filesizes.push(file.size);
							}
						});
						return filesizes.join(";");;
					}else{
						try {
							return files.item(0).fileSize;
						} catch (e) {
							return files[0].size;
						}
					}
				}
			}
			return 0;
		},
		getFileName:function(index){
			if ($(this[0]).is(":file")) {
				var files = $(this).getFiles();
				if(files.length > 0 ){
					if(files.length > 1 ){
						var filenames = [];
						$.each(this.files||[],function(i,file){
							try {
								filenames.push(files.item(i).fileName);
							} catch (e) {
								filenames.push(file.name);
							}
						});
						return filenames.join(";");
					}else{
						try {
							return files.item(0).fileName;
						} catch (e) {
							return files[0].name;
						}
					}
				}
			}
			return 0;
		},
		getFileFullPath:function() {
			var filePath = "";
			if ($(this[0]).is(":file")) {
				var files = this[0].files;
				if (!files) { 
		    		//ie浏览器
		    		if($.browser.msie){
		    			if($.browser.version <= 6){// <= ie6
			    			filePath = this[0].value; 
		    			}else if($.browser.version >= 7){// >= ie7
		    				this.select();
		    				//兼容IE9失去焦点
		    				$(this).elementBlur();
				        	filePath = document.selection.createRange().text; 
		    			}
			        }else{
			        	filePath = this[0].value;
			        }
		    	} else{
		    		
		    		// firefox 等其他标准W3c
		    		var paths = [];
		    		for ( var i = 0; i < files.length; i++) {
		    			try {
							filePath = files.item(i).getAsDataURL();
						} catch (e) {
							//升级到Firefox7.0后，需要改为： window.URL.createObjectURL(obj.files[0]);实际上是Firefox7.0废弃了item属性。
							filePath = window.URL.createObjectURL(files[i]);
						}
						paths.push(filePath);
					}
		    		filePath = paths.join(";");
		        }
			}
			return filePath;
		},
		//获得文件上传控件的值
		getFilePath:function() {
			var filePath = "";
			if ($(this[0]).is(":file")) {
				var files = $(this).getFiles();
				var paths = [];
				$.each(files||[],function(i,file){
					try {
						filePath = (file.path || files.item(i).getAsDataURL());
					} catch (e) {
						//升级到Firefox7.0后，需要改为： window.URL.createObjectURL(obj.files[0]);实际上是Firefox7.0废弃了item属性。
						filePath = window.URL.createObjectURL(file);
					}
					paths.push(filePath);
				});
				filePath = paths.join(";");
			}
			return filePath;
		},
		/**
		 * 获得文件后缀名
		 */
		getFileSuffix:function(){
			var filePath = $(this).getFilePath();
			if ($.founded(filePath)) {
				var filePaths = filePath.split(";");
				var suffixs = [];
				$.each(filePaths||[],function(i,path){
					//获取文件后缀
					suffixs.push(path.substr(path.lastIndexOf(".")).toLowerCase());
				});	
				return suffixs.join(";");
			}else{
				return null;
			}
		},
		isSuffix : function (matchSuffix) {
			if ($(this[0]).is(":file")) {
				var fileSuffix = $(this).getFileSuffix();
				if ($.founded(fileSuffix) && $.founded(matchSuffix)) {
					var fileSuffixs = fileSuffix.split(";");
					var isSuffix = false;
					$.each(fileSuffixs||[],function(i,suffix){
						isSuffix = $.fileMatch(matchSuffix,suffix,suffix);
						//如果有不匹配则退出循环
						return isSuffix;
					});	
					return isSuffix;
				}else{
			    	return false;
			    }
		    }else{
		    	return false;
		    }
		},
		/*
		 * 清空文件上传控件的值
		 * 		表单中type=file字段的value属性无法由js来附值,一但选中某个文件后,如果用户不手动去清空那么这个值将保留,
		 * 		提交表单时对应文件也会被提交上去.当然在服务器上会再次验证,不过为了避免上传不必要的的文件,还是有必要提前在客户端验证失败后将文件上传字段清空.
		 * 
		 * */
		clearFile:function () {
			var $this = $(this[0]);
			if ($($this).is(":file")) {
				var cloneElement = $($this).clone(true,true).val("");
				try {
					this.select();
				    document.execCommand("delete");
				} catch (e) {
					$this.after(cloneElement);
					$this.remove();
				}
			}
		}, 
		//返回文件的二进制格式。
		getAsBinary:function(index){
			if ($(this[0]).is(":file")) {
				try {
					//火狐 7.0 版本的方法
					return this[0].files.item(index||0).getAsBinary()
				} catch (e) {
					// 检查是否支持FileReader对象
					if (typeof FileReader != 'undefined') {
						var reader = new FileReader();
		                //异步方式，不会影响主线程
		                reader.readAsBinaryString(this[0].files[index||0]);
		                reader.onload = function(e) {
		                    var urlData = this.result;
		                    //alert(urlData);
		                    //document.getElementById("result").innerHTML += urlData;
		                };
					}
				}
			}
			return null;
		},
		//返回一个DataURL链接，可以作为<img />标签的src属性值
		getAsDataURL:function(index){
			if ($(this[0]).is(":file")) {
				try {
					//火狐 7.0 版本的方法
					return this[0].files.item(index||0).getAsDataURL()
				} catch (e) {
					// 检查是否支持FileReader对象
					if (typeof FileReader != 'undefined') {
						var reader = new FileReader();
						reader.readAsDataURL(this[0].files[index||0]);
						reader.onload = function(e) {
		                    var urlData = this.result;
		                    //alert(urlData);
		                   // document.getElementById("result").innerHTML += urlData;
		                };
					}
				}
			}
			return null;
		},
		//返回文件文本
		getAsText:function(index,encoding){
			if ($(this[0]).is(":file")) {
				try {
					//火狐 7.0 版本的方法
					return this[0].files.item(index||0).getAsText(encoding||"utf-8")
				} catch (e) {
					// 检查是否支持FileReader对象
					if (typeof FileReader != 'undefined') {
						var reader = new FileReader();
						reader.readAsDataURL(this[0].files[index||0],encoding||"utf-8");
						reader.onload = function (e) {
		                    var urlData = this.result;
		                    //document.getElementById("result").innerHTML += urlData;
		                };
					}
				}
			}
			return null;
		}
	});
	

})(jQuery);

