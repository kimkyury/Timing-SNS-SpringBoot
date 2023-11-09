package com.kkukku.timing.apis.challenge.controllers;


import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.requests.CheckCoordinateRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengePolygonResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.services.ChallengeService;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/challenges")
@Tag(name = "2. Challenge", description = "Challenge API")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "본인의 Challenge 생성", tags = {"2. Challenge"},
        description = "Challenge가 생성 && 새로운 hashTag 생성 && 연관관계 정보 생성 ")
    @PostMapping(value = "")
    public ResponseEntity<Void> createChallenge(
        @Validated @RequestBody ChallengeCreateRequest challengeCreateRequest) {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.createChallengeProcedure(memberId, challengeCreateRequest);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "본인의 Challenge 목록 가져오기", tags = {"2. Challenge"},
        description = "Main, Mypage에 사용될 본인 Challenge 목록들입니다. ")
    @GetMapping(value = "")
    public ResponseEntity<ChallengeResponse> getChallenge() {
        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        ChallengeResponse challengeResponse = challengeService.getChallenge(memberId);

        return ApiResponseUtil.success(challengeResponse);
    }

    @Operation(summary = "본인의 특정 Challenge 삭제하기", tags = {"2. Challenge"},
        description = "본인의 특정 Challenge를 삭제하며, 관련 Snapshot들도 삭제합니다.  ")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.deleteChallenge(memberId, id);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "본인의 특정 Challenge 기간 연장하기", tags = {"2. Challenge"},
        description = "본인의 특정 Challenge 완료 후, 21일을 연장할 수 있습니다. ")
    @PatchMapping(value = "/{id}/extension")
    public ResponseEntity<Void> extendChallenge(@PathVariable Long id) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.extendChallenge(memberId, id);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "타 멤버의 특정 Challenge 이어하기", tags = {"2. Challenge"},
        description = "타 회원의 Feed 정보를 이어서 본인의 Challenge로 생성합니다. HastTag 정보가 연동됩니다. GoalContent 정보는 연동되지 않습니다.(별도 작성) ")
    @PostMapping(value = "/{id}/relay")
    public ResponseEntity<Void> relayChallenge(@PathVariable Long id,
        @Valid @RequestBody ChallengeRelayRequest request) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        challengeService.relayChallenge(memberId, id, request);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "특정 Challenge의 SnapShot 촬영을 위한 Polygon 얻기", tags = {
        "2. Challenge"},
        description = "SnapShot 촬영 시 가이드 윤곽선을 그리기 위한 Polygon을 String 형태로 받아옵니다. ")
    @GetMapping(value = "/{id}/polygon")
    public ResponseEntity<ChallengePolygonResponse> getSnapshotByChallenge(@PathVariable Long id) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();

        ChallengePolygonResponse challengePolygonResponse = challengeService.getPolygonByChallenge(
            memberId, id);

        return ApiResponseUtil.success(challengePolygonResponse);
    }

    @Operation(summary = "특정 Challenge의 Polygon, Object 사진 저장", tags = {
        "2. Challenge"},
        description = "특정 Challenge의 Snapshot 최초 등록시, 사진의 객체 최종 확정을 통해 Polygon, Object가 저장됩니다")
    @PostMapping(value = "/{id}/objects", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> savePolygonAndObject(@PathVariable Long id,
        @RequestPart MultipartFile polygon,
        @RequestPart MultipartFile object) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();
        challengeService.saveObjectAndPolygon(memberId, id, polygon, object);

        return ApiResponseUtil.success();
    }

    // 이하로 Python Proxy APIs
    @Operation(summary = "특정 Challenge의 Snapshot 추가(미완)", tags = {"2. Challenge"},
        description = "특정 Challenge의 Snapshot 추가 시, AI server를 통한 유사도 판정 이후 Snapshot이 저장됩니다."
            + "<br/> *상태코드:400일 시, 객체 유사도가 낮아서 실패한 경우입니다."
            + " <br/> *상태코드:200일 시, snapshot이 저장됩니다. (현재 무조건 200)"
            + "<br/> *AIServer에는 'snapshot'에 이미지가, 'objectUrl'로 object 이미지가 전송됩니다")
    @PostMapping(value = "/{id}/snapshots", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> setSnapshot(
        @PathVariable Long id, @RequestPart MultipartFile snapshot) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();
        challengeService.setSnapshotProcedure(memberId, id, snapshot);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "특정 Challenge의 최초 Snapshot 객체 탐지 요청(미완)", tags = {
        "2. Challenge"},
        description =
            "특정 Challenge의 최초 Snapshot 추가 시, AI Server로 객체 탐지를 요청하고 Object 이미지를 리턴(미완)합니다. "
                + "<br/> *상태코드:4xx일 경우, 객체를 발견하지 못한 것입니다 (현재는 무조건 200)"
                + "<br/> *상태코드:200일 경우, object.png을 보냅니다 (현재는 안 보내짐) "
                + "<br/> AIServer에는 'snapshot'이름으로 이미지가 전송됩니다. ")
    @PostMapping(value = "/{id}/snapshots/objects/detection",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getObjectInSnapshot(@PathVariable Long id,
        @RequestPart MultipartFile snapshot) {

        ResponseSpec response = challengeService.getDetectedObject(snapshot);

        byte[] objectImage = response.body(byte[].class);
        HttpHeaders httpHeaders = response.toEntity(String.class)
                                          .getHeaders();

        return ApiResponseUtil.success(httpHeaders, objectImage);
    }


    @Operation(summary = "특정 Challenge의 최초 Snapshot추가 시, 객체 선택 및 좌표 유효성 검사", tags = {
        "2. Challenge"},
        description =
            "특정 Challenge의 최초 Snapshot 추가 시, AI Server로 현재 좌표의 유효성을 요청(미완)합니다. "
                + "<br/> *상태코드:4xx일 경우, 올바르지 않은 좌표값입니다. (현재는 무조건 200)"
                + "<br/> *상태코드:200일 경우, object.png을 Body로, Polygon String값을 헤더로 리턴합니다. (현재는 안 보내짐) "
                + "<br/> AIServer에는 FormData형식으로, 'snapshot'에 이미지가, 'coordinate'에 좌표 JSON이 String형태로 전송됩니다(NOTION참고). ")
    @PostMapping(value = "{id}/snapshots/objects/choose", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<byte[]> chooseObjectByCoordinate(
        @PathVariable Long id,
        @Valid @RequestPart CheckCoordinateRequest request,
        @RequestPart MultipartFile snapshot) {

        System.out.println("-------hi! this is choice");

        ResponseSpec response = challengeService.getChoiceObject(request, snapshot);

        byte[] objectImage = response.body(byte[].class);
        HttpHeaders httpHeaders = response.toEntity(String.class)
                                          .getHeaders();

        return ApiResponseUtil.success(httpHeaders, objectImage);
    }


}
