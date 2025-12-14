package com.clibo.domain.profile;

import com.clibo.domain.appointment.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Doctor extends Staff {

    private String specialty;
    private String licenseNumber;
    private String roomNumber;

    @ManyToOne
    private Department department;

    public boolean canPerformTest() {
        return true;
    }
}
