package com.kkukku.timing.apis.challenge.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "snapshots")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private ChallengeEntity challenge;

    @Getter
    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, insertable = false)
    private LocalDateTime createdAt;

    private SnapshotEntity(ChallengeEntity challenge, String imageUrl) {
        this.challenge = challenge;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }

    public static SnapshotEntity of(ChallengeEntity challenge, String imageUrl) {
        return new SnapshotEntity(challenge, imageUrl);
    }

}
