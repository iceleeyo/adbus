<#import "../template/template_intro.ftl" as frame > <#global
menu="移动电视"> <@frame.html title="移动电视"
js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","../css/compare/auction.css","../css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<DIV class="pgIn-container  ">

	<DIV class="pgIn-container-content">
		<DIV id="pg-guide-one">
			<DIV class="containerIn_12 mtIn10">
				<DIV class="gridIn_12">
					<DIV class="pg-guide-content">
						<DIV class="pg-guide-title">
							<H1 class="h3"></H1>
						</DIV>
						<DIV class="pg-guide-info">
							<DIV class="pg-guide-item">
								<H4 class="h4bg-bule">
									<SPAN>视频广告介绍</SPAN>
								</H4>
								<DIV class="fn-clear">
									<DIV class="gridIn_6 alphaIn">
										<DL class="p20 pl40">
											<DT>关于北京公交移动电视简介</DT>

											<DD>我们有520条公交线路，超过12000辆公交车，</DD>
											<DD>大约24000个终端，每天超过17小时不间断地播出</DD>
											<DD>公交移动电视终端（尺寸：15寸、19寸）</DD>
											<DD>涵盖长安街、CBD、中关村等数百个优质商圈和社区。</DD>
											<DD>锁定重点校区和火车站等交通枢纽。</DD>
											<DD>拥有北京70%以上资源份额。</DD>
											<DD>北京全市常住人口2114.8万人。</DD>
											<DD>68%的城市长住居民采用公交车出行。</DD>
											<DD>全天候到达率高达1300万人次。</DD>
											<DD>每时点受众均超过56.07万人次。</DD>

										</DL>
									</DIV>
									<DIV class="gridIn_6 omega text-center">
										<IMG alt="" src="homepage/tem/video.png">
									</DIV>

								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</DIV>

</DIV>
</@frame.html>
