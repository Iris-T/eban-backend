package cn.iris.mail;

import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
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
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            // 检查缓存中是否存在msgId，存在则已发送，无需操作
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                LOGGER.info("消息已经被发送>>>>>{}", msgId);
                /*
                  手动确认消息
                  tag：消息序号
                  multiple：是否确认多条
                 */
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(msg);
            /*发件人*/ {
                messageHelper.setFrom(mailProperties.getUsername());
            }
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
            LOGGER.info(">>>>>邮件(序号：{})发送成功<<<<<", msgId);
            // 将msgId存入缓存
            hashOperations.put("mail_log", msgId, "OK");
            // 手动确认
            channel.basicAck(tag, false);
        } catch (Exception e) {
            /*
             * 手动确认消息
             * tag：消息序号
             * multiple：是否确认多条
             * requeue：是否退回到消息队列
             */
            try {
                channel.basicNack(tag, false, true);
                LOGGER.error("邮件发送失败,失败原因:{}", e.getMessage());
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败,失败原因:{}", e.getMessage());
            }
        }
    }
}
