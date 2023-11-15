package com.kkukku.timing.apis.challenge.services;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.SnapshotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotService {


    private final SnapshotRepository snapshotRepository;


    public List<SnapshotEntity> getAllSnapshotByChallenge(Long challengeId) {
        return snapshotRepository.findAllByChallengeId(challengeId);
    }

    public long getCntSnapshotByChallenge(Long challengeId) {
        return snapshotRepository.countByChallengeId(challengeId);
    }

    public void deleteSnapshot(List<SnapshotEntity> snapshots) {
        snapshotRepository.deleteAll(snapshots);
    }

    public void createSnapshot(ChallengeEntity challenge, String url) {

        SnapshotEntity snapshot = SnapshotEntity.of(challenge, url);
        snapshotRepository.save(snapshot);

    }

}
