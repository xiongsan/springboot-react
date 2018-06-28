package com.fable.demo.bussiness.controller;

import com.fable.demo.common.pojo.User;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.util.ResultKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/6/28
 * Time :9:56
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
@RestController
public class LoginController {

    @RequestMapping("/login")
    public BaseResponse login(@RequestBody User user) throws IOException {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // 捕获密码错误异常
            return ResultKit.fail("password error!");
        } catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
            return ResultKit.fail("username error!");
        } catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
            return ResultKit.fail("fail times a lot error !");
        } catch (LockedAccountException lae) {
            return ResultKit.fail("account was locked !");
        }
        return ResultKit.success();
    }

}
