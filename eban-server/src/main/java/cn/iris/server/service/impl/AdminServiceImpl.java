package cn.iris.server.service.impl;

import cn.iris.server.mapper.AdminRoleMapper;
import cn.iris.server.mapper.RoleMapper;
import cn.iris.server.pojo.Admin;
import cn.iris.server.mapper.AdminMapper;
import cn.iris.server.config.component.JwtTokenUtil;
import cn.iris.server.pojo.AdminRole;
import cn.iris.server.pojo.RespBean;
import cn.iris.server.pojo.Role;
import cn.iris.server.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iris.server.utils.AdminUtils.getCurrentAdmin;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Iris
 * @since 2022-01-19
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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest req) {
        // 获取Session中的code
        String captcha = (String) req.getSession().getAttribute("captcha");
        // 字符串为空（null or "" " "）或验证码不匹配
        if (ObjectUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码输入错误，请重新输入!");
        }
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 登录合法性判断
        if (null==userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }

        // 更新Security登录用户对象
        UsernamePasswordAuthenticationToken authToken = new
                UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 生成Token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据username获取Admin信息
     * @param username 用户名
     * @return Admin对象
     */
    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("username", username)
                        // 用户是否被禁
                        .eq("enabled", true));
    }

    /**
     * 根据用户ID查询角色列表
     * @param adminId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取全部操作员
     * @param keywords 关键词
     * @return 操作员列表
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(getCurrentAdmin().getId(), keywords);
    }

    /**
     * 删除adminId对应的全部角色
     * @param adminId 操作员ID
     */
    @Override
    public void delAdminRole(Integer adminId) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
    }

    /**
     * 更新操作员角色
     * @param adminId 操作员ID
     * @param rids 角色ID列表
     * @return 自定义响应信息
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        // 先将操作员对应角色全部删除
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        // 可能遇到删除全部权限，获取到的rids为null
        if (ObjectUtils.isEmpty(rids)) {
            return RespBean.success("更新成功!");
        }
        // 添加操作员对应角色
        Integer res = adminRoleMapper.addAdminRole(adminId, rids);
        //更新行数相同则成功
        if (rids.length==res) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**
     * 更新当前用户密码
     * @param oldPass 旧密码
     * @param pass 新密码
     * @param adminId 用户ID
     * @return 自定义响应信息
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(encoder.encode(pass));
            int res = adminMapper.updateById(admin);
            if (1 == res) {
                return RespBean.success("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }
}
