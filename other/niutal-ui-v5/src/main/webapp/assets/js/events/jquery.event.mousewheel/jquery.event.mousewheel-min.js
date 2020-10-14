/*! Copyright (c) 2013 Brandon Aaron (http://brandon.aaron.sh)
 * Licensed under the MIT License (LICENSE.txt).
 *
 * Version: 3.1.11
 *
 * Requires: jQuery 1.2.2+
 * 
 * // using bind
	$('#my_elem').bind('mousewheel', function(event, delta, deltaX, deltaY) {
		if (window.console && console.log) {
			 console.log(delta, deltaX, deltaY);
		}
	});
	// using the event helper
	$('#my_elem').mousewheel(function(event, delta, deltaX, deltaY) {
		if (window.console && console.log) {
			 console.log(delta, deltaX, deltaY);
		}
	});
 * 
 */
;(function(c){"function"===typeof define&&define.amd?define(["jquery"],c):"object"===typeof exports?module.exports=c:c(jQuery)})(function(c){function l(a){var b=a||window.event,k=r.call(arguments,1),g,e=0,d=0,f,l=0,n=0;a=c.event.fix(b);a.type="mousewheel";"detail"in b&&(d=-1*b.detail);"wheelDelta"in b&&(d=b.wheelDelta);"wheelDeltaY"in b&&(d=b.wheelDeltaY);"wheelDeltaX"in b&&(e=-1*b.wheelDeltaX);"axis"in b&&b.axis===b.HORIZONTAL_AXIS&&(e=-1*d,d=0);g=0===d?e:d;"deltaY"in b&&(g=d=-1*b.deltaY);"deltaX"in b&&(e=b.deltaX,0===d&&(g=-1*e));if(0!==d||0!==e){1===b.deltaMode?(f=c.data(this,"mousewheel-line-height"),g*=f,d*=f,e*=f):2===b.deltaMode&&(f=c.data(this,"mousewheel-page-height"),g*=f,d*=f,e*=f);f=Math.max(Math.abs(d),Math.abs(e));if(!h||f<h)h=f,m.settings.adjustOldDeltas&&"mousewheel"===b.type&&0===f%120&&(h/=40);m.settings.adjustOldDeltas&&"mousewheel"===b.type&&0===f%120&&(g/=40,e/=40,d/=40);g=Math[1<=g?"floor":"ceil"](g/h);e=Math[1<=e?"floor":"ceil"](e/h);d=Math[1<=d?"floor":"ceil"](d/h);m.settings.normalizeOffset&&this.getBoundingClientRect&&(b=this.getBoundingClientRect(),l=a.clientX-b.left,n=a.clientY-b.top);a.deltaX=e;a.deltaY=d;a.deltaFactor=h;a.offsetX=l;a.offsetY=n;a.deltaMode=0;k.unshift(a,g,e,d);p&&clearTimeout(p);p=setTimeout(t,200);return(c.event.dispatch||c.event.handle).apply(this,k)}}function t(){h=null}var n=["wheel","mousewheel","DOMMouseScroll","MozMousePixelScroll"],k="onwheel"in document||9<=document.documentMode?["wheel"]:["mousewheel","DomMouseScroll","MozMousePixelScroll"],r=Array.prototype.slice,p,h;if(c.event.fixHooks)for(var q=n.length;q;)c.event.fixHooks[n[--q]]=c.event.mouseHooks;var m=c.event.special.mousewheel={version:"3.1.11",setup:function(){if(this.addEventListener)for(var a=k.length;a;)this.addEventListener(k[--a],l,!1);else this.onmousewheel=l;c.data(this,"mousewheel-line-height",m.getLineHeight(this));c.data(this,"mousewheel-page-height",m.getPageHeight(this))},teardown:function(){if(this.removeEventListener)for(var a=k.length;a;)this.removeEventListener(k[--a],l,!1);else this.onmousewheel=null;c.removeData(this,"mousewheel-line-height");c.removeData(this,"mousewheel-page-height")},getLineHeight:function(a){a=c(a)["offsetParent"in c.fn?"offsetParent":"parent"]();a.length||(a=c("body"));return parseInt(a.css("fontSize"),10)},getPageHeight:function(a){return c(a).height()},settings:{adjustOldDeltas:!0,normalizeOffset:!0}};c.fn.extend({mousewheel:function(a){return a?this.bind("mousewheel",a):this.trigger("mousewheel")},unmousewheel:function(a){return this.unbind("mousewheel",a)}})});