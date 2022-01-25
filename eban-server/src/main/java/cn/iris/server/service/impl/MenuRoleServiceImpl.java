package cn.iris.server.service.impl;

import cn.iris.server.pojo.MenuRole;
import cn.iris.server.mapper.MenuRoleMapper;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新权限角色菜单
     * 由于菜单数量较大，若做增删判断则代价较大
     * 所以采取两步实现，先全部删除，再添加菜单权限
     * @param rid  权限角色ID
     * @param mids 菜单ID列表
     * @return 自定义消息
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        // 清空权限菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        // 添加权限菜单
        if (null==mids || 0==mids.length) {
            // 若mids为空，删除权限菜单返回成功响应信息
            return RespBean.success("更新成功!");
        }
        // 不为空则进行批量更新
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if (result== mids.length) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
