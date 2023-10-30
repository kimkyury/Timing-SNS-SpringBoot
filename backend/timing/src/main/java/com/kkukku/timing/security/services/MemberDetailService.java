package com.kkukku.timing.security.services;

import com.kkukku.timing.apis.members.entities.MemberEntity;
import com.kkukku.timing.apis.members.repositories.MemberRepository;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.security.entities.MemberDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByEmail(username)
                                                    .orElseThrow(() ->
                                                        new CustomException(
                                                            ErrorCode.NOT_FOUND_MEMBER_EMAIL));

        return new MemberDetailEntity(memberEntity);
    }
}
