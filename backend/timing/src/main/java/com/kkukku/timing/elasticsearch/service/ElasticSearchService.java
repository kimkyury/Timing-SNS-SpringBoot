package com.kkukku.timing.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.kkukku.timing.elasticsearch.docs.HashTagDoc;
import com.kkukku.timing.elasticsearch.response.AutoCompleteDto;
import com.kkukku.timing.elasticsearch.response.HashtagDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
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

        ElasticsearchAggregations elasticsearchAggregations = (ElasticsearchAggregations) Objects.requireNonNull(searchHits.getAggregations());
        Objects.requireNonNull(elasticsearchAggregations.get("hash_tags_count")).aggregation();
        Aggregate aggregate = Objects.requireNonNull(elasticsearchAggregations.get("hash_tags_count")).aggregation().getAggregate();
        Buckets<StringTermsBucket> buckets = aggregate.sterms().buckets();

        List<HashtagDto> hashtags = new ArrayList<>();

        for (StringTermsBucket bucket : buckets.array()) {
            String key = bucket.key()._toJsonString();
            long count = bucket.docCount();
            long fd_id = Objects.requireNonNull(
                    bucket.aggregations().get("top_tags_hits").topHits().hits().hits().get(0)
                            .source()).toJson().asJsonObject().getInt("f_id");

            hashtags.add(new HashtagDto(
                    fd_id,
                    key,
                    count
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

        List<String> list = new ArrayList<>();
        list.add("f_id");

        Aggregation subAggregation = new Aggregation.Builder()
                .topHits(t -> t
                        .source(s -> s
                                .filter(f -> f
                                        .includes(list)
                                )
                        )
                        .size(1)
                )
                .build();

        Aggregation aggregation = new Aggregation.Builder()
                .terms(t -> t
                        .field("key_word_hashtag")
                        .size(5)
                )
                .aggregations("top_tags_hits", subAggregation)
                .build();


        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .term(m -> m
                                .field(field)
                                .value(search)
                        )
                )
                .withAggregation("hash_tags_count", aggregation)
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, HashTagDoc.class);
    }
}
