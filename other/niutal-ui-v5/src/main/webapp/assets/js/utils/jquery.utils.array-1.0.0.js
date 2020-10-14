var extendArr = {
	/**[].agv()
	 * @discretion: 获取此数组的平均值avg
	 * @method : avg
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 平均值(avg = sum / length )
	 */
	avg : function() {
		var sum = 0;
		for (var i = 0; i < this.length; i++) {
			sum += this[i];
		}
		return sum / this.length;
	},
	/**
	 * @discretion: 获取此数组的极差d
	 * @method : d
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 极差(D=Xmax - Xmin)
	 */
	d : function() {
		if (this.length > 0) {
			return this.max() - this.min();
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的最大值max
	 * @method : max
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 最大值max
	 */
	max : function() {
		if (this.length > 0) {
			var max = this[0];
			for (var i = 1; i < this.length; i++) {
				max = Math.max(max, this[i]);
			}
			return max;
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的最小值min
	 * @method : min
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 最小值min
	 */
	min : function() {
		if (this.length > 0) {
			var min = this[0];
			for (var i = 1; i < this.length; i++) {
				min = Math.min(min, this[i]);
			}
			return min;
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的分布峰度Ku
	 * @method : ku
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 分布峰度Ku
	 */
	ku : function() {
		if (this.length > 0) {
			return 0;
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的标准差σ
	 * @method : σ
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 标准差 σ =方差s^2的算术平方根
	 */
	σ : function() {
		if (this.length > 0) {
			return Math.sqrt(this.s());
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的方差s^2
	 * @method : s
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : s^2=[(x1-x)^2 +(x2-x)^2 +......(xn-x)^2]/n (x为平均数)
	 *         例如：4,8,6,2，方差为5.
	 */
	s : function() {
		if (this.length > 0) {
			var avg = this.avg();
			var sum = 0;
			for (var index = 1; index < this.length; index++) {
				sum += (this[index - 1] - avg)
						* (this[index] - avg);
			}
			return sum / this.length;
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的分布偏态SK
	 * @method : sk
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 分布偏态SK=(M-Me) /σ
	 * @notice : M = avg
	 */
	sk : function() {
		if (this.length > 0) {
			return (this.avg() - this.me()) / this.σ();
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的中位数me
	 * @method : me
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 当一组数字的个数是偶数的时候，是一组数字顺序排列后中间2位的数字的平均值
	 * @notice : 当一组数字的个数是奇数的时候，是一组数字顺序排列后中间1位的数字
	 */
	me : function() {
		if (this.length > 0) {
			if (this.length % 2 == 0) {
				return [this[this.length / 2 - 1],
						this[this.length / 2]].avg();
			} else {
				return this[(this.length - 1) / 2];
			}
		} else {
			return null;
		}
	},
	/**
	 * @discretion: 获取此数组的众数mo
	 * @method : me
	 * @return : {Number}
	 * @notice : 该数组必须是一个数字数组
	 * @notice : 一般来说，一组数据中，出现次数最多的数就叫这组数据的众数。 例如：1，2，3，3，4的众数是3。
	 *         但是，如果有两个或两个以上个数出现次数都是最多的，那么这几个数都是这组数据的众数。
	 *         例如：1，2，2，3，3，4 的众数是2和3。 还有，如果所有数据出现的次数都一样，那么这组数据没有众数。
	 *         例如：1，2，3，4，5没有众数。
	 */
	mo : function() {
		var r_mo = new Array(0);
		if (this.length > 0) {
			var times = new Array(this.length);
			for (var i = 0; i < this.length; i++) {
				times[i] = 0;
				for (var j = 0; j < this.length; j++) {
					if (this[i] == this[j]) {
						times[i]++;
					}
				}
			}
			// 检测是否每个数字出现的次数都一样，即每个数字出现一次
			var eq = false;
			for (var i = 0; i < times.length; i++) {
				if (1 == times[i]) {
					eq = true;
				} else {
					eq = false;
					break;
				}
			}
			// 如果不是每个数字出现一次,就会存在众数
			if (!eq) {
				var maxTime = times.max();
				for (var i = 0; i < this.length; i++) {
					if (times[i] == maxTime) {
						r_mo.push(this[i]);
					}
				}
			}
		}
		return r_mo;
	},
	// ----数组排序------------------
	/**
	 * @discretion: 数组正序
	 * @method : asc
	 * @param : {fn, bind} 
	 * @return : {Array} 排序后的数组
	 */
	asc : function(fn, bind) {
		var scope = bind || window;
		var arr = Array(this);
		if (fn) {
			return arr.sort(function(a, b) {
				return fn.call(scope, a, b, arr);
			});
		} else {
			var flag = arr.every(function(i,element){
				return !isNaN(element);
			});
			if(flag){
				return arr.sort(function(a, b) {
					return a - b;
				});
				
			}else{
				return arr.sort(function(a, b) {
                    return String(a).localeCompare(String(b));
                });
			}
		}
	},
	/**
	 * @discretion: 数组倒序
	 * @method : asc
	 * @param : {fn, bind} 
	 * @return : {Array} 排序后的数组
	 */
	desc : function(fn, bind) {
		var scope = bind || window;
		var arr = Array(this);
		if (fn) {
			return arr.sort(function(a, b) {
				return fn.call(scope, b, a, arr);
			});
		} else {
			var flag = arr.every(function(i,element){
				return !isNaN(element);
			});
			if(flag){
				return arr.sort(function(a, b) {
					return b - a;
				});
			}else{
				return arr.sort(function(a, b) {
                    return String(b).localeCompare(String(a));
                });
			}
		}
	},
	// ----数组操作--------------------
	/**
	 * @discretion: 将另一个数组中的所有元素纳入本数组
	 * @method : extend
	 * @param : {Array} 纳入源数组
	 * @return : {Array} 纳入新项后的主调数组
	 */
	extend : function(array) {
		var arr = Array(this);
		$.each(array||{},function(item, index) {
			arr.push(item);
		});
		return arr;
	},
	/**
	 * @discretion: 各类库中都实现相似的each方法
	 * @method : each
	 * @param :
	 *            fn(item, index, array) (function) 每次迭代中执行的函数.
	 *            当前迭代项及它的索引号将被作为参数传入该函数
	 * @param :
	 *            bind (object, 可选) 函数中this所引用的对象.
	
	each : function(fn, bind) {
		var scope = bind || window;
		for (var i = 0, j = this.length; i < j; ++i) {
			fn.call(scope, this[i], i, this);
		}
	}, */
	/**
	 * @discretion: 如果数组中的每个元素都能通过给定的函数的测试，则返回true，反之false。
	 *              换言之给定的函数也一定要返回true与false
	 * @method : every
	 * @return : {Boolean}
	 */
	every : function(fn, bind) {
		var scope = bind || window;
		for (var i = 0, j = this.length; i < j; ++i) {
			if (!fn.call(scope, i, this[i], this)) {
				return false;
			}
		}
		return true;			
	},
	/**
	 * @discretion: 类似every函数，但只要有一个通过给定函数的测试就返回true
	 * @method : some
	 * @return : {Boolean}
	 */
	some : function(fn, bind) {
		var scope = bind || window;
		for (var i = 0, j = this.length; i < j; ++i) {
			if (fn.call(scope, i, this[i], this)) {
				return true;
			}
		}
		return false;
	},
	/**
	 * @discretion: 把符合条件的元素放到一个新数组中返回
	 * @method : filter
	 * @return : {Array}
	 */
	filter : function(fn, bind) {
		var scope = bind || window;
		var a = [];
		for (var i = 0, j = this.length; i < j; ++i) {
			if (!fn.call(scope, i, this[i], this)) {
				continue;
			}
			a.push(this[i]);
		}
		return a;
	},
	/**
	 * @discretion: 根据给出的 '比对函数'判断整个数组的子元素是否满足比对函数
	 * @method : compare
	 * @return : {Array}
	 */
	compare : function(fn, bind) {
		var scope = bind || window;
		var flag = true;
		for (var i = 0; i < this.length - 1; i++) {
			flag = fn.call(scope, this[i], this[i + 1]);
			if (!flag) {
				break;
			}
		}
		return flag;
	},
	/**
	 * @discretion: 清空数组
	 * @method : empty
	 * @return : (array) 清空后的主调数组
	
	empty : function() {
		this.splice(0, this.length);
		return this;
	}, */
	shuffle : function() {
		var arr = this;
		for(var j, x, i = arr.length; i; j = parseInt(Math.random() * i), x = arr[--i], arr[i] = arr[j], arr[j] = x);
		return arr;
	},
	unique : function(){
		var arr = this,
			newArr = [],
			origLen = arr.length,
			found,
			x, y;
		for ( x = 0; x < origLen; x++ ) {
			found = undefined;
			for ( y = 0; y < newArr.length; y++ ) {
				if ( arr[x] === newArr[y] ) { 
				  found = true;
				  break;
				}
			}
			if ( !found) newArr.push( arr[x] );    
		}
		return newArr;	
	},		 
	// ----数组检测--------------------
	/**
	 * @discretion: 返回元素在数组的索引，没有则返回-1。与string的indexOf方法差不多
	 * @method : indexOf
	 * @return : {Number}
	 */
	indexOf : function(el, start) {
		var start = start || 0;
		for (var i = 0; i < this.length; ++i) {
			if (this[i] === el) {
				return i;
			}
		}
		return -1;
	},
	/**
	 * @discretion: 返回元素在数组中最后一次出现的索引，没有则返回-1。与string的lastIndexOf方法差不多
	 * @method : lastIndexOf
	 * @return : {Number}
	 */
	lastIndexOf : function(el, start) {
		var start = start || this.length;
		if (start >= this.length) {
			start = this.length;
		}
		if (start < 0) {
			start = this.length + start;
		}
		for (var i = start; i >= 0; --i) {
			if (this[i] === el) {
				return i;
			}
		}
		return -1;
	},
	/**
	 * @discretion: 测试指定项是否在数组中存在
	 * @method : contains
	 * @param :
	 *            item (object) 要进行测试的项
	 * @param :
	 *            from (number, 可选: 默认值为0) 在数组中开始搜索的起始位索引
	 * @return : (boolean) 如果数组包含给出的项,则返回true; 否则返回false
	 */
	contains : function(item, from) {
		var bl = false, from = from || 0;
		for (var index = from; index < this.length; index++) {
			if (item == this[index]) {
				bl = true;
				break;
			}
		}
		return bl;
	},
	isEmpty : function() {
		return (this && this.length > 0) ? false : true;
	},
	// ----数组值操作------------------
	
	/**
	 * @discretion: 向数组中添加一项, 如果该项在数组中已经存在,则不再添加.
	 * @method : include
	 * @param :
	 *            item (object) 目标添加项
	 * @return : (array) 添加元素后的主调数组
	 */
	include : function(item) {
		if (this.indexOf(item) < 0 ) {
			this.push(item);
		}
		return this;
	},
	/**
	 * @discretion: 删除数组中所有的指定项
	 * @method : erase
	 * @param :
	 *            item (object) 目标删除项
	 * @return : (array) 进行删除后的主调数组
	 */
	erase : function(item) {
		var index = this.indexOf(item);
		if (index != -1) {
			this.splice(index, 1);
		}
		return this;
	},
	remove : function(index) {
		if (index < 0 || index > this.length) {
			return this;
		} else {
			var temp = [];
			for (var i = 0; i < this.length; i++) {
				if (i != index) {
					temp.push(this[i]);
				}
			}
			return temp;
		}
	},
	removeAt : function (index) {
		var arr = this;
		return arr.splice(index, 1)[0];
	},
	set : function(index, item) {
		if (this.lenght == 0 || index < 0 || index > this.lenght) {
			this.push(item);
		} else {
			this.splice(index, 1, item);
		}
		return this;
	},
	get : function(index) {
		if (this.lenght == 0 || index < 0 || index > this.lenght) {
			return null;
		} else {
			return this[index];
		}
	}, 
	first : function(fn,bind) {
		var scope = bind || window;
		var temp = null;
		if (this.length > 0) {
			for (var index = 0; index < this.length; index++) {
				if (fn.call(scope, index, this[index])) {
					temp = this[index];
					break;
				}
			}
		}
		return temp;
	},
	/**
	 * @discretion: 返回数组中的最后一项
	 * @method : last
	 * @return : {object} 数组中的最后一项,如果是空数组,则返回null
	 */
	last : function(fn,bind) {
		return this.reverse().first(fn,bind);
	},
	/**
	 * @discretion: 返回从数组中随机抽取的一项
	 * @method : random
	 * @return : {object} 从数组中随机抽取的一项; 如果是空数组,则返回null
	 */
	random : function() {
		var l = this.length;
		function index() {
			var r = Math.ceil(Math.random() * Math.pow(10, String(l).length));
			return r >= l ? index() : r;
		}
		return this.length > 0 ? this[index()] : null;
	},
	/** 
	 * 求两个集合的并集 
	 * @param {Array} a 集合A 
	 * @param {Array} b 集合B 
	 * @returns {Array} 两个集合的并集 
	 * @author majun
	 */
	union: function(b){
		return this.concat(b).unique();
	},		
	/**
	 * 获取当前集合中几个相同的数据
	 */
	reiteration : function() {
		var num = 0;
		for (var i = 0; i < this.length; i++) {
			var temp = this[i];
			for (var index = (i + 1); index < this.length; index++) {
				if (temp == this[index]) {
					num++;
				}
			}
		}
		return num;
	},
	/**
	 * 获取交集
	 */
	intersect : function(){
		var a = [];
		for (var i = 0; i < this.length; i++) {
			var temp = this[i];
			for (var index = (i + 1); index < this.length; index++) {
				if (temp == this[index]) {
					a.push(temp);
				}
			}
		}
		return a;
	},
	toPlanString : function(){
		var flag = this.some(function(index,item,array){ 
			 return $.isArray(item) || $.isFunction(item) || $.isPlainObject(item) ;
		});		  
		return (flag && JSON) ? JSON.stringify(this) : this.toString();
	}
};

for ( var method in extendArr) {
	Array.prototype[method] = extendArr[method];
};
