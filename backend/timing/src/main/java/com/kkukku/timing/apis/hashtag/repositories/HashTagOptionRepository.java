package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HashTagOptionRepository extends JpaRepository<HashTagOptionEntity, Long> {

    boolean existsByContent(String content);

    Optional<HashTagOptionEntity> findByContent(String content);

    List<HashTagOptionEntity> findByContentIn(List<String> contents);

    @Query("SELECT MAX(h.id) FROM HashTagOptionEntity h")
    Optional<Long> findMaxId();
}
