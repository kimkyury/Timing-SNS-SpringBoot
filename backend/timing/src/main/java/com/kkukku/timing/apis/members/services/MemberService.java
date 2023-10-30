package com.kkukku.timing.apis.members.services;

import com.kkukku.timing.apis.members.entities.MemberEntity;
import com.kkukku.timing.apis.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveIfNotExist(String email) {
        if (memberRepository.findByEmail(email)
                            .isPresent()) {
            return;
        }

        memberRepository.save(new MemberEntity(email));
    }
}
