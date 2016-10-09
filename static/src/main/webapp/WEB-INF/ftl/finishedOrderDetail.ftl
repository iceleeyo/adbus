<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "template/template.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <#import
"template/pickBuses.ftl" as pickBuses> <@frame.html title="订单办理"
js=["js/nano.js","js/highslide/highslide-full.js",
"js/video-js/video.js", "js/video-js/lang/zh-CN.js",
"js/jquery-ui/jquery-ui.min.js","index_js/sift_common.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/jquery-dateFormat.js"
,"js/ajax-pushlet-client.js"
]
css=["js/highslide/highslide.css",
"js/video-js/video-js.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<script type="text/javascript">
function go_back(){
	history.go(-1);
}

function confirmSchedule() {
      var orderid = '${orderview.order.id}';
      var startdate1= $("#generateSchedule #startdate1").val();
        var bm=$('#generateSchedule :radio[name=bm]:checked').val();
      if(startdate1==""){
         layer.msg("请填写日期", {icon: 5});
          return;
      }
		 	layer.confirm("撤销日期为" + startdate1 + ",确定撤销排期吗？", {
		    icon:3
		}, function(index) {
		    layer.close(index);
		    if (true) {
		        layer.load(1);
		        setTimeout(function() {
		            layer.closeAll("loading");
		        }, 60000 * 10);
		        $.ajax({
		            url:"${rc.contextPath}/schedule/canelOrderWithStartDay",
		            type:"POST",
		            data:{
		                "startdate":startdate1,
		                "orderid":orderid,
		                "canelAfterAll":bm
		            },
		            success:function(data) {
		                layer.closeAll("loading");
		                if(data.left){
		                  layer.msg(data.right+"<br>3秒后自动刷新页面!",{icon: 6});
		                  
		                   setTimeout(function() {
		            				location.reload();
		       				 }, 1000 * 3);
		                } else {
		                  layer.msg(data.right, {icon: 5});
		                }
		            }
		        }, "text");
		    }
		});
	}
	
</script>

<@orderDetail.orderDetail orderview=orderview quafiles=quafiles
suppliesView=suppliesView/> 
<div id="generateSchedule" class="generateSchedule" >
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="">排期调整</A>
			</p>
		</H3>
		<br>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<input type="hidden" id="ischeckInventory" value="0" />
				
				<TR>
					<TH>订单排期日期</TH>
					<TD colspan=3>
                    <#setting
					date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''} - <#setting
					date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''} 						
					</TD>
				</TR>
				<TR>
					<TH>撤销日期</TH>
					<TD colspan=3>
                    <input class="ui-input datepicker validate[required,custom[date],past[#upDate1]]" type="text"  value="" id="startdate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> 						
					</TD>
				</TR>
				<TR>
					<TH>撤销选项</TH>
					<TD colspan=3>
                    <input type="radio" name="bm" value="N" checked=checked/>撤销当天
					<input name="bm" type="radio" value="Y" />撤销当天以后所有排期				
					</TD>
				</TR>
				<TR>
					<TH>操作</TH>
					<TD colspan=3>
						  <button id="sureButton" onclick="confirmSchedule();" class="block-btn" >确定撤销</button>
					</TD>
				</TR>
				
				
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<span id="cc1"> </span>
		</div>
	</div>
</div>


<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
			"dom" : '<"#toolbar2">t',
            "searching": false,
            
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/ajax-queryChangeLog",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[orderId]" : $('#orderId').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                 { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "userId", "defaultContent": ""
                      },
                { "data": "startDate", "defaultContent": "",
                    },
                      { "data": "isCallAfterDayAll", "defaultContent": "",
                    },
                
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }

    $(document).ready(function() {
        initTable();
    } );
</script>

<div id="generateSchedule" class="generateSchedule" >
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="">调整历史</A>
			</p>
		</H3>
		<input type="hidden" id ="orderId" value ="${orderview.order.id}">
		<#--<iframe style="width:100%;height:200px" frameborder="no" src="/schedule/changeLog?orderid=${orderview.order.id}"/>'-->
		<table id="table" class="display compact" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>操作时间</th>
				<th>操作用户</th>
				<th>撤销日期</th>
				<th>撤销当天以后所有排期</th>
			</tr>
		</thead>

	</table>
		 
	</div>
</div>


<#include "template/hisDetail.ftl" />
</@frame.html>






