package cn.iris.server.mapper;

import cn.iris.server.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门信息
     * @return 部门列表
     */
    List<Department> getAllDeps(Integer parentId);

    /**
     * 添加部门
     * @param dep 部门信息
     */
    void addDep(Department dep);

    /**
     * 删除部门
     * @param id 部门ID
     */
    void delDep(Department dep);
}
