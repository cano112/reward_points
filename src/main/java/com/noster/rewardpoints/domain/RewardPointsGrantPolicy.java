package com.noster.rewardpoints.domain;

public interface RewardPointsGrantPolicy {

    RewardPoints grant(MonetaryAmount transactionAmount);
}
