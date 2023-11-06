package com.kkukku.timing.apis.member.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.member.requests.MemberUpdateRequest;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.s3.services.S3Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @MockBean
    private S3Service s3Service;

    private static String testEmail;
    private static String originalNickname;
    private static String originalProfileUrl;

    private Integer testId;

    @BeforeAll
    static void init() {
        testEmail = "test@com";
        originalNickname = "TESTER";
        originalProfileUrl = "src/test/resources/Chirachino.jpg";
    }

    @BeforeEach
    void setUp() {
        // 가정된 회원 데이터
        memberRepository.findByEmail(testEmail)
                        .ifPresent(memberRepository::delete);

        MemberEntity memberEntity = new MemberEntity(testEmail, originalProfileUrl,
            originalNickname);
        memberRepository.save(memberEntity);

        testId = memberRepository.findByEmail(testEmail)
                                 .get()
                                 .getId();

    }


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
    @Order(1)
    @DisplayName("유저의 Nickname 수정되어야 한다")
    void shouldUpdateNickname() {

        // given
        String updatedNickname = "수정된 닉네임";
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(updatedNickname);

        // when
        memberService.updateMember(testId, memberUpdateRequest, null);

        // then
        MemberEntity updatedMember = memberRepository.findByEmail(testEmail)
                                                     .get();
        assertEquals("isUpdatedNickname", updatedNickname, updatedMember.getNickname());
        assertEquals("isOriginalProfileUrl", originalProfileUrl,
            updatedMember.getProfileImageUrl());
    }

    @Test
    @Order(2)
    @DisplayName("유저의 profileImageUrl 수정되어야 한다")
    void shouldUpdateProfileImg() {

        String updatedProfileUrl = "http://example.com/profile.png";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(updatedProfileUrl);
        MockMultipartFile multipartFile = getSampleImage();

        memberService.updateMember(testId, null, multipartFile);

        MemberEntity updatedMember = memberRepository.findByEmail(testEmail)
                                                     .get();

        assertEquals("isOriginalNickname", originalNickname, updatedMember.getNickname());
        assertEquals("isUpdatedProfileUrl", updatedProfileUrl, updatedMember.getProfileImageUrl());
    }

    @Test
    @Order(3)
    @DisplayName("유저의 Nickname, ProfileImageUrl 수정되어야 한다")
    void shouldUpdateNicknameAndProfileImg() {

        String updatedProfileUrl = "http://example.com/profile.png";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(updatedProfileUrl);
        String updatedNickname = "수정된 닉네임";
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(updatedNickname);
        MockMultipartFile multipartFile = getSampleImage();

        memberService.updateMember(testId, memberUpdateRequest, multipartFile);

        MemberEntity updatedMember = memberRepository.findByEmail(testEmail)
                                                     .get();
        assertEquals("isUpdatedNickname", updatedNickname, updatedMember.getNickname());
        assertEquals("isUpdatedNickname", updatedProfileUrl, updatedMember.getProfileImageUrl());
    }

    @Test
    @Order(4)
    @DisplayName("유저의 정보가 조회되어야 한다")
    void shouldGetMemberInfo() {

        String searchMemberEmail = "kkr@test.com";
        MemberEntity searchedMember = memberRepository.findByEmail(searchMemberEmail)
                                                      .get();

        MemberDetailResponse memberDetailResponse = memberService.getMemberInfo(searchMemberEmail);

        MemberDetailResponse expectedMemberDetailResponse = new MemberDetailResponse(
            searchedMember);
        expectedMemberDetailResponse.setProfileImageUrl(
            s3Service.getS3StartUrl() + searchedMember.getProfileImageUrl());
        System.out.println(expectedMemberDetailResponse);

        assertEquals("canReadNickname", expectedMemberDetailResponse, memberDetailResponse);

    }

    @Test
    @Order(5)
    @DisplayName("유저 탈퇴시, 탈퇴 필드 변경과 관련 Field가 초기화되어야 한다")
    void ShouldUpdateMemberIsDelete() {

        MemberEntity originalMember = memberRepository.findById(1)
                                                      .get();

        memberService.deleteMember(1);

        MemberEntity deletedMember = memberRepository.findById(1)
                                                     .get();
        Assertions.assertTrue(deletedMember.getIsDelete());
        Assertions.assertEquals("탈퇴한 사용자", deletedMember.getNickname());
        Assertions.assertEquals("/default_profile.png", deletedMember.getProfileImageUrl());
        Assertions.assertNotEquals(originalMember.getEmail(), deletedMember.getEmail());

    }
}
