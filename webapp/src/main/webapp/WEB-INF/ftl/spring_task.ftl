<#import "template/template_blank.ftl" as frame>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路列表" js=["js/jquery-dateFormat.js"] >

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    .littlesmall {font-size:10px}
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
	
function setExpression(tourl,rowId){
	var p= ($("#price").val());
	if( p==""){
		layer.msg('请输入表达式', {icon: 5});
		return;
	}
	document.getElementById('uploadbutton').setAttribute('disabled',true); 
	$("#uploadbutton").css("background-color","#85A2AD");
	$.ajax({
		url : tourl ,
		type : "POST",
		data : {
			methodKey:rowId,
			expression:p
		},
		success : function(data) {
			 if(data.left){
				var uptime = window.setTimeout(function(){
					$("#cc").trigger("click");
					clearTimeout(uptime);
					},2500)
				layer.msg('设置成功');
				table.dataTable()._fnAjaxUpdate();
			}
		}
		}, "text");
}
	
	function setOrderPrice(tourl,rowId){
				var postPath =  tourl;
				layer.open({
	    		type: 1,
	    		title: "设置表达式",
	    		skin: 'layui-layer-rim', 
	    		area: ['420px', '240px'], 
	    		content: 
				 '<br/><br/><form id="priceForm"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs" style="margin-left:-25px;">'
				 +'<div class="ui-form-item"> <label class="ui-labels mt10" style="width:170px;"><span class="ui-form-required">*</span>表达式</label>' 
				 +'<input class="ui-inputLayer validate[required,custom[number],min[1]]" '
				 +' type="text" name="price" id="price" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="0/10 * * * * ?">'
				 +'</div>'
				  +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-20px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="setExpression(\''+postPath+'\',\''+rowId+'\');" value="设置表达式"> </div>'
				 +' </div> </div></form>' 
				
				});
	}
	
	

    var table;
    var _root;
    function initTable (root) {
    	_root = root;
        table = $('#table').dataTable( {
        select: true,
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "width": "10%", "targets": 0 },
                { "sClass": "littlesmall",  "targets": 1},
                { "width": "10%", "targets": 2 },
            ],
            "ajax": {
                type: "GET",
                url: _root+"/schedultTask/public_tasks",
                data: function(d) {
                    return $.extend( {}, d, {
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
           		 {
                    "data" : "", "defaultContent": "-"
                },
                {
                    "data" : "runnable.method.declaringClass", "defaultContent": "","render": function(data, type, row, meta) {
                		return row.runnable.target.statsMonitor!=null ? row. runnable.target.statsMonitor.className:data;
                	}
                },
                {
                    "data" : "runnable.method.name", "defaultContent": ""
                }, {
                    "data" : "expression", "defaultContent": ""
                }, {
                    "data" : "runnable.target.statsMonitor.taskStats", "defaultContent": "","render": function(data, type, row, meta) {
                		return data==null?'Online':data==true?'Online':'<font color="red">Offline</font>';
                	}
                },{
                    "data" : "runnable.target.statsMonitor.lastRuntime", "defaultContent": "","render": function(data, type, row, meta) {
                		return data==null?'':$.format.date(data, "MM-dd HH:mm:ss");
                	}
                } 
            ],
            "language": {
                "url": _root+"/js/jquery.dataTables.lang.cn.json"
            },"fnCreatedRow": function (nRow, aData, iDataIndex) {
            	var c = aData.runnable.target.statsMonitor!=null ?  aData.runnable.target.statsMonitor.className: aData.runnable.method.declaringClass;
                $(nRow).attr('id', c+"@"+aData.runnable.method.name); // or whatever you choose to set as the id
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback
        });
        
        
	    $('#table tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        }
	        else {
	            table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	    } );
	 
	    
    }
		/* Get the rows which are currently selected */
		function fnGetSelected( oTableLocal )
		{
		    return oTableLocal.$('tr.selected');
		}
    function initComplete() {
        $("div#toolbar").html(
                '<div style="float:right">' +
                   <!--    '    <span><button id="button">增加任务</button><a href="alert("暂未实现");" id="export_xls" class="btn-sm btn-success" >增加任务</a>' +-->
                       '    <span><a href="javascript:;"  id="off" class="btn-sm btn-success">暂停</a>' +
                       '    <span><a href="javascript:;"  id="on" class="btn-sm btn-success">恢复</a>' +
                       '    <span><a href="javascript:;" id="delTask" class="btn-sm btn-success">删除</a>' +
                       '    <span><a href="javascript:;"  id="editCron" class="btn-sm btn-success">修改表达式</a>' +
                       '    <span><a href="javascript:;"  id="runNow" class="btn-sm btn-success">立即运行一次</a>' +
                        '    </span>' +
                        '</div>'
        );

         

        $('#name').change(function() {
            table.fnDraw();
        });
        
        $('#delTask').click( function () {
		   	var anSelected = fnGetSelected( table );
	        if ( anSelected.length !== 0 ) {
	        var _row=anSelected[0];
			 var url=_root+"/schedultTask/public_canel";
		    $.get(url,{
		  	  methodKey:_row.id
		    },function(data){
		    	if(data){
		    		layer.msg("删除成功!");
		    		anSelected[0] .remove();
		    	}else {
		    		layer.msg("删除任务成功!");
		    	}
		    });
	      		//anSelected[0] .remove();//.draw( false );
	        }else {
	        	layer.msg("请先选择列!");
	        }
		      //table.row('.selected').remove().draw( false );
	    } );
	     $('#on').click( function () {
		   	var anSelected = fnGetSelected( table );
	        if ( anSelected.length !== 0 ) {
		        var _row=anSelected[0];
				 var url=_root+"/schedultTask/public_stats";
			    $.get(url,{
			  	  methodKey:_row.id,
			  	   stats:'on'
			    },function(data){
			    	layer.msg("恢复成功!");
			    	table.dataTable()._fnAjaxUpdate();
			    });
	        }else {
	        	layer.msg("请先选择列!");
	        }
	    } );
	    
	       $('#off').click( function () {
		   	var anSelected = fnGetSelected( table );
	        if ( anSelected.length !== 0 ) {
		        var _row=anSelected[0];
				 var url=_root+"/schedultTask/public_stats";
			    $.get(url,{
			  	  methodKey:_row.id,
			  	   stats:'off'
			    },function(data){
			    	layer.msg("暂停成功!");
			    	table.dataTable()._fnAjaxUpdate();
			    });
	        }else {
	        	layer.msg("请先选择列!");
	        }
	    } );
	    
	         $('#editCron').click( function () {
		   	var anSelected = fnGetSelected( table );
	        if ( anSelected.length !== 0 ) {
	          var _row=anSelected[0];
	        	setOrderPrice(_root+"/schedultTask/public_expression",_row.id);
		       
	        }else {
	        	layer.msg("请先选择列!");
	        }
	    } );
	     
        $('#runNow').click( function () {
		   	var anSelected = fnGetSelected( table );
	        if ( anSelected.length !== 0 ) {
		        var _row=anSelected[0];
				 var url=_root+"/schedultTask/public_runTaskNow";
			    $.get(url,{
			  	  methodKey:_row.id
			    },function(data){
			    	layer.msg("执行成功!");
			    	table.dataTable()._fnAjaxUpdate();
			    });
	        }else {
	        	layer.msg("请先选择列!");
	        }
	    } );
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }

    $(document).ready(function() {
        initTable('${rc.contextPath}');
    } );
    
</script>    
            <div class="title s-clear">
            

			  	<span style="width:250px">
			  		系统定时任务[spring @schedule]
			  	</span>
			  </div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                    	<th orderBy="name">任务名</th>
                        <th orderBy="name">类名</th>
                        <th orderBy="level">方法名</th>
                         <th orderBy="_cars">expression</th>
                          <th orderBy="_cars">状态</th>
                          <th >上次运行时间</th>
                    </tr>
                    </thead>

                </table>
                
                
                
</div>



 
</@frame.html>