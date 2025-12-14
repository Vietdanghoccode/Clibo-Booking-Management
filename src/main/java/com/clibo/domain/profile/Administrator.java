package com.clibo.domain.profile;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends User {

    private String permissionScope;
    private String createdByAdmin;
}
