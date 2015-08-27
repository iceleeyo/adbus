<#import "template/template_blank.ftl" as frame>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路列表" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/lines.js"] 
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    .title {
  		border-bottom: 1px solid rgb(86, 170, 242);
  		width: 1010px;
	}
    .title span {
  		background: rgb(86, 170, 242);
  		color: rgb(255, 255, 255);
  		text-align: center;
  		width: 80px;
  		height: 30px;
  		line-height: 30px;
  		display: inline-block;
	}
	#table_wrapper{
	width: 1010px;
	}
</style>
<script type="text/javascript">

$(document).ready(function() {
var i=0;
 <@security.authorize ifAnyGranted="bodysales,bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                    i++;
   </@security.authorize>
        initTable('${rc.contextPath}',i);
    } );
</script>
            <div class="title s-clear" style="width: 1060px;margin-left: -30px;">
            
            
			  	<span>
			  		线路规划
			  	</span>
			  </div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">线路名</th>
                        <th orderBy="level">线路级别</th>
                         <th orderBy="_cars">自营车辆</th>
                           <th orderBy="_persons">人车流量</th>
                         <th>查看站点</th>
    <@security.authorize ifAnyGranted="BodyOrderManager">
                        <th>管理</th>
    </@security.authorize>
                    </tr>
                    </thead>

                </table>
                <input type="hidden" id = "address" value="">
                <input type="hidden" id = "siteLine" value="">
                
                
                
</div>



 
</@frame.html>