package com.fable.demo.bussiness.service.userService;

import com.fable.demo.bussiness.mapper.user.UserMapper;
import com.fable.demo.bussiness.shiro.UserRealm;
import com.fable.demo.common.PasswordHelper;
import com.fable.demo.common.pojo.User;
import com.fable.enclosure.bussiness.interfaces.BaseRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.service.impl.BaseServiceImpl;
import com.fable.enclosure.bussiness.util.ResultKit;
import com.fable.enclosure.bussiness.util.Tool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/7/4
 * Time :17:21
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
@Service
public class UserService extends BaseServiceImpl {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRealm userRealm;

    private Logger logger =  LoggerFactory.getLogger(UserService.class);

    public BaseResponse login(User request) {
        UsernamePasswordToken token = new UsernamePasswordToken(request.getLoginName(), request.getPassword());
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

    public BaseResponse logout() {
        SecurityUtils.getSubject().logout();
        return ResultKit.success();
    }

    public BaseResponse register(User request) {
        User user = PasswordHelper.encryptPassword(request);
        userMapper.addUser(user);
        return ResultKit.success();
    }

    public BaseResponse checkUniqueUser(User request) {
        User user = userMapper.findByUsername(request.getLoginName());
        //查看权限
//        AuthorizationInfo info = userRealm.doGetAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
//        logger.error(""+ info.getRoles());
//        logger.error(""+info.getStringPermissions());
        if (user != null)
            return ResultKit.success();
        // TODO: 2018/7/4
        return ResultKit.fail();
    }

}
