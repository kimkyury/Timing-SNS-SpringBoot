package com.kkukku.timing.memberTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.entities.MemberEntity.Gender;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.member.requests.MemberRegisterRequest;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.apis.member.services.MemberService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(properties = "spring.profiles.active=local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private MemberEntity memberEntity;
    private String memberEmail;

    private static MultipartFile imageFile;

    @BeforeAll
    static void init() {
        imageFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
    }

    @BeforeEach
    void setUp() {
        // 가정된 회원 데이터
        memberEmail = "test3@com";
        memberEntity = new MemberEntity(memberEmail);
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
    @DisplayName("최초 로그인 유저의 정보 수정")
    void shouldUpdateMemberWhenInitLogin() {
        memberRepository.save(memberEntity);
        Integer memberId = memberRepository.findByEmail(memberEmail)
                                           .get()
                                           .getId();

        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(
            "테스트",
            2000,
            Gender.F
        );
        MockMultipartFile multipartFile = getSampleImage();
        memberService.registerMember(memberId, memberRegisterRequest, multipartFile);

        MemberEntity updatedMember = memberRepository.findByEmail(memberEmail)
                                                     .get();

        assertEquals("isSmaNickname", memberRegisterRequest.getNickname(),
            updatedMember.getNickname());
        assertEquals("isSmaNickname", memberRegisterRequest.getGender(), updatedMember.getGender());
        assertEquals("isSmaNickname", memberRegisterRequest.getBirthyear(),
            updatedMember.getBirthyear());
        assertNotNull("hasProfileURL", updatedMember.getProfileImageUrl());

    }

    @Test
    @Order(2)
    @DisplayName("유저의 정보 조회")
    void shouldGetMemberInfo() {

        MemberDetailResponse memberDetailResponse = memberService.getMemberInfo("kkr@com");
        MemberEntity member = memberRepository.findById(1)
                                              .get();

        assertEquals("canReadNickname", memberDetailResponse.getNickname(), member.getNickname());
        assertEquals("canReadProfile", memberDetailResponse.getProfileImageUrl(),
            member.getProfileImageUrl());
    }

    @Test
    @Order(3)
    @DisplayName("유저의 탈퇴")
    void ShouldUpdateMemberIsDelete() {

        memberService.deleteMember(1);
        MemberEntity member = memberRepository.findById(1)
                                              .get();

        assertEquals("isDeleteTrue", true, member.isDelete());


    }
}
