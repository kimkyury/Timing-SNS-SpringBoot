package com.kkukku.timing.apis.hashtag.entities;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_hash_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeHashTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_option_id", nullable = false)
    private HashTagOptionEntity hashTagOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private ChallengeEntity challenge;

    public ChallengeHashTagEntity(HashTagOptionEntity hashTagOption, ChallengeEntity challenge) {
        this.hashTagOption = hashTagOption;
        this.challenge = challenge;
    }

}
