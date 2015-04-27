package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.DataInitializationService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserService;
import com.pantuo.util.Pair;

/**
 * <font size=5><b>公交广告交易系统接口</b></font>
 *<P>
 *
 * @author tliu
 *
 */

@Controller
@RequestMapping(value="user", produces = "application/json;charset=utf-8")
public class UserManagerController {
    private static Logger log = LoggerFactory.getLogger(UserManagerController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private DataInitializationService dataService;
    @Autowired
	SuppliesService suppliesService;
    @RequestMapping(value = "/list", method = { RequestMethod.GET})
    public String userlist() {
        return "user_list";
    }

    /**
     * <b>Ajax：获取所有用户</b>
     *
     */
    @RequestMapping(value = "/ajax-list", method = { RequestMethod.GET})
    @ResponseBody
    public DataTablePage<UserDetail> getUsers(TableRequest req) {
        return new DataTablePage(userService.getAllUsers(req.getFilter("name"), req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
    }

    @RequestMapping(value = "/{username}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public UserDetail enableUser(@PathVariable("username") String username,
                              @PathVariable("enable") String enable) {
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
    public static void main(String[] args) {
		System.out.println(1);
	}
    
    @RequestMapping(value = "/invoice", produces = "text/html;charset=utf-8")
    public String invoice(HttpServletRequest request)
    {
        return "invoice_message";
    }
    @RequestMapping(value = "saveInvoice", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveInvoice(JpaInvoice obj, Principal principal, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.addInvoice(obj, principal, request);
	}
    
    @RequestMapping(value = "/qualification", produces = "text/html;charset=utf-8")
    public String qualification(HttpServletRequest request)
    {
        return "qualification_Enter";
    }
    @RequestMapping(value = "savequalifi", method = RequestMethod.POST)
    @ResponseBody
    public Pair<Boolean, String> savequalifi( Principal principal, HttpServletRequest request)
    		throws IllegalStateException, IOException {
    	return suppliesService.savequlifi(principal, request);
    }
    @RequestMapping(value = "/enter", produces = "text/html;charset=utf-8")
    public String enter(Model model,HttpServletRequest request)
    {
    	model.addAttribute("groupsList", DataInitializationService._GROUPS);
        return "u/userEnter";
    }
    
    
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    @ResponseBody
	public UserDetail createProduct(UserDetail detail, HttpServletRequest request) {
    	userService.createUserFromPage(detail);
        return detail;
    }
    
    @RequestMapping(value = "/u_edit/update", method = { RequestMethod.POST})
    @ResponseBody
	public UserDetail updateUser(UserDetail detail, HttpServletRequest request) {
    	userService.updateUserFromPage(detail);
        return detail;
    }
    @RequestMapping(value = "/u/{userId}", method = { RequestMethod.GET})
	public String uDetail(Model model, @PathVariable("userId") String userId,HttpServletRequest request) {
    	model.addAttribute("userDetail", userService.getByUsername(userId));
        return "u/userDetail";
    }
    
    @RequestMapping(value = "/u_edit/{userId}", method = { RequestMethod.GET})
	public String userEdit(Model model, @PathVariable("userId") String userId, HttpServletRequest request) {
		UserDetail UserDetail = userService.getByUsername(userId);
		model.addAttribute("userDetail", UserDetail);
		model.addAttribute("uGroup", userService.getUserGroupList(UserDetail));
		model.addAttribute("groupsList", DataInitializationService._GROUPS);
		return "u/userEdit";
	}
}
