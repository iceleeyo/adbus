<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
<link rel="stylesheet" type="text/css"
	href="http://busme.cn/css/layer.css">
   <script type="text/javascript" src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
	<script src="${rc.contextPath}/js/datepicker.js"></script>
	<script src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>
	<script src="${rc.contextPath}/js/jquery-ui/jquery-ui.js"></script>
	<script src="${rc.contextPath}/js/jquery-dateFormat.js"></script>


<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/js/jquery-ui/jquery-ui.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/jquery-ui-1.8.16.custom.css">


<script type="text/javascript">  
function getEnd(date,days){
	var d=new Date(date);
	if(date=='' || d==""){
		return "";
	}else{
	    d.setDate(d.getDate()+ (days)-1); 
	    var m=d.getMonth()+1;
	    d.getFullYear()+'-'+m+'-'+d.getDate();
	   	d= $.format.date(d, "yyyy-MM-dd");
	   	document.write(d);
	}
	

}
</script>
<style type="text/css">

.block-btn {
    border: 0;
    font-size: 14px;
    height: 32px;
    line-height: 32px;
    text-align: center;
    background: #00a8e8;
    color: #fff;
    cursor: pointer;
    display: inline-block;
    padding: 0 24px;
    text-decoration: none;
    font-weight: 600;
    letter-spacing: 2px;
    margin-left:400px;
}
.b1 {
	white-space-collapsing: preserve;
}

* {
	font-size: 14px;
}

.b2 {
	margin: 20px 30px 0px 30px;
}

.s1 {
	font-weight: bold;
	color: black;
	font-size: 20px;
}

.s2 {
	color: black;
}

.s3 {
	color: red;
}

.s4 {
	display: inline-block;
	text-indent: 0;
	min-width: 0.3402778in;
}

.s5 {
	font-weight: bold;
}

.s6 {
	font-weight: bold;
	color: red;
}

.s7 {
	display: inline-block;
	text-indent: 0;
	min-width: 0.3027778in;
}

.s8 {
	text-decoration: underline;
}

.s9 {
	display: inline-block;
	text-indent: 0;
	min-width: 0.4861111in;
}

.s10 {
	display: inline-block;
	text-indent: 0;
	min-width: 0.14583333in;
}

.p1 {
	text-align: center;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 22pt;
}

.p2 {
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: justify;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}
.p21 {
	margin-top: 0;
	margin-bottom: 0;
	text-align: justify;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
	
}


