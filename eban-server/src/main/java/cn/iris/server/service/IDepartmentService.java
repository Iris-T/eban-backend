package cn.iris.server.service;

import cn.iris.server.pojo.Department;
import cn.iris.server.pojo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门信息
     * @return 部门列表
     */
    List<Department> getAllDeps();

    /**
     * 添加部门
     * @param dep 部门信息
     * @return 自定义响应信息
     */
    RespBean addDep(Department dep);

    /**
     * 删除部门
     * @param id 部门ID
     * @return 自定义响应信息
     */
    RespBean delDep(Integer id);
}
