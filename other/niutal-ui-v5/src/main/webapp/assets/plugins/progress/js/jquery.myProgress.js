/**
 * myProgress.js
 * Version: 1.0
 * Author: Mahuaide
 * Download:
 * You may use this script for free
 */
;
(function ($) {
    if (typeof($.fn.myProgress) != 'undefined') {
        return false;
    }
    $.fn.myProgress = function (options) {
        initOptions(options);
        $(this).each(function () {
            var this_ = $(this);
            var $percent = $(this).find("div.percent-show>span");
            var progress_in = $(this).find("div.progress-bar");
            initCss(options, $(this));
            var t = setInterval(function () {
                $percent.html(parseInt(progress_in.width() / this_.width() * 100))
            }, options.speed / 100);
            progress_in.animate({
                width: options.percent + "%"
            }, options.speed, function () {
                clearInterval(t);
                t = null;
                $percent.html(options.percent);
                options.percent == 100 && progress_in.css("border-radius", 0);
            });
        });
        return $(this);
    }

    function initOptions(options) {
        (!options.hasOwnProperty("speed") || isNaN(options.speed)) && (options.speed = 1000);
        (!options.hasOwnProperty("percent") || isNaN(options.percent)) && (options.percent = 100);
        !options.hasOwnProperty("width") && (options.width = '180px');
        !options.hasOwnProperty("height") && (options.height = '20px');
        !options.hasOwnProperty("direction") && (options.direction = 'left');
        options.fontSize = Math.floor(parseInt(options.height) * 6 / 10) + "px";
        options.lineHeight = options.height;
    }

    function initCss(options, obj) {
        obj.css({
            "width": options.width,
            "height": options.height
        }).find("div.percent-show").css({
            "lineHeight": options.lineHeight,
            "fontSize": options.fontSize
        });
        if(options.direction =="right"){
            obj.find("div.progress-in").addClass("direction-right");
        }else{
            obj.find("div.progress-in").addClass("direction-left");
        }
    }
})(jQuery);
