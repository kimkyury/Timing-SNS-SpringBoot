package com.kkukku.timing.apis.hashtag.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_hash_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedHashTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_option_id", nullable = false)
    private HashTagOptionEntity hashTagOption;

    @Column(nullable = false)
    private Long feedId;

    public FeedHashTagEntity(HashTagOptionEntity hashTagOption, Long feedId) {
        this.hashTagOption = hashTagOption;
        this.feedId = feedId;
    }

}
