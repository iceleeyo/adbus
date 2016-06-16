<#import "../template/template_intro.ftl" as frame > <#global
menu="广告刊例"> <@frame.html title="广告刊例"
js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<DIV class="pg-container  ">


	<SCRIPT>
//seajs.use(['components/components'], function(Components) {
//  new Components.Header().init();
//});
</SCRIPT>

	<DIV class="pg-container-content">
		<DIV id="pg-guide-one">
			<DIV class="container_12 mt10">
				<DIV class="grid_12">
					<DIV class="pg-guide-content">
						<DIV class="pg-guide-title">
							<H1 class="h3"></H1>
						</DIV>
						<DIV class="pg-guide-item">
							<H4 class="h4bg-yellow">
								<SPAN>广告刊例</SPAN>
							</H4>
							<DIV class="fn-clear">
								<DIV class="grid_6 alpha text-center performance">
									<DIV class="pl100">
										<IMG alt="广告刊例" src="homepage/tem/advert_example.png">
									</DIV>
								</DIV>
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
