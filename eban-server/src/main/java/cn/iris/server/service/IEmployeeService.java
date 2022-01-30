package cn.iris.server.service;

import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.RespPageBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 查询员工
     * @param currentPage 当前页
     * @param pageSize 页面大小
     * @param employee 员工对象
     * @param beginDateScope 入职时间范围
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] beginDateScope);
}
