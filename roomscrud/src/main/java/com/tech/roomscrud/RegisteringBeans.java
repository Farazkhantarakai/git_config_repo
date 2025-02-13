package com.tech.roomscrud;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisteringBeans {

@Bean
public Environment getEnvirment(){
    return new Environment("dev");
}

}
