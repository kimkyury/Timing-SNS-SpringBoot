package com.kkukku.timing.apis.feed.services;

import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
}
