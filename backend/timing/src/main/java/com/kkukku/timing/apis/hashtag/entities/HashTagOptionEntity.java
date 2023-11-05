package com.kkukku.timing.apis.hashtag.entities;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private String content;

    public HashTagOptionEntity(String content) {
        this.content = content;
    }
}