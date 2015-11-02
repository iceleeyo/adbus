<#import "template/template.ftl" as frame>
<#global menu="车辆查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆查询" js=["js/sift.js","js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["css/sift.css","css/account.css","js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
            "columnDefs": [
                { "orderable": false, "targets": [7, 8,9,10] },
            ],
            "iDisplayLength" : 50,
            "aLengthMenu": [[50, 100], [50, 100]],
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
                        "filter[sh]" : $('#sh').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.plateNumber", "defaultContent": ""},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.categoryStr", "defaultContent": "", "render": function(data) {
                  if(data=="机动车"){
                     return '<span class="invalid">'+data+'</span>';
                  }else{
                     return data;
                  }
                } },
                { "data": "jpaBus.company.name", "defaultContent": ""},
                { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
                { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                  { "data": "busInfo.offlinecontract.adcontent", "defaultContent": ""},
                 { "data": "jpaBus.description", "defaultContent": ""},
                     { "data": "jpaBus.office", "defaultContent": ""},
                      { "data": "jpaBus.branch", "defaultContent": ""},
                { "data": "jpaBus.enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
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
                        '        <input id="serinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>旧自编号</span>' +
                        '    <span>' +
                        '        <input id="oldserinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路</span>' +
                        '    <span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                  	'<span style="float:right;"><a class="block-btn" id="export_xls" href="javascript:void(0);">导出查询数据</a>'+
                 <!--   '<a class="block-btn" style="margin-left: 20px;" href="javascript:void(0);">导出所有</a></span>'+-->
                  	'</div>'
        );

        $('#serinum,#oldserinum,#name,#linename').change(function() {
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
                        +"&filter[category]=" + $('#category').val()
                        +"&filter[levelStr]=" + $('#levelStr').val();
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
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">车辆查询</div>
             <input type="hidden" id="sh" value=""/>
              <div class="container-12 mt10 s-clear">
						<div class="sift-box">
						   <div class="sift-item s-clear">
								<span>线路级别：</span>
								<div class="sift-list" qt="lev">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#"  qc="S" >特级<i>×</i></a>
									<a class="item" href="#"  qc="APP" >A++<i>×</i></a>
									<a class="item" href="#"  qc="AP" >A+<i>×</i></a>
									<a class="item" href="#"  qc="A" >A<i>×</i></a>
								</div>
							</div>
						   <div class="sift-item s-clear">
								<span>营销中心：</span>
								<div class="sift-list" qt="com">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#"  qc="2" >自营<i>×</i></a>
									<a class="item" href="#"  qc="4" >CBS<i>×</i></a>
									<a class="item" href="#"  qc="3" >白马<i>×</i></a>
									<a class="item" href="#"  qc="1" >七彩<i>×</i></a>
								</div>
							</div>
							 <div class="sift-item s-clear">
								<span>车辆类型：</span>
								<div class="sift-list" qt="gor">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#"  qc="baoche" >包车<i>×</i></a>
									<a class="item" href="#"  qc="banche" >班车<i>×</i></a>
									<a class="item" href="#"  qc="jidongche" >机动车<i>×</i></a>
									<a class="item" href="#"  qc="yunyingche" >运营车<i>×</i></a>
								</div>
							</div>
							 <div class="sift-item s-clear">
								<span>公司名称：</span>
								<div class="sift-list" qt="company">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#"  qc="1" >大公共公司<i>×</i></a>
									<a class="item" href="#"  qc="2"> 八方达公司<i>×</i></a>
								</div>
							</div>
							 <div class="sift-item s-clear">
								<span>车辆状态：</span>
								<div class="sift-list" qt="stats">
									<a class="item " href="#" sort="-1" qc="all">所有</a>
									<a class="item active" href="#"  qc="1" >正常车辆<i >×</i></a>
									<a class="item" href="#"  qc="2"> 回收站车辆<i>×</i></a>
								</div>
							</div>
									
			</div>					
	</div>					
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车辆自编号</th>
                        <th>旧自编号</th>
                        <th >车牌号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                         <th>是否有广告</th>
                        <th>合同编号</th>
                        <th>实际上刊日期</th>
                        <th>预计下刊日期</th>
                         <th>广告内容</th>
                        <th>车辆描述</th>
                        <th>公司名称</th>
                        <th>所属分公司</th>
                        <th orderBy="enabled">状态</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>