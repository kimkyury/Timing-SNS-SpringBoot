package com.kkukku.timing.apis.test.controllers;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByNameContaining(String name, Pageable pageable);
}
