/*
 * 快捷键插件:详情参见：jquery.hotkeys.js(v1.0)
 */
;(function(b){b.hotkeys={version:"1.0",specialKeys:{8:"backspace",9:"tab",10:"return",13:"return",16:"shift",17:"ctrl",18:"alt",19:"pause",20:"capslock",27:"esc",32:"space",33:"pageup",34:"pagedown",35:"end",36:"home",37:"left",38:"up",39:"right",40:"down",45:"insert",46:"del",59:";",61:"=",96:"0",97:"1",98:"2",99:"3",100:"4",101:"5",102:"6",103:"7",104:"8",105:"9",106:"*",107:"+",109:"-",110:".",111:"/",112:"f1",113:"f2",114:"f3",115:"f4",116:"f5",117:"f6",118:"f7",119:"f8",120:"f9",121:"f10",122:"f11",123:"f12",144:"numlock",145:"scroll",173:"-",186:";",187:"=",188:",",189:"-",190:".",191:"/",192:"`",219:"[",220:"\\",221:"]",222:"'",224:"meta"},shiftNums:{"`":"~","1":"!","2":"@","3":"#","4":"$","5":"%","6":"^","7":"&","8":"*","9":"(","0":")","-":"_","=":"+",";":": ","'":'"',",":"<",".":">","/":"?","\\":"|"},textAcceptingInputTypes:["text","password","number","email","url","range","date","month","week","time","datetime","datetime-local","search","color","tel"],textInputTypes:/textarea|input|select/i,options:{filterInputAcceptingElements:true,filterTextInputs:true,filterContentEditable:true,filterEvent:function(){return false}}};function a(d){try{if(typeof d.data==="string"){d.data={keys:d.data}}if(!d.data||!d.data.keys||typeof d.data.keys!=="string"){return}var c=d.handler,f=d.data.keys.toLowerCase().split(" ");d.handler=function(e){var q=d.data.filterInputAcceptingElements||b.hotkeys.options.filterInputAcceptingElements;var s=d.data.filterContentEditable||b.hotkeys.options.filterContentEditable;var n=d.data.filterTextInputs||b.hotkeys.options.filterTextInputs;var r=d.data.filterEvent||b.hotkeys.options.filterEvent;if(this!==e.target&&(q&&b.hotkeys.textInputTypes.test(e.target.nodeName)||(s&&b(e.target).attr("contenteditable"))||(n&&b.inArray(e.target.type,b.hotkeys.textAcceptingInputTypes)>-1))||(r.call(this,e)==true)){return}console.log(e.which);var p=e.type!=="keypress"&&b.hotkeys.specialKeys[e.which],o=String.fromCharCode(e.which).toLowerCase(),j="",k={};b.each(["alt","ctrl","shift"],function(i,l){if(e[l+"Key"]&&p!==l){j+=l+"+"}});if(e.metaKey&&!e.ctrlKey&&p!=="meta"){j+="meta+"}if(e.metaKey&&p!=="meta"&&j.indexOf("alt+ctrl+shift+")>-1){j=j.replace("alt+ctrl+shift+","hyper+")}if(p){k[j+p]=true}else{k[j+o]=true;k[j+b.hotkeys.shiftNums[o]]=true;if(j==="shift+"){k[b.hotkeys.shiftNums[o]]=true}}for(var m=0,h=f.length;m<h;m++){if(k[f[m]]){return c.apply(this,arguments)}}}}catch(g){}}b.each(["keydown","keyup","keypress"],function(){b.event.special[this]={add:a}})})(jQuery||this.jQuery||window.jQuery);

