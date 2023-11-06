package com.kkukku.timing.apis.challenge.services;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.services.ChallengeHashTagService;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final HashTagOptionService hashTagOptionService;
    private final ChallengeHashTagService challengeHashTagService;
    private final SnapshotService snapshotService;
    private final S3Service s3Service;

    public void createChallengeProcedure(Integer memberId,
        ChallengeCreateRequest challengeCreateRequest) {

        MemberEntity member = memberService.getMemberById(memberId);

        hashTagOptionService.createHashTagOptions(challengeCreateRequest.getHashTags());

        ChallengeEntity savedChallenge = saveChallenge(member, challengeCreateRequest);

        List<HashTagOptionEntity> hashTagOptions = hashTagOptionService.getHashTagOption(
            challengeCreateRequest.getHashTags());

        challengeHashTagService.createChallengeHashTag(savedChallenge, hashTagOptions);
    }

    public ChallengeEntity saveChallenge(MemberEntity member,
        ChallengeCreateRequest challengeCreateRequest) {

        ChallengeEntity challenge = ChallengeEntity.create(member, challengeCreateRequest);
        return challengeRepository.save(challenge);
    }


    public ChallengeResponse getChallenge(Integer memberId) {

        return new ChallengeResponse(
            challengeRepository.findByMemberId(memberId)
                               .stream()
                               .map(c -> {
                                   String thumbnailUrl =
                                       s3Service.getS3StartUrl() + c.getThumbnailUrl();
                                   int countDays = diffDay(c.getStartedAt(),
                                       LocalDate.now()
                                                .minusDays(1));
                                   int maxDays = diffDay(c.getStartedAt(), c.getEndedAt());
                                   return new Challenge(thumbnailUrl, countDays, maxDays);
                               })
                               .toList());
    }


    private int diffDay(LocalDate startedAt, LocalDate yesterday) {

        return (int) ChronoUnit.DAYS.between(startedAt, yesterday);
    }

    public void deleteChallenge(Integer memberId, Long challengeId) {

        checkOwnChallenge(memberId, challengeId);

        List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(challengeId);
        snapshots.stream()
                 .map(SnapshotEntity::getImageUrl)
                 .forEach(s3Service::deleteFile);

        challengeRepository.deleteById(challengeId);

    }

    public void checkOwnChallenge(Integer memberId, Long challengeId) {

        challengeRepository.findByIdAndMemberId(challengeId, memberId)
                           .orElseThrow(
                               () -> new CustomException(ErrorCode.THIS_CHALLENGE_IS_NOT_YOURS));

    }
}
