package cn.iris.server.mapper;

import cn.iris.server.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询员工
     * @param page mybatisplus分页对象
     * @param employee 员工对象
     * @param beginDateScope 入职时间范围
     * @return IPage
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee,@Param("beginDateScope") LocalDate[] beginDateScope);

    /**
     * 获取员工信息列表
     * @param id 员工ID
     * @return 员工信息列表
     */
    List<Employee> getEmp(Integer id);

    /**
     * 获取所有员工账套（分页）
     * @param page 页面
     * @return IPage
     */
    IPage<Employee> getEmpWithSalary(Page<Employee> page);
}
