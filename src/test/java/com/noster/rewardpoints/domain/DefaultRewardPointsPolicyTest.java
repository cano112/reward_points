package com.noster.rewardpoints.domain;

import com.noster.rewardpoints.domain.entities.RewardPoints;
import com.noster.rewardpoints.domain.policies.DefaultRewardPointsPolicy;
import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.domain.values.Points;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultRewardPointsPolicyTest {

    private final DefaultRewardPointsPolicy policy = new DefaultRewardPointsPolicy();

    @Test
    void shouldReturnPoints_givenTransactionBetween50$And100$() {
        // Given
        final var amount = new MonetaryAmount(90);

        // When
        final Points points = policy.grant(amount);

        // Then
        assertThat(points).isEqualTo(new Points(40));
    }

    @Test
    void shouldReturnNoPoints_given50$Transaction() {
        // Given
        final var amount = new MonetaryAmount(50);

        // When
        final Points points = policy.grant(amount);

        // Then
        assertThat(points).isEqualTo(new Points(0));
    }
}
