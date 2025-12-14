package com.clibo.domain.profile;

import com.clibo.domain.profile.User;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class Staff extends User {

    protected double salary;
    protected String employmentStatus;
    protected LocalDate hiredDate;
    protected String workingSchedule;
}
