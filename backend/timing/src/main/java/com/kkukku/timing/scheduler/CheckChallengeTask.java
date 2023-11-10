package com.kkukku.timing.scheduler;

import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.challenge.services.SnapshotService;
import com.kkukku.timing.s3.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckChallengeTask {

    private final ChallengeService challengeService;
    private final SnapshotService snapshotService;
    private final S3Service s3Service;

    @Scheduled(cron = "0 0 0 * * *")
    private void job() {

    }


    public void deleteChallengeProcedure() {
        // 1. 모든 Challenge를 가져와서
//        challengeService.getAllChallenge().stream()
//            .map(challenge -> {
//                    LocalDate startedAt = challenge.getStartedAt();
//                    long conditionDays = challengeService.diffDay(startedAt, LocalDate.now()) - 1;
//                    long snapshotCnt = snapshotService.getCntSnapshotByChallenge(challenge.getId());
//                    if (snapshotCnt < conditionDays) {
//                        return challenge;
//                    }
//                )

        // 2. 각 객체의 diff(startedAt, now()))-1 만큼 Snapshot이 존재하는지 확인한다
        // 3. 만약, Snapshot이 개수만큼 존재하지 않다면 해당 Challenge와 관련된 모든 Snapshot, S3이미지 등을 삭제한다

    }

}
