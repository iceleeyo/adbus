/**
 * Platform JS
 * Created by Zhang Jie
 * 2014/10/9
 *
*/

var advPlatform = {
	//调整页面尺寸
	resize: function(){
		var winWidth = $(window).width(),
			winHeight = $(window).height(),
			headHeight = $('#Platform-Header').height(),
			copyHeight = $('#Copyright').height();
		$('#Platform-Body').height(winHeight-headHeight);
		$('#Platform-Left').height(winHeight-headHeight);
		$('#Platform-Workspace').height(winHeight-headHeight);
		$('#Workspace').height(winHeight-headHeight-copyHeight);
	},
	//左侧菜单初始化
	initMenu: function(){
		$('#Platform-Left').jScroll();
		var _this = this;
		$('#TopBar-Quit').click(function(){
			_this.Quit();
		});
		$('.Main-Menu').click(function(){
			$(this).parent().removeClass('Active').addClass('Active').siblings().removeClass('Active');
		});
		$('.Sub-Menu a').click(function(){
			$(this).parent().removeClass('Active').addClass('Active').siblings().removeClass('Active');
		});
		$('.Sub-Menu').each(function(){
			$(this).find('li:first').addClass('first');
		});
		//$('.Sub-Menu li:first').addClass('first');
		$('.Main-Menu .Expand').click(function(){
			var _this = this;
			var $submenu = $(this).parent().next('.Sub-Menu');
			//$submenu.find('li:first').addClass('first');
			if($submenu.length > 0){
				if($submenu.is(':hidden')){
					$submenu.slideDown(300);
				}else{
					$submenu.slideUp(300, function(){
						$submenu.find('li').removeClass('Active');
					});
				}
			}
		});
	},

	Quit: function(){
		jDialog.Confirm('确实要退出本系统吗？', function(result){
			if(result) window.parent.location.href = "logout.action";
		});
	},
	init: function(){
		var _this = this;
		_this.resize();
		_this.initMenu();
		//_this.initAjaxLoad();
		$(window).resize(function(){
			_this.resize();
		});
	}
};

$.ajaxSetup({
	cache: false,
	contentType: 'application/x-www-form-urlencoded;charset=utf-8'
});

/* mousewheel For jQuery 1.2.2+ */
;(function (factory) {
	if ( typeof define === 'function' && define.amd ) {
		define(['jquery'], factory);
	} else if (typeof exports === 'object') {
		module.exports = factory;
	} else {
		factory(jQuery);
	}
}(function ($) {
	var toFix = ['wheel', 'mousewheel', 'DOMMouseScroll', 'MozMousePixelScroll'];
	var toBind = 'onwheel' in document || document.documentMode >= 9 ? ['wheel'] : ['mousewheel', 'DomMouseScroll', 'MozMousePixelScroll'];
	var lowestDelta, lowestDeltaXY;

	if ( $.event.fixHooks ) {
		for ( var i = toFix.length; i; ) {
			$.event.fixHooks[ toFix[--i] ] = $.event.mouseHooks;
		}
	}
	$.event.special.mousewheel = {
		setup: function() {
			if ( this.addEventListener ) {
				for ( var i = toBind.length; i; ) {
					this.addEventListener( toBind[--i], handler, false );
				}
			} else {
				this.onmousewheel = handler;
			}
		},
		teardown: function() {
			if ( this.removeEventListener ) {
				for ( var i = toBind.length; i; ) {
					this.removeEventListener( toBind[--i], handler, false );
				}
			} else {
				this.onmousewheel = null;
			}
		}
	};
	$.fn.extend({
		mousewheel: function(fn) {
			return fn ? this.bind("mousewheel", fn) : this.trigger("mousewheel");
		},
		unmousewheel: function(fn) {
			return this.unbind("mousewheel", fn);
		}
	});
	function handler(event) {
		var orgEvent = event || window.event,
			args = [].slice.call(arguments, 1),
			delta = 0,
			deltaX = 0,
			deltaY = 0,
			absDelta = 0,
			absDeltaXY = 0,
			fn;
		event = $.event.fix(orgEvent);
		event.type = "mousewheel";
		if ( orgEvent.wheelDelta ) { delta = orgEvent.wheelDelta; }
		if ( orgEvent.detail )	 { delta = orgEvent.detail * -1; }
		if ( orgEvent.deltaY ) {
			deltaY = orgEvent.deltaY * -1;
			delta  = deltaY;
		}
		if ( orgEvent.deltaX ) {
			deltaX = orgEvent.deltaX;
			delta  = deltaX * -1;
		}
		if ( orgEvent.wheelDeltaY !== undefined ) { deltaY = orgEvent.wheelDeltaY; }
		if ( orgEvent.wheelDeltaX !== undefined ) { deltaX = orgEvent.wheelDeltaX * -1; }
		absDelta = Math.abs(delta);
		if ( !lowestDelta || absDelta < lowestDelta ) { lowestDelta = absDelta; }
		absDeltaXY = Math.max(Math.abs(deltaY), Math.abs(deltaX));
		if ( !lowestDeltaXY || absDeltaXY < lowestDeltaXY ) { lowestDeltaXY = absDeltaXY; }
		fn = delta > 0 ? 'floor' : 'ceil';
		delta  = Math[fn](delta / lowestDelta);
		deltaX = Math[fn](deltaX / lowestDeltaXY);
		deltaY = Math[fn](deltaY / lowestDeltaXY);
		args.unshift(event, delta, deltaX, deltaY);
		return ($.event.dispatch || $.event.handle).apply(this, args);
	}
}));

