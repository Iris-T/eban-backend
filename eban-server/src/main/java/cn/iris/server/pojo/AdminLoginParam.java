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
@ApiModel(value = "AdminLogin对象", description = "")
public class AdminLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "密码",required = true)
    private String password;

}
