package cn.iris.server.mapper;

import cn.iris.server.pojo.AdminRole;
import cn.iris.server.pojo.RespBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 添加操作员角色
     * @param adminId 操作员ID
     * @param rids 角色ID列表
     * @return 自定义响应信息
     */
    Integer addAdminRole(Integer adminId, Integer[] rids);
}
