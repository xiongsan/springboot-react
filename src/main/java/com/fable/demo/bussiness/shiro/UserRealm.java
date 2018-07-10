package com.fable.demo.bussiness.shiro;

import com.fable.demo.bussiness.mapper.user.UserMapper;
import com.fable.demo.common.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("userRealm")
public class UserRealm extends AuthorizingRealm {
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 提供用户信息返回权限信息
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 根据用户名查询当前用户拥有的角色
        Set<String> roleNames = userMapper.findRoles(username);
        // 将角色名称提供给info
        authorizationInfo.setRoles(roleNames);
        // 根据用户名查询当前用户权限
        Set<String> permissionNames = userMapper.findPermissions(username);
        // 将权限名称提供给info
        authorizationInfo.setStringPermissions(permissionNames);

        return authorizationInfo;
    }

    /**
     * 提供账户信息返回认证信息
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            // 用户名不存在抛出异常
            throw new UnknownAccountException();
        }
        if (user.getLocked() == 1) {
            // 用户被管理员锁定抛出异常
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,
                user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
        return authenticationInfo;
    }
}