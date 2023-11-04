package com.kkukku.timing.apis.like.repositories;

import com.kkukku.timing.apis.like.entities.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

}
