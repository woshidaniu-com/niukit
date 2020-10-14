<div class="panel margin-b0">
	<div class="collapse" role="tabpanel" id="collapse_${panelId}_${name}" style="display: none;height:325px;">
		<div class="form-group cond-bar mg">
			<label class="col-sm-1 control-label">已选：</label>
			<div class="col-sm-11">
                <div class="tag-list-cond-blank" style="display: block;">[未选择条件]</div>
				<ul class="tag-list tag-list-cond" type="date" searchname="${name}" format="${format}">
				</ul>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${text}：</label>
			<div class="col-sm-11">
				<div class="form-inline">
					<div class="form-group form-group-sm">
						<label class="control-label"></label>
						<input type="text" class="form-control laydate-icon-zfcolor" id="${panelId}_${name}_range" style="width:280px;">
						<button type="button" class="btn zf-btn btn-default btn-sm" name="addDateRangeBtn" title="添加"> <i class="fa fa-plus"></i> </button>
					</div>
				</div>
			</div>
		</div>
		<div class="cond-caret" style="left: 527px;"></div>
	</div>
</div>
<script type="text/javascript">

	var ${panelId}_${name}_range = {
	  elem: '#${panelId}_${name}_range',
	  theme: 'zfcolor',
	  type:"${type}",
	  range:'~'
	};
	
	laydate.render(${panelId}_${name}_range);
	
</script>
    