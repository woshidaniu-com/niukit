<div class="panel margin-b0">
	<div class="collapse" role="tabpanel" id="collapse_${panelId}_${name}" style="display: none;">
		<div class="form-group cond-bar mg">
			<label class="col-sm-1 control-label">已选：</label>
			<div class="col-sm-11">
                <div class="tag-list-cond-blank" style="display: block;">[未选择条件]</div>
				<ul class="tag-list tag-list-cond" type="number" searchname="${name}" >
				</ul>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${text}：</label>
			<div class="col-sm-11">
				<div class="form-inline">
					<div class="form-group form-group-sm">
						<label class="control-label">从</label>
						
						<div class="input-group input-group-sm spinner" data-trigger="spinner">
			                <input type="text" class="form-control"  data-rule="${format}">
			                <div class="input-group-addon">
			                  <a href="javascript:;" class="spin-up" data-spin="up"><i class="fa fa-sort-up icon-sort-up"></i></a>
			                  <a href="javascript:;" class="spin-down" data-spin="down"><i class="fa fa-sort-down icon-sort-down"></i></a>
			                </div>
		               </div>
						
						<label class="control-label">到</label>
						
						<div class="input-group input-group-sm spinner" data-trigger="spinner" >
			                <input type="text" class="form-control"  data-rule="${format}">
			                <div class="input-group-addon">
			                  <a href="javascript:;" class="spin-up" data-spin="up"><i class="fa fa-sort-up icon-sort-up"></i></a>
			                  <a href="javascript:;" class="spin-down" data-spin="down"><i class="fa fa-sort-down icon-sort-down"></i></a>
			                </div>
		               </div>
						
						<button type="button" class="btn zf-btn btn-default btn-sm" name="addNumberBtn" title="添加"> 
							<i class="fa fa-plus"></i> 
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="cond-caret" style="left: 527px;"></div>
	</div>
</div>
