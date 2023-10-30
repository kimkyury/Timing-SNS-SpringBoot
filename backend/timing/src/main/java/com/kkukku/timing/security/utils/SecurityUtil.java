package com.kkukku.timing.security.utils;

import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.security.entities.MemberDetailEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class SecurityUtil {

    public static Integer getLoggedInMemberPrimaryKey() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.MISSING_LOGIN_INFO);
        }

        Object principal = authentication.getPrincipal();
        Optional<Integer> id = Optional.empty();

        if (principal instanceof MemberDetailEntity) {
            id = Optional.ofNullable(((MemberDetailEntity) principal).getUserId());
        }

        return id.orElseThrow(() -> new CustomException(ErrorCode.MISSING_LOGIN_INFO));
    }

    public static String getLoggedInMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.MISSING_LOGIN_INFO);
        }

        Object principal = authentication.getPrincipal();
        Optional<String> email = Optional.empty();

        if (principal instanceof MemberDetailEntity) {
            email = Optional.ofNullable(((MemberDetailEntity) principal).getUsername());
        }

        return email.orElseThrow(() -> new CustomException(ErrorCode.MISSING_LOGIN_INFO));
    }
}
