package com.noster.rewardpoints.domain.policies;

import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;

import java.math.RoundingMode;

// TODO: extract to two separate policies?
public class DefaultRewardPointsPolicy implements RewardPointsGrantPolicy {
    @Override
    public Points grant(MonetaryAmount transactionAmount) {
        final int amountRounded = transactionAmount
                .amount()
                .setScale(0, RoundingMode.DOWN)
                .intValue();
        if (amountRounded >= 50 && amountRounded <= 100) {
            return new Points(amountRounded - 50);
        }
        if (amountRounded > 100) {
            return new Points(50 + 2 * (amountRounded - 100));
        }
        return new Points(0);
    }
}
