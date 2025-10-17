package com.surest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateMemberApi() throws Exception {
        MemberDto dto = new MemberDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("abc@xyz.com");
        dto.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateMemberApi() throws Exception {
        MemberDto dto = new MemberDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("abc@xyz.com");
        dto.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(response).get("id").asText();

        dto = new MemberDto();
        dto.setFirstName("John1");
        dto.setLastName("Doe1");
        dto.setEmail("abc1@xyz.com");
        dto.setDateOfBirth(java.time.LocalDate.of(1991, 1, 1));
        json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/members/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John1"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteMemberApi() throws Exception {
        MemberDto dto = new MemberDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("abc@xyz.com");
        dto.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/members/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/members/{id}",id))
                .andExpect(status().isNotFound());


    }

}
