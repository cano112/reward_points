package com.noster.rewardpoints.infra.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noster.rewardpoints.domain.entities.GroupedPoints;
import com.noster.rewardpoints.domain.values.SummaryGrouping;
import com.noster.rewardpoints.usecases.GetSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class GetSummaryController {

    private final GetSummary getSummary;

    public GetSummaryController(GetSummary getSummary) {
        this.getSummary = getSummary;
    }

    @GetMapping("/users/{userId}/rewards/points/summary")
    SummaryResponse getSummary(
            @PathVariable UUID userId,
            @RequestParam Instant start,
            @RequestParam Instant end,
            @RequestParam("group_by") SummaryGrouping groupBy
    ) {
        final var response = getSummary.with(new GetSummary.Request(userId, start, end, groupBy));
        return SummaryResponse.of(response);
    }

    record SummaryResponse(
            @JsonProperty("points_summary") PointsSummary pointsSummary
    ) {
        record PointsSummary(
                @JsonProperty("grouped") Grouped grouped,
                @JsonProperty("total") Total total
        ) {
            record Grouped(
                    @JsonProperty("by") SummaryGrouping by,
                    @JsonProperty("entries") List<Entry> entries
            ) {
            }

            record Total(@JsonProperty("points") int points) {
            }

            record Entry(
                    @JsonProperty("group") int group,

                    @JsonProperty("points") int points
            ) {
                private static Entry of(GroupedPoints.Group group) {
                    return new Entry(group.key(), group.points().value());
                }
            }
        }

        static SummaryResponse of(GetSummary.Response response) {
            final var entries = response
                    .groupedPoints()
                    .groups()
                    .stream()
                    .map(PointsSummary.Entry::of)
                    .toList();

            final var grouped = new PointsSummary.Grouped(
                    response.groupedPoints().by(),
                    entries
            );

            final var total = new PointsSummary.Total(
                    response.total().value()
            );

            return new SummaryResponse(new PointsSummary(
                    grouped,
                    total
            ));
        }
    }

}
