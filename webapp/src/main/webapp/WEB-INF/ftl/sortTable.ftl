<#import "template/template_blank.ftl" as frame> <#assign
security=JspTaglibs["/WEB-INF/tlds/security.tld"] /> <@frame.html
title="线路列表"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/lines.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<style type="text/css">
.center {
	margin: auto;
}

.frame {
	width: 1000px;
}

.div {
	text-align: center;
	margin: 25px;
}

div#toolbar {
	float: left;
}

.processed {
	color: limegreen;
}

.invalid {
	color: red;
}

.hl {
	background-color: #ffff00;
}

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

#table_wrapper {
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
        
        
        var fixHelperModified = function(e, tr) {
    var $originals = tr.children();
    var $helper = tr.clone();
    $helper.children().each(function(index) {
        $(this).width($originals.eq(index).width())
    });
    return $helper;
},
    updateIndex = function(e, ui) {
        $('td.sorting_1', ui.item.parent()).each(function (i) {
           // $(this).html(i + 1);
        });
         var _temp='';
          $('#table tbody tr').each(function(){
			_temp+=($(this).attr("id")) +",";
        });
        alert("取到排序值:已按id排序" +_temp);
    };
$("#table tbody").sortable({
    helper: fixHelperModified,
    stop: updateIndex
}).disableSelection();
    } );
    
</script>
<div class="title s-clear">

	<table id="sort" class="display compact" cellspacing="0" width="100%"
		title="Kurt Vonnegut novels">
		<thead>
			<tr>
				<th class="index">No.</th>
				<th>Year</th>
				<th>Title</th>
				<th>Grade</th>
			</tr>
		</thead>
		<tbody>
			<tr id="1969">
				<td class="index">1</td>
				<td>1969</td>
				<td>Slaughterhouse-Five</td>
				<td>A+</td>
			</tr>
			<tr id="1952">
				<td class="index">2</td>
				<td>1952</td>
				<td>Player Piano</td>
				<td>B</td>
			</tr>
			<tr id="1963">
				<td class="index">3</td>
				<td>1963</td>
				<td>Cat's Cradle</td>
				<td>A+</td>
			</tr>
			<tr id="1973">
				<td class="index">4</td>
				<td>1973</td>
				<td>Breakfast of Champions</td>
				<td>C</td>
			</tr>
			<tr id="1965">
				<td class="index">5</td>
				<td>1965</td>
				<td>God Bless You, Mr. Rosewater</td>
				<td>A</td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript">
var fixHelperModified = function(e, tr) {
    var $originals = tr.children();
    var $helper = tr.clone();
    $helper.children().each(function(index) {
        $(this).width($originals.eq(index).width())
    });
    return $helper;
},
    updateIndex = function(e, ui) {
        $('td.index', ui.item.parent()).each(function (i) {
            $(this).html(i + 1);
        });
        var _temp='';
          $('#sort tbody tr').each(function(){
			_temp+=($(this).attr("id")) +",";
        });
        alert("取到排序值:已按id排序" +_temp);
    };
$("#sort tbody").sortable({
    helper: fixHelperModified,
    stop: updateIndex
}).disableSelection();

</script>

	<span> 线路规划 </span>
</div>
<table id="table" class="display compact" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th orderBy="name">线路名</th>
			<th orderBy="level">线路级别</th>
			<th orderBy="_cars">自营车辆</th>
			<th orderBy="_persons">人车流量</th>
			<th>查看站点</th> <@security.authorize ifAnyGranted="BodyOrderManager">
			<th>管理</th> </@security.authorize>
		</tr>
	</thead>

</table>
<input type="hidden" id="address" value="">
<input type="hidden" id="siteLine" value="">



</div>




</@frame.html>
