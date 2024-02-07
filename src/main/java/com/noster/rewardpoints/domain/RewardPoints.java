package com.noster.rewardpoints.domain;

import java.util.UUID;

public record RewardPoints(
        UUID id,
        UUID transactionId,
        UUID userId,
        int points
) {
}
