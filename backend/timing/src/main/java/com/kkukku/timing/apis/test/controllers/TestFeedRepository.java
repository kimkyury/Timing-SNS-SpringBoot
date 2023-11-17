package com.kkukku.timing.apis.test.controllers;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface TestFeedRepository extends JpaRepository<TestFeed, Long> {
    List<TestFeed> findAllByContentsContaining(String contents, Pageable pageable);
}
