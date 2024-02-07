package com.noster.rewardpoints.infra.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.usecases.AcceptTransaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
public class AcceptTransactionController {

    private final AcceptTransaction acceptTransaction;

    public AcceptTransactionController(AcceptTransaction acceptTransaction) {
        this.acceptTransaction = acceptTransaction;
    }

    @PostMapping("/users/{userId}/rewards/transactions")
    ResponseEntity<Void> postTransaction(
            @PathVariable UUID userId,
            @Valid @RequestBody Request request
    ) {
        acceptTransaction.with(new AcceptTransaction.Request(
                request.transactionId,
                new MonetaryAmount(request.transactionAmount),
                userId,
                request.timestamp
        ));
        return ResponseEntity.noContent().build();
    }

    record Request(
            @JsonProperty("transaction_id") @NotNull UUID transactionId,
            @JsonProperty("transaction_amount") @NotNull @Min(0) BigDecimal transactionAmount,
            @JsonProperty("timestamp") @NotNull Instant timestamp
    ) {
    }
}
