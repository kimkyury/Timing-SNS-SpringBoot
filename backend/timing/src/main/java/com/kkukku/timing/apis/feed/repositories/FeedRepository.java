package com.kkukku.timing.apis.feed.repositories;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {

    @Query(value = "SELECT * FROM feeds WHERE member_id != :memberId AND is_delete = false AND is_private = false ORDER BY id desc", nativeQuery = true)
    Page<FeedEntity> findFeedsOrderById(Integer memberId, Pageable pageable);

    Long countByMember_IdAndIsDeleteIsFalse(Integer memberId);

    Long countByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(Integer memberId);

    List<FeedEntity> findAllByMember_IdAndIsDeleteIsFalse(Integer memberId);

    List<FeedEntity> findAllByMember_IdAndIsDeleteIsFalseAndIsPrivateFalse(Integer memberId);

    @EntityGraph(attributePaths = {"parent"})
    List<FeedEntity> findAllByRoot_Id(Long rootId);

    List<FeedEntity> findAllByMember_Id(Integer memberId);

    Optional<FeedEntity> findByIdAndMember_Id(Long id, Integer memberId);

    @Query(value = "SELECT f.id, f.member_id, f.parent_id, f.root_id, f.started_at, f.ended_at, f.goal_content, f.thumbnail_url, f.timelapse_url, f.is_private, f.is_delete, f.review, f.updated_at, f.created_at FROM (SELECT *, CalculateFeedScore(:member_id, id) AS Score FROM feeds WHERE member_id != :member_id AND is_delete = false AND is_private = false) AS f ORDER BY f.Score DESC", nativeQuery = true)
    Page<FeedEntity> findFeedsWithScore(Integer memberId, Pageable pageable);

}
