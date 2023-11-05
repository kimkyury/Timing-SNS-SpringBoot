package com.kkukku.timing.apis.hashtag.services;

import static org.springframework.test.util.AssertionErrors.assertEquals;

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
public class HashTagOptionServiceTest {

    @Autowired
    private HashTagOptionService hashTagOptionService;

    @Autowired
    private HashTagOptionRepository hashTagOptionRepository;

    @Test
    @Transactional
    @Order(1)
    @DisplayName("Challenge 생성 시, 새로운 hashTag 생성")
    void souldCreateHashtag() {

        List<String> hashTags = new ArrayList<>();
        String hashTag1 = "아침";
        String hashTag2 = "점심";
        hashTags.add(hashTag1);
        hashTags.add(hashTag2);

        hashTagOptionService.createHashTagOptions(hashTags);

        boolean isExistHashTag1 = hashTagOptionRepository.existsByContent(hashTag1);
        boolean isExistHashTag2 = hashTagOptionRepository.existsByContent(hashTag2);
        assertEquals("isExistHashTag1", true, isExistHashTag1);
        assertEquals("isExistHashTag2", true, isExistHashTag2);

    }

}
