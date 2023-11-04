package com.kkukku.timing.apis.challenge.services;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final HashTagOptionService hashTagOptionService;

    public void createChallenge(Integer memberId, ChallengeCreateRequest challengeCreateRequest) {

        MemberEntity member = memberService.getMemberEntityById(memberId);

        List<String> hashTags = challengeCreateRequest.getHashTags();
        hashTagOptionService.createHashTagOptions(hashTags);

        ChallengeEntity challenge;
        if (challengeCreateRequest.getGoalContent() == null) {
            challenge = new ChallengeEntity(
                member,
                challengeCreateRequest.getStartAt()
            );
        } else {
            challenge = new ChallengeEntity(
                member,
                challengeCreateRequest.getStartAt(),
                challengeCreateRequest.getGoalContent()
            );
        }

        challengeRepository.save(challenge);
    }

}