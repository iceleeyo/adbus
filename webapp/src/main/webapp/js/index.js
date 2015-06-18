
			   //选中左侧菜单父选项，子选项展开
			   $(".pg-side-item-t").click(function(){
			   	  var firsticon=$(this);
			      $(this).next().toggle();
				});
			   //选中我的时
			   $(".aa-icon").click(function(){
			   	    
			   		$(".pg-side-item-t").removeAttr("style");
			   		$(".side-exp-item-t").removeAttr("style");
			   		$(".pg-side-exp-list").hide();
			   		$(this).css({'color': '#fff', 'background': '#2da7e0'});

			   		$(".pg-icon-a").removeAttr("style");
			   		$(this).children().css({'background':'url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px 0'});
			   });
			   //选中左侧菜单子选项，父选项变色
			   $(".side-exp-item-t").click(function(){
			   	  $(".side-exp-item-t").removeAttr("style")
			   	  $(this).css({'color': '#fff', 'background': '#7fd4f3'});
			   	  var parent=$(this).parents('.pg-side-exp-list').prev(); //parent:pg-side-item-t
			   	  $(".pg-side-exp-list").hide();  //选中子选项时，父选项的兄弟选项的子选项隐藏
			   	  parent.next().show();  //选中子选项时，其兄弟子选项显示
			   	  $(".pg-side-item-t").removeAttr("style");
			   	  parent.css({'background-color': '#2da7e0', 'color': '#fff'});
			   	  $('.pg-icon-a').removeAttr("style");
			   	  //点击子选项时，只有该子选项的父选项改变状态
			   	  if($(this).hasClass("bb-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -20px');
			   	  else if(parent.hasClass("cc-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -40px');
			   	  else if(parent.hasClass("dd-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -60px');
			   	  else if(parent.hasClass("ee-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -80px');
			   	  else if(parent.hasClass("ff-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -100px');
			   	  else if(parent.hasClass("gg-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -120px');
			   	  else if(parent.hasClass("hh-icon"))
			   	  	parent.children().css('background','url(../imgs/sidebar-icons.png?v=1548a) no-repeat -20px -40px');
			   });
			   //鼠标移入移出事件
			   $(".pg-nav-hover").mouseover(function(){
				    $(".pg-nav-hover").next().show();
				  });
			   $(".pg-nav-hover").mouseout(function(){
				    $(".pg-nav-hover").next().hide();
				  });
			   $(".pg-dropdown").mouseover(function(){
			    $(this).show();
			   });
			   $(".pg-dropdown").mouseout(function(){
			    $(this).hide();
			   });

			   $(".pg-nav-hover-us").mouseover(function(){
				    $(".pg-nav-hover-us").next().show();
			    });
			   $(".pg-nav-hover-us").mouseout(function(){
				    $(".pg-nav-hover-us").next().hide();
			   });
			   $(".pg-dropdown-us").mouseover(function(){
			    $(this).show();
			   });
			   $(".pg-dropdown-us").mouseout(function(){
			    $(this).hide();
			   });

			   $(".pg-nav-item-p").mouseover(function(){
				    $(".pg-nav-dropdown").show();
				    $(this).addClass("pg-nav-hover");
				  });
			   $(".pg-nav-item-p").mouseout(function(){
				    $(".pg-nav-dropdown").hide();
				    $(this).removeClass("pg-nav-hover");
				  });
			   $(".pg-nav-dropdown").mouseover(function(){
			        $(this).show();
			        $(".pg-nav-item-p").addClass("pg-nav-hover");
			   });
			   $(".pg-nav-dropdown").mouseout(function(){
			        $(this).hide();
			        $(".pg-nav-item-p").removeClass("pg-nav-hover");
			   });