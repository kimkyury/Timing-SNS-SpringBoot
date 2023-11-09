package com.kkukku.timing.apis.challenge.requests;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckCoordinateRequest {

    @NotNull
    private float x;

    @NotNull
    private float y;

}
