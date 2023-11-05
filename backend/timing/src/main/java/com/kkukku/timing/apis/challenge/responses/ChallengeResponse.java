package com.kkukku.timing.apis.challenge.responses;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeResponse {

    private List<Challenge> challenges;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    class Challenge {

        private String imgUrl;
        private int countDays;
        private int maxDays;

    }
}
