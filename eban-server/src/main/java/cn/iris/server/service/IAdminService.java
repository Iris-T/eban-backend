package cn.iris.server.service;

import cn.iris.server.pojo.Admin;
import cn.iris.server.pojo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录后返回Token
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param req request
     * @return JwtToken
     */
    RespBean login(String username, String password, String code, HttpServletRequest req);

    /**
     * 通过username获取Admin信息
     * @param username 用户名
     * @return Admin对象
     */
    Admin getAdminByUsername(String username);
}
