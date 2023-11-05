package com.kkukku.timing.apis.hashtag.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
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
    @DisplayName("해시태그는 새로운 Content에 한해서 생성시킨다")
    void souldCreateHashtag() {

        List<String> hashTags = new ArrayList<>();
        String newHashTag1 = "아침";
        String newHashTag2 = "점심";
        String existHashTag = "운동";
        hashTags.add(newHashTag1);
        hashTags.add(newHashTag2);

        hashTagOptionService.createHashTagOptions(hashTags);

        boolean isExistHashTag1 = hashTagOptionRepository.existsByContent(newHashTag1);
        boolean isExistHashTag2 = hashTagOptionRepository.existsByContent(newHashTag2);
        Long duplicatedHashTagOptionId = hashTagOptionRepository.findByContent(existHashTag)
                                                                .get()
                                                                .getId();
        Long maxId = hashTagOptionRepository.findMaxId()
                                            .get();
        assertTrue(isExistHashTag1);
        assertTrue(isExistHashTag2);
        assertNotEquals(maxId, duplicatedHashTagOptionId);
    }

    @Test
    @Transactional
    @Order(2)
    @DisplayName("content에 대하여 대응되는 HashTagOption들을 가져온다")
    void shouldGetAllHashTagOptionByChallenge() {

        // given
        List<String> searchContents = new ArrayList<>();
        searchContents.add("운동");
        searchContents.add("취미");

        // when
        List<HashTagOptionEntity> actualHashTagOptionList = hashTagOptionService.getHashTagOption(
            searchContents);

        // then
        List<Long> expectedHashTagOptionIdList = new ArrayList<>();
        expectedHashTagOptionIdList.add(1L);
        expectedHashTagOptionIdList.add(3L);
        List<HashTagOptionEntity> expectedhashTagOptionList = hashTagOptionRepository.findAllById(
            expectedHashTagOptionIdList);

        assertEquals(expectedhashTagOptionList, actualHashTagOptionList);
    }

}
