package cn.iris.server.service.impl;

import cn.iris.server.pojo.Oplog;
import cn.iris.server.mapper.OplogMapper;
import cn.iris.server.service.IOplogService;
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
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
