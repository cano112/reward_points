package com.noster.rewardpoints.usecases;

import com.noster.rewardpoints.domain.entities.GroupedPoints;
import com.noster.rewardpoints.domain.values.Points;
import com.noster.rewardpoints.domain.values.SummaryGrouping;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class GetSummary {

    private final RewardPointsRepository rewardPointsRepository;

    GetSummary(RewardPointsRepository rewardPointsRepository) {
        this.rewardPointsRepository = rewardPointsRepository;
    }

    public record Response(
            GroupedPoints groupedPoints,
            Points total
    ) {

    }

    public record Request(
            UUID userId,
            Instant start,
            Instant end,
            SummaryGrouping aggregation
    ) {
    }

    public Response with(Request request) {
        final var groups = rewardPointsRepository.pointsGroupedByMonth(request.userId, request.start, request.end);
        final var groupedPoints = new GroupedPoints(
                SummaryGrouping.MONTH, // The only option possible for now.
                groups
        );
        return new Response(groupedPoints, groupedPoints.total());
    }
}
