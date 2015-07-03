<#import "template/template.ftl" as frame>
<#global menu="产品竞价">
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="产品竞价" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
    $(document).ready(function() {
    });
    
    
function compare(){
var productid=$("#productid").val();
var myprice=$("#myprice").val();
$.ajax({
			url:"${rc.contextPath}/product/comparePrice",
			type:"POST",
			async:false,
			dataType:"json",
			data:{
			"cpdid":productid,
			"myprice":myprice
			},
			success:function(data){
			  jDialog.Alert(data.right);
			}
      }); 
}
	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
							<form id="userForm2" name="userForm2" action="put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
								enctype="multipart/form-data" method="post"">
								<div class="withdraw-title fn-clear">
								</div>
								<div class="withdrawInputs">
									<div class="inputs">

										<div class="ui-form-item">
											围观数${jpaCpd.pv} 
										compare数${jpaCpd.setcount} 
											当前价¥ ${jpaCpd.comparePrice!''}<br>
											原价 ¥${(jpaCpd.product.price)!''}<br>
											围观数2342人次<br>
											<input type="text" id="myprice"/>
											<input type="button" onclick="compare()" value="出价"/>
											<input type="hidden" id="productid" value="${(jpaCpd.product.id)!''}"/>	
										</div>
									</div>
								</div>
								     <table>
								     <tr>
								         <td>时间</td>
								         <td>出价人</td>
								         <td>出价</td>
								         <td>状态</td>
								        </tr>
								<#list userCpdList as item>
								        <tr>
								         <td>${item.created?string("yyyy-MM-dd HH:mm:ss")}</td>&nbsp;
								         <td>${item.userId!''}</td>&nbsp;
								         <td>${item.comparePrice!''}</td>&nbsp;
								         <td>出局</td>
								        </tr>
								</#list>
								     </table>
							</form>
</div>
</@frame.html>
