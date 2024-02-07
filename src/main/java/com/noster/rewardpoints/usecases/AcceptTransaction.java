package com.noster.rewardpoints.usecases;

import com.noster.rewardpoints.domain.entities.RewardPoints;
import com.noster.rewardpoints.domain.policies.points.DefaultRewardPointsPolicy;
import com.noster.rewardpoints.domain.policies.points.RewardPointsGrantPolicy;
import com.noster.rewardpoints.domain.values.MonetaryAmount;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AcceptTransaction {

    private final RewardPointsRepository rewardPointsRepository;

    private final RewardPointsGrantPolicy rewardPointsGrantPolicy;

    public AcceptTransaction(RewardPointsRepository rewardPointsRepository) {
        this.rewardPointsRepository = rewardPointsRepository;
        this.rewardPointsGrantPolicy = new DefaultRewardPointsPolicy();
    }

    public record Request(
            UUID transactionId,
            MonetaryAmount amount,
            UUID userId,
            Instant timestamp) {
    }


    public void with(Request request) {
        final var points = rewardPointsGrantPolicy.grant(request.amount);
        rewardPointsRepository.save(new RewardPoints(
                request.transactionId,
                request.userId,
                points
        ));
    }
}
