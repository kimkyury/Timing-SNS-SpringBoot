package com.kkukku.timing.apis.member.services;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.member.requests.MemberRegisterRequest;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.s3.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public void saveIfNotExist(String email) {
        if (memberRepository.findByEmail(email)
                            .isPresent()) {
            return;
        }

        memberRepository.save(new MemberEntity(email));
    }

    public void registerMember(Integer memberId, MemberRegisterRequest memberRegisterRequest,
        MultipartFile multipartFile) {

        String profileImageUrl = s3Service.uploadFileProcedure(multipartFile);

        memberRepository.findById(memberId)
                        .ifPresent(
                            selectMember -> {
                                selectMember.registerInfo(
                                    memberRegisterRequest, profileImageUrl);
                                memberRepository.save(selectMember);
                            });
    }

    public MemberDetailResponse getMemberInfo(String memberEmail) {

        MemberEntity memberEntity = memberRepository.findByEmail(memberEmail)
                                                    .get();

        String nickname = memberEntity.getNickname();
        String profileImageUrl = memberEntity.getProfileImageUrl();

        return new MemberDetailResponse(
            nickname, profileImageUrl
        );
    }

    public void deleteMember(Integer memberId) {
        memberRepository.findById(memberId)
                        .ifPresent(selectMember -> {
                            selectMember.delete();
                            memberRepository.save(selectMember);
                        });
    }
}
