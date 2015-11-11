<#import "../template/template_intro.ftl" as frame > <#global
menu="业务指南"> <@frame.html title="业务指南"
js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />


<DIV class="pg-container-content">
	<DIV id="pg-guide-one">
		<DIV class="container_12 mt10">
			<DIV class="grid_12">
				<DIV class="pg-guide-content">
					<DIV class="pg-guide-title">
						<H1 class="h3"></H1>
					</DIV>
					<DIV class="pg-guide-item">
						<H4 class="h4bg-bule">
							<SPAN>北京公交移动电视简介</SPAN>
						</H4>
						<DIV class="fn-clear">
							<DIV class="gridIn_6 alphaIn">
								<P class="pIn20 plIn40">
									<SPAN class="guide-first-word">北</SPAN>京公交移动电视覆盖北京520条公交线路，12000辆公交车，24000个终端，每天超过17小时不间断地播出。
									公交移动电视终端的尺寸有15寸、19寸两种。通过广电无线传输，整个北京市同步播出。涵盖长安街、CBD、中关村等数百个优质商圈和社区。
									锁定重点校区和火车站等交通枢纽。 拥有北京70%以上资源份额。 北京全市常住人口2114.8万人。
									68%的城市长住居民采用公交车出行。 全天候到达率高达1300万人次。 每时点受众均超过56.07万人次。
								</P>
							</DIV>
							<DIV class="grid_6 omega text-center" style="margin-left: -2px;">
								<IMG alt="" src="homepage/tem/abc.png">
							</DIV>
						</DIV>
					</DIV>

					<DIV class="pg-guide-item dark">
						<H4 class="h4bg-orange">
							<SPAN>为什么选择公交移动电视</SPAN>
						</H4>
						<DIV class="fn-clear">
							<DIV class="grid_6 omega text-center">
								<IMG alt="" src="homepage/tem/aaa.png">
							</DIV>

							<DIV class="grid_6 alpha">
								<P class="pIn20 plIn40">
									<SPAN class="guide-first-word">移</SPAN>动电视处于大众人群前往各目的地过程中的一级媒介交叉点上。具有超高性价及全效传播的特性
									。对于出行人群，我们具有不可替代、不可复制的传播力。
								</P>

								<P class="pIn20 plIn40">
									<SPAN class="guide-first-word">品</SPAN>牌信息全面覆盖并拦截出行途中的目标消费群体，用丰富生动的节目与多种多样的广告结合形式，引起目标消费群体的关注，从而勾起他们潜在的购买欲望，在途中激发他们的即时购买冲动，并直接引导到终端进行消费，让广告投资获取更高的回报。
								</P>
							</DIV>
						</DIV>
					</DIV>


					<DIV class="pg-guide-item">
						<H4 class="h4bg-green">
							<SPAN>主要优势在哪里</SPAN>
						</H4>

						<DIV class="fn-clear">
							<DIV class="grid_11 alpha">
								<DL class="pIn20 plIn40">
									<DT>有效填补传统电视的日间收视空白</DT>
									<DD>19:00-22:00是传统电视的收视黄金时段，但随着工作和生活习惯的改变，城市现代人在这段时间里的已不再局限于待在家中观看电视，而更多的是加班、聚餐、相约唱K或逛街购物等户外活动，这也令传统电视广告的到达效果大打折扣。</DD>
									<DT>日间收视比本地电视频道更具优势</DT>
									<DD>以北京为例，与北京本地收视最好的影视频道及收视平均的生活频道对比，日间两个高峰的收视具有明显优势，且日间高峰收视率高于生活频道的夜间黄金时段。</DD>
								</DL>
							</DIV>
							<DIV class="grid_6 omega text-center"></DIV>
							<DIV class="fn-clear p35">
								<IMG alt="" src="homepage/tem/1.png">
							</DIV>
							<SCRIPT>
											//seajs.use(['components/components'], function(Components) {
											//  new Components.Footer().init();
											//});
										</SCRIPT>

						</DIV>
					</DIV>

					<DIV class="pg-guide-item dark">
						<H4 class="h4bg-yellow">
							<SPAN>覆盖哪些线路</SPAN>
						</H4>
						<DIV class="fn-clear">
							<DIV class="grid_11 omega text-center">
								<IMG alt="" src="homepage/tem/line.png">
							</DIV>
						</DIV>
					</DIV>

					<DIV class="pg-guide-item ">
						<H4 class="h4bg-cyanblue">
							<SPAN>成功的案例有哪些</SPAN>
						</H4>
						<DIV class="fn-clear">
							<DIV class="grid_11 omega text-center">
								<IMG alt="" src="homepage/tem/exp.png">
							</DIV>
						</DIV>
					</DIV>



				</DIV>
			</DIV>
		</DIV>
	</DIV>
</DIV>
</@frame.html>
