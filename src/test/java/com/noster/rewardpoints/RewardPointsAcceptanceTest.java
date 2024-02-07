package com.noster.rewardpoints;

import com.noster.rewardpoints.api.Api;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(PostgresConfiguration.class)
class RewardPointsAcceptanceTest {

    private final DSLContext jooq;

    private final Api api;

    @Autowired
    RewardPointsAcceptanceTest(DSLContext jooq, MockMvc mockMvc) {
        this.jooq = jooq;
        this.api = new Api(mockMvc);
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

        api.getSummary(
                        userId,
                        Map.of(
                                "start", "2020-10-01T00:00:00Z",
                                "end", "2020-12-31T23:59:59Z",
                                "group_by", "MONTH"
                        )
                )
                .then()
                .status(HttpStatus.OK)
                .body(sameJSONAs(
                        //language=JSON
                        """
                                {
                                    "points_summary": {
                                        "grouped": {
                                            "by": "MONTH",
                                            "entries": [
                                                {
                                                    "group": 10,
                                                    "points": 140
                                                },
                                                {
                                                    "group": 11,
                                                    "points": 50
                                                },
                                                {
                                                    "group": 12,
                                                    "points": 50
                                                }
                                            ]
                                        },
                                        "total": {
                                            "points": 240
                                        }
                                    }
                                }
                                    """
                ));

    }

    private void givenTransaction(BigDecimal amount, Instant timestamp, UUID userId) {
        api.postTransaction(userId, amount, timestamp)
                .then()
                .status(HttpStatus.NO_CONTENT);
    }

}
