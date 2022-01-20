package cn.iris.server.controller;


import cn.iris.server.pojo.Menu;
import cn.iris.server.service.IAdminService;
import cn.iris.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户ID查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenuByAdminId() {
        return menuService.getMenuByAdminId();
    }

}
