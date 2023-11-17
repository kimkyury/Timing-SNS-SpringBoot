package com.kkukku.timing.apis.hashtag.services;

import com.kkukku.timing.apis.hashtag.entities.FeedHashTagEntity;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.repositories.FeedHashTagRepository;
import com.kkukku.timing.apis.hashtag.responses.FeedHashTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedHashTagService {

    private final FeedHashTagRepository feedHashTagRepository;

    public List<FeedHashTagResponse> getHashTagsByFeedId(Long feedId) {
        return feedHashTagRepository.findAllByFeedId(feedId)
                .stream()
                .map(FeedHashTagResponse::new)
                .toList();
    }

    public List<HashTagOptionEntity> getHashTagOptionByFeedId(Long feedId) {
        return feedHashTagRepository.findAllByFeedId(feedId)
                .stream()
                .map(FeedHashTagEntity::getHashTagOption)
                .toList();
    }

    public void saveHashTagsByFeedId(Long feedId, List<HashTagOptionEntity> hashTags) {
        feedHashTagRepository.saveAll(hashTags.stream()
                .map(
                        hashTag -> new FeedHashTagEntity(hashTag, feedId))
                .toList());
    }

}