/* jScroll For jQuery 1.8 & jQuery mousewheel */
var i = 0;
;(function($){
	var jScrollTmr = [];
	$(window).resize(function(){
		$('.jScrollTrack').hide();
		$('.jScrollBar').hide();
	});
	$.jScroll_scroll = function(obj, isshow){
		var tmrid = $(obj).data('tmrid');
		clearTimeout(jScrollTmr[tmrid]);
		var pos = $(obj).position();
		pos.left += isNaN(parseInt($(obj).css('margin-left'))) ? 0 : parseInt($(obj).css('margin-left'));
		pos.left += isNaN(parseInt($(obj).css('border-left-width'))) ? 0 : parseInt($(obj).css('border-left-width'));
		pos.left += $(obj).innerWidth() - $(obj).nextAll('div.jScrollTrack').eq(0).outerWidth() - 1;
		pos.top += isNaN(parseInt($(obj).css('margin-top'))) ? 0 : parseInt($(obj).css('margin-top'));
		pos.top += isNaN(parseInt($(obj).css('border-top-width'))) ? 0 : parseInt($(obj).css('border-top-width'));
		var wh = $(obj).innerHeight();
		var sh = obj.scrollHeight;
		var barh = (wh >= sh) ? wh : parseInt(wh*wh/sh);
		var dc = sh - wh;
		var bartop;
		if(dc <= 0){
			bartop = 0;
			$(obj).nextAll('.jScrollTrack').eq(0).hide();
			$(obj).nextAll('.jScrollBar').eq(0).hide();
		}else{
			isshow = (typeof(isshow) == 'undefined') ? true : isshow;
			var bardc = wh - barh;
			var st = obj.scrollTop;
			bartop = pos.top+parseInt(st*bardc/dc);
			if(isshow) $(obj).nextAll('.jScrollTrack').eq(0).show();
			if(isshow) $(obj).nextAll('.jScrollBar').eq(0).show();
			$(obj).nextAll('div.jScrollTrack').eq(0).css({height:wh,top:pos.top,left:pos.left});
			$(obj).nextAll('div.jScrollBar').eq(0).css({height:barh,top:bartop,left:pos.left});
			if($(obj).data('hide')){
				jScrollTmr[tmrid] = setTimeout(function(){
					$(obj).nextAll('.jScrollTrack').eq(0).hide();
					$(obj).nextAll('.jScrollBar').eq(0).hide();
				}, 2000);
			}
		}
	};
	$.jScroll_move = function(obj){
		$(obj).data('hide', false);
		var $track = $(obj).nextAll('.jScrollTrack').eq(0);
		var $bar = $(obj).nextAll('.jScrollBar').eq(0);
		var trackpos = $track.position();
		var barpos = $bar.position();
		var dc = $track.outerHeight() - $bar.outerHeight();
		var sdc = barpos.top - trackpos.top;
		var scrolltop = parseInt((obj.scrollHeight-$(obj).innerHeight())*sdc/dc);
		obj.scrollTop = scrolltop;
	};
	$.fn.extend({
		jScroll: function(options){
			var defaults = {
				step : 50,
			};
			var opts = $.extend(defaults, options);
			return this.each(function(){
				var _this = this;
				jScrollTmr.push(null);
				var key = jScrollTmr.length - 1;
				$(this).data('tmrid', key);
				$(this).data('hide', true);
				$(this).data('height', this.scrollHeight);
				$(this).data('chkchange', setInterval(function(){
					if($(_this).data('height') !== _this.scrollHeight){
						$(_this).data('height', _this.scrollHeight);
						$.jScroll_scroll(_this, false);
					}
				}, 100));
				$(this).css('overflow', 'hidden');
				$('<div class="jScrollTrack"></div><div class="jScrollBar"></div>').insertAfter($(this));
				$(this).nextAll('.jScrollBar').eq(0).mouseover(function(){
					$(_this).data('hide', false);
					clearTimeout(jScrollTmr[key]);
				});
				$(this).nextAll('.jScrollBar').eq(0).mouseout(function(){
					jScrollTmr[key] = setTimeout(function(){
						$(_this).nextAll('.jScrollTrack').eq(0).hide();
						$(_this).nextAll('.jScrollBar').eq(0).hide();
					}, 2000);
				});
				$.jScroll_scroll(this, false);
				$(this).scroll(function(){
					$.jScroll_scroll(this);
				});
				$(this).mousewheel(function(event, delta, deltaX, deltaY){
					if(delta == -1){
						this.scrollTop += opts.step;
					}else if(delta == 1){
						this.scrollTop -= opts.step;
					}
					return false;
				});
			});
		}
	});
})(jQuery);

