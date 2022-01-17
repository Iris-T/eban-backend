package cn.iris.server.service.impl;

import cn.iris.server.pojo.Admin;
import cn.iris.server.mapper.AdminMapper;
import cn.iris.server.config.sercurity.JwtTokenUtil;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登陆后返回Token
     * @param userName
     * @param password
     * @param req
     * @return
     */
    @Override
    public RespBean login(String userName, String password, HttpServletRequest req) {
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        // 登录合法性判断
        if (null==userDetails || passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }

        // 更新Security登录用户对象
        Object userDetails1;
        Object principal;
        UsernamePasswordAuthenticationToken authToken = new
                UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 生成Token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return RespBean.success("登陆成功", tokenMap);
    }

    /**
     * 根据username获取Admin信息
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUsername(String username) {

        return adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("username", username)
                        // 用户是否被禁
                        .eq("enabled", true));
    }
}
