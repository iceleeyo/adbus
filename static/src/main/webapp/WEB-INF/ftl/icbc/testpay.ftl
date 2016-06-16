<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="GBK">
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<!-- 解决360兼容性问题 让360兼容IE8即可 -->
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title> </title>  
</head>  
<body>  
    <div class="box border">  
        <div class="box-title">  
            <h2><span class="glyphicon glyphicon-user"></span><jdf:message code="跳转到支付页面"/></h2>  
        </div>  
        <div class="box-body">  
            <form id="form1" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
    <input type="hidden" name="APIName" value="B2B"/>
    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
    <!--若不正确，将无银行反馈信息，注意不能省略"http://"-->
    <input type="hidden" name="MerchantURL" value="${callback}"/>
    <input type="hidden" name="ContractNo" value="${contractNo}"/>
    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
    <input type="hidden" name="ContractAmt" value="10"/>
    <input type="hidden" name="Account_cur" value="001"/>
    <input type="hidden" name="JoinFlag" value="2"/>
    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1}"/>
    <input type="hidden" name="Cert" value="${a2}"/>
    <input type="hidden" name="SendType" value="0"/>
    <input type="hidden" name="TranTime" value="${TranTime}" />
    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
    <input type="hidden" name="GoodsCode" value="001"/>
    <input type="hidden" name="GoodsName" value="paper"/>
    <input type="hidden" name="Amount" value="1"/>
    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
    <input type="hidden" name="TransFee" value="1"/>
    <input type="hidden" name="ShopRemark" value=""/>
    <input type="hidden" name="ShopRem" value=""/>
    <input type="submit" value="确定"/>
</form>
        </div>  
    </div>  
</body>  
</html>  