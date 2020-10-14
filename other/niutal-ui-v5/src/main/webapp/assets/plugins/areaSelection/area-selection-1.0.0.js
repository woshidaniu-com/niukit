(function($){
	var html='<div class="area-selection">'+
	    '<button type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
	    '<div class="area-selection-box">'+
	        '<ul class="nav nav-tabs" role="tablist">'+
	            '<li role="presentation" class="active"><a href="#province" role="tab">省份</a></li>'+
	            '<li role="presentation" class="hide"><a href="#city" role="tab">城市</a></li>'+
	            '<li role="presentation" class="hide"><a href="#county" role="tab">县区 </a></li>'+
	        '</ul>'+
	        '<div class="tab-content">'+
	            '<div role="tabpanel" class="tab-pane fade in active" id="province">'+
	                '<ul class="tag-list" data-type="province">'+
	                '</ul>'+
	            '</div>'+
	            '<div role="tabpanel" class="tab-pane fade" id="city">'+
	                '<ul class="tag-list" data-type="city">'+	
	                '</ul>'+
	            '</div>'+
	            '<div role="tabpanel" class="tab-pane fade" id="county">'+
	                '<ul class="tag-list" data-type="county">'+
	                '</ul>'+
	            '</div>'+
	        '</div>'+
	    '</div>'+
	'</div>';
	
	var AreaSelection = function (el, options) {
        this.options = options;
        this.$el = $(el);
        if ($(".area-selection").size()==0) {
        	$("body").append(html);
        }
        this.init(options);
    };

	AreaSelection.DEFAULTS={
		splitChar:'',
		level:'3',
		dataURL:_path+'/xtgl/xzqh/getAreaSelectionData.zf',
		jsonData:{
			parent:[
		    	//{dm:'110000',mc:'北京市'},
		    	//{dm:'130000',mc:'河北省'}
	    	],
	    	childrens:{
	    		//110000:[{dm:'110101',mc:'东城区'},{dm:'110102',mc:'西城区'}],
	    		//130200:[{dm:'130202',mc:'路南区'},{dm:'130203',mc:'路北区'}]
	    	}
		},
		getSelectData:function(el){
			return $(el).data("data-area");
		}
	}


	//初始化行政区划数据元素
	var _initData = function (el,arr, type, tag_list) {
        var $t = $('<li><a href="" role="tab" data-toggle="tab" class="transition-common"></a></li>');
        var $tl = tag_list.empty();
        for(var i = 0,j=arr.length; i < j ; i++){
            var $tt = $t.clone();
            $tt.find('a').attr('href','#'+type).text(arr[i]["mc"]);
            $tt.find('a').attr('data-dm',arr[i]["dm"]);
            $tl.append($tt);
        }
        $tl.find('li>a').off().click(function(e) {
            e.preventDefault();
            $('.area-selection .area-selection-box .nav>li>a').removeAttr('data-toggle');
            $('.area-selection .area-selection-box .nav>li>a>.badge').remove();
            $(this).parent('li').removeClass('active');
            $(this).closest('.tag-list').find('li>a').removeClass('cur');
            $(this).addClass('cur');
			
            var retStr = ''; // 返回的字符串，请自行调整
            var parentDm=$(this).attr("data-dm");
            var childrenArr =AreaSelection.DEFAULTS.jsonData.childrens;
            if ($(this).closest('.tag-list').data('type') == 'province' && childrenArr[parentDm] != undefined && Number(AreaSelection.DEFAULTS.level) > 1) {
            	//显示市
               _initData(el,childrenArr[parentDm],'county',$('.area-selection .area-selection-box .tag-list[data-type="city"]')); 
            } else if($(this).closest('.tag-list').data('type') == 'city' && childrenArr[parentDm] != undefined && Number(AreaSelection.DEFAULTS.level) > 2){
            	//显示县
              _initData(el,childrenArr[parentDm],'county',$('.area-selection .area-selection-box .tag-list[data-type="county"]'));
            } else { 
            	//处理选中结果
            	var curArr = $(this).closest('.tab-content').find('.tag-list>li>a.cur');
                curArr.each(function (i,n) {
                    retStr+=$(this).text();
                    if (curArr.length > i+1){
                		retStr+=AreaSelection.DEFAULTS.splitChar;
                	}
                });
                
                el.val(retStr);
                el.data('data-area', $(this).attr("data-dm"))
                $('.area-selection').hide().css({'top':0,'left':0});
            }
            var $navli = $('.area-selection .area-selection-box .nav>li');
            if ($navli.find('a[href="' + $(this).attr('href') + '"]').length > 0) {
                $navli.removeClass('active');
                $navli.find('a[href="' + $(this).attr('href') + '"]').parent('li').addClass('active');
            }
            $navli.find('a[href="#'+$(this).closest('.tag-list').data('type')+'"]').closest('li').prevAll('li').find('a').attr('data-toggle','tab');
            $navli.find('a[href="#'+$(this).closest('.tag-list').data('type')+'"]').attr('data-toggle','tab');
        });
    };

	AreaSelection.prototype.init=function(options){
		var $this = this.$el;
		$this.css({'cursor':'pointer'});
		$this.after('<span class="glyphicon glyphicon-remove input-group-input-remove"></span>');
		$this.focus(function(){
			var $node = $(this);
	        $('.area-selection .area-selection-box .nav>li>a').removeAttr('data-toggle').click(function (e) {
	            e.preventDefault();
	        });
	        $('.area-selection .area-selection-box .nav>li>a>.badge').remove();
	        $('.area-selection .close').off().click(function () {
	           $('.area-selection').hide().css({'top':0,'left':0});
	        });
	        $('.area-selection .area-selection-box .tag-list').empty(); // 清空数据
	        _initData($this,options.jsonData.parent,'city',$('.area-selection .area-selection-box .tag-list[data-type="province"]'));
	        $('.area-selection .area-selection-box .nav>li').removeClass('active');
	        $('.area-selection .area-selection-box .nav>li:first').addClass('active');
	        $('.area-selection .area-selection-box .tab-pane').removeClass('active').removeClass('in');
	        $('.area-selection .area-selection-box .tab-pane:first').addClass('active').addClass('in');
	
	        if (Number(AreaSelection.DEFAULTS.level) > 1){
	        	$('.area-selection .area-selection-box .nav>li:eq(1)').removeClass('hide');
	        }
	        if (Number(AreaSelection.DEFAULTS.level) > 2){
	        	$('.area-selection .area-selection-box .nav>li:eq(2)').removeClass('hide');
	        }
	        
	        var eh = $node.outerHeight();
	        var ew = $node.outerWidth();
	        $('.area-selection').offset($node.offset());
	        $('.area-selection').css({
	            'top':function () {
	                return parseFloat($(this).css('top')) + eh; 
	            },
	            'width':ew
	        }).slideDown(200);
		});//focus end
		
		$('.area-selection').on('mouseleave',function(){
			$('.area-selection').hide().css({'top':0,'left':0});
			$this.blur();
		});
		
		/*以下增加仿IE10输入框清空功能*/
        var inputTimer;
        $('.input-group-input>.form-control').on('keyup focus',function () {
            clearTimeout(inputTimer);
            var $this = $(this);
            if($this.val() != ''){
                $this.siblings('.input-group-input-remove').show();
                $this.siblings('.input-group-input-remove').click(function () {
                    $this.val('');
                    $this.data('data-area','')
                });
            }
        }).blur(function () {
            var $this = $(this);
            inputTimer = setTimeout(function () {
                $this.siblings('.input-group-input-remove').off('click');
                $this.siblings('.input-group-input-remove').hide();
            },150);
        });
        /*以上增加仿IE10输入框清空功能*/
	}
	
	$.fn.areaSelection=function(option){
		var value;
		var el = this;
		if (typeof option === 'string') {
			value = $.fn.areaSelection.defaults[option].apply(this,el); 
		}
    	var options = $.extend(AreaSelection.DEFAULTS, option);
    	if (options.dataURL != '' && $(".area-selection").size()==0){
        	$.getJSON(options.dataURL,{},function(data){
        		var jsonData = {
        			parent:data["parent"],
        			childrens:data["childrens"]
        		}
        		$.extend(options, {jsonData:jsonData});
        	});
        }
    	return value || new AreaSelection(this, options);
    }
	$.fn.areaSelection.Constructor = AreaSelection;
    $.fn.areaSelection.defaults = AreaSelection.DEFAULTS;
}(jQuery));