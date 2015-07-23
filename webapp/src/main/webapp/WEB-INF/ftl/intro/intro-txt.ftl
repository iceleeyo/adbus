<#import "../template/template_intro.ftl" as frame >
<#global menu="车身媒体">
<@frame.html title="车身媒体" js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] 
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<DIV class="pg-container" style="margin-top: 10px;padding-bottom: 20px;">

	
<SCRIPT>
//seajs.use(['components/components'], function(Components) {
//  new Components.Header().init();
//});
</SCRIPT>
     
<DIV class="pg-container-content" style="">
	<DIV id="pg-guide-one">
		<DIV class="container_12 mt10">
			<DIV class="grid_12">
				<DIV class="pg-guide-content">
					<DIV class="pg-guide-title">
						<H1 class="h3"></H1>
					</DIV>
					<DIV class="pg-guide-info">
<DIV class="pg-guide-item">
<H4 class="h4bg-green"><SPAN>info图片广告介绍</SPAN></H4>
<DIV class="fn-clear">
<DIV class="gridIn_6 alpha text-center"><IMG alt="人人贷理财12-14%的高收益" src="homepage/tem/info.png"> 
                </DIV>                 
<DIV class="gridIn_6 omegaIn">
<DL class="p20 pr40">
<DT>INFO字幕：</DT>
  <DD>每条最多50个全角字符，每天每条播出50次以上。</DD>
  <DT>INFO图片：</DT>
  <DD>画面播放20秒为一个单位，每天滚动播出90次以上。</DD></DL></DIV>               </DIV>
      </DIV>
				</DIV>
			</DIV>   
		</DIV>
	</DIV>
</DIV>
<SCRIPT>
//seajs.use(['components/components'], function(Components) {
//  new Components.Footer().init();
//});
</SCRIPT>
     
</DIV>
</DIV>
</@frame.html>
