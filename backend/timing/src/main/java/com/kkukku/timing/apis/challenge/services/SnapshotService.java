package com.kkukku.timing.apis.challenge.services;

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

}