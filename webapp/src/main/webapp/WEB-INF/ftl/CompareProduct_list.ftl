<#import "template/template.ftl" as frame>
<#global menu="竞价产品列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="竞价产品列表" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"] >

<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,4,5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/product/compareProduct-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[protype]" : $('#protype').val(),
                        "filter[prostate]" : $('#prostate').val(),
                        <@security.authorize ifAnyGranted="ShibaOrderManager">
                        "filter[ischangeorder]" : $('#ischangeorder').val()
                        </@security.authorize>
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "product.name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return '<a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer(\'${rc.contextPath}/product/ajaxdetail/\','+row.product.id+');"  >'+row.product.name+'</a>';
                } },
                { "data": "product.type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '视频';
                        if (data == 'image')
                            return '图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'Info';
                        return '';
                    } },
                { "data": "saleprice", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                { "data": "comparePrice", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                 { "data": "pv", "defaultContent": "" },
                { "data": "setcount", "defaultContent": "" },
                { "data": "biddingDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                } },
                   <@security.authorize ifAnyGranted="advertiser">
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations = '';
                      operations+= '<a class="table-link" href="${rc.contextPath}/product/to_comparePage/'+data+'">竞价</a>';
                       return operations;
                    }},
                     	</@security.authorize>
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
                     '<select class="ui-input ui-input-mini" name="protype" id="protype" style="width:140px">' +
                    '<option value="" selected="selected">产品类型</option>' +
                  	'<option value="video">视屏广告</option>' +
                  	'<option value="image">图片广告</option>' +
                    '<option value="info">字幕广告</option>' +
         			'</select>' +
                         '    <span></span>' +
                    '<select class="ui-input ui-input-mini" name="prostate" id="prostate" > ' +
                    '<option value="" selected="selected">竞价状态</option>' +
                      <@security.authorize ifAnyGranted="ShibaOrderManager">
                  		'<option value="wait">未竞价</option>' +
                  		</@security.authorize>
                  	'<option value="ing">竞价中</option>' +
                    '<option value="over">竞价结束</option>' +
         			'</select>' +
         			 '    <span id="c"></span>' +
                        '</div>'
        );
        
        <@security.authorize ifAnyGranted="advertiser">
        $('#protype,#prostate,#ischangeorder').change(function() {
            table.fnDraw();
        });
        
        </@security.authorize>
		<@security.authorize ifAnyGranted="ShibaOrderManager">
        $('#protype,#prostate,#ischangeorder').change(function() {
           if($('#prostate').val()=='over'){
              $("#c").html(
                    '<select class="ui-input ui-input-mini" name="ischangeorder" id="ischangeorder" >' +
                    '<option value="" selected="selected">所有</option>' +
                  	'<option value="Y">已转订单</option>' +
                  	'<option value="N">未转订单</option>' +
         			'</select>' 
     			   );
	         $('#protype,#prostate,#ischangeorder').change(function() {
	         table.fnDraw();
	      	 });
           }else{
            $("#c").html('');
           }
            table.fnDraw();
        });
        
        </@security.authorize>
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title" style="padding-top: 0px;text-align:left;">
									竞价产品列表
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th >套餐名称</th>
                        <th >类型</th>
                        <th orderBy="saleprice">底价(元)</th>
                        <th orderBy="comparePrice">当前价(元)</th>
                           <th >围观次数</th>
                        <th >竞价次数</th>
                        <th orderBy="biddingDate">截止时间</th>
                        <@security.authorize ifAnyGranted="advertiser">
                           <th >竞价</th>
                           	</@security.authorize>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>