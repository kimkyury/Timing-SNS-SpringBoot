package com.kkukku.timing.apis.comment.services;

import static org.springframework.test.util.AssertionErrors.assertEquals;

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
public class CommentServiceTest {

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private CommentService commentService;

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
    public void saveCommentTest() {
        Long before = commentService.getCommentCountByFeedId(1L);

        Long feedId = 1L;
        String content = "Test";

        commentService.saveComment(feedId, content);

        Long after = commentService.getCommentCountByFeedId(1L);

        assertEquals("Compare with before count and after count", before + 1, after);
    }
}
