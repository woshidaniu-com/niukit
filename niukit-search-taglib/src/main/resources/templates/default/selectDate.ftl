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
						<label class="control-label">从</label>
						<input type="text" class="form-control laydate-icon-zfcolor" id="${panelId}_${name}_start">
						<label class="control-label">到</label>
						<input type="text" class="form-control laydate-icon-zfcolor" id="${panelId}_${name}_end">
						<button type="button" class="btn zf-btn btn-default btn-sm" name="addDateBtn" title="添加"> <i class="fa fa-plus"></i> </button>
					</div>
				</div>
			</div>
		</div>
		<div class="cond-caret" style="left: 527px;"></div>
	</div>
</div>
<script type="text/javascript">
	var ${panelId}_${name}_start = {
	  elem: '#${panelId}_${name}_start',
	  format: '${format}',
	  choose: function(datas){
	     ${panelId}_${name}_end.min = datas; //开始日选好后，重置结束日的最小日期
	     ${panelId}_${name}_end.start = datas //将结束日的初始值设定为开始日
	  }
	};
	var ${panelId}_${name}_end = {
	  elem: '#${panelId}_${name}_end',
	  format: '${format}',
	  choose: function(datas){
	    ${panelId}_${name}_start.max = datas; //结束日选好后，重置开始日的最大日期
	  }
	};
	laydate(${panelId}_${name}_start);
	laydate(${panelId}_${name}_end);
</script>