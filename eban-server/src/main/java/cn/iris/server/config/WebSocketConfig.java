package cn.iris.server.config;

import cn.iris.server.config.component.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket配置类
 * @author Iris 2022/2/15
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 添加端点Endpoint，使得网页可通过websocket连接服务
     * 即是前端可通过socket JS连接至配置的服务地址
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
         * 1. 将ws/ep路径注册为tomp的端点
         * 用户连接后即可进行websocket通讯（支持socket JS）
         * 2. setAllowedOrigins("*")：允许跨域
         * 3. withSockJS()：支持socket JS访问
         */
        registry.addEndpoint("/ws/ep")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 正常情况websocket无需配置
     * 在使用jwt后需要配置
     * 输入通道参数配置
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // 判断是否为连接，是>>获取Token并设置用户对象
                if(StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    // 验证token是否为空
                    if (!ObjectUtils.isEmpty(token)) {
                        String authToken = token.substring(tokenHead.length());
                        String username = jwtTokenUtil.getUserNameByToken(authToken);
                        // 判断用户名
                        if(!ObjectUtils.isEmpty(username)) {
                            // 登录
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            // 验证Token是否有效
                            if(jwtTokenUtil.validateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                accessor.setUser(authenticationToken);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置代理域，可配置多个
        // 配置代理目的地前缀为/queue，可在配置域上向客户端推送信息
        registry.enableSimpleBroker("/queue");
    }
}
