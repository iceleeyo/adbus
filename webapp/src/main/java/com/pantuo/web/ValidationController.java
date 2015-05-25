package com.pantuo.web;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.UserServiceInter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Ajax validation
 *
 * @author tliu
 */
@Controller
@RequestMapping(value="/validate", produces = "application/json;charset=utf-8")
public class ValidationController {

    @Autowired
    private UserServiceInter userService;

    @RequestMapping(value = "/ajaxValidateUser", method = { RequestMethod.GET})
    @ResponseBody
    public Object[] ajaxValidateUser(@RequestParam("username") String username,
                                    @RequestParam("fieldId") String fieldId,
                                    @RequestParam("fieldValue") String fieldValue) {
        if (username == null || !username.equals(fieldValue)) {
            return new Object[] {fieldId, false, "请输入正确的用户名。"};
        }
        UserDetail u = userService.findByUsername(username);
        if (u == null) {
            return new Object[] {fieldId, true, "用户名可用。"};
        } else {
            return new Object[] {fieldId, false, "用户名已被使用，请更换用户名。"};
        }
    }
    @RequestMapping(value = "/ajaxValidateUserNone", method = { RequestMethod.GET})
    @ResponseBody
    public Object[] ajaxValidateUserNone(@RequestParam("username") String username,
                                    @RequestParam("fieldId") String fieldId,
                                    @RequestParam("fieldValue") String fieldValue) {
        if (username == null || !username.equals(fieldValue)) {
            return new Object[] {fieldId, false, "请输入正确的用户名。"};
        }
        UserDetail u = userService.findByUsername(username);
        if (u == null) {
            return new Object[] {fieldId, false, "用户名不存在,请重新选择。"};
        } else {
            return new Object[] {fieldId, true, "用户选择成功。"};
        }
    }
}
