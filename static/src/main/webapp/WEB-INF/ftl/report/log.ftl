<#import "../template/template.ftl" as frame> <#global menu="系统更新日志">
<@frame.html title="系统更新日志">

<style type="text/css">
ul,li{list-style:none}
a{text-decoration:none; color:#f40;}
a:hover { color:#f60;}
.cls{zoom:1}
.cls:after{content:".";display:block;height:0;clear:both;visibility:hidden}
.link:hover{text-decoration:underline}
hr{height:0;border-left:none;border-right:0;border-top:1px dashed #2d2f34;border-bottom:1px dashed #474954}
.wrapper{width:960px;margin:0 auto}
.rz_content{padding:50px 0;}
.rz_content .wrapper{position:relative;}
.rz_content .line-left{position:absolute;left:0;top:15px;width:70px}
.rz_content .line-right{position:absolute;right:0;top:15px;width:310px}
.rz_content .main{background:url(../images/line-bg.png) repeat-y 249px 0}
.rz_content .main .title{position:absolute;line-height:40px;padding-left:67px;left:230px;top:0;color:#58a6fb;font-size:24px;background:url(../images/clock.png) no-repeat left top}
.rz_content .main .year{position:relative;z-index:100}
.rz_content .main .year h2{height:40px;width:170px;padding-right:30px;font-size:24px;line-height:40px;text-align:right}
.rz_content .main .year h2 a{color:#58a6fb}
.rz_content .main .year h2 i{display:block;position:relative;height:0;width:0;left:190px;top:-20px;border-width:6px;border-style:solid;border-color:#59a7fb transparent transparent transparent;-webkit-transition:.5s;-moz-transition:.5s;-ms-transition:.5s;-o-transition:.5s;transition:.5s;-webkit-transform-origin:6px 3px;-moz-transform-origin:6px 3px;-ms-transform-origin:6px 3px;-o-transform-origin:6px 3px;transform-origin:6px 3px}
.rz_content .main .year .list{margin:10px 0;position:relative;overflow:hidden;-webkit-transition:height 1s cubic-bezier(0.025,0.025,0.000,1.115),opacity 1s;-moz-transition:height 1s cubic-bezier(0.025,0.025,0.000,1.115),opacity 1s;-ms-transition:height 1s cubic-bezier(0.025,0.025,0.000,1.115),opacity 1s;-o-transition:height 1s cubic-bezier(0.025,0.025,0.000,1.115),opacity 1s;transition:height 1s cubic-bezier(0.025,0.025,0.000,1.115),opacity 1s}
.rz_content .main .year .list ul{bottom:0}
.rz_content .main .year .list ul li{background:url(../images/circle.png) no-repeat 235px 31px;padding:30px 0;color:#a1a4b8}
.rz_content .main .year .list ul li .date,.rz_content .main .year .list ul li .version{float:left;display:block;clear:left;width:200px;line-height:24px;text-align:right}
.rz_content .main .year .list ul li .date{font-size:18px;line-height:32px;color:#bec1d5}
.rz_content .main .year .list ul li .intro,.rz_content .main .year .list ul li .more{float:left;display:block;width:400px;margin-left:100px;line-height:24px}
.rz_content .main .year .list ul li .intro{font-size:18px;line-height:32px;color:#63d029}
.rz_content .main .year .list ul li.highlight{background-image:url(../images/circle-h.png)}
.rz_content .main .year .list ul li.highlight .date,.rz_content .main .year .list ul li.highlight .intro{color:#ec6a13}
.rz_content .wrapper:first-child .main .year.close h2 i{transform:rotate(-90deg);-webkit-transform:rotate(-90deg);-moz-transform:rotate(-90deg);-ms-transform:rotate(-90deg);-o-transform:rotate(-90deg)}
.rz_content .wrapper:first-child .main .year.close .list{opacity:0;height:0!important}
</style>


<div class="rz_content">
	<div class="wrapper">
		<hr class="line-left">
		<hr class="line-right">
		<div class="main">
			<h1 class="title">世巴媒体管理平台-系统更新日志</h1>
            	<div class="year">
				<h2><a href="#">2016年<i></i></a></h2>
				<div class="list">
					<ul>
					<li class="cls highlight">
							<p class="date">2016-09-27</p>
							<p class="intro">功能修改</p>
							<p class="version">视频1.1</p>
							<div class="more">
                              <p>1.多个订单关联到一个合同，支付及支付确认跟合同关联，先下订单，再创建合同</p>
                              <p>2.优化产品定义时的参数输入问题</p>
							  <p>3.调整合同的格式，增加备注信息到合同里</p>
							  <p>4.一些前端问题的优化及调整，数据输入项的限制等</p>
							  <p><font color='#ec6a13'>5.修改了待办事项等一些表格项的顺序及排序问题，方便操作</font></p>
							</div>
				    </li>
				    
				    
					<li class="cls ">
							<p class="date">2016-08-08</p>
							<p class="intro">功能修改</p>
							<p class="version">视频1.0</p>
							<div class="more">
                              <p>1.订单的分期设置及分期支付</p>
                              <p>2.销售员直接替客户下单，不需要创建广告主用户</p>
                              <p><font color='#ec6a13'>3.一些前端问题的优化及调整</font></p>
							</div>
				    </li>
				    
				    
					
					
						 
				   </ul>
                </div>
            </div>
			 
			 
			</div>
		</div>
	</div>
</div>


</@frame.html>
