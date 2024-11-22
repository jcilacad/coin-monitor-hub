package com.projects.coin_monitor_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CoinmonitorhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinmonitorhubApplication.class, args);
	}

}
