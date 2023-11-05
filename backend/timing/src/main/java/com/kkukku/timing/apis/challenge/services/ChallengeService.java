package com.kkukku.timing.apis.challenge.services;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.services.ChallengeHashTagService;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
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

                                   System.out.println(c.getStartedAt() + " " + LocalDate.now()
                                                                                        .minusDays(
                                                                                            1));
                                   int countDays = diffDay(c.getStartedAt(), LocalDate.now()
                                                                                      .minusDays(
                                                                                          1));
                                   int maxDays = diffDay(c.getStartedAt(), c.getEndedAt());

                                   System.out.println("result: " + countDays + " " + maxDays);
                                   return new Challenge(thumbnailUrl, countDays,
                                       maxDays);
                               })
                               .toList());
    }


    public int diffDay(LocalDate startedAt, LocalDate yesterday) {
        return (int) ChronoUnit.DAYS.between(startedAt, yesterday);

    }

}
