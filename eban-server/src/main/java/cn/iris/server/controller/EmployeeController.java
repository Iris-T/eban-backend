package cn.iris.server.controller;


import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.RespPageBean;
import cn.iris.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "查询员工（分页）")
    @GetMapping("/")
    public RespPageBean getEmployee(
            // 当前Page（默认为1）
            @RequestParam(defaultValue = "1") Integer currentPage,
            // 页面大小（默认为10）
            @RequestParam(defaultValue = "10") Integer pageSize,
            // 筛选参数
            Employee employee,
            LocalDate[] beginDateScope) {
        return employeeService.getEmployeeByPage(currentPage,pageSize,employee,beginDateScope);
    }

}
