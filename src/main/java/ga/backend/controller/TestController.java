package ga.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/test")
    public void test() {
        System.out.println("테스트!");
    }

    @GetMapping("/test2")
    @ResponseBody
    public String test2() {
        return "test2";
    }
}
