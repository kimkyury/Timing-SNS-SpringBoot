package com.kkukku.timing.apis.challenge.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCompleteRequest {

    @NotNull
    private Long challengeId;

}
