package com.jewelry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class JewelryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelryApplication.class, args);
	}

}
