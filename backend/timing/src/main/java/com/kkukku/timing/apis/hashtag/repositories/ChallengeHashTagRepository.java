package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeHashTagRepository extends JpaRepository<HashTagOptionEntity, Long> {

    List<HashTagOptionEntity> findHashTagOptionEntityByChallengeId(Long challengeId);

}
