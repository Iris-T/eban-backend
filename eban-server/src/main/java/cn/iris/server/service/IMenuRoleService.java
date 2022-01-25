package cn.iris.server.service;

import cn.iris.server.pojo.MenuRole;
import cn.iris.server.pojo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新权限角色菜单
     * @param rid 权限角色ID
     * @param mids 菜单ID列表
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
