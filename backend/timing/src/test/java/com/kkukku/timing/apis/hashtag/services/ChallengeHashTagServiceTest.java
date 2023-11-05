package com.kkukku.timing.apis.hashtag.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.hashtag.entities.ChallengeHashTagEntity;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.repositories.ChallengeHashTagRepository;
import com.kkukku.timing.apis.hashtag.repositories.HashTagOptionRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeHashTagServiceTest {


    @Autowired
    private ChallengeHashTagService challengeHashTagService;

    @Autowired
    private ChallengeHashTagRepository challengeHashTagRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private HashTagOptionRepository hashTagOptionRepository;


    @Test
    @Transactional
    @Order(1)
    @DisplayName("ChallengeHashTag를 생성한다")
    void shouldCreateChallengeHashTag() {

        //given
        Long targetChallengeId = 4L;
        ChallengeEntity challenge = challengeRepository.findById(targetChallengeId)
                                                       .get();
        List<HashTagOptionEntity> expectedHashTagOptions = new ArrayList<>();
        expectedHashTagOptions.add(hashTagOptionRepository.findById(1L)
                                                          .get());
        expectedHashTagOptions.add(hashTagOptionRepository.findById(2L)
                                                          .get());

        // when
        challengeHashTagService.createChallengeHashTag(challenge, expectedHashTagOptions);

        // then
        List<HashTagOptionEntity> actualHashTagOptionEntities = challengeHashTagRepository.findAllByChallengeId(
                                                                                              targetChallengeId)
                                                                                          .stream()
                                                                                          .map(
                                                                                              ChallengeHashTagEntity::getHashTagOption)
                                                                                          .toList();

        for (int i = 0; i < expectedHashTagOptions.size(); i++) {
            assertEquals(
                expectedHashTagOptions.get(i)
                                      .getId(),
                actualHashTagOptionEntities.get(i)
                                           .getId());
        }

    }
}
