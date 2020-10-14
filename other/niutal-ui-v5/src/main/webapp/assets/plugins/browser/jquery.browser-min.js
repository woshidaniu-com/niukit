/*!
* jQuery Browser Plugin v0.0.6
* https://github.com/gabceb/jquery-browser-plugin
*
* Original jquery-browser code Copyright 2005, 2013 jQuery Foundation, Inc. and other contributors
* http://jquery.org/license
*
* Modifications Copyright 2013 Gabriel Cebrian
* https://github.com/gabceb
*
* Released under the MIT license
*
* Date: 2013-07-29T17:23:27-07:00
*/
;(function(f,e,h){var a,d;f.uaMatch=function(k){k=k.toLowerCase();var j=/(opr)[\/]([\w.]+)/.exec(k)||/(chrome)[ \/]([\w.]+)/.exec(k)||/(version)[ \/]([\w.]+).*(safari)[ \/]([\w.]+)/.exec(k)||/(webkit)[ \/]([\w.]+)/.exec(k)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(k)||/(msie) ([\w.]+)/.exec(k)||k.indexOf("trident")>=0&&/(rv)(?::| )([\w.]+)/.exec(k)||k.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(k)||[];var i=/(ipad)/.exec(k)||/(iphone)/.exec(k)||/(android)/.exec(k)||/(windows phone)/.exec(k)||/(win)/.exec(k)||/(mac)/.exec(k)||/(linux)/.exec(k)||/(cros)/i.exec(k)||[];return{browser:j[3]||j[1]||"",version:j[2]||"0",platform:i[0]||""}};a=f.uaMatch(e.navigator.userAgent);d={};if(a.browser){d[a.browser]=true;d.version=a.version;d.versionNumber=parseInt(a.version)}if(a.platform){d[a.platform]=true}if(d.android||d.ipad||d.iphone||d["windows phone"]){d.mobile=true}if(d.cros||d.mac||d.linux||d.win){d.desktop=true}if(d.chrome||d.opr||d.safari){d.webkit=true}if(d.rv){var g="msie";a.browser=g;d[g]=true}if(d.opr){var c="opera";a.browser=c;d[c]=true}if(d.safari&&d.android){var b="android";a.browser=b;d[b]=true}d.name=a.browser;d.platform=a.platform;f.browser=d})(jQuery,window);
