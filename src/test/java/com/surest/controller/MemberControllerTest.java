package com.surest.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.config.JwtAuthenticationFilter;
import com.surest.dto.MemberDto;
import com.surest.dto.mapper.MemberMapper;
import com.surest.entity.Member;
import com.surest.service.MemberServiceImpl;
import com.surest.util.JwtHelper;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
// @Import(MemberControllerTest.TestConfig.class)
class MemberControllerTest {

  @Autowired private MockMvc mockMvc;

  //    @Autowired
  @MockitoBean private MemberServiceImpl memberService;
  @MockitoBean public JwtHelper jwtHelper;
  @Autowired private MemberMapper memberMapper;

  @Autowired private ObjectMapper objectMapper;

  @Mock JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void listMembers_returnsPage() throws Exception {
    Page<Member> page = new PageImpl<>(Collections.emptyList());
    when(memberService.findMembers(anyInt(), anyInt(), anyString(), any(), any())).thenReturn(page);

    mockMvc
        .perform(
            get("/members").param("page", "0").param("size", "10").param("sort", "lastName,asc"))
        .andExpect(status().isOk());

    verify(memberService, times(1)).findMembers(eq(0), eq(10), eq("lastName,asc"), any(), any());
  }

  @Test
  void getMember_returnsMember() throws Exception {
    UUID id = UUID.randomUUID();
    Member member = new Member();
    when(memberService.findById(id)).thenReturn(member);

    mockMvc.perform(get("/members/{id}", id)).andExpect(status().isOk());

    verify(memberService, times(1)).findById(id);
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void createMember_returnsCreated() throws Exception {
    MemberDto dto = new MemberDto();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("abc@xyz.com");
    dto.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));

    Member member = new Member();
    member.setFirstName("John");
    member.setLastName("Doe");
    member.setEmail("abc@xyz.com");
    member.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));

    String json = objectMapper.writeValueAsString(dto);

    when(memberMapper.toEntity(any(MemberDto.class))).thenReturn(member);
    when(memberService.create(any(Member.class))).thenReturn(member);

    mockMvc
        .perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());

    verify(memberService, times(1)).create(member);
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void updateMember_returnsOk() throws Exception {
    UUID id = UUID.randomUUID();
    MemberDto dto = new MemberDto();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("abc@xyz.com");
    dto.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
    Member member = new Member();
    String json = objectMapper.writeValueAsString(dto);

    when(memberMapper.toEntity(any(MemberDto.class))).thenReturn(member);
    when(memberService.update(eq(id), any(Member.class))).thenReturn(member);

    mockMvc
        .perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

    verify(memberMapper, times(1)).toEntity(any(MemberDto.class));
    verify(memberService, times(1)).update(eq(id), any(Member.class));
  }

  @Test
  void deleteMember_returnsNoContent() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(memberService).delete(id);

    mockMvc.perform(delete("/members/{id}", id)).andExpect(status().isNoContent());

    verify(memberService, times(1)).delete(id);
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    public MemberServiceImpl memberService() {
      return Mockito.mock(MemberServiceImpl.class);
    }

    @Bean
    public MemberMapper memberMapper() {
      return Mockito.mock(MemberMapper.class);
    }
  }
}
