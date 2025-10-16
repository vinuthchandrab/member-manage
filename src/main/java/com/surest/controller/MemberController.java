package com.surest.controller;

import com.surest.dto.MemberDto;
import com.surest.dto.mapper.MemberMapper;
import com.surest.entity.Member;
import com.surest.service.MemberServiceImpl;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberServiceImpl memberService;

    private final MemberMapper memberMapper;

    public MemberController(MemberServiceImpl memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @GetMapping
    public Page<Member> listMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName) {
        return memberService.findMembers(page, size, sort, firstName, lastName);
    }

    @Cacheable(value = "member", key = "#id")
    @GetMapping("/{id}")
    public Member getMember(@PathVariable UUID id) {
        return memberService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDto memberdto) {
        Member member= memberMapper.toEntity(memberdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable UUID id, @Valid @RequestBody MemberDto memberDto) {
        Member member = memberMapper.toEntity(memberDto);
        return memberService.update(id, member);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "member", key = "#id")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
