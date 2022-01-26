package cn.iris.server.utils;

import cn.iris.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工具类
 * @author Iris 2022/1/26
 */
public class AdminUtils {

    /**
     * 获取当前登录操作员
     * @return Admin对象
     */
    public static Admin getCurrentAdmin() {
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
