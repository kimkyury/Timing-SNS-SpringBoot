package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.hashtag.entities.ChallengeHashTagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeHashTagRepository extends JpaRepository<ChallengeHashTagEntity, Long> {


    @EntityGraph(attributePaths = {"hashTagOption"})
    List<ChallengeHashTagEntity> findAllByChallengeId(Long challengeId);

    boolean existsByChallengeIdAndHashTagOptionId(Long ChallengeId, Long TagOptionId);

}
