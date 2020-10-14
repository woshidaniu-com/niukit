$(function(){
	
	$('[name="process_defination_view_btn"]').click(function(){
		
		var defintionId = $(this).attr('data-id');
		
		var url = _path + "/diagram-viewer/index.zf" ;
		$.showDialog(url,'流程图查看',$.extend({},viewConfig,{"width":1000,data:{'processDefinitionId':defintionId}}));
		
	});
	
});