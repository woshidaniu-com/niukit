(function(e,d,c){if("onpropertychange" in document){var a=/^INPUT|TEXTAREA$/,b=function(f){return a.test(f.nodeName)};e.event.special[c]={setup:function(){var f=this;if(!b(f)){return false}e.data(f,"@oldValue",f.value);e.event.add(f,"propertychange",function(g){if(e.data(this,"@oldValue")!==this.value){e.event.trigger("input",null,this)}e.data(this,"@oldValue",this.value)})},teardown:function(){var f=this;if(!b(f)){return false}e.event.remove(f,"propertychange");e.removeData(f,"@oldValue")}}}e.fn[c]=function(f){return f?this.bind(c,f):this.trigger(c)}})(jQuery,[],"input");