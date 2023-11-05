package com.kkukku.timing.apis.challenge.services;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final HashTagOptionService hashTagOptionService;

    public void createChallengeProcedure(Integer memberId,
        ChallengeCreateRequest challengeCreateRequest) {

        MemberEntity member = memberService.getMemberById(memberId);

        hashTagOptionService.createHashTagOptions(challengeCreateRequest.getHashTags());

        ChallengeEntity savedChallenge = saveChallenge(member, challengeCreateRequest);

        // 1. 존재하지 않는 hashTagOption 생성
        // 2. 전체 hashTagOption Entity 가져오기
        // 3. Chellange 새로 생성
        // 4. 2번과 3번의 결과를 통해 ChallengeHashTag 생성

    }

    public ChallengeEntity saveChallenge(MemberEntity member,
        ChallengeCreateRequest challengeCreateRequest) {

        ChallengeEntity challenge = ChallengeEntity.create(member, challengeCreateRequest);
        return challengeRepository.save(challenge);
    }


    public ChallengeEntity getChallengeEntity(Long challengeId) {

        return challengeRepository.findById(challengeId)
                                  .orElseThrow(() -> new CustomException(
                                      ErrorCode.NOT_EXIST_CHALLENGE));
    }

}
