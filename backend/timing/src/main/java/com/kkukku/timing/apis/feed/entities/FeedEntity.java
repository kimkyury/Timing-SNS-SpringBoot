package com.kkukku.timing.apis.feed.entities;

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

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isPrivate;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDelete;

    private String review;

    @Column(nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @PreUpdate
    public void updateTimeStamps() {
        this.updatedAt = LocalDateTime.now();
    }

}
