package cn.iris.server.controller;

import cn.iris.server.pojo.AdminLoginParam;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
