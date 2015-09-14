<#import "template/template.ftl" as frame>
<#global menu="用户登录">
<@frame.html title="用户登录" left=false nav=false js=["js/querystring-0.9.0.js","js/layer-v1.9.3/layer/layer.js"] css=["css/login.css"]>
<style type="text/css">
.ls-10{
	width: 85%;
 	float: left;
 	margin-left: 15%;
}
</style>
<div class="pg-container">
    <div class="pg-container-main">
        <div class="container-12 mt10 s-clear">
            <div class="login-content s-clear">
                <div class="s-left login-left">
                    <div class="login-box">
                        <img src="images/login.png">
                    </div>
                </div>
                <div class="s-right login-right">
                    <div class="login-info module">
                        <form id='loginForm' name='loginForm' class="login-form" action="login" method='POST'>
                            <fieldset><br>
                             <#if (SPRING_SECURITY_LAST_EXCEPTION.message)?? && (SPRING_SECURITY_LAST_EXCEPTION.message)?index_of("Bad")!=-1>
                             	<font color="red">密码错误!</font>
   								<#else>
   								${(SPRING_SECURITY_LAST_EXCEPTION.message)!''}
							</#if>
                             
                                <div class="login-tips mb10" style="display : none;">
                                    <span class="login-tip">密码不能为空</span>

                                </div>
                                <div class="login-item">
                                    <input class="login-input input-p gray-input" type="text" placeholder="请输入用户名" id="username" name="username">
                                    <span class="login-name-icon icon-position-user"></span>
                                </div>
                                <div class="login-item">
                                    <input class="login-input input-p gray-input" type="password" placeholder="请输入密码" id="password" name="password">
                                    <span class="login-name-icon icon-position-pwd"></span>
                                </div>
                                <div class="login-item s-clear">
                                    <a class="s-right" href="${rc.contextPath}/user/find_pwd">忘记密码</a>
                                </div>
                                <div class="login-item p-center">
                                    <p class="mt37"></p>
                                    <input type="submit" name="submit" value="立即登录" class="login-btn login-btn-size func-submit"/>
                                </div>
                                <input type="hidden"
                                       name="${(_csrf.parameterName)!''}" value="${(_csrf.token)!''}" />
                                <div class="login-item p-center">
                                    <span>没有账号？</span>
                                    <a href="${rc.contextPath}/register">免费注册</a>
                                </div>
                            </fieldset>
                        </form>
                        <div class="login-bottom p-center s-clear">
                            <label class="s-left pl20"></label>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@frame.html>