jQuery.getMousePosition = function(e){
	var posx = 0;
	var posy = 0;
	if(!e) var e = window.event;
	if(e.pageX || e.pageY){
		posx = e.pageX;
		posy = e.pageY;
	}else if(e.clientX || e.clientY){
		posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop  + document.documentElement.scrollTop;
	}
	return {'x':posx, 'y':posy};
};

/**
 * 对话框
 * 
 */
var jDialog = {
	Show: function(options){
		var defaults = {
			title: '提示',
			content: '',
			width: 600,
			height: 0,
			submit: false,
			submitlabel: '提交',
			cancellabel: '关闭',
			onbegin: null,
			onend: null
		};
		var opts = $.extend(defaults, options);
		if(typeof opts.onbegin == 'function')
			opts.begin();
		if(!document.getElementById('jDialog')){
			$('body').append('<div id="jDialog_Overlay" style="z-index:9998"></div><div id="jDialog" style="z-index:9999"></div>');
		}else{
			$('#jDialog_Overlay').show();
			$('#jDialog').show();
		}
		var code = '<div class="jDialog_Title">' + opts.title + '</div>' +
			'<div id="jDialog_Content" style="width:' + opts.width + 'px">'+opts.content+'</div>';
		var btncode = '<div class="jDialog_Buttons">';
		if(opts.submit) btncode += '<button type="button" class="jDialog_Submit">' + opts.submitlabel + '</button>';
		btncode += '<button type="button" class="jDialog_Close">' + opts.cancellabel + '</button></div>';
		$('#jDialog').html(code);
		if($('#jDialog form').length > 0){
			$('#jDialog form').eq(0).append(btncode);
		}else{
			$('#jDialog').append(btncode);
		}
		if(opts.height == 0){
			//调整高度
		}else{
			$('#jDialog_Content').height(opts.height);
		}
		this.setPosition();
		if(typeof opts.onend == 'function')
			opts.onend();
	},
	Hide: function(){
		$('#jDialog').html('').hide();
		$('#jDialog_Overlay').hide();
	},
	Load: function(title, url){
		this.Show({title:title, content:'<div class="Page-Loading"></div>'});
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'html',
			success: function(data){
				$('#jDialog_Content').html(data);
			},
			error: function(){
				$('#jDialog_Content').html('载入页面失败');
			}
		});
	},
	LoadForm: function(title, url){
		this.Show({title:title, content:'<div class="Page-Loading"></div>', submit:true});
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'html',
			success: function(data){
				$('#jDialog_Content').html(data);
			},
			error: function(){
				$('#jDialog_Content').html('载入页面失败');
			}
		});
	},
	setPosition: function(){
		var winWidth = $(window).width(),
			winHeight = $(window).height(),
			sclTop = $(document).scrollTop(),
			width = $('#jDialog').outerWidth(),
			height = $('#jDialog').outerHeight();
		var top = (winHeight - height) / 2;
		if(top < 0) top = 0;
		top += sclTop;
		var left = (winWidth - width) / 2;
		if(left < 0) left = 0;
		$('#jDialog').css({top:top, left:left});
	},
	Alert: function(msg, callback){
		if(typeof callback == 'function'){
			this.Show({title:'系统提示', content:msg, onend:callback});
		}else{
			this.Show({title:'系统提示', content:msg});
		}
	},
	
	SAlert: function(msg, callback){
		if(typeof callback == 'function'){
			this.Show({title:'物料列表', content:msg, onend:callback});
		}else{
			this.Show({title:'物料列表', content:msg});
		}
	},
	Confirm: function(msg, callback){
		this.Show({content:msg, width:400, submit:true, submitlabel: '确定', cancellabel: '取消'});
		$('button.jDialog_Submit').click(function(){
			callback(true);
			jDialog.Hide();
		});
	}
};
$(document).off('click', 'button.jDialog_Close').on('click', 'button.jDialog_Close', function(){
    jDialog.Hide();
});
/*$('button.jDialog_Close').die().live('click', function(){
	jDialog.Hide();
});*/
$(document).keydown(function(event){
	if(event.keyCode == 27){
		if(document.getElementById('jDialog')){
			jDialog.Hide();
		}
	}
});

