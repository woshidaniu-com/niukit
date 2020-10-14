var extendNum = {
	/**
     * @discretion: 将取值范围限制在指定区间(在区间内,取原值;超出区间,则取邻近的区间边界值)
     * @method    : limit
     * @param	  : min (number) 下限
     * @param	  : max (number) 上限
     * @return    : (number) 在区间内取出的值
     */
	limit:function(min,max) {
        return (this<min)?min:((this>max)?max:this);
    },
    /**
     * @discretion: 对数值进行指定位上的四舍五入
     * @method    : round
     * @return    : (number) 四舍五入后的值
     */
	round:function() {
        return Math.round(this);
    },
    /**
     * @discretion: 将数字转成大写人民币字符
     * @method    : toRMBString
     * @return    : {String} 
     * @notice    : 
     */
    toRMBString:function() {
        return String(this).toRMBString();
    },
    /**
     * @discretion: 将数组格式化为指定的格式类型
     * @method    : format
     * @param     : {String} || '###,###,###.######', 如："#####年##月##日"
     * @return    : {String} 
     * @notice    : 数字位数不全时以0补全
     * @notice    : 结果仅由数字,. 组成，说明是数字，则进行多余位处理
    */
	format:function(pattern) {
        if(pattern){
        	//将数值转成字符
        	var value = new Number(this).toString();pattern = (pattern+"");
        	//判断是数字
        	if(!(/^(\+|\-)?(\d+)(\.\d+)?$/.test(value))) { 
		        return NaN; //返回Not A Number，终止。
		    }
            //1.取得符合正则表达式里的[]里三部分放入value_1,value_2,value_3 三个变量中
            var value_1 = RegExp.$1, value_2 = RegExp.$2, value_3 = RegExp.$3;
            if(/^(\+|\-)?([^\.]+)(\.\S+)?/mg.test(pattern)){
                //2.取得格式字符串表达式里[]里三部分放入pattern1,pattern2,pattern3三个变量中
                var pattern1 = RegExp.$1, pattern2 = RegExp.$2, pattern3 = RegExp.$3,fixed = 0; 
                //计算四舍五入的位数
                fixed = (pattern3.indexOf(".")!=-1)?(pattern3.substr(pattern3.indexOf(".")+1).length):0;
                //对小数点后的小数进行四舍五入
                fixed > 0 ? value_3 = Number("0"+value_3).toFixed(fixed).toString():"";
                if(parseInt(value_3) >= 1){
                    //如果四舍五入后的值大于等于1,则整数位加四舍五入的结果
                    value_2 = (parseInt(value_2) + 1)+"";
                }
                value_3 = value_3.substr(value_3.indexOf(".")+1);
                if(!String.prototype.serializeArray){
                	if(console && console.error){
        				console.error("serializeArray is not defined at String.prototype");
        			}
                }
                //对整数位进行0补齐
                var index = 0,
                    pattern4 = String((pattern3?(pattern2+pattern3):pattern2)+ ""||"").serializeArray().reverse().join(""),
                    value_4 = String((value_3 ?(value_2+value_3):value_2) + ""||"").serializeArray().reverse().join("");
                var value_5 = pattern4.replace(/(\#)/mg, function(regex, match) {
                    var temp = value_4.charAt(index)?value_4.charAt(index):"0";
                    index ++;
                    return temp;
                });
               
                value_5 = value_5.serializeArray().reverse().join("");
                //如果处理后的结果仅由数字,.组成，说明是数字的格式化。则进行多余位处理
                if(value_5.replace(/[\d|\.|,]/mg,"").length==0){
                    var firstNumIndex = 0;
                    if(/([1-9]{1})/mg.test(value_5)) { 
                        value_5 = value_5.substr(value_5.indexOf(RegExp.$1));
                    }
                }
                return value_1+value_5;
            }
            return this;
        }
    }
};

for ( var method in extendNum) {
	Number.prototype[method] = extendNum[method];
};