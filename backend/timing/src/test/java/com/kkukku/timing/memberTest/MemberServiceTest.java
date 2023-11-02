package com.kkukku.timing.MemberTest;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.member.services.MemberService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private static MemberEntity testMemberEntity;

    @BeforeAll
    static void init() {
        testMemberEntity = new MemberEntity("test3@com");
    }

    @Test
    @DisplayName("최초 로그인 유저의 정보 수정")
    void test1() {

        Optional<MemberEntity> member = memberRepository.findByEmail("test@com");
        System.out.println(member);

        List<MemberEntity> lists = memberRepository.findAll();
        System.out.println("Datas1 : " + lists);

        memberRepository.save(testMemberEntity);
        Optional<MemberEntity> newMember = memberRepository.findByEmail("test3@com");
        System.out.println(newMember.get());
        List<MemberEntity> lists2 = memberRepository.findAll();
        System.out.println("Datas2 : " + lists2);

    }

    @Test
    @DisplayName("수정된 정보 조회")
    void test2() {
    }

    @Test
    @DisplayName("유저의 탈퇴")
    void testDeleteUser() {

    }

    @Test
    @DisplayName("탈퇴된 유저의 정보 조회")
    void testGetUserInfo() {

    }
}
