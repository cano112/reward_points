package com.noster.rewardpoints.infra.db;

import com.noster.rewardpoints.domain.entities.RewardPoints;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;

import static org.jooq.generated.Tables.REWARD_POINTS;

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
}
