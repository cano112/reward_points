package com.noster.rewardpoints.domain.policies.points;

import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;

import java.math.RoundingMode;

final class LinearRewardPointsPolicy implements RewardPointsGrantPolicy {
    private final int threshold;

    LinearRewardPointsPolicy(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Points grant(MonetaryAmount transactionAmount) {
        final int amountRounded = transactionAmount
                .amount()
                .setScale(0, RoundingMode.DOWN)
                .intValue();
        if (amountRounded > threshold) {
            return new Points(amountRounded - threshold);
        }
        return new Points(0);
    }
}
