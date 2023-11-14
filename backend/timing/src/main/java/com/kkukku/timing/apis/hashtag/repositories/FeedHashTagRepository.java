package com.kkukku.timing.apis.hashtag.repositories;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.hashtag.entities.FeedHashTagEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedHashTagRepository extends JpaRepository<FeedHashTagEntity, Long> {

    @EntityGraph(attributePaths = {"hashTagOption"})
    List<FeedHashTagEntity> findAllByFeedId(Long feedId);

    @Query("SELECT f FROM FeedEntity f WHERE f.id IN (SELECT fh.feedId FROM FeedHashTagEntity fh WHERE fh.hashTagOption.id = :feedId) ORDER BY f.id desc")
    Page<FeedEntity> findByFeedId(Long feedId, Pageable page);

}
