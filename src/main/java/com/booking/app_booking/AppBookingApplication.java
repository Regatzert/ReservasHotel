package com.booking.app_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBookingApplication.class, args);
	}

}
