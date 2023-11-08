package com.kkukku.timing.external.services;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.external.requests.CheckCoordinateRequest;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VisionAIService {

    // TODO: Apply Python Server URL (application.yml)
    private String baseUrl = "";


    public MultipartFile getDetectedObject(MultipartFile multipartFile) {
        return null;
    }

    public void checkCoordinate(MultipartFile image, CheckCoordinateRequest request) {

    }

    public void checkSimilarity(
        InputStreamResource snapshotStreamResource, InputStreamResource objectStreamResource) {

        RestClient restClient = RestClient.create();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("snapshot", snapshotStreamResource)
                   .filename("snapshot");
        bodyBuilder.part("object", objectStreamResource)
                   .filename("object");
        MultiValueMap<String, HttpEntity<?>> multipartBody = bodyBuilder.build();

        ResponseEntity<Void> result = restClient.post()
                                                .uri(baseUrl + "/")
                                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                                .body(multipartBody)
                                                .retrieve()
                                                .toBodilessEntity();

        System.out.println("Response status: " + result.getStatusCode());

    }

    public MultipartFile getMovieBySnapshots(List<SnapshotEntity> snapshots) {
        return null;
    }


}
