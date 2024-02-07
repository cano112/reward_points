package com.noster.rewardpoints;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class RewardPointsApplicationTests {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

    private final DSLContext jooq;

    private final MockMvc mockMvc;

    @Autowired
    RewardPointsApplicationTests(DSLContext jooq, MockMvc mockMvc) {
        this.jooq = jooq;
        this.mockMvc = mockMvc;
    }

    @AfterEach
    void clearDb() {
        jooq.truncate(Tables.REWARD_POINTS).execute();
    }

    @Test
    void shouldReturnMonthlyAndTotalRewardPointsSummary_givenTransactionRecordPresent() {
        final var userId = UUID.fromString("fa4deba2-3954-45a0-82c9-582244e1d135");
        givenTransaction(BigDecimal.valueOf(120), Instant.parse("2020-09-01T10:00:00Z"), userId);
        givenTransaction(BigDecimal.valueOf(120), Instant.parse("2020-10-01T10:00:00Z"), userId);
        givenTransaction(BigDecimal.valueOf(100), Instant.parse("2020-10-02T10:00:00Z"), userId);
        givenTransaction(BigDecimal.valueOf(100), Instant.parse("2020-11-01T10:00:00Z"), userId);
        givenTransaction(BigDecimal.valueOf(100), Instant.parse("2020-12-01T10:00:00Z"), userId);

        given()
                .mockMvc(mockMvc)
                .when()
                .get(STR."/users/\{userId}/rewards/points/summary", Map.of(
                        "start", "2020-10-01T00:00:00Z",
                        "end", "2020-12-31:23:59:59Z",
                        "aggregations", "MONTHLY,TOTAL"
                ))
                .then()
                .status(HttpStatus.OK)
                .body(sameJSONAs(
                        //language=JSON
                        """
                                {
                                    "user_id": "fa4deba2-3954-45a0-82c9-582244e1d135",
                                    "period": {
                                        "start": "2020-10-01T00:00:00Z",
                                        "end": "2020-12-31:23:59:59Z"
                                    },
                                    "aggregations": {
                                        "monthly": [
                                            {
                                                "month": 10,
                                                "points": 140
                                            },
                                            {
                                                "month": 11,
                                                "points": 50
                                            },
                                            {
                                                "month": 12,
                                                "points": 50
                                            }
                                        ],
                                        "total": 240
                                    }
                                }
                                    """
                ));

    }

    private void givenTransaction(BigDecimal amount, Instant timestamp, UUID userId) {
        given()
                .mockMvc(mockMvc)
                .when()
                .post(STR."/users/\{userId}/rewards/transactions", STR."""
                        {
                        	"transaction_id": "\{UUID.randomUUID()}",
                        	"transaction_amount": \{amount},
                            "currency": "USD",
                            "timestamp": "\{DateTimeFormatter.ISO_INSTANT.format(timestamp)}"
                        }
                        """)
                .then()
                .status(HttpStatus.CREATED);
    }

}
