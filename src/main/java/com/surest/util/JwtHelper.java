package com.surest.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
  //// hs512
  // 5xt6rFvxMtOtWgpjFwbEiGqWCdIMnc6IRRr74HKtorUjqDDqrthar7DGsawxEAESSi1Eb5abW4TAFU5VKOGsAg==
  //// hs256 MU6ik0VpcZvp73d6P+pbFtk9Icczgko7rL2dyjMJ4+s=
  // String SECRET_KEY1 =
  // Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
  //     {
  //        System.out.println("=====>"+SECRET_KEY1);
  //    }
  private final SecretKey SECRET_KEY;

  public JwtHelper(@Value("${base64key}") String secretkey) {
    this.SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretkey));
  }

  private static final long validity = 5 * 60 * 60 * 1000; // 5h

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + validity))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  private boolean isTokenExpired(String token) {
    Date expiration =
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    return expiration.before(new Date());
  }
}
