package com.kkukku.timing.apis.like.services;

import com.kkukku.timing.apis.like.entities.LikeEntity;
import com.kkukku.timing.apis.like.repositories.LikeRepository;
import com.kkukku.timing.security.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Long getCommentCountByFeedId(Long feedId) {
        return likeRepository.countByFeedId(feedId);
    }

    public Boolean isLiked(Long feedId) {
        return likeRepository.existsByFeedIdAndMemberId(feedId,
            SecurityUtil.getLoggedInMemberPrimaryKey());
    }

    public void saveLike(Long feedId) {
        if (isLiked(feedId)) {
            return;
        }

        likeRepository.save(new LikeEntity(feedId, SecurityUtil.getLoggedInMemberPrimaryKey()));
    }

    public void deleteLike(Long feedId) {
        if (!isLiked(feedId)) {
            return;
        }

        likeRepository.delete(likeRepository.findByFeedIdAndMemberId(feedId,
            SecurityUtil.getLoggedInMemberPrimaryKey()));
    }
}
