package com.kkukku.timing.external.services;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.requests.CheckCoordinateRequest;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VisionAIService {

    // TODO: Apply Python Server URL (application.yml)
    private String baseUrl = "";

    public byte[] getDetectedObject(MultiValueMap<String, Object> body) {

        RestClient restClient = RestClient.create();
        return restClient.post()
                         .uri(baseUrl + "/objectDetaction")
                         .contentType(MediaType.MULTIPART_FORM_DATA)
                         .body(body)
                         .retrieve()
                         .onStatus(HttpStatusCode::is4xxClientError,
                             (request, response) -> {
                                 throw new CustomException(
                                     ErrorCode.NOT_FOUNT_OBJECT_IN_IMAGE);
                             })
                         .body(byte[].class);
    }

    public void checkCoordinate(MultipartFile image, CheckCoordinateRequest request) {

    }

    public void checkSimilarity(MultiValueMap<String, Object> body) {

        RestClient restClient = RestClient.create();
        ResponseEntity<Void> result = restClient.post()
                                                .uri(baseUrl + "/objectDetaction/similarity")
                                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                                .body(body)
                                                .retrieve()
                                                .onStatus(HttpStatusCode::is4xxClientError,
                                                    (request, response) -> {
                                                        throw new CustomException(
                                                            ErrorCode.NOT_HIGH_SIMILARITY_SNAPSHOT);
                                                    })
                                                .toBodilessEntity();

        System.out.println("Response status: " + result.getStatusCode());

    }

    public MultipartFile getMovieBySnapshots(List<SnapshotEntity> snapshots) {
        return null;
    }


}
