package com.clibo.domain.medical;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TestCatalog {

    @Id
    @GeneratedValue
    private Long id;

    private String testName;
    private double price;
    private String labRoom;
}
