package cn.iris.server.service;

import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.pojo.RespPageBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * 获取最大工号
     * @return 自定义响应信息>>>最大工号
     */
    RespBean maxWorkID();

    /**
     * 添加员工
     * @param emp 员工对象
     * @return 自定义响应信息
     */
    RespBean addEmp(Employee emp);

    /**
     * 获取员工数据，用于导出
     * @param id 员工ID
     * @return 员工信息列表
     */
    List<Employee> getEmp(Integer id);
}
