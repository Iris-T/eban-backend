package cn.iris.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码接口
 * @author Iris 2022/1/18
 */
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha", produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {

        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers（use addHeader）
        response.addHeader("Cache-control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 nocache header
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //============生成验证码【Begin】============|
        // 获取验证码文本
        String text = defaultKaptcha.createText();
        System.out.println(" 验证码内容->"+text);

        // 将验证码放入Session中
        request.getSession().setAttribute("captcha", text);
        // 根据文本生成图片
        BufferedImage image = defaultKaptcha.createImage(text);

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 通过输出流输出jpg图片
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null!=outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //============生成验证码【End】==============|
    }
}
