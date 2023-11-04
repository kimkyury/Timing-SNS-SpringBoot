package com.kkukku.timing.apis.hashtag.services;

import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.repositories.HashTagOptionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagOptionService {


    private final HashTagOptionRepository hashTagOptionRepository;

    @Transactional
    public void createHashTagOptions(List<String> hashTagStrList) {

        List<String> uniqueHashTags = hashTagStrList.stream()
                                                    .distinct()
                                                    .filter(hashTagStr ->
                                                        !isExistHashTagOption(hashTagStr))
                                                    .toList(); // 웬만하면 ArrayList 상태

        List<HashTagOptionEntity> hashTagOptions = uniqueHashTags.stream() // 각 요소(String) -> 해시태그옵션
                                                                 .map(HashTagOptionEntity::new)
                                                                 .collect(Collectors.toList());

        hashTagOptionRepository.saveAll(hashTagOptions);
    }

    private boolean isExistHashTagOption(String hashTagStr) {
        return hashTagOptionRepository.existsByContent(hashTagStr);
    }

}
