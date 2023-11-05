package com.kkukku.timing.apis.comment.services;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import com.kkukku.timing.apis.comment.repositories.CommentRepository;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.security.utils.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;

    public Long getCommentCountByFeedId(Long feedId) {
        return commentRepository.countByFeedId(feedId);
    }

    public void saveComment(Long feedId, String content) {
        commentRepository.save(
            new CommentEntity(feedId, memberService.getMemberById(
                SecurityUtil.getLoggedInMemberPrimaryKey()), content));
    }

    public List<CommentEntity> getCommentsByFeedId(Long feedId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return commentRepository.findByFeedId(feedId, pageable)
                                .getContent();
    }

}
