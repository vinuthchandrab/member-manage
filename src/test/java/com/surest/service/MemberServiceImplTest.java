package com.surest.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.surest.entity.Member;
import com.surest.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void findMembers_shouldReturnPageUsingFirstName() {
        Member member = new Member();
        Page<Member> page = new PageImpl<>(List.of(member));
        when(memberRepository.findByFirstNameContainingIgnoreCase(eq("John"), any()))
                .thenReturn(page);

        Page<Member> result = memberService.findMembers(0, 5, "firstName,asc", Optional.of("John"), Optional.empty());

        assertThat(result.getContent()).contains(member);
        verify(memberRepository).findByFirstNameContainingIgnoreCase("John", PageRequest.of(0, 5, Sort.by("firstName").ascending()));
    }

    @Test
    void findMembers_shouldReturnPageUsingLastName() {
        Member member = new Member();
        Page<Member> page = new PageImpl<>(List.of(member));
        when(memberRepository.findByLastNameContainingIgnoreCase(eq("Doe"), any()))
                .thenReturn(page);

        Page<Member> result = memberService.findMembers(0, 5, "lastName,desc", Optional.empty(), Optional.of("Doe"));

        assertThat(result.getContent()).contains(member);
        verify(memberRepository).findByLastNameContainingIgnoreCase("Doe", PageRequest.of(0, 5, Sort.by("lastName").descending()));
    }

    @Test
    void findMembers_shouldReturnAllWhenNoFilters() {
        Member member = new Member();
        Page<Member> page = new PageImpl<>(List.of(member));
        when(memberRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<Member> result = memberService.findMembers(0, 10, "id,asc", Optional.empty(), Optional.empty());

        assertThat(result.getContent()).contains(member);
        verify(memberRepository).findAll(PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    void findById_shouldReturnMemberIfExists() {
        UUID id = UUID.randomUUID();
        Member member = new Member();
        member.setId(id);
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Member result = memberService.findById(id);

        assertThat(result).isEqualTo(member);
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> memberService.findById(id));
    }

    @Test
    void create_shouldThrowIfEmailExists() {
        Member member = new Member();
        member.setEmail("test@example.com");
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> memberService.create(member));
    }

    @Test
    void create_shouldSaveAndReturnMember() {
        Member member = new Member();
        member.setEmail("unique@example.com");
        when(memberRepository.existsByEmail("unique@example.com")).thenReturn(false);
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.create(member);

        assertThat(result).isEqualTo(member);
        verify(memberRepository).save(member);
    }

    @Test
    void update_shouldUpdateAndReturnMember() {
        UUID id = UUID.randomUUID();
        Member existing = new Member();
        existing.setId(id);

        Member update = new Member();
        update.setFirstName("Jane");
        update.setLastName("Doe");
        update.setDateOfBirth(LocalDate.of(1990, 1, 1));
        update.setEmail("jane.doe@example.com");

        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        when(memberRepository.save(existing)).thenReturn(existing);

        Member result = memberService.update(id, update);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("jane.doe@example.com");
        verify(memberRepository).save(existing);
    }

    @Test
    void delete_shouldEvictAndDeleteMember() {
        UUID id = UUID.randomUUID();
        doNothing().when(memberRepository).deleteById(id);

        memberService.delete(id);

        verify(memberRepository).deleteById(id);
    }
}
