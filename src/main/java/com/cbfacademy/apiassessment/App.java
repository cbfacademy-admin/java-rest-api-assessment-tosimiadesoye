package com.cbfacademy.apiassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = "com.cbfacademy.apiassessment")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }


}
