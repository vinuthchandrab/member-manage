package com.surest.service;

import com.surest.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface MemberService {
  Page<Member> findMembers(
      int page, int size, String sort, Optional<String> firstName, Optional<String> lastName);

  Member findById(UUID id);

  Member create(Member member);

  Member update(UUID id, Member update);

  void delete(UUID id);
}
