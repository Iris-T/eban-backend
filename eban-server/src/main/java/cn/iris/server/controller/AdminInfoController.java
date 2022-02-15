package cn.iris.server.controller;

import cn.iris.server.pojo.Admin;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 个人中心 前端控制器
 * @author Iris 2022/2/15
 */
@RestController
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        // 添加密码验证，防止被抓包进行恶意修改信息
        if (ObjectUtils.isEmpty(admin.getPassword()) && adminService.updateById(admin)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**
     * 更新当前用户密码
     * @param info info：map类型，包含新旧密码
     * @return 自定义响应信息
     */
    @ApiOperation(value = "更新密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }
}
