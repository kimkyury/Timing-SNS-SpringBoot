package com.kkukku.timing.apis.comment.repositories;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
