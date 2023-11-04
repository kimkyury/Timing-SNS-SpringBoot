package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagOptionRepository extends JpaRepository<HashTagOptionEntity, Long> {

    boolean existsByContent(String content);


}
