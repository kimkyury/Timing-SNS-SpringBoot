package com.kkukku.timing.apis.challenge.repositories;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotRepository extends JpaRepository<SnapshotEntity, Long> {

    List<SnapshotEntity> findAllByChallengeId(Long challengeId);

    long countByChallengeId(Long challengeId);
}
