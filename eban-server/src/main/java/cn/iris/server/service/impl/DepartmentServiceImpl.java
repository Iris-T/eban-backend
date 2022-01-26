package cn.iris.server.service.impl;

import cn.iris.server.pojo.Department;
import cn.iris.server.mapper.DepartmentMapper;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 获取所有部门信息
     * @return 部门列表
     */
    @Override
    public List<Department> getAllDeps() {
        return departmentMapper.getAllDeps(-1);
    }

    /**
     * 添加部门
     * @param dep 部门信息
     * @return 自定义相应信息
     */
    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if (1==dep.getResult()) {
            return RespBean.success("添加成功!", dep);
        }
        return RespBean.error("添加失败!");
    }

    /**
     * 删除部门
     * @param id 部门ID
     * @return 自定义响应信息
     */
    @Override
    public RespBean delDep(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.delDep(dep);
        switch (dep.getResult()) {
            case -2: return RespBean.error("该部门下有子部门，删除失败!");
            case -1: return RespBean.error("该部门下还有员工，删除失败!");
            case 1: return RespBean.success("删除成功!");
            default: return RespBean.error("删除失败");
        }
    }
}
