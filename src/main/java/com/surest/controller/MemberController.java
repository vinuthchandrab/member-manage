package com.surest.controller;

import com.surest.dto.MemberDto;
import com.surest.dto.mapper.MemberMapper;
import com.surest.entity.Member;
import com.surest.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
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


    @GetMapping("/{id}")
    public Member getMember(@PathVariable @NotEmpty String id) {
            return memberService.findById(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDto memberdto) {
        Member member= memberMapper.toEntity(memberdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable String id, @Valid @RequestBody MemberDto memberDto) {
        Member member = memberMapper.toEntity(memberDto);
        return memberService.update(UUID.fromString(id), member);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable @NotBlank String id) {
        try {
            memberService.delete(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid UUID format: " + id);
        }
    }
}
