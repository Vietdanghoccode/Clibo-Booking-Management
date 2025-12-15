package com.clibo.domain.profile;

import com.clibo.domain.profile.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Patient extends User {

    private boolean verified;
    private String bankInfo;
}
