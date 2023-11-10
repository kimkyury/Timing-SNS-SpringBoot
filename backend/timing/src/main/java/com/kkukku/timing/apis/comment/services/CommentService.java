package com.kkukku.timing.apis.comment.services;

import com.kkukku.timing.apis.comment.entities.CommentEntity;
import com.kkukku.timing.apis.comment.repositories.CommentRepository;
import com.kkukku.timing.apis.comment.responses.CommentResponse;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.s3.services.S3Service;
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
    private final S3Service s3Service;

    public Long getCommentCountByFeedId(Long feedId) {
        return commentRepository.countByFeedId(feedId);
    }

    public void saveComment(Long feedId, String content) {
        commentRepository.save(
            new CommentEntity(feedId, memberService.getMemberById(
                SecurityUtil.getLoggedInMemberPrimaryKey()), content));
    }

    public List<CommentResponse> getCommentsByFeedId(Long feedId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return commentRepository.findByFeedIdOrderById(feedId, pageable)
                                .getContent()
                                .stream()
                                .map(comment -> new CommentResponse(comment, s3Service))
                                .toList();
    }

}
