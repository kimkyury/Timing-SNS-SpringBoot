package com.kkukku.timing.apis.challenge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SnapshotServiceTest {


    @Autowired
    private SnapshotService snapshotService;

    @Test
    @Transactional
    @Order(1)
    @DisplayName("특정 Challenge의 Snapshot들 목록보기")
    void shouldGetAllSnapshotByChallenge() {

        // given
        Long challengeId = 2L;

        // then
        List<SnapshotEntity> actualSnapshots = snapshotService.getAllSnapshotByChallenge(
            challengeId);

        // then
        List<Long> expectedIds = new ArrayList<>();
        expectedIds.add(1L);
        expectedIds.add(2L);
        expectedIds.add(3L);

        for (int i = 0; i < expectedIds.size(); i++) {
            assertEquals(expectedIds.get(i), actualSnapshots.get(i)
                                                            .getId());
        }
    }

}
