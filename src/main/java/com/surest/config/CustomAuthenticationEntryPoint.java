package com.surest.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
public class CustomAuthenticationEntryPoint {

  @Bean
  public AuthenticationEntryPoint unauthorizedHandler() {
    return (request, response, authException) -> {
      response.setContentType("application/json;charset=UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("{\"message\":\"Please log in to access1 this resource.\"}");
    };
  }
}
