package com.noster.rewardpoints.domain.policies.points;

import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;

import java.util.Set;

public class DefaultRewardPointsPolicy implements RewardPointsGrantPolicy {

    private final RewardPointsGrantPolicy policy = new AggregateRewardPointsPolicy(
            Set.of(
                    new LinearRewardPointsPolicy(50),
                    new LinearRewardPointsPolicy(100)
            )
    );

    @Override
    public Points grant(MonetaryAmount transactionAmount) {
        return policy.grant(transactionAmount);
    }
}
