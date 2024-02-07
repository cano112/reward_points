package com.noster.rewardpoints.usecases.ports;

import com.noster.rewardpoints.domain.RewardPoints;

public interface RewardPointsRepository {

    void save(RewardPoints rewardPoints);
}
