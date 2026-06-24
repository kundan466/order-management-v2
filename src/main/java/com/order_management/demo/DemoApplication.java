package com.order_management.demo;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		 System.out.println("Timezone = " + TimeZone.getDefault());
        System.out.println("ZoneId = " + ZoneId.systemDefault());
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("welcome to order management system");
		log.info("application starts");
		// System.out.println(TimeZone.getDefault());
		// System.out.println(System.getProperty("user.timezone"));
	}

}
