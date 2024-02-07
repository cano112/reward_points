package com.noster.rewardpoints.api;

import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class Api {

    private final MockMvc mockMvc;

    public Api(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public MockMvcResponse postTransaction(String userId, BigDecimal amount, Instant timestamp, UUID transactionId) {
        return postTransaction(userId, STR."""
                        {
                        	"transaction_id": "\{transactionId}",
                        	"transaction_amount": \{amount},
                            "timestamp": "\{DateTimeFormatter.ISO_INSTANT.format(timestamp)}"
                        }
                        """);
    }

    public MockMvcResponse postTransaction(UUID userId, BigDecimal amount, Instant timestamp) {
        return postTransaction(UUID.randomUUID(), amount, timestamp, userId);
    }

    public MockMvcResponse postTransaction(UUID userId, BigDecimal amount, Instant timestamp, UUID transactionId) {
        return postTransaction(userId.toString(), amount, timestamp, transactionId);
    }

    public MockMvcResponse postTransaction(String userId, String requestBody) {
        return given()
                .mockMvc(mockMvc)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(STR."/users/\{userId}/rewards/transactions");
    }

    public MockMvcResponse postTransaction(UUID userId, String requestBody) {
        return postTransaction(userId.toString(), requestBody);
    }

    public MockMvcResponse getSummary(UUID userId, Map<String, String> queryParams) {
        return given()
                .mockMvc(mockMvc)
                .when()
                .get(STR."/users/\{userId}/rewards/points/summary", queryParams);
    }
}
