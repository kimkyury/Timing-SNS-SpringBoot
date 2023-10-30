package com.kkukku.timing.apis.members.repositories;

import com.kkukku.timing.apis.members.entities.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    Optional<MemberEntity> findByEmail(String email);
}
