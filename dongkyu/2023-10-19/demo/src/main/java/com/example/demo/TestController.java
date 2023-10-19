package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final String VIDEO_PATH = "src/main/resources/static/";

    @GetMapping(path = "/video/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> streamVideo(@PathVariable String fileName)
        throws IOException {
        File videoFile = new File(VIDEO_PATH + fileName + ".mp4");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(videoFile));

        return ResponseEntity.ok()
                             .contentLength(videoFile.length())
                             .contentType(MediaType.parseMediaType("application/octet-stream"))
                             .body(resource);
    }
}
