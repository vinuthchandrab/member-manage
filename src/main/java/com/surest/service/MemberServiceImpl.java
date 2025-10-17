package com.surest.service; // MemberService.java

import com.surest.entity.Member;
import com.surest.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
  public final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
  public Page<Member> findMembers(
      int page, int size, String sort, Optional<String> firstName, Optional<String> lastName) {
    Sort sorting = Sort.by(sort.split(",")[0]).ascending();
    if (sort.endsWith(",desc")) {
      sorting = sorting.descending();
    }
    Pageable pageable = PageRequest.of(page, size, sorting);

    // Basic filtering example; for more complex use JpaSpecificationExecutor or custom
    // Specification
    if (firstName.isPresent()) {
      return memberRepository.findByFirstNameContainingIgnoreCase(firstName.get(), pageable);
    } else if (lastName.isPresent()) {
      return memberRepository.findByLastNameContainingIgnoreCase(lastName.get(), pageable);
    }
    return memberRepository.findAll(pageable);
  }

  @Cacheable(value = "member", key = "#id")
  @Override
  public Member findById(UUID id) {
    return memberRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));
  }

  @Transactional
  @Override
  public Member create(Member member) {
    if (memberRepository.existsByEmail(member.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }
    return memberRepository.save(member);
  }

  @Transactional
  @Override
  public Member update(UUID id, Member update) {
    Member member = findById(id);
    member.setFirstName(update.getFirstName());
    member.setLastName(update.getLastName());
    member.setDateOfBirth(update.getDateOfBirth());
    member.setEmail(update.getEmail());
    return memberRepository.save(member);
  }

  @CacheEvict(value = "member", key = "#id")
  @Override
  public void delete(UUID id) {
    memberRepository.deleteById(id);
  }
}
