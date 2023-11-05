package com.kkukku.timing.challengeTest;


import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.hashtag.repositories.ChallengeHashTagRepository;
import com.kkukku.timing.apis.hashtag.repositories.HashTagOptionRepository;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeCreateTest {

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

    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @Transactional
    @Order(1)
    void shoudCreateChallenge() {

        // 1. Create시에는 Member Id가 필요하다

    }

}
