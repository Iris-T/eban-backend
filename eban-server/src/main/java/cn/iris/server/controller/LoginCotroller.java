package cn.iris.server.controller;

import cn.iris.server.pojo.Admin;
import cn.iris.server.pojo.AdminLoginParam;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Iris 2022/1/17
 */
@Api(tags = "LoginController")
@RestController
public class LoginCotroller {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录后返回Token")
    @PostMapping("/login")
    public RespBean login(AdminLoginParam authParam, HttpServletRequest req) {
        return adminService.login(authParam.getUserName(), authParam.getPassword(), req);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) {
        if (null==principal) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUsername(username);
        // 密码不返回给前端，防止信息泄露
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("注销成功!");
    }

}