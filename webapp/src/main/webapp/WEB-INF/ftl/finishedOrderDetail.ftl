<#import "template/template.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <@frame.html title="订单详细"
js=["js/highslide/highslide-full.js", "js/video-js/video.js",
"js/video-js/lang/zh-CN.js"] css=["js/highslide/highslide.css",
"js/video-js/video-js.css"]> <#include "template/preview.ftl" />
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
<div id="process" class="section4">

	<div class="node fore ready">
		<ul>
			<li class="tx1">&nbsp;</li>
			<li class="tx2">提交订单</li>
			<li id="track_time_0" class="tx3"></li>
		</ul>
	</div>
	<div class="proce ready">
		<ul>
			<li class="tx1">&nbsp;</li>
		</ul>
	</div>
	<div class="node ready">
		<ul>
			<li class="tx1">&nbsp;</li>
			<li class="tx2">支付</li>
			<li id="track_time_4" class="tx3"></li>
		</ul>
	</div>
	<div class="proce ready">
		<ul>
			<li class="tx1">&nbsp;</li>
		</ul>
	</div>
	<div class="node ready">
		<ul>
			<li class="tx1">&nbsp;</li>
			<li class="tx2">审核</li>
			<li id="track_time_1" class="tx3"></li>
		</ul>
	</div>
	<div class="proce ready">
		<ul>
			<li class="tx1">&nbsp;</li>
		</ul>
	</div>
	<div class="node ready">
		<ul>
			<li class="tx1">&nbsp;</li>
			<li class="tx2">广告播出</li>
			<li id="track_time_5" class="tx3"></li>
		</ul>
	</div>
	<div class="proce ready">
		<ul>
			<li class="tx1">&nbsp;</li>
		</ul>
	</div>
	<div class="node ready">
		<ul>
			<li class="tx1">&nbsp;</li>
			<li class="tx2">播出完成</li>
			<li id="track_time_6" class="tx3"></li>
		</ul>
	</div>
</div>
<@orderDetail.orderDetail orderview=orderview quafiles=quafiles
suppliesView=suppliesView/> <#include "template/hisDetail.ftl" />
</@frame.html>






