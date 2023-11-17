package com.kkukku.timing.oauth2.services;

import com.kkukku.timing.apis.member.services.MemberService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) originAttributes.get(
            "kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email")
                                   .toString();
        String profileImageUrl = profile.get("profile_image_url")
                                        .toString();
        String nickname = profile.get("nickname")
                                 .toString();

        memberService.saveIfNotExist(email, profileImageUrl, nickname);

        String userNameAttributeName = userRequest.getClientRegistration()
                                                  .getProviderDetails()
                                                  .getUserInfoEndpoint()
                                                  .getUserNameAttributeName();

        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;

        return new DefaultOAuth2User(authorities, originAttributes,
            userNameAttributeName);
    }
}
