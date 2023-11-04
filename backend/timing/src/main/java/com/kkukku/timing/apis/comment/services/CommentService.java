package com.kkukku.timing.apis.comment.services;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import com.kkukku.timing.apis.comment.repositories.CommentRepository;
import com.kkukku.timing.apis.member.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;

    public Long getCommentCountByFeedId(Long feedId) {
        return commentRepository.countByFeedId(feedId);
    }

    public void saveComment(Long feedId, Integer memberId, String content) {
        commentRepository.save(
            new CommentEntity(feedId, memberService.getMemberById(memberId), content));
    }
}
