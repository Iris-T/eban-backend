package cn.iris.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Iris 2022/1/20
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/emp/basic/hello")
    public String hello2() {
        return "hello";
    }

    @GetMapping("/emp/advanced/hello")
    public String hello3() {
        return "hello";
    }
}
