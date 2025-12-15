package com.clibo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String fullName;
    private String phone;
    private boolean verified;
    private List<String> permissions;

    public LoginResponse(Long userId, String fullName, String phone, boolean verified) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.verified = verified;
        // Default permissions for authenticated patients
        this.permissions = List.of("READ", "WRITE", "EDIT");
    }
}
