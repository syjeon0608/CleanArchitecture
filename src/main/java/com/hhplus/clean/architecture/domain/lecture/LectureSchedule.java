package com.hhplus.clean.architecture.domain.lecture;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LectureSchedule {

    @Id
    @GeneratedValue
    private Long id;

}
