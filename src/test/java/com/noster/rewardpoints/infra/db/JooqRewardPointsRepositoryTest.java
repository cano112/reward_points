package com.noster.rewardpoints.infra.db;

import com.noster.rewardpoints.PostgresConfiguration;
import com.noster.rewardpoints.domain.RewardPoints;
import com.noster.rewardpoints.usecases.ports.RewardPointsRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.RewardPointsRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.Tables.REWARD_POINTS;

@SpringBootTest
@Import(PostgresConfiguration.class)
public class JooqRewardPointsRepositoryTest {

    private final UUID REWARD_POINTS_ID = UUID.fromString("a2375e87-4c94-4253-8858-056302cadad0");
    private final UUID TRANSACTION_ID = UUID.fromString("02614b56-d978-44bb-9850-eb8bac801b06");
    private final UUID USER_ID = UUID.fromString("399a29ab-342e-4188-87db-bf7e2451dab1");

    private final JooqRewardPointsRepository rewardPointsRepository;
    private final DSLContext dsl;

    @Autowired
    JooqRewardPointsRepositoryTest(JooqRewardPointsRepository rewardPointsRepository, DSLContext dsl) {
        this.rewardPointsRepository = rewardPointsRepository;
        this.dsl = dsl;
    }

    @Test
    void shouldSaveRewardPoints() {
        // Given
        final var points = new RewardPoints(REWARD_POINTS_ID, TRANSACTION_ID, USER_ID, 5);

        // When
        rewardPointsRepository.save(points);

        // Then
        final RewardPointsRecord record = dsl
                .selectFrom(REWARD_POINTS)
                .where(REWARD_POINTS.ID.equal(points.id()))
                .fetchOne();
        assertThat(record)
                .isEqualTo(new RewardPointsRecord(REWARD_POINTS_ID, TRANSACTION_ID, USER_ID, 5));
    }


}
