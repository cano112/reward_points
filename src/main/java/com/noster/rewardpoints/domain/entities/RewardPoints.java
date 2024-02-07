package com.noster.rewardpoints.domain.entities;

import com.noster.rewardpoints.domain.values.Points;

import java.time.Instant;
import java.util.UUID;

public record RewardPoints(
        UUID id,
        UUID transactionId,
        UUID userId,
        Points points,
        Instant timestamp

) {
    public RewardPoints(UUID transactionId, UUID userId, Points points, Instant timestamp) {
        this(UUID.randomUUID(), transactionId, userId, points, timestamp);
    }
}
