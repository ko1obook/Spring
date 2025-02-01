package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.education.model.Formatter;

@RestController
@RequestMapping("api/v1")
public class TestController {

    private final Formatter formatter;

    @Autowired
    public TestController(@Qualifier("fooFormatter") Formatter formatter) {
        this.formatter = formatter;
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello, World!";
    }

    @GetMapping("/format")
    public String getFormat(){
        return formatter.format();
    }
}