/* AjaxForm */
;(function($){
	$.fn.extend({
		AjaxForm: function(options){
			var defaults = {
				onsubmit: function(data){}
			};
			var opts = $.extend(defaults, options);
			function sendForm(obj){
				var url = $(obj).attr('action'),
					method = $(obj).attr('method'),
					dataType = $(obj).attr('dataType');
				if(typeof url != 'undefined'){
					if(typeof method == 'undefined') method = 'GET';
					method = method.toUpperCase();
					if(method != 'POST') method = 'GET';
					if(typeof dataType == 'undefined') dataType = 'html';
					var senddata = {};
					$(obj).find(':input').each(function(){
						var iptname = $(this).attr('name');
						var iptval;
						if(iptname != ''){
							if($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio'){
								iptval = $(this).prop('checked') ? $(this).val() : null;
							}else{
								iptval = $(this).val();
							}
						}
						if(typeof senddata[iptname] == 'undefined'){
							if(iptval != null) senddata[iptname] = iptval;
						}else{
							if(iptval != null) senddata[iptname] += ','+iptval;
						}
					});
					$.ajax({
						type: method,
						url: url,
						dataType: dataType,
						data: senddata,
						success: function(data){
							if(typeof opts.onsubmit == 'function'){
								opts.onsubmit(data);
							}
						},
						error: function(){
							jDialog.Alert('处理请求失败');
						}
					});
				}
			}
			return this.each(function(){
				var _this = this;
				if($(this).find('button[type=submit]').length > 0){
					$(this).find('button[type=submit]').click(function(event){
						event.preventDefault();
						sendForm(_this);
					});
				}else{
					sendForm(_this);
				}
			});
		}
	});
})(jQuery);

$(document).ready(function(){
	advPlatform.init();
});