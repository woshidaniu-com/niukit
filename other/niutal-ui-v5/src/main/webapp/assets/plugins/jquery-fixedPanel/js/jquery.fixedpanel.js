/* FixedPanel, v1.2.1 (2012-07-12), Huberto Kusters */
(function ($) {
    /*
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || Plugin:  FixedPanel
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || FixedPanel is a plugin that makes specified elements permanent visible within one or more specified containers.
    || If no container is specified then the body element is used.
    || If one of the containers is scrolled, and the specified element is about to disappear, the plugin sees to it that the
    || element is possitioned as fixed and as a result will stay visible. If however, the fixed position is one of the other
    || specified containers, then the element is positioned absolute within the inner most container and will scroll out of view.
    || In the test below the div#menu_container element will remain visible within the containers div#contents_container and body.
    || The div#button_bar element will remain visible within the containers div#main_container and body.
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || Example FixedPanelTest.html
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    || <html xmlns="http://www.w3.org/1999/xhtml" >
    ||     <head>
    ||         <title>Fixed Panel Test</title>
    ||         <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    ||         <script type="text/javascript" src="jquery.fixedpanel.js"></script>
    ||         <script type="text/javascript" src="FixedPanelTest.js"></script>
    ||         <link type="text/css" rel="Stylesheet" href="FixedPanelTest.css" />
    ||     </head>
    ||     <body>
    ||         <div id="main_container">
    ||             <div id="header">Header</div>
    ||             <div id="contents_container">
    ||                 <div id="menu_container">
    ||                     <ol>
    ||                         <li>Home</li>
    ||                         <li>Links</li>
    ||                         <li>About</li>
    ||                     </ol>
    ||                 </div>
    ||                 <div id="contents">
    ||     Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed venenatis lacus vitae lectus sagittis non ornare erat auctor. Sed eget auctor est. Nam condimentum, mi at congue pellentesque, sem tortor iaculis arcu, dapibus facilisis diam justo quis risus. Donec gravida consequat tellus, in tempor ipsum feugiat vitae. Proin tempus varius est sed aliquet. Praesent viverra, mauris eu vehicula convallis, diam velit elementum massa, vitae gravida nisi est nec mauris. Morbi metus odio, porta vel lacinia eget, bibendum ac enim. In hac habitasse platea dictumst. Praesent sapien nibh, malesuada tristique tristique vel, congue faucibus nisl. Nunc nibh purus, commodo eu vestibulum non, fringilla ac magna. Phasellus eleifend malesuada faucibus. Cras ultricies lobortis justo, id rutrum sem elementum non. Maecenas ante ligula, ullamcorper scelerisque aliquet sit amet, cursus eu metus. Fusce eget libero nibh. Proin in quam eget nisl molestie porttitor. Ut interdum rhoncus lorem, sed cursus arcu dapibus ac.
    ||
    ||     Praesent a metus ligula. Nulla sit amet magna orci. Curabitur at odio nisl, rhoncus accumsan dolor. Vivamus enim turpis, sagittis id posuere nec, auctor id dolor. Pellentesque at libero et mi tincidunt mattis. Nulla tempus ligula et enim viverra vitae fermentum nunc placerat. Curabitur at laoreet tortor. Suspendisse porttitor mattis malesuada. Pellentesque at lectus elit. Aliquam vel neque id eros dapibus ultricies. Sed pellentesque diam id lacus ultricies porta. Morbi ut enim erat, nec bibendum tellus. Praesent dapibus blandit nisi, sed tempus tortor pulvinar in. Sed tristique, lorem et facilisis pharetra, lorem nulla elementum urna, id tempus risus mi et turpis. Nam venenatis porta nibh, at ornare libero aliquet in. Nullam vitae dui est.
    ||
    ||     Fusce dictum hendrerit nisi sed sollicitudin. Mauris eget velit et libero pellentesque adipiscing ac ut magna. Cras quam enim, vehicula a ultrices in, porttitor ac urna. Sed id cursus tellus. Vivamus aliquam urna eu sem pulvinar non pellentesque orci dapibus. Aliquam a arcu vel neque rhoncus convallis nec eu mi. Donec molestie, leo nec ullamcorper congue, risus turpis euismod arcu, a dapibus enim mauris sed diam. Praesent imperdiet bibendum est, aliquam porttitor mi tincidunt et.
    ||
    ||     Praesent in purus sem, vel dignissim nisi. Aenean nec nulla in felis interdum semper at et diam. Ut quis lacus lacus. Nullam at urna magna, in bibendum felis. Nam feugiat lobortis congue. Mauris non mauris vel mi laoreet venenatis eu quis lacus. Nunc elementum accumsan metus, eu semper sem euismod sit amet. Suspendisse potenti. Nulla facilisi. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin lobortis libero sed risus dignissim imperdiet consequat metus cursus. Nulla facilisi. Cras mollis semper adipiscing. Sed eget augue orci. Donec pretium luctus eros, et eleifend magna gravida vitae. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.
    ||
    ||     Vestibulum sem magna, scelerisque quis vestibulum eget, malesuada vel sem. Duis dictum urna non eros sollicitudin congue. Pellentesque metus dolor, interdum non faucibus vitae, aliquam eu magna. Etiam tortor nisi, adipiscing sed varius et, ornare id purus. Donec vehicula ante elementum nisi fringilla porta. Pellentesque a elit sit amet nisi vestibulum tempus in faucibus est. Aenean scelerisque diam eget justo aliquam venenatis. Sed elementum, mi nec vehicula vulputate, risus magna fermentum metus, non pretium dolor magna vel lacus. Nam ac sapien ut neque sollicitudin vulputate. Donec non ante a odio interdum aliquam. Aliquam erat volutpat.
    ||
    ||     Cras neque metus, auctor in accumsan nec, hendrerit at purus. Ut rutrum blandit placerat. Etiam eros turpis, congue vitae cursus ut, rhoncus eu eros. Sed fringilla commodo augue eu scelerisque. Morbi aliquet dui sed lacus vulputate ultrices. Donec ac placerat ligula. Ut iaculis bibendum augue, et gravida lectus malesuada ut. Nullam suscipit, odio mattis porttitor ultrices, nunc erat laoreet turpis, in elementum neque dui et mi. Nam rhoncus ultricies turpis eu feugiat. Mauris neque velit, consectetur vel cursus et, suscipit eget augue. Duis ante odio, dictum vel porta et, gravida id mauris. Morbi fermentum venenatis odio, ut ultricies arcu dapibus non. Integer feugiat adipiscing justo fermentum tempus.
    ||
    ||     Nam sapien velit, porttitor id dapibus nec, vulputate ut quam. Nam rutrum eros et magna aliquet placerat. Integer auctor mauris tortor, sit amet facilisis lacus. Nulla consectetur nisi lorem. Nam sodales scelerisque egestas. Nulla egestas luctus ornare. Morbi et dictum urna. Fusce massa leo, posuere et tempus eu, volutpat a lacus. Aliquam erat volutpat.
    ||
    ||     Praesent magna lectus, consectetur et vehicula a, faucibus ac lorem. Praesent eget ante nunc, ac lacinia enim. Nulla laoreet quam eu augue pellentesque vitae vestibulum orci suscipit. Ut vestibulum interdum euismod. Praesent volutpat viverra leo, eget tristique est hendrerit eu. Integer urna justo, consectetur ac molestie a, condimentum in libero. Nam sollicitudin arcu nisl, a sodales urna. Vivamus ultricies ultrices sapien eu blandit. Fusce hendrerit dui erat. Proin lobortis placerat nisi in porttitor. Curabitur congue nisl id metus ornare bibendum. Nullam nec quam ut eros luctus luctus quis eget felis. Suspendisse a aliquam orci. Vivamus gravida quam tortor. Vivamus bibendum suscipit convallis. Etiam placerat augue eget libero vestibulum tempor.
    ||
    ||     Ut fringilla laoreet luctus. Proin convallis aliquam lobortis. Proin sit amet dui arcu, mollis tempus tortor. Curabitur eu libero aliquet purus vehicula varius. Cras pharetra molestie felis at porttitor. Sed ac justo et sem convallis rutrum eu adipiscing tortor. Cras pulvinar auctor dolor a auctor. Aliquam urna dui, ultricies at fermentum vestibulum, vehicula dictum enim. Aenean aliquam ornare lorem eu pretium. Phasellus urna orci, commodo vitae ornare a, ornare id purus. Pellentesque a diam mauris. Praesent pretium aliquam sapien.
    ||
    ||     Mauris commodo imperdiet laoreet. Fusce vestibulum elit luctus eros egestas vel dictum elit egestas. Integer adipiscing posuere molestie. Aliquam venenatis imperdiet justo ut lobortis. Quisque quis eros ut arcu fermentum pharetra ac a elit. Donec et luctus nunc. Donec eget adipiscing mauris. Nunc in lectus elit. Duis quis pharetra nisi. Vivamus nec tempus risus. Duis sagittis ultricies est eu luctus. Donec fermentum justo ut eros vulputate dapibus. In diam est, feugiat non ultricies at, viverra sit amet mi. Mauris fringilla condimentum felis eget bibendum. Quisque erat elit, suscipit eget fermentum vulputate, tempor non massa.
    ||                 </div>
    ||                 <div class="clear"></div>
    ||             </div>
    ||             <div id="button_bar">
    ||                 <button>Button A</button>
    ||                 <button>Button B</button>
    ||                 <button>Button C</button>
    ||                 <button>Button D</button>
    ||                 <button>Button E</button>
    ||             </div>
    ||             <div id="footer">Footer</div>
    ||         </div>
    ||         <div id="filler"></div>
    ||     </body>
    || </html>
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || Example FixedPanelTest.js
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || $(window).ready(function (){
    ||     $('div#menu_container').FixedPanel({
    ||         Containers: $('div#contents_container, body')
    ||     });
    ||     $('div#button_bar').FixedPanel({
    ||         Containers : $('div#main_container, body')
    ||     });
    || });
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || Example FixedPanelTest.css
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    || div, body, html {
    ||     font-family: verdana;
    ||     font-size: 14px;
    ||     background-color: #fff;
    ||     margin: 0px;
    ||     padding: 0px;
    ||     border: 0px;
    || }
    ||
    || .clear {
    ||     position:relative;
    ||     height: 1px;
    ||     width: 100%;
    ||     clear:both;
    ||     margin-bottom: -1px;
    || }
    ||
    || div#main_container {
    ||     position: relative;
    ||     width: 1000px;
    ||     margin: 10px auto;
    ||     border: 1px solid #ccc;
    || }
    ||
    || div#header, div#footer, div#contents_container, div#button_bar  {
    ||     position: relative;
    ||     width:100%;
    || }
    ||
    || div#header, div#footer, div#button_bar {
    ||     height: 30px;
    ||     background-color: #eee;
    ||     text-align: center;
    ||     padding: 10px 0px;
    || }
    ||
    || div#contents, div#menu_container {
    ||     float: left;
    || }
    ||
    || div#contents {
    ||     width: 839px;
    ||     border-left: 1px solid #ccc;
    ||     padding: 5px;
    || }
    ||
    || div#menu_container {
    ||     width: 150px;
    || }
    ||
    || div#filler {
    ||     width:100%;
    ||     height: 1000px;
    || }
    || --------------------------------------------------------------------------------------------------------------------------------------------------
    */
    /* validated with http://www.jslint.com/ */
    "use strict";
    /*jslint browser: true, maxerr: 50, indent: 4, devel: true*/
    /*global jQuery*/

    var DataElement = 'fixedpanel-element',
        ContainerDataElement = 'fixedpanel-container',
        OffsetContainsBorder = null,
        WindowResizeSetup = false,

    log = function (message) {
        if (window.console) { console.log(message); }
    },

    isBody = function (elem) {
        if (elem instanceof jQuery) {
            return elem.get(0).tagName === 'BODY';
        }
        return elem.tagName === 'BODY';
    },

    isHtml = function (elem) {
        if (elem instanceof jQuery) {
            return elem.get(0).tagName === 'HTML';
        }
        return elem.tagName === 'HTML';
    },

    isDocument = function (elem) {
        if (elem instanceof jQuery) {
            return elem.get(0) === document || elem.get(0) === document.documentElement;
        }
        return elem === document || elem === document.documentElement;
    },

    isViewport = function (elem) {
        if (elem instanceof jQuery) {
            return elem.get(0) === window;
        }
        return elem === window;
    },

    getPosition = function (elem, parentElem) {
        var obj = elem.get(0),
            parentObj = parentElem ? parentElem.get(0) : null,
            position = {
                top: obj.offsetTop || 0,
                left: obj.offsetLeft || 0
            };
        while (!isBody(obj) && obj.offsetParent && obj.offsetParent != parentObj &&
            !isBody(obj.offsetParent) && !isHtml(obj.offsetParent)) {
            obj = obj.offsetParent;
            position.top += obj.offsetTop || 0;
            position.left += obj.offsetLeft || 0;
            position.top -= obj.scrollTop || 0;
            position.left -= obj.scrollLeft || 0;
            if (!getOffsetContainsBorder()) {
                position.top += obj.clientTop || 0;
                position.left += obj.clientLeft || 0;
            }
        }
        if (getOffsetContainsBorder() && obj.offsetParent) {
            position.top -= obj.offsetParent.clientTop || 0;
            position.left -= obj.offsetParent.clientLeft || 0;
        }
        // This is necessary in order to make use of the body element as a container
        if (!isBody(elem) && (parentObj === null || isBody(parentObj))) {
            position.top -= $(window).scrollTop() || 0;
            position.left -= $(window).scrollLeft() || 0;
        }
        return position;
    },

    getDimensionsWithoutScrollBar = function (elem) {
        var obj = elem.get(0);
        if (!isBody(obj)) {
            return {
                width: obj.clientWidth,
                height : obj.clientHeight
            };
        }
        return {
            width: $(window).width(),
            height : $(window).height()
        };
    },

    getDimensions = function (elem) {
        var obj = elem.get(0);
        return {
            width: obj.offsetWidth,
            height : obj.offsetHeight
        };
    },

    getOffsetContainsBorder = function () {
        if (OffsetContainsBorder !== null) return OffsetContainsBorder;
        var elem1 =
            $('<div></div>').css({
                'position' : 'absolute',
                'width' : '100px',
                'height' : '100px',
                'border' : '10px solid black',
                'left' : '10px',
                'top' : '10px',
                'visibility' : 'hidden'
            }),
            elem2 =
            $('<div></div>').css({
                'position' : 'relative',
                'width' : '10px',
                'height' : '10px',
                'border' : '0px',
                'left' : '10px',
                'top' : '10px',
                'visibility' : 'hidden'
            }),
            obj = null,
            offsetLeft,
            offsetTop;

        $('body').append(elem1.append(elem2));
        obj = elem2.get(0);
        offsetLeft = obj.offsetLeft;
        offsetTop = obj.offsetTop;
        elem1.css({
            'border' : '0px'
        });
        OffsetContainsBorder = offsetLeft != obj.offsetLeft || offsetTop != obj.offsetTop;
        elem1.remove();
        return OffsetContainsBorder;
    },

    intersectRanges = function (range1, range2) {
        var result = {
            xmin : 0,
            xmax : 0,
            ymin : 0,
            ymax : 0,
            leftPriority: 0,
            rightPriority: 0,
            topPriority: 0,
            bottomPriority: 0
        };
        if (range1.xmin > range2.xmin) {
            result.xmin = range1.xmin;
            result.leftPriority = range1.leftPriority;
        } else if (range1.xmin === range2.xmin) {
            result.xmin = range1.xmin;
            result.leftPriority = Math.max(range1.leftPriority, range2.leftPriority);
        } else {
            result.xmin = range2.xmin;
            result.leftPriority = range2.leftPriority;
        }
        if (range1.xmax < range2.xmax) {
            result.xmax = range1.xmax;
            result.rightPriority = range1.rightPriority;
        } else if (range1.xmax === range2.xmax) {
            result.xmax = range1.xmax;
            result.rightPriority = Math.max(range1.rightPriority, range2.rightPriority);
        } else {
            result.xmax = range2.xmax;
            result.rightPriority = range2.rightPriority;
        }
        if (range1.ymin > range2.ymin) {
            result.ymin = range1.ymin;
            result.topPriority = range1.topPriority;
        } else if (range1.ymin === range2.ymin) {
            result.ymin = range1.ymin;
            result.topPriority = Math.max(range1.topPriority, range2.topPriority);
        } else {
            result.ymin = range2.ymin;
            result.topPriority = range2.topPriority;
        }
        if (range1.ymax < range2.ymax) {
            result.ymax = range1.ymax;
            result.bottomPriority = range1.bottomPriority;
        } else if (range1.ymax === range2.ymax) {
            result.ymax = range1.ymax;
            result.bottomPriority = Math.max(range1.bottomPriority, range2.bottomPriority);
        } else {
            result.ymax = range2.ymax;
            result.bottomPriority = range2.bottomPriority;
        }
        return result;
    },

    moveIntoRange = function (rangeToMove, rangeToMoveInto) {
        var result = {
            moved : false,
            fitting : true
        };

        if (rangeToMove.xmin < rangeToMoveInto.xmin) {
            result.moved = true;
            rangeToMove.xmax += rangeToMoveInto.xmin - rangeToMove.xmin;
            rangeToMove.xmin = rangeToMoveInto.xmin;
            if (rangeToMove.xmax > rangeToMoveInto.xmax) {
                result.fitting = false;
                if (rangeToMoveInto.rightPriority > rangeToMoveInto.leftPriority) {
                    rangeToMove.xmin -= rangeToMove.xmax - rangeToMoveInto.xmax;
                    rangeToMove.xmax = rangeToMoveInto.xmax;
                }
            }
        }
        else if (rangeToMove.xmax > rangeToMoveInto.xmax) {
            result.moved = true;
            rangeToMove.xmin -= rangeToMove.xmax - rangeToMoveInto.xmax;
            rangeToMove.xmax = rangeToMoveInto.xmax;
            if (rangeToMove.xmin < rangeToMoveInto.xmin) {
                result.fitting = false;
                if (rangeToMoveInto.leftPriority > rangeToMoveInto.rightPriority) {
                    rangeToMove.xmax += rangeToMoveInto.xmin - rangeToMove.xmin;
                    rangeToMove.xmin = rangeToMoveInto.xmin;
                }
            }
        }
        if (rangeToMove.ymin < rangeToMoveInto.ymin) {
            result.moved = true;
            rangeToMove.ymax += rangeToMoveInto.ymin - rangeToMove.ymin;
            rangeToMove.ymin = rangeToMoveInto.ymin;
            if (rangeToMove.ymax > rangeToMoveInto.ymax) {
                result.fitting = false;
                if (rangeToMoveInto.bottomPriority > rangeToMoveInto.topPriority) {
                    rangeToMove.ymin -= rangeToMove.ymax - rangeToMoveInto.ymax;
                    rangeToMove.ymax = rangeToMoveInto.ymax;
                }
            }
        }
        else if (rangeToMove.ymax > rangeToMoveInto.ymax) {
            result.moved = true;
            rangeToMove.ymin -= rangeToMove.ymax - rangeToMoveInto.ymax;
            rangeToMove.ymax = rangeToMoveInto.ymax;
            if (rangeToMove.ymin < rangeToMoveInto.ymin) {
                result.fitting = false;
                if (rangeToMoveInto.topPriority > rangeToMoveInto.bottomPriority) {
                    rangeToMove.ymax += rangeToMoveInto.ymin - rangeToMove.ymin;
                    rangeToMove.ymin = rangeToMoveInto.ymin;
                }
            }
        }
        return result;
    },

    getOuterRange = function (elem) {
        var pos = getPosition(elem),
            dim = getDimensions(elem),
            result = {
                xmin : pos.left,
                xmax : pos.left + dim.width - 1,
                ymin : pos.top,
                ymax : pos.top + dim.height - 1,
                leftPriority: 1,
                rightPriority: 1,
                topPriority: 1,
                bottomPriority: 1
            };
        return result;
    },

    getInnerRange = function (elem, priority) {
        /* IMPORTANT: the higher the priority the better */
        var pos = getPosition(elem),
            dim = getDimensionsWithoutScrollBar(elem),
            obj = elem.get(0),
            result = {
                xmin : pos.left + (obj.clientLeft || 0),
                xmax : pos.left + (obj.clientLeft || 0) + dim.width - 1,
                ymin : pos.top + (obj.clientTop || 0),
                ymax : pos.top + (obj.clientTop || 0) + dim.height - 1,
                leftPriority: priority || 1,
                rightPriority: priority || 1,
                topPriority: priority || 1,
                bottomPriority: priority || 1
            };
        return result;
    },

    getInnerRangeOfElements = function (elems) {
        var range1 = null,
            range2 = null,
            i = 0;

        /* Remember: outer containers get lower priority then inner containers */
        if (elems && elems.length !== 0) {
            range2 = getInnerRange(elems[0], 1);
            for (i = 1; i < elems.length; i += 1) {
                range1 = range2;
                range2 = intersectRanges(range1, getInnerRange(elems[i], i + 1));
            }
        }
        return range2;
    },

    getRangeToPosition = function (range, container) {
        var containerRange = getInnerRange(container),
            result = {
                top: 0,
                left: 0
            };
        result.left = range.xmin - containerRange.xmin;
        result.top = range.ymin - containerRange.ymin;
        // adjust for border
        result.left -= container.clientLeft || 0;
        result.top -= container.clientTop || 0;
        // adjust for scrollposition
        result.left += isBody(container) ? $(window).scrollLeft() : container.scrollLeft();
        result.top += isBody(container) ? $(window).scrollTop() : container.scrollTop();
        return result;
    },

    adjustRangeWithLimits = function (range, toplimits, rightlimits, bottomlimits, leftlimits) {
        var limit = null,
            limitRange = null;

        if (toplimits) {
            $.each(toplimits, function () {
                limit = $(this);
                limitRange = getOuterRange(limit);

                if (range.ymin <= limitRange.ymax) {
                    range.ymin = limitRange.ymax + 1;
                    range.topPriority = 9999;
                }
            });
        }

        if (bottomlimits) {
            $.each(bottomlimits, function () {
                limit = $(this);
                limitRange = getOuterRange(limit);

                if (range.ymax >= limitRange.ymin) {
                    range.ymax = limitRange.ymin - 1;
                    range.bottomPriority = 9999;
                }
            });
        }

        if (leftlimits) {
            $.each(leftlimits, function () {
                limit = $(this);
                limitRange = getOuterRange(limit);

                if (range.xmin <= limitRange.xmax) {
                    range.xmin = limitRange.xmax + 1;
                    range.leftPriority = 9999;
                }
            });
        }

        if (rightlimits) {
            $.each(rightlimits, function () {
                limit = $(this);
                limitRange = getOuterRange(limit);

                if (range.xmax >= limitRange.xmin) {
                    range.xmax = limitRange.xmin - 1;
                    range.rightPriority = 9999;
                }
            });
        }
        return range;
    },

    doScroll = function (elem, options) {
        if (!elem.data(DataElement)) return;

        var data = elem.data(DataElement),
            containers = data.containers,
            placeholder = data.placeholder,
            containerRange = adjustRangeWithLimits(getInnerRangeOfElements(containers), data.toplimits, data.rightlimits, data.bottomlimits, data.leftlimits),
            placeholderRange = getOuterRange(placeholder),
            container = containers[containers.length - 1],
            moveResult = moveIntoRange(placeholderRange, containerRange),
            position = null;

        if (!moveResult.fitting || !moveResult.moved) {
            // placeholder does not fit or it is not moved, so we place it abolute in its container
            // We have to convert the absolute dimensions to relative dimensions within the container
            position = getRangeToPosition(placeholderRange, container);
            elem.css({
                'position' : 'absolute',
                'left' : position.left + 'px',
                'top' : position.top + 'px',
                'width' : placeholder.css('width'),
                'height' : placeholder.css('height')
            });
            if (!elem.hasClass(options.UnfixedClass)) {
                elem.removeClass(options.FixedClass).addClass(options.UnfixedClass);
                if (options.UnfixedCallback) {
                    options.UnfixedCallback(elem);
                }
            }
        } else {
            // placeholder had to be moved and it fits, so we need to place it fixed in the viewport
            // We only have to adjust for the scrollposition of the window.
            elem.css({
                'position' : 'fixed',
                'left' : placeholderRange.xmin + 'px',
                'top' : placeholderRange.ymin + 'px',
                'width' : placeholder.width() + 'px',
                'height' : placeholder.height() + 'px'
            });
            if (!elem.hasClass(options.FixedClass)) {
                elem.removeClass(options.UnfixedClass).addClass(options.FixedClass);
                if (options.FixedCallback) {
                    options.FixedCallback(elem);
                }
            }
        }
    },

    setupResize = function () {
        if (WindowResizeSetup === true) { return; }
        $(window).resize(function () {
            $('body').scroll();
        });
        WindowResizeSetup = true;
    },

    setupLimits = function (elem, limits) {
        var data = elem.data(DataElement),
            elemRange = getOuterRange(elem),
            limit = null,
            limitRange = null;

        limits.each(function () {
            limit = $(this);
            limitRange = getOuterRange(limit);

            if (limitRange.xmax < elemRange.xmin) {
                if (!data.leftlimits) { data.leftlimits = []; }
                data.leftlimits.push(limit);
            }
            else if (limitRange.xmin > elemRange.xmax) {
                if (!data.rightlimits) { data.rightlimits = []; }
                data.rightlimits.push(limit);
            }

            if (limitRange.ymax < elemRange.ymin) {
                if (!data.toplimits) { data.toplimits = []; }
                data.toplimits.push(limit);
            }
            else if (limitRange.ymin > elemRange.ymax) {
                if (!data.bottomlimits) { data.bottomlimits = []; }
                data.bottomlimits.push(limit);
            }
        });
    },

    setupElement = function (elem, options) {
        var containers = options.Containers,
            parent = null,
            placeholder = null,
            elemContainer = null,
            position = null,
            offsetParents = [],
            newContainers = [],
            i = 0,
            checkFunc = null,
            scrollFunc = null;

        // Check validity of the element
        if (isBody(elem) || isHtml(elem) || isViewport(elem) || isDocument(elem)) {
            log('FixedPanel: supplied element must not be the body- or html-element or the window- or document-object.');
            return;
        }

        // Check if the element is not yet setup
        if (elem.data(DataElement)) {
            log('FixedPanel: supplied element is already setup.');
            return;
        }

        // Collect list of offsetParents in order of outer to inner
        parent = elem;
        do {
            parent = parent.offsetParent();
            if (parent.length !== 0) {
                offsetParents.unshift(parent);
            }
        } while (parent.length !== 0 && !isBody(parent));

        // Check if containers are offsetParents
        checkFunc = function () {
            var container = this;
            if (isHtml(container) || isViewport(container)) {
                container = $('body').get(0);
            }
            if (container === offsetParents[i].get(0)) {
                newContainers.push(offsetParents[i]);
                return false; /* stop each function */
            }
        };
        for (i = 0; i < offsetParents.length && newContainers.length != containers.length; i += 1) {
            containers.each(checkFunc);
        }
        //IMPORTANT: newContainers contains the supplied containers but now in order of outer to inner.
        if (newContainers.length === 0) {
            log('FixedPanel: supplied containers are not offsetParents.');
            return;
        }
        if (newContainers.length !== containers.length) {
            log('FixedPanel: some supplied containers are not offsetParents.');
        }

        // Get inner container
        elemContainer = newContainers[newContainers.length - 1];

        // setup scroll handlers for all offsetParents
        scrollFunc = function (event) {
            var container = isViewport($(this)) ? $('body') : $(this),
                me = $(this),
                containerData = container.data(ContainerDataElement),
                i = 0;

            // This might look strange, but it is necessary for IE.
            // For some reason, the real scrollbar position and scrollLeft and/or scrollRight positions
            // are sometimes out of sync, which produces different position calculations between absolute
            // and fixed locations. As a result the element is sometimes 'jumping' 1 pixel when switching
            // between absolute and fixed positions.
            // To overcome this, we explicitly set the scrollLeft and scrollRight to sync the scrollbars.
            me.scrollLeft(me.scrollLeft());
            me.scrollTop(me.scrollTop());

            if (!containerData || !containerData.elements || containerData.elements.length === 0) { return; }
            event.stopPropagation();

            for (i = 0; i < containerData.elements.length; i += 1) {
                doScroll(containerData.elements[i], options);
            }
        };

        for (i = 0; i < offsetParents.length; i += 1) {
            var offsetParent = offsetParents[i];
            // First register element with offsetParent
            if (!offsetParent.data(ContainerDataElement)) {
                offsetParent.data(ContainerDataElement, {
                    elements : [elem]
                });
                if (isBody(offsetParent)) {
                    $(window).scroll(scrollFunc);
                } else {
                    offsetParent.scroll(scrollFunc);
                }

            } else {
                offsetParent.data(ContainerDataElement).elements.push(elem);
            }
        }

        // setup element

        // substitute element by placeholder
        placeholder = $('<div></div>')
            .height(elem.height())
            .width(elem.width())
            .addClass(options.PlaceholderClass)
            .css({
                'border-color'          : 'transparent',
                /* For some reason this cannot be set when using IE7/8.
                   We can skip this because it is the default anyway.
                'background-color'      : 'transparent',
                */
                'position'              : elem.css('position'),
                'left'                  : elem.css('left'),
                'top'                   : elem.css('top'),
                'margin-top'            : elem.css('margin-top'),
                'margin-right'          : elem.css('margin-right'),
                'margin-bottom'         : elem.css('margin-bottom'),
                'margin-left'           : elem.css('margin-left'),
                'padding-top'           : elem.css('padding-top'),
                'padding-right'         : elem.css('padding-right'),
                'padding-bottom'        : elem.css('padding-bottom'),
                'padding-left'          : elem.css('padding-left'),
                'border-left-width'     : elem.css('border-left-width'),
                'border-top-width'      : elem.css('border-top-width'),
                'border-right-width'    : elem.css('border-right-width'),
                'border-bottom-width'   : elem.css('border-bottom-width'),
                'border-left-style'     : elem.css('border-left-style'),
                'border-top-style'      : elem.css('border-top-style'),
                'border-right-style'    : elem.css('border-right-style'),
                'border-bottom-style'   : elem.css('border-bottom-style'),
                'float'                 : elem.css('float'),
                'clear'                 : elem.css('clear')
            });
        position = getPosition(elem, elemContainer);
        placeholder.insertAfter(elem);
        elem = elem.remove();
        elem.css({
            'position'          : 'absolute',
            'left'              : position.left + 'px',
            'top'               : position.top + 'px',
            'z-index'           : 10
        }).addClass(options.FixedClass);
        elemContainer.append(elem);
        elem.data(DataElement, {
            placeholder     : placeholder,
            containers      : newContainers,
            leftlimits      : null,
            toplimits       : null,
            rightlimits     : null,
            bottomlimmits   : null
        });
        if (options.Limits) {
            setupLimits(elem, options.Limits);
        }
        elemContainer.scroll();
    },

    methods = {
        init: function (options) {

            var result = null;
            /* Default values for the supported options */
            var settings = $.extend({
                /*
                || Containers (array of jquery elements) in which the element is to be constrained.
                */
                Containers          : null,
                /*
                || Limiting elements (array of jquery elements) that restrict the placement of the element.
                */
                Limits              : null,
                /*
                || Class applied to Placeholder element. This element will replace the specified element to keep
                || the documentflow of the html-elements unchanged.
                */
                PlaceholderClass    : 'fixedpanel-placeholder',
                /*
                || Class applied to the element when it's position is fixed.
                */
                FixedClass          : 'fixedpanel-fixed',
                /*
                || Class applied to the element when it's position is unfixed (absolute position).
                */
                UnfixedClass        : 'fixedpanel-unfixed',
                /*
                || Callback to be called when the element's position is fixed.
                */
                FixedCallback       : null, /* function (elem) {} */
                /*
                || Callback to be called when the element's position is unfixed.
                */
                UnfixedCallback     : null  /* function (elem) {} */
            }, options);

            /* Set default container, if non specified */
            if (!settings.Containers || !(settings.Containers instanceof jQuery) || settings.Containers.length === 0) {
                settings.Containers = $('body');
            }
            if (!settings.Limits || !(settings.Limits instanceof jQuery) || settings.Limits.length === 0) {
                settings.Limits = null;
            }

            /* Handle all elements supplied an return them again to support chaining */
            result = this.each(function () {
                setupElement($(this), settings);
                setupResize();
            });
            $('body').scroll();
            return result;
        }
    };

    $.fn.FixedPanel = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        }
        if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        }
        $.error('Method ' +  method + ' does not exist on jQuery.FixedPanel');
    };
}(jQuery));
