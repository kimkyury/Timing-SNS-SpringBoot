package com.kkukku.timing.apis.feed.entities;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FeedEntity parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    private FeedEntity root;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate endedAt;

    private String goalContent;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private String timelapseUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)", insertable = false)
    @Setter
    private Boolean isPrivate;

    @Column(nullable = false, columnDefinition = "TINYINT(1)", insertable = false)
    @Setter
    private Boolean isDelete;

    @Setter
    private String review;

    @Column(nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @PreUpdate
    public void updateTimeStamps() {
        this.updatedAt = LocalDateTime.now();
    }

    public FeedEntity(ChallengeEntity challenge, String timelapseUrl) {
        this.member = challenge.getMember();
        this.startedAt = challenge.getStartedAt();
        this.endedAt = challenge.getEndedAt();
        this.goalContent = challenge.getGoalContent();
        this.thumbnailUrl = challenge.getThumbnailUrl();
        this.timelapseUrl = timelapseUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDelete = false;
        this.isPrivate = true;

    }

    public void setRelation(FeedEntity parent) {
        if (parent != null) {
            this.parent = parent;
            this.root = parent.getRoot();
        } else {
            this.root = this;
        }
    }

    @Override
    public String toString() {
        return "FeedEntity{" +
            "id=" + id +
            ", member=" + member +
            ", startedAt=" + startedAt +
            ", endedAt=" + endedAt +
            ", goalContent='" + goalContent + '\'' +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            ", timelapseUrl='" + timelapseUrl + '\'' +
            ", isPrivate=" + isPrivate +
            ", isDelete=" + isDelete +
            ", review='" + review + '\'' +
            ", updatedAt=" + updatedAt +
            ", createdAt=" + createdAt +
            '}';
    }
}
