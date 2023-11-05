package com.kkukku.timing.apis.hashtag.services;

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
}
