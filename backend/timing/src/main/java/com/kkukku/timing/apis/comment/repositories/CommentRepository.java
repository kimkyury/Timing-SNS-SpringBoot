package com.kkukku.timing.apis.comment.repositories;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Long countByFeedId(Long feedId);

    @EntityGraph(attributePaths = {"member"})
    Slice<CommentEntity> findByFeedId(Long feedId, Pageable pageable);
}
