!function(b){return"function"==typeof define&&define.amd?define(["jquery"],function(a){return b(a,window,document)}):"object"==typeof exports?module.exports=b(require("jquery"),window,document):b(jQuery,window,document)}(function(ap,ao,an){var am,al,ak,aj,ai,ah,ag,af,ae,ad,ac,ab,aa,Y,W,U,S,Q,O,M,K,J,I,Z,X,V,T,R,P,N,L;I={paneClass:"nano-pane",sliderClass:"nano-slider",contentClass:"nano-content",iOSNativeScrolling:!1,preventPageScrolling:!1,disableResize:!1,alwaysVisible:!1,flashDelay:1500,sliderMinHeight:20,sliderMaxHeight:null,documentContext:null,windowContext:null},Q="scrollbar",S="scroll",ae="mousedown",ad="mouseenter",ac="mousemove",aa="mousewheel",ab="mouseup",U="resize",ai="drag",ah="enter",M="up",W="panedown",ak="DOMMouseScroll",aj="down",K="wheel",ag="keydown",af="keyup",O="touchmove",am="Microsoft Internet Explorer"===ao.navigator.appName&&/msie 7./i.test(ao.navigator.appVersion)&&ao.ActiveXObject,al=null,T=ao.requestAnimationFrame,J=ao.cancelAnimationFrame,P=an.createElement("div").style,L=function(){var h,g,l,k,j,i;for(k=["t","webkitT","MozT","msT","OT"],h=j=0,i=k.length;i>j;h=++j){if(l=k[h],g=k[h]+"ransform",g in P){return k[h].substr(0,k[h].length-1)}}return !1}(),N=function(b){return L===!1?!1:""===L?b:L+b.charAt(0).toUpperCase()+b.substr(1)},R=N("transform"),X=R!==!1,Z=function(){var e,c,f;return e=an.createElement("div"),c=e.style,c.position="absolute",c.width="100px",c.height="100px",c.overflow=S,c.top="-9999px",an.body.appendChild(e),f=e.offsetWidth-e.clientWidth,an.body.removeChild(e),f},V=function(){var b,f,e;return f=ao.navigator.userAgent,(b=/(?=.+Mac OS X)(?=.+Firefox)/.test(f))?(e=/Firefox\/\d{2}\./.exec(f),e&&(e=e[0].replace(/\D+/g,"")),b&&+e>23):!1},Y=function(){function a(c,b){this.el=c,this.options=b,al||(al=Z()),this.$el=ap(this.el),this.doc=ap(this.options.documentContext||an),this.win=ap(this.options.windowContext||ao),this.body=this.doc.find("body"),this.$content=this.$el.children("."+this.options.contentClass),this.$content.attr("tabindex",this.options.tabIndex||0),this.content=this.$content[0],this.previousPosition=0,this.options.iOSNativeScrolling&&null!=this.el.style.WebkitOverflowScrolling?this.nativeScrolling():this.generate(),this.createEvents(),this.addEvents(),this.reset()}return a.prototype.preventScrolling=function(d,c){if(this.isActive){if(d.type===ak){(c===aj&&d.originalEvent.detail>0||c===M&&d.originalEvent.detail<0)&&d.preventDefault()}else{if(d.type===aa){if(!d.originalEvent||!d.originalEvent.wheelDelta){return}(c===aj&&d.originalEvent.wheelDelta<0||c===M&&d.originalEvent.wheelDelta>0)&&d.preventDefault()}}}},a.prototype.nativeScrolling=function(){this.$content.css({WebkitOverflowScrolling:"touch"}),this.iOSNativeScrolling=!0,this.isActive=!0},a.prototype.updateScrollValues=function(){var d,c;d=this.content,this.maxScrollTop=d.scrollHeight-d.clientHeight,this.prevScrollTop=this.contentScrollTop||0,this.contentScrollTop=d.scrollTop,c=this.contentScrollTop>this.previousPosition?"down":this.contentScrollTop<this.previousPosition?"up":"same",this.previousPosition=this.contentScrollTop,"same"!==c&&this.$el.trigger("update",{position:this.contentScrollTop,maximum:this.maxScrollTop,direction:c}),this.iOSNativeScrolling||(this.maxSliderTop=this.paneHeight-this.sliderHeight,this.sliderTop=0===this.maxScrollTop?0:this.contentScrollTop*this.maxSliderTop/this.maxScrollTop)},a.prototype.setOnScrollStyles=function(){var b;X?(b={},b[R]="translate(0, "+this.sliderTop+"px)"):b={top:this.sliderTop},T?(J&&this.scrollRAF&&J(this.scrollRAF),this.scrollRAF=T(function(c){return function(){return c.scrollRAF=null,c.slider.css(b)}}(this))):this.slider.css(b)},a.prototype.createEvents=function(){this.events={down:function(b){return function(c){return b.isBeingDragged=!0,b.offsetY=c.pageY-b.slider.offset().top,b.slider.is(c.target)||(b.offsetY=0),b.pane.addClass("active"),b.doc.bind(ac,b.events[ai]).bind(ab,b.events[M]),b.body.bind(ad,b.events[ah]),!1}}(this),drag:function(b){return function(c){return b.sliderY=c.pageY-b.$el.offset().top-b.paneTop-(b.offsetY||0.5*b.sliderHeight),b.scroll(),b.contentScrollTop>=b.maxScrollTop&&b.prevScrollTop!==b.maxScrollTop?b.$el.trigger("scrollend"):0===b.contentScrollTop&&0!==b.prevScrollTop&&b.$el.trigger("scrolltop"),!1}}(this),up:function(b){return function(c){return b.isBeingDragged=!1,b.pane.removeClass("active"),b.doc.unbind(ac,b.events[ai]).unbind(ab,b.events[M]),b.body.unbind(ad,b.events[ah]),!1}}(this),resize:function(b){return function(c){b.reset()}}(this),panedown:function(b){return function(c){return b.sliderY=(c.offsetY||c.originalEvent.layerY)-0.5*b.sliderHeight,b.scroll(),b.events.down(c),!1}}(this),scroll:function(b){return function(c){b.updateScrollValues(),b.isBeingDragged||(b.iOSNativeScrolling||(b.sliderY=b.sliderTop,b.setOnScrollStyles()),null!=c&&(b.contentScrollTop>=b.maxScrollTop?(b.options.preventPageScrolling&&b.preventScrolling(c,aj),b.prevScrollTop!==b.maxScrollTop&&b.$el.trigger("scrollend")):0===b.contentScrollTop&&(b.options.preventPageScrolling&&b.preventScrolling(c,M),0!==b.prevScrollTop&&b.$el.trigger("scrolltop"))))}}(this),wheel:function(b){return function(d){var e;if(null!=d){return e=d.delta||d.wheelDelta||d.originalEvent&&d.originalEvent.wheelDelta||-d.detail||d.originalEvent&&-d.originalEvent.detail,e&&(b.sliderY+=-e/3),b.scroll(),!1}}}(this),enter:function(b){return function(d){var e;if(b.isBeingDragged){return 1!==(d.buttons||d.which)?(e=b.events)[M].apply(e,arguments):void 0}}}(this)}},a.prototype.addEvents=function(){var b;this.removeEvents(),b=this.events,this.options.disableResize||this.win.bind(U,b[U]),this.iOSNativeScrolling||(this.slider.bind(ae,b[aj]),this.pane.bind(ae,b[W]).bind(""+aa+" "+ak,b[K])),this.$content.bind(""+S+" "+aa+" "+ak+" "+O,b[S])},a.prototype.removeEvents=function(){var b;b=this.events,this.win.unbind(U,b[U]),this.iOSNativeScrolling||(this.slider.unbind(),this.pane.unbind()),this.$content.unbind(""+S+" "+aa+" "+ak+" "+O,b[S])},a.prototype.generate=function(){var b,n,m,l,k,j,e;return l=this.options,j=l.paneClass,e=l.sliderClass,b=l.contentClass,(k=this.$el.children("."+j)).length||k.children("."+e).length||this.$el.append('<div class="'+j+'"><div class="'+e+'" /></div>'),this.pane=this.$el.children("."+j),this.slider=this.pane.find("."+e),0===al&&V()?(m=ao.getComputedStyle(this.content,null).getPropertyValue("padding-right").replace(/[^0-9.]+/g,""),n={right:-14,paddingRight:+m+14}):al&&(n={right:-al},this.$el.addClass("has-scrollbar")),null!=n&&this.$content.css(n),this},a.prototype.restore=function(){this.stopped=!1,this.iOSNativeScrolling||this.pane.show(),this.addEvents()},a.prototype.reset=function(){var x,w,v,u,t,s,r,q,p,o,e,d;return this.iOSNativeScrolling?void (this.contentHeight=this.content.scrollHeight):(this.$el.find("."+this.options.paneClass).length||this.generate().stop(),this.stopped&&this.restore(),x=this.content,u=x.style,t=u.overflowY,am&&this.$content.css({height:this.$content.height()}),w=x.scrollHeight+al,o=parseInt(this.$el.css("max-height"),10),o>0&&(this.$el.height(""),this.$el.height(x.scrollHeight>o?o:x.scrollHeight)),r=this.pane.outerHeight(!1),p=parseInt(this.pane.css("top"),10),s=parseInt(this.pane.css("bottom"),10),q=r+p+s,d=Math.round(q/w*r),d<this.options.sliderMinHeight?d=this.options.sliderMinHeight:null!=this.options.sliderMaxHeight&&d>this.options.sliderMaxHeight&&(d=this.options.sliderMaxHeight),t===S&&u.overflowX!==S&&(d+=al),this.maxSliderTop=q-d,this.contentHeight=w,this.paneHeight=r,this.paneOuterHeight=q,this.sliderHeight=d,this.paneTop=p,this.slider.height(d),this.events.scroll(),this.pane.show(),this.isActive=!0,x.scrollHeight===x.clientHeight||this.pane.outerHeight(!0)>=x.scrollHeight&&t!==S?(this.pane.hide(),this.isActive=!1):this.el.clientHeight===x.scrollHeight&&t===S?this.slider.hide():this.slider.show(),this.pane.css({opacity:this.options.alwaysVisible?1:"",visibility:this.options.alwaysVisible?"visible":""}),v=this.$content.css("position"),("static"===v||"relative"===v)&&(e=parseInt(this.$content.css("right"),10),e&&this.$content.css({right:"",marginRight:e})),this)},a.prototype.scroll=function(){return this.isActive?(this.sliderY=Math.max(0,this.sliderY),this.sliderY=Math.min(this.maxSliderTop,this.sliderY),this.$content.scrollTop(this.maxScrollTop*this.sliderY/this.maxSliderTop),this.iOSNativeScrolling||(this.updateScrollValues(),this.setOnScrollStyles()),this):void 0},a.prototype.scrollBottom=function(b){return this.isActive?(this.$content.scrollTop(this.contentHeight-this.$content.height()-b).trigger(aa),this.stop().restore(),this):void 0},a.prototype.scrollTop=function(b){return this.isActive?(this.$content.scrollTop(+b).trigger(aa),this.stop().restore(),this):void 0},a.prototype.scrollTo=function(b){return this.isActive?(this.scrollTop(this.$el.find(b).get(0).offsetTop),this):void 0},a.prototype.stop=function(){return J&&this.scrollRAF&&(J(this.scrollRAF),this.scrollRAF=null),this.stopped=!0,this.removeEvents(),this.iOSNativeScrolling||this.pane.hide(),this},a.prototype.destroy=function(){return this.stopped||this.stop(),!this.iOSNativeScrolling&&this.pane.length&&this.pane.remove(),am&&this.$content.height(""),this.$content.removeAttr("tabindex"),this.$el.hasClass("has-scrollbar")&&(this.$el.removeClass("has-scrollbar"),this.$content.css({right:""})),this},a.prototype.flash=function(){return !this.iOSNativeScrolling&&this.isActive?(this.reset(),this.pane.addClass("flashed"),setTimeout(function(b){return function(){b.pane.removeClass("flashed")}}(this),this.options.flashDelay),this):void 0},a}(),ap.fn.nanoScroller=function(a){return this.each(function(){var e,b;if((b=this.nanoscroller)||(e=ap.extend({},I,a),this.nanoscroller=b=new Y(this,e)),a&&"object"==typeof a){if(ap.extend(b.options,a),null!=a.scrollBottom){return b.scrollBottom(a.scrollBottom)}if(null!=a.scrollTop){return b.scrollTop(a.scrollTop)}if(a.scrollTo){return b.scrollTo(a.scrollTo)}if("bottom"===a.scroll){return b.scrollBottom(0)}if("top"===a.scroll){return b.scrollTop(0)}if(a.scroll&&a.scroll instanceof ap){return b.scrollTo(a.scroll)}if(a.stop){return b.stop()}if(a.destroy){return b.destroy()}if(a.flash){return b.flash()}}return b.reset()})},ap.fn.nanoScroller.Constructor=Y});!function(c,b){if("function"==typeof define&&define.amd){define(["jquery"],b)}else{if("undefined"!=typeof exports){b(require("jquery"))}else{var a={exports:{}};b(c.jquery),c.metisMenu=a.exports}}}(this,function(d){function b(f,e){if(!(f instanceof e)){throw new TypeError("Cannot call a class as a function")}}!function(e){e&&e.__esModule}(d);var a="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},c=function(m){function h(){return{bindType:j.end,delegateType:j.end,handle:function(e){if(m(e.target).is(this)){return e.handleObj.handler.apply(this,arguments)}}}}function g(){if(window.QUnit){return !1}var o=document.createElement("mm");for(var e in l){if(void 0!==o.style[e]){return{end:l[e]}}}return !1}function k(o){var n=this,p=!1;return m(this).one(f.TRANSITION_END,function(){p=!0}),setTimeout(function(){p||f.triggerTransitionEnd(n)},o),this}var j=!1,l={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd otransitionend",transition:"transitionend"},f={TRANSITION_END:"mmTransitionEnd",triggerTransitionEnd:function(e){m(e).trigger(j.end)},supportsTransitionEnd:function(){return Boolean(j)}};return j=g(),m.fn.emulateTransitionEnd=k,f.supportsTransitionEnd()&&(m.event.special[f.TRANSITION_END]=h()),f}(jQuery);!function(j){var g="metisMenu",i=j.fn[g],f={toggle:!0,preventDefault:!0,activeClass:"active",collapseClass:"collapse",collapseInClass:"in",collapsingClass:"collapsing",triggerElement:"a",parentTrigger:"li",subMenu:"ul"},h={SHOW:"show.metisMenu",SHOWN:"shown.metisMenu",HIDE:"hide.metisMenu",HIDDEN:"hidden.metisMenu",CLICK_DATA_API:"click.metisMenu.data-api"},e=function(){function k(m,l){b(this,k),this._element=m,this._config=this._getConfig(l),this._transitioning=null,this.init()}return k.prototype.init=function(){var l=this;j(this._element).find(this._config.parentTrigger+"."+this._config.activeClass).has(this._config.subMenu).children(this._config.subMenu).attr("aria-expanded",!0).addClass(this._config.collapseClass+" "+this._config.collapseInClass),j(this._element).find(this._config.parentTrigger).not("."+this._config.activeClass).has(this._config.subMenu).children(this._config.subMenu).attr("aria-expanded",!1).addClass(this._config.collapseClass),j(this._element).find(this._config.parentTrigger).has(this._config.subMenu).children(this._config.triggerElement).on(h.CLICK_DATA_API,function(n){var q=j(this),p=q.parent(l._config.parentTrigger),r=p.siblings(l._config.parentTrigger).children(l._config.triggerElement),m=p.children(l._config.subMenu);l._config.preventDefault&&n.preventDefault(),"true"!==q.attr("aria-disabled")&&(p.hasClass(l._config.activeClass)?(q.attr("aria-expanded",!1),l._hide(m)):(l._show(m),q.attr("aria-expanded",!0),l._config.toggle&&r.attr("aria-expanded",!1)),l._config.onTransitionStart&&l._config.onTransitionStart(n))})},k.prototype._show=function(n){if(!this._transitioning&&!j(n).hasClass(this._config.collapsingClass)){var m=this,p=j(n),q=j.Event(h.SHOW);if(p.trigger(q),!q.isDefaultPrevented()){p.parent(this._config.parentTrigger).addClass(this._config.activeClass),this._config.toggle&&this._hide(p.parent(this._config.parentTrigger).siblings().children(this._config.subMenu+"."+this._config.collapseInClass).attr("aria-expanded",!1)),p.removeClass(this._config.collapseClass).addClass(this._config.collapsingClass).height(0),this.setTransitioning(!0);var l=function(){m._config&&m._element&&(p.removeClass(m._config.collapsingClass).addClass(m._config.collapseClass+" "+m._config.collapseInClass).height("").attr("aria-expanded",!0),m.setTransitioning(!1),p.trigger(h.SHOWN))};c.supportsTransitionEnd()?p.height(p[0].scrollHeight).one(c.TRANSITION_END,l).emulateTransitionEnd(350):l()}}},k.prototype._hide=function(n){if(!this._transitioning&&j(n).hasClass(this._config.collapseInClass)){var m=this,p=j(n),q=j.Event(h.HIDE);if(p.trigger(q),!q.isDefaultPrevented()){p.parent(this._config.parentTrigger).removeClass(this._config.activeClass),p.height(p.height())[0].offsetHeight,p.addClass(this._config.collapsingClass).removeClass(this._config.collapseClass).removeClass(this._config.collapseInClass),this.setTransitioning(!0);var l=function(){m._config&&m._element&&(m._transitioning&&m._config.onTransitionEnd&&m._config.onTransitionEnd(),m.setTransitioning(!1),p.trigger(h.HIDDEN),p.removeClass(m._config.collapsingClass).addClass(m._config.collapseClass).attr("aria-expanded",!1))};c.supportsTransitionEnd()?0==p.height()||"none"==p.css("display")?l():p.height(0).one(c.TRANSITION_END,l).emulateTransitionEnd(350):l()}}},k.prototype.setTransitioning=function(l){this._transitioning=l},k.prototype.dispose=function(){j.removeData(this._element,"metisMenu"),j(this._element).find(this._config.parentTrigger).has(this._config.subMenu).children(this._config.triggerElement).off("click"),this._transitioning=null,this._config=null,this._element=null},k.prototype._getConfig=function(l){return l=j.extend({},f,l)},k._jQueryInterface=function(l){return this.each(function(){var n=j(this),p=n.data("metisMenu"),m=j.extend({},f,n.data(),"object"===(void 0===l?"undefined":a(l))&&l);if(!p&&/dispose/.test(l)&&this.dispose(),p||(p=new k(this,m),n.data("metisMenu",p)),"string"==typeof l){if(void 0===p[l]){throw new Error('No method named "'+l+'"')}p[l]()}})},k}();j.fn[g]=e._jQueryInterface,j.fn[g].Constructor=e,j.fn[g].noConflict=function(){return j.fn[g]=i,e._jQueryInterface}}(jQuery)});!function(a){a(document).ready(function(){a(document).trigger("nifty.ready")});a(document).on("nifty.ready",function(){var c=a(".add-tooltip");if(c.length){c.tooltip()}var b=a(".add-popover");if(b.length){b.popover()}a("#navbar-container .navbar-top-links").on("shown.bs.dropdown",".dropdown",function(){a(this).find(".nano").nanoScroller({preventPageScrolling:true})})})}(jQuery);!function(c){var d=null,b=function(f){var g=f.find(".mega-dropdown-toggle"),e=f.find(".mega-dropdown-menu");g.on("click",function(h){h.preventDefault();f.toggleClass("open")})},a={toggle:function(){this.toggleClass("open");return null},show:function(){this.addClass("open");return null},hide:function(){this.removeClass("open");return null}};c.fn.niftyMega=function(f){var e=false;this.each(function(){if(a[f]){e=a[f].apply(c(this).find("input"),Array.prototype.slice.call(arguments,1))}else{if(typeof f==="object"||!f){b(c(this))}}});return e};c(document).on("nifty.ready",function(){d=c(".mega-dropdown");if(d.length){d.niftyMega();c("html").on("click",function(f){if(!c(f.target).closest(".mega-dropdown").length){d.removeClass("open")}})}})}(jQuery);!function(a){a(document).on("nifty.ready",function(){var b=a('[data-dismiss="panel"]');if(b.length){b.one("click",function(d){d.preventDefault();var c=a(this).parents(".panel");c.addClass("remove").on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd",function(f){if(f.originalEvent.propertyName=="opacity"){c.remove()}})})}else{b=null}})}(jQuery);!function(a){a(document).one("nifty.ready",function(){var f=a(".scroll-top"),e=a(window),d=function(){return(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent))}();if(f.length&&!d){var c=false;var g=250;function b(){if(e.scrollTop()>g&&!c){f.addClass("in").stop(true,true).css({animation:"none"}).show(0).css({animation:"jellyIn .8s"});c=true}else{if(e.scrollTop()<g&&c){f.removeClass("in");c=false}}}b();e.scroll(b);f.on("click",function(h){h.preventDefault();a("body, html").animate({scrollTop:0},500)})}else{f=null;e=null}d=null})}(jQuery);!function(c){var e={displayIcon:true,iconColor:"text-dark",iconClass:"fa fa-refresh fa-spin fa-2x",title:"",desc:""},b=function(){return(((1+Math.random())*65536)|0).toString(16).substring(1)},a={show:function(f){var g=c(f.attr("data-target")),i="nifty-overlay-"+b()+b()+"-"+b(),h=c('<div id="'+i+'" class="panel-overlay"></div>');f.prop("disabled",true).data("niftyOverlay",i);g.addClass("panel-overlay-wrap");h.appendTo(g).html(f.data("overlayTemplate"));return null},hide:function(g){var h=c(g.attr("data-target"));var f=c("#"+g.data("niftyOverlay"));if(f.length){g.prop("disabled",false);h.removeClass("panel-overlay-wrap");f.hide().remove()}return null}},d=function(i,f){if(i.data("overlayTemplate")){return null}var g=c.extend({},e,f),h=(g.displayIcon)?'<span class="panel-overlay-icon '+g.iconColor+'"><i class="'+g.iconClass+'"></i></span>':"";i.data("overlayTemplate",'<div class="panel-overlay-content pad-all unselectable">'+h+'<h4 class="panel-overlay-title">'+g.title+"</h4><p>"+g.desc+"</p></div>");return null};c.fn.niftyOverlay=function(f){if(a[f]){return a[f](this)}else{if(typeof f==="object"||!f){return this.each(function(){d(c(this),f)})}}return null}}(jQuery);!function(d){var h,c={},b,g,a,f=false,e=function(){var j=document.body||document.documentElement,k=j.style,i=k.transition!==undefined||k.WebkitTransition!==undefined;return i}();d.niftyNoty=function(s){var l={type:"primary",icon:"",title:"",message:"",closeBtn:true,container:"page",floating:{position:"top-right",animationIn:"jellyIn",animationOut:"fadeOut"},html:null,focus:true,timer:0,onShow:function(){},onShown:function(){},onHide:function(){},onHidden:function(){}},j=d.extend({},l,s),k=d('<div class="alert-wrap"></div>'),p=function(){var t="";if(s&&s.icon){t='<div class="media-left alert-icon"><i class="'+j.icon+'"></i></div>'}return t},n,o=function(){var t=j.closeBtn?'<button class="close" type="button"><i class="pci-cross pci-circle"></i></button>':"";var u='<div class="alert alert-'+j.type+'" role="alert">'+t+'<div class="media">';if(!j.html){return u+p()+'<div class="media-body"><h4 class="alert-title">'+j.title+'</h4><p class="alert-message">'+j.message+"</p></div></div>"}return u+j.html+"</div></div>"}(),m=function(t){j.onHide();if(j.container==="floating"&&j.floating.animationOut){k.removeClass(j.floating.animationIn).addClass(j.floating.animationOut);if(!e){j.onHidden();k.remove()}}k.removeClass("in").on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd",function(u){if(u.originalEvent.propertyName=="max-height"){j.onHidden();k.remove()}});clearInterval(n);return null},r=function(t){d("body, html").animate({scrollTop:t},300,function(){k.addClass("in")})},q=function(){j.onShow();if(j.container==="page"){if(!h){h=d('<div id="page-alert"></div>');if(!a||!a.length){a=d("#content-container")}a.prepend(h)}b=h;if(j.focus){r(0)}}else{if(j.container==="floating"){if(!c[j.floating.position]){c[j.floating.position]=d('<div id="floating-'+j.floating.position+'" class="floating-container"></div>');if(!g||!a.length){g=d("#container")}g.append(c[j.floating.position])}b=c[j.floating.position];if(j.floating.animationIn){k.addClass("in animated "+j.floating.animationIn)}j.focus=false}else{var t=d(j.container);var u=t.children(".panel-alert");var v=t.children(".panel-heading");if(!t.length){f=false;return false}if(!u.length){b=d('<div class="panel-alert"></div>');if(v.length){v.after(b)}else{t.prepend(b)}}else{b=u}if(j.focus){r(t.offset().top-30)}}}f=true;return false}();if(f){b.append(k.html(o));k.find('[data-dismiss="noty"]').one("click",m);if(j.closeBtn){k.find(".close").one("click",m)}if(j.timer>0){n=setInterval(m,j.timer)}if(!j.focus){var i=setInterval(function(){k.addClass("in");clearInterval(i)},200)}k.one("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd animationend webkitAnimationEnd oAnimationEnd MSAnimationEnd",function(t){if((t.originalEvent.propertyName=="max-height"||t.type=="animationend")&&f){j.onShown();f=false}})}}}(jQuery);!function(b){var d={dynamicMode:true,selectedOn:null,onChange:null},c=function(f,e){var m=b.extend({},d,e);var l=f.find(".lang-selected"),k=l.parent(".lang-selector").siblings(".dropdown-menu"),h=k.find("a"),i=h.filter(".active").find(".lang-id").text(),g=h.filter(".active").find(".lang-name").text();var j=function(n){h.removeClass("active");n.addClass("active");l.html(n.html());i=n.find(".lang-id").text();g=n.find(".lang-name").text();f.trigger("onChange",[{id:i,name:g}]);if(typeof m.onChange=="function"){m.onChange.call(this,{id:i,name:g})}};h.on("click",function(n){if(m.dynamicMode){n.preventDefault();n.stopPropagation()}f.dropdown("toggle");j(b(this))});if(m.selectedOn){j(b(m.selectedOn))}},a={getSelectedID:function(){return b(this).find(".lang-id").text()},getSelectedName:function(){return b(this).find(".lang-name").text()},getSelected:function(){var e=b(this);return{id:e.find(".lang-id").text(),name:e.find(".lang-name").text()}},setDisable:function(){b(this).addClass("disabled");return null},setEnable:function(e){b(this).removeClass("disabled");return null}};b.fn.niftyLanguage=function(f){var e=false;this.each(function(){if(a[f]){e=a[f].apply(this,Array.prototype.slice.call(arguments,1))}else{if(typeof f==="object"||!f){c(b(this),f)}}});return e}}(jQuery);!function(e){var b,c=function(h){if(h.data("nifty-check")){return}else{h.data("nifty-check",true);if(h.text().trim().length){h.addClass("form-text")}else{h.removeClass("form-text")}}var f=h.find("input")[0],j=f.name,g=function(){if(f.type=="radio"&&j){return e(".form-radio").not(h).find("input").filter('input[name="'+j+'"]').parent()}else{return false}}(),i=function(){if(f.type=="radio"&&g.length){g.each(function(){var k=e(this);if(k.hasClass("active")){k.trigger("nifty.ch.unchecked")}k.removeClass("active")})}if(f.checked){h.addClass("active").trigger("nifty.ch.checked")}else{h.removeClass("active").trigger("nifty.ch.unchecked")}};if(f.checked){h.addClass("active")}else{h.removeClass("active")}e(f).on("change",i)},a={isChecked:function(){return this[0].checked},toggle:function(){this[0].checked=!this[0].checked;this.trigger("change");return null},toggleOn:function(){if(!this[0].checked){this[0].checked=true;this.trigger("change")}return null},toggleOff:function(){if(this[0].checked&&this[0].type=="checkbox"){this[0].checked=false;this.trigger("change")}return null}},d=function(){b=e(".form-checkbox, .form-radio");if(b.length){b.niftyCheck()}};e.fn.niftyCheck=function(g){var f=false;this.each(function(){if(a[g]){f=a[g].apply(e(this).find("input"),Array.prototype.slice.call(arguments,1))}else{if(typeof g==="object"||!g){c(e(this))}}});return f};e(document).on("nifty.ready",d).on("change",".form-checkbox, .form-radio",d).on("change",".btn-file :file",function(){var g=e(this),j=g.get(0).files?g.get(0).files.length:1,h=g.val().replace(/\\/g,"/").replace(/.*\//,""),i=function(){try{return g[0].files[0].size}catch(k){return"Nan"}}(),f=function(){if(i=="Nan"){return"Unknown"}var k=Math.floor(Math.log(i)/Math.log(1024));return(i/Math.pow(1024,k)).toFixed(2)*1+" "+["B","kB","MB","GB","TB"][k]}();g.trigger("fileselect",[j,h,f])})}(jQuery);!function(a){a(document).on("nifty.ready",function(){var b=a("#mainnav-shortcut");if(b.length){b.find("li").each(function(){var c=a(this);c.popover({animation:false,trigger:"hover",placement:"bottom",container:"#mainnav-container",viewport:"#mainnav-container",template:'<div class="popover mainnav-shortcut"><div class="arrow"></div><div class="popover-content"></div>'})})}else{b=null}})}(jQuery);
/* jQuery resizeEnd Event v1.0.1 - Copyright (c) 2013 Giuseppe Gurgone - License http://git.io/iRQs3g */
!function(b,c){var a={};a.eventName="resizeEnd",a.delay=250,a.poll=function(){var e=b(this),d=e.data(a.eventName);d.timeoutId&&c.clearTimeout(d.timeoutId),d.timeoutId=c.setTimeout(function(){e.trigger(a.eventName)},a.delay)},b.event.special[a.eventName]={setup:function(){var d=b(this);d.data(a.eventName,{}),d.on("resize",a.poll)},teardown:function(){var e=b(this),d=e.data(a.eventName);d.timeoutId&&c.clearTimeout(d.timeoutId),e.removeData(a.eventName),e.off("resize",a.poll)}},b.fn[a.eventName]=function(d,f){return arguments.length>0?this.on(a.eventName,null,d,f):this.trigger(a.eventName)}}(jQuery,this);!function(f){var q=null,d=null,i=null,e=null,n=null,a=null,m=false,v=false,o=null,k=null,p=null,l=f(window),c=false,g=function(){var w,x=f('#mainnav-menu > li > a, #mainnav-menu-wrap .mainnav-widget a[data-toggle="menu-widget"]');x.each(function(){var N=f(this),B=N.children(".menu-title"),A=N.siblings(".collapse"),F=f(N.attr("data-target")),H=(F.length)?F.parent():null,D=null,L=null,y=null,G=null,E=0,M=0,K=N.outerHeight()-N.height()/4,C=false,z=function(){if(F.length){N.on("click",function(O){O.preventDefault()})}if(A.length){N.on("click",function(O){O.preventDefault()}).parent("li").removeClass("active");return true}else{return false}}(),I=null,J=function(O){clearInterval(I);I=setInterval(function(){O.nanoScroller({preventPageScrolling:true,alwaysVisible:true});clearInterval(I)},100)};f(document).click(function(O){if(!f(O.target).closest("#mainnav-container").length){N.removeClass("hover").popover("hide")}});f("#mainnav-menu-wrap > .nano").on("update",function(P,O){N.removeClass("hover").popover("hide")});N.popover({animation:false,trigger:"manual",container:"#mainnav",viewport:N,html:true,title:function(){if(z){return B.html()}return null},content:function(){var O;if(z){O=f('<div class="sub-menu"></div>');A.addClass("pop-in").wrap('<div class="nano-content"></div>').parent().appendTo(O)}else{if(F.length){O=f('<div class="sidebar-widget-popover"></div>');F.wrap('<div class="nano-content"></div>').parent().appendTo(O)}else{O='<span class="single-content">'+B.html()+"</span>"}}return O},template:'<div class="popover menu-popover"><h4 class="popover-title"></h4><div class="popover-content"></div></div>'}).on("show.bs.popover",function(){if(!D){D=N.data("bs.popover").tip();L=D.find(".popover-title");y=D.children(".popover-content");if(!z&&F.length==0){return}G=y.children(".sub-menu")}if(!z&&F.length==0){return}}).on("shown.bs.popover",function(){if(!z&&F.length==0){var O=0-(0.5*N.outerHeight());y.css({"margin-top":O+"px",width:"auto"});return}var R=parseInt(D.css("top")),P=N.outerHeight(),Q=function(){if(d.hasClass("mainnav-fixed")){return f(window).outerHeight()-R-P}else{return f(document).height()-R-P}}(),S=y.find(".nano-content").children().css("height","auto").outerHeight();y.find(".nano-content").children().css("height","");if(R>Q){if(L.length&&!L.is(":visible")){P=Math.round(0-(0.5*P))}R-=5;y.css({top:"",bottom:P+"px",height:R}).children().addClass("nano").css({width:"100%"}).nanoScroller({preventPageScrolling:true});J(y.find(".nano"))}else{if(!d.hasClass("navbar-fixed")&&e.hasClass("affix-top")){Q-=50}if(S>Q){if(d.hasClass("navbar-fixed")||e.hasClass("affix-top")){Q-=(P+5)}Q-=5;y.css({top:P+"px",bottom:"",height:Q}).children().addClass("nano").css({width:"100%"}).nanoScroller({preventPageScrolling:true});J(y.find(".nano"))}else{if(L.length&&!L.is(":visible")){P=Math.round(0-(0.5*P))}y.css({top:P+"px",bottom:"",height:"auto"})}}if(L.length){L.css("height",N.outerHeight())}y.on("click",function(){y.find(".nano-pane").hide();J(y.find(".nano"))})}).on("click",function(){if(!d.hasClass("mainnav-sm")){return}x.popover("hide");N.addClass("hover").popover("show")}).hover(function(){x.popover("hide");N.addClass("hover").popover("show").one("hidden.bs.popover",function(){N.removeClass("hover");if(z){A.removeAttr("style").appendTo(N.parent())}else{if(F.length){F.appendTo(H)}}clearInterval(w)})},function(){clearInterval(w);w=setInterval(function(){if(D){D.one("mouseleave",function(){N.removeClass("hover").popover("hide")});if(!D.is(":hover")){N.removeClass("hover").popover("hide")}}clearInterval(w)},100)})});v=true},b=function(){var w=f("#mainnav-menu").find(".collapse");if(w.length){w.each(function(){var x=f(this);if(x.hasClass("in")){x.parent("li").addClass("active")}else{x.parent("li").removeClass("active")}})}q.popover("destroy").unbind("mouseenter mouseleave");v=false},t=function(){var w=d.width(),x;if(w<=740){x="xs"}else{if(w>740&&w<992){x="sm"}else{if(w>=992&&w<=1200){x="md"}else{x="lg"}}}if(k!=x){k=x;o=x;if(o=="sm"&&d.hasClass("mainnav-lg")){f.niftyNav("collapse")}else{if(o=="xs"&&d.hasClass("mainnav-lg")){d.removeClass("mainnav-sm mainnav-out mainnav-lg").addClass("mainnav-sm")}else{if(o=="lg"){}}}}},s=function(){if(d.hasClass("boxed-layout")&&d.hasClass("mainnav-fixed")&&i.length){e.css({left:i.offset().left+"px"})}else{e.css({left:""})}},j=function(){if(!c){try{e.niftyAffix("update")}catch(w){c=true}}},h=function(w){j();b();t();s();if(m=="collapse"||d.hasClass("mainnav-sm")){d.removeClass("mainnav-in mainnav-out mainnav-lg");g()}n=f("#mainnav").height();m=false;return null},r=function(){if(!p){p={xs:"mainnav-out",sm:e.data("sm")||e.data("all"),md:e.data("md")||e.data("all"),lg:e.data("lg")||e.data("all")};var w=false;for(var x in p){if(p[x]){w=true;break}}if(!w){p=null}t()}},u={revealToggle:function(){if(!d.hasClass("reveal")){d.addClass("reveal")}d.toggleClass("mainnav-in mainnav-out").removeClass("mainnav-lg mainnav-sm");if(v){b()}return},revealIn:function(){if(!d.hasClass("reveal")){d.addClass("reveal")}d.addClass("mainnav-in").removeClass("mainnav-out mainnav-lg mainnav-sm");if(v){b()}return},revealOut:function(){if(!d.hasClass("reveal")){d.addClass("reveal")}d.removeClass("mainnav-in mainnav-lg mainnav-sm").addClass("mainnav-out");if(v){b()}return},slideToggle:function(){if(!d.hasClass("slide")){d.addClass("slide")}d.toggleClass("mainnav-in mainnav-out").removeClass("mainnav-lg mainnav-sm");if(v){b()}return},slideIn:function(){if(!d.hasClass("slide")){d.addClass("slide")}d.addClass("mainnav-in").removeClass("mainnav-out mainnav-lg mainnav-sm");if(v){b()}return},slideOut:function(){if(!d.hasClass("slide")){d.addClass("slide")}d.removeClass("mainnav-in mainnav-lg mainnav-sm").addClass("mainnav-out");if(v){b()}return},pushToggle:function(){d.toggleClass("mainnav-in mainnav-out").removeClass("mainnav-lg mainnav-sm");if(d.hasClass("mainnav-in mainnav-out")){d.removeClass("mainnav-in")}if(v){b()}return},pushIn:function(){d.addClass("mainnav-in").removeClass("mainnav-out mainnav-lg mainnav-sm");if(v){b()}return},pushOut:function(){d.removeClass("mainnav-in mainnav-lg mainnav-sm").addClass("mainnav-out");if(v){b()}return},colExpToggle:function(){if(d.hasClass("mainnav-lg mainnav-sm")){d.removeClass("mainnav-lg")}d.toggleClass("mainnav-lg mainnav-sm").removeClass("mainnav-in mainnav-out");return l.trigger("resize")},collapse:function(){d.addClass("mainnav-sm").removeClass("mainnav-lg mainnav-in mainnav-out");m="collapse";return l.trigger("resize")},expand:function(){d.removeClass("mainnav-sm mainnav-in mainnav-out").addClass("mainnav-lg");return l.trigger("resize")},togglePosition:function(){d.toggleClass("mainnav-fixed");j()},fixedPosition:function(){d.addClass("mainnav-fixed");a.nanoScroller({preventPageScrolling:true});j()},staticPosition:function(){d.removeClass("mainnav-fixed");a.nanoScroller({preventPageScrolling:false});j()},update:h,refresh:h,getScreenSize:function(){return k},bind:function(){var z=f("#mainnav-menu");if(z.length==0){return false}q=f('#mainnav-menu > li > a, #mainnav-menu-wrap .mainnav-widget a[data-toggle="menu-widget"]');d=f("#container");i=d.children(".boxed");e=f("#mainnav-container");n=f("#mainnav").height();var y=null;e.off("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd").on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd",function(A){if(y||A.target===e[0]){clearInterval(y);y=setInterval(function(){f(window).trigger("resize");clearInterval(y);y=null},300)}});var x=f(".mainnav-toggle");if(x.length){x.off("click").on("click",function(A){A.preventDefault();A.stopPropagation();if(x.hasClass("push")){f.niftyNav("pushToggle")}else{if(x.hasClass("slide")){f.niftyNav("slideToggle")}else{if(x.hasClass("reveal")){f.niftyNav("revealToggle")}else{f.niftyNav("colExpToggle")}}}})}try{}catch(w){console.error(w.message)}try{a=f("#mainnav-menu-wrap > .nano");if(a.length){a.nanoScroller({preventPageScrolling:(d.hasClass("mainnav-fixed")?true:false)})}}catch(w){console.error(w.message)}f(window).off("resizeEnd").on("resizeEnd",h).trigger("resize")}};f.niftyNav=function(y,w){if(u[y]){if(y=="colExpToggle"||y=="expand"||y=="collapse"){if(o=="xs"&&y=="collapse"){y="pushOut"}else{if((o=="xs"||o=="sm")&&(y=="colExpToggle"||y=="expand")&&d.hasClass("mainnav-sm")){y="pushIn"}}}var x=u[y].apply(this,Array.prototype.slice.call(arguments,1));if(y!="bind"){h()}if(w){return w()}else{if(x){return x}}}return null};f.fn.isOnScreen=function(){var w={top:l.scrollTop(),left:l.scrollLeft()};w.right=w.left+l.width();w.bottom=w.top+l.height();var x=this.offset();x.right=x.left+this.outerWidth();x.bottom=x.top+this.outerHeight();return(!(w.right<x.left||w.left>x.right||w.bottom<x.bottom||w.top>x.top))}}(jQuery);!function(g){var f=null,i,k=g(window),b={toggleHideShow:function(){i.toggleClass("aside-in");k.trigger("resize");if(i.hasClass("aside-in")){d()}},show:function(){i.addClass("aside-in");k.trigger("resize");d()},hide:function(){i.removeClass("aside-in");k.trigger("resize")},toggleAlign:function(){i.toggleClass("aside-left");c()},alignLeft:function(){i.addClass("aside-left");c()},alignRight:function(){i.removeClass("aside-left");c()},togglePosition:function(){i.toggleClass("aside-fixed");c()},fixedPosition:function(){i.addClass("aside-fixed");c()},staticPosition:function(){i.removeClass("aside-fixed");c()},toggleTheme:function(){i.toggleClass("aside-bright")},brightTheme:function(){i.addClass("aside-bright")},darkTheme:function(){i.removeClass("aside-bright")},update:function(){c()},bind:function(){j()}},d=function(){var l=i.width();if(i.hasClass("mainnav-in")&&l>740){if(l>740&&l<992){g.niftyNav("collapse")}else{i.removeClass("mainnav-in mainnav-lg mainnav-sm").addClass("mainnav-out")}}},e=g("#container").children(".boxed"),h=0,a=0,c=function(){try{f.niftyAffix("update")}catch(m){}var l={};if(i.hasClass("boxed-layout")&&i.hasClass("aside-fixed")&&e.length){if(i.hasClass("aside-left")){l={"-ms-transform":"translateX("+e.offset().left+"px)","-webkit-transform":"translateX("+e.offset().left+"px)",transform:"translateX("+e.offset().left+"px)"}}else{l={"-ms-transform":"translateX("+(0-e.offset().left)+"px)","-webkit-transform":"translateX("+(0-e.offset().left)+"px)",transform:"translateX("+(0-e.offset().left)+"px)"}}}else{l={"-ms-transform":"","-webkit-transform":"",transform:"",right:""}}f.css(l)},j=function(){f=g("#aside-container");if(f.length){i=g("#container");k.off("resizeEnd").on("resizeEnd",c);f.off("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd").on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd",function(m){if(m.target==f[0]){k.trigger("resize")}});f.find(".nano").nanoScroller({preventPageScrolling:(i.hasClass("aside-fixed")?true:false)});var l=g(".aside-toggle");if(l.length){l.off("click").on("click",function(m){m.preventDefault();g.niftyAside("toggleHideShow")})}}};g.niftyAside=function(m,l){if(b[m]){b[m].apply(this,Array.prototype.slice.call(arguments,1));if(l){return l()}}return null}}(jQuery);!function(f){var b,d,i,g,e,h,c=function(j){clearInterval(h);h=setInterval(function(){if(j[0]==b[0]){g.nanoScroller({flash:true,preventPageScrolling:(i.hasClass("mainnav-fixed")?true:false)})}else{if(j[0]==d[0]){e.nanoScroller({preventPageScrolling:(i.hasClass("aside-fixed")?true:false)})}}clearInterval(h);h=null},500)},a=function(){i=f("#container");b=f("#mainnav-container");d=f("#aside-container");g=f("#mainnav-menu-wrap > .nano");e=f("#aside > .nano");if(b.length){b.niftyAffix({className:"mainnav-fixed"})}if(d.length){d.niftyAffix({className:"aside-fixed"})}};f.fn.niftyAffix=function(j){return this.each(function(){var l=f(this),k;if(typeof j==="object"||!j){k=j.className;l.data("nifty.af.class",j.className)}else{if(j=="update"){if(!l.data("nifty.af.class")){a()}k=l.data("nifty.af.class");c(l)}else{if(j=="bind"){a()}}}if(i.hasClass(k)&&!i.hasClass("navbar-fixed")){l.affix({offset:{top:f("#navbar").outerHeight()}}).on("affixed.bs.affix affix.bs.affix",function(){c(l)})}else{if(!i.hasClass(k)||i.hasClass("navbar-fixed")){f(window).off(l.attr("id")+".affix");l.removeClass("affix affix-top affix-bottom").removeData("bs.affix")}}})}}(jQuery);
