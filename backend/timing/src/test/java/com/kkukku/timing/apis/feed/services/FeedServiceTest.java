package com.kkukku.timing.apis.feed.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kkukku.timing.apis.feed.responses.FeedOtherResponse;
import com.kkukku.timing.apis.feed.responses.FeedOwnResponse;
import com.kkukku.timing.security.services.MemberDetailService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest(properties = "spring.profiles.active=local")
public class FeedServiceTest {

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private FeedService feedService;

    @BeforeEach
    public void login() {
        String email = "unit@test.com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            memberDetailService.loadUserByUsername(email), null,
            AuthorityUtils.NO_AUTHORITIES);

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
    }

    @Test
    public void getOwnFeedsTest() {
        String[] titles = {"http://example.com/thumbnail2.jpg", "http://example.com/thumbnail4.jpg",
            "http://example.com/thumbnail6.jpg"};
        List<FeedOwnResponse> feeds = feedService.getOwnFeeds();

        assertEquals(titles.length, feeds.size(), "Check own feeds size with data.sql");

        for (int i = 0; i < titles.length; i++) {
            assertTrue(titles[i].equals(feeds.get(i)
                                             .getThumbnailUrl()));
        }
    }

    @Test
    public void getOtherFeedsTest() {
        String[] titles = {"http://example.com/thumbnail1.jpg"};
        List<FeedOtherResponse> feeds = feedService.getOtherFeeds("kkr@test.com");

        assertEquals(titles.length, feeds.size(), "Check other feeds size with data.sql");

        for (int i = 0; i < titles.length; i++) {
            assertTrue(titles[i].equals(feeds.get(i)
                                             .getThumbnailUrl()));
        }
    }

    @Test
    public void countInfluencedFeedsTest() {
        int[] answer = {1, 5, 0, 4, 0, 1, 0, 0};

        for (int i = 0; i < answer.length; i++) {
            Integer count = feedService.countInfluencedFeeds((long) (i + 1));
            assertEquals(answer[i], count, "Check influenced feed with data.sql");
        }
    }

}
