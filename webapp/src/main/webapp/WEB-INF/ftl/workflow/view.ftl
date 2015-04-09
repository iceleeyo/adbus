<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>流程图跟踪</title>
        <link rel="stylesheet" href="${rc.contextPath}/style/blueprint/screen.css" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="${rc.contextPath}/style/blueprint/print.css" type="text/css" media="print"> 
        <link href="${rc.contextPath}/style/style.css" type="text/css" rel="stylesheet">
		<link href="${rc.contextPath}/js/qtip/jquery.qtip.min.css" type="text/css" rel="stylesheet">
		<script src="${rc.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${rc.contextPath}/js/qtip/jquery.qtip.pack.js"></script>
		<script type="text/javascript" src="${rc.contextPath}/js/html/jquery.outerhtml.js"></script>
        <script type="text/javascript">
        	 
        	$(function(){
        		//流程节点详细信息
            	var traceUrl="${rc.contextPath}/workflow/process/${processInstanceId}/trace";
            	$.getJSON(traceUrl,function(infos){
            		 
            		var positionHtml = "";
                    
                    // 生成图片
                    var varsArray = new Array();
                    $.each(infos, function(i, v) {
                        var $positionDiv = $('<div/>', {
                            'class': 'activiyAttr'
                        }).css({
                            position: 'absolute',
                            left: (v.x - 1),
                            top: (v.y - 1),
                            width: (v.width - 2),
                            height: (v.height - 2)
                        });
                        if (v.currentActiviti) {
                            $positionDiv.addClass('ui-corner-all-12').css({
                                border: '2px solid red'
                            });
                        }
                        positionHtml += $positionDiv.outerHTML();
                        varsArray[varsArray.length] = v.vars;
                    });
                    
                    //流程图地址
                    var image="<img alt='跟踪工作流' src='${rc.contextPath}/workflow/view/${processInstanceId}' style='position:absolute; left:0px; top:0px;'>";
                     
                    //追加到body
                    $("body").append(image).append(positionHtml);
                    
                    
                    // 设置每个节点的data
                    $('.activiyAttr').each(function(i, v) {
                        $(this).data('vars', varsArray[i]);
                    });
            		
                 	// 此处用于显示每个节点的信息，如果不需要可以删除
                    $('.activiyAttr').qtip({
                        content: function() {
                            var vars = $(this).data('vars');
                            var tipContent = "<table class='need-border'>";
                            $.each(vars, function(varKey, varValue) {
                                if (varValue) {
                                    tipContent += "<tr><td class='label'>" + varKey + "</td><td>" + varValue + "<td/></tr>";
                                }
                            });
                            tipContent += "</table>";
                            return tipContent;
                        },
                        position: {
                            at: 'bottom left',
                            adjust: {
                                x: 3
                            }
                        }
                    });
                    // end qtip
            	});
        	});
        </script>
    </head>
    <body>
    </body>
</html>