package com.noster.rewardpoints.usecases.ports;

import com.noster.rewardpoints.domain.entities.GroupedPoints;
import com.noster.rewardpoints.domain.entities.RewardPoints;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RewardPointsRepository {

    void save(RewardPoints rewardPoints);

    List<GroupedPoints.Group> pointsGroupedByMonth(UUID userId, Instant start, Instant end);
}
