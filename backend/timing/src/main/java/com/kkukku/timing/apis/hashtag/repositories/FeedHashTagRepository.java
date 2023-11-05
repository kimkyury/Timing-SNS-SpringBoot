package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.hashtag.entities.FeedHashTagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedHashTagRepository extends JpaRepository<FeedHashTagEntity, Long> {

    @EntityGraph(attributePaths = {"hashTagOption"})
    List<FeedHashTagEntity> findAllByFeedId(Long feedId);
    
}
