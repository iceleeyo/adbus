package com.pantuo.web;

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.User;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <font size=5><b>公交广告交易系统接口</b></font>
 *<P>
 *
 * @author tliu
 *
 */

@Controller
@RequestMapping(produces = "application/json;charset=utf-8")
public class AdbusController {
    private static Logger log = LoggerFactory.getLogger(AdbusController.class);

    @Autowired
    private UserService userService;

    /**
     * <b>测试Ajax：获取所有用户</b>
     *
     */
    @RequestMapping(value = "/user/list", method = { RequestMethod.GET})
    @ResponseBody
    public DataTablePage<User> getUsers(
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "length", required = false, defaultValue = "10") int length,
            @RequestParam(value = "draw", required = false, defaultValue = "1") int draw) {
        if (length < 1)
            length = 1;
        return new DataTablePage(userService.getAllUsers(start/length, length), draw);
    }

    @RequestMapping(value = "/user/{username}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public User enableUser(@PathVariable("username") String username,
                              @PathVariable("enable") String enable) {
        boolean en = "enable".equals(enable);
        List<User> users = userService.getUserRepo().findByUsername(username);
        if (users.size() != 1) {
            log.warn("enableUser(): find {} users for username {}", users.size(), username);
            User u = new User();
            u.setErrorInfo(BaseEntity.ERROR, "找到" + users.size() + "个用户");
            return u;
        }

        if (users.get(0).isEnabled() != en) {
            users.get(0).setEnabled(en);
            userService.getUserRepo().save(users.get(0));
        }
        return users.get(0);
    }
}
