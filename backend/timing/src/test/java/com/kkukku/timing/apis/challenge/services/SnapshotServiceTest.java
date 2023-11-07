package com.kkukku.timing.apis.challenge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.SnapshotRepository;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SnapshotServiceTest {

    @Autowired
    private SnapshotRepository snapshotRepository;

    @Autowired
    private SnapshotService snapshotService;

    @MockBean
    private S3Service s3Service;

    @Value("${cloud.aws.s3.url}")
    private String s3StartUrl;

    public MockMultipartFile getSampleImage() {
        Path path = Paths.get("src/test/resources/Chirachino.jpg");
        String name = "file";
        String originalFileName = "Chirachino.jpg";
        String contentType = "image/jpeg";
        byte[] content = "".getBytes();
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(e);
        }

        return new MockMultipartFile(name, originalFileName, contentType,
            content);
    }


    @Test
    @Transactional
    @Order(1)
    @DisplayName("특정 Challenge의 Snapshot들 목록보기")
    void shouldGetAllSnapshotByChallenge() {

        // given
        Long challengeId = 2L;

        // then
        List<SnapshotEntity> actualSnapshots = snapshotService.getAllSnapshotByChallenge(
            challengeId);

        // then
        List<Long> expectedIds = new ArrayList<>();
        expectedIds.add(1L);
        expectedIds.add(2L);
        expectedIds.add(3L);

        for (int i = 0; i < expectedIds.size(); i++) {
            assertEquals(expectedIds.get(i), actualSnapshots.get(i)
                                                            .getId());
        }

    }


    @Test
    @Transactional
    @Order(2)
    @DisplayName("특정 Challenge에 대한 최초 SnapShot 추가")
    void shouldSaveThumbnailAndSnapShot() {

        // given
        String updatedProfileUrl = "http://example.com/profile.png";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(updatedProfileUrl);
        MockMultipartFile multipartFile = getSampleImage();


    }

    @Test
    @Transactional
    @Order(3)
    @DisplayName("특정 Challenge에 대한 최초 아닌 SnapShot 추가")
    void shouldSaveSnapShot() {

        // given
        String updatedProfileUrl = "http://example.com/profile.png";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(updatedProfileUrl);
        MockMultipartFile multipartFile = getSampleImage();

    }

}
