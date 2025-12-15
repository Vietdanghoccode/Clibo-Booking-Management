package com.clibo.domain.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue
    protected Long id;

    protected String fullName;
    protected String phone;
    protected String address;
    protected LocalDate dateOfBirth;
    protected String email;

    protected String identifyCitizen;

    @Enumerated(EnumType.STRING)
    protected Role role;

    @Enumerated(EnumType.STRING)
    protected Gender gender;

    protected String password;

    protected boolean isVerified;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return phone; // login bằng phone
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

}
