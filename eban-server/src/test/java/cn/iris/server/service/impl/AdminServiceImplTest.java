package cn.iris.server.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

/**
 * @author Iris 2022/1/18
 */
public class AdminServiceImplTest {

    @Test
    public void codeCheckTest() {
        System.out.println(!StringUtils.hasText("code") || !"captcha".equalsIgnoreCase("code"));
    }
}
