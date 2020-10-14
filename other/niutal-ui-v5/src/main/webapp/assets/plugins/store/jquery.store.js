/**
// 存储 'username' 的值为 'marcus'
$.store.set('username', 'marcus')
// 存储 'username' 的值为 'marcus' 过期时间为1000毫秒
$.store.set('username', 'marcus', 1000)
// 获取 'username'
$.store.get('username')
// 移除 'username' 字段
$.store.remove('username')
// 清除所有本地存储
$.store.clear()
// 存储对象 - 自动调用 JSON.stringify
$.store.set('user', { name: 'marcus', likes: 'javascript' })
// 获取存储的对象 - 自动执行 JSON.parse
var user = $.store.get('user')
alert(user.name + ' likes ' + user.likes)
// 从所有存储中获取值
$.store.getAll().user.name == 'marcus'
// 遍历所有存储
$.store.forEach(function(key, val) {
  console.log(key, '==', val)
})
 */
;(function($) {
	 
	$.store = $.store || {};
	
	/**
	 * 1、增加store自动过期实现：LocalStorage 并没有提供过期时间接口，只能通过存储时间做比对实现
	 */
	$.store.set = function(key, val, exp) {
	    if(exp){
	    	store.set(key, {"val": val, "exp": exp, "time": new Date().getTime()});
	    }else{
	    	store.set(key, val);
	    }
	};
	
	$.store.get = function(key) {
	    var info = store.get(key)
	    if (!info) {
	    	return null;
	    }
	    if($.isPlainObject(info) && info["exp"]){
	    	if (new Date().getTime() - info.time > info.exp) {
	    		store.remove(key);
			    return null;
			}
			return info.val
	    }else{
	    	return info;
	    }
	};
	
	$.store.has = function(key) {
		store.has(key);
	};
	 
	$.store.remove = function(key){
		store.remove(key);
	};
	
	$.store.clear = function(){
		store.clear();
	};
	
	$.store.transact = function(key, defaultVal, transactionFn){
		store.transact(key, defaultVal, transactionFn);
	};
	
	$.store.getAll = function(){
		return store.getAll();
	};
	
	$.store.forEach = function(callback){
		store.forEach(callback);
	};
	
	$.store.serialize = function(value){
		store.serialize(value);
	};
	
	$.store.deserialize = function(value){
		store.deserialize(value);
	};
	
}(jQuery));