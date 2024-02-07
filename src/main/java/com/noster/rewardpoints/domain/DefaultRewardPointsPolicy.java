package com.noster.rewardpoints.domain;

// TODO: extract to two separate policies?
public class DefaultRewardPointsPolicy implements RewardPointsGrantPolicy {
    @Override
    public RewardPoints grant(MonetaryAmount transactionAmount) {
        return null;
    }
}
