package com.surest.entity;

import jakarta.persistence.*;
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
@Table(schema = "public", name = "role")
public class Role {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  private String name;
}
