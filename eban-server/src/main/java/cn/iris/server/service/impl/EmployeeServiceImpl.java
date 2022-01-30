package cn.iris.server.service.impl;

import cn.iris.server.pojo.Employee;
import cn.iris.server.mapper.EmployeeMapper;
import cn.iris.server.pojo.RespPageBean;
import cn.iris.server.service.IEmployeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询员工
     * @param currentPage 当前页
     * @param pageSize 页面大小
     * @param employee 员工对象
     * @param beginDateScope 入职时间范围
     * @return 公共返回分页对象
     */
    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] beginDateScope) {
        System.out.println(currentPage);
        System.out.println(pageSize);
        System.out.println(employee.toString());
        System.out.println(Arrays.toString(beginDateScope));
        // 开启分页
        Page<Employee> page = new Page<>(currentPage, pageSize);
        IPage employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        RespPageBean pageBean = new RespPageBean(employeeByPage.getTotal(), employeeByPage.getRecords());
        return pageBean;
    }
}
