package com.kkukku.timing.challengeTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
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
        hashTags.add("아침");
        String createGoalContent = "그런 목표가 있습니다.";
        ChallengeCreateRequest challengeCreateRequest = new ChallengeCreateRequest(
            LocalDate.now(), hashTags, createGoalContent);

        // when
        challengeService.createChallenge(testMemberId, challengeCreateRequest);

        // then
        ChallengeEntity createChallenge = challengeRepository.findByMemberId(testMemberId)
                                                             .getLast();
        assertEquals("createGoalContent", createGoalContent, createChallenge.getGoalContent());

    }

    @Test
    @Transactional
    @Order(2)
    @DisplayName("Challenge 생성 시, 새로운 hashTag 생성")
    void souldCreateHashtag() {

        List<String> hashTags = new ArrayList<>();
        hashTags.add("아침");
        String createGoalContent = "그런 목표가 있습니다.";
        ChallengeCreateRequest challengeCreateRequest = new ChallengeCreateRequest(
            LocalDate.now(), hashTags, createGoalContent);


    }

}
