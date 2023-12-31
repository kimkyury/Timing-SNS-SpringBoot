package com.kkukku.timing.apis.member.services;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.repositories.MemberRepository;
import com.kkukku.timing.apis.member.requests.MemberUpdateRequest;
import com.kkukku.timing.apis.member.responses.MemberDetailResponse;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public void saveIfNotExist(String email, String profileImageUrl, String nickname) {

        if (memberRepository.findByEmail(email)
                            .isPresent()) {
            return;
        }
        memberRepository.save(new MemberEntity(email, profileImageUrl, nickname));
    }

    public void updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest,
        MultipartFile multipartFile) {

        MemberEntity member = getMemberById(memberId);
        if (memberUpdateRequest != null) {
            member.setNickname(memberUpdateRequest.getNickname());
            memberRepository.save(member);
        }

        if (multipartFile != null) {
            String profileImageUrl = "/" + s3Service.uploadFile(multipartFile);
            member.setProfileImageUrl(profileImageUrl);
            memberRepository.save(member);
        }
    }

    public MemberDetailResponse getMemberInfo(String memberEmail) {

        MemberEntity memberEntity = getMemberByEmail(memberEmail);

        memberEntity.saveProfileImgUrlWithS3(s3Service);

        return new MemberDetailResponse(memberEntity);
    }

    public void deleteMember(Integer memberId) {
        MemberEntity member = getMemberById(memberId);

        String profileImgUrl = member.getProfileImageUrl();
        if (!profileImgUrl.startsWith("http")) {
            s3Service.deleteFile(profileImgUrl);
        }

        member.delete();

        memberRepository.save(member);
    }

    public MemberEntity getMemberById(Integer id) {
        return memberRepository.findById(id)
                               .orElseThrow(() -> new CustomException(
                                   ErrorCode.NOT_EXIST_MEMBER_EMAIL));
    }

    public MemberEntity getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                               .orElseThrow(() -> new CustomException(
                                   ErrorCode.NOT_EXIST_MEMBER_EMAIL));
    }

}
