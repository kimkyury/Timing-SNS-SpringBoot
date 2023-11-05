package com.kkukku.timing.apis.feed.repositories;

import com.kkukku.timing.apis.feed.entities.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {

}
