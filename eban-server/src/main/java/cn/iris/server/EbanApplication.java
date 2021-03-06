package cn.iris.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot启动类
 * @author Iris 2022/1/16
 */
@SpringBootApplication
@MapperScan("cn.iris.server.mapper")
@EnableScheduling
public class EbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbanApplication.class, args);
    }
}