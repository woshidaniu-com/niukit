var processModeluerConfig = {
	width		: "800px",
	modalName	: "processModulerModal",
	formName	: "ajaxForm",
	gridName	: "tabGrid",
	offAtOnce	: true,
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					$this.reset();
					if(responseText.indexOf("成功") > -1){
						$.success(responseText,function() {
							if(opts.offAtOnce){
								$.closeModal(opts["modalName"]||"modifyModal");
							}
							var tabGrid = $("#" + opts["gridName"]||"tabGrid");
							//清除页面元素
							if($(tabGrid).size() > 0){
								$(tabGrid).reloadGrid();
							}
						});
					}else if(responseText.indexOf("失败") > -1){
						$.error(responseText,function() {
							
						});
					} else{
						$.alert(responseText,function() {
							
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};