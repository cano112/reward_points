package com.noster.rewardpoints.domain.values;

import java.math.BigDecimal;

public record MonetaryAmount(BigDecimal amount) {

    public MonetaryAmount(double amount) {
        this(BigDecimal.valueOf(amount));
    }
}
