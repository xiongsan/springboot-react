package com.fable.demo.bussiness.mapper.user;


import com.fable.demo.common.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/3/30
 * Time :11:27
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public interface UserMapper {
    Set<String> findRoles(@Param("loginName") String loginName);
    Set<String> findPermissions(@Param("loginName") String loginName);
    User findByUsername(@Param("loginName") String loginName);
}
