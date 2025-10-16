package com.surest.repository; // MemberRepository.java

import com.surest.entity.Member;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberRepository
    extends JpaRepository<Member, UUID>, JpaSpecificationExecutor<Member> {
  Page<Member> findByFirstNameContainingIgnoreCase(String firstName, Pageable page);

  Page<Member> findByLastNameContainingIgnoreCase(String lastName, Pageable page);

  boolean existsByEmail(String email);
}