.p3 {
	text-indent: 0.3888889in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: justify;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p4 {
	text-indent: 0.29166666in;
	margin-left: 0.35416666in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p5 {
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p6 {
	margin-left: 0.29166666in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p7 {
	text-indent: 0.29166666in;
	
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p8 {
	text-indent: 0.29166666in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: justify;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p9 {
	text-indent: -0.29166666in;
	margin-left: 0.29166666in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p10 {
	text-indent: 0.29166666in;
	margin-left: 0.29166666in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p11 {
	text-indent: 0.19444445in;
	margin-left: 0.14583333in;
	margin-top: 0.108333334in;
	margin-bottom: 0.108333334in;
	text-align: start;
	hyphenate: auto;
	font-family: 宋体;
	font-size: 14pt;
}

.p12 {
	text-align: justify;
	hyphenate: auto;
	font-family: Times New Roman;
	font-size: 10pt;
}

.fy{
	page-break-after:always;

}
.table-c table{border-right:1px solid #000;border-bottom:1px solid #000;height:40px;}
.table-c table td{border-left:1px solid #000;border-top:1px solid #000;padding-left:5px;width: 15%;}
.table-c table th{border-left:1px solid #000;border-top:1px solid #000}
.table-c table tr{border-left:1px solid #000;border-top:1px solid #000;height: 40px;}
.p21_hr{border: none;border-top: 1px solid rgb(199, 192, 192); line-height: 0px; margin:0px;}
.p21 .s2{color:rgb(199, 192, 192);}
.logo{height: 40px;width: 80px;}
</style>
<meta content="Administrator" name="author">
</head>
<body class="b1 b2">
<div id="divPrint">
	
	<p class="p21">
		<img class="logo" src="http://busme.cn/imgs/shiba_log.jpg">
		<span class="s2" >北京世巴传媒有限公司</span>
		<span class="s2" style="float:right; text-align:right;margin-top: 26px;" >合同编号：<span class="s3">${(jpaPayContract.contractCode)!''}</span></span>
		<hr class="p21_hr" />
	</p>
	
	<p class="p1">
		<span class="s1">关于  ${(jpaPayContract.contractCode)!''} 合同的补充协议</span>
	</p>
	<p class="p2">
		<span class="s2">甲方（全称）：</span><span class="s3">${(agreement.company)!''}</span></span>
	</p>
	<p class="p2">
		<span class="s2">乙方（全称）：</span><span class="s3">北京世巴传媒有限公司 </span>
	</p>
	
	
	<br>
	
	<p class="p3">
		<span class="s2">本协议中的所有术语，除非另有说明，否则其定义与双方于 <input style="width:90px;border-color: #00A7E8;" class="ui-input datepicker validate[required,custom[date] " type="text" value="${(agreement.signD)!''}" id="signD" >签订的合同编号为 ${(jpaPayContract.contractCode)!''}的广告/节目发布合同（以下简称“原合同”）中的定义相同。<br/><br/>
		发布内容为《<textarea rows="3" cols="50" id="publish">${(agreement.publish)!''}</textarea>   》硬广广告。<br/><br/>发布时间为 <input style="width:90px; border-color: #00A7E8;" class="ui-input datepicker validate[required,custom[date] " type="text" value="${(agreement.startD)!''}" id="startD" >至 <input style="width:90px; border-color: #00A7E8;" class="ui-input datepicker validate[required,custom[date] " type="text" value="${(agreement.endD)!''}" id="endD" >止。</span>
										
										
										 
	</p>
	<p class="p3">
		<span class="s2">甲乙双方本着互利互惠的原则，经友好协商，依据实际情况，在原合同基础上变更合同条款部分内容，特订立以下补充协议。</span>
	</p>
	
	<p class="p7">
	<span class="s7">1.</span>
		<textarea style="margin-left:30px;" rows="7" cols="90" id="agree1">${(agreement.agree1)!''}</textarea>
	</p>

	<p class="p7">
		<span class="s7">2.</span><span>本协议生效后，即成为原协议不可分割的组成部分，与原协议具有同等的法律效力;本协议与原协议有相互冲突时，以本协议为准。  
		</span>
	</p>
	<p class="p7">
		<span class="s7">3.</span><span>除本协议中明确所作修改的条款之外，原协议的其余部分继续有效。  
		</span>
	</p>
	<p class="p7">
		<span class="s7">4.</span><span>本协议一式肆份，甲、乙双方各执贰份，具有同等法律效力，自双方签字盖章之日起生效。
		</span>
	</p>

	
	<p class="p7 fy">
	<span class="s7"><br><br<br><br>
	&nbsp;&nbsp;&nbsp;甲方（盖章）：              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;乙方（盖章）：
	<br><br><br><br>
	&nbsp;&nbsp;&nbsp;授权代表（签字）：                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;授权代表（签字）：
	<br><br><br><br>
	&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;月&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;月&nbsp;&nbsp;日
	<br><br><br><br></span></p>
	
	
	<br><br>
</div>
<a class="block-btn" style="margin-top: -5px;margin-left: 42%;" href="javascript:saveAgree()" target="_self">保存</a>
</body>
<script>   
var global_Html = "";   
function saveAgree() {
                var obj=new Object(); 
                obj.agree1=$("#agree1").val();
                obj.signD=$("#signD").val();
                obj.startD=$("#startD").val();
                obj.endD=$("#endD").val();
                obj.publish=$("#publish").val();
           var str = JSON.stringify(obj);  
       var param={"jsonStr":str}
       var id=${jpaPayContract.id};
      $.ajax({
		    			url:"${rc.contextPath}/payContract/saveMark/"+id,
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    			layer.msg(data.right);
		    			if(data.left){
		    			   window.setTimeout('hand',1500);
		    			}
		    			}
		       });  
}
function hand(){
window.location.reload();
}
</script>

<br><br><br><br><br><br>

</body>
<!--增加lay最新版本-->
	<script type="text/javascript" language="javascript"
		src="http://busme.cn/js/layer-v1.9.3/layer/layer.js"></script>
	<script type="text/javascript" language="javascript"
		src="http://busme.cn/js/layer.onload.js"></script>
	<script type="text/javascript" language="javascript"
		src="http://busme.cn/js/layer-v1.9.3/layer-site.js"></script>	
</html>
