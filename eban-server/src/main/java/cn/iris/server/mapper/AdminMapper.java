package cn.iris.server.mapper;

import cn.iris.server.pojo.Admin;
import cn.iris.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有操作员
     * @param id 当前登录用户ID
     * @param keywords 关键词
     * @return 操作员列表
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);
}
