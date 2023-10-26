package com.kkukku.timing.apis.test.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hash_tag_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTagOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    public HashTagOptionEntity(String content) {
        this.content = content;
    }
}