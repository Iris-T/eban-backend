package cn.iris.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户登录实体类
 * @author Iris 2022/1/17
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "AdminLogin对象", description = "登录信息对象")
public class AdminLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;

}
