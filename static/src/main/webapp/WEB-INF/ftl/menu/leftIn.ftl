<style type="text/css">
.side-exp-select {
	cursor: pointer;
	color: #fff;
	background: #7fd4f3;
}

.side-exp-p-select .pg-side-item-t {
	color: #fff !important;
	background: #2da7e0;
}
</style>
<div class="ls-2">
	<div class="pg-side">
		<ul class="pg-side-list">
			<li class="pg-side-item"><a class="pg-side-item-t aa-icon"
				href="${rc.contextPath}/intro-video"> <i
					class="s-left pg-icon-a a-icon"></i> 移动电视
			</a></li>

			<li class="pg-side-item"><a class="pg-side-item-t bb-icon"
				href="${rc.contextPath}/intro-txt"> <i
					class="s-left pg-icon-a b-icon"></i> 字幕图片
			</a></li>

			<li class="pg-side-item"><a class="pg-side-item-t ee-icon"
				href="${rc.contextPath}/intro-price"> <i
					class="s-left pg-icon-a g-icon"></i> 广告刊例
			</a></li>

			<li class="pg-side-item"><a class="pg-side-item-t cc-icon"
				href="${rc.contextPath}/intro-ywzn"> <i
					class="s-left pg-icon-a f-icon"></i> 业务指南
			</a></li>

			<li class="pg-side-item"><a class="pg-side-item-t dd-icon"
				href="${rc.contextPath}/about-me"> <i
					class="s-left pg-icon-a d-icon"></i> 关于我们
			</a></li>
			<li class="pg-side-item"><a class="pg-side-item-t dd-icon"
				href="${rc.contextPath}/intro-notice"> <i
					class="s-left pg-icon-a d-icon"></i> 网站公告
			</a></li>
		</ul>
	</div>
</div>
<script type="text/javascript">
	function active(menu) {
		$(".pg-side-item").each(function() {
			if (menu) {
				var curr = $(this);
				var st = 0;

				var list = $(this).find(".side-exp-item-t");
				list.each(function() {
					if ($(this).text().trim() == menu) {
						curr.find(".pg-side-exp-list").show();
						$(this).addClass("side-exp-select");
						curr.addClass("side-exp-p-select");
						st = 1;
					}
				});

				//try to find from top level
				if (st == 0 && list.length == 0) {
					var top = $(this).find(".pg-side-item-t");
					if (top.text().trim() == menu) {
						curr.addClass("side-exp-p-select");
						st = 1;
					}
				}
				if (st == 0) {
					curr.find(".pg-side-exp-list").hide();
				}

			}

			var active = $(this).find(".side-exp-item-t.active");
			if (!active[0]) {
				// $(this).find(".pg-side-exp-list").hide();
			} else {
				//  $(this).find(".pg-side-exp-list").show();
			}

		});
	}

	$(document)
			.ready(
					function() {
						//move single sub-menu to top
						$(".pg-side-item")
								.each(
										function() {
											var list = $(this)
													.find(
															".pg-side-exp-list .side-exp-item-t");
											if (list.length == 1) {
												var top = $(this).find(
														".pg-side-item-t");
												if (top) {
													top.attr("href", $(list[0])
															.attr("href"));
													top.find(".pg-icon-a")[0].nextSibling.data = $(
															list[0]).text();
													$(this)
															.find(
																	".pg-side-exp-list")
															.remove();
												}
											}
										});

						//open selected menu
						var menu = '<#if menu??>${menu}<#else></#if>';
						$(".pg-side-item-t").click(
								function() {
									$(this).parent(".pg-side-item").find(
											".pg-side-exp-list").toggle();
								});

						active(menu);
					});
</script>