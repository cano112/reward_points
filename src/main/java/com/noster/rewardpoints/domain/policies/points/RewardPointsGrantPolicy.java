package com.noster.rewardpoints.domain.policies.points;

import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;

public interface RewardPointsGrantPolicy {

    Points grant(MonetaryAmount transactionAmount);
}
