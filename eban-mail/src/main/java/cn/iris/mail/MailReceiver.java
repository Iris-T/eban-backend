package cn.iris.mail;

import cn.iris.server.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 消息接收器
 * @author Iris 2022/2/11
 */
@Component
public class MailReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private TemplateEngine templateEngine;

    @RabbitListener(queues = "mail.welcome")
    public void handler(Employee employee) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(msg);
        try {
            /*发件人*/
            messageHelper.setFrom(mailProperties.getUsername());
            /*收件人*/
            messageHelper.setTo(employee.getEmail());
            /*主题*/
            messageHelper.setSubject("入职欢迎邮件（无需回复）");
            /*发送日期*/
            messageHelper.setSentDate(new Date());
            /*邮件内容*/
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("depName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            /*以HTML形式发出*/
            messageHelper.setText(mail, true);
            /*发送*/
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            LOGGER.error("邮件发送失败,失败原因:{}", e.getMessage());
        }
    }
}
