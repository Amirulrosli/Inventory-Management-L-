package com.nep.itn08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.nep"})
public class NepAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NepAssignmentApplication.class, args);
	}

}
