/**
 * 验证相关
 *
 * @param
 */
function isNotEmptyString(str) {
	if (str != null && str != undefined && str != "") {
		return true;
	}	
	return false;
}
function isPasswd(s) {  
	var patrn=/^(.){6,20}$/;  
	if (!patrn.test(s)) return false
		return true
}
function checkUserName(str){
	var re=/^[A-Za-z0-9\._'\u4e00-\u9fa5]+$/ig;
	if (!re.test(str)) return false
   		return true 
}

function checkID(value, element) {
	var tmpValue = $.trim(value);
	var pattern =/^[a-zA-Z][a-zA-Z0-9_]{4,11}$/;
	if (tmpValue != ''){
		if(pattern.test(tmpValue)){
			return true;
		}
	}
    return false;
}

function checkPassword(value, element) {
	var tmpValue = $.trim(value);
	var pattern =/^[a-zA-Z0-9_]{8,16}$/;
    if (tmpValue != ''){
		if(pattern.test(tmpValue)){
			return true;
		}
	}
    return false; 
}

function checkChinese(value, element) {
	var tmpValue = $.trim(value);
	var pattern = /^[u4E00-u9FA5]+$/;
	if (tmpValue != ''){
		if(!pattern.test(tmpValue)){
			return true;
		}
	}
	return false;
}

var tableId="tbl";
var oddRowColor="#f1f1f1";
var evenRowColor="#fff";
var hoverRowColor="#C6D7F8";
var highlightRowColor="#FFD460";
function senfe(o,a,b,c,d) {
	var t=document.getElementById(o).getElementsByTagName("tr"); 
	for(var i=0;i<t.length;i++) { 
		t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?a:b; 
		t[i].onclick=function() {
			var td = this.getElementsByTagName("td")[0];
			var ck = null;
			if (td != null) {
				ck = td.firstChild;
			}
			if(this.x!="1"){
				this.x="1";
				this.style.backgroundColor=d; 
				if (ck != null) {
					ck.checked = true;
				}
			} else { 
				this.x="0"; 
				this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b; 
				if (ck != null) {
					ck.checked = false;
				}
			} 
		} 
		t[i].onmouseover=function() { 
			if(this.x!="1")this.style.backgroundColor=c; 
		} 
		t[i].onmouseout=function() { 
			if(this.x!="1")this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b; 
		} 
	} 
}

function checkAll(tblId, selector) {
	var len = $(selector).length;
	if (len > 0) {
		var t=document.getElementById(tblId).getElementsByTagName("tr");
		if ($('#checkAll').attr("checked") == "checked") {
			$(selector).attr("checked", "checked");
			
			for(var i=1;i<t.length;i++) { 
				t[i].x="1";
				t[i].style.backgroundColor=highlightRowColor;
			}
		}
		else {
			$(selector).attr("checked", false);
			for(var i=1;i<t.length;i++) { 
				t[i].x="0";
				t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?evenRowColor:oddRowColor;
			}
		}
	}
}

//检查标题，只允许中文、英文、数字、下划线
function chkTitle(str, minlen, maxlen){
	var re = /^[A-Za-z0-9_\u4e00-\u9fa5]+$/ig;
	if(re.test(str)){
		var ret = true;
		if(typeof minlen == 'number'){
			if(str.length < minlen) ret = false;
		}
		if(typeof maxlen == 'number'){
			if(str.length > maxlen) ret = false;
		}
		return ret;
	}else{
		return false;
	}
}

//检查面板标题，只允许中文、英文、数字、下划线[]
function chkPanelTitle(str, minlen, maxlen){
	var re = /^[A-Za-z0-9_\[\]\u4e00-\u9fa5]+$/ig;
	if(re.test(str)){
		var ret = true;
		if(typeof minlen == 'number'){
			if(str.length < minlen) ret = false;
		}
		if(typeof maxlen == 'number'){
			if(str.length > maxlen) ret = false;
		}
		return ret;
	}else{
		return false;
	}
}

//验证密码
function checkPassword2(value) {
	var pattern =/^[A-Za-z0-9\W]+$/ig;
	var ret = true;
	if(pattern.test(value)){
		if(value.length < 6) ret = false;
		if(value.length > 12) ret = false;
	}else{
		ret = false;
	}
	return ret;
}

//检查是否7位以内0以上整数
function chkInt(str){
	var re = /^[0-9]{1,7}$/ig;
	return re.test(str);
}

//检查是否为数字（包括整数1-7位以内、小数）
function chkNumber(str){
	var re = /^[0-9]{1,7}((\.[0-9]{1,2}){0,1})?$/ig;
	return re.test(str);
}

//检查是否是7位以内正整数
function chkPositiveInt(str){
	var re = /^[1-9]{1}[0-9]{0,6}$/ig;
	return re.test(str);
}

//检查是否是电话号码
function chkPhone(str){
	var re = /^(13[0-9]{9})|(15[389][0-9]{8})|(17[0-9]{9})|(18[0-9]{9})$/; 
	return re.test(str);
}
//检查是否是邮件地址
function isEmail(str) { 
	 var re = /^[0-9a-z][_.0-9a-z-]{0,31}@([0-9a-z][0-9a-z-]{0,30}[0-9a-z]\.){1,4}[a-z]{2,4}$/; 
	 return re.test(str);
}
