package com.kkukku.timing.apis.like.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kkukku.timing.security.services.MemberDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest(properties = "spring.profiles.active=local")
public class LikeServiceTest {

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private LikeService likeService;

    @BeforeEach
    public void login() {
        String email = "unit@com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            memberDetailService.loadUserByUsername(email), null,
            AuthorityUtils.NO_AUTHORITIES);

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
    }

    @Test
    void saveLikeTest() {
        Long feedId = 2L;
        Long before = likeService.getLikeCountByFeedId(feedId);

        likeService.saveLike(feedId);
        Long after = likeService.getLikeCountByFeedId(feedId);

        assertEquals(before + 1, after, () -> "Once liked test");

        likeService.saveLike(feedId);
        after = likeService.getLikeCountByFeedId(feedId);

        assertEquals(before + 1, after, () -> "Twice liked test");
    }

    @Test
    void deleteLikeTest() {
        Long feedId = 2L;
        likeService.saveLike(2L);
        Long before = likeService.getLikeCountByFeedId(feedId);

        likeService.deleteLike(2L);
        Long after = likeService.getLikeCountByFeedId(feedId);

        assertEquals(before - 1, after, () -> "Once disliked test");

        likeService.deleteLike(2L);
        after = likeService.getLikeCountByFeedId(feedId);

        assertEquals(before - 1, after, () -> "Twice disliked test");
    }
}
