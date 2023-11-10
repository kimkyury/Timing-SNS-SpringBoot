package com.kkukku.timing.elasticsearch.controllers;

import com.kkukku.timing.elasticsearch.response.AutoCompleteDto;
import com.kkukku.timing.elasticsearch.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hashtags")
@RequiredArgsConstructor
public class SearchController {

    private final ElasticSearchService searchService;

    @PostMapping("/autocomplete")
    public void getAutoHashtags(@RequestBody AutoCompleteDto.Request request) {
        searchService.getHashtags(request.getSearch());
        searchService.getChosungHashtags(request.getSearch());
    }

//    @GetMapping("/api/search")
//    public void getDocs(Pageable pageable) {
////        TermQuery termQuery = new TermQuery.Builder().field("name").value("정릉2차").build();
////        Criteria criteria = new Criteria("name").matches("정릉2차");
////        Query query = new CriteriaQuery(criteria);
////        SearchHits<TestItems> searchHits = elasticsearchOperations.search(query, TestItems.class);
//
//        Query query = NativeQuery.builder()
//                        .withQuery(q -> q
//                                .term(m -> m
//                                        .field("name")
//                                        .value("경희궁의 아")
//                                )
//                        )
//                        .withPageable(pageable)
//                                .build();
//
////        Query query = NativeQuery.builder()
////                        .withQuery(q -> q
////                                .match(m -> m
////                                        .field("name")
////                                        .query("경희궁의 아")
////                                )
////                        )
////                        .withPageable(pageable)
////                                .build();
//
//        SearchHits<TestItems> searchHits = elasticsearchOperations.search(query, TestItems.class);
//        System.out.println(searchHits.getSearchHits().size());
//        for(SearchHit<TestItems> testItemsSearchHit :searchHits) {
//            System.out.println(testItemsSearchHit.getContent());
//        }
//    }
//
//    @PostMapping("/api/search/nori")
//    public void getNori(@RequestBody FeedSearchDto feedSearchDto, Pageable pageable) {
//
//        Query query = NativeQuery.builder()
//                        .withQuery(q -> q
//                                .match(m -> m
//                                        .field("contents")
//                                        .query(feedSearchDto.getSearch())
//                                )
//                        )
//                        .withPageable(pageable)
//                                .build();
//
//        SearchHits<FeedSample> searchHits = elasticsearchOperations.search(query, FeedSample.class);
////        System.out.println(searchHits.getSearchHits().size());
////        for(SearchHit<FeedSample> testItemsSearchHit :searchHits) {
////            System.out.println(testItemsSearchHit.getContent());
////        }
//    }
//
//    @GetMapping("/api/search2")
//    public void getDocs2() {
//        List<TestItems> testItemsFlux = testItemsRepository.findByName("정릉2차");
//        System.out.println(testItemsFlux.size());
//    }
}
