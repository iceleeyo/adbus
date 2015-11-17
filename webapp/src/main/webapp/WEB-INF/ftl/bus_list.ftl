<#import "template/template.ftl" as frame> <#global menu="车辆查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆查询"
js=["js/sift.js","js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["css/sift.css","css/account.css","js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/basecss.css">

<style type="text/css">

 .ui-widget-content {
   // width: auto !important;
}
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "aaSorting": [[1, "des"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,5,6,7, 8,9,10,11,12,13,14,15,16,17,18] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[oldserinum]" : $('#oldserinum').val(),
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[linename]" : $('#linename').val(),
                       // "filter[category]" : $('#category').val(),
                        //"filter[levelStr]" : $('#levelStr').val(),
                        "filter[sh]" : $('#sh').val(),
                        "filter[tags]" : $('#tags').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
              { "data": "jpaBus.company.name", "defaultContent": ""},
               { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
              
                { "data": "jpaBus.plateNumber", "defaultContent": ""},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                  { "data": "jpaBus.description", "defaultContent": ""},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.offlinecontract.adcontent", "defaultContent": ""},
                 { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                    { "data": "busInfo.busOnline.realEndDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                <#--
                { "data": "jpaBus.categoryStr", "defaultContent": "", "render": function(data) {
                  if(data=="机动车"){
                     return '<span class="invalid">'+data+'</span>';
                  }else{
                     return data;
                  }
                } },
              
                { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
             
                     { "data": "jpaBus.office", "defaultContent": ""},  -->
                            { "data": "busInfo._adtype", "defaultContent": ""},
                              { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
                          { "data": "busInfo.busOnline.days", "defaultContent": ""},
                       { "data": "busInfo._sktype", "defaultContent": ""},
                { "data": "jpaBus.enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } }, { "data": "busInfo.offlinecontract.company", "defaultContent": ""},
                  { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
            <#--     { "data": "jpaBus.branch", "defaultContent": ""},-->
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>车辆自编号</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>旧自编号</span>' +
                        '    <span>' +
                        '        <input id="oldserinum" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;<br><br>' +
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="name" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路</span>' +
                        '    <span>' +
                        '    <input id="tags" placeholder="多选输入框。" style="width:350px;">' +
                        '    </span>&nbsp;&nbsp;' +
                  	'<span><a class="block-btn" id="export_xls" href="javascript:void(0);">导出查询数据</a>'+
                 <!--   '<a class="block-btn" style="margin-left: 20px;" href="javascript:void(0);">导出所有</a></span>'+-->
                  	'</div>'
        );
function split( val ) {
      return val.split( /,\s*/ );
    }
    $( "#tags" )
      // don't navigate away from the field on tab when selecting an item
      .bind( "keydown", function( event ) {
        if ( event.keyCode === $.ui.keyCode.TAB &&
            $( this ).autocomplete( "instance" ).menu.active ) {
          event.preventDefault();
        }
      })
      .autocomplete({
        minLength: 0,
        source : "${rc.contextPath}/busselect/autoComplete?tag=b",
        focus: function() {
          // prevent value inserted on focus
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
        
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push( ui.item.value );
          // add placeholder to get the comma-and-space at the end
          terms.push( "" );
           this.value = terms.join( ", " );
          table.fnDraw();
          return false;
        }
      }).focus(function () {
       				 $(this).autocomplete("search");
   	 	});;
        $('#serinum,#oldserinum,#name,#tags').change(function() {
            table.fnDraw();
        });
         $('#linename').change(function() {
            ishaveline($("#linename").val());
            table.fnDraw();
        });
         $("#linename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				table.fnDraw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
        $("#export_xls").click(function(){
          var params =  "filter[plateNumber]=" + $('#name').val()
                        +"&filter[linename]=" + $('#linename').val()
                      //  +"&filter[category]=" + $('#category').val()
                       // +"&filter[levelStr]=" + $('#levelStr').val();
                     //   alert(params);
                        var w = ''+ $('#name').val()+$('#linename').val() ;
                        if($('#name').val() =='' && $('#linename').val() ==''){
                         	jDialog.Alert("导出车辆信息,由于数据量较大执行较慢 正在优化开发随后几天将开放!请先缩小查询范围再导出");
                         	return ;
                        }
                        
            location.href='${rc.contextPath}/bus/ajax-list.xls?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1&'+params;
        });
    }
function ishaveline(linename){
     $.ajax({
				url :  "${rc.contextPath}/busselect/ishaveline/"+linename,
				type : "POST",
				success : function(data) {
				if(!data.left){
				    layer.msg(data.right);
				  }
				 }
			}, "text");
    }
    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
			
    $(document).ready(function() {
    $("#sh").val("stats_1");
        initTable();
         initSwift(table)
    } );
     $(function() {
    
  });
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">车辆查询</div>
	<input type="hidden" id="sh" value="" />
	<div class="container-12 mt10 s-clear" style="width: 100%;">
		<div class="sift-box">
			<div class="sift-item s-clear">
				<span>线路级别：</span>
				<div class="sift-list" qt="lev">
					<a class="item active" href="#" sort="-1" qc="all">所有</a> 
					<a class="item" href="#" qc="S">特级<i>×</i></a> 
					<a class="item" href="#" qc="APP">A++<i>×</i></a> 
					<a class="item" href="#" qc="AP">A+<i>×</i></a> 
					<a class="item" href="#" qc="A">A<i>×</i></a>
					<a class="item" href="#" qc="LATLONG">其他<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>营销中心：</span>
				<div class="sift-list" qt="com">
					<a class="item active" href="#" sort="-1" qc="all">所有</a> 
					<a class="item" href="#" qc="10">自营<i>×</i></a> 
					<a class="item" href="#" qc="13">CBS<i>×</i></a> 
					<a class="item" href="#" qc="12">白马<i>×</i></a>
					<a class="item" href="#" qc="8">七彩<i>×</i></a>
					<a class="item" href="#" qc="15">市场<i>×</i></a>
					<a class="item" href="#" qc="11">其他<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>车辆类型：</span>
				<div class="sift-list" qt="gor">
					<a class="item active" href="#" sort="-1" qc="all">所有</a> 
					<a class="item" href="#" qc="baoche">包车<i>×</i></a> 
					<a class="item" href="#" qc="banche">班车<i>×</i></a> 
					<a class="item" href="#" qc="jidongche">机动车<i>×</i></a>
					<a class="item" href="#" qc="yunyingche">运营车<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>公司名称：</span>
				<div class="sift-list" qt="company">
					<a class="item active" href="#" sort="-1" qc="all">所有</a>
					<a class="item" href="#" qc="1">大公共公司<i>×</i></a> 
					<a class="item" href="#" qc="2"> 八方达公司<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>车辆状态：</span>
				<div class="sift-list" qt="stats">
					<a class="item " href="#" sort="-1" qc="all">所有</a> 
					<a class="item active" href="#" qc="1">正常车辆<i>×</i></a>
					<a class="item" href="#" qc="2"> 回收站车辆<i>×</i></a>
				</div>
			</div>

		</div>
	</div>
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
			<th>营销中心</th>
			<th orderBy="line.id">线路名称</th>
			<th orderBy="line.level">线路级别</th>
				<th orderBy="serialNumber">车辆自编号</th>
				<th orderBy="plateNumber">车牌号</th>
				<th>车辆型号</th>
				<th>车辆描述</th>
				<th>合同编号</th>
				<th>广告内容</th>
				<th>实际上刊日期</th>
				<th>预计下刊日期</th>
				<th>实际下刊日期</th>
				
				<!--
				<th>类别</th>
				<th>公司名称</th>
				<th>所属分公司</th>
				-->
				<th>广告类型</th>
				<th>车身广告状态</th>
				<th>刊期</th>
				
				<th>上刊类型</th>
				<th orderBy="enabled">车辆情况</th>
					<th>客户名称</th>
				<th>老车辆自编号</th>
				
			</tr>
		</thead>
	</table>
</div>
</@frame.html>
