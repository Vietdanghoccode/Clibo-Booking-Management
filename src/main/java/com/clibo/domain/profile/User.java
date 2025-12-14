package com.clibo.domain.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class User {

    @Id
    @GeneratedValue
    protected Long id;

    protected String fullName;
    protected String phone;
    protected String address;
    protected LocalDate dateOfBirth;
    protected String email;

    @Enumerated(EnumType.STRING)
    protected Gender gender;

    protected String password;

}
