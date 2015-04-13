package com.pantuo.web;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.pojo.TableRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.service.UserService;

/**
 * <font size=5><b>公交广告交易系统接口</b></font>
 *<P>
 *
 * @author tliu
 *
 */

@Controller
@RequestMapping(value="user", produces = "application/json;charset=utf-8")
public class AdbusController {
    private static Logger log = LoggerFactory.getLogger(AdbusController.class);

    @Autowired
    private UserService userService;

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
}
