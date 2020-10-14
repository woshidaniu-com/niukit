/**
 * xiaobin.zhang
 */
!
function(a) {
	"use strict";
	
	var _mulitiple_sort_zh_CN = {

	        formatMultipleSort: function() {
	            return '多列排序';
	        },
	        formatAddLevel: function() {
	            return '增加';
	        },
	        formatDeleteLevel: function() {
	            return '删除';
	        },
	        formatColumn: function() {
	            return '列';
	        },
	        formatOrder: function() {
	            return '方式';
	        },
	        formatSortBy: function() {
	            return '排序';
	        },
	        formatThenBy: function() {
	            return '排序';
	        },
	        formatSort: function() {
	            return '确定';
	        },
	        formatCancel: function() {
	            return '取消';
	        },
	        formatDuplicateAlertTitle: function() {
	            return '存在重复的排序列！';
	        },
	        formatDuplicateAlertDescription: function() {
	            return '请删除重复的排序列。';
	        },
	        formatSortOrders: function() {
	            return {
	                asc: '升序',
	                desc: '降序'
	            };
	        }
	    
			
	};
	$.extend($.fn.bootstrapTable.defaults, _mulitiple_sort_zh_CN);
}(jQuery);