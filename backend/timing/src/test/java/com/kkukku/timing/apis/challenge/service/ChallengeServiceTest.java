package com.kkukku.timing.apis.challenge.service;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.hashtag.repositories.ChallengeHashTagRepository;
import com.kkukku.timing.apis.hashtag.repositories.HashTagOptionRepository;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeServiceTest {


    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private HashTagOptionRepository hashTagOptionRepository;

    @Autowired
    private ChallengeHashTagRepository challengeHashTagRepository;

    @Autowired
    private ChallengeService challengeService;

    @MockBean
    private S3Service s3Service;

    private static Integer testMemberId;

    @BeforeAll
    static void init() {
        testMemberId = 3; // 만들어진 Challenge가 없는 회원
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @Transactional
    @Order(1)
    @DisplayName("goalContent 있는 Challenge 생성")
    void shouldCreateChallengeWithGoalContent() {

        // given
        List<String> hashTags = new ArrayList<>();
        String hashTagStr = "아침";
        hashTags.add(hashTagStr);
        String createGoalContent = "그런 목표가 있습니다.";
        ChallengeCreateRequest challengeCreateRequest = new ChallengeCreateRequest(
            LocalDate.now(), hashTags, createGoalContent);

        // when
        challengeService.createChallenge(testMemberId, challengeCreateRequest);

        // then
        ChallengeEntity createChallenge = challengeRepository.findByMemberId(testMemberId)
                                                             .getLast();

//        boolean isExistHashTag1 = hashTagOptionRepository.existsByContent(hashTagStr);
//
//        Optional<hashTagOptionRepository.findByContent(hashTagStr);
//        boolean isExistChallengeHashTag1 = challengeHashTagRepository.existsByChallengeIdAndHashTagOptionId( hashTagStr);
//
//        assertEquals("createGoalContent", createGoalContent, createChallenge.getGoalContent());
//        assertEquals("isExistHashTagOption", true, isExistHashTag1);
//        assertEqulas("isExistChallengeHashTag", true, )
    }

    @Test
    @Transactional
    @Order(2)
    @DisplayName("특정 유저의 모든 챌린지 보기")
    void souldCreateHashtag() {

        Integer testMemberId = 2;

        // List < challengeService.getAllChallengs(testMemberId);

    }

}
