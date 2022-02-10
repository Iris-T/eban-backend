package cn.iris.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.iris.server.pojo.*;
import cn.iris.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IDepartmentService departmentService;

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
        return employeeService.getEmployeeByPage(currentPage, pageSize, employee, beginDateScope);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getAllPoliticsStatus() {
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJoblevels() {
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDeps();
    }

    @ApiOperation(value = "获取最大工号")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        return employeeService.maxWorkID();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee emp) {
        return employeeService.addEmp(emp);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateById(employee)) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean delEmp(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmp(HttpServletResponse resp) {
        List<Employee> infoList = employeeService.getEmp(null);
        /*使用03版本Excel，兼容性&导出速度*/
        ExportParams exportParams = new ExportParams("员工信息表", "员工信息表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, infoList);
        ServletOutputStream out = null;
        try {
            /*以流的形式输出*/
            resp.setHeader("content-type", "application/octet-stream");
            /*防止中文乱码*/
            resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("员工信息表.xls", "utf-8"));
            out = resp.getOutputStream();
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*关闭资源*/
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping(value = "/import")
    public RespBean importEmp(MultipartFile file) {
        ImportParams params = new ImportParams();
        /*删除标题行（首行）*/
        params.setTitleRows(1);
        List<Nation> nations = nationService.list();
        List<PoliticsStatus> politicsStatuses = politicsStatusService.list();
        List<Department> departments = departmentService.list();
        List<Joblevel> joblevels = joblevelService.list();
        List<Position> positions = positionService.list();
        try {
            List<Employee> empInfos = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            empInfos.forEach(employee -> {
                        /*获取民族ID*/
                        employee.setNationId(nations.get(nations.indexOf(new Nation(employee.getNation().getName()))).getId());
                        /*获取政治面貌ID*/
                        employee.setPoliticId(politicsStatuses.get(politicsStatuses.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                        /*获取部门ID*/
                        employee.setDepartmentId(departments.get(departments.indexOf(new Department(employee.getDepartment().getName()))).getId());
                        /*获取职称ID*/
                        employee.setJobLevelId(joblevels.get(joblevels.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                        /*获取职位ID*/
                        employee.setPosId(positions.get(positions.indexOf(new Position(employee.getPosition().getName()))).getId());
                    });
            if (employeeService.saveBatch(empInfos)) {
                return RespBean.success("导入成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}
