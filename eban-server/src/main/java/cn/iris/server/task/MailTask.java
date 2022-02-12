package cn.iris.server.task;

import cn.iris.server.pojo.Employee;
import cn.iris.server.pojo.MailConstants;
import cn.iris.server.pojo.MailLog;
import cn.iris.server.service.IEmployeeService;
import cn.iris.server.service.IMailLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送 -- 定时任务
 * @author Iris 2022/2/12
 */
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
      邮件发送定时任务
      每10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * * ?")
    public void mailTask() {
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                // 查询发送失败的消息
                .eq("status", 0)
                // 尝试时间未失效的消息
                .lt("tryTime", LocalDateTime.now()));

        list.forEach(mailLog -> {
            // 重试次数超过三次，视为投递失败，不再重试
            if (3 <= mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status",2)
                        .eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count", mailLog.getCount()+1)
                    .set("updateTime", LocalDateTime.now())
                    // 更新尝试时间
                    .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId", mailLog.getMsgId()));
            Employee emp = employeeService.getEmp(mailLog.getEid()).get(0);
            // 发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(mailLog.getMsgId()));
        });
    }
}
