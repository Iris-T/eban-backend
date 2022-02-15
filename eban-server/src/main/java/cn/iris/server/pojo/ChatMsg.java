package cn.iris.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 聊天信息实体类
 * @author Iris 2022/2/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatMsg {

    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String fromNiceName;
}
