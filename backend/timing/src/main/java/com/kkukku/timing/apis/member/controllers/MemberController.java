package com.kkukku.timing.apis.member.controllers;

import com.kkukku.timing.apis.member.requests.MemberRegisterRequest;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "1. Member", description = "Member API")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Member의 가입 후 최초 접근, 정보 등록", tags = {"1. Member"})
    @PatchMapping(value = "/", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> registerMember(
        @Valid @RequestPart MemberRegisterRequest memberRegisterRequest,
        @Valid @RequestPart("profileImage") MultipartFile profileImage) {

        Integer memberId = SecurityUtil.getLoggedInMemberPrimaryKey();
        memberService.registerMember(memberId, memberRegisterRequest, profileImage);

        return ApiResponseUtil.success();
    }

    @Operation(summary = "Member의 본인 정보 조회", tags = {"1. Member"})
    @GetMapping(value = "/")
    public ResponseEntity<MemberDetailResponse> getOwnMemberInfo() {

        String memberEmail = SecurityUtil.getLoggedInMemberEmail();
        MemberDetailResponse memberDetailResponse = memberService.getMemberInfo(memberEmail);

        return ApiResponseUtil.success(memberDetailResponse);
    }

    @Operation(summary = "Member의 타 멤버 정보 조회", tags = {"1. Member"})
    @GetMapping(value = "/")
    public ResponseEntity<MemberDetailResponse> getOtherMemberInfo(
        @RequestParam(name = "email", required = false) String memberEmail) {

        MemberDetailResponse memberDetailResponse = memberService.getMemberInfo(memberEmail);

        return ApiResponseUtil.success(memberDetailResponse);
    }

}
