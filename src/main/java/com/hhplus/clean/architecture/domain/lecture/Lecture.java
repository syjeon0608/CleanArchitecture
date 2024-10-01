package com.hhplus.clean.architecture.domain.lecture;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Lecture {

    @Id
    @GeneratedValue
    private Long id;

}
