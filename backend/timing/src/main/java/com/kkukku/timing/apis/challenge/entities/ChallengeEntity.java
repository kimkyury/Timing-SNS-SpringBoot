package com.kkukku.timing.apis.challenge.entities;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "challenges")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate endedAt;

    private String goalContent;

    @Setter
    private String objectUrl;

    public ChallengeEntity(MemberEntity member, LocalDate startedAt, String goalContent) {
        this.member = member;
        this.startedAt = startedAt;
        this.goalContent = goalContent;
        this.endedAt = calculateEndDate(startedAt);
    }

    public ChallengeEntity(MemberEntity member, LocalDate startedAt) {
        this.member = member;
        this.startedAt = startedAt;
        this.endedAt = calculateEndDate(startedAt);
    }

    private LocalDate calculateEndDate(LocalDate startDate) {
        return startDate.plusDays(21);
    }
}
