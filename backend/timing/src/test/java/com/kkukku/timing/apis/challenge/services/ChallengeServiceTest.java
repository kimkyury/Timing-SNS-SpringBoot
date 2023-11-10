package com.kkukku.timing.apis.challenge.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.repositories.SnapshotRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengePolygonResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.hashtag.entities.ChallengeHashTagEntity;
import com.kkukku.timing.apis.hashtag.entities.FeedHashTagEntity;
import com.kkukku.timing.apis.hashtag.repositories.ChallengeHashTagRepository;
import com.kkukku.timing.apis.hashtag.repositories.FeedHashTagRepository;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.external.services.VisionAIService;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MultiValueMap;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeServiceTest {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private FeedHashTagRepository feedHashTagRepository;

    @Autowired
    private ChallengeHashTagRepository challengeHashTagRepository;

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private SnapshotRepository snapshotRepository;

    @MockBean
    private S3Service s3Service;

    @Value("${cloud.aws.s3.url}")
    private String s3StartUrl;

    @MockBean
    private VisionAIService visionAIService;

    public MockMultipartFile getSampleImage(String pathStr, String filename) {
        Path path = Paths.get(pathStr);
        String name = "file";
        String contentType = "image/jpeg";

        byte[] content = "".getBytes();
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(e);
        }

        return new MockMultipartFile(name, filename, contentType, content);
    }

    public MockMultipartFile getSampleText(String pathStr, String fileName) {
        Path path = Paths.get(pathStr);
        String name = "file";
        String contentType = "text/plain"; // 텍스트 파일의 MIME 타입
        byte[] content = null;

        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new MockMultipartFile(name, fileName, contentType, content);
    }

    private S3Object createMockS3Object(String filePath) throws IOException {

        byte[] contentBytes = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream contentStream = new ByteArrayInputStream(contentBytes);

        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(contentStream, null);
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(s3ObjectInputStream);

        return s3Object;
    }

    public S3Object createS3ObjectFromMultipartFile(MockMultipartFile file)
        throws IOException {
        S3Object s3Object = new S3Object();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3Object에 ObjectMetadata 설정
        s3Object.setObjectMetadata(metadata);

        // S3Object의 내용을 설정
        s3Object.setObjectContent(new ByteArrayInputStream(file.getBytes()));

        return s3Object;
    }


    @Test
    @Transactional
    @Order(1)
    @DisplayName("goalContent 있는 Challenge 생성")
    void shouldCreateChallengeWithGoalContent() {

        // given
        Integer testMemberId = 1;
        MemberEntity testMember = memberRepository.findById(testMemberId)
                                                  .get();
        List<String> hashTags = new ArrayList<>();
        String hashTagStr = "아침";
        hashTags.add(hashTagStr);
        String createGoalContent = "그런 목표가 있습니다.";
        ChallengeCreateRequest challengeCreateRequest = new ChallengeCreateRequest(
            LocalDate.now(), hashTags, createGoalContent);

        // when
        challengeService.saveChallenge(testMember, challengeCreateRequest);

        // then
        ChallengeEntity actualChallenge = challengeRepository.findByMemberId(testMemberId)
                                                             .getLast();
        assertEquals(createGoalContent, actualChallenge.getGoalContent());
    }

    @Test
    @Transactional
    @Order(2)
    @DisplayName("goalContent 없는 Challenge 생성")
    void shouldCreateChallengeWithoutGoalContent() {

        // given
        Integer testMemberId = 1;
        MemberEntity testMember = memberRepository.findById(testMemberId)
                                                  .get();
        List<String> hashTags = new ArrayList<>();
        String hashTagStr = "아침";
        hashTags.add(hashTagStr);
        ChallengeCreateRequest challengeCreateRequest = new ChallengeCreateRequest(
            LocalDate.now(), hashTags, null);

        // when
        challengeService.saveChallenge(testMember, challengeCreateRequest);

        // then
        ChallengeEntity actualChallenge = challengeRepository.findByMemberId(testMemberId)
                                                             .getLast();
        assertNull(actualChallenge.getGoalContent());
    }

    @Test
    @Transactional
    @Order(3)
    @DisplayName("특정 유저의 모든 챌린지 보기")
    void shouldGetAllChallengeByMember() {

        // given
        Integer testMemberId = 1;
        Mockito.when(s3Service.getS3StartUrl())
               .thenReturn(s3StartUrl);

        // when
        ChallengeResponse challengeResponse = challengeService.getChallenge(
            testMemberId);

        // then
        List<ChallengeEntity> expectedChallenges = challengeRepository.findByMemberId(testMemberId);
        List<Challenge> actualChallenges = challengeResponse.getChallenges();

        for (int i = 0; i < expectedChallenges.size(); i++) {
            assertEquals(
                s3StartUrl + expectedChallenges.get(i)
                                               .getThumbnailUrl(),
                actualChallenges.get(i)
                                .getThumbnailUrl()
            );
        }
    }


    @Test
    @Transactional
    @Order(4)
    @DisplayName("자신의 특정 챌린지가 삭제되어야 한다")
    void shouldDeleteChallengeWhenOwn() {

        //given
        Integer memberId = 1;
        Long challengeId = 2L;
        List<SnapshotEntity> beforeSnapshots = snapshotService.getAllSnapshotByChallenge(
            challengeId);

        // when
        challengeService.deleteChallenge(memberId, challengeId);

        // then
        Optional<ChallengeEntity> expectedChallenge = challengeRepository.findById(
            challengeId);
        assertTrue(expectedChallenge.isEmpty());

        List<SnapshotEntity> afterSnapshots = snapshotService.getAllSnapshotByChallenge(
            challengeId);
        List<ChallengeHashTagEntity> afterChallengeHashTags = challengeHashTagRepository.findAllByChallengeId(
            challengeId);

        assertEquals(0, afterSnapshots.size());
        assertEquals(0, afterChallengeHashTags.size());
    }

    @Test
    @Transactional
    @Order(5)
    @DisplayName("자신의 것이 아닌 Challenge는 삭제할 수 없다")
    void shouldNotDeleteChallengeWhenNotOwn() {

        //given
        Integer memberId = 2;
        Long targetChallengeId = 1L;

        assertThrows(CustomException.class, () ->
            challengeService.deleteChallenge(memberId, targetChallengeId));

    }

    @Test
    @Transactional
    @Order(6)
    @DisplayName("자신의 특정 챌린지에 대하여 기간을 연장한다")
    void shouldExtendOwnChallenge() {

        //given
        Long targetChallengeId = 1L;
        Integer memberId = 1;
        ChallengeEntity beforeChallenge = challengeRepository.findById(targetChallengeId)
                                                             .get();
        int expectedDiffDay = (int) ChronoUnit.DAYS.between(beforeChallenge.getStartedAt(),
            beforeChallenge.getEndedAt()) + 21;

        // when
        challengeService.extendChallenge(memberId, targetChallengeId);

        // then
        ChallengeEntity actualChallenge = challengeRepository.findById(targetChallengeId)
                                                             .get();
        int actualDiffDay = (int) ChronoUnit.DAYS.between(actualChallenge.getStartedAt(),
            actualChallenge.getEndedAt());

        assertEquals(actualDiffDay, expectedDiffDay);

    }


    @Test
    @Transactional
    @Order(7)
    @DisplayName("타 유저의 Feed에서 Challenge 이어가기를 수행한다")
    void shouldRelayChallengeFromOtherFeed() {

        //given
        Long feedIdToTest = 2L;
        Integer memberId = 1;
        LocalDate expectedDate = LocalDate.now()
                                          .plusDays(1);
        ChallengeRelayRequest request = new ChallengeRelayRequest(
            expectedDate,
            null
        );

        // when
        challengeService.relayChallenge(memberId, feedIdToTest, request);

        // then
        Optional<ChallengeEntity> newChallenge = challengeRepository.findByParentId(feedIdToTest);

        assertTrue(newChallenge.isPresent());
        assertEquals(expectedDate, newChallenge.get()
                                               .getStartedAt());

        List<ChallengeHashTagEntity> challengeHashTags = challengeHashTagRepository.findAllByChallengeId(
            newChallenge.get()
                        .getId());
        List<FeedHashTagEntity> feedHashTags = feedHashTagRepository.findAllByFeedId(feedIdToTest);

        assertAll("Challenge should have same hashtags as the feed",
            () -> assertEquals(challengeHashTags.size(), feedHashTags.size()),
            () -> IntStream.range(0, challengeHashTags.size())
                           .forEach(i -> assertEquals(
                               challengeHashTags.get(i)
                                                .getHashTagOption()
                                                .getContent(),
                               feedHashTags.get(i)
                                           .getHashTagOption()
                                           .getContent())
                           )
        );
    }

    @Test
    @Transactional
    @Order(8)
    @DisplayName("특정 챌린지의 Polygon 정보를 String값으로 반환한다")
    void shouldGetPolygonStringByChallenge() throws IOException {

        String afterPolygonName = "test_polygon.png";
        String afterPolygonPath = "src/test/resources/image/" + afterPolygonName;
        MockMultipartFile testPolygonFile = getSampleText(afterPolygonPath, afterPolygonName);
        S3Object mockS3Object = createS3ObjectFromMultipartFile(testPolygonFile);
        when(s3Service.getFile(any(String.class))).thenReturn(mockS3Object);

        // given
        Long challengeId = 2L;
        Integer memberId = 1;

        // when
        ChallengePolygonResponse response = challengeService.getPolygonByChallenge(memberId,
            challengeId);

        // then
        String expectedPolygonStr = new String(testPolygonFile.getBytes(), StandardCharsets.UTF_8);

        assertEquals(expectedPolygonStr, response.getPolygon());

    }


    @Test
    @Transactional
    @Order(9)
    @DisplayName("특정 Cahllenge에 대하여 Object와 Polygon 파일을 저장합니다.")
    void shouldSaveObjectAndPolygonFile() {

        // given
        String afterPolygonName = "test_polygon.png";
        String polygonContent = "testPolygon";
        String afterPolygonPath = "src/test/resources/image/" + afterPolygonName;
        MockMultipartFile polygonFile = getSampleText(afterPolygonPath, afterPolygonName);

        String afterObjectName = "test_object.png";
        String afterObjectPath = "src/test/resources/image/" + afterObjectName;
        MockMultipartFile objectFile = getSampleImage(afterObjectPath, afterObjectName);

        Long testChallengeId = 1L;
        Integer testMemberId = 1;

        when(s3Service.uploadStringAsTextFile(polygonContent, "polygon")).thenReturn(
            afterPolygonName);
        when(s3Service.uploadFile(objectFile)).thenReturn(afterObjectName);

        // when
        challengeService.saveObjectAndPolygon(
            testMemberId, testChallengeId, polygonContent, objectFile);

        // then
        ChallengeEntity challenge = challengeRepository.findById(testChallengeId)
                                                       .get();

        assertEquals("/" + afterObjectName, challenge.getObjectUrl());
        assertEquals("/" + afterPolygonName, challenge.getPolygonUrl());

    }


    @Test
    @Transactional
    @Order(10)
    @DisplayName("특정 스냅샷 추가시, 유사성이 높다면 스냅샷이 등록된다")
    void shouldSaveWhenHighSimilarity() throws IOException {

        // given
        String afterSnapshotName = "test_snapshot3.png";
        String afterSnapshotPath = "src/test/resources/image/" + afterSnapshotName;
        MockMultipartFile snapshotFile = getSampleText(afterSnapshotPath, afterSnapshotName);

        String objectName = "test_object.png";
        String objectPath = "src/test/resources/image/" + objectName;
        S3Object mockS3Object = createMockS3Object(objectPath);

        when(s3Service.getFile(any(String.class))).thenReturn(mockS3Object);
        when(s3Service.uploadFile(snapshotFile)).thenReturn(afterSnapshotName);

        Long challengeId = 2L;
        Integer memberId = 1;

        // when
        challengeService.setSnapshotProcedure(memberId, challengeId, snapshotFile);

        // then
        SnapshotEntity actualSnapshot = snapshotService.getAllSnapshotByChallenge(challengeId)
                                                       .getLast();

        assertEquals("/" + afterSnapshotName, actualSnapshot.getImageUrl());

    }


    @Test
    @Transactional
    @Order(10)
    @DisplayName("특정 스냅샷 추가시, 유사성이 낮다면 스냅샷이 등록되지 않는다")
    void shouldSaveWhenLowSimilarity() throws IOException {

        String afterSnapshotName = "test_snapshot3.png";
        String afterSnapshotPath = "src/test/resources/image/" + afterSnapshotName;
        MockMultipartFile snapshotFile = getSampleText(afterSnapshotPath, afterSnapshotName);

        String objectName = "test_object.png";
        String objectPath = "src/test/resources/image/" + objectName;
        S3Object mockS3Object = createMockS3Object(objectPath);

        doThrow(new CustomException(ErrorCode.NOT_HIGH_SIMILARITY_SNAPSHOT)).when(
                                                                                visionAIService)
                                                                            .checkSimilarity(
                                                                                any(MultiValueMap.class));

        when(s3Service.getFile(any(String.class))).thenReturn(mockS3Object);
        when(s3Service.uploadFile(snapshotFile)).thenReturn(afterSnapshotName);

        Long challengeId = 2L;
        Integer memberId = 1;

        // when
        assertThrows(CustomException.class, () -> {
            challengeService.setSnapshotProcedure(memberId, challengeId, snapshotFile);
        });

        // then
        List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(challengeId);
        assertNotEquals(("/" + afterSnapshotName), snapshots.get(snapshots.size() - 1)
                                                            .getImageUrl());


    }

    @Test
    @Transactional
    @Order(10)
    @DisplayName("특정 스냅샷 추가시, 최초 등록이면 썸네일이 등록된다")
    void shouldSetThumbnailWhenInitAddSnapshot() throws IOException {

        // given
        String afterSnapshotName = "test_snapshot3.png";
        String afterSnapshotPath = "src/test/resources/image/" + afterSnapshotName;
        MockMultipartFile snapshotFile = getSampleText(afterSnapshotPath, afterSnapshotName);

        String objectName = "test_object.png";
        String objectPath = "src/test/resources/image/" + objectName;
        S3Object mockS3Object = createMockS3Object(objectPath);

        when(s3Service.getFile(any(String.class))).thenReturn(mockS3Object);
        when(s3Service.uploadFile(snapshotFile)).thenReturn(afterSnapshotName);

        Long challengeId = 1L;
        Integer memberId = 1;

        // when
        challengeService.setSnapshotProcedure(memberId, challengeId, snapshotFile);

        // then
        SnapshotEntity actualSnapshot = snapshotService.getAllSnapshotByChallenge(challengeId)
                                                       .getLast();
        ChallengeEntity actualChallenge = challengeRepository.findById(challengeId)
                                                             .get();

        assertEquals("/" + afterSnapshotName, actualSnapshot.getImageUrl());
        assertEquals("/" + afterSnapshotName, actualChallenge.getThumbnailUrl());
    }


}
