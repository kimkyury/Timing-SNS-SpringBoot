package com.kkukku.timing.apis.hashtag.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kkukku.timing.apis.hashtag.responses.FeedHashTagResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
public class FeedHashTagServiceTest {

    @Autowired
    private FeedHashTagService feedHashTagService;

    @Test
    public void getHashTagsByFeedIdTest() {
        Long feedId = 1L;
        String[] hashTags = {"요리", "저녁"};

        List<FeedHashTagResponse> feedHashTags = feedHashTagService.getHashTagsByFeedId(feedId);

        for (int i = 0; i < hashTags.length; i++) {
            assertEquals(hashTags[i], feedHashTags.get(i)
                                                  .getContent(), "Check hash tag options");
        }
    }

}