package com.surest.dto; // MemberDto.java

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public class MemberDto {

  private UUID id;

  @NotBlank
  @Size(max = 100)
  private String firstName;

  @NotBlank
  @Size(max = 100)
  private String lastName;

  @NotNull private LocalDate dateOfBirth;

  @NotBlank
  @Email
  @Size(max = 255)
  private String email;

  // Getters and setters

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
