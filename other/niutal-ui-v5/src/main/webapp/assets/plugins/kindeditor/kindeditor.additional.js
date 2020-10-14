/*
 *	编辑器中使用的全局变量
 *	作者：wandalong
 *  此处的_stylePath 为应用系统必须提供的一个全局变量，表示这样式服务的地址，如：http://127.0.0.1:8080/zfstyle_v5
 * 	此处的_path为应用系统必须提供的一个全局变量，应用服务的地址值为 request.getContextPath()获取的结果;，如：/jwglxt  
 */
//编辑器路径
var editorPath = _stylePath+"/assets/js/plugins/kindeditor",
	//样式文件路径
	//处理文件上传的访问路径
	uploadJson = _path+'/editor/kindeditor_fileUpload.html',
	//浏览服务器上文件的访问路径
	//fileManagerJson = editorPath+'/files.jsp';
	fileManagerJson = _path+'/editor/kindeditor_fileManage.html';

//默认视图
var defaultOption = {
	name:'default',
	resizeType : 0,//0 : 编辑器大小固定 1 : 可以调整编辑器高度 2 ： 可以调整编辑器宽度、高度
	//themeType : 'default',//样式风格
	themesPath: _stylePath + "/assets/js/plugins/kindeditor/themes/",
	pluginsPath:_stylePath + "/assets/js/plugins/kindeditor/plugins/",
	uploadJson : uploadJson,
	fileManagerJson : fileManagerJson,
	allowFileManager : false,//是否允许选择服务器上文件
	formatUploadUrl	: false,//是否格式化返回的上传路径
	filePostName : "file",
	items : [
	    //'code', 程序代码     
	    //'multiimage',批量上传图片     
		'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
		'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 
		'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
		'anchor', 'link', 'unlink', '|', 'about'
	],
	afterCreate: function () {
		this.sync();
		this.focus();
    },
    afterChange: function () {
        this.sync();
    },
    afterBlur: function () {
        this.sync();
    }
};

//简单视图
var simpleOption = {
	name:'simple',
	resizeType : 0,
	themeType : 'simple',//样式风格
	//cssPath : cssPath,
	themesPath: _stylePath + "/assets/js/plugins/kindeditor/themes/",
	pluginsPath:_stylePath + "/assets/js/plugins/kindeditor/plugins/",
	uploadJson : uploadJson,
	fileManagerJson : fileManagerJson,
	allowPreviewEmoticons : false,//是否允许浏览远程服务器文件
	allowImageUpload : true,//是否允许上传图片
	formatUploadUrl	: false,//是否格式化返回的上传路径
	filePostName : "file",
	items : [
		'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
		'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
		'insertunorderedlist', '|', 'emoticons', 'image', 'link', 'about'
	],
	afterCreate: function () {
		this.sync();
		this.focus();
    },
    /*afterChange: function () {
	    this.sync();
	},*/
    afterBlur: function () {
        this.sync();
    }
};


//新闻视图
var newsOption = {
	name:'news',
	resizeType : 0,//0 : 编辑器大小固定 1 : 可以调整编辑器高度 2 ： 可以调整编辑器宽度、高度
	//themeType : 'default',//样式风格
	themesPath: _stylePath + "/assets/js/plugins/kindeditor/themes/",
	pluginsPath:_stylePath + "/assets/js/plugins/kindeditor/plugins/",
	uploadJson : uploadJson,
	fileManagerJson : fileManagerJson,
	allowFileManager : false,//是否允许选择服务器上文件
	formatUploadUrl	: false,//是否格式化返回的上传路径
	filePostName : "file",
	items : [
		'source', '|', 'undo', 'redo', '|', 'preview', 'template', 'cut', 'copy', 'paste',
		'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		'superscript',  'selectall', '|', 'fullscreen', '/',
		'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 
		'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
		'anchor', 'link', 'unlink', '|', 'about'
	],
	afterCreate: function () {
		this.sync();
		this.focus();
    },
    /*afterChange: function () {
        this.sync();
    },*/
    afterBlur: function () {
        this.sync();
    }         
};