package com.noster.rewardpoints;

import org.springframework.boot.SpringApplication;

public class RewardPointsTestApplication {

	public static void main(String[] args) {
		SpringApplication.from(RewardPointsApplication::main).with(PostgresConfiguration.class).run(args);
	}

}
