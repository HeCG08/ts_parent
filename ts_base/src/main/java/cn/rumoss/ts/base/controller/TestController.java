package cn.rumoss.ts.base.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Value("${rumo.target}")
    private String target;

    @RequestMapping(value = "/target", method = RequestMethod.GET)
    public String target(){
        return target;
    }

}
