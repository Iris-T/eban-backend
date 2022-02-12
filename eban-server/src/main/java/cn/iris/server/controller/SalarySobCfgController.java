package cn.iris.server.controller;

import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.pojo.RespPageBean;
import cn.iris.server.pojo.Salary;
import cn.iris.server.service.IEmployeeService;
import cn.iris.server.service.ISalaryService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工账套前端控制器
 * @author Iris 2022/2/12
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有员工工资账套")
    @GetMapping("/salaries")
    public List<Salary> getSalaries() {
        return salaryService.list();
    }

    @ApiOperation(value = "添加所有员工账套（分页）")
    @GetMapping("/")
    public RespPageBean getEmpWithSalary(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        return employeeService.getEmpWithSalary(currentPage, size);
    }

    @ApiOperation(value = "更新员工账套")
    @PutMapping("/")
    public RespBean updateEmpSalary(Integer eid, Integer sid) {
        if(employeeService.update(new UpdateWrapper<Employee>()
                .set("salaryId", sid)
                .eq("id", eid))) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
