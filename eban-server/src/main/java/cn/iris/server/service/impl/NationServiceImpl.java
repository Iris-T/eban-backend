package cn.iris.server.service.impl;

import cn.iris.pojo.Nation;
import cn.iris.mapper.NationMapper;
import cn.iris.server.service.INationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-16
 */
@Service
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
