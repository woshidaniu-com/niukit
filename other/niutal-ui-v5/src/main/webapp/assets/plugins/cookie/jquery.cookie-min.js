(function(a){a.Cookie=a.cookie=function(c,k,n){if(typeof k!="undefined"){n=n||{};if(k===null){k="";n.expires=-1}var f="";if(n.expires&&(typeof n.expires=="number"||n.expires.toUTCString)){var g;if(typeof n.expires=="number"){g=new Date();g.setTime(g.getTime()+(n.expires*24*60*60*1000))}else{g=n.expires}f="; expires="+g.toUTCString()}var m=n.path?"; path="+(n.path):"";var h=n.domain?"; domain="+(n.domain):"";var b=n.secure?"; secure":"";document.cookie=[c,"=",encodeURIComponent(k),f,m,h,b].join("")}else{var e=null;if(document.cookie&&document.cookie!=""){var l=document.cookie.split(";");for(var j=0;j<l.length;j++){var d=jQuery.trim(l[j]);if(d.substring(0,c.length+1)==(c+"=")){e=decodeURIComponent(d.substring(c.length+1));break}}}return e}}}(jQuery));