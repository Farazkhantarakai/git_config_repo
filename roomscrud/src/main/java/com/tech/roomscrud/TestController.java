package com.tech.roomscrud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;


@RestController
public class TestController  {


   @Value("${welcome_data}")
     private String name;

//    @Autowired
//private Environment environment;



    @GetMapping("/get")
    public String getValue(){
        return name;
    }

}
