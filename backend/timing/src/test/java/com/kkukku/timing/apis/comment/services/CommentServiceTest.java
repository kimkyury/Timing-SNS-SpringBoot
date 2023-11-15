package com.kkukku.timing.apis.comment.services;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.kkukku.timing.apis.comment.responses.CommentResponse;
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
public class CommentServiceTest {

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private CommentService commentService;

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
    public void saveCommentTest() {
        Long feedId = 1L;
        String content = "Test";
        Long before = commentService.getCommentCountByFeedId(feedId);

        commentService.saveComment(feedId, content);

        Long after = commentService.getCommentCountByFeedId(feedId);

        assertEquals("Compare with before count and after count", before + 1, after);
    }

    @Test
    public void getCommentsByFeedIdTest() {
        Long feedId = 1L;
        Integer pageSize = 10;

        for (int i = 0; i < 100; i++) {
            commentService.saveComment(feedId, "Test");
        }

        for (int i = 0; i < 10; i++) {
            List<CommentResponse> comments = commentService.getCommentsByFeedId(feedId, i + 1,
                pageSize);

            assertEquals("Check only 10 rows", pageSize, comments.size());
        }
    }
}
