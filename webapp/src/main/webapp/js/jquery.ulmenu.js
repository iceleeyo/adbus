;(function($){
	$.fn.extend({
		ulMenu: function(options){
			var defaults = {
				iptID	: null,			//生成hidden控件的id和name
				valAttr	: null,			//li的属性取值
				defVal	: '',			//默认选择值
				fixEdge	: [0,2,0,0],	//修复边缘尺寸，默认下拉菜单宽度与框相同
				onSel	: null			//选择下拉后执行的方法
			};
			var opts = $.extend(defaults, options);
			function showMenu(obj, arrData){
				var selIndex = $(obj).data('index');
				var code = '<div id="ulMenuList"><ul>';
				for(var i=0; i<arrData.length; i++){
					if(i == selIndex)
						code += '<li val="'+arrData[i][1]+'" class="ulMenuSelected">'+arrData[i][0]+'</li>';
					else
						code += '<li val="'+arrData[i][1]+'">'+arrData[i][0]+'</li>';
				}
				code += '</ul></div>';
				$('body').append(code);
				$('#ulMenuList li').each(function(i){
					$(this).click(function(event){
						event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
						$(obj).data('value',$(this).attr('val')).data('index',i).find('span').html(arrData[i][0]);
						if($(obj).prev().length > 0){
							if($(obj).prev().get(0).tagName == 'INPUT')
								$(obj).prev().val($(this).attr('val'));
						}
						hideMenu();
						if(typeof opts.onSel == 'function') opts.onSel(obj);
					});
				});
				var pos = $(obj).offset();
				$('#ulMenuList').css({
					width: ($(obj).outerWidth()-opts.fixEdge[1]-opts.fixEdge[3]),
					top: (pos.top+$(obj).height()+opts.fixEdge[0]),
					left: (pos.left+opts.fixEdge[3])
				});
			}
			function hideMenu(){
				$('#ulMenuList').remove();
			}
			return this.each(function(){
				var _this = this;
				var arrValue = [];
				$(this).children('li').each(function(){
					var val = '';
					if(typeof opts.valAttr == 'string'){
						val = $(this).attr(opts.valAttr);
						val = typeof val == 'string' ? val : '';
					}
					arrValue.push([$(this).text(), val]);
				});
				var selIndex = null,
					selVal = '';
				for(var i=0; i<arrValue.length; i++){
					if(arrValue[i][1] == opts.defVal){
						selIndex = i;
						selVal = opts.defVal;
					}
				}
				if(selIndex == null && arrValue.length > 0){
					selIndex = 0;
					selVal = arrValue[0][1];
				}
				if(opts.iptID) $(this).before('<input id="'+opts.iptID+'" type="hidden" name="'+opts.iptID+'" value="'+selVal+'" />');
				$(this).before('<div class="ulMenu"><div class="ulMenuCaption"><span>'+arrValue[selIndex][0]+'</span></div></div>');
				$(this).prev().data('value',selVal).data('index',selIndex).click(function(event){
					event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
					if(document.getElementById('ulMenuList')){
						hideMenu();
					}else{
						showMenu(this, arrValue);
					}
				});
				$(document).click(function(){
					hideMenu();
				});
				$(this).remove();
			});
		}
	});
})(jQuery);