package com.surest.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "public", name = "user")
public class User {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false, name = "password_hash")
  private String password;

  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles; // "ROLE_USER,ROLE_ADMIN"
  // getters and setters...
}
