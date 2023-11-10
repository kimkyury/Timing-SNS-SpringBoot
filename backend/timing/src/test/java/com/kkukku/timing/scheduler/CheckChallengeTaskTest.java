package com.kkukku.timing.scheduler;


import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckChallengeTaskTest {

    @Autowired
    private CheckChallengeTask challengeTask;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;


    @Test
    @Transactional
    @DisplayName("하루 동안 진행하지 않은 Challenge는 모두 삭제한다")
    public void ShouldDeleteWhenNotProcessedChallenge() {

        long beforeChallengeCnt = challengeRepository.countBy();

        // when
        challengeTask.deleteChallengeProcedure();
        // 1. 모든 Challenge를 가져와서
        // 2. 각 객체의 diff(startedAt, now()))-1 만큼 Snapshot이 존재하는지 확인한다
        // 3. 만약, Snapshot이 개수만큼 존재하지 않다면 해당 Challenge와 관련된 모든 Snapshot, S3이미지 등을 삭제한다

        // then
        long afterChallengeCnt = challengeRepository.countBy();
        assertTrue(beforeChallengeCnt > afterChallengeCnt);
    }


}
