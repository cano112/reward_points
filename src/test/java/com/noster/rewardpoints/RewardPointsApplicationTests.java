package com.noster.rewardpoints;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class RewardPointsApplicationTests {

	@Container
	@ServiceConnection
	private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

	@Test
	void contextLoads() {
	}

}
