package com.noster.rewardpoints.domain.entities;

import com.noster.rewardpoints.domain.values.Points;
import com.noster.rewardpoints.domain.values.SummaryGrouping;

import java.util.SequencedCollection;

public record GroupedPoints(SummaryGrouping by, SequencedCollection<Group> groups) {

    public record Group(
            int key,
            Points points
    ) {
    }

    public Points total() {
        final var sum = groups.stream()
                .mapToInt(group -> group.points.value())
                .sum();
        return new Points(sum);
    }

}