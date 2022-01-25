package cn.iris.server.service.impl;

import cn.iris.server.pojo.Admin;
import cn.iris.server.pojo.Menu;
import cn.iris.server.mapper.MenuMapper;
import cn.iris.server.pojo.Role;
import cn.iris.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过全局用户对象获取用户ID
     * 获取菜单列表
     * @return 菜单列表
     */
    @Override
    public List<Menu> getMenuByAdminId() {
        Integer adminID = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 在Redis中获取menus
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_"+adminID);
        // 如果获取到的列表为空则通过访问数据库赋值
        if (CollectionUtils.isEmpty(menus)) {
            menus = menuMapper.getMenuByAdminId(adminID);
            // 将数据设置到Redis中
            valueOperations.set("menu_"+adminID, menus);
        }
        return menus;
    }

    /**
     * 根据角色获取角色权限列表
     * @return 权限列表
     */
    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    /**
     * 获取权限对应的所有菜单
     * @return 菜单列表
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
