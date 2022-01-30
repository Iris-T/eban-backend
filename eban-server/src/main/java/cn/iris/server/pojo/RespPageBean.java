package cn.iris.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 * @author Iris 2022/1/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {

    /**
     * 总长度
     */
    private Long total;
    /**
     * 数据
     */
    private List<?> data;
}
