package com.kkukku.timing.apis.challenge.requests;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCoordinateRequest {

    @NotNull
    private float x;

    @NotNull
    private float y;

}
