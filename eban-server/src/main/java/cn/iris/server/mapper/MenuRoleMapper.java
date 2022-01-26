package cn.iris.server.mapper;

import cn.iris.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 插入多条权限菜单
     * @param rid 权限角色ID
     * @param mids 菜单ID列表
     * @return 受影响结果数
     */
    Integer insertRecord(@Param("rid") Integer rid, Integer[] mids);
}
