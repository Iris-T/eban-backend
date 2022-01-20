package cn.iris.server.service;

import cn.iris.server.pojo.Menu;
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
public interface IMenuService extends IService<Menu> {


    /**
     * 通过全局用户对象获取用户ID
     * 获取菜单列表
     * @return 菜单列表
     */
    List<Menu> getMenuByAdminId();

}
