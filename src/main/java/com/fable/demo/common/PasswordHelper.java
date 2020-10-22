package com.fable.demo.common;

import com.fable.demo.common.pojo.User;
import com.fable.enclosure.bussiness.util.Tool;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/6/26
 * Time :11:46
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class PasswordHelper {
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static final String algorithmName = "md5";
    private static final int hashIterations = 2;

    public static User encryptPassword(User user) {
        // User对象包含最基本的字段Username和Password
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        // 将用户的注册密码经过散列算法替换成一个不可逆的新密码保存进数据，散列过程使用了盐
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                user.getCredentialsSalt(), hashIterations).toHex();
        user.setPassword(newPassword);
        user.setId(Tool.newGuid());
        return user;
    }

}