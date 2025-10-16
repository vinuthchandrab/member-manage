package com.surest.controller;

import com.surest.dto.JwtRequest;
import com.surest.dto.JwtResponse;
import com.surest.util.JwtHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authManager;
  private final JwtHelper jwtHelper;

  public AuthController(AuthenticationManager authManager, JwtHelper jwtHelper) {
    this.authManager = authManager;
    this.jwtHelper = jwtHelper;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
    Authentication authentication =
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtHelper.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
  }
}
