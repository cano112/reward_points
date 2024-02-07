package com.noster.rewardpoints.infra.rest;

import com.noster.rewardpoints.api.Api;
import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.usecases.AcceptTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.BDDMockito.then;

@WebMvcTest(value = AcceptTransactionController.class)
@Import({
        LogbookAutoConfiguration.class,
})
class AcceptTransactionControllerTest {

    private static final UUID USER_ID = UUID.fromString("f6941dd8-6e08-4c56-b945-34d17112e0e2");
    private static final UUID TRANSACTION_ID = UUID.fromString("f6941dd8-6e08-4c56-b945-34d17112e0e3");

    private final Api api;

    @MockBean
    private AcceptTransaction acceptTransaction;

    @Autowired
    AcceptTransactionControllerTest(MockMvc mockMvc) {
        this.api = new Api(mockMvc);
    }

    @Test
    void shouldAcceptTransaction() {
        api.postTransaction(
                USER_ID,
                BigDecimal.valueOf(120),
                Instant.parse("2020-10-10T10:00:00Z"),
                TRANSACTION_ID
        ).then().status(HttpStatus.NO_CONTENT);

        final var expectedRequest = new AcceptTransaction.Request(
                TRANSACTION_ID,
                new MonetaryAmount(BigDecimal.valueOf(120)),
                USER_ID,
                Instant.parse("2020-10-10T10:00:00Z")
        );
        then(acceptTransaction).should().with(expectedRequest);
    }

    @Test
    void shouldReturnError_givenUserIdNotUUID() {
        api.postTransaction(
                        "not-uuid",
                        BigDecimal.valueOf(120),
                        Instant.parse("2020-10-10T10:00:00Z"),
                        TRANSACTION_ID
                ).then()
                .status(HttpStatus.BAD_REQUEST)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenTransactionIdNotUUID() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                	"transaction_id": "not-uuid",
                                	"transaction_amount": 120,
                                    "timestamp": "2020-10-10T10:00:00Z"
                                }
                                """
                ).then()
                .status(HttpStatus.BAD_REQUEST)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenMissingTransactionId() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                	"transaction_amount": 120,
                                    "timestamp": "2020-10-10T10:00:00Z"
                                }
                                """
                )
                .then()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenMissingTransactionAmount() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                    "transaction_id": "f6941dd8-6e08-4c56-b945-34d17112e0e2",
                                    "timestamp": "2020-10-10T10:00:00Z"
                                }
                                """
                )
                .then()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenMissingTimestamp() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                    "transaction_id": "f6941dd8-6e08-4c56-b945-34d17112e0e2",
                                    "transaction_amount": 120
                                }
                                """
                )
                .then()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenTransactionAmountLessThan0() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                    "transaction_id": "f6941dd8-6e08-4c56-b945-34d17112e0e2",
                                    "transaction_amount": -4,
                                    "timestamp": "2020-10-10T10:00:00Z"
                                }
                                """
                )
                .then()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenTimestampNotIso8601() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                    "transaction_id": "f6941dd8-6e08-4c56-b945-34d17112e0e2",
                                    "transaction_amount": -4,
                                    "timestamp": "2020-01-01 10:00:00"
                                }
                                """
                )
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .contentType("application/problem+json");
    }

    @Test
    void shouldReturnError_givenMalformedJson() {
        api.postTransaction(
                        USER_ID,
                        """
                                {
                                    "transaction_id": "f6941dd8-
                                }
                                """
                )
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .contentType("application/problem+json");
    }
}
