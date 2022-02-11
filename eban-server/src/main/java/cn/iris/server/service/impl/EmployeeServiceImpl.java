package cn.iris.server.service.impl;

import cn.iris.server.pojo.Employee;
import cn.iris.server.mapper.EmployeeMapper;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.pojo.RespPageBean;
import cn.iris.server.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        // 开启分页
        Page<Employee> page = new Page<>(currentPage, pageSize);
        IPage<Employee> employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        return new RespPageBean(employeeByPage.getTotal(), employeeByPage.getRecords());
    }

    /**
     * 获取最大工号
     * @return 自定义响应信息>>>最大工号
     */
    @Override
    public RespBean maxWorkID() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        String res = String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1);
        return RespBean.success(null, res);
    }

    /**
     * 添加员工
     * @param emp 员工对象
     * @return 自定义响应信息
     */
    @Override
    public RespBean addEmp(Employee emp) {
        // 做员工合同期限的计算
        LocalDate beginContract = emp.getBeginContract();
        LocalDate endContract = emp.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        emp.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));

        if (1==employeeMapper.insert(emp)) {
            /*发送信息*/
            Employee empNew = employeeMapper.getEmp(emp.getId()).get(0);
            rabbitTemplate.convertAndSend("mail.welcome", empNew);
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    /**
     * 获取员工信息列表
     * @param id 员工ID
     * @return 员工信息列表
     */
    @Override
    public List<Employee> getEmp(Integer id) {
        return employeeMapper.getEmp(id);
    }
}
