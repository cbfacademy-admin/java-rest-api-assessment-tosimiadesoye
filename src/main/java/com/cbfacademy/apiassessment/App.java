package com.cbfacademy.apiassessment;

import com.cbfacademy.apiassessment.orderEntry.OrderEntryController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = "com.cbfacademy.apiassessment.orderEntry")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}



	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s", name);
	}

}
