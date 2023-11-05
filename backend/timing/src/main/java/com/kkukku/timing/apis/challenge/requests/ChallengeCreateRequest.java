package com.kkukku.timing.apis.challenge.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCreateRequest {

    @Future(message = "startAt은 현재 날짜(를 포함한) 이후여야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Challenge의 시작날짜는 필수 필드입니다.")
    private LocalDate startedAt;

    @NotNull(message = "최소 1개 이상의 HashTag를 작성해주세요. ")
    private List<String> hashTags;

    private String goalContent;
}
