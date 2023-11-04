package com.kkukku.timing.apis.challenge.repositories;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Integer> {

    List<ChallengeEntity> findByMemberId(Integer memberId);

}
