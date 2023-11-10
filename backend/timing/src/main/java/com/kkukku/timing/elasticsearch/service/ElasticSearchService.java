package com.kkukku.timing.elasticsearch.service;

import com.kkukku.timing.elasticsearch.docs.HashTagDoc;
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

    public void getHashtags(String search) {
        SearchHits<HashTagDoc> searchHits = operationTermQuery(HASHTAG_FIELD_NAME, search);
        System.out.println(searchHits.getSearchHits().size());
        for (SearchHit<HashTagDoc> testItemsSearchHit : searchHits) {
            System.out.println(testItemsSearchHit.getContent());
        }
    }

    public void getChosungHashtags(String search) {
        SearchHits<HashTagDoc> searchHits = operationTermQuery(CHOSUNG_HASHTAG_FIELD_NAME, search);
        System.out.println(searchHits.getSearchHits().size());
        for (SearchHit<HashTagDoc> testItemsSearchHit : searchHits) {
            System.out.println(testItemsSearchHit.getContent());
        }
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
