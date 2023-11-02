package com.kkukku.timing.apis.member.repositories;

import com.kkukku.timing.apis.member.entities.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    Optional<MemberEntity> findByEmail(String email);
}
