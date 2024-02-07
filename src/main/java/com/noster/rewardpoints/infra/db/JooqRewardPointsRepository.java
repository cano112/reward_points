package com.noster.rewardpoints.infra.db;

import com.noster.rewardpoints.domain.entities.GroupedPoints;
import com.noster.rewardpoints.domain.entities.RewardPoints;
import com.noster.rewardpoints.domain.values.Points;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.jooq.generated.Tables.REWARD_POINTS;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.sum;

@Repository
public class JooqRewardPointsRepository implements RewardPointsRepository {

    private final DSLContext dsl;

    JooqRewardPointsRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void save(RewardPoints rewardPoints) {
        dsl.insertInto(REWARD_POINTS)
                .set(REWARD_POINTS.ID, rewardPoints.id())
                .set(REWARD_POINTS.TRANSACTION_ID, rewardPoints.transactionId())
                .set(REWARD_POINTS.USER_ID, rewardPoints.userId())
                .set(REWARD_POINTS.POINTS, rewardPoints.points().value())
                .set(REWARD_POINTS.TIMESTAMP, rewardPoints.timestamp().atOffset(ZoneOffset.UTC))
                .execute();
    }

    @Override
    public List<GroupedPoints.Group> pointsGroupedByMonth(UUID userId, Instant start, Instant end) {
        return dsl.select(
                        month(REWARD_POINTS.TIMESTAMP),
                        sum(REWARD_POINTS.POINTS)
                )
                .from(REWARD_POINTS)
                .where(REWARD_POINTS.USER_ID.equal(userId)
                        .and(REWARD_POINTS.TIMESTAMP.between(
                                start.atOffset(ZoneOffset.UTC),
                                end.atOffset(ZoneOffset.UTC))
                        )
                )
                .groupBy(month(REWARD_POINTS.TIMESTAMP))
                .fetch()
                .map(rec -> {
                    final var points = new Points(rec.value2().toBigInteger().intValue());
                    return new GroupedPoints.Group(rec.value1(), points);
                });
    }


}
