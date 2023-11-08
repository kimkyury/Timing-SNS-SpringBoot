package com.kkukku.timing.apis.hashtag.services;


import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.hashtag.entities.ChallengeHashTagEntity;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.repositories.ChallengeHashTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeHashTagService {

    private final ChallengeHashTagRepository challengeHashTagRepository;

    public void createChallengeHashTag(ChallengeEntity challenge,
        List<HashTagOptionEntity> hashTagOptionList) {

        List<ChallengeHashTagEntity> challengeHashTagList = hashTagOptionList.stream()
                                                                             .map(
                                                                                 hashTagOption -> new ChallengeHashTagEntity(
                                                                                     hashTagOption,
                                                                                     challenge))
                                                                             .toList();
        challengeHashTagRepository.saveAll(challengeHashTagList);
    }

    public List<HashTagOptionEntity> getHashTagOptionByChallengeId(Long challengeId) {
        return challengeHashTagRepository.findAllByChallengeId(challengeId)
                                         .stream()
                                         .map(ChallengeHashTagEntity::getHashTagOption)
                                         .toList();
    }

}
