(function($){
    var defaults = {
        'container' : '#content',//容器
        'sections' : 'section',//子容器
        'easing' : 'ease',//特效方式，ease-in,ease-out,linear
        'duration' : 1000,//每次动画执行的时间
        'pagination' : false,//是否显示分页
        'loop' : false,//是否循环
        'keyboard' : true,//是否支持键盘
        'direction' : 'vertical',//滑动的方向 horizontal,vertical,
        'onpageSwitch' : function(pagenum){}
    };

    var win = $(window),
        container,sections;

    var opts = {},
        canScroll = true;

    var iIndex = 0;

    var arrElement = [];

    var sectionHeight = win.height(),
        sectionWidth = win.width();

    var SP = $.fn.switchPage = function(options){
        opts = $.extend({}, defaults , options||{});

        container = $(opts.container),
        sections = container.find(opts.sections);

        sections.each(function(){
            arrElement.push($(this));
            $(this).css('height', sectionHeight);
        });

        return this.each(function(){
            if(opts.direction == "horizontal"){
            initLayout();
            }

            if(opts.pagination){
                initPagination();
            }

            if(opts.keyboard){
                keyDown();
            }
        });
    }

    //滚轮向上滑动事件
    SP.moveSectionUp = function(){
        if(iIndex > 0){
            iIndex--;
        } else if(iIndex == 0){
            iIndex = 0;
        }else if(opts.loop){
            iIndex = arrElement.length-1;
        }
        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    };

    //滚轮向下滑动事件
    SP.moveSectionDown = function(){
        if(iIndex < (arrElement.length-1)){
            iIndex++;
        }else if(iIndex == (arrElement.length)){
            iIndex = arrElement.length; 
        }else if(opts.loop){
            iIndex = 0;
        }
        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    };

    //私有方法
    //页面滚动事件
    function scrollPage(element){
        var dest = element.position();
        if(typeof dest === 'undefined'){ return; }
        initEffects(dest,element);
    }

    //重写鼠标滑动事件
    $(document).on("mousewheel DOMMouseScroll", MouseWheelHandler);
    function MouseWheelHandler(e) {
        e.preventDefault();
        var value = e.originalEvent.wheelDelta || -e.originalEvent.detail;
        var delta = Math.max(-1, Math.min(1, value));
        if(canScroll){
            if (delta < 0) {
                SP.moveSectionDown();
            }else {
                SP.moveSectionUp();
            }
        }
        return false;
    }

    //横向布局初始化
    function initLayout(){
        var length = sections.length,
            width = (length*100)+"%",
            cellWidth = (100/length).toFixed(2)+"%";
        container.width(width).addClass("left");
        sections.width(cellWidth).addClass("left");
    }

    //初始化分页
    function initPagination(){
        var length = sections.length;
        if(length){

        }
        var pageHtml = '<ul id="pages"><li class="active"></li>';
        for(var i=1;i<length;i++){
            pageHtml += '<li></li>';
        }
        pageHtml += '</ul>';
        $("body").append(pageHtml);
    }

    //分页事件
    function paginationHandler(){
        var pages = $("#pages li");
        pages.eq(iIndex).addClass("active").siblings().removeClass("active");
    }

    //是否支持css的某个属性
    function isSuportCss(property){
        var body = $("body")[0];
        for(var i=0; i<property.length;i++){
            if(property[i] in body.style){
                return true;
            }
        }
        return false;
    }

    //渲染效果
    function initEffects(dest,element){
        var transform = ["-webkit-transform","-ms-transform","-moz-transform","transform"],
            transition = ["-webkit-transition","-ms-transition","-moz-transition","transition"];

        canScroll = false;
        if(isSuportCss(transform) && isSuportCss(transition)){
            var traslate = "";
            if(opts.direction == "horizontal"){
                traslate = "-"+dest.left+"px, 0px, 0px";
            }else{
                traslate = "0px, -"+dest.top+"px, 0px";
            }
            container.css({
                "transition":"all "+opts.duration+"ms "+opts.easing,
                "transform":"translate3d("+traslate+")"
            });
            container.on("webkitTransitionEnd msTransitionend mozTransitionend transitionend",function(){
                canScroll = true;
            });
        }else{
            var cssObj = (opts.direction == "horizontal")?{left: -dest.left}:{top: -dest.top};
            container.animate(cssObj, opts.duration, function(){
                canScroll = true;
            });
        }
        element.addClass("active").siblings().removeClass("active");
        if(opts.pagination){
            paginationHandler();
        }
    }

    //窗口Resize
    var resizeId;
    win.resize(function(){
        clearTimeout(resizeId);
        resizeId = setTimeout(function(){
            reBuild();
        },500);
    });

    function reBuild(){
        var currentHeight = win.height(),
            currentWidth = win.width();

        var element = arrElement[iIndex];
        if(opts.direction == "horizontal"){
            var offsetLeft = element.offset().left;
            if(Math.abs(offsetLeft)>currentWidth/2 && iIndex <(arrElement.length-1)){
                iIndex ++;
            }
        }else{
            var offsetTop = element.offset().top;
            if(Math.abs(offsetTop)>currentHeight/2 && iIndex <(arrElement.length-1)){
                iIndex ++;
            }
        }
        if(iIndex){
            paginationHandler();
            var cuerrentElement = arrElement[iIndex],
                dest = cuerrentElement.position();
            initEffects(dest,cuerrentElement);
        }
    }

    //绑定键盘事件
    function keyDown(){
        var keydownId;
        win.keydown(function(e){
            clearTimeout(keydownId);
            keydownId = setTimeout(function(){
                var keyCode = e.keyCode;
                if(keyCode == 37||keyCode == 38){
                    SP.moveSectionUp();
                }else if(keyCode == 39||keyCode == 40){
                    SP.moveSectionDown();
                }
            },150);
        });
    }


    $('.menu ul.list-line li').on('click', function(e) {
        e.preventDefault();
        var numn;
        numn = $(this).index();
        iIndex = numn;
        $(this).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    });

    $('.jack .icons .up').on('click', function(e) {
        e.preventDefault();
        if(iIndex > 0){
            iIndex--;
        } else if(iIndex == 0){
            iIndex = 0;
        }else if(opts.loop){
            iIndex = arrElement.length-1;
        }
        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    });

    $('.jack .icons .down').on('click', function(e) {
        e.preventDefault();
        if(iIndex < (arrElement.length-1)){
            iIndex++;
        }else if(iIndex == (arrElement.length)){
            iIndex = arrElement.length; 
        }else if(opts.loop){
            iIndex = 0;
        }
        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    });

     $('.cdown').on('click', function(e) {
        iIndex = 1;
        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
        scrollPage(arrElement[iIndex]);
    });

    //首页图片滑动+尾页滑动
    $.fn.slidePic = function(_height, _width){
        _this = $(this);
        var sliders = $(this).find('.slider');
        var pic_number,pic_number1, pic_number2;
        var i = 1,
            q = 1;

        pic_number = sliders.length;
        pic_number1 = $('.background-one .slider').length;
        console.log(pic_number1);

        var sh = setInterval(slideTimeSet, 5000); //图片计时循环
        function slideTimeSet() {
            var topval;
            
            var iNextTransD = "-"+iNextD+"px, 0px, 0px";
            $('.background-one .slide-back').removeAttr('style').css({"width":  sectionWidth * pic_number1,
                                  "height": sectionHeight, 
                                  "transform": "translate3d("+iNextTransD+")", 
                                  "-webkit-transform": "translate3d("+iNextTransD+")", 
                                  "transition-duration": "0.6s", 
                                  "-webkit-transition-duration": "0.6s"});
            $('.navi a').eq(q).addClass('active').siblings('.active').removeClass('active');
            
            $('.background-six ul.menu li').eq(i).addClass('active').siblings('.active').removeClass('active');
            //计数器
            /*if(i < pic_number - 1) {
                i ++;
            } else if(i == pic_number - 1) {
                i = 0;    
            }*/
            if(q < pic_number1 - 1) {
                q ++;
            } else if(q == pic_number1 - 1) {
                q = 0;    
            }
        }
        $('.navi a').hover(
            function() {
                clearInterval(sh);
                q = $(this).index();
                slideTimeSet();
            },
            function() {
                sh = setInterval(slideTimeSet, 5000);
            }
        );       
        $('.background-six ul.menu li').on('click', function(e) {
            e.preventDefault();
            i = $(this).index();
            var iNext = _width * i;

            var iNextTrans = "-"+iNext+"px, 0px, 0px";
            _this.find('.slide-back').removeAttr('style').css({"width": _width * pic_number,
                                  "height": _height, 
                                  "transform": "translate3d("+iNextTrans+")", 
                                  "-webkit-transform": "translate3d("+iNextTrans+")", 
                                  "transition-duration": "0.6s", 
                                  "-webkit-transition-duration": "0.6s"});
            topval = i * 42;
            _this.find('.menu .sl.small').removeClass('style').css({"height": "22px", "top": topval});
        }
            
        );       
    }; 
})(jQuery);
