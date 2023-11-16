package com.kkukku.timing.apis.test.controllers;

import static com.kkukku.timing.response.ApiResponseUtil.success;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.apis.challenge.services.SnapshotService;
import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import com.kkukku.timing.apis.feed.responses.FeedDetailResponse;
import com.kkukku.timing.apis.feed.services.FeedService;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.test.requests.FeedDummyRequest;
import com.kkukku.timing.apis.test.responses.FeedResponse;
import com.kkukku.timing.apis.test.responses.MemberResponse;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import com.kkukku.timing.security.services.MemberDetailService;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "0. Test", description = "Test API")
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;
    private final ChallengeService challengeService;
    private final SnapshotService snapshotService;
    private final MemberDetailService memberDetailService;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedHashTagService feedHashTagService;
    private final TestRepository testRepository;
    private final TestFeedRepository testFeedRepository;
    private final FeedService feedService;
    private final ChallengeRepository challengeRepository;

    private boolean isFind;

    @Operation(summary = "응답테스트", tags = {"0. Test"})
    @GetMapping("/ping")
    public String ping() {
        return "pong3";
    }

    @Operation(summary = "s3사진 업로드 테스트", tags = {"0. Test"})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("image") MultipartFile image) {

        String fileName = s3Service.uploadFile(image);
        return success("Saved fileName: " + fileName);
    }

    @Operation(summary = "s3사진 가져오기 테스트", tags = {"0. Test"})
    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam String fileName) {

        Optional<S3Object> s3ObjectOptional = Optional.ofNullable(s3Service.getFile(fileName));
        if (s3ObjectOptional.isEmpty()) {
            return ApiResponseUtil.error(ErrorCode.NOT_FOUND);
        }

        S3Object s3Object = s3ObjectOptional.get();
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(s3Object.getObjectMetadata()
                                                                           .getContentType()))
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                 "attachment; filename=\"" + fileName + "\"")
                             .body(resource);

    }

    @GetMapping(value = "/feeds")
    public ResponseEntity<List<FeedResponse>> getFeeds() {
        return ApiResponseUtil.success(feedRepository.findAll()
                                                     .stream()
                                                     .map(
                                                         feed -> new FeedResponse(feed,
                                                             feedHashTagService.getHashTagsByFeedId(
                                                                 feed.getId()), s3Service))
                                                     .toList());
    }

    @GetMapping(value = "/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ApiResponseUtil.success(memberRepository.findAll()
                                                       .stream()
                                                       .map(MemberResponse::new)
                                                       .toList());
    }

    @PostMapping(value = "/feeds", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> saveFeed(
        @RequestPart List<MultipartFile> snapshots,
        @RequestPart MultipartFile objectImage,
        @RequestPart @Valid FeedDummyRequest feedDummyRequest) {
        if (snapshots.size() % 21 != 0) {
            return ResponseEntity.badRequest()
                                 .body("스냅샷 개수가 21의 배수만 가능");
        }
        // TODO Login START
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            memberDetailService.loadUserByUsername(feedDummyRequest.getMemberEmail()), null,
            AuthorityUtils.NO_AUTHORITIES);

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        // TODO Login END

        // TODO Create Challenge START
        if (feedDummyRequest.getParentId() == null) {
            challengeService.createChallengeProcedure(SecurityUtil.getLoggedInMemberPrimaryKey(),
                new ChallengeCreateRequest(feedDummyRequest.getEndedAt()
                                                           .minusDays(snapshots.size()),
                    feedDummyRequest.getHashTags(), feedDummyRequest.getGoalContent()));
        } else {
            challengeService.relayChallenge(SecurityUtil.getLoggedInMemberPrimaryKey(),
                feedDummyRequest.getParentId(), new ChallengeRelayRequest(
                    feedDummyRequest.getEndedAt()
                                    .minusDays(snapshots.size()),
                    feedDummyRequest.getGoalContent()));
        }
        // TODO Create Challenge END

        ChallengeResponse challengeResponse = challengeService.getChallenge(
            SecurityUtil.getLoggedInMemberPrimaryKey());
        Long challengeId = -1L;

        for (Challenge challenge : challengeResponse.getChallenges()) {
            if (challengeId.equals(-1L)) {
                challengeId = challenge.getId();
            } else if (challengeId < challenge.getId()) {
                challengeId = challenge.getId();
            }
        }

        ChallengeEntity challenge = challengeService.getChallengeById(challengeId);

        // TODO Create snapshot's object, polygon START
        challengeService.saveObjectAndPolygon(SecurityUtil.getLoggedInMemberPrimaryKey(),
            challenge.getId(), feedDummyRequest.getPolygon(), objectImage);
        // TODO Create snapshot's object, polygon END

        // TODO Create Snapshots START
        snapshots.forEach(snapshot -> {
            String url = "/" + s3Service.uploadFile(snapshot);
            snapshotService.createSnapshot(challenge, url);

            if ("/default_thumbnail.png".equals(challenge.getThumbnailUrl())) {
                challenge.setThumbnailUrl(url);
                challengeRepository.save(challenge);
            }
        });
        // Todo Create Snapshots END

        // TODO Convert to Feed START
        feedService.convertToFeed(challengeId);
        // TODO Convert to Feed END

        return ApiResponseUtil.success();
    }

    @PostMapping(value = "/objects/choose",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> chooseObjectByCoordinate(@RequestPart String x,
        @RequestPart String y, @RequestPart MultipartFile snapshot) {
        ResponseSpec response = challengeService.getChoiceObject(x, y, snapshot);
        ResponseEntity<byte[]> responseEntity = response.toEntity(byte[].class);
        byte[] objectImage = responseEntity.getBody();
        HttpHeaders httpHeaders = responseEntity.getHeaders();

        return ApiResponseUtil.success(httpHeaders, objectImage);
    }

    @PostMapping(value = "/objects/detection",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getObjectInSnapshot(@RequestPart MultipartFile snapshot) {
        ResponseSpec response = challengeService.getDetectedObject(snapshot);

        byte[] objectImage = response.body(byte[].class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);

        return ApiResponseUtil.success(httpHeaders, objectImage);
    }

    @PostMapping("/search")
    public void searchTest(@RequestBody SearchTestDto searchTestDto, Pageable pageable) {
        List<Test> list = testRepository.findAllByNameContaining(searchTestDto.getName(), pageable);
    }

    @PostMapping("/search/nori")
    public void searchTest2(@RequestBody SearchTestDto searchTestDto, Pageable pageable) {
        List<TestFeed> list = testFeedRepository.findAllByContentsContaining(
            searchTestDto.getName(), pageable);
//        System.out.println(list.size());
//        for(TestFeed t : list) {
//            System.out.println(t);
//        }
    }


    @Operation(summary = "추천 피드 상세 조회", tags = {"0.Test"},
        description = "개발 중입니다. member_id를 id 자리에 넣어주세요")
    @GetMapping("/feed/recommend/{member_id}")
    public ResponseEntity<List<FeedDetailResponse>> recommendTest(
        @RequestParam(name = "page") Integer page) {

        Pageable pageable = PageRequest.of(page - 1, 3);

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();
        List<FeedDetailResponse> feeds = feedRepository.findFeedsWithScore(memberId, pageable)
                                                       .stream()
                                                       .map(feed -> feedService.getTestFeedDetail(
                                                           feed.getId()))
                                                       .toList();

        return ApiResponseUtil.success(feeds);
    }


}
