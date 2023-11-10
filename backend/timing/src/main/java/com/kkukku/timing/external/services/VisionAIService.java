package com.kkukku.timing.external.services;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VisionAIService {

    @Value("${external.vision-ai.url}")
    private String baseUrl;

    public ResponseSpec getDetectedObject(MultiValueMap<String, Object> body) {

        RestClient restClient = RestClient.create();
        return restClient.post()
                         .uri(baseUrl + "/objectDetection")
                         .contentType(MediaType.MULTIPART_FORM_DATA)
                         .body(body)
                         .retrieve()
                         .onStatus(HttpStatusCode::is4xxClientError,
                             (request, response) -> {
                                 throw new CustomException(
                                     ErrorCode.NOT_FOUNT_OBJECT_IN_IMAGE);
                             });
    }

    public ResponseSpec checkCoordinate(MultiValueMap<String, Object> body) {

        RestClient restClient = RestClient.create();
        return restClient.post()
                         .uri(baseUrl + "/objectDetection/chooseObject")
                         .contentType(MediaType.MULTIPART_FORM_DATA)
                         .body(body)
                         .retrieve()
                         .onStatus(HttpStatusCode::is4xxClientError,
                             (request, response) -> {
                                 throw new CustomException(
                                     ErrorCode.NOT_FOUNT_OBJECT_IN_IMAGE);
                             });

    }

    public void checkSimilarity(MultiValueMap<String, Object> body) {

        RestClient restClient = RestClient.create();
        restClient.post()
                  .uri(baseUrl + "/objectDetection/similarity")
                  .contentType(MediaType.MULTIPART_FORM_DATA)
                  .body(body)
                  .retrieve()
                  .onStatus(HttpStatusCode::is4xxClientError,
                      (request, response) -> {
                          throw new CustomException(
                              ErrorCode.NOT_HIGH_SIMILARITY_SNAPSHOT);
                      });

    }

    public MultipartFile getMovieBySnapshots(List<SnapshotEntity> snapshots) {
        return null;
    }


}
