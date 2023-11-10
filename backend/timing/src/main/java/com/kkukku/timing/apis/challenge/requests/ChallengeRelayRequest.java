package com.kkukku.timing.apis.challenge.requests;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRelayRequest {

    @Future(message = "startAt은 현재 날짜(를 포함한) 이후여야 합니다.")
    @NotNull(message = "이어가기를 시작할 날짜를 명시해주세요. ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startedAt;

    private String goalContent;

}
