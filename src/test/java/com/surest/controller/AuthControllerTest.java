package com.surest.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surest.dto.JwtRequest;
import com.surest.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean AuthenticationManager authManager;
  @MockitoBean public JwtHelper jwtHelper;
  @MockitoBean
  public Authentication authentication;
  @Autowired
  private ObjectMapper objectMapper;
  @Mock
  UserDetails userDetails;

  @Test
  void postUserLoginDetails_returnsToken() throws Exception {
    JwtRequest request = new JwtRequest("testuser", "testpassword");
    String json = objectMapper.writeValueAsString(request);

    when(authManager.authenticate( any())).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("testuser");
    when(userDetails.getPassword()).thenReturn("testpassword");
    mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    verify(jwtHelper,times(1)).generateToken(userDetails);
  }

}
