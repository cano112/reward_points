package com.noster.rewardpoints.infra.db;

import com.noster.rewardpoints.domain.RewardPoints;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.springframework.stereotype.Repository;

@Repository
public class JooqRewardPointsRepository implements RewardPointsRepository {

    private final DSLContext dsl;

    JooqRewardPointsRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void save(RewardPoints rewardPoints) {

    }
}
