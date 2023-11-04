package com.kkukku.timing.apis.comment.services;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void saveCommentTest() {
        Long before = commentService.getCommentCountByFeedId(1L);

        Long feedId = 1L;
        Integer memberId = 1;
        String content = "Test";

        commentService.saveComment(feedId, memberId, content);

        Long after = commentService.getCommentCountByFeedId(1L);

        assertEquals("Compare with before count and after count", before + 1, after);
    }
}
