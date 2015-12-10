package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.mybatis.domain.ActIdGroup;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.CardService;
import com.pantuo.service.DataInitializationService;
import com.pantuo.service.GoupManagerService;
import com.pantuo.service.InvoiceServiceData;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.MailTask.Type;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.impl.GoupManagerServiceImpl;
import com.pantuo.service.security.Request;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.InvoiceView;
import com.pantuo.web.view.RoleView;
import com.pantuo.web.view.UserQualifiView;

/**
 * <font size=5><b>公交广告交易系统接口</b></font>
 *<P>
 *
 * @author tliu
 *
 */

@Controller
@RequestMapping(value = "user", produces = "application/json;charset=utf-8")
@SessionAttributes("_utype")
public class UserManagerController {
	private static Logger log = LoggerFactory.getLogger(UserManagerController.class);

	@Autowired
	private UserServiceInter userService;
	
	@Value("${sys.type}")
	private String isBodySys;
	@Autowired
	private MailJob mailJob;
	@Autowired
	MailService mailService;
	@Autowired
	@Lazy
	private DataInitializationService dataService;
	@Autowired
	private InvoiceServiceData invoiceServiceData;
	@Autowired
	SuppliesService suppliesService;
	@Autowired
	OrderService orderService;
	@Autowired
	GoupManagerService goupManagerService;
	@Autowired
	@Lazy
	ProductService productService;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	@Lazy
	CardService cardService;
	
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET })
	public String userlist(Model model) {
		model.addAttribute("usertype", "screen");
		return "user_list";
	}
	
	@RequestMapping(value = "/adlist", method = { RequestMethod.GET })
	public String useradlist(Model model) {
		model.addAttribute("usertype", "pub");
		return "user_list";
	}
	
	@RequestMapping(value = "/bodyuserlist", method = { RequestMethod.GET })
	public String bodyuserlist(Model model) {
		model.addAttribute("usertype", "body");
		return "user_list";
	}

	/**
	 * <b>Ajax：获取所有用户</b>
	 *
	 */
	@RequestMapping(value = "/ajax-list", method = { RequestMethod.GET })
	@ResponseBody
	public DataTablePage<UserDetail> getUsers(TableRequest req, @ModelAttribute("_utype") String user) {
		System.out.println(user);
		/*return new DataTablePage(userService.getAllUsers(req.getFilter("utype"),req.getFilter("name"), req.getPage(), req.getLength(),
				req.getSort("id")), req.getDraw());*/
		return new DataTablePage<UserDetail>(userService.getUsers(req.getFilter("utype"), req.getFilter("name"), null,
				req.getPage(), req.getLength(), req.getSort("id"), UType.valueOf(user)), req.getDraw());
	}
	@RequestMapping(value = "/invoiceList")
	public String invoicelist() {

		return "invoiceList";
	}
	@RequestMapping("ajax-invoiceList")
	@ResponseBody
	public DataTablePage<JpaInvoice> getAllInvoice(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		return new DataTablePage(invoiceServiceData.getAllInvoice(city, req, principal), req.getDraw());
	}
	@RequestMapping(value = "/{username}/{enable}", method = { RequestMethod.POST })
	@ResponseBody
	public UserDetail enableUser(@PathVariable("username") String username, @PathVariable("enable") String enable) {
		boolean en = "enable".equals(enable);
		UserDetail user = userService.findDetailByUsername(username);
		if (user == null) {
			UserDetail u = new UserDetail();
			u.setErrorInfo(BaseEntity.ERROR, "找不到用户名为" + username + "的用户，或者数据冲突");
			return u;
		}

		if (user.isEnabled() != en) {
			user.setEnabled(en);
			userService.saveDetail(user);
		}
		return user;
	}
	@RequestMapping(value = "/ustats/{username}/{ustats}", method = { RequestMethod.POST })
	@ResponseBody
	public UserDetail ustatsUpdate(@PathVariable("username") String username, @PathVariable("ustats") String ustats,HttpServletRequest request) {
		UserDetail user = userService.findDetailByUsername(username);
		if (user == null) {
			UserDetail u = new UserDetail();
			u.setErrorInfo(BaseEntity.ERROR, "找不到用户名为" + username + "的用户，或者数据冲突");
			return u;
		}
		user.setUstats(UserDetail.UStats.valueOf(ustats));
		userService.saveDetail(user);
		mailJob.putMailTask(new MailTask(user, Type.sendCanCompareMail)); 
		return user;
	}

	@RequestMapping(value = "/invoice", produces = "text/html;charset=utf-8")
	public String invoice(Model model,Principal principal,HttpServletRequest request) {
		//InvoiceView invoiceView=userService.findInvoiceByUser(principal);
		//model.addAttribute("invoiceView", invoiceView);
		return "invoice_message";
	}
	
	@RequestMapping(value = "/invoice_edit/{invoice_id}", produces = "text/html;charset=utf-8")
	public String invoice_edit(Model model,@PathVariable("invoice_id") int invoice_id,Principal principal,HttpServletRequest request) {
		InvoiceView invoiceView=userService.findInvoiceByUser(invoice_id,principal);
		model.addAttribute("invoiceView", invoiceView);
		return "invoice_message";
	}
	@RequestMapping(value = "/delInvoice/{invoice_id}")
	@ResponseBody
	public Pair<Boolean, String> delInvoice(Model model,
			@PathVariable("invoice_id") int invoice_id, Principal principal,
				HttpServletRequest request) {
		return userService.delInvoice(invoice_id,principal);
	}
	@RequestMapping(value = "/invoice_detail/{invoice_id}")
	@ResponseBody
	public InvoiceView invoice_detail(Model model,
			@PathVariable("invoice_id") int invoice_id, Principal principal,
			HttpServletRequest request) {
		   InvoiceView invoiceView=userService.findInvoiceByUser(invoice_id,principal);
		   return invoiceView;
	}
	@RequestMapping(value = "saveInvoice", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Object, String> saveInvoice(@CookieValue(value = "city", defaultValue = "-1") int city,JpaInvoice obj, Principal principal, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.addInvoice(city,obj, principal, request);
	}
	@RequestMapping(value = "/find_pwd", produces = "text/html;charset=utf-8")
	public String find_pwd(HttpServletRequest request) {
		return "find_pwd";
	}
	
	@RequestMapping(value = "/contract_templete", produces = "text/html;charset=utf-8")
	public String contract_templete(Model model,Principal principal,
			@RequestParam(value="orderid" ,required=false, defaultValue ="0") int orderid,
			@RequestParam(value="productid" ,required=false, defaultValue ="0") int productid,
			@RequestParam(value = "meids", required = false) String meids,
			HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		if(orderid>0){
			Orders orders=orderService.selectOrderById(orderid);
			if(orders!=null){
				if (Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
					if(!StringUtils.equals(Request.getUserId(principal), orders.getUserId())){
						throw new AccessDeniedException("非法操作！");
					}
				}
				List<JpaOrders> ordersList=orderService.findordersList(orders.getContractCode());
				UserDetail userDetail = userService.findByUsername(orders.getUserId());
				model.addAttribute("ordersList", ordersList);
				model.addAttribute("userDetail", userDetail);
				model.addAttribute("contractCode",orders.getContractCode());
			}
			
		}else{
			UserDetail userDetail = userService.findByUsername(Request.getUserId(principal));
			if(meids!=null && meids!=""){
				List<JpaCardBoxMedia> cardBoxMedis=productService.selectProByMedias(meids);
				model.addAttribute("cardBoxMedis", cardBoxMedis);
			}
			model.addAttribute("userDetail", userDetail);
		}
		return "contract_templete";
	}
	
	@RequestMapping(value = "/busContract_templete", produces = "text/html;charset=utf-8")
	public String busContract_templete(Model model,Principal principal,
			@RequestParam(value="orderid" ,required=false, defaultValue ="0") int orderid,
			@RequestParam(value="productid" ,required=false, defaultValue ="0") int productid,
			HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		UserDetail userDetail = userService.findByUsername(Request.getUserId(principal));
		model.addAttribute("userDetail", userDetail);
		return "contract_bustemplete";
	}
	@RequestMapping(value = "/activate", produces = "text/html;charset=utf-8")
	public String activate(Model model,Principal principal,HttpServletRequest request,@RequestParam(value = "userId") String userId,
			@RequestParam(value = "uuid") String uuid) {
		if (StringUtils.isNoneBlank(uuid) && StringUtils.equals(uuid, userService.getUserUniqCode(userId))) {
			UserDetail user = userService.findDetailByUsername(userId);
			user.setIsActivate(1);
			userService.saveDetail(user);
			model.addAttribute("msg", "账号激活成功");
			return "activateSuccess";
		} else {
			model.addAttribute("msg", "链接无效");
			return "error";
		}
	}
	@RequestMapping(value = "/reset_pwd", produces = "text/html;charset=utf-8")
	public String reset_pwd(Model model, HttpServletRequest request, @RequestParam(value = "userId") String userId,
			@RequestParam(value = "uuid") String uuid) {
		if (StringUtils.isNoneBlank(userId)) {
			model.addAttribute("userId", userId);
		}
		if (StringUtils.isNoneBlank(uuid) && StringUtils.equals(uuid, GlobalMethods.md5Encrypted(userId.getBytes()))) {
			return "reset_pwd";
		} else {
			model.addAttribute("msg", "链接无效");
			return "error";
		}
	}
	
	@RequestMapping(value="/edit_pwd",produces="text/html;charset=utf-8")
	public String edit_pwd(Model model,Principal principal){
		model.addAttribute("userid", Request.getUserId(principal));
		return "edit_pwd";
	}
	/*@PreAuthorize(" !hasRole('advertiser')  ")*/
		@PreAuthorize( " hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
	+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
	+ "or hasRole('ShibaSuppliesManager')or hasRole('UserManager') ")
	@RequestMapping(value = "/autoComplete")
	@ResponseBody
	public List<AutoCompleteView> queryUserByname(Model model, HttpServletRequest request,
			@RequestParam(value = "term") String name) {
		return userService.autoCompleteByName(name);
	}

	@RequestMapping(value = "/change_pwd")
	@ResponseBody
	public Pair<Boolean, String> change_pwd(Model model, Principal principal,
			@RequestParam(value = "userId") String userId, @RequestParam(value = "psw") String psw, @RequestParam(value = "oldpassword" ,required = false) String oldpassword,
			HttpServletRequest request) throws Exception {
		if(StringUtils.isNotBlank(oldpassword)){
			return userService.editPwd(Request.getUserId(principal),oldpassword, psw);
		}
		return userService.updatePwd(userId, psw);
	}
	@RequestMapping(value = "/isAdvertiser/{userid}")
	@ResponseBody
	public Pair<Boolean, String> isAdvertiser(Model model, Principal principal,
			@PathVariable(value = "userid") String userid,
			HttpServletRequest request) throws Exception {
		if (!userService.isUserHaveGroup(userid, SystemRoles.advertiser.name())) {
			return new Pair<Boolean, String>(false, userid + " 不是广告主,保存失败！");
		}
		return new Pair<Boolean, String>(true, userid + " 是广告主！");
	}

	@RequestMapping(value = "/send_pwd_link")
	@ResponseBody
	public Pair<Boolean, String> send_pwd_link(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		if (StringUtils.isNoneBlank(userId)) {
			UserDetail user = userService.getByUsername(userId);
			if (user == null) {
				return new Pair<Boolean, String>(true, "不存在的用户名");
			}
			return mailService.sendRestPwdMail(user);
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

    
	@RequestMapping(value = "/qualification", produces = "text/html;charset=utf-8")
	public String qualification(Model model,Principal principal,HttpServletRequest request) {
		model.addAttribute("userDetail", userService.getByUsernameSafe(Request.getUserId(principal)));
		return "qualification_Enter";
	}
	
	@RequestMapping(value = "/updateQualifi", method = { RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> updateQualifi(Principal principal,UserQualifiView userQualifiView,HttpServletRequest request) {
		return suppliesService.savequlifi(principal,userQualifiView, request, null);
	}
	

/*	@PreAuthorize("hasRole('advertiser') " + "or hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')or hasRole('UserManager') ")*/
	
	
	@RequestMapping(value = "/enter")
	public String enter(Model model, HttpServletRequest request,@CookieValue(value = "city", defaultValue = "-1")int city) {
		model.addAttribute("groupsList", DataInitializationService._GROUPS);
		model.addAttribute("bdGroupsList", goupManagerService.getAllDescionGroup(city));
		return "u/userEnter";
	}
	//@PreAuthorize(" hasRole('UserManager')  ")
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	@ResponseBody
	public UserDetail createProduct(UserDetail detail, HttpServletRequest request,Principal principal) {
		if(detail!=null &&StringUtils.contains(isBodySys, "body")){
			detail.setUtype(UType.body);
		}else {
			detail.setUtype(UType.screen);
		}
		userService.createUserFromPage(detail,request,principal);
		return detail;
	}


	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	@ResponseBody
	public UserDetail register(UserDetail detail, HttpServletRequest request,Principal principal) {
		if(detail!=null && !StringUtils.contains(isBodySys, "body")){
			detail.setUtype(UType.pub);
		}
		userService.createUserFromPage(detail,request,principal);
		return detail;
	}
	@RequestMapping(value = "/u_edit/update", method = { RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> updateUser(UserDetail detail, Principal principal,HttpServletRequest request) {
		return userService.updateUserFromPage(detail,principal, request);
	}
	
	@RequestMapping(value = "savequalifi", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> savequalifi(Principal principal, @RequestParam(value = "description") String description, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.savequlifi(principal,null, request,description);
	}
	@RequestMapping(value = "/u/{userId}", method = { RequestMethod.GET })
	public String uDetail(Model model, @PathVariable("userId") String userId, HttpServletRequest request) {
		List<Attachment> attachment=attachmentService.findUserQulifi(userId);
		model.addAttribute("userDetail", userService.getByUsernameSafe(userId));
		model.addAttribute("attachment", attachment);
		return "u/userDetail";
	}
	@PreAuthorize( " hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')or hasRole('UserManager') ")
	@ResponseBody
	@RequestMapping(value = "/u_ajax/{userId}", method = { RequestMethod.GET })
	public Pair<Object, Object> showUDetail(Model model, @PathVariable("userId") String userId, HttpServletRequest request) {
		UserDetail u =  userService.getByUsernameSafe(userId);
		List<Attachment> attachment=attachmentService.findUserQulifi(userId);
		return new Pair<Object, Object>(u, attachment);
	}
	@ResponseBody
	@RequestMapping(value = "/qua/{userId}")
	public UserQualifiView qua(Model model, @PathVariable("userId") String userId, HttpServletRequest request) {
		UserDetail userDetail=userService.getByUsernameSafe(userId);
		return cardService.getUserQualifiView(userDetail.getQulifijsonstr());
	}
	@RequestMapping(value = "/UserQulifi", produces = "text/html;charset=utf-8")
	public String UserQulifi(Model model,Principal principal,HttpServletRequest request) {
		UserDetail userDetail=userService.getByUsernameSafe(Request.getUserId(principal));
		model.addAttribute("userDetail",userDetail);
		model.addAttribute("jsonView", cardService.getUserQualifiView(userDetail.getQulifijsonstr()));
//		List<Attachment> attachments=attachmentService.findUserQulifi(Request.getUserId(principal));
//		model.addAttribute("attachments", attachments);
//		if(attachments.size()>0){
//			model.addAttribute("typelist", userService.gettypeListByAttach(attachments));
//		}
		return "UserQualifi";
	}
	@ResponseBody
	@RequestMapping(value = "/queryPayvoucher/{orderid}")
	public List<Attachment> queryPayvoucher(Principal principal, @PathVariable("orderid") int orderid, HttpServletRequest request) {
		List<Attachment> attachment=suppliesService.queryPayvouchers(principal, orderid);
		return attachment;
	}
	
	
	//@PreAuthorize(" hasRole('UserManager')  ")
	@RequestMapping(value = "/u_edit/{userId}", method = { RequestMethod.GET })
	public String userEdit(@CookieValue(value = "city", defaultValue = "-1")int city,
			Model model, @PathVariable("userId") String userId, HttpServletRequest request) {
		UserDetail UserDetail = userService.getByUsernameSafe(userId);
		model.addAttribute("userDetail", UserDetail);
		model.addAttribute("uGroup", userService.getUserGroupList(UserDetail));
		model.addAttribute("groupsList", DataInitializationService._GROUPS);
		//
		model.addAttribute("bdGroupsList", goupManagerService.getAllDescionGroup(city));
		
		return "u/userEdit";
	}
	@RequestMapping(value = "/addRole")
	public String addRole(Model model) {
		List<JpaFunction> functions= goupManagerService.getAllFunction();
		model.addAttribute("functions", functions);
		return "u/addRole";
	}
	@RequestMapping(value = "/to_editRole/{groupid}")
	public String to_editRole(@CookieValue(value = "city", defaultValue = "-1") int city,Model model,@PathVariable("groupid") String groupid) {
		List<JpaFunction> functions= goupManagerService.getAllFunction();
		ActIdGroup actIdGroup=goupManagerService.getActIdGroupByID(groupid, city);
		
		if(actIdGroup!=null && StringUtils.isNoneBlank(actIdGroup.getId())){
			String showId=actIdGroup.getId().replace(String.format(GoupManagerServiceImpl.BODY_TAG, city) + "_", StringUtils.EMPTY);;
			model.addAttribute("showId", showId);
		}
		model.addAttribute("functions", functions);
		model.addAttribute("funcIDList", goupManagerService.findFuncIdsByGroupId(groupid));
		model.addAttribute("actIdGroup", actIdGroup);
		return "u/editRole";
	}
	@RequestMapping(value = "/role_list")
	public String roleList(Model model) {
		return "u/role_list";
	}
	@RequestMapping(value = "/saveRole")
	@ResponseBody
	public Pair<Boolean, String> saveRole(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "rolename", required = false) String rolename,
			@RequestParam(value = "funcode", required = false) String funcode,
			@RequestParam(value = "fundesc", required = false) String fundesc,
			@RequestParam(value = "ids", required = true) String ids
			) throws ParseException{
		return goupManagerService.saveRole(null,ids,rolename,funcode,fundesc,principal,city);
	}
	@RequestMapping(value = "/deleRole/{groupid}")
	@ResponseBody
	public Pair<Boolean, String> deleRole(
			@PathVariable("groupid") String groupid
			) throws ParseException{
		return goupManagerService.deleteGroup(groupid);
	}
	@RequestMapping(value = "/editRole/{groupid}")
	@ResponseBody
	public Pair<Boolean, String> editRole(
			@PathVariable("groupid") String groupid,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "rolename", required = false) String rolename,
			@RequestParam(value = "funcode", required = false) String funcode,
			@RequestParam(value = "fundesc", required = false) String fundesc,
			@RequestParam(value = "ids", required = true) String ids
			) throws ParseException{
		return goupManagerService.editRole(groupid,ids,rolename,funcode,fundesc,principal,city);
	}
	@RequestMapping("ajax-roleList")
	@ResponseBody
	public List<RoleView> roleList(	@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		return goupManagerService.findAllBodyRoles(cityId);
	}
}
