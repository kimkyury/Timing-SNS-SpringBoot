package com.kkukku.timing.elasticsearch.service;

import com.kkukku.timing.elasticsearch.docs.HashTagDoc;
import com.kkukku.timing.elasticsearch.response.AutoCompleteDto;
import com.kkukku.timing.elasticsearch.response.HashtagDto;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final static int PAGE_NUMBER = 0;
    private final static int PAGE_SIZE = 5;
    private final static String CHOSUNG_HASHTAG_FIELD_NAME = "chosung_hashtag";
    private final static String HASHTAG_FIELD_NAME = "hashtag";

    public AutoCompleteDto.Response getHashTags(String search) {
        String regEx = "[ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ]*";
        SearchHits<HashTagDoc> searchHits;

        if (Pattern.matches(regEx, search)) { //초성 검색
            searchHits = getChosungHashtags(search);
        } else {
            searchHits = getHashtags(search);
        }

        List<HashtagDto> hashtags = new ArrayList<>();

        for (SearchHit<HashTagDoc> searchHit : searchHits) {
            hashtags.add(new HashtagDto(
                    searchHit.getContent().getId(),
                    searchHit.getContent().getHashtag()
            ));
        }

        return new AutoCompleteDto.Response(hashtags);
    }

    public SearchHits<HashTagDoc> getHashtags(String search) {
        return operationTermQuery(HASHTAG_FIELD_NAME, search);
    }

    public SearchHits<HashTagDoc> getChosungHashtags(String search) {
        return operationTermQuery(CHOSUNG_HASHTAG_FIELD_NAME, search);
    }

    SearchHits<HashTagDoc> operationTermQuery(String field, String search) {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .term(m -> m
                                .field(field)
                                .value(search)
                        )
                )
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, HashTagDoc.class);
    }
}
