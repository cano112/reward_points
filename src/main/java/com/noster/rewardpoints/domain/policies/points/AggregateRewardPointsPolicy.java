package com.noster.rewardpoints.domain.policies.points;

import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;

import java.util.Collections;
import java.util.Set;

final class AggregateRewardPointsPolicy implements RewardPointsGrantPolicy {

    private final Set<? extends RewardPointsGrantPolicy> policies;

    public AggregateRewardPointsPolicy(Set<? extends RewardPointsGrantPolicy> policies) {
        this.policies = Collections.unmodifiableSet(policies);
    }

    @Override
    public Points grant(MonetaryAmount transactionAmount) {
        final var sum = policies
                .stream()
                .map(policy -> policy.grant(transactionAmount))
                .mapToInt(Points::value)
                .sum();
        return new Points(sum);
    }
}
