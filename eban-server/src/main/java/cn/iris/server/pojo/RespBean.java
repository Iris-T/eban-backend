package cn.iris.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共响应类
 * @author Iris 2022/1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * @param message 返回响应信息
     */
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    /**
     * 成功返回结果
     * @param message 响应信息
     * @param obj 响应对象
     * @return 返回响应信息和对象
     */
    public static RespBean success(String message, Object obj) {
        return new RespBean(200, message, obj);
    }

    /**
     * 失败返回结果
     * @param message 响应信息
     * @return 返回响应信息
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    /**
     * 失败返回结果
     * @param message 响应信息
     * @param obj 响应对象
     * @return 返回响应信息和对象
     */
    public static RespBean error(String message, Object obj) {
        return new RespBean(500, message, obj);
    }
}
