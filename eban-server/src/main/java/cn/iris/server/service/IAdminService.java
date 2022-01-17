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
 * @since 2022-01-16
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登陆后放回Token
     * @param userName
     * @param password
     * @param req
     * @return
     */
    RespBean login(String userName, String password, HttpServletRequest req);

    /**
     * 通过username获取Admin信息
     * @param username
     * @return
     */
    Admin getAdminByUsername(String username);
}
