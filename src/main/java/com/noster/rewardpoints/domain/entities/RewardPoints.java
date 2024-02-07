package com.noster.rewardpoints.domain.entities;

import com.noster.rewardpoints.domain.values.Points;

import java.util.UUID;

public record RewardPoints(
        UUID id,
        UUID transactionId,
        UUID userId,
        Points points

) {
    public RewardPoints(UUID transactionId, UUID userId, Points points) {
        this(UUID.randomUUID(), transactionId, userId, points);
    }
}
