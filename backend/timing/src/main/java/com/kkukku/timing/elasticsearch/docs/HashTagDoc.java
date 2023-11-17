package com.kkukku.timing.elasticsearch.docs;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "hashtag")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class HashTagDoc {
    @Id
    private Long id;

    private String hashtag;

    private String chosungHashtag;
}
