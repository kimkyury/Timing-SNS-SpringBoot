package com.kkukku.timing.scheduler;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.challenge.services.SnapshotService;
import com.kkukku.timing.s3.services.S3Service;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckChallengeTask {

    private final ChallengeService challengeService;
    private final SnapshotService snapshotService;
    private final S3Service s3Service;
    private final ChallengeRepository challengeRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void job() {
//        deleteChallengeProcedure();
    }

    public void deleteChallengeProcedure() {

        List<ChallengeEntity> challenges = challengeService.getAllChallenge()
                                                           .stream()
                                                           .filter(
                                                               challenge -> challenge.getStartedAt()
                                                                                     .isBefore(
                                                                                         LocalDate.now()))
                                                           .filter(challenge -> {
                                                                   LocalDate startedAt = challenge.getStartedAt();
                                                                   long mustDays =
                                                                       challengeService.diffDay(
                                                                           startedAt, LocalDate.now());
                                                                   long snapshotCnt = snapshotService.getCntSnapshotByChallenge(
                                                                       challenge.getId());
                                                                   return snapshotCnt < mustDays;
                                                               }
                                                           )
                                                           .toList();
        deleteChallenge(challenges);
    }

    public void deleteChallenge(List<ChallengeEntity> challenges) {

        challenges.forEach(
            challenge -> {
                List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(
                    challenge.getId());
                snapshots.stream()
                         .map(SnapshotEntity::getImageUrl)
                         .forEach(s3Service::deleteFile);
                snapshotService.deleteSnapshot(snapshots);
            }
        );

        challengeRepository.deleteAll(challenges);
    }
}
